package com.rz.httpapi.api;

import com.rz.httpapi.api.ResponseData.ResponseData;
import com.rz.httpapi.bean.NewsBean;
import com.rz.httpapi.bean.PayOrderInfoBean;
import com.rz.httpapi.bean.UserInfoModel;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by rzw2 on 2017/9/15.
 */

public interface ApiPayService {

    @FormUrlEncoded
    @POST(ApiPay.PAY_ORDER)
    Observable<ResponseData> payOrder(
            @Field("custId") String custId,
            @Field("orderId") String orderId,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST(ApiPay.PAY_ORDER_DETAILS)
    Observable<ResponseData<PayOrderInfoBean>> payOrderDetails(
            @Field("orderId") String orderId
    );

    /**
     * 查询用户安全信息
     *
     * @param custId
     * @return
     */
    @FormUrlEncoded
    @POST(ApiPay.SEARCH_USER_NEWS)
    Observable<ResponseData<UserInfoModel>> searchUserNews(
            @Field("custId") String custId
    );
}
