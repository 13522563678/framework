package com.kcwl.ddd.application.constants;

public enum BizOfficeType {
    TOP(1, "TOP", "总部"),
    OFFICE(2, "OFFICE", "办事处"),
    DIVISION(3, "DIVISION", "事业部"),
    BRANCH_COMPANY(4, "BRANCH_company", "分子公司"),
    JOINT_COMPANY(5, "JOINT_COMPANY", "合资公司"),
    INTERNAL_DEPART(6, "INTERNAL_DEPART", "内部部门"),
    ;

    private Integer code;
    private String value;
    private String label;

    BizOfficeType(Integer code, String value, String label) {
        this.code = code;
        this.value = value;
        this.label = label;
    }

    public static String getDesc(String value) {
        for (BizOfficeType aase : values()) {
            if (aase.getValue().equals(value)) {
                return aase.getLabel();
            }
        }
        return null;
    }

    public static BizOfficeType of(String value) {
        for (BizOfficeType aase : values()) {
            if (aase.getValue().equals(value)) {
                return aase;
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }
}
