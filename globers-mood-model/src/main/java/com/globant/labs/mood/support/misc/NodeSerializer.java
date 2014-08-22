package com.globant.labs.mood.support.misc;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.*;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public class NodeSerializer extends JsonSerializer<NodeImpl> {

    @Override
    public void serialize(NodeImpl value, JsonGenerator generator, SerializerProvider provider) throws IOException, JsonProcessingException {
        writeNodeImpl(generator, value);
    }

    /**
     * @param generator
     * @param node
     * @throws IOException
     */
    public void writeNodeImpl(JsonGenerator generator, NodeImpl node) throws IOException {
        generator.writeStartObject();
        generator.writeFieldName(node.getKey());

        if (node.getValue() == null) {
            generator.writeNull();

        } else if (node.getValue().getClass().isAssignableFrom(ArrayList.class)) {
            final Map<String, Object> map = getMap(List.class.cast(node.getValue()));
            generator.writeObject(map);

        } else if (node.getValue().getClass().isAssignableFrom(NodeImpl.class)) {
            final NodeImpl nodeImpl = NodeImpl.class.cast(node.getValue());
            generator.writeObject(getMap(nodeImpl));

        } else {
            generator.writeObject(node.getValue());
        }

        generator.writeEndObject();
    }

    /**
     * @param node
     * @return
     */
    private Map<String, Object> getMap(final NodeImpl node) {
        final NodeImpl[] nodes = {node};
        return getMap(Arrays.asList(nodes));
    }

    /**
     * @param nodes
     * @return
     */
    private Map<String, Object> getMap(final List<NodeImpl> nodes) {
        final Map<String, Object> map = new HashMap<String, Object>();
        for (final NodeImpl currentNode : nodes) {
            if (currentNode.getValue().getClass().isAssignableFrom(ArrayList.class)) {
                map.put(currentNode.getKey(), getMap(List.class.cast(currentNode.getValue())));
            } else {
                map.put(currentNode.getKey(), currentNode.getValue());
            }
        }
        return map;
    }
}