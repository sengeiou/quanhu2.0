package com.rz.circled.adapter;

import android.content.Context;
import android.widget.TextView;

import com.rz.circled.R;
import com.rz.common.adapter.ViewHolder;

/**
 * Created by Gsm on 2017/9/2.
 */
public class SearchRewardAdapter extends SearchCommonAdapter {

    public SearchRewardAdapter(Context context, int layoutId) {
        super(context, layoutId);
    }

    @Override
    public void convert(ViewHolder helper, Object item, int position) {
        TextView avatarTxt = (TextView) helper.getViewById(R.id.iv_search_person);
        TextView rewardTxt = (TextView) helper.getViewById(R.id.tv_reward);
        TextView tvName = (TextView) helper.getViewById(R.id.tv_name);
        TextView tvContent = (TextView) helper.getViewById(R.id.tv_content);
        TextView tvTime = (TextView) helper.getViewById(R.id.tv_search_time);
        TextView tvPerNum = (TextView) helper.getViewById(R.id.tv_join_num);
        TextView tvStatus = (TextView) helper.getViewById(R.id.tv_search_status);



    }
}
