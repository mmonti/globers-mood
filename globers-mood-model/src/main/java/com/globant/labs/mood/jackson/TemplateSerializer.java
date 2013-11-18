package com.globant.labs.mood.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.globant.labs.mood.model.persistent.Template;

import java.io.IOException;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public class TemplateSerializer extends JsonSerializer<Template> {

    public static final String ID_FIELD = "id";
    public static final String NAME_FIELD = "name";
    public static final String DESCRIPTION_FIELD = "description";

    @Override
    public void serialize(final Template template, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField(ID_FIELD, template.getId());
        jsonGenerator.writeStringField(NAME_FIELD, template.getName());
        jsonGenerator.writeStringField(DESCRIPTION_FIELD, template.getDescription());
        jsonGenerator.writeEndObject();
    }
}
