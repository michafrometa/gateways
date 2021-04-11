package com.mice.gateways.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;

public final class TestUtil {

    private static final ObjectMapper mapper = createObjectMapper();

    public static ObjectMapper createObjectMapper() {
        return new ObjectMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
                .registerModule(new JavaTimeModule());
    }

    /**
     * Convert an object to JSON byte array.
     *
     * @param object the object to convert.
     * @return the JSON byte array.
     * @throws IOException throws JsonProcessingException exception
     */
    public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
        return mapper.writeValueAsBytes(object);
    }
}
