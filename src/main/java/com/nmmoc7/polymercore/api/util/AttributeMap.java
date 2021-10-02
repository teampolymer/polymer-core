package com.nmmoc7.polymercore.api.util;

import java.util.HashMap;

public class AttributeMap implements IAttributeProvider {
    private final HashMap<String, Object> valueMap = new HashMap<>();

    @Override
    public <T> void setAttribute(AttributeKey<T> key, T value) {
        valueMap.put(key.getAttributeName(), value);
    }

    @Override
    public <T> T getAttribute(AttributeKey<T> key) {
        Object value = valueMap.get(key.getAttributeName());
        if (key.isInstance(value)) {
            return key.cast(value);
        }
        return null;
    }
}
