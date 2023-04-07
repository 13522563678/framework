package com.kcwl.ddd.application.constants;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ckwl
 */

public enum IdentityTypeEnum {
    /**
     * 定义身份类型
     */
    SHIPPER_INTERNAL_USER(1, 10,"货主-内部部门用户"),
    SHIPPER_EXTERNAL_USER(3, 30,"货主-外部部门用户"),
    PLATFORM_FUNDING_AGENCY(4, 40,"平台方-垫资人"),
    PLATFORM_OPERATOR(99, 99,"平台方-运营"),
    CARRIER_TCOM_MANAGER(6, 5,"承运方-物流公司管理者"),
    CARRIER_SFLTOP(7, 3,"承运方-车老板"),
    CARRIER_DRIVER(2, 1,"承运方-个体司机"),
    CARRIER_BRKR(8, 4,"承运方-运力辅助人(代收人)"),
    CARRIER_TDSPR(9, 7,"承运方-辅助服务商");

    private Integer  type;
    private Integer  subType;
    private String  desc;

    public Integer getCode() {
        return type;
    }

    public Integer getType() {
        return type;
    }

    public Integer getSubType() {
        return subType;
    }

    public String getDesc() {
        return desc;
    }

    IdentityTypeEnum(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }
    IdentityTypeEnum(int type, int subType, String desc) {
        this.type = type;
        this.desc = desc;
        this.subType = subType;
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
