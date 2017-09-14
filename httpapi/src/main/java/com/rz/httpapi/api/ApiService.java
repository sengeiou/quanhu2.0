package com.rz.httpapi.api;

import com.rz.httpapi.api.ResponseData.ResponseData;
import com.rz.httpapi.api.constants.IConstants;
import com.rz.httpapi.bean.AccountBean;
import com.rz.httpapi.bean.AnnouncementResponseBean;
import com.rz.httpapi.bean.BannerAddSubjectModel;
import com.rz.httpapi.bean.BaseInfo;
import com.rz.httpapi.bean.BillDetailModel;
import com.rz.httpapi.bean.CircleDynamic;
import com.rz.httpapi.bean.CircleEntrModle;
import com.rz.httpapi.bean.CircleMemberModel;
import com.rz.httpapi.bean.ClubStats;
import com.rz.httpapi.bean.FamousModel;
import com.rz.httpapi.bean.FriendInfoModel;
import com.rz.httpapi.bean.FriendRequireModel;
import com.rz.httpapi.bean.HotSubjectModel;
import com.rz.httpapi.bean.LoginWayBean;
import com.rz.httpapi.bean.MoreFamousModel;
import com.rz.httpapi.bean.OpusData;
import com.rz.httpapi.bean.OpusTag;
import com.rz.httpapi.bean.PaySignModel;
import com.rz.httpapi.bean.RegisterBean;
import com.rz.httpapi.bean.RequireFriendByPhoneModel;
import com.rz.httpapi.bean.RewardGiftModel;
import com.rz.httpapi.bean.SearchDataBean;
import com.rz.httpapi.bean.StarListBean;
import com.rz.httpapi.bean.Ticket;
import com.rz.httpapi.bean.TransferDetail;
import com.rz.httpapi.bean.TransferResultBean;
import com.rz.httpapi.bean.UserInfoBean;
import com.rz.httpapi.bean.UserInfoModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
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
    Call<ResponseData<UserInfoBean>> login(
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
     * 添加喜欢的圈子
     */
    @FormUrlEncoded
    @POST(CircleApi.SAVE_FAVORITE_CIRCLE)
    public Observable<ResponseData> addLoveCircle(
            @Field("circleId") String circleId,
            @Field("custId") String custId,
            @Field("type") int type
    );
    /**
     * 删除喜欢的圈子
     */
    @FormUrlEncoded
    @POST(CircleApi.REMOVE_FAVORITE_CIRCLE)
    public Observable<ResponseData> delLoveCircle(
            @Field("circleId") String circleId,
            @Field("custId") String custId
    );
    /**
     * 获取喜欢圈子列表
     */
    @FormUrlEncoded
    @POST(CircleApi.FAVORITE_CIRCLE_LIST)
    public Observable<ResponseData<List<CircleEntrModle>>> getLoveCircleList(
            @Field("custId") String custId
    );

    /**
     * 获取首页banner
     */
    @FormUrlEncoded
    @POST(CircleApi.CIRCLE_BANNER_LIST)
    public Call<ResponseData<List<BannerAddSubjectModel>>> getBanner(
            @Field("bannerType") String bannerType
    );

    /**
     * 获取首页圈子动态列表
     *
     * @return
     */
    @GET(CircleApi.CIRCLE_DYNAMIC_GET)
    public Observable<ResponseData<List<CircleDynamic>>> getCircleDynamic(
            @Query("cityCode") String cityCode,
            @Query("createTime") long createTime,
            @Query("custId") String custId,
            @Query("pageNo") int pageNo
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
     * 获取更多达人
     */
    @FormUrlEncoded
    @POST(CircleApi.MORE_FAMOUS_LIST)
    public Observable<ResponseData<MoreFamousModel<List<StarListBean>>>> getMoreFamous(
            @Field("appIds") String appIds
    );
    /**
     * 获取推荐话题
     */
    @GET(CircleApi.CIRCLE_SUBJECT_LIST)
    public Observable<ResponseData<List<HotSubjectModel>>> getSubject(
    );
    /**
     * 获取更多话题
     */
    @GET(CircleApi.MORE_CIRCLE_SUBJECT)
    public Observable<ResponseData<List<HotSubjectModel>>> getMoreSubject(
            @Query("limit") int limit,
            @Query("start") int start
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


    /**
     * 圈子成员列表
     *
     * @param appId 圈子id
     * @return
     */
    @FormUrlEncoded
    @POST(API.CIRCLE_MEMBER)
    Call<ResponseData<List<CircleMemberModel>>> getCircleMember(
            @Field("appId") String appId, @Field("custId") String custId
    );

    /**
     * 圈子统计收益
     *
     * @param custId
     * @param clubId
     * @param loginUserId
     * @return
     */
    @FormUrlEncoded
    @POST(API.CLUB_STATS)
    public Call<ResponseData<ClubStats>> getClubStats(
            @Field("act") String act,
            @Field("custId") String custId,
            @Field("clubId") String clubId,
            @Field("loginUserId") String loginUserId
    );

    /**
     * 收益详情
     */
    @FormUrlEncoded
    @POST(API.TRANSFER_CLUB_DETAIL)
    public Call<ResponseData<TransferDetail>> transferClubDetail(
            @Field("act") String actionCode,
            @Field("loginUserId") String loginUserId,
            @Field("clubId") String clubId
    );

    @FormUrlEncoded
    @POST(API.OPUS_TAG)
    public Call<ResponseData<List<OpusTag>>> getOpusTag(
            @Field("act") int actionCode,
            @Field("type") String type
    );

    /**
     * 作品标签搜索
     *
     * @param actionCode
     * @param loginUserId
     * @param type
     * @param tag
     * @param start
     * @param limit
     * @return
     */
    @FormUrlEncoded
    @POST(API.OPUS_TAGINFO_LIST)
    public Call<ResponseData<List<OpusData>>> searchOpusTagInfoList(
            @Field("act") int actionCode,
            @Field("loginUserId") String loginUserId,
            @Field("type") int type,
            @Field("tag") String tag,
            @Field("start") int start,
            @Field("limit") int limit
    );

    /**
     * 获取作品列表
     *
     * @param actionCode
     * @param categroy
     * @param type
     * @param start
     * @param limit
     * @return
     */
    @FormUrlEncoded
    @POST(API.OPUS_LIST)
    public Call<ResponseData<List<OpusData>>> getOpusList(
            @Field("act") int actionCode,
            @Field("custId") String custId,
            @Field("category") String categroy,
            @Field("loginUserId") String loginUserId,
            @Field("type") Integer type,
            @Field("start") int start,
            @Field("limit") int limit
    );

    /**
     * 获取圈子图片
     */
    @FormUrlEncoded
    @POST(API.CIRCLE_IMGS)
    public Call<ResponseData<String[]>> getCircleImgs(
            @Field("act") int actionCode,
            @Field("custId") String custId,
            @Field("limit") int limit
    );

    /**
     * 好友详情
     *
     * @return
     */
    @FormUrlEncoded
    @POST(APIUser.FRIEND_DETAIL)
    Call<ResponseData<FriendInfoModel>> getFriendDetail(
            @Field("act") int act,
            @Field("custId") String custId,
            @Field("fid") String fid
    );

    /**
     * 加好友通过手机号（申请、同意、拒绝）
     *
     * @param aCustId 加好友发起者custId（是）
     * @param phone   手机号
     * @return
     */
    @FormUrlEncoded
    @POST(APIFriend.REQUIRE_FRIEND_BY_PHONE)
    Call<ResponseData<RequireFriendByPhoneModel>> requireFriendByPhone(
            @Field("custId") String aCustId,
            @Field("phone") String phone
    );


    /**
     * 好友删除
     *
     * @param custId 加好友发起者custId
     * @param fid    加好友接受者custId
     * @return
     */
    @FormUrlEncoded
    @POST(APIFriend.FRIEND_DETELE)
    Call<ResponseData> friendDetele(
            @Field("custId") String custId,
            @Field("fid") String fid
    );

    /**
     * 好友备注
     *
     * @param act
     * @param custId    加好友发起者custId
     * @param fid       加好友接受者custId
     * @param nameNotes 给好友编辑备注名，限长128
     * @return
     */
    @FormUrlEncoded
    @POST(APIFriend.FRIEND_REMARK)
    Call<ResponseData> remarkFriend(
            @Field("act") int act,
            @Field("custId") String custId,
            @Field("fid") String fid,
            @Field("nameNotes") String nameNotes
    );

    /**
     * 删除好友申请
     *
     * @param custId 加好友发起者custId
     * @param rid    请求ID，用于同意、拒绝申请时传参
     * @return
     */
    @FormUrlEncoded
    @POST(APIFriend.REQUIRE_DETELE)
    Call<ResponseData> requireDetele(
            @Field("custId") String custId,
            @Field("rid") Integer rid
    );

    /**
     * 申请加好友列表
     *
     * @param custId 发起者custId
     * @param limit  每页条数
     * @param start  分页游标 起始位置
     * @return
     */
    @FormUrlEncoded
    @POST(APIFriend.REQUIRE_LIST)
    Call<ResponseData<List<FriendRequireModel>>> requireList(
            @Field("custId") String custId,
            @Field("limit") int limit,
            @Field("start") int start
    );

    /**
     * 加好友（申请、同意、拒绝）
     *
     * @param custId 加好友发起者custId（是）
     * @param fid    加好友接受者custId（是）
     * @param msg    加好友对应的请求消息，最长256字符（否）
     * @param type   1申请加好友，2同意加好友，3拒绝加好友（是）
     * @param rid    请求ID，用于同意、拒绝申请时传参（否）
     * @return
     */
    @FormUrlEncoded
    @POST(APIFriend.REQUIRE_FRIEND)
    Call<ResponseData> requireFriend(
            @Field("custId") String custId,
            @Field("fid") String fid,
            @Field("msg") String msg,
            @Field("type") Integer type,
            @Field("rid") Integer rid
    );

    /**
     * 好友搜索
     *
     * @return
     */
    @FormUrlEncoded
    @POST(APIUser.FRIEND_SEARCH)
    Call<ResponseData<List<BaseInfo>>> searchFriend(
            @Field("custId") String custId,
            @Field("keyWord") String keyword,
            @Field("start") int start,
            @Field("limit") int limit
    );

    /**
     * 好友列表
     *
     * @param custId 发起者custId
     * @return
     */
    @FormUrlEncoded
    @POST(APIFriend.FRIEND_ALL_LIST)
    Call<ResponseData<List<BaseInfo>>> friendList(
            @Field("custId") String custId
    );


    /**
     * 获取支付订单
     *
     * @param custId
     * @param payWay
     * @param orderSrc
     * @param orderAmount
     * @param currency
     * @return
     */
    @FormUrlEncoded
    @POST(PayAPI.PAY)
    Call<ResponseData<PaySignModel>> payProvingSign(
            @Field("act") int act,
            @Field("custId") String custId,
            @Field("payWay") String payWay,
            @Field("orderSrc") String orderSrc,
            @Field("orderAmount") String orderAmount,
            @Field("currency") String currency
    );


    /**
     * 统一搜索
     *
     * @return
     */
    @FormUrlEncoded
    @POST(APIUser.QH_SEARCH)
    Call<ResponseData<SearchDataBean>> searchQH(
            @Field("circleId") String circleId,
            @Field("coterieId") String coterieId,
            @Field("keyWord") String keyWord,
            @Field("limit") int limit,
            @Field("resourceType") String resourceType,
            @Field("searchType") int searchType,
            @Field("start") int start
    );
//我的api
    /**
     * 查询账单明细
     *
     * @param custId
     * @return
     */
    @FormUrlEncoded
    @POST(PayAPI.GET_BILL_ORDER)
    Call<ResponseData<List<BillDetailModel>>> getBillList(
            @Field("productType") int productType,
            @Field("custId") String custId,
            @Field("date") String date,
            @Field("type") int type,
            @Field("start") int start,
            @Field("limit") int limit
    );

//    /**
//     * 转发统计
//     *
//     * @param custId
//     * @return
//     */
//    @FormUrlEncoded
//    @POST(CircleApi.CIRCLE_TRANSFER_STATS)
//    public Call<ResponseData<CircleStatsModel>> getCircleStats(
//            @Field("custId") String custId
//    );

}
