package com.globant.labs.mood.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.appengine.api.search.checkers.Preconditions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@JsonSerialize(using = NodeSerializer.class)
public class NodeImpl implements Node, Serializable {

    private String key;
    private Object value;

    private NodeImpl parent;
    private List<NodeImpl> childs;

    /**
     *
     * @param key
     */
    public NodeImpl(final String key) {
        this.key = key;
        this.value = null;
        this.parent = null;
        this.childs = new ArrayList<NodeImpl>();
    }

    /**
     *
     * @return
     */
    public String getKey() {
        return key;
    }

    /**
     *
     * @return
     */
    public Object getValue() {
        return value;
    }

    /**
     *
     * @return
     */
    public List<NodeImpl> getChilds() {
        return childs;
    }

    /**
     *
     * @return
     */
    public NodeImpl getParent() {
        return parent;
    }

    /**
     *
     * @param measure
     * @return
     */
    @Override
    public Node path(final Measure measure) {
        return path(measure.name());
    }

    /**
     *
     * @param name
     * @return
     */
    @Override
    public Node path(final String name) {
        final NodeImpl path = new NodeImpl(name);

        path.parent = this;
        this.childs.add(path);
        this.value = path;

        if (childs.size() > 0) {
            this.value = this.childs;
        }

        return path;
    }

    /**
     *
     * @param value
     * @return
     */
    @Override
    public Node value(final Object value) {
        this.value = value;
        NodeImpl ref = this;
        while (ref.parent != null) {
            ref = ref.parent;
        }
        return ref;
    }

    @Override
    public Node get(final String name) {
        Preconditions.checkNotNull(name, "name cannot be null");

        // = Backwards search.
        NodeImpl root = this;
        while (root.parent != null) {
            if (root.getKey().equals(name)) {
                return root;
            }
            root = root.parent;
        }

        // = Forward search.
        while(root.getValue() != null) {
            if (root.getKey().equals(name)) {
                return root;
            }

            if (root.getValue().getClass().isAssignableFrom(NodeImpl.class)) {
                root = NodeImpl.class.cast(root.getValue());
            } else if (root.getValue().getClass().isAssignableFrom(ArrayList.class)) {
                return getMatchingNode(root.getChilds(), name);
            };
        }
        return null;
    }

    /**
     *
     * @param nodes
     * @param name
     * @return
     */
    private Node getMatchingNode(final List<NodeImpl> nodes, final String name) {
        if (nodes == null || (nodes != null && nodes.isEmpty())) {
            return null;
        }

        for (final NodeImpl current : nodes) {
            if (current.getKey().equals(name)) {
                return current;
            }
            getMatchingNode(current.getChilds(), name);
        }
        return null;
    }

}
