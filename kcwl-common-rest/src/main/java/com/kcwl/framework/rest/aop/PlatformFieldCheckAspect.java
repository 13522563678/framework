package com.kcwl.framework.rest.aop;

import com.kcwl.ddd.infrastructure.constants.GlobalConstant;
import com.kcwl.ddd.interfaces.dto.QueryDTO;
import com.kcwl.framework.utils.StringUtil;
import com.kcwl.tenant.TenantDataHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

import java.util.Map;

/**
 * @author ckwl
 */
@Aspect
@Order(-1)
public class PlatformFieldCheckAspect {

    private static final Logger logger = LoggerFactory.getLogger(PlatformFieldCheckAspect.class);
    private static final String SUFFIX_UNION_PLATFORM = "00";

    public PlatformFieldCheckAspect() {
        logger.info("init PlatformFieldCheckAspect!");
    }

    @Around("@within(org.springframework.web.bind.annotation.RestController)")
    public Object controllerMethodPoint(ProceedingJoinPoint pjp) throws Throwable {
        setPlatformNo(pjp.getArgs());
        return pjp.proceed();
    }

    private void setPlatformNo(Object[] argValues) {
        if (argValues != null) {
            for (Object argValue : argValues) {
                if (argValue instanceof QueryDTO) {
                    QueryDTO condition = (QueryDTO) argValue;
                    if (StringUtil.isEmpty(condition.getPlatformNo())) {
                        String platformNo = getSpecificPlatformNo();
                        if (platformNo != null) {
                            condition.setPlatformNo(platformNo);
                        }
                    }
                } else if (argValue instanceof Map) {
                    Map<String, Object> condition = (Map)argValue;
                    Object paramPlatformNo = condition.get(GlobalConstant.PARAM_TENANT_FIELD_NAME);
                    if (isNullOrEmpty(paramPlatformNo)) {
                        String platformNo = getSpecificPlatformNo();
                        if (platformNo != null) {
                            condition.put (GlobalConstant.PARAM_TENANT_FIELD_NAME, platformNo);
                        }
                    }
                }
            }
        }
    }

    private String getSpecificPlatformNo() {
        String platformNo = TenantDataHolder.get();
        if ((platformNo != null) && (!platformNo.endsWith(SUFFIX_UNION_PLATFORM))) {
            return platformNo;
        }
        return null;
    }

    public static boolean isNullOrEmpty(Object obj) {
        if ((obj == null) || (obj.equals(GlobalConstant.EMPTY_STRING)) || (obj.equals(GlobalConstant.NULL_STRING))) {
            return true;
        }
        return false;
    }
}
