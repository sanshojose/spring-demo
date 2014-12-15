package com.rogers.sample.profile;

import play.data.validation.Constraints;

public class SelfRegistrationRequestMessage {

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
}
