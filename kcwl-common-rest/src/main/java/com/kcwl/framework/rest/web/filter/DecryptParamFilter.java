package com.kcwl.framework.rest.web.filter;

import com.kcwl.ddd.application.constants.YesNoEnum;
import com.kcwl.ddd.domain.entity.UserAgent;
import com.kcwl.ddd.infrastructure.api.CommonCode;
import com.kcwl.ddd.infrastructure.encrypt.KcKeyManager;
import com.kcwl.framework.rest.helper.ResponseHelper;
import com.kcwl.framework.rest.web.CommonWebProperties;
import com.kcwl.framework.rest.web.filter.reqeust.FormToJsonRequestWrapper;
import com.kcwl.framework.utils.DecryptUtil;
import com.kcwl.framework.rest.web.filter.reqeust.DecryptRequestWrapper;
import com.kcwl.framework.utils.MapParamUtil;
import com.kcwl.framework.utils.RequestUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author ckwl
 */
@Slf4j
public class DecryptParamFilter extends OncePerRequestFilter {

    private boolean enableCrypt=true;
    private CommonWebProperties.HttpContent httpContent;

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String encryptionData  = httpServletRequest.getParameter("encryptionData");

        if ( !enableCrypt || StringUtils.isBlank(encryptionData) ){
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }
        try {
            String product = getProductType(httpServletRequest);
            String cryptKey = KcKeyManager.getInstance().getParamPrivateKey(product);

            Map<String ,Object> param =DecryptUtil.decryptParam(encryptionData, cryptKey);
            if(param==null){
                ResponseHelper.buildResponseBody(CommonCode.JSON_DECODE_FAIL, httpServletResponse);
                return;
            }

            boolean isJsonContent = isJsonContentRequest(param);

            if ( !isJsonContent ) {
                for (Map.Entry<String, String[]> entry : httpServletRequest.getParameterMap().entrySet()) {
                    if ("encryptionData".equalsIgnoreCase(entry.getKey())) {
                        continue;
                    }
                    String[] entryValues = entry.getValue();
                    if (entryValues != null) {
                        param.put(entry.getKey(), entryValues.length == 1 ? entryValues[0] : entryValues);
                    }
                }
            }
            DecryptRequestWrapper requestWrapper = createDecryptRequestWrapper(httpServletRequest, param, isJsonContent);
            filterChain.doFilter(requestWrapper, httpServletResponse);
        } catch (Exception e) {
            log.error("出错了："+e.getMessage(),e);
            ResponseHelper.buildResponseBody(CommonCode.SYS_ERROR, httpServletResponse);
        }
    }

    public void setEnableCrypt(boolean enabled) {
        this.enableCrypt = enabled;
    }

    public void setHttpContent(CommonWebProperties.HttpContent httpContent) {
        this.httpContent = httpContent;
    }

    private DecryptRequestWrapper createDecryptRequestWrapper(HttpServletRequest httpServletRequest, Map<String ,Object> param, boolean isJsonContent) {
        if ( log.isDebugEnabled() ) {
            log.debug("enableFormToJson={}, isJsonContent={}", httpContent.isEnableFormToJson(), isJsonContent);
        }
        if ( httpContent.isEnableFormToJson() && isJsonContent ) {
            return new FormToJsonRequestWrapper(httpServletRequest, param);
        }
        return new DecryptRequestWrapper(httpServletRequest, MapParamUtil.convertToMultiValueMap(param)) ;
    }

    private boolean isFormToJsonContext(HttpServletRequest httpServletRequest) {
        String path = RequestUtil.getRequestPath(httpServletRequest);
        if ( MediaType.APPLICATION_FORM_URLENCODED_VALUE.equalsIgnoreCase(httpServletRequest.getContentType()) && httpContent.isFormToJson(path) ) {
            return true;
        }
        return false;
    }

    private boolean isJsonContentRequest(Map<String ,Object> param) {
        Integer jsonContentValue = MapParamUtil.getInteger(param, "isJsonContent");
        if ( log.isDebugEnabled() ) {
            log.debug("jsonContentValue={} ", jsonContentValue);
        }
        if ( jsonContentValue  != null ) {
            return jsonContentValue.equals(YesNoEnum.YEA.getValue());
        }
        return false;
    }

    private String getProductType(HttpServletRequest httpServletRequest) {
       return RequestUtil.getCookieValue(httpServletRequest, UserAgent.FILED_PRODUCT);
    }
}
