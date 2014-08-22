package com.globant.labs.mood.support.misc;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public interface Node {

    /**
     * @param name
     * @return
     */
    Node path(final String name);

    /**
     * @param value
     * @return
     */
    Node value(final Object value);

    /**
     * @param name
     * @return
     */
    Node get(final String name);

}
