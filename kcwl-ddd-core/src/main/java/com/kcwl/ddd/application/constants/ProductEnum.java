package com.kcwl.ddd.application.constants;

public enum ProductEnum {
    CARRIER_APP(1, "CARRIER","司机APP"),
    OPMS_WEB(4, "BPMS","综合运营"),
    TMS_WEB(5, "TMS","TMS"),
    BPMS_WEB(6, "BPMS","业绩系统"),
    SHIPPER_WEB(7, "SHIPPER_WEB","客商Web"),
    ICMS_WEB(8, "ICMS","数智平台"),
    SHIPPER_APP(9, "SHIPPER_APP","物流APP");

    private Integer id;
    private String  code;
    private String shortName;
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

    ProductEnum(int id, String value, String desc) {
        this.id = id;
        this.shortName = value;
        this.code = this.id.toString();
        this.desc = desc;
    }
}
