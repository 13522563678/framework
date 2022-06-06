package com.kcwl.ddd.infrastructure.constants;

/**
 * redis数据库中所有的key变量
 *
 * 所有的静态变量后面有“_”表示前缀，如果没有则表示它为一个key
 * @author  kwj
 */
public class PrefixConstant {
    public static final  String COOKIE_TOKEN = "token";

    public static final String REQUEST_USER_INFO = "user_info";

    //司机登录前缀
    public static final  String REDIS_CARRIER_LOGIN_TOKEN = "carrier_login_";

    //货主登录前缀
    public static final  String REDIS_SHIPPER_LOGIN_TOKEN = "shipper_login_";

    //客商登录前缀
    public static final  String REDIS_KS_LOGIN_TOKEN = "KS:LOGIN:SSID:";

    //煤矿人员登录前缀
    public static final String REDIS_COALMINE_LOGIN_TOKEN = "coalmine_login_";

    // 大屏疫情用户登录前缀
    public static final  String REDIS_EPIDEMIC_LOGIN_TOKEN = "epidemic:login:";

    //货主登录人所属公司信息
    public static final  String REDIS_SHIPPER_COMPANY = "shipper_company_";

    //煤矿人员所属煤矿信息
    public static final String REDIS_MINER_COALMINE = "miner_coalmine_";

    //友盟token前缀
    public static final String REDIS_UMENG_TOKEN = "umeng_token_";

    //快成盒子扫码标识前缀
    public static final String QR_SCAN = "qr_scan_";
    //快成盒子自动登录前缀
    public static final String BOX_WAPCLIENT = "kcwl_box_wapclient_";
    //快成盒子扫码登录wapclient前缀
    public static final String QR_WAPCLIENT = "qr_wapclient_";


    /**短信服务器**/
    public static final String SMS_SERVER = "sms_server" ;

    //cookie 类型  ios android pc
    public static final  String COOKIE_TYPE = "token_type";

    //sessionId
    public static final  String COOKIE_SESSIONID = "sessionId";

    // tenantId
    public static final String COOKIE_TENANTID = "tenantId";



    //发送盒子车主认证短信验证码的前缀   司机端
    public static final String REDIS_VERIFY_BOX_CARRIER = "verify_box_carrier_";
    //发送短信验证码的前缀   司机端
    public static final String REDIS_VERIFY_CARRIER = "verify_carrier_";
    //发送短信验证码的前缀   web端
    public static final String REDIS_CARRIER_VALIDATE = "carrier_validate:";
    //修改密码令牌   司机端
    public static final String REDIS_VERIFY_CARRIER_TOKEN = "verify_carrier_token_";

    //图像验证码
    public static final String VALIDATE_CODE = "validateCode:";

    //发送短信验证码的前缀   货主端
    public static final String REDIS_VERIFY_SHIPPER = "verify_shipper_";
    //发送短信验证码的前缀   运销部拨款
    public static final String REDIS_VERIFY_ALLOCATION = "verify_allocation_";
    //修改密码令牌   货主端
    public static final String REDIS_VERIFY_SHIPPER_TOKEN = "verify_shipper_token_";

    //修改支付密码令牌   货主端
    public static final String REDIS_VERIFY_SHIPPER_IDCARDNO = "verify_shipper_idCardNO_";

    //修改支付密码令牌   司机端
    public static final String REDIS_VERIFY_CARRIER_IDCARDNO = "verify_carrier_idCardNO_";


    //当前轨迹点
    public static final String CURRENT_TRACK = "current_track_";

    //V2.8.1 司机当前位置
    public static final String DRIVER_LOCATION = "driver_location_";

    public static  final String ABC_RECHARGE_BALANCE="abc_recharge_balance_";
    //用户输入密码错误次数
    public static final String USER_PASSWORD_ERROR="user:pwdError:";

    //csrf token
    public static final  String REDIS_USER_CSRF_TOKEN = "user:csrftoken:";
    // 疫情弹窗
    public static final  String REDIS_EPIDEMIC_POP_TYPE = "epidemic:poptype:";

    // 用户隐私号
    public static final  String REDIS_USER_SECRECY_MOBILE = "user:secrecymobile:";

    //CRM登录前缀
    public static final  String REDIS_CRM_LOGIN_TOKEN = "crm:ssid:";

    public static final String REDIS_SMS_VERIFY_CODE = "sms:verifycode:";

}
