package com.globant.labs.mood.support.misc;

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
