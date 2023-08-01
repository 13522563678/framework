package com.kcwl.framework.rest.web.filter;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.kcwl.ddd.application.constants.YesNoEnum;
import com.kcwl.ddd.domain.entity.UserAgent;
import com.kcwl.ddd.infrastructure.api.CommonCode;
import com.kcwl.ddd.infrastructure.constants.GlobalConstant;
import com.kcwl.ddd.infrastructure.encrypt.KcKeyManager;
import com.kcwl.framework.rest.helper.ConfigBeanName;
import com.kcwl.framework.rest.helper.ResponseHelper;
import com.kcwl.framework.rest.web.CommonWebProperties;
import com.kcwl.framework.rest.web.filter.reqeust.DecryptRequestWrapper;
import com.kcwl.framework.rest.web.filter.reqeust.FormToJsonRequestWrapper;
import com.kcwl.framework.utils.DecryptUtil;
import com.kcwl.framework.utils.KcBeanRepository;
import com.kcwl.framework.utils.MapParamUtil;
import com.kcwl.framework.utils.RequestUtil;
import com.kcwl.sensitiveword.exception.SensitiveWordScanException;
import com.kcwl.sensitiveword.provider.SensitiveWordScanProvider;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author ckwl
 */
@Slf4j
public class DecryptParamFilter extends OncePerRequestFilter {

    private CommonWebProperties.HttpContent httpContent;

    /**
     * 默认的 排除的不参与 敏感词检测的 请求参数key
     */
    private final List<String> defaultSensitiveWordScanExcludeApiParamKeys =
            Collections.unmodifiableList(ListUtil.list(false, GlobalConstant.KC_TRACE, GlobalConstant.KC_TOKEN, GlobalConstant.KC_APP_SIGN, "appId", "secretId", GlobalConstant.KC_APP_TIMESTAMP));

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String encryptionData = httpServletRequest.getParameter(EncryptParam.ENCRYPT_PARAM_NAME);
        String apiPath = httpServletRequest.getRequestURI();
        CommonWebProperties.Crypt cryptConfig = getCryptConfig();
        CommonWebProperties.SensitiveWordConfig sensitiveWordConfig = getSensitiveWordConfig();
        if (!cryptConfig.isEnabled() || StringUtils.isBlank(encryptionData) || cryptConfig.excludePath(apiPath)) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }
        try {
            String product = getProductType(httpServletRequest);
            String cryptKey = KcKeyManager.getInstance().getParamPrivateKey(product);

            Map<String, Object> param = DecryptUtil.decryptParam(encryptionData, cryptKey);
            if (param == null) {
                ResponseHelper.buildResponseBody(CommonCode.JSON_DECODE_FAIL, httpServletResponse);
                return;
            }

            boolean isJsonContent = isJsonContentRequest(param);
            Map<String, String[]> originalParam = httpServletRequest.getParameterMap();
            if (!isJsonContent) {
                for (Map.Entry<String, String[]> entry : originalParam.entrySet()) {
                    if (EncryptParam.ENCRYPT_PARAM_NAME.equalsIgnoreCase(entry.getKey())) {
                        continue;
                    }
                    String[] entryValues = entry.getValue();
                    if (entryValues != null) {
                        param.put(entry.getKey(), entryValues.length == 1 ? entryValues[0] : entryValues);
                    }
                }
            }
            // 2023/6/5 敏感词检测
            if (sensitiveWordConfig.isEnable() && sensitiveWordConfig.isGlobalScannerEnable() && !CollUtil.contains(sensitiveWordConfig.getExcludeApiPaths(), apiPath)) {
                try {
                    SensitiveWordScanProvider sensitiveWordScanProvider = SpringUtil.getBean(SensitiveWordScanProvider.class);
                    param.entrySet().stream()
                            .filter(entry -> !CollUtil.contains(this.defaultSensitiveWordScanExcludeApiParamKeys, entry.getKey()))
                            .filter(entry -> !CollUtil.contains(sensitiveWordConfig.getExcludeApiParamKeys(), entry.getKey()))
                            .map(Map.Entry::getValue)
                            .filter(Objects::nonNull)
                            .forEach(value -> {
                                if (sensitiveWordScanProvider.existsSensitiveWord(JSONUtil.toJsonStr(value))) {
                                    throw new SensitiveWordScanException(String.format("用户输入 %s 包含敏感词 !", value));
                                }
                            });
                } catch (BeansException beansException) {
                    log.error("敏感词检测，获取检测服务实例Bean异常：", beansException);
                } catch (SensitiveWordScanException sensitiveWordScanException) {
                    throw sensitiveWordScanException;
                } catch (Exception exception) {
                    log.error("敏感词检测，异常： ", exception);
                }
            }
            DecryptRequestWrapper requestWrapper = createDecryptRequestWrapper(httpServletRequest, param, originalParam, isJsonContent);
            filterChain.doFilter(requestWrapper, httpServletResponse);
        } catch (SensitiveWordScanException sensitiveWordScanException) {
            log.error(" {} ", sensitiveWordScanException.getMessage());
            ResponseHelper.buildResponseBody(CommonCode.CONTAIN_SENSITIVE_WORDS, httpServletResponse);
        } catch (Exception e) {
            log.error("出错了：" + e.getMessage(), e);
            ResponseHelper.buildResponseBody(CommonCode.SYS_ERROR, httpServletResponse);
        }
    }

    public void setHttpContent(CommonWebProperties.HttpContent httpContent) {
        this.httpContent = httpContent;
    }

    private DecryptRequestWrapper createDecryptRequestWrapper(HttpServletRequest httpServletRequest, Map<String, Object> encryptParam, Map<String, String[]> originalParam, boolean isJsonContent) {
        if (log.isDebugEnabled()) {
            log.debug("enableFormToJson={}, isJsonContent={}", httpContent.isEnableFormToJson(), isJsonContent);
        }
        if (httpContent.isEnableFormToJson() && isJsonContent) {
            return new FormToJsonRequestWrapper(httpServletRequest, encryptParam,originalParam);
        }
        return new DecryptRequestWrapper(httpServletRequest, MapParamUtil.convertToMultiValueMapV2(encryptParam));
    }

    private boolean isFormToJsonContext(HttpServletRequest httpServletRequest) {
        String path = RequestUtil.getRequestPath(httpServletRequest);
        if (MediaType.APPLICATION_FORM_URLENCODED_VALUE.equalsIgnoreCase(httpServletRequest.getContentType()) && httpContent.isFormToJson(path)) {
            return true;
        }
        return false;
    }

    private boolean isJsonContentRequest(Map<String, Object> param) {
        Integer jsonContentValue = MapParamUtil.getInteger(param, "isJsonContent");
        if (log.isDebugEnabled()) {
            log.debug("jsonContentValue={} ", jsonContentValue);
        }
        if (jsonContentValue != null) {
            return jsonContentValue.equals(YesNoEnum.YEA.getValue());
        }
        return false;
    }

    private String getProductType(HttpServletRequest httpServletRequest) {
        return RequestUtil.getCookieValue(httpServletRequest, UserAgent.FILED_PRODUCT);
    }

    private CommonWebProperties.Crypt getCryptConfig() {
        CommonWebProperties commonWebProperties = KcBeanRepository.getInstance().getBean(ConfigBeanName.COMMON_WEB_CONFIG_NAME, CommonWebProperties.class);
        return commonWebProperties.getCrypt();
    }

    private CommonWebProperties.SensitiveWordConfig getSensitiveWordConfig() {
        CommonWebProperties commonWebProperties = KcBeanRepository.getInstance().getBean(ConfigBeanName.COMMON_WEB_CONFIG_NAME, CommonWebProperties.class);
        return commonWebProperties.getSensitiveWord();
    }
}
