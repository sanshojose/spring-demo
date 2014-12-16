package com.rogers.sample.cache;

import play.Logger;

import java.util.Date;

public class CachedResult<T> {
    private static final Logger.ALogger logger = Logger.of(CachedResult.class);

    private final Date cachedDate, cacheExpiryDate;
    private final T result;

    public CachedResult(T result, Date cachedDate, Date cacheExpiryDate) {
        this.cachedDate = cachedDate;
        this.cacheExpiryDate = cacheExpiryDate;
        this.result = result;
    }

    public T get() {
        return result;
    }

    public Date getCachedDate() {
        return cachedDate;
    }

    public Date getCacheExpiryDate() {
        return cacheExpiryDate;
    }

    public boolean isExpired(){
        if(cachedDate == null || getCacheExpiryDate() == null){
            logger.warn("Unexpected state (did something change?) - The caching actor created a wrapped CachedResult without any dates.\n\t");
            return true;
        } else
            return new Date().after(cacheExpiryDate);
    }
}
