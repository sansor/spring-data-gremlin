/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.spring.data.gremlin.conversion.script;

import com.microsoft.spring.data.gremlin.common.Constants;
import com.microsoft.spring.data.gremlin.exception.GremlinUnexpectedEntityTypeException;
import org.apache.tinkerpop.shaded.jackson.core.JsonProcessingException;
import org.apache.tinkerpop.shaded.jackson.databind.MapperFeature;
import org.apache.tinkerpop.shaded.jackson.databind.ObjectMapper;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class AbstractGremlinScriptLiteral {

    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.configure(MapperFeature.AUTO_DETECT_FIELDS, false);
    }

    private static String generateProperty(@NonNull String name, @NonNull String value) {
        return String.format(Constants.GREMLIN_PRIMITIVE_PROPERTY_STRING, name, value);
    }

    private static String generateProperty(@NonNull String name, @NonNull Integer value) {
        return String.format(Constants.GREMLIN_PRIMITIVE_PROPERTY_NUMBER, name, value);
    }

    private static String generateProperty(@NonNull String name, @NonNull Boolean value) {
        return String.format(Constants.GREMLIN_PRIMITIVE_PROPERTY_BOOLEAN, name, value);
    }

    private static String generateProperty(@NonNull String name, @NonNull Object value) {

        if (value instanceof Integer) {
            return generateProperty(name, (Integer) value);
        } else if (value instanceof Boolean) {
            return generateProperty(name, (Boolean) value);
        } else if (value instanceof String) {
            return generateProperty(name, (String) value);
        } else {
            final String propertyScript;

            try {
                propertyScript = generateProperty(name, mapper.writeValueAsString(value));
            } catch (JsonProcessingException e) {
                throw new GremlinUnexpectedEntityTypeException("Failed to write object to String", e);
            }

            return propertyScript;
        }
    }

    public static List<String> generateProperties(@NonNull final Map<String, Object> properties) {
        final List<String> scripts = new ArrayList<>();

        properties.forEach((name, value) -> scripts.add(generateProperty(name, value)));

        return scripts;
    }

    private static String generateHas(@NonNull String name, @NonNull Integer value) {
        return String.format(Constants.GREMLIN_PRIMITIVE_HAS_NUMBER, name, value);
    }

    private static String generateHas(@NonNull String name, @NonNull Boolean value) {
        return String.format(Constants.GREMLIN_PRIMITIVE_HAS_BOOLEAN, name, value);
    }

    private static String generateHas(@NonNull String name, @NonNull String value) {
        return String.format(Constants.GREMLIN_PRIMITIVE_HAS_STRING, name, value);
    }

    public static String generateHas(@NonNull String name, @NonNull Object value) {

        if (value instanceof Integer) {
            return generateHas(name, (Integer) value);
        } else if (value instanceof Boolean) {
            return generateHas(name, (Boolean) value);
        } else if (value instanceof String) {
            return generateHas(name, (String) value);
        } else {
            final String hasScript;

            try {
                hasScript = generateHas(name, mapper.writeValueAsString(value));
            } catch (JsonProcessingException e) {
                throw new GremlinUnexpectedEntityTypeException("Failed to write object to String", e);
            }

            return hasScript;
        }
    }
}