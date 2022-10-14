package com.kcwl.ddd.application.constants;

public enum InvoiceTypeEnum {
    LONG_DISTANCE(1,  "长途"),
    SHORT_DISTANCE(2,  "短途");

    private Integer code ;
    private String value;

    InvoiceTypeEnum(Integer code,String value){
        this.code = code;
        this.value = value;
    }

    public static InvoiceTypeEnum of(Integer code) {
        for (InvoiceTypeEnum invoiceTypeEnum : values()) {
            if (invoiceTypeEnum.getCode().equals(code)) {
                return invoiceTypeEnum;
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
}
