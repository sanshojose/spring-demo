package com.rogers.sample.cache;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Creator;
import akka.japi.pf.ReceiveBuilder;
import akka.pattern.Patterns;
import akka.util.Timeout;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSetFuture;
import com.datastax.driver.core.Row;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rogers.util.Iso8601DateUtil;
import play.libs.F;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.Date;
import java.util.List;

/**
 * Caching actor sits infront of any other service to place the item into the cache.
 */

public class CachingActor extends AbstractActor {
    private final String VALUE_ROW = "json";
    private final String DATE_ADDED_ROW = "added";
    private final String DATE_EXPIRES_ROW= "expires";

    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    private static final Timeout timeout = new Timeout(Duration.create(15, "seconds"));
    private final ObjectMapper mapper = new ObjectMapper();
    private final Cluster cluster;
    private final String keyspace;

    private final ActorRef serviceActor;

    public static Props props(final List<String> cassandraContactPoints, final ActorRef serviceActor, String keyspace) {
        return Props.create(new Creator<CachingActor>() {
            private static final long serialVersionUID = 1L;

            @Override
            public CachingActor create() throws Exception {
                return new CachingActor(cassandraContactPoints, serviceActor, keyspace);
            }
        });

    }

    /**
     * @param cassandraContactPoints
     * @param serviceActor
     * @param keyspace
     */

    private CachingActor(List<String> cassandraContactPoints, ActorRef serviceActor, String keyspace) {
        this.serviceActor = serviceActor;
        this.keyspace = keyspace;

        Cluster.Builder builder = Cluster.builder();
        for (String cassandraContactPoint : cassandraContactPoints) {
            builder.addContactPoint(cassandraContactPoint);
        }

        cluster = builder.build();

        receive(ReceiveBuilder
                        .match(RequestCachableMessage.class, message -> getCachable(message))
                        .matchAny(message ->
                                log.warning("[CACHEACTOR] [?] Unexpected message type - Cache actor ignoring message: " + message.getClass() + ": " + message.toString()))
                        .build()
        );
    }

    private void getCachable(RequestCachableMessage message) throws JsonProcessingException {
        final F.RedeemablePromise promise = F.RedeemablePromise.empty();

        ResultSetFuture rs = queryCache(message);
        rs.addListener(() -> {
            try {
                log.info("[CACHEACTOR] [" + message.getRequestId() + "] Cassandra returned " + rs.get());
                processCacheResult(message, promise, message.getResultTypeReference(), rs);
            } catch (Exception e) {
                log.warning("[CACHEACTOR] [" + message.getRequestId() + "] Not able to retrieve record from cache due to exception. Trying service actor\nExecption: " + e.getClass().getName()+" "+ e.getMessage(), e);
                Future future = Patterns.ask(serviceActor, message, timeout);

                F.Promise.wrap(future)
                        .map((result) -> {
                            promise.success(result);
                            updateCache(message, result);
                            return null;
                        }).onFailure((ex) -> {
                    log.error("[CACHEACTOR] [" + message.getRequestId() + "] Encountered an error retrieving record.", e);
                    promise.failure((Throwable)ex);
                });
            }
        }, context().dispatcher());

        Patterns.pipe(promise.wrapped(), context().dispatcher()).to(sender());
    }

    private ResultSetFuture queryCache(RequestCachableMessage message) {
        String selectStatement = "SELECT * FROM "
                + message.getCacheTable() + " where key='"
                + message.getCacheKey() + "';";

        log.info("[CACHEACTOR] [" + message.getRequestId() + "] Querying database for cache: " + selectStatement);

        return cluster.connect(keyspace).executeAsync(selectStatement);
    }

    private void processCacheResult(RequestCachableMessage message, F.RedeemablePromise promise, TypeReference tr, ResultSetFuture rs) throws Exception {
        List<Row> rows = rs.get().all();
        if (rows.size() > 0) { //asserts we are searching on distinct pk.

            Row row = rows.get(0);
            String resultString = row.getString(VALUE_ROW);
            log.info("[CACHEACTOR] [" + message.getRequestId() + "] data received from cache: " + resultString);

            Object result = mapper.readValue(resultString.getBytes(), tr);
            log.info("[CACHEACTOR] [" + message.getRequestId() + "] deserialized result from cache successfully");

            Date addedToCache = row.getDate(DATE_ADDED_ROW);
            Date expires = row.getDate(DATE_EXPIRES_ROW);
            CachedResult wrappedResult =    new CachedResult(result, addedToCache, expires);

            if(wrappedResult.isExpired()) {
                throw new Exception("cache expired");
            }

            if(message.shouldReturnWrappedResult()){
                promise.success(wrappedResult);
            }else{
                promise.success(result);
            }
        } else {
            throw new Exception("Didn't find a record");
        }
    }


    private void updateCache(final RequestCachableMessage message, Object result) throws JsonProcessingException {
        String insertStatement = "INSERT INTO " + message.getCacheTable() + " (key, json, added, expires) VALUES ('"
                + message.getCacheKey() + "','"
                + mapper.writeValueAsString(result)
                + "','" + Iso8601DateUtil.currentDateToIso8601()
                + "','" + Iso8601DateUtil.currentDatePlusMillisIso8601(60000L) //expire in 1 minute for now TODO move to config
                + "');";

        log.info("[CACHEACTOR] [" + message.getRequestId() + "] Updating cache: " + insertStatement);
        ResultSetFuture insertResultFuture = cluster.connect(keyspace).executeAsync(insertStatement);
        insertResultFuture.addListener(() -> {
            try {
                log.info("[CACHEACTOR] [" + message.getRequestId() + "] Cache updated - " + insertResultFuture.get());
            } catch (Exception e) {
                log.error("[CACHEACTOR] [" + message.getRequestId() + "Encountered issue inserting record ", e);
            }
        }, context().dispatcher());
    }
}
