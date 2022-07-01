package com.kcwl.ddd.domain.entity;

import com.kcwl.ddd.infrastructure.api.CommonCode;
import com.kcwl.ddd.infrastructure.exception.BizException;
import com.kcwl.ddd.infrastructure.utils.ValidationUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * 电话号码
 */
public class PhoneNumber implements ValueObject<PhoneNumber> {

    private String tel;

    /**
     * 有效性正则
     */
    public static final Pattern VALID_PATTERN = Pattern.compile("(^(\\d{2,4}[-_－—]?)?\\d{3,8}([-_－—]?\\d{3,8})?([-_－—]?\\d{1,7})?$)|(^0?1[35]\\d{9}$)") ;
    /**
     * Constructor.
     *
     * @param telNumber
     */
    public PhoneNumber(final String telNumber) {
        if(!ValidationUtil.isPhone(telNumber)) {
            throw new BizException(CommonCode.DATA_LOGIC_ERROR_CODE.getCode(), "电话号码格式不正确");
        }
        this.tel = telNumber;
    }

    public String getTel() {
        return tel;
    }

    @Override
    public boolean sameValueAs(PhoneNumber other) {
        return other != null && this.tel.equals(other.tel);
    }

    @Override
    public String toString() {
        return tel;
    }
}
