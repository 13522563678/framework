package com.kcwl.ddd.infrastructure.utils;

import com.kcwl.ddd.infrastructure.api.CommonCode;
import com.kcwl.ddd.infrastructure.exception.BizException;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtil {
    //------------------常量定义
    /**
     * Email正则表达式="^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
     */
    //public static final String EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";;
    public static final String EMAIL = "\\w+(\\.\\w+)*@\\w+(\\.\\w+)+";
    /**
     * 电话号码正则表达式= (^(\d{2,4}[-_－—]?)?\d{3,8}([-_－—]?\d{3,8})?([-_－—]?\d{1,7})?$)|(^0?1[35]\d{9}$)
     */
    public static final String PHONE = "(^(\\d{2,4}[-_－—]?)?\\d{3,8}([-_－—]?\\d{3,8})?([-_－—]?\\d{1,7})?$)|(^0?1[35]\\d{9}$)" ;
    /**
     * 手机号码正则表达式=^(13[0-9]|14[0-9]|15[0-9]|17[0-9]|18[0-9])\d{8}$
     */
    public static final String MOBILE ="^(13[0-9]|14[0-9]|15[0-9]|17[0-9]|18[0-9])\\d{8}$";


    /**
     * 手机号码正则表达式=^1[\d]{10}  简单校验
     */
    public static final String SIMPLE_MOBILE ="^1[3-9]\\d{9}$";


    /**
     * 数字(整数，负数，小数)
     */
    public static final String NUMBER = "^(\\-|\\+)?\\d+(\\.\\d+)?$";


    /**
     * Integer正则表达式 ^-?(([1-9]\d*$)|0)
     */
    public static final String  INTEGER = "^-?(([1-9]\\d*$)|0)";
    /**
     * 正整数正则表达式 >=0 ^[1-9]\d*|0$
     */
    public static final String  INTEGER_NEGATIVE = "^[1-9]\\d*|0$";
    /**
     * 负整数正则表达式 <=0 ^-[1-9]\d*|0$
     */
    public static final String  INTEGER_POSITIVE = "^-[1-9]\\d*|0$";
    /**
     * Double正则表达式 ^-?([1-9]\d*\.\d*|0\.\d*[1-9]\d*|0?\.0+|0)$
     */
    public static final String  DOUBLE ="^-?([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|0?\\.0+|0)$";
    /**
     * 正Double正则表达式 >=0  ^[1-9]\d*\.\d*|0\.\d*[1-9]\d*|0?\.0+|0$　
     */
    public static final String  DOUBLE_NEGATIVE ="^[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|0?\\.0+|0$";
    /**
     * 负Double正则表达式 <= 0  ^(-([1-9]\d*\.\d*|0\.\d*[1-9]\d*))|0?\.0+|0$
     */
    public static final String  DOUBLE_POSITIVE ="^(-([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*))|0?\\.0+|0$";
    /**
     * 年龄正则表达式 ^(?:[1-9][0-9]?|1[01][0-9]|120)$ 匹配0-120岁
     */
    public static final String  AGE="^(?:[1-9][0-9]?|1[01][0-9]|120)$";
    /**
     * 邮编正则表达式  [0-9]\d{5}(?!\d) 国内6位邮编
     */
    public static final String  CODE="[0-9]\\d{5}(?!\\d)";
    /**
     * 匹配由数字、26个英文字母或者下划线组成的字符串 ^\w+$
     */
    public static final String STR_ENG_NUM_="^\\w+$";
    /**
     * 匹配由数字和26个英文字母组成的字符串 ^[A-Za-z0-9]+$
     */
    public static final String STR_ENG_NUM="^[A-Za-z0-9]+";

    /**
     * 匹配由26个英文字母组成的字符串  ^[A-Za-z]+$
     */
    public static final String STR_ENG="^[A-Za-z]+$";
    /**
     * 过滤特殊字符串正则
     * regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
     */
    public static final String STR_SPECIAL="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
    /***
     * 日期正则 支持：
     *  YYYY-MM-DD
     *  YYYY/MM/DD
     *  YYYY_MM_DD
     *  YYYYMMDD
     *  YYYY.MM.DD的形式
     */
    public static final String DATE_ALL="((^((1[8-9]\\d{2})|([2-9]\\d{3}))([-\\/\\._]?)(10|12|0?[13578])([-\\/\\._]?)(3[01]|[12][0-9]|0?[1-9])$)" +
            "|(^((1[8-9]\\d{2})|([2-9]\\d{3}))([-\\/\\._]?)(11|0?[469])([-\\/\\._]?)(30|[12][0-9]|0?[1-9])$)" +
            "|(^((1[8-9]\\d{2})|([2-9]\\d{3}))([-\\/\\._]?)(0?2)([-\\/\\._]?)(2[0-8]|1[0-9]|0?[1-9])$)|(^([2468][048]00)([-\\/\\._]?)(0?2)([-\\/\\._]?)(29)$)|(^([3579][26]00)" +
            "([-\\/\\._]?)(0?2)([-\\/\\._]?)(29)$)" +
            "|(^([1][89][0][48])([-\\/\\._]?)(0?2)([-\\/\\._]?)(29)$)|(^([2-9][0-9][0][48])([-\\/\\._]?)" +
            "(0?2)([-\\/\\._]?)(29)$)" +
            "|(^([1][89][2468][048])([-\\/\\._]?)(0?2)([-\\/\\._]?)(29)$)|(^([2-9][0-9][2468][048])([-\\/\\._]?)(0?2)" +
            "([-\\/\\._]?)(29)$)|(^([1][89][13579][26])([-\\/\\._]?)(0?2)([-\\/\\._]?)(29)$)|" +
            "(^([2-9][0-9][13579][26])([-\\/\\._]?)(0?2)([-\\/\\._]?)(29)$))";
    /***
     * 日期正则 支持：
     *  YYYY-MM-DD
     */
    public static final String DATE_FORMAT1="(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)";

    /**
     * URL正则表达式
     * 匹配 http www ftp
     */
    public static final String URL = "^(http|www|ftp|)?(://)?(\\w+(-\\w+)*)(\\.(\\w+(-\\w+)*))*((:\\d+)?)(/(\\w+(-\\w+)*))*(\\.?(\\w)*)(\\?)?" +
            "(((\\w*%)*(\\w*\\?)*(\\w*:)*(\\w*\\+)*(\\w*\\.)*(\\w*&)*(\\w*-)*(\\w*=)*(\\w*%)*(\\w*\\?)*" +
            "(\\w*:)*(\\w*\\+)*(\\w*\\.)*" +
            "(\\w*&)*(\\w*-)*(\\w*=)*)*(\\w*)*)$";

    /**
     * 身份证正则表达式
     */
    public static final String IDCARD="((11|12|13|14|15|21|22|23|31|32|33|34|35|36|37|41|42|43|44|45|46|50|51|52|53|54|61|62|63|64|65)[0-9]{4})" +
            "(([1|2][0-9]{3}[0|1][0-9][0-3][0-9][0-9]{3}" +
            "[Xx0-9])|([0-9]{2}[0|1][0-9][0-3][0-9][0-9]{3}))";

    /**
     * 密码校验
     * 大小写字母+数字+特殊符号组合，长度为8~12位
     */
    public static final String PW_PATTERN = "^(?![A-Za-z0-9]+$)(?![a-z0-9\\W]+$)(?![A-Za-z\\W]+$)(?![A-Z0-9\\W]+$)[a-zA-Z0-9\\W]{8,12}$";


    /**
     * 密码长度8-12位，包括大小写字母、数字及特殊符号组成。（其中最少两种组成）
     */
    public static final String SIMPLE_PWD_PATTERN = "^(?![A-Z]+$)(?![a-z]+$)(?!\\d+$)(?![\\W_]+$)\\S{8,12}$";


    public static final String  SPECIAL_NUMERIC = "[ _`~!@#$%^&*+=|{}':;',\\[\\].<>/?~！@#￥%……&*——+|{}【】‘；：”“’。，、？]|\n|\r|\t";

    /**
     * 是否为数字字符串
     */
    public static final String NUMBER_TEXT = "^([0-9]+)$";

    /**
     * 组织机构代码：长度必须为2到20位的大写字母
     */
    public static final String ORGAN_CODE = "^[A-Z]{2,20}+$";

    /**
     * 统一社会信用代码必须为18位数字+字母组合 ^[A-Za-z0-9]{18}+$
     */
    public static final String UNIFIED_SOCIAL_CREDIT_CODE = "^[A-Za-z0-9]{18}+$";

    /**
     * 机构代码
     */
    public static final String JIGOU_CODE = "^[A-Z0-9]{8}-[A-Z0-9]$";

    /**
     * 匹配数字组成的字符串  ^[0-9]+$
     */
    public static final String STR_NUM = "^[0-9]+$";

    /**
     * 车牌号
     * https://blog.csdn.net/LIsmooth/article/details/80981490
     */
    public static final String PLATE_NUMBER = "([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼]{1}(([A-HJ-Z]{1}[A-HJ-NP-Z0-9]{5})|([A-HJ-Z]{1}(([DF]{1}[A-HJ-NP-Z0-9]{1}[0-9]{4})|([0-9]{5}[DF]{1})))|([A-HJ-Z]{1}[A-D0-9]{1}[0-9]{3}警)))|([0-9]{6}使)|((([沪粤川云桂鄂陕蒙藏黑辽渝]{1}A)|鲁B|闽D|蒙E|蒙H)[0-9]{4}领)|(WJ[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼·•]{1}[0-9]{4}[TDSHBXJ0-9]{1})|([VKHBSLJNGCE]{1}[A-DJ-PR-TVY]{1}[0-9]{5})";


    /**
     *
     */
    public static final String REAL_NAME = "^([\\u4e00-\\u9fa5·]{1,20}|[a-zA-Z\\.\\s]{1,20})$";

    /**
     * 交易金额校验
     */
    public static final String TRAN_AMOUNT  = "^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$";

    /**
     * 验证开票金额 可以为负数
     */
    // public static final String INVOICE_AMOUNT_REGEX ="^(-?[0-9]+)(.[0-9]{0,2})?$";
    public static final String INVOICE_AMOUNT_REGEX ="^((-?[1-9]{1}\\d*)|(-?[0]{1}))(\\.(\\d){0,2})?$";
    /**
     * 用户名
     */
    public static final String USER_NAME = "^[\\u4e00-\\u9fa5\\da-zA-Z]{2,10}$";


    /**
     * 用户名校验
     * @param userName
     * @return
     */
    public static boolean isUserName(String userName) {
        return Regular(userName,USER_NAME) ;
    }

////------------------验证方法
    /**
     * 判断字段是否为空 符合返回ture
     * @param str
     * @return boolean
     */
    public static synchronized boolean StrisNull(String str) {
        return null == str || str.trim().length() <= 0 ? true : false ;
    }
    /**
     * 判断字段是非空 符合返回ture
     * @param str
     * @return boolean
     */
    public static  boolean StrNotNull(String str) {
        return !StrisNull(str) ;
    }
    /**
     * 字符串null转空
     * @param str
     * @return boolean
     */
    public static  String nulltoStr(String str) {
        return StrisNull(str)?"":str;
    }
    /**
     * 字符串null赋值默认值
     * @param str    目标字符串
     * @param defaut 默认值
     * @return String
     */
    public static  String nulltoStr(String str,String defaut) {
        return StrisNull(str)?defaut:str;
    }
    /**
     * 判断字段是否为Email 符合返回ture
     * @param str
     * @return boolean
     */
    public static  boolean isEmail(String str) {
        return Regular(str,EMAIL);
    }
    /**
     * 判断是否为电话号码 符合返回ture
     * @param str
     * @return boolean
     */
    public static  boolean isPhone(String str) {
        return Regular(str,PHONE);
    }
    /**
     * 判断是否为手机号码 符合返回ture
     * @param str
     * @return boolean
     */
    public static  boolean isMobile(String str) {
        return Regular(str,SIMPLE_MOBILE);
    }
    /**
     * 判断是否为手机号码 符合返回ture
     * @param str
     * @return boolean
     */
    public static  boolean isSimpleMobile(String str) {
        return Regular(str,SIMPLE_MOBILE);
    }

    /**
     * 判断是否为Url 符合返回ture
     * @param str
     * @return boolean
     */
    public static  boolean isUrl(String str) {
        return Regular(str,URL);
    }
    /**
     * 判断字段是否为数字 正负整数 正负浮点数 符合返回ture
     * @param str
     * @return boolean
     */
    public static  boolean isNumber(String str) {
        return Regular(str,NUMBER);
    }


    /**
     * 判断字段是否为INTEGER  符合返回ture
     * @param str
     * @return boolean
     */
    public static  boolean isInteger(String str) {
        return Regular(str,INTEGER);
    }
    /**
     * 判断字段是否为正整数正则表达式 >=0 符合返回ture
     * @param str
     * @return boolean
     */
    public static  boolean isINTEGER_NEGATIVE(String str) {
        return Regular(str,INTEGER_NEGATIVE);
    }
    /**
     * 判断字段是否为负整数正则表达式 <=0 符合返回ture
     * @param str
     * @return boolean
     */
    public static  boolean isINTEGER_POSITIVE(String str) {
        return Regular(str,INTEGER_POSITIVE);
    }
    /**
     * 判断字段是否为DOUBLE 符合返回ture
     * @param str
     * @return boolean
     */
    public static  boolean isDouble(String str) {
        return Regular(str,DOUBLE);
    }

    /**
     * 判断字段是否为正浮点数正则表达式 >=0 符合返回ture
     * @param str
     * @return boolean
     */
    public static  boolean isDOUBLE_NEGATIVE(String str) {
        return Regular(str,DOUBLE_NEGATIVE);
    }
    /**
     * 判断字段是否为负浮点数正则表达式 <=0 符合返回ture
     * @param str
     * @return boolean
     */
    public static  boolean isDOUBLE_POSITIVE(String str) {
        return Regular(str,DOUBLE_POSITIVE);
    }
    /**
     * 判断字段是否为日期 符合返回ture
     * @param str
     * @return boolean
     */
    public static  boolean isDate(String str) {
        return Regular(str,DATE_ALL);
    }
    /**
     * 验证2010-12-10
     * @param str
     * @return
     */
    public static  boolean isDate1(String str) {
        return Regular(str,DATE_FORMAT1);
    }
    /**
     * 判断字段是否为年龄 符合返回ture
     * @param str
     * @return boolean
     */
    public static  boolean isAge(String str) {
        return Regular(str,AGE) ;
    }
    /**
     * 判断字段是否超长
     * 字串为空返回fasle, 超过长度{leng}返回ture 反之返回false
     * @param str
     * @param leng
     * @return boolean
     */
    public static  boolean isLengOut(String str,int leng) {
        return StrisNull(str)?false:str.trim().length() > leng ;
    }
    /**
     * 判断字段是否为身份证 符合返回ture
     * @param str
     * @return boolean
     */
    public static  boolean isIdCard(String str) {
        if(StrisNull(str)) return false ;
        if(str.trim().length() == 15 || str.trim().length() == 18) {
            return Regular(str,IDCARD);
        }else {
            return false ;
        }

    }
    /**
     * 判断字段是否为邮编 符合返回ture
     * @param str
     * @return boolean
     */
    public static  boolean isCode(String str) {
        return Regular(str,CODE) ;
    }
    /**
     * 判断字符串是不是全部是英文字母
     * @param str
     * @return boolean
     */
    public static boolean isEnglish(String str) {
        return Regular(str,STR_ENG) ;
    }
    /**
     * 判断字符串是不是全部是英文字母+数字
     * @param str
     * @return boolean
     */
    public static boolean isENG_NUM(String str) {
        return Regular(str,STR_ENG_NUM) ;
    }
    /**
     * 判断字符串是不是全部是英文字母+数字+下划线
     * @param str
     * @return boolean
     */
    public static boolean isENG_NUM_(String str) {
        return Regular(str,STR_ENG_NUM_) ;
    }
    /**
     * 过滤特殊字符串 返回过滤后的字符串
     * @param str
     * @return boolean
     */
    public static  String filterStr(String str) {
        Pattern p = Pattern.compile(STR_SPECIAL);
        Matcher m = p.matcher(str);
        return   m.replaceAll("").trim();
    }


    /**
     * 校验机构代码格式
     * @return
     */
    public static boolean isJigouCode(String str){
        return Regular(str,JIGOU_CODE) ;
    }

    /**
     * 判断字符串是不是数字组成
     * @param str
     * @return boolean
     */
    public static boolean isSTR_NUM(String str) {
        return Regular(str,STR_NUM) ;
    }

    /**
     * 匹配是否符合正则表达式pattern 匹配返回true
     * @param str 匹配的字符串
     * @param pattern 匹配模式
     * @return boolean
     */
    private static  boolean Regular(String str,String pattern){
        if(null == str || str.trim().length()<=0)
            return false;
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 车牌号校验
     * @param plateNumber
     * @return
     */
    public static boolean isPlateNumber(String plateNumber) {
        return Regular(plateNumber,PLATE_NUMBER) ;
    }


    public static void main(String[] args) {
        boolean plateNumber = isPlateNumber("鲁H65K00");
        System.out.println(plateNumber);
    }
    /**
     * 校验密码
     * @param pwd
     * @return
     */
    public static Boolean validatePwd(String pwd,String pattern) {
        return Regular(pwd,pattern) ;
    }
    /**
     * 姓名
     *1.可以是中文
     *2.可以是英文，允许输入点（英文名字中的那种点）， 允许输入空格
     *3.中文和英文不能同时出现
     *4.长度在20个字符以内
     * @param name
     * @return
     */
    public static Boolean validateName(String name) {
        return Regular(name,REAL_NAME) ;
    }


    /**
     * 判断字符串中是否包含中文
     * @param str
     * 待校验字符串
     * @return 是否为中文
     * @warn 不能校验是否为中文标点符号
     */
    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    /**
     * 交易金额格式校验
     *
     * @param tranAmount
     * @return
     */
    public static Boolean validateTranAmount(String tranAmount) {
        return Regular(tranAmount, TRAN_AMOUNT);
    }


    /**
     *  验证机构编码
     * @param organCode
     * @return
     */
    public static boolean validateOrganCode(String organCode){
        return Regular(organCode, ORGAN_CODE);
    }


    /**
     * 发票金额格式校验 可以为负数
     *
     * @param tranAmount
     * @return
     */
    public static Boolean validateInvoiceAmount(String tranAmount) {
        return Regular(tranAmount, INVOICE_AMOUNT_REGEX);
    }

    /**
     * 校验是否存在特殊符号
     *
     * @param str
     * @return
     */
    public static Boolean specialNumeric(String str) {
        if (StringUtils.isBlank(str)) {
            return false;
        }
        Pattern p = Pattern.compile(SPECIAL_NUMERIC);
        Matcher m = p.matcher(str);
        return m.find();
    }


    private static Validator validator;

    static {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    /**
     * 校验对象
     * @param object        待校验对象
     * @param groups        待校验的组
     * @throws BizException  校验不通过，则报BizException异常
     */
    public static void validateEntity(Object object, Class<?>... groups)
            throws BizException {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, groups);
        if (!constraintViolations.isEmpty()) {
            StringBuilder msg = new StringBuilder();
            for(ConstraintViolation<Object> constraint:  constraintViolations){
                msg.append(constraint.getMessage()).append("<br>");
            }
            throw new BizException(CommonCode.DATA_LOGIC_ERROR_CODE.getCode(), msg.toString());
        }
    }
}
