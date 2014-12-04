package com.rogers.sample.rest;

import com.rogers.sample.AppConfig;
import com.rogers.sample.profile.SelfRegistrationRequestMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 *
 */

@Component
public class AsyncRestClientFactory {

    private final AppConfig config;


    @Autowired
    public AsyncRestClientFactory(AppConfig config){

        this.config = config;
    }

    @Bean(name="selfRegistrationRestClient")
    public AsyncRestClient getSelfRegistrationRestClient() {
        return new AsyncRestClient<SelfRegistrationRequestMessage>(config.getConfig().getString("rest.selfRegistration.endpoint"));
    }
}
