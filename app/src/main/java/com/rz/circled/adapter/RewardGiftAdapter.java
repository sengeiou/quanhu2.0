package com.rz.circled.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rz.circled.R;
import com.rz.common.adapter.CommonAdapter;
import com.rz.common.adapter.ViewHolder;
import com.rz.httpapi.bean.RewardGiftModel;

import java.util.List;

/**
 * Created by rzw2 on 2017/9/14.
 */

public class RewardGiftAdapter extends CommonAdapter<RewardGiftModel> {

    public RewardGiftAdapter(Context context, List<RewardGiftModel> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
    }

    @Override
    public void convert(ViewHolder helper, RewardGiftModel item, int position) {
        RelativeLayout rlRoot = (RelativeLayout) helper.getViewById(R.id.rl_item_transfer_gift_root);
        ImageView mIvIcon = (ImageView) helper.getViewById(R.id.iv_item_transfer_gift_icon);
        helper.setImageByUrl(mIvIcon, item.getImg(), R.color.white);
        TextView mTvName = (TextView) helper.getViewById(R.id.tv_item_transfer_gift_name);
        TextView mTvPrice = (TextView) helper.getViewById(R.id.tv_item_transfer_gift_price);
        ImageView mIvCheck = (ImageView) helper.getViewById(R.id.iv_item_transfer_gift_check);
        mTvName.setText(item.getName());
        mTvPrice.setText(Integer.parseInt(item.getPrice()) / 100 + mContext.getString(R.string.youranbi));
        if (checkMap.get(helper.getPosition(), false)) {
            mIvCheck.setVisibility(View.VISIBLE);
            rlRoot.setBackgroundResource(R.drawable.shape_reward_gift_select);
        } else {
            mIvCheck.setVisibility(View.GONE);
            rlRoot.setBackgroundResource(R.drawable.shape_reward_gift_divider);
        }
    }
}
