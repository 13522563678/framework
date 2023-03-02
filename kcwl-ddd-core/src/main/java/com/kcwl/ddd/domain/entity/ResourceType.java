package com.kcwl.ddd.domain.entity;

/**
 * <p>
 * 谨慎使用，将在权限服务 下个迭代版本 废弃
 *
 * @since 2023.03.02
 * </p>
 */

@Deprecated
public enum ResourceType implements ValueObject<ResourceType> {
    MENU("MENU"),
    BUTTON("BUTTON"),
    API("API"),
    DATA("DATA"),
    COL_GROUP("COL_GROUP"),
    COL_FIELD("COL_FIELD");

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
