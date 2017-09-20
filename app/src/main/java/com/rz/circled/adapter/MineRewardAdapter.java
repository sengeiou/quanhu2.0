package com.rz.circled.adapter;

import android.content.Context;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rz.circled.R;
import com.rz.common.adapter.CommonAdapter;
import com.rz.common.adapter.ViewHolder;

/**
 * Created by Administrator on 2017/9/19 0019.
 */

public class MineRewardAdapter extends CommonAdapter {

    public MineRewardAdapter(Context context, int layoutId) {
        super(context, layoutId);
    }

    @Override
    public void convert(ViewHolder helper, Object item, int position) {
        ImageView avatar = (ImageView) helper.getViewById(R.id.avatar);
        TextView tvName = (TextView) helper.getViewById(R.id.tv_name);
        TextView tvTime = (TextView) helper.getViewById(R.id.tv_time);
        TextView tvTitle = (TextView) helper.getViewById(R.id.tv_title);
        ImageView rewardImg = (ImageView) helper.getViewById(R.id.reward_img);
        TextView tvContent = (TextView) helper.getViewById(R.id.tv_content);

    }
}
