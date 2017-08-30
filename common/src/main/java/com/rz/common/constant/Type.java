package com.rz.common.constant;

/**
 * 接口返回数据 类别标识
 */
public interface Type {
    /**
     * 视频
     */
    int TYPE_VIDEO = 0;
    /**
     * 音频
     */
    int TYPE_AUDIO = 1;

    /**
     * 第三方QQ登录
     */
    public static final int LOGIN_QQ = 3;
    /**
     * 第三方微信登录
     */
    public static final int LOGIN_WX = 1;
    /**
     * 第三方新浪微博登录
     */
    public static final int LOGIN_SINA = 2;
    /**
     * 手机号登录
     */
    public static final int LOGIN_PHONE = 4;
    /**
     * 第三方绑定
     */
    public static int ACTION_BIND = 0;
    //页面
    public static final String KEY_PAGE = "key_page";
    //注册成功返回码
    public static final int REGISTER_RESULT_CODE = 1002;
    /**
     * 默认数值
     */
    public static final int DEFAULTVALUE = -1;
    /**
     * 倒计时常量 单位秒
     */
    public static final int COUNTDOWN = 90;
    /**
     * 短信验证码
     */
    public static String VERIFY_CODE = "1";
    /**
     * 功能码--注册
     */
    public static String FUNCTION_CODE_1 = "1";

    /**
     * 功能码--找回密码
     */
    public static String FUNCTION_CODE_2 = "2";
    /**
     * 功能码--更换手机
     */
    public static String FUNCTION_CODE_5 = "5";


    interface JpushMessageType {

        int COLTYPE_USER_SAFE = 2;
        int COLTYPE_SYSTEM_INFORMATION = 5;

        /****************************
         * 系统消息 colType=5
         ****************************/
        //申请通过
        int APPLY_FOR_PASS = 5001;
        //申请不通过
        int APPLY_FOR_REFUSE = 5002;
        //资料被打回
        int INFORMATION_IS_INCORRECT = 5003;
        //后台已发货
        int SHIPPED = 5004;
        //提前三天
        int REPAYMENT_LAST_THREE_DAY = 5005;
        //冻结超过三天
        int FREEZE_EXCEED_THREE_DAY = 5006;
        //逾期后一天提醒
        int OVERDUE_ONE_DAY = 5007;
        //手动、自动解冻后通知
        int UNFREEZE = 5008;
        //审核通过
        int REVIEW_PASS = 5009;
        //资料被打回
        int REVIEW_REJECT = 5010;
        //审核不通过
        int REVIEW_REFUSE = 5011;

    }

}
