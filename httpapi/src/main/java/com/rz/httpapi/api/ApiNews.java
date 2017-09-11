package com.rz.httpapi.api;

/**
 * Created by rzw2 on 2017/9/1.
 * 消息接口地址
 */

public class ApiNews {

    public static final int NEWS_ANNOUNCEMENT = 1;
    public static final int NEWS_SYSTEM = 2;
    public static final int NEWS_INTERACTIVE = 3;
    public static final int NEWS_RECOMMEND = 4;
    public static final int NEWS_ACCOUNT = 5;

    public static final int NEWS_COMMENT = 100;
    public static final int NEWS_ANSWER = 101;
    public static final int NEWS_GROUP = 102;
    public static final int NEWS_ACTIVITY = 103;

    public static final String NEWS_UNREAD = "v3/message/getUnread";

    public static final String NEWS_MULTI_LIST = "v3/message/getList";


}
