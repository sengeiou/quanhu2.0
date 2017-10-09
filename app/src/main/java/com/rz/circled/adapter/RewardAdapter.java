package com.rz.circled.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rz.circled.R;
import com.rz.circled.widget.GlideCircleImage;
import com.rz.common.adapter.CommonAdapter;
import com.rz.common.adapter.ViewHolder;
import com.rz.common.utils.TimeUtil;
import com.rz.httpapi.bean.MyRewardBean;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by Gsm on 2017/9/2.
 */
public class RewardAdapter extends CommonAdapter {

    public RewardAdapter(Context context, int layoutId) {
        super(context, layoutId);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void convert(ViewHolder helper, Object item, int position) {

        MyRewardBean model = (MyRewardBean) item;

//        RelativeLayout bgLayout = (RelativeLayout) helper.getViewById(R.id.reward_bg_layout);
        ImageView avatarImg = (ImageView) helper.getViewById(R.id.iv_search_person);   //头像
        TextView rewardTxt = (TextView) helper.getViewById(R.id.tv_reward);            //悬赏金额
        TextView tvName = (TextView) helper.getViewById(R.id.tv_name);                  //悬赏人
        TextView tvContent = (TextView) helper.getViewById(R.id.tv_content);            //内容
        TextView tvTime = (TextView) helper.getViewById(R.id.tv_search_time);          //距离结束时间
        TextView tvPerNum = (TextView) helper.getViewById(R.id.tv_join_num);           //参与人数
        TextView tvStatus = (TextView) helper.getViewById(R.id.tv_search_status);       //悬赏状态
        TextView tvPlace = (TextView) helper.getViewById(R.id.reward_place);           //悬赏地址
        LinearLayout placeLayout = (LinearLayout) helper.getViewById(R.id.place_layout);
        ImageView picImg = (ImageView) helper.getViewById(R.id.pic_logo_img);


        if(!TextUtils.isEmpty(model.getUser().getCustNname())){
            tvName.setText(model.getUser().getCustNname());
        }

        Glide.with(mContext).load(model.getUser().getCustImg()).transform(new GlideCircleImage(mContext)).
                placeholder(R.drawable.ic_default_head).error(R.drawable.ic_default_head).crossFade().into(avatarImg);

        if(!TextUtils.isEmpty(model.getPrice())){
            float price = Float.valueOf(model.getPrice());
            DecimalFormat fnum = new DecimalFormat("##0.00");
            String  dd = fnum.format(price/100);
            rewardTxt.setText("悬赏金额 "+dd);
        }else{
            rewardTxt.setText("悬赏金额 0.00");
        }

        if(!TextUtils.isEmpty(model.getContent())){
            tvContent.setText(model.getContent());
        }

        if(!TextUtils.isEmpty(model.getLocation())){
            placeLayout.setVisibility(View.VISIBLE);
            tvPlace.setText(model.getLocation());
        }else{
            placeLayout.setVisibility(View.GONE);
        }

        if( TextUtils.isEmpty(model.getPics())){
            picImg.setVisibility(View.GONE);

//            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//            lp.setMargins(2, 0, 0, 0);
//
//            tvTime.setLayoutParams(lp);

        }else{
            picImg.setVisibility(View.VISIBLE);
        }

        //获取当前时间
        Date d = new Date();
        System.out.println(d);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateNowStr = sdf.format(d);

        //判断悬赏是否在进行中
        if(model.getComplete() == 1){
            tvStatus.setTextColor(mContext.getColor(R.color.colorAccent));
            tvStatus.setText("进行中");
            tvTime.setVisibility(View.VISIBLE);

            String time = TimeUtil.getTime(dateNowStr,model.getTerminalTime());
            String timeArray[] = time.replace("-","").split(",");
            String time1 = timeArray[0];
            String time2 = timeArray[1];
            String time3 = timeArray[2];

            if(Integer.parseInt(time1)>0){
                tvTime.setText("还剩" + time1+"天" + time2 + "时" + time3 + "分");
            }else{
                if(Integer.parseInt(time2)>0){
                    tvTime.setText("还剩" + time2 + "时" + time3 + "分");
                }else{
                    tvTime.setText("还剩" + time3 + "分");
                }
            }
        }else if(model.getComplete() == 2){
            tvStatus.setTextColor(ContextCompat.getColor(mContext,R.color.font_gray_m));
            tvStatus.setText("已完成");
            tvTime.setVisibility(View.GONE);
        }else if(model.getComplete() == 3){
            tvStatus.setTextColor(ContextCompat.getColor(mContext,R.color.font_gray_m));
            tvTime.setVisibility(View.GONE);
            tvStatus.setText("已过期");
        }

        if(model.getReplyNum() == 0){
            tvPerNum.setVisibility(View.GONE);
        }else{
            tvPerNum.setVisibility(View.VISIBLE);
            tvPerNum.setText("该悬赏有"+model.getReplyNum()+"个回答");
        }
    }

}
