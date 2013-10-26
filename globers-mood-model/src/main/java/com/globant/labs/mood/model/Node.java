package com.globant.labs.mood.model;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public interface Node {

    /**
     *
     * @param name
     * @return
     */
    Node path(final String name);

    /**
     *
     * @param measure
     * @return
     */
    Node path(final Measure measure);

    /**
     *
     * @param value
     * @return
     */
    Node value(final Object value);

    /**
     *
     * @param name
     * @return
     */
    Node get(final String name);

}
