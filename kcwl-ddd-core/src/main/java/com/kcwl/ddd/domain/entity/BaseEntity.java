package com.kcwl.ddd.domain.entity;

public class BaseEntity<T> {
    /**
     * Entities compare by identity, not by attributes.
     *
     * @param other The other entity.
     * @return true if the identities are the same, regardless of other attributes.
     */
    boolean sameIdentityAs(T other){
        return  (other != null) && (this.equals(other));
    }
}
