package com.kcwl.ddd.application.constants;

import java.util.ArrayList;
import java.util.List;

public enum IdentityTypeEnum {
    SHIPPER_INTERNAL_USER(1, "货主-内部部门用户"),
    SHIPPER_EXTERNAL_USER(3, "货主-外部部门用户"),
    PLATFORM_FUNDING_AGENCY(4, "平台方-垫资人"),
    PLATFORM_OPERATOR(5, "平台方-运营"),
    CARRIER_TCOM_MANAGER(6, "承运方-物流公司管理者"),
    CARRIER_SFLTOP(7, "承运方-车老板"),
    CARRIER_DRIVER(2,"承运方-个体司机"),
    CARRIER_BRKR(8, "承运方-运力辅助人(代收人)"),
    CARRIER_TDSPR(9, "承运方-辅助服务商");

    private Integer  type;
    private String  desc;

    public Integer getCode() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    IdentityTypeEnum(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static IdentityTypeEnum of(Integer value) {
        if (value == null) {
            return null;
        }
        for (IdentityTypeEnum identityTypeEnum : values()) {
            if (identityTypeEnum.type.equals(value)) {
                return identityTypeEnum;
            }
        }
        return null;
    }

    /**
     * 获取个体运力列表
     * @return
     */
    public static List<Integer> getPersonalCarrierCodeList() {
        List<Integer> codes = new ArrayList<>();
        codes.add(IdentityTypeEnum.CARRIER_DRIVER.getCode());
        codes.add(IdentityTypeEnum.CARRIER_BRKR.getCode());
        return codes;
    }

    /**
     * 获取企业运力列表
     * @return
     */
    public static List<Integer> getCompanyCarrierCodeList() {
        List<Integer> codes = new ArrayList<>();
        codes.add(IdentityTypeEnum.CARRIER_TCOM_MANAGER.getCode());
        codes.add(IdentityTypeEnum.CARRIER_TDSPR.getCode());
        return codes;
    }
}
