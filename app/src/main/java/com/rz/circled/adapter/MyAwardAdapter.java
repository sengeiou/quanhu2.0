package com.rz.circled.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rz.circled.R;
import com.rz.common.adapter.CommonAdapter;
import com.rz.common.adapter.ViewHolder;
import com.rz.common.widget.MyListView;
import com.rz.httpapi.bean.CouponsBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Gsm on 2017/9/18.
 */
public class MyAwardAdapter extends CommonAdapter<CouponsBean> {
    public MyAwardAdapter(Context context, int layoutId) {
        super(context, layoutId);
    }

    private MyListView mListView;

    private boolean canUse;

    public void setParentView(MyListView listView) {
        mListView = listView;
    }

    public void setCanUse(boolean canUse) {
        this.canUse = canUse;
    }

    @Override
    public void convert(ViewHolder helper, final CouponsBean item, int position) {
        helper.setText(R.id.tv_award_item_type, item.getPrizesName());
        helper.setText(R.id.tv_award_item_title, item.getOnlyCode());
        helper.setText(R.id.tv_award_item_time, formatTime(item.getBeginTime()) + " - " + formatTime(item.getEndTime()));
        helper.setText(R.id.tv_award_item_detail, item.getRemark());
        helper.setText(R.id.tv_award_item_unit, item.getPrizesUnit());
        TextView tvStatus = (TextView) helper.getViewById(R.id.tv_award_item_status);
        RelativeLayout rlLeft = (RelativeLayout) helper.getViewById(R.id.rl_award_item_left);
        final TextView tvDetail = (TextView) helper.getViewById(R.id.tv_award_item_detail);
        View viewLine = helper.getViewById(R.id.view_line);
        tvDetail.setVisibility(item.showDetail ? View.VISIBLE : View.GONE);
        viewLine.setVisibility(item.showDetail ? View.VISIBLE : View.GONE);
        if (canUse) {
            tvStatus.setText(R.string.can_use);
            tvStatus.setBackgroundResource(R.drawable.shape_coupons_blue);
            rlLeft.setBackgroundResource(R.mipmap.bg_award_normal_item);
        } else {
            if (item.getStatus() == 2)
                tvStatus.setText(R.string.used);
            else tvStatus.setText(R.string.expired);
            tvStatus.setTextColor(ContextCompat.getColor(mContext, R.color.font_gray_s));
            tvStatus.setBackgroundResource(R.drawable.shape_coupons_gray);
            rlLeft.setBackgroundResource(R.mipmap.bg_coupons_expired_item);
        }
        TextView tvShow = (TextView) helper.getViewById(R.id.tv_award_item_detail_show);
        tvShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.showDetail = !item.showDetail;
                updateSingleItem(item.getId());
            }
        });
        Drawable drawable = ContextCompat.getDrawable(mContext, item.showDetail ? R.mipmap.icon_award_arrow_up : R.mipmap.icon_award_arrow_down);
        /// 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tvShow.setCompoundDrawables(null, null, drawable, null);
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

    public void updateSingleItem(int id) {
        if (mListView != null) {
            int start = mListView.getFirstVisiblePosition();
            for (int i = start, j = mListView.getLastVisiblePosition(); i <= j; i++)
                if (id == ((CouponsBean) mListView.getItemAtPosition(i)).getId()) {
                    View view = mListView.getChildAt(i - start);
                    getView(i, view, mListView);
                    break;
                }
        }
    }
}
