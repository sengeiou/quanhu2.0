package com.rz.circled.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rz.circled.R;
import com.rz.common.adapter.CommonAdapter;
import com.rz.common.adapter.ViewHolder;
import com.rz.httpapi.bean.CouponsBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Gsm on 2017/9/18.
 */
public class MyCouponsAdapter extends CommonAdapter<CouponsBean> {

    public MyCouponsAdapter(Context context, int layoutId) {
        super(context, layoutId);
    }

    private boolean canUse;

    public void setCanUse(boolean canUse) {
        this.canUse = canUse;
    }

    @Override
    public void convert(ViewHolder helper, CouponsBean item, int position) {
        helper.setText(R.id.tv_coupons_item_type, item.getPrizesName());
        helper.setText(R.id.tv_coupons_item_title, item.getRemark());
        helper.setText(R.id.tv_coupons_item_number, item.getCanNum());
        helper.setText(R.id.tv_coupons_item_time, formatTime(item.getBeginTime()) + " - " + formatTime(item.getEndTime()));
        TextView tv_coupons_title = helper.getView(R.id.tv_coupons_item_title);
        TextView tvStatus = (TextView) helper.getViewById(R.id.tv_coupons_item_status);
        RelativeLayout rlLeft = (RelativeLayout) helper.getViewById(R.id.rl_coupons_item_left);
        if (canUse) {
            tvStatus.setText(R.string.can_use);
            tvStatus.setBackgroundResource(R.drawable.shape_coupons_blue);
            rlLeft.setBackgroundResource(R.mipmap.bg_coupons_normal_item);
        } else {
            if (item.getStatus() == 2)
                tvStatus.setText(R.string.used);
            else tvStatus.setText(R.string.expired);
            tv_coupons_title.setTextColor(ContextCompat.getColor(mContext, R.color.color_666666));
            tvStatus.setBackgroundResource(R.drawable.shape_coupons_gray);
            tvStatus.setTextColor(ContextCompat.getColor(mContext, R.color.font_gray_s));
            rlLeft.setBackgroundResource(R.mipmap.bg_coupons_expired_item);
        }
    }

    private String formatTime(String oldTime) {
        SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = oldFormat.parse(oldTime);
            SimpleDateFormat newFormat = new SimpleDateFormat("yyyy.MM.dd");
            String format = newFormat.format(date.getTime());
            return format;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return oldTime;
    }


}
