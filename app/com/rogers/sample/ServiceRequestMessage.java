package com.rogers.sample;

/**
 * Base class for requests to go through the actor system.
 */

public abstract class ServiceRequestMessage {
    private String requestId;
    public String getRequestId(){
        return requestId;
    }
    public String setRequestId(){
        return requestId;
    }
}
