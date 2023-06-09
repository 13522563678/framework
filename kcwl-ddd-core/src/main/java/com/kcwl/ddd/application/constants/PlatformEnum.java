package com.kcwl.ddd.application.constants;

/**
 * @author ckwl
 */

public enum PlatformEnum {

    /**
     * 平台码枚举类型
     */
    PLATFORM_KC("201", "快成", "快成网络货运平台"),
    PLATFORM_JN("203", "晋能", "晋能网络货运平台"),
    PLATFORM_SD("202", "绥德", "绥德网络货运平台"),
    PLATFORM_LC("206", "黎城", "黎城网络货运平台"),
    PLATFORM_YF("207", "易丰", "易丰网络货运平台"),
    PLATFORM_XZ("209", "西藏", "西藏网络货运平台"),
    PLATFORM_JL("214", "建龙", "建龙网络货运平台"),
    PLATFORM_NM("218", "内蒙", "内蒙网络货运平台"),

    PLATFORM_UNION_V1("100", "快成", "快成联盟(旧)"),
    PLATFORM_UNION_V2("200", "快成", "快成联盟(新)");

    private String platformNo;
    private String shortName;
    private String fullName;

    PlatformEnum(String platformNo, String shortName, String fullName) {
        this.platformNo = platformNo;
        this.shortName = shortName;
        this.fullName = fullName;
    }

    public String getPlatformNo() {
        return platformNo;
    }

    public String getShortName() {
        return shortName;
    }

    public String getFullName() {
        return fullName;
    }

    public static String getPlatformShortNameByPlatformNo(String platformNo) {
        for (PlatformEnum e : values()) {
            if (e.platformNo.equals(platformNo)) {
                return e.getShortName();
            }
        }
        return "";
    }

    public static String getPlatformFullNameByPlatformNo(String platformNo) {
        for (PlatformEnum e : values()) {
            if (e.platformNo.equals(platformNo)) {
                return e.getFullName();
            }
        }
        return "";
    }
}
