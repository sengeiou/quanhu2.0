package com.rz.circled.adapter;

import android.content.Context;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rz.circled.R;
import com.rz.circled.widget.GlideCircleImage;
import com.rz.common.adapter.CommonAdapter;
import com.rz.common.adapter.ViewHolder;
import com.rz.httpapi.bean.MineRewardBean;

/**
 * Created by Administrator on 2017/9/19 0019.
 */

public class MineRewardAdapter extends CommonAdapter {

    public MineRewardAdapter(Context context, int layoutId) {
        super(context, layoutId);
    }

    @Override
    public void convert(ViewHolder helper, Object item, int position) {

        MineRewardBean model = (MineRewardBean) item;
        ImageView avatar = (ImageView) helper.getViewById(R.id.avatar);
        TextView tvName = (TextView) helper.getViewById(R.id.tv_name);
        TextView tvTime = (TextView) helper.getViewById(R.id.tv_time);
        TextView tvTitle = (TextView) helper.getViewById(R.id.tv_title);
        ImageView rewardImg = (ImageView) helper.getViewById(R.id.reward_img);
        TextView tvContent = (TextView) helper.getViewById(R.id.tv_content);


        if(model.getUser()!= null && model.getUser().getCustImg() != null){
            Glide.with(mContext).load(model.getUser().getCustImg()).transform(new GlideCircleImage(mContext)).into(avatar);
        }

        tvName.setText(model.getUser().getCustNname());
        tvTime.setText(model.getCreateTime()+"");
        tvTitle.setText( model.getUser().getCustNname() +"打赏了价值" + model.getRewardPrice() +"("+ model.getGiftInfo().getName()+")给");

        if(model.getResourceInfo().getPics() != null){
            Glide.with(mContext).load(model.getResourceInfo().getPics()).into(rewardImg);
        }
        tvContent.setText(model.getResourceInfo().getContent());

    }
}
