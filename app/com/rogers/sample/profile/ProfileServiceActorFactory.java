package com.rogers.sample.profile;

import akka.actor.ActorRef;
import akka.actor.ActorRefFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class ProfileServiceActorFactory {

    /**
     * Creates the idmServiceActor in spring context.
     * @param profileRegistrationRestClient
     * @param context
     * @return
     */
    @Bean(name="regServiceActor")
    public static ActorRef create(
            @Qualifier("selfRegistrationRestClient") com.rogers.sample.rest.AsyncRestClient profileRegistrationRestClient,
            ActorRefFactory context){
        return context.actorOf(ProfileServiceActor.props(profileRegistrationRestClient), "regService" + UUID.randomUUID());
    }
}