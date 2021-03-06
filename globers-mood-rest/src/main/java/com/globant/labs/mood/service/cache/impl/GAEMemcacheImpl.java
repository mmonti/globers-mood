package com.globant.labs.mood.service.cache.impl;

import com.globant.labs.mood.service.cache.CacheService;
import com.google.appengine.api.memcache.MemcacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.Serializable;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@Component
public class GAEMemcacheImpl implements CacheService {

    private static final Logger logger = LoggerFactory.getLogger(GAEMemcacheImpl.class);

    private Object lock = new Object();
    private MemcacheService memcacheService;

    /**
     * @param memcacheService
     */
    @Inject
    public GAEMemcacheImpl(final MemcacheService memcacheService) {
        this.memcacheService = memcacheService;
    }

    /**
     * @param key
     * @param returningObject
     * @param <T>
     * @return
     */
    public <T> T get(final Serializable key, final Class<T> returningObject) {
        final Object object = memcacheService.get(key);
        return (T) (object);
    }

    /**
     * @param key
     * @param value
     */
    public void store(final Serializable key, final Object value) {
        synchronized (lock) {
            memcacheService.put(key, value);
        }
    }

    /**
     * @param key
     * @param value
     */
    public void storeIfAbsent(final Serializable key, final Object value) {
        final Object result = get(key, Object.class);
        if (result == null) {
            store(key, value);
        }
    }

}
