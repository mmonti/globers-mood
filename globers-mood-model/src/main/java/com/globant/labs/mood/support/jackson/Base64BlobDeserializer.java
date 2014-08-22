package com.globant.labs.mood.support.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.appengine.api.datastore.Blob;
import com.google.common.io.BaseEncoding;

import java.io.IOException;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public class Base64BlobDeserializer extends JsonDeserializer<Blob> {

    @Override
    public Blob deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        final ObjectCodec objectCodec = jsonParser.getCodec();
        final JsonNode node = objectCodec.readTree(jsonParser);
        final String content = node.asText();
        return new Blob(BaseEncoding.base64().decode(content));
    }
}
