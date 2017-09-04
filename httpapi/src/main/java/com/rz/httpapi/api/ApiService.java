package com.rz.httpapi.api;

import com.rz.httpapi.api.ResponseData.ResponseData;
import com.rz.httpapi.api.constants.IConstants;
import com.rz.httpapi.bean.AccountBean;
import com.rz.httpapi.bean.AnnouncementResponseBean;
import com.rz.httpapi.bean.BannerAddSubjectModel;
import com.rz.httpapi.bean.CircleDynamic;
import com.rz.httpapi.bean.CircleEntrModle;
import com.rz.httpapi.bean.FamousModel;
import com.rz.httpapi.bean.LoginWayBean;
import com.rz.httpapi.bean.RegisterBean;
import com.rz.httpapi.bean.RewardGiftModel;
import com.rz.httpapi.bean.Ticket;
import com.rz.httpapi.bean.TransferResultBean;
import com.rz.httpapi.bean.UserInfoBean;
import com.rz.httpapi.bean.UserInfoModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by rzw2 on 2016/7/22.
 */
public interface ApiService {

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

    @FormUrlEncoded
    @POST(API.URL_ANNOUNCEMENT)
    Call<ResponseData<AnnouncementResponseBean>> announcementList(@Field("searchDate") String searchDate);

    /**
     * 发送验证码
     *
     * @param act
     * @param phone 手机号
     * @param type  类型1：短信验证码，2：语音验证码
     * @param code  功能码1：注册；2：找回密码；3：实名认证；4：设置支付密码；5：更换手机；6：找回支付密码；7：提现；
     * @return
     */
    @FormUrlEncoded
    @POST(APIUser.SEND_VERI_CODE)
    Call<ResponseData<RegisterBean>> sendVeriCode(
            @Field("act") int act,
            @Field("phone") String phone,
            @Field("type") String type,
            @Field("code") String code
    );

    /**
     * 验证短信验证码
     *
     * @param act
     * @param phone    验证手机号
     * @param code     1：注册；2：找回密码；验证码；3：实名认证；4：设置支付密码；5：更换手机；6：找回支付密码；7：提现；
     * @param veriCode 短信码
     * @return
     */
    @FormUrlEncoded
    @POST(APIUser.CHECK_CODE)
    Call<ResponseData<RegisterBean>> checkCode(
            @Field("act") int act,
            @Field("phone") String phone,
            @Field("code") String code,
            @Field("veriCode") String veriCode
    );

    /**
     * 注册接口
     *
     * @param act
     * @param phone    手机号
     * @param password 密码
     * @param veriCode 手机验证码
     * @param inviter  邀请者用户ID
     * @param channel  渠道编码
     * @return
     */
    @FormUrlEncoded
    @POST(APIUser.REGISTER)
    Call<ResponseData<UserInfoBean>> register(
            @Field("act") int act,
            @Field("phone") String phone,
            @Field("password") String password,
            @Field("veriCode") String veriCode,
            @Field("inviter") String inviter,
            @Field("channel") String channel
    );

    /**
     * 登录接口，POST请求，表单登录
     *
     * @param act
     * @param phone    手机号
     * @param password 密码
     * @return
     */
    @FormUrlEncoded
    @POST(APIUser.LOGIN)
    Observable<ResponseData<UserInfoBean>> login(
            @Field("act") int act,
            @Field("phone") String phone,
            @Field("password") String password
    );

    /**
     * 第三方登录接口
     *
     * @param act
     * @param openId      三方维护id
     * @param accessToken 三方维护token
     * @param type        1，微信 2，微博 3，qq
     * @param channel     渠道编码
     * @return
     */
    @FormUrlEncoded
    @POST(APIUser.OTHER_LOGIN)
    Call<ResponseData<UserInfoBean>> otherLogin(
            @Field("act") int act,
            @Field("openId") String openId,
            @Field("accessToken") String accessToken,
            @Field("type") int type,
            @Field("channel") String channel
    );

    /**
     * 解绑、绑定第三方账号
     *
     * @param act
     * @param custId      当前用户id
     * @param type        1，微信 2，微博 3，qq
     * @param openId      第三方id
     * @param action      0，绑定 1，解绑
     * @param accessToken 第三方token
     * @return
     */
    @FormUrlEncoded
    @POST(APIUser.BIND_THIRD_ACCOUNT)
    Call<ResponseData> bindThirdAccount(
            @Field("act") int act,
            @Field("custId") String custId,
            @Field("type") int type,
            @Field("openId") String openId,
            @Field("action") int action,
            @Field("accessToken") String accessToken
    );

    /**
     * 绑定手机号
     *
     * @param act
     * @param custId   当前用户Id
     * @param phone    手机号
     * @param password 密码
     * @param veriCode 短信验证码
     * @return
     */
    @FormUrlEncoded
    @POST(APIUser.BIND_PHONE)
    Call<ResponseData> bindPhone(
            @Field("act") int act,
            @Field("custId") String custId,
            @Field("phone") String phone,
            @Field("password") String password,
            @Field("veriCode") String veriCode
    );

    /**
     * 忘记密码--更改密码
     */
    @FormUrlEncoded
    @POST(APIUser.CHANGE_PW)
    Call<ResponseData> changePw(
            @Field("act") int act,
            @Field("phone") String phone,
            @Field("password") String password,
            @Field("veriCode") String veriCode
    );

    /**
     * 获取登录方式
     *
     * @param act
     * @param custId 当前用户id
     * @return
     */
    @FormUrlEncoded
    @POST(APIUser.LOGIN_METHOD)
    Call<ResponseData<List<LoginWayBean>>> getLoginMethod(
            @Field("act") int act,
            @Field("custId") String custId
    );

    /**
     * 修改登录密码
     */
    @FormUrlEncoded
    @POST(APIUser.MODIFY_PW)
    Call<ResponseData> modifyPw(
            @Field("custId") String custId,
            @Field("password") String password,
            @Field("newPassword") String newPassword
    );

    /**
     * 退出APP
     *
     * @param custId 当前用户id
     * @return
     */
    @FormUrlEncoded
    @POST(APIUser.EXIT_APP)
    Call<ResponseData> exitAPP(
            @Field("custId") String custId
    );
    /*****************************作品、圈子相关********app-circle********************************************/

    /**
     * 获取首页圈子入口列表
     */
    @FormUrlEncoded
    @POST(CircleApi.CIRCLE_ENTRANCE_LIST)
    public Call<ResponseData<List<CircleEntrModle>>> getCircleEntrList(
            @Field("status") int status
    );

    /**
     * 获取首页banner
     */
    @FormUrlEncoded
    @POST(CircleApi.CIRCLE_BANNER_LIST)
    public Call<ResponseData<List<BannerAddSubjectModel>>> getBanner(
            @Field("act") int act
    );

    /**
     * 获取首页圈子动态列表
     *
     * @param custId
     * @param start
     * @param limit
     * @return
     */
    @FormUrlEncoded
    @POST(CircleApi.CIRCLE_DYNAMIC_GET)
    public Call<ResponseData<List<CircleDynamic>>> getCircleDynamic(
            @Field("custId") String custId,
            @Field("start") int start,
            @Field("limit") int limit
    );


    /*******************个人信息编辑保存 start**********************/

    /**
     * 保存头像
     *
     * @param custId
     * @param headImg
     * @return
     */
    @FormUrlEncoded
    @POST(APIUser.PERSON_INFO_UPDATE)
    Call<ResponseData> editSaveHeadImg(
            @Field("act") int act,
            @Field("custId") String custId,
            @Field("headImg") String headImg
    );

    /**
     * 保存昵称
     *
     * @param custId
     * @param nickName
     * @return
     */
    @FormUrlEncoded
    @POST(APIUser.PERSON_INFO_UPDATE)
    Call<ResponseData> editSaveNickName(
            @Field("act") int act,
            @Field("custId") String custId,
            @Field("nickName") String nickName
    );

    /**
     * 保存性别
     *
     * @param custId
     * @param sex
     * @return
     */
    @FormUrlEncoded
    @POST(APIUser.PERSON_INFO_UPDATE)
    Call<ResponseData> editSaveSex(
            @Field("act") int act,
            @Field("custId") String custId,
            @Field("sex") String sex
    );

    /**
     * 保存地区
     *
     * @param custId
     * @param location
     * @return
     */
    @FormUrlEncoded
    @POST(APIUser.PERSON_INFO_UPDATE)
    Call<ResponseData> editSaveAress(
            @Field("act") int act,
            @Field("custId") String custId,
            @Field("location") String location
    );

    /**
     * 保存个人签名
     *
     * @param custId
     * @param signature
     * @return
     */
    @FormUrlEncoded
    @POST(APIUser.PERSON_INFO_UPDATE)
    Call<ResponseData> editSaveSignature(
            @Field("act") int act,
            @Field("custId") String custId,
            @Field("signature") String signature

    );

    /**
     * 保存个人简介
     *
     * @param custId
     * @param desc
     * @return
     */
    @FormUrlEncoded
    @POST(APIUser.PERSON_INFO_UPDATE)
    Call<ResponseData> editSaveDesc(
            @Field("act") int act,
            @Field("custId") String custId,
            @Field("desc") String desc
    );

    /*******************个人信息编辑保存 end**********************/

    /**
     * 举报接口
     *
     * @param custId
     * @param type
     * @param sourceId
     * @param content
     * @return
     */
    @FormUrlEncoded
    @POST(API.REPORT)
    public Call<ResponseData> report(
            @Field("custId") String custId,
            @Field("type") int type,
            @Field("sourceId") String sourceId,
            @Field("content") String content
    );

    /**
     * 获取圈子达人
     */
    @FormUrlEncoded
    @POST(CircleApi.CIRCLE_FAMOUS_LIST)
    public Call<ResponseData<List<FamousModel>>> getFamous(
            @Field("appIds") String appIds
    );

    /**
     * 获取转发价格列表
     *
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST(CircleApi.CIRCLE_TRANSFER_GET_PRICE)
    Call<ResponseData<List<RewardGiftModel>>> v3CircleGetTransferPrice(
            @Field("custId") String custId
    );

    /**
     * 匹配可用支付券
     */
    @FormUrlEncoded
    @POST(CircleApi.MATCH_VOUCHER)
    public Call<ResponseData<Ticket>> MatchVoucher(
            @Field("cost") String cost,
            @Field("custId") String custId,
            @Field("type") int type);

    /**
     * 圈子转发
     *
     * @return
     */
    @FormUrlEncoded
    @POST(CircleApi.TRANSFER)
    Call<ResponseData<TransferResultBean>> v3CircleTransfer(
            @Field("custId") String custId,//*
            @Field("parentId") String parentId,
            @Field("authorId") String authorId,//*
            @Field("appId") String appId,//*
            @Field("moduleId") String moduleId,//*
            @Field("infoId") String infoId,
            @Field("infoTitle") String infoTitle,
            @Field("infoDesc") String infoDesc,
            @Field("infoThumbnail") String infoThumbnail,
            @Field("infoPic") String infoPic,
            @Field("infoVideo") String infoVideo,
            @Field("infoVideoPic") String infoVideoPic,
            @Field("price") long price,
            @Field("payPasswd") String payPasswd,
            @Field("infoCreateTime") String createTime
    );

    /**
     * 获取用户账户余额
     *
     * @param custId
     * @return
     */
    @FormUrlEncoded
    @POST(PayAPI.GET_ACCOUNT)
    Call<ResponseData<AccountBean>> getUserAccount(
            @Field("act") int act,
            @Field("custId") String custId
    );

    /**
     * 查询用户安全信息
     *
     * @param custId
     * @return
     */
    @FormUrlEncoded
    @POST(PayAPI.SEARCH_USER_NEWS)
    Call<ResponseData<UserInfoModel>> searchUserNews(
            @Field("act") int act,
            @Field("custId") String custId
    );

}
