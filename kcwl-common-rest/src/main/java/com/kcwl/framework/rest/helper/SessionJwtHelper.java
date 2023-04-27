package com.kcwl.framework.rest.helper;

import com.kcwl.ddd.domain.entity.UserAgent;
import com.kcwl.ddd.infrastructure.encrypt.KcKeyManager;
import com.kcwl.ddd.infrastructure.session.SessionData;
import com.kcwl.framework.rest.web.CommonWebProperties;
import com.kcwl.framework.utils.JsonUtil;
import com.kcwl.framework.utils.KcBeanRepository;
import com.kcwl.framework.utils.KcEncryptAesUtil;
/**
 * @author ckwl
 */
public class SessionJwtHelper {

    private static final String JWT_ENC_IV = "1234567kc9876543";

    private SessionJwtHelper(){
    }

    public static SessionData getJwtSessionData(String jwtSession, UserAgent requestUserAgent) {
        if ( jwtSession != null ) {
            String jwtEncKey = KcKeyManager.getInstance().getParamPrivateKey(requestUserAgent.getProduct());
            String jsonSession = KcEncryptAesUtil.decryptWithBase62(jwtSession, jwtEncKey, JWT_ENC_IV);
            return JsonUtil.fromJson(jsonSession, SessionData.class);
        }
        return null;
     }

    public static String createJwtSession(UserAgent requestUserAgent, SessionData sessionData) {
        if ( sessionData != null ) {
            String jwtEncKey = KcKeyManager.getInstance().getParamPrivateKey(requestUserAgent.getProduct());
            String jsonSession = JsonUtil.toJson(getJwtSessionData(sessionData));
            return KcEncryptAesUtil.encryptWithBase62(jsonSession, jwtEncKey, JWT_ENC_IV);
        }
        return null;
    }

    public static boolean isEnableJwtAuth() {
        CommonWebProperties.JwtConfig jwtConfig = KcBeanRepository.getInstance().getBean(ConfigBeanName.JWT_CONFIG_NAME, CommonWebProperties.JwtConfig.class);
        return (jwtConfig != null) && jwtConfig.isEnableJwtAuth();
    }

    private static SessionData getJwtSessionData(SessionData sessionData) {
        SessionData jwtSessionData = new SessionData();
        jwtSessionData.setSessionId(sessionData.getSessionId());
        jwtSessionData.setToken(sessionData.getToken());
        jwtSessionData.setPlatformNo(sessionData.getPlatformNo());
        jwtSessionData.setProduct(sessionData.getProduct());
        jwtSessionData.setIdentityType(sessionData.getIdentityType());
        jwtSessionData.setIdentityId(sessionData.getIdentityId());
        jwtSessionData.setUserId(sessionData.getUserId());
        jwtSessionData.setMobile(sessionData.getMobile());
        jwtSessionData.setUserName(sessionData.getUserName());
        jwtSessionData.setRealName(sessionData.getRealName());
        jwtSessionData.setRoles(sessionData.getRoles());
        jwtSessionData.setUserTag(sessionData.getUserTag());
        jwtSessionData.setHeadPic(sessionData.getHeadPic());
        jwtSessionData.setOrgId(sessionData.getOrgId());
        jwtSessionData.setOrgName(sessionData.getOrgName());
        jwtSessionData.setOrgType(sessionData.getOrgType());
        jwtSessionData.setDeptId(sessionData.getDeptId());
        jwtSessionData.setDeptName(sessionData.getDeptName());
        return jwtSessionData;
    }
}
