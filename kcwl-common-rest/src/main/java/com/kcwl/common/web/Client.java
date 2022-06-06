package com.kcwl.common.web;

import com.kcwl.ddd.infrastructure.session.SessionData;

import java.util.Date;


/**
 * 在线用户对象
 *
 * @author kwj
 * @date 2016-6-28
 */
public class Client extends SessionData {

    private static final long serialVersionUID = -8768731942644494004L;
    public static final byte AUTH_STATUS_2 = 2;//2 认证通过

    private Integer roleId;// 角色id，V2.6.0添加

    private Byte checkStatus;//审核状态  1未审核  2审核通过  3审核不通过

    private String umengToken;//友盟的设备唯一标识
    /**
     * 登录时间
     */
    private java.util.Date logindDateTime;

    private Long userId;//用户id

    private String mobile;//手机号

    private String headPic;//头像

    private Byte invoiceMessageStatus;//货单通知   0关闭 1开启

    private Byte voiceMessageStatus;//语音通知 0关闭 1开启

    private String sessionId;

    private Long companyId;

    private Long coalMineId;

    //当companyBizType为-1表示西南短倒，否则属于shipper_company_biz_type里的类型
    private int companyBizType;

    private int enterpriseBizType;

    private String platformNo;

    private int identityType;

    private int userTag;

    private String key;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Byte getInvoiceMessageStatus() {
        return invoiceMessageStatus;
    }

    public void setInvoiceMessageStatus(Byte invoiceMessageStatus) {
        this.invoiceMessageStatus = invoiceMessageStatus;
    }

    public Byte getVoiceMessageStatus() {
        return voiceMessageStatus;
    }

    public void setVoiceMessageStatus(Byte voiceMessageStatus) {
        this.voiceMessageStatus = voiceMessageStatus;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    @Override
    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Byte getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(Byte checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getUmengToken() {
        return umengToken;
    }

    public void setUmengToken(String umengToken) {
        this.umengToken = umengToken;
    }

    public Date getLogindDateTime() {
        return logindDateTime;
    }

    public void setLogindDateTime(Date logindDateTime) {
        this.logindDateTime = logindDateTime;
    }


    @Override
    public Long getUserId() {
        return userId;
    }
    @Override
    public Integer getUserType() {
        return identityType;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * 判断该用户是否已认证通过
     *
     * @return
     */
    public boolean isChackPass() {
        return this.checkStatus.byteValue() == AUTH_STATUS_2;
    }

    public Long getCoalMineId() {
        return coalMineId;
    }

    public void setCoalMineId(Long coalMineId) {
        this.coalMineId = coalMineId;
    }

    public int getCompanyBizType() {
        return companyBizType;
    }

    public void setCompanyBizType(int companyBizType) {
        this.companyBizType = companyBizType;
    }

    public int getEnterpriseBizType() {
        return enterpriseBizType;
    }

    public void setEnterpriseBizType(int enterpriseBizType) {
        this.enterpriseBizType = enterpriseBizType;
    }

    public String getPlatformNo() {
        return platformNo;
    }

    public void setPlatformNo(String platformNo) {
        this.platformNo = platformNo;
    }

    public int getIdentityType() {
        return identityType;
    }
    public void setIdentityType(int identityType) {
        this.identityType = identityType;
    }

    public int getUserTag() {
        return userTag;
    }

    public void setUserTag(int userTag) {
        this.userTag = userTag;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "Client{" +
                "roleId=" + roleId +
                ", checkStatus=" + checkStatus +
                ", umengToken='" + umengToken + '\'' +
                ", logindDateTime=" + logindDateTime +
                ", userId=" + userId +
                ", mobile='" + mobile + '\'' +
                ", headPic='" + headPic + '\'' +
                ", invoiceMessageStatus=" + invoiceMessageStatus +
                ", voiceMessageStatus=" + voiceMessageStatus +
                ", sessionId='" + sessionId + '\'' +
                ", companyId=" + companyId +
                ", coalMineId=" + coalMineId +
                ", companyBizType=" + companyBizType +
                ", enterpriseBizType=" + enterpriseBizType +
                ", identityType=" + identityType +
                ", userTag=" + userTag +
                ", key=" + key +
                ", platformNo='" + platformNo + '\'' +
                '}';
    }
}
