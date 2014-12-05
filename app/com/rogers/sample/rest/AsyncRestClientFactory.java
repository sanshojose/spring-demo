package com.rogers.sample.rest;

import com.rogers.sample.AppConfig;
import com.rogers.sample.profile.SelfRegistrationRequestMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import play.Logger;

/**
 * Creates selfRegistrationRestClient
 */

@Component
public class AsyncRestClientFactory {

    private static final Logger.ALogger logger = Logger.of(AsyncRestClientFactory.class);
    private final AppConfig config;

    @Autowired
    public AsyncRestClientFactory(AppConfig config){
        logger.info("AsyncRestClientFactory.AsyncRestClientFactory()::Instantiating");
        this.config = config;
    }

    @Bean(name="selfRegistrationRestClient")
    public AsyncRestClient getSelfRegistrationRestClient() {
        logger.info("Invoking AsyncRestClientFactory.getSelfRegistrationRestClient()");
        return new AsyncRestClient<SelfRegistrationRequestMessage>(config.getConfig().getString("rest.selfRegistration.endpoint"));
    }
}
