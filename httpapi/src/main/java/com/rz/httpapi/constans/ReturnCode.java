package com.rz.httpapi.constans;

/**
 * 接口数据返回类型
 */
public class ReturnCode {

    //成功
    public static final int SUCCESS = 1;

    //失败-后台提醒
    public static final int FAIL_REMIND_1 = 2;

    //失败-前端提醒
    public static final int FAIL_REMIND_2 = 3;

    //用户过期
    public static final int USER_EXPIRE = 4;

    //用户黑名单
    public static final int USER_BLACK = 5;

}
