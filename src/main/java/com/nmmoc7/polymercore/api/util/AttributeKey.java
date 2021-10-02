package com.nmmoc7.polymercore.api.util;

/**
 * 表示一个属性的Key
 *
 * @param <T>
 */
public class AttributeKey<T> {
    private final Class<T> valueClass;
    private final String attributeName;

    private AttributeKey(Class<T> valueClass, String attributeName) {
        this.valueClass = valueClass;
        this.attributeName = attributeName;
    }


    /**
     * 创建一个属性Key
     *
     * @param valueClass 属性的类型，仅用于变量类型转换
     * @param name       属性实际存储的key，注意不要冲突
     */
    public static <T> AttributeKey<T> create(Class<T> valueClass, String name) {
        return new AttributeKey<>(valueClass, name);
    }

    public T cast(Object o) {
        return valueClass.cast(o);
    }

    public boolean isInstance(Object o) {
        return valueClass.isInstance(o);
    }

    public Class<T> getValueClass() {
        return valueClass;
    }

    public String getAttributeName() {
        return attributeName;
    }
}
