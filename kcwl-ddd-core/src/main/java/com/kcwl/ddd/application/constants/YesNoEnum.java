package com.kcwl.ddd.application.constants;

import com.kcwl.ddd.domain.entity.ValueObject;

public enum YesNoEnum implements ValueObject<YesNoEnum> {

    NAY (0, "否"),

    YEA(1, "是");


    private Integer value;

    private String label;

    YesNoEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public Integer getValue() {
        return value;
    }

    /**
     * 根据匹配value的值获取Label
     *
     * @param value
     * @return
     */
    public static String getLabelByValue(Integer value) {
        for (YesNoEnum s : YesNoEnum.values()) {
            if (value.equals(s.getValue())) {
                return s.getLabel();
            }
        }
        return "";
    }

    /**
     * 获取StatusEnum
     *
     * @param value
     * @return
     */
    public static YesNoEnum getYeaNayEnum(Integer value) {
        if (value == null) {
            return null;
        }
        for (YesNoEnum s : YesNoEnum.values()) {
            if (value.equals(s.getValue())) {
                return s;
            }
        }
        return null;
    }

    @Override
    public boolean sameValueAs(final YesNoEnum other) {
        return this.equals(other);
    }
}

