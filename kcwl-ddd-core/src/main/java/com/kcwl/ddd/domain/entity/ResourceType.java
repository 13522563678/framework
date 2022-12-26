package com.kcwl.ddd.domain.entity;

public enum ResourceType implements ValueObject<ResourceType> {
    MENU("MENU"),
    BUTTON("BUTTON"),
    API("API"),
    DATA("DATA");

    private String value;

    ResourceType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static ResourceType of(String code) {
        for (ResourceType resourceType : values()) {
            if (resourceType.value.equals(code)) {
                return resourceType;
            }
        }
        return null;
    }

    @Override
    public boolean sameValueAs(ResourceType other) {
        return this.value.equals(other.value);
    }
}
