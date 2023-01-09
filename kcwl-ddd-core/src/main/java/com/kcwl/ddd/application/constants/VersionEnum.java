package com.kcwl.ddd.application.constants;

import com.kcwl.ddd.domain.entity.ValueObject;

/**
 * 数据来源版本，取值：20：表示数据来自2.0版本 54：表示数据来自5.4版本 60：表示数据来自6.0版本；
 */
public enum VersionEnum implements ValueObject<VersionEnum> {
    /**
     * 2.0版本
     */
    V_2(20,"2.0版本"),

    /**
     * 5.4版本
     */
    V_5(54,"5.4版本"),
    /**
     * 6.0版本
     */
    V_6(60,"6.0版本");


    private Integer value;

    private String label;

    VersionEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public Integer getValue() {
        return value;
    }

    public static String getLabelByValue(Integer value){
        for (VersionEnum s : VersionEnum.values()) {
            if(value.equals(s.getValue())){
                return s.getLabel();
            }
        }
        return "";
    }

    public static VersionEnum getStatusEnum(Integer value){
        if(value == null) {
            return null;
        }
        for (VersionEnum s : VersionEnum.values()) {
            if(value.equals(s.getValue())){
                return s;
            }
        }
        return null;
    }


    @Override
    public boolean sameValueAs(VersionEnum other) {
        return this.equals(other);
    }
}
