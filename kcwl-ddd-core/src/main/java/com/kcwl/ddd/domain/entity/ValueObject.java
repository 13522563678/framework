package com.kcwl.ddd.domain.entity;

/**
 * 值对象标记接口
 * @param <T>
 */
public interface ValueObject<T> extends MarkerInterface {

    /**
     * Value objects compare by the values of their attributes, they don't have an identity.
     *
     * @param other The other value object.
     * @return <code>true</code> if the given value object's and this value object's attributes are the same.
     */
    boolean sameValueAs(T other);
}
