package com.teampolymer.polymer.core.api.util;

public interface IAttributeProvider {
    <T> void setAttribute(AttributeKey<T> key, T value);

    <T> T getAttribute(AttributeKey<T> key);
}
