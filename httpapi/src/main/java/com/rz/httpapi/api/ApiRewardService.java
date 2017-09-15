package com.rz.httpapi.api;

import com.rz.httpapi.api.ResponseData.ResponseData;
import com.rz.httpapi.bean.RewardBean;
import com.rz.httpapi.bean.RewardGiftModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by rzw2 on 2017/9/14.
 */

public interface ApiRewardService {

    /**
     * @param custId     打赏人
     * @param giftId     礼物ID
     * @param giftNum    礼物数量
     * @param resourceId 资源id
     * @param tocustId   被打赏人
     * @param type       打赏的对象类型 0:资源
     * @return
     */
    @FormUrlEncoded
    @POST(ApiReward.REWARD_DO)
    Call<ResponseData<RewardBean>> rewardDo(
            @Field("custId") String custId,
            @Field("giftId") String giftId,
            @Field("giftNum") int giftNum,
            @Field("resourceId") String resourceId,
            @Field("tocustId") String tocustId,
            @Field("type") String type
    );

    /**
     * @param start
     * @param limit
     * @return
     */
    @FormUrlEncoded
    @POST(ApiReward.REWARD_GIFT_LIST)
    Call<ResponseData<List<RewardGiftModel>>> rewardGiftList(
            @Field("start") int start,
            @Field("limit") int limit
    );
}
