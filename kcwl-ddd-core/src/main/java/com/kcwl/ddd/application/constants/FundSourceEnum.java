package com.kcwl.ddd.application.constants;

public enum FundSourceEnum {
    ABC(1, "abc", "农业银行"),
    CEB(2, "ceb", "光大银行"),
    CITIC(3, "citic", "中信银行"),
    SPDB(4, "spdb", "浦发银行");

    private int code;
    private String bankCode;
    private String desc;

    FundSourceEnum(int code, String bankCode, String desc) {
        this.code = code;
        this.bankCode = bankCode;
        this.desc = desc;
    }

    public static int getCodeByBankCode(String bankCode) {
        for (FundSourceEnum e : values()) {
            if (e.bankCode.equals(bankCode)) {
                return e.code;
            }
        }
        return 0;
    }

    public static String getBankCodeByCode(int code) {
        for (FundSourceEnum e : values()) {
            if (e.code == code) {
                return e.bankCode;
            }
        }
        return "";
    }


    public static String getDescByBankCode(int code) {
        for (FundSourceEnum e : values()) {
            if (e.code == code) {
                return e.desc;
            }
        }
        return "";
    }

    public static FundSourceEnum getFundSource(int code) {
        for (FundSourceEnum e : values()) {
            if (e.code == code) {
                return e;
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }

    public String getBankCode() {
        return bankCode;
    }

    public String getDesc() {
        return desc;
    }
}
