package com.globant.labs.mood.model;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public interface Context {

    /**
     *
     * @param key
     * @param value
     */
    void add(final String key, final Object value);

    /**
     *
     * @param key
     * @return
     */
    Object get(final String key);
}
