package com.rz.circled.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.bumptech.glide.Glide;
import com.rz.circled.R;
import com.rz.circled.widget.GlideCircleImage;
import com.rz.common.adapter.ViewHolder;
import com.rz.httpapi.bean.RewardModel;

import org.json.JSONObject;


/**
 * Created by Gsm on 2017/9/2.
 */
public class SearchRewardAdapter extends SearchCommonAdapter {

    public SearchRewardAdapter(Context context, int layoutId) {
        super(context, layoutId);
    }

    @Override
    public void convert(ViewHolder helper, Object item, int position) {

        RewardModel model = (RewardModel) item;
        ImageView avatarImg = (ImageView) helper.getViewById(R.id.iv_search_person);   //头像
        TextView rewardTxt = (TextView) helper.getViewById(R.id.tv_reward);            //悬赏金额
        TextView tvName = (TextView) helper.getViewById(R.id.tv_name);                  //悬赏人
        TextView tvContent = (TextView) helper.getViewById(R.id.tv_content);            //内容
        TextView tvTime = (TextView) helper.getViewById(R.id.tv_search_time);          //距离结束时间
        TextView tvPerNum = (TextView) helper.getViewById(R.id.tv_join_num);           //参与人数
        TextView tvStatus = (TextView) helper.getViewById(R.id.tv_search_status);       //悬赏状态
        TextView tvPlace = (TextView) helper.getViewById(R.id.reward_place);           //悬赏地址



        tvName.setText(model.getContent());
        Glide.with(mContext).load(model.getImgUrl()).transform(new GlideCircleImage(mContext)).into(avatarImg);

        rewardTxt.setText("悬赏金额 "+model.getPrice());
        tvContent.setText(model.getContent());

        if(!TextUtils.isEmpty(model.getLocation())){
            tvPlace.setText(model.getLocation());
        }

    }

    class ContentSource{
        String text;
        String image;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }


}
