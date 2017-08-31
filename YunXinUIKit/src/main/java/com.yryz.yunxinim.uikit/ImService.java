package com.yryz.yunxinim.uikit;

import com.rz.httpapi.api.ApiIM;
import com.rz.httpapi.api.ResponseData.ResponseData;
import com.yryz.yunxinim.uikit.model.CheckCircleModel;
import com.yryz.yunxinim.uikit.model.CheckTeamModel;
import com.yryz.yunxinim.uikit.model.CircleTeamModel;
import com.yryz.yunxinim.uikit.model.TeamCreateModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by rzw2 on 2017/3/29.
 */

public interface ImService {
    /**
     * ***************************** 本地im接口 start *******************************
     */

    /**
     * 创建群
     *
     * @param tname        群名称，最大长度64字符（是）
     * @param custId       用户custId（是）
     * @param members      ["aaa","bbb"](JSONArray对应的accid)，一次最多拉200个成员（否）
     * @param announcement 群公告，最大长度1024字符（否）
     * @param intro        群描述，最大长度512字符（否）
     * @param msg          邀请发送的文字，最大长度150字符（是）
     * @param icon         群头像，最大长度1024字符（否）
     * @param appId        所在圈子ID（否）
     * @return
     */
    @FormUrlEncoded
    @POST(ApiIM.CREATE_TEAM)
    public Call<ResponseData<TeamCreateModel>> createTeam(
            @Field("tname") String tname,
            @Field("custId") String custId,
            @Field("members") String members,
            @Field("announcement") String announcement,
            @Field("intro") String intro,
            @Field("msg") String msg,
            @Field("icon") String icon,
            @Field("appId") String appId
    );

    /**
     * 解散群
     *
     * @param custId 用户custId
     * @param tid    群Id
     * @return
     */
    @FormUrlEncoded
    @POST(ApiIM.DETELE_TEAM)
    Call<ResponseData> deteleTeam(
            @Field("custId") String custId,
            @Field("tid") String tid
    );

    /**
     * 编辑群资料
     *
     * @param custId       用户custId
     * @param tid          群Id
     * @param tname        群名称
     * @param icon         群图标
     * @param announcement 群公告
     * @param intro        群描述
     * @return
     */
    @FormUrlEncoded
    @POST(ApiIM.UPDATE_TEAM)
    Call<ResponseData> updateTeam(
            @Field("custId") String custId,
            @Field("tid") String tid,
            @Field("tname") String tname,
            @Field("icon") String icon,
            @Field("announcement") String announcement,
            @Field("intro") String intro
    );

    /**
     * 移交群组
     *
     * @param custId 申请者ID
     * @param tid    群Id
     * @param owner  新群主ID
     * @return
     */
    @FormUrlEncoded
    @POST(ApiIM.TRANSFER_TEAM)
    Call<ResponseData> transferTeam(
            @Field("custId") String custId,
            @Field("tid") String tid,
            @Field("owner") String owner,
            @Field("isLeaveTeam") int isLeaveTeam
    );

    /**
     * 判断用户是否属于群
     *
     * @param custId 申请者ID
     * @param tid    群Id
     * @return
     */
    @FormUrlEncoded
    @POST(ApiIM.CHECK_IN_TEAM)
    Call<ResponseData<CheckTeamModel>> checkInTeam(
            @Field("custId") String custId,
            @Field("tid") String tid
    );

    /**
     * 判断是否属于圈子
     *
     * @param circleKey 圈子Id
     * @param tid       群Id
     * @return
     */
    @FormUrlEncoded
    @POST(ApiIM.CHECK_IN_CIRCLE)
    Call<ResponseData<CheckCircleModel>> checkInCircle(
            @Field("circleKey") String circleKey,
            @Field("tid") String tid
    );

    /**
     * 查询圈子中的群组列表
     *
     * @param appId
     * @param custId
     * @param start
     * @param limit
     * @return
     */
    @FormUrlEncoded
    @POST(ApiIM.GET_TEAM_BY_CIRCLE)
    Call<ResponseData<List<CircleTeamModel>>> getTeamInCircle(
            @Field("appId") String appId,
            @Field("custId") String custId,
            @Field("start") int start,
            @Field("limit") int limit
    );

    /**
     * 查询群列表
     *
     * @param tids 圈子Id
     * @return
     */
    @FormUrlEncoded
    @POST(ApiIM.GET_ALL_TEAM)
    Call<ResponseData<List<CircleTeamModel>>> getAllTeam(
            @Field("tids") String tids
    );

    /**
     * 加入圈子群
     *
     * @param custId 申请者ID
     * @param tid    群Id
     * @return
     */
    @FormUrlEncoded
    @POST(ApiIM.JOIN_TEAM)
    Call<ResponseData> joinTeam(
            @Field("custId") String custId,
            @Field("fid") String fid,
            @Field("tid") String tid
    );

    /**
     * 退出圈子群
     *
     * @param custId
     * @param tid
     * @return
     */
    @FormUrlEncoded
    @POST(ApiIM.QUIT_TEAM)
    Call<ResponseData> quitTeam(
            @Field("custId") String custId,
            @Field("fid") String fid,
            @Field("tid") String tid
    );

    /**
     * ***************************** 本地im接口 end *******************************
     */

}
