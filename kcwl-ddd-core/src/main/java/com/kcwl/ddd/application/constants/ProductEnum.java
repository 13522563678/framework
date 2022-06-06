package com.kcwl.ddd.application.constants;

public enum ProductEnum {
    CARRIER_APP(1, "司机APP"),
    SHIPPER_APP(9, "物流APP"),
    CRM_WEB(8, "客商Web"),
    SHIPPER_WEB(7, "客商Web");

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

    ProductEnum(int id, String desc) {
        this.id = id;
        this.code = this.id.toString();
        this.desc = desc;
    }
}
