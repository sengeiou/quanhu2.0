package com.rz.httpapi.api;

import com.rz.httpapi.api.ResponseData.ResponseData;
import com.rz.httpapi.api.constants.IConstants;
import com.rz.httpapi.bean.AnnouncementResponseBean;
import com.rz.httpapi.bean.LoginWayBean;
import com.rz.httpapi.bean.RegisterBean;
import com.rz.httpapi.bean.UserInfoBean;

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

}
