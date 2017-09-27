package com.rz.circled.adapter;

import android.content.Context;
import android.widget.TextView;

import com.rz.circled.R;
import com.rz.common.adapter.CommonAdapter;
import com.rz.common.adapter.ViewHolder;
import com.rz.common.utils.StringUtils;
import com.rz.httpapi.bean.MyLevelBean;

/**
 * Created by Gsm on 2017/9/19.
 */
public class MyLevelAdapter extends CommonAdapter<MyLevelBean> {
    public MyLevelAdapter(Context context, int layoutId) {
        super(context, layoutId);
    }

    @Override
    public void convert(ViewHolder helper, MyLevelBean item, int position) {
        helper.setText(R.id.tv_level_item_resource, item.getEventName());
        helper.setText(R.id.tv_level_item_time, StringUtils.getDateTimeFromMillisecondNo3(StringUtils.toDate(item.getUpdateTime()).getTime()));
        helper.setText(R.id.tv_level_item_number, item.getNewGrow() > 0 ? "+" + item.getNewGrow() : String.valueOf(item.getNewGrow()));
        if (item.getNewGrow() > 0)
            ((TextView) helper.getView(R.id.tv_level_item_number)).setTextColor(mContext.getResources().getColor(R.color.font_color_blue));
        else
            ((TextView) helper.getView(R.id.tv_level_item_number)).setTextColor(mContext.getResources().getColor(R.color.color_fb4545));
        if (position % 2 == 0)
            helper.getConvertView().setBackgroundResource(R.color.white);
        else
            helper.getConvertView().setBackgroundResource(R.color.color_f6fbfd);
    }

}
