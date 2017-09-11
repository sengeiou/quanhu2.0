package com.rz.httpapi.api;

/**
 * Created by rzw2 on 2017/9/1.
 * 消息接口地址
 */

public class ApiNews {

    public static final int NEWS_ANNOUNCEMENT = 1;
    public static final int NEWS_SYSTEM = 2;
    public static final int NEWS_INTERACTIVE = 4;
    public static final int NEWS_RECOMMEND = 5;
    public static final int NEWS_ACCOUNT = 3;

    public static final int NEWS_COMMENT = 4001;
    public static final int NEWS_ANSWER = 4002;
    public static final int NEWS_GROUP = 4003;
    public static final int NEWS_ACTIVITY = 4004;

    static final String NEWS_UNREAD = "v3/message/getUnread";

    static final String NEWS_MULTI_LIST = "v3/message/getList";

    static final String NEWS_OVERVIEW = "v3/message/getMessageCommon";
}
