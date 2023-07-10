package com.kcwl.ddd.application.constants;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ckwl
 */

public enum ProductEnum {
    /**
     * 定义产品端类型
     */
    API_OPEN(0, "OPEN","开放接口"),
    CARRIER_APP(1, "CARRIER","司机APP"),
    SPMS_WEB(4, "SPMS","综合运营平台"),
    TMS_WEB(5, "TMS","TMS管理系统"),
    BPMS_WEB(6, "BPMS","业务绩效平台"),
    SHIPPER_WEB(7, "SHIPPER_WEB","发运Web"),
    OPMS_WEB(8, "OPMS","数智承运平台"),
    SHIPPER_APP(9, "SHIPPER_APP","发运APP"),
    FMGT_WEB(10, "FMGT","资金管理系统"),
    DA_WEB(11, "DA","数据资产管理平台"),
    API_CALL_BACK(12, "CALLBACK","接口回调"),
    API_PARTNER(13, "PARTNER","三方接口"),
    KFB_APP(21, "KFB-APP","快福宝APP");

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

    ProductEnum(int id, String code, String desc) {
        this.id = id;
        this.code = code;
        this.desc = desc;
    }

    public static ProductEnum getProductEnum(String code) {
        for (ProductEnum productEnum : values()) {
            if (productEnum.getCode().equals(code)) {
                return productEnum;
            }
        }
        return null;
    }

    public static ProductEnum getProductEnum(Integer id) {
        for (ProductEnum productEnum : values()) {
            if (productEnum.getId().equals(id)) {
                return productEnum;
            }
        }
        return null;
    }

    public static List<ProductEnum> getProductEnumList(List<String> codeList) {
        List<ProductEnum> productEnums = new ArrayList<>();
        for (ProductEnum productEnum : values()) {
            String productCode = productEnum.code;
            if (codeList.contains(productCode)) {
                productEnums.add(productEnum);
            }
        }
        return productEnums;
    }

}
