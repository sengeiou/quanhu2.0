package com.rz.httpapi.api;

/**
 * Created by rzw2 on 2017/3/29.
 * IM接口
 */

public class ApiIM {
    /**
     * 创建群
     */
    public static final String CREATE_TEAM = "v3/im/createTeam";
    /**
     * 解散群
     */
    public static final String DETELE_TEAM = "v3/im/deleteTeam";
    /**
     * 更新群资料
     */
    public static final String UPDATE_TEAM = "v3/im/updateTeam";
    /**
     * 转让群
     */
    public static final String TRANSFER_TEAM = "v3/im/updateTeamOwner";
    /**
     * 检查用户是否属于群
     */
    public static final String CHECK_IN_TEAM = "v3/im/checkInTeam";
    /**
     * 检查群是否属于圈子
     */
    public static final String CHECK_IN_CIRCLE = "v3/im/InCircle";
    /**
     * 获取圈子所有群
     */
    public static final String GET_TEAM_BY_CIRCLE = "v3/im/getTeamByCircle";
    /**
     * 查询群列表
     */
    public static final String GET_ALL_TEAM = "v3/im/batchGetTeam";
    /**
     * 加入圈子群
     */
    public static final String JOIN_TEAM = "v3/im/joinTeam";
    /**
     * 退出圈子群
     */
    public static final String QUIT_TEAM = "v3/im/leaveTeam";
}
