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
    }

    /**
     * app的基本配置
     */
    interface App {
        String APP_ID = "w2oqkv4yi892";
        String APP_SECRET = "78cba16f947e4c7bb9579196c6126851";
    }
}
