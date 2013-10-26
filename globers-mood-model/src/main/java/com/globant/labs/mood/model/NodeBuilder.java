package com.globant.labs.mood.model;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public class NodeBuilder {

    /**
     *
     * @param name
     * @return
     */
    public static Node create(final String name) {
        return new NodeImpl(name);
    }

}
