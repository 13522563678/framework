package com.kcwl.ddd.application.constants;

import java.util.ArrayList;
import java.util.List;

public enum IdentityTypeEnum {
    SHIPPER(1, "货主"),
    SHIPPER_COMPLETED_AGENT(2, "代理货主(全托管)"),
    SHIPPER_EXTERNAL_AGENT(3, "代理货主(外部发货部门)"),
    TRANSPORT_AGENT(4, "运力经纪人"),
    PLATFORM_OPERATOR(5, "平台运营"),
    TMS_ADMIN(6, "运力TMS调度人员"),
    COMPANY_VEHICLE_ADMIN(7, "企业车队管理员"),
    PERSONAL_VEHICLE_ADMIN(8, "车老板"),
    DRIVER(9,"司机"),
    DRIVER_COLLECTOR(11, "运力辅助人(代收人)"),
    PERSONAL_TRANSPORT_ASSISTANT(12, "个体运力辅助服务商"),
    COMPANY_TRANSPORT_ASSISTANT(13, "企业运力辅助服务商");

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
        codes.add(IdentityTypeEnum.DRIVER.getCode());
        codes.add(IdentityTypeEnum.DRIVER_COLLECTOR.getCode());
        codes.add(IdentityTypeEnum.PERSONAL_VEHICLE_ADMIN.getCode());
        codes.add(IdentityTypeEnum.PERSONAL_TRANSPORT_ASSISTANT.getCode());
        return codes;
    }

    /**
     * 获取企业运力列表
     * @return
     */
    public static List<Integer> getCompanyCarrierCodeList() {
        List<Integer> codes = new ArrayList<>();
        codes.add(IdentityTypeEnum.COMPANY_VEHICLE_ADMIN.getCode());
        codes.add(IdentityTypeEnum.COMPANY_TRANSPORT_ASSISTANT.getCode());
        return codes;
    }
}
