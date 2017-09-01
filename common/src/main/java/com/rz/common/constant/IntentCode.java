package com.rz.common.constant;

/**
 * 作者：Administrator on 2016/9/5 0005 15:53
 * 功能：
 * 说明：
 */
public class IntentCode {

    /**
     * 通用code
     */
    public static class General {

        //退出
        public static final int APP_EXIT = 156;

        //返回首頁
        public static final int BACK_HOME = 157;

        //返回首頁
        public static final int BACK_MESSAGE = 158;
        //返回发现
        public static final int BACK_FIND = 159;
    }

    /**
     * 登录code
     */
    public static class Login {
        //登录请求code
        public static final int LOGIN_REQUEST_CODE = 1000;

        //处理登录成功返回code
        public static final int LOGIN_RESULT_CODE = 1001;
    }

    /**
     * 注册code
     */
    public static class Register {
        //注册成功返回码
        public static final int REGISTER_RESULT_CODE = 1002;
        //注册成功返回码
        public static final int REGISTER_RESULT_ONLY_CODE = 1003;
    }

    /**
     * 忘记密码code
     */
    public static class ForGetPw {
        //忘记密码界面
        public static final int FIND_1_REQUEST_CODE = 1000;
        //忘记密码界面
        public static final int FIND_1_RESULT_CODE = 1001;
    }

    /**
     * 我的界面
     */
    public static class MineFrg {
        //请求code
        public static final int MINE_REQUEST_CODE = 800;
    }

    /**
     * 设置界面
     */
    public static class Setting {
        //返回code
        public static final int SETTING_RESULT_CODE = 8001;
    }

    /**
     * 公告界面
     */
    public static class Notice {
        //返回code
        public static final int NOTICE_RESULT_CODE = 9001;
    }

    public static final int PAGE_CODE_BANNER = 10001;
    public static final int PAGE_ADDFRIEND = 100009;

    /**
     * 充值
     */
    public static class RechargeMoney {
        //请求code
        public static final int RECHARGE_REQUEST_CODE = 105;
        //返回code
        public static final int RECHARGE_RESULT_CODE = 106;
    }

    public static class BankCard {
        //请求code
        public static final int BankCard_REQUEST_CODE = 107;
        //返回code
        public static final int BankCard_RESULT_CODE = 108;
        //绑定银行卡请求code
        public static final int BIND_REQUEST_CODE = 109;
        //绑定银行卡返回code
        public static final int BIND_RESULT_CODE = 110;
    }

    /**
     * 晒一晒详情
     */
    public static class Showdetail {
        //晒一晒请求code
        public static final int SHOWDETAIL_REQUEST_CODE = 111;
        //晒一晒返回code
        public static final int SHOWDETAIL_RESULT_CODE = 112;
    }

    /**
     * 通讯录
     */
    public static class Contacts {
        //通讯录请求code
        public static final int Contacts_REQUEST_CODE = 113;
        //通讯录返回code
        public static final int Contacts_RESULT_CODE = 114;
    }

    public static class ShareAty {
        //分享请求code
        public static final int share_request_code = 115;
        //分享返回code
        public static final int share_result_code = 116;
    }

    public static class InviteForward {
        //邀请好友请求code
        public static final int INVITE_REQUEST_CODE = 117;
    }

    public static class MyPromotioin {
        //邀请好友请求code
        public static final int MY_PROMOTION_CODE = 123;
    }

    public static class SafeUserCode {
        //设置账户安全保护信息
        public static final int SAFEUSERCODE_REQUEST_CODE = 118;
        //返回code
        public static final int SAFEUSERCODE_RESULT_CODE = 118;
    }

    //群组创建
    public static class TeamCreate {
        public static final int REQUEST_CODE_NORMAL = 119;
        public static final int REQUEST_CODE_ADVANCED = 120;
    }

    //聊天设置
    public static class MsgSetting {
        public static final int REQUEST_CODE_MSG_SETTING = 121;
    }

    //随手晒分享
    public static class ShowShare {
        public static final int REQUEST_CODE_SHOW_SHARE = 122;
    }
}


