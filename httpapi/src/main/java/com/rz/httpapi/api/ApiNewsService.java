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

    /**
     * @param custId
     * @param type   通知公告：1 系统消息：2 互动消息：3 推荐与活动：4 账户与安全：5
     * @param label  评论：100 问答：101 私圈：102 活动：103
     * @param limit
     * @param start
     * @return
     */
    @FormUrlEncoded
    @POST(ApiNews.NEWS_MULTI_LIST)
    Call<ResponseData<List<NewsBean>>> newsMulitList(
            @Field("custId") String custId,
            @Field("type") int type,
            @Field("label") int label,
            @Field("limit") int limit,
            @Field("start") int start
    );

}
