package com.rogers.sample.cache;

import com.fasterxml.jackson.core.type.TypeReference;
import com.rogers.sample.ServiceRequestMessage;

/**
* Implement this interface to allow your model to be cached.
*/
public abstract class RequestCachableMessage extends ServiceRequestMessage {

    protected Boolean returnWrappedResult = false;

    abstract public String getCacheKey();
    abstract public String getCacheTable();
    abstract public TypeReference getResultTypeReference();

    /**
     * Setting this to true will bundle the result in a wrapper that has expiry.
     * The result will be returned in a CachedResult - CachedResult.get()
     * Otherwise, if false, the result will be returned directly.
     * @return
     */
    public Boolean shouldReturnWrappedResult(){

        return returnWrappedResult;
    }

    public void setShouldReturnWrappedResult(Boolean bool){
        returnWrappedResult = bool;
    }
}
