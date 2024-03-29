package com.rz.httpapi.api;

import com.rz.httpapi.api.ResponseData.ResponseData;
import com.rz.httpapi.api.constants.IConstants;
import com.rz.httpapi.bean.GroupBannerBean;
import com.rz.httpapi.bean.NewsBean;
import com.rz.httpapi.bean.NewsUnreadBean;
import com.rz.httpapi.bean.PrivateGroupBean;

import java.util.HashMap;
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
    Call<ResponseData<HashMap<String, String>>> newsUnread(
            @Field("custId") String custId
    );

    /**
     * @param custId
     * @param type   通知公告：1 系统消息：2 互动消息：4 推荐与活动：5 账户与安全：3
     * @param label
     * @param limit
     * @param start
     * @return
     */
    @FormUrlEncoded
    @POST(ApiNews.NEWS_MULTI_LIST)
    Call<ResponseData<List<NewsBean>>> newsMultiList(
            @Field("custId") String custId,
            @Field("type") int type,
            @Field("label") Integer label,
            @Field("start") int start,
            @Field("limit") int limit
    );

    @FormUrlEncoded
    @POST(ApiNews.NEWS_OVERVIEW)
    Call<ResponseData<HashMap<String, NewsBean>>> newsOverview(
            @Field("custId") String custId
    );

}
