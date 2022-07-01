package com.kcwl.ddd.domain.entity;

import com.kcwl.ddd.infrastructure.api.CommonCode;
import com.kcwl.ddd.infrastructure.exception.BizException;
import com.kcwl.ddd.infrastructure.utils.ValidationUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

public class Mobile implements ValueObject<Mobile> {

    private String mobile;

    /**
     * 有效性正则
     */
    private static final Pattern VALID_PATTERN = Pattern.compile("^((13[0-9])|(14[0,1,4-9])|(15[0-3,5-9])|(16[2,5,6,7])|(17[0-8])|(18[0-9])|(19[0-3,5-9]))\\d{8}$");

    /**
     * Constructor.
     *
     * @param mobile
     */
    public Mobile(final String mobile) {
        if(StringUtils.isEmpty(mobile)) {
            throw new BizException(CommonCode.DATA_LOGIC_ERROR_CODE.getCode(), "手机号不能为空");
        }
        if (!ValidationUtil.isMobile(mobile)) {
            throw new BizException(CommonCode.DATA_LOGIC_ERROR_CODE.getCode(), "手机号格式不正确");
        }
        this.mobile = mobile;
    }

    public String getMobile() {
        return mobile;
    }

    @Override
    public boolean sameValueAs(Mobile other) {
        return other != null && this.mobile.equals(other.mobile);
    }

    @Override
    public String toString() {
        return mobile;
    }
}
