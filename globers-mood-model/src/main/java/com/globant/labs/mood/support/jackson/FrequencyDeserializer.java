package com.globant.labs.mood.support.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.globant.labs.mood.model.persistent.Frequency;

import java.io.IOException;

/**
 * Created by mmonti on 8/11/14.
 */
public class FrequencyDeserializer extends JsonDeserializer<Frequency> {

    @Override
    public Frequency deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        return Frequency.valueOf(jsonParser.getValueAsString());
    }

}
