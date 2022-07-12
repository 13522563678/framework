package com.kcwl.ddd.infrastructure.session;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

public class SessionData implements Serializable {
    /**
     * 会话id
     */
    private String sessionId;

    /**
     * 会话id
     */
    private String token;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户手机号
     */
    private String mobile;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 身份ID,全平台唯一
     */
    private String identityId;

    /**
     * 身份类型：司机，车队长，车队管理员，货主，物流辅助人，代理货主
     */
    private Integer identityType;

    /**
     * 用户头像
     */
    private String headPic;

    /**
     * 用户所属机构id
     */
    private Long orgId;
    /**
     * 用户所属机构名称
     */
    private String orgName;

    /**
     * 用户所属机构类型
     */
    private Integer orgType;
    /**
     * 用户所属部门id
     */
    private Long deptId;
    /**
     * 用户所属部门名称
     */
    private String deptName;

    /**
     * 部门名称类型
     */
    private Integer deptType;
    /**
     * 用户所属角色：角色id，是多个用',’分隔
     */
    private String roles;
    /**
     * 授权标识：1 未审核； 2审核通过；3审核不通过
     */
    private Byte authTag;

    /**
     * 用户标签：标签 1 测试 2 内部 3 真实
     */
    private Byte userTag;

    /**
     * 用户登录时间
     */
    private Date loginTime;

    /**
     * 用户会话加盐标识
     */
    private String key;

    /**
     * 用户所属平台码，如果没有平台码区分，为空字符串；
     */
    private String platformNo;

    /**
     * 用户所登录的终端类型
     */
    private Integer product;

    /**
     * 附加数据信息，value值仅支持基本数据类型或String类型
     */
    private HashMap extra;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Integer getIdentityType() {
        return identityType;
    }

    public void setIdentityType(Integer identityType) {
        this.identityType = identityType;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public Byte getAuthTag() {
        return authTag;
    }

    public void setAuthTag(Byte authTag) {
        this.authTag = authTag;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPlatformNo() {
        return platformNo;
    }

    public void setPlatformNo(String platformNo) {
        this.platformNo = platformNo;
    }

    public HashMap getExtra() {
        return extra;
    }

    public void addExtraData(String key, Integer data) {
        saveExtraData(key, data);
    }

    public void addExtraData(String key, Long data) {
        saveExtraData(key, data);
    }

    public void addExtraData(String key, String data) {
        saveExtraData(key, data);
    }

    public void addExtraData(String key, Byte data) {
        saveExtraData(key, data);
    }

    public Object getExtraData(String key) {
        Object val = null;
        if ( extra != null ) {
            val = extra.get(key);
        }
        return val;
    }

    public Byte getUserTag() {
        return userTag;
    }

    public void setUserTag(Byte userTag) {
        this.userTag = userTag;
    }

    public Integer getProduct() {
        return product;
    }

    public void setProduct(Integer product) {
        this.product = product;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getIdentityId() {
        return identityId;
    }

    public void setIdentityId(String identityId) {
        this.identityId = identityId;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public Integer getOrgType() {
        return orgType;
    }

    public void setOrgType(Integer orgType) {
        this.orgType = orgType;
    }

    public Integer getDeptType() {
        return deptType;
    }

    public void setDeptType(Integer deptType) {
        this.deptType = deptType;
    }

    @Override
    public String toString() {
        return "SessionData{" +
                "sessionId='" + sessionId + '\'' +
                ", token='" + token + '\'' +
                ", userId=" + userId +
                ", mobile='" + mobile + '\'' +
                ", userName='" + userName + '\'' +
                ", realName='" + realName + '\'' +
                ", identityId='" + identityId + '\'' +
                ", identityType=" + identityType +
                ", headPic='" + headPic + '\'' +
                ", orgId=" + orgId +
                ", orgName='" + orgName + '\'' +
                ", orgType=" + orgType +
                ", deptId=" + deptId +
                ", deptName='" + deptName + '\'' +
                ", deptType=" + deptType +
                ", roles='" + roles + '\'' +
                ", authTag=" + authTag +
                ", userTag=" + userTag +
                ", loginTime=" + loginTime +
                ", key='" + key + '\'' +
                ", platformNo='" + platformNo + '\'' +
                ", product=" + product +
                ", extra=" + extra +
                '}';
    }

    private void saveExtraData(String key, Object data) {
        if ( extra == null ) {
            extra = new HashMap();
        }
        extra.put(key, data);
    }
}
