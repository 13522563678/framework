package com.kcwl.common.web;

/**
 * @author ckwl
 */
public class ApiAuthInfo {
    private String kcToken;
    private String url;
    private String ssid;
    private String nonce;
    private String timeStamp;
    private String sign;
    private String key;

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getKcToken() {
        return kcToken;
    }

    public void setKcToken(String kcToken) {
        this.kcToken = kcToken;
    }
}
