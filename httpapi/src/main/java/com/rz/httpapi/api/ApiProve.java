package com.rz.httpapi.api;

import com.rz.httpapi.api.ResponseData.ResponseData;
import com.rz.httpapi.bean.ProveStatusBean;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Gsm on 2017/9/16.
 */
public interface ApiProve {
    /**
     * 达人认证个人
     *
     * @param act
     * @param custId
     * @param realName
     * @param contactCall
     * @param idCard
     * @param location
     * @param tradeField
     * @param ownerAppId
     * @param resourceDesc
     * @return
     */
    @FormUrlEncoded
    @POST(APIUser.PROVE_INFO)
    Call<ResponseData> proveInfoPerson(
            @Field("authType") int act,
            @Field("custId") String custId,
            @Field("realName") String realName,
            @Field("contactCall") String contactCall,
            @Field("idCard") String idCard,
            @Field("location") String location,
            @Field("tradeField") String tradeField,
            @Field("ownerAppId") String ownerAppId,
            @Field("resourceDesc") String resourceDesc

    );

    /**
     * 达人认证机构
     *
     * @param act
     * @param custId
     * @param organizationName
     * @param realName
     * @param contactCall
     * @param location
     * @param tradeField
     * @param ownerAppId
     * @param resourceDesc
     * @param organizationPaper
     * @return
     */
    @FormUrlEncoded
    @POST(APIUser.PROVE_INFO)
    Call<ResponseData> proveInfoAgency(
            @Field("authType") int act,
            @Field("custId") String custId,
            @Field("organizationName") String organizationName,
            @Field("realName") String realName,
            @Field("contactCall") String contactCall,
            @Field("location") String location,
            @Field("tradeField") String tradeField,
            @Field("ownerAppId") String ownerAppId,
            @Field("resourceDesc") String resourceDesc,
            @Field("organizationPaper") String organizationPaper

    );

    /**
     * 达人认证个人修改
     *
     * @param act
     * @param custId
     * @param realName
     * @param contactCall
     * @param idCard
     * @param location
     * @param tradeField
     * @param ownerAppId
     * @param resourceDesc
     * @return
     */
    @FormUrlEncoded
    @POST(APIUser.PROVE_INFO_CHANGE)
    Call<ResponseData> proveInfoPersonChange(
            @Field("authType") int act,
            @Field("custId") String custId,
            @Field("realName") String realName,
            @Field("contactCall") String contactCall,
            @Field("idCard") String idCard,
            @Field("location") String location,
            @Field("tradeField") String tradeField,
            @Field("ownerAppId") String ownerAppId,
            @Field("resourceDesc") String resourceDesc

    );

    /**
     * 达人认证机构修改
     *
     * @param act
     * @param custId
     * @param organizationName
     * @param realName
     * @param contactCall
     * @param location
     * @param tradeField
     * @param ownerAppId
     * @param resourceDesc
     * @param organizationPaper
     * @return
     */
    @FormUrlEncoded
    @POST(APIUser.PROVE_INFO_CHANGE)
    Call<ResponseData> proveInfoAgencyChange(
            @Field("authType") int act,
            @Field("custId") String custId,
            @Field("organizationName") String organizationName,
            @Field("realName") String realName,
            @Field("contactCall") String contactCall,
            @Field("location") String location,
            @Field("tradeField") String tradeField,
            @Field("ownerAppId") String ownerAppId,
            @Field("resourceDesc") String resourceDesc,
            @Field("organizationPaper") String organizationPaper

    );

    @FormUrlEncoded
    @POST(APIUser.GET_PROVE_STATUS)
    Call<ResponseData<ProveStatusBean>> getProveStatus(
            @Field("custId") String custId
    );
}
