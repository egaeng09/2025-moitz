package com.f12.moitz.infrastructure.client.open.dto.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class YnToBooleanConverter extends JsonDeserializer<Boolean> {

    @Override
    public Boolean deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
        final String value = p.getText();
        return "Y".equalsIgnoreCase(value);
    }

}
