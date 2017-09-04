package com.rz.httpapi.api;

import com.rz.httpapi.api.ResponseData.ResponseData;
import com.rz.httpapi.api.constants.IConstants;
import com.rz.httpapi.bean.AnnouncementResponseBean;
import com.rz.httpapi.bean.LoginWayBean;
import com.rz.httpapi.bean.PrivateGroupBean;
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
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GET(ApiPG.PRIVATE_GROUP_ALL + "{pageNo}" + "/" + "{pageSize}")
    Call<ResponseData<List<PrivateGroupBean>>> privateGroupList(
            @Path("pageNo") int pageNo,
            @Path("pageSize") int pageSize
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
            @Field("name") String name,
            @Field("ownerId") String ownerId,
            @Field("ownerIntro") String ownerIntro,
            @Field("ownerName") String ownerName
    );

    /**
     * @param coterieId     圈子id，即tenantId
     * @param consultingFee 咨询费，0表示免费
     * @param icon          封面图
     * @param intro         圈子简介
     * @param joinCheck     成员加入是否需要审核（0不审核，1审核）
     * @param joinFee       加入私圈金额(悠然币)，0表示免费
     * @param name          圈子名称
     * @param ownerIntro    个人简介
     * @param qrUrl         私圈名片(二维码)
     * @return
     */
    @FormUrlEncoded
    @POST(ApiPG.PRIVATE_GROUP_SETTING + "{coterieId}")
    Call<ResponseData> privateGroupSetting(
            @Path("coterieId") String coterieId,
            @Field("consultingFee") int consultingFee,
            @Field("icon") String icon,
            @Field("intro") String intro,
            @Field("joinCheck") int joinCheck,
            @Field("joinFee") int joinFee,
            @Field("name") String name,
            @Field("ownerIntro") String ownerIntro,
            @Field("qrUrl") String qrUrl
    );

    /**
     * @param coterieId 圈子id，即tenantId
     * @return
     */
    @GET(ApiPG.PRIVATE_GROUP_DETAILS + "{coterieId}")
    Call<ResponseData<List<PrivateGroupBean>>> privateGroupDetails(
            @Path("coterieId") String coterieId
    );

    @GET(ApiPG.PRIVATE_GROUP_ESSENCE)
    Call<ResponseData<List<PrivateGroupBean>>> privateGroupEssence();

    @GET(ApiPG.PRIVATE_GROUP_MYSEFL_CREATE + "{custId}")
    Call<ResponseData<List<PrivateGroupBean>>> privateGroupMyselfCreate(@Path("custId") String custId);

    @GET(ApiPG.PRIVATE_GROUP_MYSELF_JOIN + "{custId}")
    Call<ResponseData<List<PrivateGroupBean>>> privateGroupMyselfJoin(@Path("custId") String custId);
}
