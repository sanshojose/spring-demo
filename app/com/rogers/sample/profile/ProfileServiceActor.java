package com.rogers.sample.profile;

import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;
import play.libs.F;
import play.libs.ws.*;

import scala.concurrent.Future;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorRefFactory;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;
import akka.pattern.Patterns;

public class ProfileServiceActor extends AbstractActor{

    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    private final com.rogers.sample.rest.AsyncRestClient<SelfRegistrationRequestMessage> selfRegistrationClient;

    public static ActorRef create(com.rogers.sample.rest.AsyncRestClient selfRegistrationClient, ActorRefFactory context) {
        return context.actorOf(ProfileServiceActor.props(selfRegistrationClient), "regService" + UUID.randomUUID().toString());
    }

    public static Props props(com.rogers.sample.rest.AsyncRestClient selfRegistrationClient) {
        return Props.create(ProfileServiceActor.class, selfRegistrationClient);
    }

    private ProfileServiceActor(com.rogers.sample.rest.AsyncRestClient<SelfRegistrationRequestMessage> selfRegistrationClient) {
        this.selfRegistrationClient = selfRegistrationClient;

        receive(ReceiveBuilder.match(SelfRegistrationRequestMessage.class, message -> {Future<JsonNode> future = selfRegistration(message);
                                                                                       Patterns.pipe(future, context().dispatcher()).to(sender());
        }).matchAny(message ->log.warning("Unexpected message type - registration service actor ignoring message: " + message.getClass() + ": " + message.toString())).build());
    }

    private Future<JsonNode> selfRegistration(SelfRegistrationRequestMessage request) {
    	 F.Promise<JsonNode> promise = selfRegistrationClient.makeRequest(request);
         return promise.wrapped();
	}

}
