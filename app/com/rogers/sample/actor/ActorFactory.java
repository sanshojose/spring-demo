package com.rogers.sample.actor;

import akka.actor.ActorRef;
import akka.actor.ActorRefFactory;
import com.rogers.sample.AppConfig;
import com.rogers.sample.cache.CachingActor;
import com.rogers.sample.profile.ProfileServiceActor;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import play.Logger;

import java.util.UUID;

@Configuration
public class ActorFactory {
    private static final Logger.ALogger logger = Logger.of(ActorFactory.class);

    /**
     * Creates the regServiceActor in spring context.
     * @param profileRegistrationRestClient
     * @param context
     * @return ActorRef
     */
    @Bean(name="regServiceActor")
    public static ActorRef createRegServiceActor(
            @Qualifier("appConfig") AppConfig appConfig,
            @Qualifier("selfRegistrationRestClient") com.rogers.sample.rest.AsyncRestClient profileRegistrationRestClient,
            ActorRefFactory context){
        logger.info("Inside ActorFactory.createRegServiceActor():: creates the ProfileServiceActor");
        ActorRef bareActor = context.actorOf(ProfileServiceActor.props(profileRegistrationRestClient), "regService" + UUID.randomUUID());
        return wrapActorInCacheIfCachingOn(appConfig, bareActor, context);
    }

    /**
     *
     * @param appConfig
     * @param bareActor
     * @param context
     * @return
     */

    public static ActorRef wrapActorInCacheIfCachingOn(AppConfig appConfig, ActorRef bareActor, ActorRefFactory context){
        if(appConfig.getConfig().getBoolean("persistence.cache.enabled") == true)
            return context.actorOf(CachingActor.props(
                    appConfig.getConfig().getStringList("persistence.cassandra.endpoints"),
                    bareActor, appConfig.getConfig().getString("persistence.cassandra.keyspace")));
        else
            return bareActor;
    }

}
