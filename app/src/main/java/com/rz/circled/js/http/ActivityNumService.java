package com.rz.circled.js.http;

import com.rz.httpapi.api.APIUser;
import com.rz.httpapi.api.ResponseData.ResponseData;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2017/9/20 0020.
 */

public interface ActivityNumService {


    /**
     * 获取活动数统计
     * @return
     */
    @FormUrlEncoded
    @POST(APIUser.GET_LIST_COUNT)
    Call<ResponseData> getMylistCount(
            @Field("paramId") String paramId
    );

}
