package com.rz.common.constant;

/**
 * 作者：Administrator on 2016/8/20 0020 14:22
 * 功能：状态code，根据code，进行不同操作
 * 说明：
 */
public class CodeStatus {

    /**
     * 通用code
     */
    public static class Gegeneral {
        //数据正在加载
        public static final int DATA_LOADING = 300;
        //没有网络
        public static final int ERROR_NET = 301;
        //数据加载失败
        public static final int ERROR_DATA = 302;
        //数据获取成功，列表有数据
        public static final int DATA_SUCCESS_FULL = 303;
        //数据获取成功，列表没有数据
        public static final int DATA_SUCCESS_NULL = 304;
        //操作失败
        public static final int ACTION_FAIL = 999;
        //操作成功
        public static final int ACTION_SUCCESS = 1000;
        //当前数据长度没有达到limit
        public static final int NOT_FULL_DATA = 310;
        //没有转发券
        public static final int NO_TICKET_MSG = 305;
        /**
         * web页找不到
         */
        public static final int WEB_ERROR = 160;
    }

    /**
     * 注册
     */
    public static class RegisterCode {
        //验证码发送失败
        public static final int MSG_FAIL = 1001;
        //注册失败
        public static final int REGIST_FAIL = 1002;
    }

    /**
     * 登录
     */
    public static class LoginCode {
        //登录成功
        public static final int LOGIN_FAIL = 1003;
        //登录失败
        public static final int LOGIN_SUCCESS = 1004;
        //第三方登录
        public static final int LOGIN_OTHER = 1005;
    }

    /**
     * 圈子
     */
    public static class CircleCode {
        //获取数据失败
        public static final int CIRCLE_FAIL = 3001;
        //接口调用成功，但没有数据，data为null,向下拉
        public static final int CIRCLE_SUCCESS_NO_DATA_DOWN = 1001;
        //接口调用成功，但没有数据，data为null,向上拉
        public static final int CIRCLE_SUCCESS_NO_DATA_UP = 1002;
        //接口调用成功，有数据
        public static final int CIRCLE_SUCCESS = 1003;
        //用户未登录
        public static final int CIRCLE_LOGIN = 10;
    }


    /**
     * 支付
     */
    public static class PayCode {
        //取消
        public static final int PAY_CANDEL = 1011;
        //支付成功
        public static final int PAY_SUCCESS = 1012;
        //支付失败，支付控件相关
        public static final int PAY_FAIL = 1013;
        //支付异常，包括生成支付订单错误等
        public static final int PAY_ABNORMAL = 1014;
        //支付结果确认中
        public static final int PAY_CONFIRM = 1015;

    }


    /**
     * 个人信息
     */
    public static class PersonInfoCode {
        //取消
        public static final int PERSON_INFO_SUCCESS = 1021;
        public static final int PERSON_INFO_FAIL = 1022;
    }

    /**
     * 免密支付
     */
    public static class EasyPay {
        public static final int OPEN_EASY_PAY_SUCCESS = 1000;

        public static final int CLOASE_EASY_PAY_SUCCESS = 10001;
    }

    /**
     * 去支付
     */
    public static class GoToForward {
        //设置支付密码
        public static final int set_pay_pw = 1000;
    }

    public static class RedBag {
        //加载中
        public static final int REDBAG_LOADING = 1000;

        //加载失败
        public static final int REDBAG_ERROR = 1001;

        //加载成功
        public static final int REDBAG_SUCCESS = 1002;

        //网络错误
        public static final int REDBAG_NET_ERROR = 1003;

        //拆红包成功
        public static final int CHAI_SUCCESS = 1004;
    }

    /**
     * 用户点赞
     */
    public static class UserLike {
        //点赞失败
        public static final int LIKE_ERROR = 200;

        //点赞成功
        public static final int LIKE_SUCCESS = 201;
    }

    /**
     * 用户评论
     */
    public static class UserComment {
        //评论失败
        public static final int Comment_ERROR = 202;

        //评论成功
        public static final int Comment_SUCCESS = 203;

        //回复评论
        public static final int COMMENT_ASK = 199;
    }

    /**
     * 用户删除晒一晒评论
     */
    public static class UserDelComment {
        //删除评论成功，更新界面数据
        public static final int DEL_COMMENT_UPDATE = 204;

        //删除晒一晒评论失败
        public static final int DelComment_ERROR = 205;

        //删除晒一晒评论成功
        public static final int DelComment_SUCCESS = 206;

        //刪除最后一条晒一晒
        public static final int DEL_LAST_COMMENT = 199;
    }

    /**
     * 用户删除晒一晒
     */
    public static class UserDelShine {
        //删除晒一晒评败
        public static final int DelShine_ERROR = 207;

        //删除晒一晒评论成功
        public static final int DelShine_SUCCESS = 208;

        //删除晒一晒成功，并更新界面
        public static final int DelShine_SUCCESS_update = 209;
    }

    /**
     * 用户评论列表
     */
    public static class UserCommentList {
        //评论列表失败
        public static final int CommentLis_ERROR = 210;

        //评论列表成功
        public static final int CommentLis_SUCCESS = 211;

        //暂无更多评论列表
        public static final int COMMENTLIS_NO_MORE = 212;

        //暂无评论列表
        public static final int COMMENTLIS_NULL = 213;
    }

    /**
     * 获得点赞列表
     */
    public static class getAllZan {
        //获得点赞失败
        public static final int all_zan_fail = 214;
        //点赞列表为空
        public static final int all_zan_null = 215;
        //点赞列表成功
        public static final int all_zan_success = 216;
    }

    /**
     * 获得礼物列表
     */
    public static class getAllGift {
        //获得礼物失败
        public static final int all_gift_fail = 217;
        //礼物列表为空
        public static final int all_gift_null = 218;
        //礼物列表成功
        public static final int all_gift_success = 219;
    }

    /**
     * 获得评论列表
     */
    public static class getAllComment {
        //获得评论失败
        public static final int all_comment_fail = 220;
        //评论列表为空
        public static final int all_comment_null = 221;
        //评论列表成功
        public static final int all_comment_success = 222;
    }

    /**
     * 加好友类型
     */
    public static class requireFriend {
        //申请加好友
        public static final int require_type_add = 1;
        //同意加好友
        public static final int require_type_agree = 2;
        //拒绝加好友
        public static final int require_type_refuse = 3;
    }

    /**
     * 加好友状态
     */
    public static class requireFriendStatus {
        //申请加好友
        public static final int require_status_wait = 0;
        //同意加好友
        public static final int require_status_agree = 1;
        //拒绝加好友
        public static final int require_status_refuse = 2;
        //是申请发起方
        public static final int is_require_true = 1;
        //否申请发起方
        public static final int is_require_false = 0;
    }

    /**
     * 删除申请好友or好友信息
     */
    public static class deteleRequire {
        //删除申请好友信息
        public static final int delete_require_friend = 226;
        //删除好友
        public static final int delete_friend = 227;
    }
}
