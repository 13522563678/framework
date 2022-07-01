package com.kcwl.ddd.application.constants;

public enum StateEnum {
    /**
     * 有效
     */
    ENABLE(0,"启用"),

    /**
     * 禁用
     */
    DISABLE(1,"禁用");


    private Integer value;

    private String label;

    StateEnum(Integer value, String label) {
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
    public static String getLabelByValue(Integer value){
        for (StateEnum s : StateEnum.values()) {
            if(value.equals(s.getValue())){
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
    public static StateEnum getStatusEnum(Integer value){
        if(value == null) {
            return null;
        }
        for (StateEnum s : StateEnum.values()) {
            if(value.equals(s.getValue())){
                return s;
            }
        }
        return null;
    }
}
