package com.rz.httpapi.api;

/**
 * Created by Administrator on 2017/4/13 0013.
 */

public class CircleApi {

    /**
     * 获取圈子入口列表
     */
    public static final String CIRCLE_ENTRANCE_LIST = "v3/circle/getCircleList"; /**
     * 获取喜欢圈子列表
     */
    public static final String FAVORITE_CIRCLE_LIST = "v3/circle/getFavoriteCircle";
    /**
     * 添加喜欢的圈子
     */
    public static final String SAVE_FAVORITE_CIRCLE = "v3/circle/saveFavoriteCircle";
    /**
     * 删除喜欢的圈子
     */
    public static final String REMOVE_FAVORITE_CIRCLE = "v3/circle/removeFavoriteCircle";
    /**
     * 首页banner
     */
    public static final String CIRCLE_BANNER_LIST = "v3/recommend/banner";
    /**
     * 首页热门话题
     */
    public static final String CIRCLE_SUBJECT_LIST = "v3/recommend/topic";
    /**
     * 首页圈子达人
     */
    public static final String CIRCLE_FAMOUS_LIST = "v3/star/starCommend";
    /**
     * 更多达人
     */
    public static final String MORE_FAMOUS_LIST = "v3/star/getAllStar";
    /**
     * 查询圈子转发详情
     */
    public static final String CIRCLE_TRANSFER_DETAIL = "v3/transfer/getTransferDetail";

    /**
     * 首页动态
     */
    public static final String CIRCLE_DYNAMIC_GET = "v3/dynamic/get";

    /**
     * 我的收藏列表
     */
    public static final String CIRCLE_COLLECT_LIST = "v3/collection/list";
    /**
     * 我的收藏列表
     */
    public static final String CIRCLE_DELETE_COLLECT = "v3/collection/delete";

    /**
     * 3.3查询转发列表（我转别人）
     */
    public static final String CIRCLE_TRANSFER_LIST = "v3/transfer/getTransferList";

    /**
     * 3.5查询作品列表（别人转我）
     */
    public static final String CIRCLE_OPUS_LIST = "v3/transfer/getOpus";

    /**
     * 查询用户转发统计
     */
    public static final String CIRCLE_TRANSFER_STATS = "v3/transfer/getStats";

    /**
     * 转发操作
     */
    public static final String TRANSFER = "v3/transfer/transfer";


    /**
     * 评论
     */
    public static final String CIRCLE_COMMENT = "v3/comment/comment";

    /**
     * 评论回复
     */
    public static final String CIRCLE_TRANSFER_COMMENT_REPLY = "v3/comment/reply";


    /**
     * 评论回复删除
     */
    public static final String CIRCLE_COMMENT_DELETE = "v3/comment/delete";


    /**
     * 评论列表
     */
    public static final String CIRCLE_COMMENT_LIST = "v3/comment/commentList";


    /**
     * 评论状态统计
     */
    public static final String CIRCLE_COMMENT_STATS = "v3/comment/commentStat";


    /**
     * 点赞
     */
    public static final String CIRCLE_ZAN = "v3/good/good";


    /**
     * 点赞列表
     */
    public static final String CIRCLE_ZAN_LIST = "v3/good/list";


    /**
     * 点赞状态统计
     */
    public static final String CIRCLE_ZAN_STATS = "v3/good/goodStat";

    /**
     * 圈子广场列表
     */
    public static final String CIRCLE_TRANSFER_SQUARE_LIST = "v3/transfer/getSquareTransfer";

    /**
     * 获取转发价格列表
     */
    public static final String CIRCLE_TRANSFER_GET_PRICE = "v3/transfer/getPrice";
    /**
     * 评论状态
     */
    public static final String CIRCLE_COMMENT_STATUS = "v3/comment/commentStat";
    /**
     * 打赏列表
     */
    public static final String CIRCLE_TRASFER_REWARD_LIST = "v3/transfer/getCust";

    /**
     * 获取跟转用户列表
     */
    public static final String CIRCLE_TRANSFER_GET_CUST = "v3/transfer/getCust";

    /**
     * 用户加入的圈子列表
     */
    public static final String CIRCLE_BY_CUST = "v3/circle/getCircleByCust";

    /**
     * 全局搜索
     */
    public static final String SEARCH_ALL = "v3/search/all";

    /**
     * 动态搜索
     */
    public static final String SEARCH_DYNAMIC = "v3/search/dynamic";

    public static final String MATCH_VOUCHER = "v3/voucher/getUseableVoucher";

}
