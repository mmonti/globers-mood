package com.globant.labs.mood.model;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public class ContextFactory {

    /**
     *
     * @return
     */
    public static Context getInstance() {
        return new MapBackedContext();
    }

}
