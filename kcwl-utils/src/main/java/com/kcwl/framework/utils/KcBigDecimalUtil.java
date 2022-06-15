package com.kcwl.framework.utils;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author ckwl
 */
public class KcBigDecimalUtil {
    private static final int DEFAULT_LENGTH = 16;

    /**
     * BigDecimal 减法 v1-v2
     *
     * @param v1
     * @param v2
     * @return v1-v2
     */
    public static BigDecimal subtract(BigDecimal v1, BigDecimal v2) {
        if (v1 == null) {
            if (v2 == null) {
                return null;
            }
            return v2.negate();
        }
        if (v1 != null && v2 == null) {
            return v1;
        }
        return v1.subtract(v2);
    }

    /**
     * BigDecimal 加法 v1+v2
     *
     * @param v1
     * @param v2
     * @return v1+v2
     */

    public static BigDecimal add(BigDecimal v1, BigDecimal v2) {

        if (v1 == null) {
            if (v2 == null) {
                return null;
            }
            return v2;
        } else if (v2 == null) {
            return v1;
        }

        // if(v1 != null && v2 == null) return v1;

        return v1.add(v2).setScale(2,BigDecimal.ROUND_HALF_UP);
    }



    public static BigDecimal defaultZeroAdd(BigDecimal v1, BigDecimal v2) {

        if (v1 == null) {
            v1 =BigDecimal.ZERO;
        }
        if (v2 == null) {
            v2 =BigDecimal.ZERO;
        }
        return v1.add(v2).setScale(2,BigDecimal.ROUND_HALF_UP);
    }


    /**
     * BigDecimal 乘法 v1*v2
     *
     * @param v1
     * @param v2
     * @return v1*v2
     */
    public static BigDecimal multiply(BigDecimal v1, BigDecimal v2) {
        if (v1 == null || v2 == null) {
            return null;
        }
        v1.setScale(3, RoundingMode.UP);
        return v1.multiply(v2);
    }

    /**
     * BigDecimal 乘法 v1*v2
     *
     * @desc 相乘的结果 按四舍五入保留两位小数
     * @param v1
     * @param v2
     * @return v1*v2
     */
    public static BigDecimal multiply2(BigDecimal v1, BigDecimal v2) {
        if (v1 == null || v2 == null) {
            return null;
        }
        BigDecimal result = v1.multiply(v2);
        result.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        return result;
    }

    /**
     * BigDecimal 乘法 v1*v2
     *
     * @desc 相乘后四舍五入
     * @param v1
     * @param v2
     * @param scale 保留的小数位数
     * @param roundingMode 四舍五入方式,与BigDecimal中定义的值一致：BigDecimal.ROUND_HALF_EVEN， RoundingMode.UP
     * @return v1*v2
     */
    public static BigDecimal multiply(BigDecimal v1, BigDecimal v2, int scale, int roundingMode) {
        if (v1 == null ) {
            return v2;
        }
        if ( v2 == null ) {
            return v1;
        }
        BigDecimal result = v1.multiply(v2);
        return  result.setScale(scale, roundingMode);
    }

    /**
     * BigDecimal 除法法 v1/v2
     *
     * @param v1
     * @param v2
     * @return v1/v2
     */
    public static BigDecimal divide(BigDecimal v1, BigDecimal v2) {
        if (v1 == null || v2 == null || v2.compareTo(new BigDecimal(0.0)) == 0)
            return null;
        return divide(v1, DEFAULT_LENGTH, v2);
    }

    /**
     * BigDecimal 除法法 v1/v2
     *
     * @param v1
     * @param length
     *            有效长度
     * @param v2
     * @return v1/v2
     */
    public static BigDecimal divide(BigDecimal v1, int length, BigDecimal v2) {
        return v1.divide(v2, length, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * BigDecimal 除法法 v1/v2
     *
     * @param v1
     * @param v2
     *            有效长度
     * @param v2
     * @return v1/v2
     */
    public static BigDecimal divide(int v1, int v2) {
        BigDecimal t = new BigDecimal(v1);
        BigDecimal t1 = new BigDecimal(v2);
        return divide(t, t1);
    }

    /**
     * BigDecimal 取相反数
     *
     * @param v
     * @return v的相反数 -v
     */
    public static BigDecimal abs(BigDecimal v) {
        if (v == null) {
            return null;
        }
        return v.abs();
    }

    /**
     * BigDecimal 取相反数
     *
     * @param v
     * @return v的相反数 -v
     */
    public static BigDecimal negate(BigDecimal v) {
        if (v == null) {
            return null;
        }
        return v.negate();
    }

    /**
     * BigDecimal 转换为long
     *
     * @param v
     * @return
     */
    public static long longValue(BigDecimal v) {
        if (v == null) {
            return 0L;
        }
        return v.longValue();
    }

    /**
     * BigDecimal 转换为int
     *
     * @param v
     * @return
     */
    public static int intValue(BigDecimal v) {
        if (v == null) {
            return 0;
        }
        return v.intValue();
    }

    /**
     * BigDecimal 转换为float
     *
     * @param v
     * @return
     */
    public static float floatValue(BigDecimal v) {
        if (v == null) {
            return 0.0f;
        }
        return v.floatValue();
    }

    /**
     * BigDecimal 转换为double
     *
     * @param v
     * @return
     */
    public static double doubleValue(BigDecimal v) {
        if (v == null) {
            return 0.0d;
        }
        return v.doubleValue();
    }

    /**
     * 字符串转换为BigDecimal 对象
     *
     * @param str
     * @return
     */
    public static BigDecimal createBigDecimal(String str) {
        if (StringUtil.isEmpty(str)) {
            return new BigDecimal(0);
        }
        if (!StringUtils.isNumeric(str)) {
            throw new NumberFormatException("this string is not a valid number");
        }
        return new BigDecimal(str);
    }

    /**
     * 金额 分转元
     *
     * @param v
     * @return
     */
    public static BigDecimal centToYuan(Long v) {
        if (v == null) {
            return null;
        }
        return new BigDecimal(v + "").divide(new BigDecimal("100")).setScale(2);
    }


    /**
     * 金额 分转元
     *
     * @param v
     * @return
     */
    public static BigDecimal centToYuan(Integer v) {
        if (v == null) {
            return null;
        }
        return new BigDecimal(v + "").divide(new BigDecimal("100")).setScale(2);
    }

    /**
     * 元转分
     *
     * @param v
     * @return Long
     */
    public static Long yuanToCent(BigDecimal v) {
        if (v == null) {
            return null;
        }
        return new BigDecimal(v + "").multiply(new BigDecimal("100")).longValue();
    }

    public static String stringValue(BigDecimal v) {
        if (v == null){
            return "";
        }
        return v.toString();
    }
}
