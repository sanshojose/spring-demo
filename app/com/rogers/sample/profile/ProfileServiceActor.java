package com.rogers.sample.profile;

import com.fasterxml.jackson.databind.JsonNode;
import play.Logger;
import play.libs.F;

import scala.concurrent.Future;
import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;
import akka.pattern.Patterns;

/**
 * ProfileServiceActor will be handling the request-response asynchronously
 */
public class ProfileServiceActor extends AbstractActor{

    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    private static final Logger.ALogger logger = Logger.of(ProfileServiceActor.class);

    private final com.rogers.sample.rest.AsyncRestClient<SelfRegistrationRequestMessage> selfRegistrationClient;

    public static Props props(com.rogers.sample.rest.AsyncRestClient selfRegistrationClient) {
        return Props.create(ProfileServiceActor.class, selfRegistrationClient);
    }

    private ProfileServiceActor(com.rogers.sample.rest.AsyncRestClient<SelfRegistrationRequestMessage> selfRegistrationClient) {
        this.selfRegistrationClient = selfRegistrationClient;
        logger.info("ProfileServiceActor.ProfileServiceActor():: Waiting for messages");
        receive(ReceiveBuilder.match(SelfRegistrationRequestMessage.class, message -> {Future<JsonNode> future = selfRegistration(message);
                                                                                       Patterns.pipe(future, context().dispatcher()).to(sender());
        }).matchAny(message ->log.warning("Unexpected message type - registration service actor ignoring message: " + message.getClass() + ": " + message.toString())).build());
    }

    private Future<JsonNode> selfRegistration(SelfRegistrationRequestMessage request) {
         logger.info("ProfileServiceActor.selfRegistration()::About to call selfRegistrationClient.makeRequest()");
    	 F.Promise<JsonNode> promise = selfRegistrationClient.makeRequest(request);
         return promise.wrapped();
	}

}
