package com.rz.common.constant;

import android.graphics.Color;
import android.os.Environment;

/**
 * 常量
 */
public class Constants {
    /**
     * 关注圈子名字
     */
    public static final String FOLLOW = "FOLLOW";
    public static final String FOLLOW_ID = "FOLLOW_ID";
    public static final String UPDATE_LOVE_CIRCLE = "UPDATE_LOVE_CIRCLE";
    public static final String HOME_FRAGMENT_CACHE = "HOME_FRAGMENT_CACHE";
    public static final String BANNER_CACHE = "BANNER_CACHE";

    /**
     * 设置关注
     */
    public static final String FOLLOW_DATA = "follow_data";
    /**
     * 默认数值
     */
    public static final int DEFAULTVALUE = -1;

    /**
     * 免密支付额度，单位分
     */
    public static final double EasyPayMoney = 5000;

    /**
     * 转发金额 单位元
     */
    public static final double FORWARD_MONEY = 3;

    /**
     * 每页新闻内容的数据是20条
     */
    public static final int PAGESIZE = 20;

    public static final int PAGESIZE_MAX = 100;
    /**
     * 倒计时常量 单位秒
     */
    public static final int COUNTDOWN = 90;
    /**
     * 录音限制时长
     */
    public static final int MAX_INTERVAL_TIME = 120;

    public static int AD_DAY_COST = 30;

    /**
     * 当前 DEMO 应用的回调页，第三方应用可以使用自己的回调页。
     * <p/>
     * <p>
     * 注：关于授权回调页对移动客户端应用来说对用户是不可见的，所以定义为何种形式都将不影响， 但是没有定义将无法使用 SDK 认证登录。
     * 建议使用默认回调页：https://api.weibo.com/oauth2/default.html
     * </p>
     */
    public static final String REDIRECT_URL = "http://sns.whalecloud.com";

    /**
     * Scope 是 OAuth2.0 授权机制中 authorize 接口的一个参数。通过 Scope，平台将开放更多的微博
     * 核心功能给开发者，同时也加强用户隐私保护，提升了用户体验，用户在新 OAuth2.0 授权页中有权利 选择赋予应用的功能。
     * <p/>
     * 我们通过新浪微博开放平台-->管理中心-->我的应用-->接口管理处，能看到我们目前已有哪些接口的 使用权限，高级权限需要进行申请。
     * <p/>
     * 目前 Scope 支持传入多个 Scope 权限，用逗号分隔。
     * <p/>
     * 有关哪些 OpenAPI 需要权限申请，请查看：http://open.weibo.com/wiki/%E5%BE%AE%E5%8D%9AAPI
     * 关于 Scope 概念及注意事项，请查看：http://open.weibo.com/wiki/Scope
     */
    public static final String SCOPE = "email,direct_messages_read,direct_messages_write,"
            + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
            + "follow_app_official_microblog," + "invitation_write";

    public static final int PUBLISH_REQUEST = 10;
    public static final int PUBLISH_RESULT = 11;
    public static final int PUBLISH_RESULT_CAMERA = 12;
    public static final int PUBLISH_RESULT_DEL_PIC = 13;

    public static final String CHANNEL_KEY = "UMENG_CHANNEL";

    public static final String TRANSIT_PIC = "picture";
    /**
     * 默认常量
     */
    public static final int DEFAULTVALUES = -1;

    public final static String STORE_PATH = Environment
            .getExternalStorageDirectory().getAbsolutePath()
            + "/openim/images/";

    public static final class Sina {
        public static final String APP_KEY = "584112165";
        public static final String APP_SECRET = "a4f06fcdbab55e46de60ad1bc402657a";
        public static final String REDIRECT_URL = "https://www.quanhu365.com/";
        //        public static final String REDIRECT_URL = "http://sns.whalecloud.com/sina2/callback";
    }

    public static final class QQ {
        public static final String APP_ID = "101419427";
        public static final String APP_KEY = "17fe3acefa3aeaae815d10f0315e9093";
    }

    public static final class WeiXin {
        public static final String APP_ID = "wxd9f7249e4c8382a8";
        public static final String APP_SECRET = "a3270441bda749648f20e4857e32ba97";
    }

    public static final class Bugly {
        public static final String APP_ID = "d2a9cccb9f";
        public static final String APP_KEY = "241d5838-1e23-4a5a-9a51-d1c0e34c2986";
    }

    public static final int[] COLOR_SCHEMES = {
            Color.parseColor("#ff33b5e5"),
            Color.parseColor("#ffff4444"),
            Color.parseColor("#ffffbb33"),
            Color.parseColor("#ff99cc00"),
    };

    /**
     * 表示首页推荐栏目的栏目id
     */
    public static final String COMMEND_ID = "100asgd";

    /**
     * 微信APPId
     */
    public static final String APP_ID = "wxf7ece255b91036cd";

    /**
     * 5块钱的手续费 单位分
     */
    public static final String PAY_MONEY = "500";
    /**
     * 文件服务器防盗链
     */
    public static final String KEY = "?key=2b8592675d7ce779e376dd5f7d001e8f";
    /**
     * 拍照显示图片
     */
    public static final int REQUEST_CODE_TAKE_PICTURE = 1;
    /**
     * 从手机上选择图片显示
     */
    public static final int REQUEST_CODE_TAKE_ALBUM = 0;
    /**
     * 选取照片后进行裁剪
     */
    public static final int CHOOSETRUE = 2;

    /**
     * 首页fragment加载倒计时 单位秒
     */
    public static final int PAGELOAD = 1;


    /**
     * 是否设置了手势密码 设置了就表示从首页跳到手势密码界面验证
     */
    public static final int HOM_PAGE_TO_GESTURE = 1;

    /**
     * 退出界面
     */
    public static final int JUMP_OUT = 1;

    /**
     * 首页面栏目请求code channelAty
     */
    public static final int CHANNELREQUEST = 1;

    /**
     * 添加好友好刷新通讯录
     */
    public static final int ADDFRIENDREQUEST = 10000;

    /**
     * 添加好友好刷新通讯录
     */
    public static final int ADDFRIENDRESULT = 10001;

    /**
     * 首页面栏目返回code channelAty
     */
    public static final int CHANNELRESULT = 2;

    /**
     * 拉黑
     */
    public static final int COME_BLACK = 2;
    /**
     * 取消关注
     */
    public static final int CANCEL_ASTTENTION = 3;
    /**
     * 加关注
     */
    public static final int ADD_ASTTENTION = 4;

    /**
     * 详情里面付费请求code
     */
    public static final int DETAIL_REQUEST_CODE = 1;
    /**
     * 阅读付费成功回掉 code
     */
    public static final int READ_RESULT_CODE = 2;
    /**
     * 转发付费成功回掉 code
     */
    public static final int FORWARD_RESULT_CODE = 4;

    /**
     * 跳到获取银行卡界面请求code
     */
    public static final int CARD_REQUEST_CODE = 3;
    /**
     * 跳到获取银行卡界面返回code
     */
    public static final int CARD_RESULT_CODE = 4;

    /**
     * 账户安全界面跳入安全问题设置界面
     */
    public static final int JUMPTOSAFE_REQUEST = 3;

    /**
     * 账户安全界面跳入安全问题设置界面
     */
    public static final int JUMPTOSAFE_RESULT = 4;

    /**
     * 已实名认证 -找回支付密码
     */
    public static final int FIND_PAY_PW = 100;

    /**
     * 从付款界面跳入GetAllBankAty银行卡界面
     */
    public static final int PAYATY2GETALLBANK = 200;

    /**
     * 微信支付请求code
     */
    public static final int WXPAY_REQUEST_CODE = 123;
    /**
     * 微信支付返回resultcode
     */
    public static final int WXPAY_RESULT_CODE = 321;

    /**
     * 启动service的入口标识
     */
    public static final String START_SERVICE_FLAG = "start_service_flag";
    /**
     * 启动service的入口标识:版本更新
     */
    public static final String START_SERVICE_VERSION = "start_service_lversion";

    /**
     * 当前 DEMO 应用的 APP_KEY，第三方应用应该使用自己的 APP_KEY 替换该 APP_KEY
     */
    public static final String APP_KEY = "277504927";

    /**
     * 当前 DEMO 应用的回调页，第三方应用可以使用自己的回调页。
     * <p/>
     * <p>
     * 注：关于授权回调页对移动客户端应用来说对用户是不可见的，所以定义为何种形式都将不影响， 但是没有定义将无法使用 SDK 认证登录。
     * 建议使用默认回调页：https://api.weibo.com/oauth2/default.html
     * </p>
     */
//无网络时用户访问的客服地址
    public static final String CUSTOMER_SERVICE = "customer_service";
    public static final String Lottery_Tag = "yryz_lottery_open";

    public static final String MSG_TAB_REFRESH = "msg_tab_refresh";

    public static final String CIRCLE_FRAGMENT_REFRESH = "circle_fragment_refresh";

    public static final String LOGIN_IN_SUCCESS = "login_in_success";

    public static final String LOGIN_OUT_SUCCESS = "login_out_success";

    public static final String SWITCH_SHARE_SUCCESS = "switch_share_success";


}
