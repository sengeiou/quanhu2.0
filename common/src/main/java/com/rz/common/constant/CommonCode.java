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
    }

    /**
     * app的基本配置
     */
    interface App {
        String APP_ID = "w2oqkv4yi892";
        String APP_SECRET = "78cba16f947e4c7bb9579196c6126851";
    }

    interface EventType {
        int FINISH_LOADING = 10001;

        int EDITOR_PIC_DELETE = 10002;
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
        public static final int COUNTDOWN = 90;
        /**
         * 录音限制时长
         */
        public static final int MAX_INTERVAL_TIME = 120;

        public static int RECORDE_SHOW = 10001;

        public static int RECORDE_OPUS_VIDEO = 10002;
    }


}
