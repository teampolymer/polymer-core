package com.nmmoc7.polymercore.api.util;

public interface IAttributeProvider {
    <T> void setAttribute(AttributeKey<T> key, T value);

    <T> T getAttribute(AttributeKey<T> key);
}
