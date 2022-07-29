package com.kcwl.ddd.domain.entity;

public class BaseEntity<T> {

    Long id;

    /**
     * Entities compare by identity, not by attributes.
     *
     * @param other The other entity.
     * @return true if the identities are the same, regardless of other attributes.
     */
    boolean sameIdentityAs(T other){
        return  (other != null) && (this.equals(other));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
