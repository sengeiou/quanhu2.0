package com.rz.circled.constants;

/**
 * Created by rzw2 on 2017/9/5.
 */

public class JPushTypeConstants {
    public static final int COLTYPE_SYSTEM = 1;

    public static final int COLTYPE_SAFE = 2;

    public static final int COLTYPE_MY_DYNAMIC = 3;

    public static final int COLTYPE_FRIEND_DYNAMIC = 4;

    /****************************
     * 我的动态 colType=3
     ****************************/
    //申请加好友
    public static final int APPLY_FOR_ADD_FRIEND = 3001;
    //邀请我转发
    public static final int INVITE_TRANSFER_MINE = 3002;
    //有人跟转我
    public static final int SOMEBODY_WITH_THE_RUN = 3003;
    //有人打赏我(随手晒)
    public static final int SOMEBODY_PLAY_ME_SHOW = 3004;
    //有人打赏我（转发）
    public static final int SOMEBODY_PLAY_ME_TRANSFER = 3005;
    //新提问
    public static final int NEW_ASK = 3006;
    //新问答
    public static final int NEW_ANSWER = 3007;

    /****************************
     * 账户变动与安全 colType=2
     ****************************/
    //转发支付成功
    public static final int TRANSFER_PLAY_SUCCESS = 2001;
    //打赏支付成功
    public static final int PLAY_SUCCESS = 2002;
    //红包支付成功
    public static final int LUCKY_PAY_SUCCESS = 2003;
    //充值成功
    public static final int RECHARGE_SUCCESS = 2004;
    //红包到期退款
    public static final int LUCKY_EXPIRED = 2005;
    //积分兑换成功
    public static final int INTEGRAL_EXCHARGE_SUCCESS = 2006;
    //提现成功
    public static final int WITHDRAW_SUCCESS = 2007;
    //登录密码提醒
    public static final int LOGIN_PASSWORD_CHANGE = 2008;
    //支付密码提醒
    public static final int PAY_PASSWORD_CHANGE = 2009;

    /****************************
     * 好友动态 colType=4
     ****************************/
    //好友发布悠秀
    public static final int FRIEND_PUBLISH_SHOW = 4001;
    //好友转发内容
    public static final int FRIEND_TRANSFER = 4002;

    /****************************
     * 系统消息 colType=1
     ****************************/
    //中奖
    public static final int LOTTERY_WINNING = 1001;
    //参与抽奖 没中
    public static final int LOTTERY_NO_WINNING = 1002;
    //审核通过
    public static final int VERIFY_PASS = 1003;
    //审核通过
    public static final int VERIFY_REFUSE = 1004;
    //活动
    public static final int SYSTEM_ACTIVITY = 1005;
    //下架
    public static final int OFF_THE_SHELF = 1006;
    //警告
    public static final int WARNING = 1007;
}
