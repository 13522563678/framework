package com.kcwl.framework.rest.helper;

import com.kcwl.ddd.domain.entity.UserAgent;
import com.kcwl.ddd.infrastructure.encrypt.KcKeyManager;
import com.kcwl.ddd.infrastructure.session.SessionData;
import com.kcwl.framework.utils.JsonUtil;
import com.kcwl.framework.utils.KcEncryptAesUtil;
/**
 * @author ckwl
 */
public class SessionJwtHelper {

    private static final String JWT_ENC_IV = "1234567kc9876543";

    private SessionJwtHelper(){
    }


    public static SessionData getJwtSessionData(String jwtSession, UserAgent requestUserAgent) {
        String jwtEncKey = KcKeyManager.getInstance().getParamPrivateKey(requestUserAgent.getProduct());
        String jsonSession = KcEncryptAesUtil.decryptWithBase62(jwtSession, jwtEncKey, JWT_ENC_IV);
        return JsonUtil.fromJson(jsonSession, SessionData.class);
     }

    public static String createJwtSession(UserAgent requestUserAgent, SessionData sessionData) {
        if ( sessionData != null ) {
            String jwtEncKey = KcKeyManager.getInstance().getParamPrivateKey(requestUserAgent.getProduct());
            String jsonSession = JsonUtil.toJson(sessionData);
            return KcEncryptAesUtil.encryptWithBase62(jsonSession, jwtEncKey, JWT_ENC_IV);
        }
        return null;
    }
}
