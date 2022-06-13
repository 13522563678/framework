package com.kcwl.ddd.application.constants;

public enum ProductEnum {
    CARRIER_APP(1, "司机APP"),
    OPMS_WEB(4, "综合运营"),
    TMS_WEB(5, "TMS"),
    BPMS_WEB(6, "业绩系统"),
    SHIPPER_WEB(7, "客商Web"),
    ICMS_WEB(8, "数智平台"),
    SHIPPER_APP(9, "物流APP");

    private Integer id;
    private String  code;
    private String  desc;

    public Integer getId() {return this.id;}

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public boolean equals(String product) { return this.code.equals(product);}

    public boolean equals(Integer product) { return this.code.equals(product);}

    ProductEnum(int id, String desc) {
        this.id = id;
        this.code = this.id.toString();
        this.desc = desc;
    }
}
