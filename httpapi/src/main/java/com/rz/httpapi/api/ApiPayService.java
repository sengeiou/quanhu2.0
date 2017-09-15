package com.rz.httpapi.api;

import com.rz.httpapi.api.ResponseData.ResponseData;
import com.rz.httpapi.bean.NewsBean;
import com.rz.httpapi.bean.PayOrderInfoBean;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by rzw2 on 2017/9/15.
 */

public interface ApiPayService {

    @FormUrlEncoded
    @POST(ApiPay.PAY_ORDER)
    Call<ResponseData> payOrder(
            @Field("custId") String custId,
            @Field("orderId") String orderId,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST(ApiPay.PAY_ORDER_DETAILS)
    Call<ResponseData<PayOrderInfoBean>> payOrderDetails(
            @Field("orderId") String orderId
    );
}
