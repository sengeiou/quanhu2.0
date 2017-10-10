package com.rz.common.constant;

/**
 * Created by Gsm on 2017/8/23.
 */
public interface CommonCode {
    /**
     * 数据状态
     */
    interface General {
        /**
         * 当前无网络
         */
        int UN_NETWORK = 110;
        /**
         * 加载中
         */
        int DATA_LOADING = 100;
        /**
         * 加载失败
         */
        int LOAD_ERROR = 120;
        /**
         * 数据为空
         */
        int DATA_EMPTY = 130;
        /**
         * 有数据
         */
        int DATA_SUCCESS = 140;
        /**
         * 数据不满足特定长度
         */
        int DATA_LACK = 150;

        /**
         * web页找不到
         */
        int WEB_ERROR = 160;
        /**
         * 没有消息
         */
        int NEWS_EMPTY = 170;

        /**
         * 数据错误
         */
        int ERROR_DATA = 302;
        /**
         * 支付失败
         */
        int PAY_SUCCESS = 303;
    }

    /**
     * app的基本配置
     */
    interface App {
        String APP_ID = "vebff12m1762";
        String APP_SECRET = "59473d316dc24d0d8c783cbb981b56dc";
    }

    interface EventType {
        int FINISH_LOADING = 10001;

        int EDITOR_PIC_DELETE = 10002;

        int SEARCH_KEYWORD = 20001;

        String CIRCLE_FRAGMENT_REFRESH = "circle_fragment_refresh";

        int PROVE_UPDATE = 30001;

        int TYPE_LOGOUT = 300002;
        int TYPE_LOGIN = 300003;
        int TYPE_REWARD_COUNT = 300004;
        int TYPE_USER_UPDATE = 300005;
        int TYPE_EMPTY_COUNT = 300006;
        int TYPE_SAVE = 300007;
        int TYPE_ADD_LAYOUT = 300008;
        int TYPE_SECOND_LAYOUT = 300009;

        int TYPE_REWARD_REFRESH = 40001;
    }

    /**
     * 一些请求
     */
    interface REQUEST {
        /**
         * 去拍照
         */
        int TAKE_PICTURE = 210;
        /**
         * 选取照片
         */
        int CHOOSE_TRUE = 211;
        /**
         * 公共请求code
         */
        int PUBLISH_REQUEST = 310;

        /**
         * 点击相册选择回调
         */
        int PUBLISH_RESULT = 311;

        /**
         * 照相机回调
         */
        int PUBLISH_RESULT_CAMERA = 312;

        /**
         * 删除相片
         */
        int PUBLISH_RESULT_DEL_PIC = 313;
    }

    interface Constant {
        /**
         * 倒计时常量 单位秒
         */
        int COUNTDOWN = 90;
        /**
         * 录音限制时长
         */
        int MAX_INTERVAL_TIME = 180;

        int RECORDE_SHOW = 10001;

        int RECORDE_OPUS_VIDEO = 10002;

        int PAGE_SIZE = 10;

        int TAB_MAIN_HOME = 1;
    }

    /**
     * 加好友类型
     */
    interface requireFriend {
        //申请加好友
        int require_type_add = 1;
        //同意加好友
        int require_type_agree = 2;
        //拒绝加好友
        int require_type_refuse = 3;
    }

    /**
     * 加好友状态
     */
    interface requireFriendStatus {
        //申请加好友
        int require_status_wait = 0;
        //同意加好友
        int require_status_agree = 1;
        //拒绝加好友
        int require_status_refuse = 2;
        //是申请发起方
        int is_require_true = 1;
        //否申请发起方
        int is_require_false = 0;
    }

    /**
     * 支付
     */
    interface PayCode {
        //取消
        int PAY_CANDEL = 1011;
        //支付成功
        int PAY_SUCCESS = 1012;
        //支付失败，支付控件相关
        int PAY_FAIL = 1013;
        //支付异常，包括生成支付订单错误等
        int PAY_ABNORMAL = 1014;
        //支付结果确认中
        int PAY_CONFIRM = 1015;

    }


}
