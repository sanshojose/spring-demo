package com.rogers.sample.profile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.rogers.sample.cache.RequestCachableMessage;
import play.data.validation.Constraints;

import java.util.List;

public class SelfRegistrationRequestMessage extends RequestCachableMessage{

    //@Constraints.Required
    private String name;
    @Constraints.Email
    @Constraints.Required
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCacheKey() {
        return name;
    }

    public String getCacheTable() {
        return "accounts";
    }

    public TypeReference getResultTypeReference() {
        return new TypeReference<JsonNode>(){};
    }
}
