package com.kcwl.ddd.interfaces.dto;

@Deprecated
public class QueryDTO extends BaseDTO {

    private String platformNo;

    public String getPlatformNo() {
        return this.platformNo;
    }

    public void setPlatformNo(String platformNo){
        this.platformNo = platformNo;
    }
}
