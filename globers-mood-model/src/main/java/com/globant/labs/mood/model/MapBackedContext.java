package com.globant.labs.mood.model;

import com.google.common.base.Preconditions;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public class MapBackedContext extends HashMap implements Context {

    public void add(final String key, final Object value) {
        Preconditions.checkNotNull(key, "key is null");
        this.put(key, value);
    }

    public Object get(final String key) {
        Preconditions.checkNotNull(key, "key is null");
        if (!this.containsKey(key)) {
        }
        return this.get(key);
    }

}
