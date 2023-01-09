package com.kcwl.ddd.application.constants;

import com.kcwl.ddd.domain.entity.ValueObject;

/**
 * 数据来源版本，取值：20：表示数据来自2.0版本 54：表示数据来自5.4版本 60：表示数据来自6.0版本；
 */
public enum SourceVersionEnum implements ValueObject<SourceVersionEnum> {
    /**
     * 2.0版本
     */
    TWENTY(20,"2.0版本"),

    /**
     * 5.4版本
     */
    FIFTY_FOUR(54,"5.4版本"),
    /**
     * 6.0版本
     */
    SIXTY(60,"6.0版本");


    private Integer value;

    private String label;

    SourceVersionEnum(Integer value, String label) {
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
        for (SourceVersionEnum s : SourceVersionEnum.values()) {
            if(value.equals(s.getValue())){
                return s.getLabel();
            }
        }
        return "";
    }

    public static SourceVersionEnum getStatusEnum(Integer value){
        if(value == null) {
            return null;
        }
        for (SourceVersionEnum s : SourceVersionEnum.values()) {
            if(value.equals(s.getValue())){
                return s;
            }
        }
        return null;
    }


    @Override
    public boolean sameValueAs(SourceVersionEnum other) {
        return this.equals(other);
    }
}
