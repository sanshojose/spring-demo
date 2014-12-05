package com.rogers.sample.profile;

import akka.actor.ActorRef;
import akka.actor.ActorRefFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import play.Logger;

import java.util.UUID;

@Configuration
public class ProfileServiceActorFactory {

    private static final Logger.ALogger logger = Logger.of(ProfileServiceActorFactory.class);
    /**
     * Creates the regServiceActor in spring context.
     * @param profileRegistrationRestClient
     * @param context
     * @return ActorRef
     */
    @Bean(name="regServiceActor")
    public static ActorRef create(
            @Qualifier("selfRegistrationRestClient") com.rogers.sample.rest.AsyncRestClient profileRegistrationRestClient,
            ActorRefFactory context){
        logger.info("Inside ProfileServiceActorFactory.create():: creates the ProfileServiceActor");
        return context.actorOf(ProfileServiceActor.props(profileRegistrationRestClient), "regService" + UUID.randomUUID());
    }
}