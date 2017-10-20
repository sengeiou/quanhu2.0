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
import com.rz.common.adapter.ViewHolder;
import com.rz.common.utils.StringFormatUtil;
import com.rz.common.utils.TimeUtil;
import com.rz.httpapi.bean.RewardModel;
import com.yryz.yunxinim.uikit.common.util.string.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by Gsm on 2017/9/2.
 */
public class SearchRewardAdapter extends SearchCommonAdapter {
    private String keyWord = "";
    StringFormatUtil stringFormatUtil;

    public SearchRewardAdapter(Context context, int layoutId) {
        super(context, layoutId);
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
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
        LinearLayout placeLayout = (LinearLayout) helper.getViewById(R.id.place_layout);
        ImageView picImg = (ImageView) helper.getViewById(R.id.pic_logo_img);


        if(StringUtil.isEmpty(keyWord)){
            tvName.setText(model.getCustSimpleDTO().getCustNname());
        }else{
            stringFormatUtil = new StringFormatUtil(mContext, model.getCustSimpleDTO().getCustNname(), keyWord, R.color.colorAccent).fillColor();
            if(stringFormatUtil != null && stringFormatUtil.getResult() != null){
                tvName.setText(stringFormatUtil.getResult().toString());
            }else{
                tvName.setText(model.getCustSimpleDTO().getCustNname());
            }

        }

        Glide.with(mContext).load(model.getCustSimpleDTO().getCustImg()).transform(new GlideCircleImage(mContext)).
                placeholder(R.drawable.ic_default_head).error(R.drawable.ic_default_head).crossFade().into(avatarImg);

        rewardTxt.setText("悬赏金额 "+model.getPrice());

        float price = Float.valueOf(model.getPrice());
        DecimalFormat fnum = new DecimalFormat("##0.00");
        String  dd = fnum.format(price/100);
        rewardTxt.setText("悬赏金额 "+dd);

        tvContent.setText(model.getContent());


        if(StringUtil.isEmpty(keyWord)){
            tvContent.setText(model.getContent());
        }else{
            stringFormatUtil = new StringFormatUtil(mContext, model.getContent(), keyWord, R.color.colorAccent).fillColor();
            if(stringFormatUtil != null && stringFormatUtil.getResult() != null){
                tvContent.setText(stringFormatUtil.getResult().toString());
            }else{
                tvContent.setText(model.getContent());
            }
        }

        if(!TextUtils.isEmpty(model.getLocation())){
            placeLayout.setVisibility(View.VISIBLE);
            tvPlace.setText(model.getLocation());
        }else{
            placeLayout.setVisibility(View.GONE);
        }

        if( TextUtils.isEmpty(model.getImgUrl())){
            picImg.setVisibility(View.GONE);
//            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
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

            tvStatus.setTextColor(ContextCompat.getColor(mContext, R.color.colorAccent));
            tvStatus.setText("进行中");
            tvTime.setVisibility(View.VISIBLE);

            String time = TimeUtil.getTime(dateNowStr,model.getTerminalTime());
            String timeArray[] = time.replace("-","").split(",");
            String time1 = timeArray[0];
            String time2 = timeArray[1];
            String time3 = timeArray[2];

            if(Integer.parseInt(time1)>0){
                tvTime.setText("还剩" + time1+"天" + time2 + "小时" + time3 + "分钟");
            }else{
                if(Integer.parseInt(time2)>0){
                    tvTime.setText("还剩" + time2 + "小时" + time3 + "分钟");
                }else{
                    if(Integer.parseInt(time3)<1){
                        tvTime.setText("还剩1分钟");
                    }else{
                        tvTime.setText("还剩" + time3 + "分钟");
                    }
                }
            }
//            tvTime.setText("还剩"+TimeUtil.getTime(dateNowStr,model.getTerminalTime()));
        }else if(model.getComplete() == 2){

            tvStatus.setTextColor(ContextCompat.getColor(mContext, R.color.colorAccent));
            tvStatus.setText("公示期");
            tvTime.setVisibility(View.GONE);
        }else if(model.getComplete() == 3){
            tvStatus.setTextColor(ContextCompat.getColor(mContext, R.color.font_gray_m));
            tvTime.setVisibility(View.GONE);
            tvStatus.setText("已完成");
        }else if(model.getComplete() == 4){
            tvStatus.setTextColor(ContextCompat.getColor(mContext, R.color.font_gray_m));
            tvTime.setVisibility(View.GONE);
            tvStatus.setText("已过期");
        }

        if(StringUtil.isEmpty(keyWord)){
            tvContent.setText(model.getContent());
        }else{
            stringFormatUtil = new StringFormatUtil(mContext, model.getContent(), keyWord, R.color.colorAccent).fillColor();
            if(stringFormatUtil != null && stringFormatUtil.getResult() != null) {
                tvContent.setText(stringFormatUtil.getResult());
            }else{
                tvContent.setText(model.getContent());
            }
        }

        if(model.getReplyNum() == 0){
            tvPerNum.setVisibility(View.GONE);
        }else{
            tvPerNum.setVisibility(View.VISIBLE);
            tvPerNum.setText(model.getReplyNum()+"人参加");
        }
    }

}
