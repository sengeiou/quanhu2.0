package com.rz.circled.js.http;

import com.google.gson.JsonObject;
import com.rz.httpapi.api.APIUser;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by fengan on 2017/3/24/024.
 */

//来自web的请求
public interface APIWebService {
    //有参的post请求,参数为json
    @POST("{url}")
    @Headers({"Content-type:application/json; charset=UTF-8"})
    public Call<JsonObject> reQuestNetPost(@Path(value = "url", encoded = true) String url, @Body RequestBody body, @HeaderMap Map<String, String> header);

    //有参数的get请求
    @GET("{url}")
    public Call<JsonObject> reQuestNetGetMap(@Path(value = "url", encoded = true) String url, @QueryMap Map<String, String> params, @HeaderMap Map<String, String> header);

    //无参数的get请求
    @GET("{url}")
    public Call<JsonObject> reQuestNetGet(@Path(value = "url", encoded = true) String url, @HeaderMap Map<String, String> header);

    //有参的put请求,参数为json
    @PUT("{url}")
    @Headers({"Content-type:application/json; charset=UTF-8"})
    public Call<JsonObject> reQuestNetPut(@Path(value = "url", encoded = true) String url, @Body RequestBody body, @HeaderMap Map<String, String> header);



    //有参的delete请求,参数为json
//    @DELETE("{url}")
    @HTTP(method = "DELETE", path = "{url}", hasBody = true)
    @Headers({"Content-type:application/json; charset=UTF-8"})
    public Call<JsonObject> reQuestNetDelete(@Path(value = "url", encoded = true) String url, @Body RequestBody body, @HeaderMap Map<String, String> header);

    //有参的patch请求,参数为json
    @PATCH("{url}")
    @Headers({"Content-type:application/json; charset=UTF-8"})
    public Call<JsonObject> reQuestNetPatch(@Path(value = "url", encoded = true) String url, @Body RequestBody body, @HeaderMap Map<String, String> header);

    /**
     * 发送验证码 js
     */
    @FormUrlEncoded
    @POST(APIUser.SEND_VERI_CODE)
    public Call<JsonObject>sendVeriCodeJs(
            @Field("act") int act,
            @Field("phone") String phone,
            @Field("type") String type,
            @Field("code") String code
    );
}
