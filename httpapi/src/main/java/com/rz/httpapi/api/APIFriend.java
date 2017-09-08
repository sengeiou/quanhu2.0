package com.rz.httpapi.api;

/**
 * Created by rzw2 on 2017/3/27.
 */

public class APIFriend {
    /**
     * 加好友（申请、同意、拒绝）
     */
    public static final String REQUIRE_FRIEND = "v3/friend/require";

    /**
     * 通过手机号加好友（申请、同意、拒绝）
     */
    public static final String REQUIRE_FRIEND_BY_PHONE = "v3/friend/requireByPhone";

    /**
     * 申请加好友列表
     */
    public static final String REQUIRE_LIST = "v3/friend/listRequire";

    /**
     * 删除申请加好友
     */
    public static final String REQUIRE_DETELE = "v3/friend/deleteRequire";

    /**
     * 更新好友信息
     * public static final String FRIEND_REMARK = "v2/relation/modifyfriends";
     */
    public static final String FRIEND_REMARK = "v3/friend/modifyfriends";

    /**
     * 删除好友
     */
    public static final String FRIEND_DETELE = "v3/friend/deleteFriend";

    /**
     * 好友列表
     */
    public static final String FRIEND_LIST = "v3/friend/listFriend";

    /**
     * 好友列表ALL
     *  public static final String FRIEND_LIST = "v2/relation/listAll";
     */
    public static final String FRIEND_ALL_LIST = "v3/friend/listAllFriend";

    /**
     * 好友统计
     */
    public static final String FRIEND_STATS = "v3/friend/stats";

}

