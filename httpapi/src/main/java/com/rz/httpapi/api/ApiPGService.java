package com.rz.httpapi.api;

import com.rz.httpapi.api.ResponseData.ResponseData;
import com.rz.httpapi.api.constants.IConstants;
import com.rz.httpapi.bean.AnnouncementResponseBean;
import com.rz.httpapi.bean.CircleBelongBean;
import com.rz.httpapi.bean.CircleEntrModle;
import com.rz.httpapi.bean.GroupBannerBean;
import com.rz.httpapi.bean.LoginWayBean;
import com.rz.httpapi.bean.PrivateGroupBean;
import com.rz.httpapi.bean.PrivateGroupListBean;
import com.rz.httpapi.bean.PrivateGroupResourceBean;
import com.rz.httpapi.bean.RegisterBean;
import com.rz.httpapi.bean.UserInfoBean;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by rzw2 on 2016/7/22.
 * 私圈接口定义
 */
public interface ApiPGService {

    /**
     * 登录
     *
     * @param username 用户名
     * @param password 密码 MD5后传递参数
     * @return LoginBean
     */
    @FormUrlEncoded
    @POST(IConstants.URL_DEBUG)
    Observable login(@Field("username") String username,
                     @Field("password") String password
    );

    /**
     * @param pageNum
     * @param pageSize
     * @return
     */
    @FormUrlEncoded
    @POST(ApiPG.PRIVATE_GROUP_ALL)
    Call<ResponseData<List<PrivateGroupBean>>> privateGroupList(
            @Field("pageNum") int pageNum,
            @Field("pageSize") int pageSize
    );

    /**
     * @param circleId   圈子id，即tenantId
     * @param icon       封面图
     * @param intro      简介
     * @param name       私圈名称
     * @param ownerId    圈主userId
     * @param ownerIntro 个人简介
     * @param ownerName  圈主姓名
     * @return
     */
    @FormUrlEncoded
    @POST(ApiPG.PRIVATE_GROUP_CREATE)
    Call<ResponseData> privateGroupCreate(
            @Field("circleId") String circleId,
            @Field("icon") String icon,
            @Field("intro") String intro,
            @Field("joinCheck") int joinCheck,
            @Field("joinFee") int joinFee,
            @Field("name") String name,
            @Field("ownerId") String ownerId,
            @Field("ownerIntro") String ownerIntro,
            @Field("ownerName") String ownerName
    );

    /**
     * @param bannerType 1，引导页广告；2，首页广告；3，私圈首页广告
     * @return
     */
    @FormUrlEncoded
    @POST(ApiPG.PRIVATE_GROUP_BANNER)
    Call<ResponseData<List<GroupBannerBean>>> privateGroupBanner(@Field("bannerType") String bannerType);

    @FormUrlEncoded
    @POST(ApiPG.PRIVATE_GROUP_ESSENCE)
    Call<ResponseData<List<PrivateGroupResourceBean>>> privateGroupEssence(@Field("start") int start, @Field("limit") int limit);

    @FormUrlEncoded
    @POST(ApiPG.PRIVATE_GROUP_RECOMMEND)
    Call<ResponseData<PrivateGroupListBean>> privateGroupRecommend(@Field("custId") String custId);

    @FormUrlEncoded
    @POST(ApiPG.PRIVATE_GROUP_MYSELF_JOIN)
    Call<ResponseData<PrivateGroupListBean>> privateGroupMyselfJoin(@Field("custId") String custId, @Field("pageNum") int pageNum, @Field("pageSize") int pageSize);

    @FormUrlEncoded
    @POST(ApiPG.PRIVATE_GROUP_MYSELF_CREATE)
    Call<ResponseData<PrivateGroupListBean>> privateGroupMyselfCreate(@Field("custId") String custId, @Field("pageNum") int pageNum, @Field("pageSize") int pageSize);

    @FormUrlEncoded
    @POST(ApiPG.PRIVATE_GROUP_BELONG)
    Call<ResponseData<CircleBelongBean>> privateGroupBelong(@Field("custId") String custId);
}
