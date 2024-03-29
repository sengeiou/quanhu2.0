package com.rz.common.constant;

/**
 * 接口返回数据 类别标识
 */
public interface Type {
    /**
     * 重置支付密码
     */
    public static int RESET_PAY_PW = 2;

    /**
     * 设置了支付密码
     */
    int HAD_SET_PW = 1;

    /**
     * 没有支付密码
     */
    int HAD_NO_SET_PW = 0;
    /**
     * 功能码--找回支付密码
     */
   String FUNCTION_CODE_6 = "6";

    /**
     * 开启了免密支付
     */
    int OPEN_EASY_PAY = 1;
    /**
     * 没有开启了免密支付
     */
    public static int CLOSE_EASY_PAY = 0;
    //收益流水
    public static final int TYPE_SCORE = 2;
    //消费流水
    int TYPE_BALANCE = 1;
    /**
     * 红包记录
     */
    int TYPE_RECORD_BAG = 6;
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
     * 从圈子跳转登录
     */
    public static final int TYPE_LOGIN_WEB = 5;
    /**
     * 从向导跳转登录
     */
    public static final int TYPE_LOGIN_GUIDE = 9;
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

    /**
     * 功能码 -- 通用
     */
    public static String FUNCTION_CODE_8 ="8";

    /**
     * 转发券
     */

    /**
     * 功能码 -- 登录
     */
    public static String FUNCTION_CODE_9 = "9";


    int TYPE_TICKET = 12;


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

    /**
     * 用户账户余额正常
     */
    public static final int USER_MONEY_NORMAL = 1;

    /**
     * 所有的
     */
    public static final int custType_0 = 0;
    /**
     * 我关注的
     */
    public static final int custType_1 = 1;
    /**
     * 关注我的
     */
    public static final int custType_2 = 2;
    /**
     * 互相关注
     */
    public static final int custType_3 = 3;
    /**
     * 黑名单
     */
    public static final int custType_4 = 4;
    /**
     * 平台陌生人
     */
    public static final int custType_5 = 5;


    /**
     * 好友关系
     */
    public static final int relation_friend = 1;

    /**
     * 陌生人
     */
    public static final int relation_stranger = 0;


    /**
     * 微信支付
     */
    public static final String TYPE_WX_PAY = "4";

    /**
     * 支付宝支付
     */
    public static final String TYPE_ALI_PAY = "3";


}
