package com.globant.labs.mood.service.cache;

import java.io.Serializable;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public interface CacheService {

    /**
     *
     * @param key
     * @param returningObject
     * @param <T>
     * @return
     */
    <T> T get(final Serializable key, final Class<T> returningObject);

    /**
     *
     * @param key
     * @param value
     */
    void store(final Serializable key, final Object value);

    /**
     *
     * @param key
     * @param value
     */
    void storeIfAbsent(final Serializable key, final Object value);
}
