package com.rz.circled.adapter;

import android.content.Context;
import android.widget.TextView;

import com.rz.circled.R;
import com.rz.common.adapter.CommonAdapter;
import com.rz.common.adapter.ViewHolder;
import com.rz.common.utils.StringUtils;
import com.rz.httpapi.bean.CouponsBean;

/**
 * Created by Gsm on 2017/9/18.
 */
public class MyCouponsAdapter extends CommonAdapter<CouponsBean> {
    public MyCouponsAdapter(Context context, int layoutId) {
        super(context, layoutId);
    }

    @Override
    public void convert(ViewHolder helper, CouponsBean item, int position) {
        helper.setText(R.id.tv_coupons_item_type, item.getVoucherTypeName());
        helper.setText(R.id.tv_coupons_item_title, item.getVoucherDesc());
        helper.setText(R.id.tv_coupons_item_time,
                StringUtils.getDateTimeFromMillisecondNo3(item.getReceiveTime()) + "-" + StringUtils.getDateTimeFromMillisecondNo3(item.getExpireTime()));
        TextView tvStatus = (TextView) helper.getViewById(R.id.tv_coupons_item_status);
        if (item.canUse()) {
            tvStatus.setText(R.string.can_use);
            tvStatus.setBackgroundResource(R.drawable.shape_coupons_blue);
        } else {
            if (item.getStatus() == 1)
                tvStatus.setText(R.string.used);
            else tvStatus.setText(R.string.expired);
            tvStatus.setBackgroundResource(R.drawable.shape_coupons_gray);
        }
    }
}
