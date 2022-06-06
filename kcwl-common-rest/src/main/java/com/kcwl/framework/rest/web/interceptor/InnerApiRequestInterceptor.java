package com.kcwl.framework.rest.web.interceptor;


import com.kcwl.ddd.infrastructure.api.CommonCode;
import com.kcwl.ddd.infrastructure.constants.GlobalConstant;
import com.kcwl.framework.rest.helper.ResponseHelper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 对于不需要认证的后台服务之间的请求，在Header中通过USER_TOKEN传递会话数据
 * USER_TOKEN的数据格式为：Session对象的Json字符串，然后以urlsafe的Base64格式编码
 *
 * @author 姚华成
 * @date 2017-12-28
 */
@Slf4j
public class InnerApiRequestInterceptor extends HandlerInterceptorAdapter{

    private String appSecretKey;

    public InnerApiRequestInterceptor(String appSecretKey) {
        this.appSecretKey = appSecretKey;
    }

    @SneakyThrows
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        String appSecret = request.getHeader(GlobalConstant.APP_SECRET_FIELD_NAME);
        if ( !appSecretKey.equals(appSecret) ) {
            String msg = "请提供正确的appSecret！";
            log.warn(msg);
            ResponseHelper.buildResponseBody(CommonCode.AUTH_ERROR_CODE, response);
            return false;
        }
        return true;
    }
}
