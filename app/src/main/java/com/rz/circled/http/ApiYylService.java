package com.rz.circled.http;

import com.rz.circled.BuildConfig;
import com.rz.httpapi.api.APIUser;
import com.rz.httpapi.api.CircleApi;
import com.rz.httpapi.api.ResponseData.ResponseData;
import com.rz.httpapi.bean.ActivityBean;
import com.rz.httpapi.bean.CouponsBean;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Gsm on 2017/9/19.
 */
public interface ApiYylService {
    /**
     * 获取推荐活动列表
     */
    @GET(BuildConfig.BaseOpusUrl + CircleApi.FIND_ACTIVITY_TABLE)
    public Observable<ResponseData<ActivityBean>> getActivityList(
            @Query("pageNo") int pageNo,
            @Query("pageSize") int pageSize

    );

    /**
     * 我的页面活动列表
     */
    @GET(BuildConfig.BaseOpusUrl + CircleApi.MINE_ACTIVITY)
    public Observable<ResponseData<ActivityBean>> getMineActivityList(
            @Query("pageNo") int pageNo,
            @Query("pageSize") int pageSize,
            @Query("paramId") String paramId

    );

    @GET(BuildConfig.BaseOpusUrl + APIUser.GET_COUPONS_LIST)
    Call<ResponseData<List<CouponsBean>>> getCouponsList(
            @Query("isOverdue") int isOverdue,
            @Query("paramId") String paramId,
            @Query("type") int type
    );

    /**
     * 获取我的活动数量
     * @param paramId
     * @return
     */
    @GET(BuildConfig.BaseOpusUrl + APIUser.GET_LIST_COUNT)
    Call<ResponseData> mylistCount(
            @Query("paramId") String paramId

    );
}
