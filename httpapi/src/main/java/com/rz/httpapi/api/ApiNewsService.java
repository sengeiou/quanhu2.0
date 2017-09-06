package com.rz.httpapi.api;

import com.rz.httpapi.api.ResponseData.ResponseData;
import com.rz.httpapi.api.constants.IConstants;
import com.rz.httpapi.bean.GroupBannerBean;
import com.rz.httpapi.bean.NewsBean;
import com.rz.httpapi.bean.NewsUnreadBean;
import com.rz.httpapi.bean.PrivateGroupBean;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by rzw2 on 2016/7/22.
 * 私圈接口定义
 */
public interface ApiNewsService {

    @FormUrlEncoded
    @POST(ApiNews.NEWS_UNREAD)
    Call<ResponseData<NewsUnreadBean>> newsUnread(
            @Field("custId") String custId
    );

    @FormUrlEncoded
    @POST(ApiNews.NEWS_ANNOUNCEMENT)
    Call<ResponseData<List<NewsBean>>> newsAnncouncement(
            @Field("custId") String custId,
            @Field("limit") String limit,
            @Field("readTime") String readTime
    );

    @FormUrlEncoded
    @POST(ApiNews.NEWS_SYSTEM_INFORMATION)
    Call<ResponseData<List<NewsBean>>> newsSystemInformation(
            @Field("custId") String custId,
            @Field("limit") String limit,
            @Field("readTime") String readTime
    );

    @FormUrlEncoded
    @POST(ApiNews.NEWS_ACCOUNT_INFORMATION)
    Call<ResponseData<List<NewsBean>>> newsAccountInformation(
            @Field("custId") String custId,
            @Field("limit") String limit,
            @Field("readTime") String readTime
    );

    @FormUrlEncoded
    @POST(ApiNews.NEWS_INTERACTIVE)
    Call<ResponseData<List<NewsBean>>> newsInteractive(
            @Field("custId") String custId,
            @Field("limit") String limit,
            @Field("readTime") String readTime
    );

    @FormUrlEncoded
    @POST(ApiNews.NEWS_RECOMMEND)
    Call<ResponseData<List<NewsBean>>> newsRecommend(
            @Field("custId") String custId,
            @Field("limit") String limit,
            @Field("readTime") String readTime
    );

}
