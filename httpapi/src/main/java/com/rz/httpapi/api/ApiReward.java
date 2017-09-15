package com.rz.httpapi.api;

/**
 * Created by rzw2 on 2017/9/14.
 */

public class ApiReward {
    /**
     * 打赏
     */
    static final String REWARD_DO = "v3/reward/sendReward";
    /**
     * 礼物列表
     */
    static final String REWARD_GIFT_LIST = "v3/reward/giftList";
    /**
     * 打赏列表
     */
    static final String REWARD_LIST = "v3/reward/list";
    /**
     * 我的打赏
     */
    static final String REWARD_MY = "v3/reward/getMyReward";
    /**
     * 我的打赏明细
     */
    static final String REWARD_MY_DETAILS = "v3/reward/getMyDetail";
    /**
     * 查询我的打赏统计信息
     */
    static final String REWARD_STATISTICS = "v3/reward/getMyRewardStat";
    /**
     * 检查打赏回调状态
     */
    static final String REWARD_CALLBACK_STATS = "v3/reward/checkRewardNotify";

}
