package com.kcwl.framework.rest.jackson;

/**
 * @author ckwl
 */

public enum FieldTypeEnum {

    /**
     * 接口code
     */
    STRING("String","String类型"),
    PRIMITIVE("Primitive","基础数据类型"),
    ARRAY("Array","Array类型"),
    OBJECT("Object","对象类型");

    private String type;
    private String desc;

    private FieldTypeEnum(String type, String description){
        this.type = type;
        this.desc = description;
    }

    public String getCode() {
        return type;
    }

    public String getDescription() {
        return desc;
    }
}
