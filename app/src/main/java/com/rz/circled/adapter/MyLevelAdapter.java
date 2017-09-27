package com.rz.circled.adapter;

import android.content.Context;
import android.graphics.Color;
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
        TextView tv_grow = helper.getView(R.id.tv_level_item_number);
        int newGrow = item.getNewGrow();
        if (newGrow > 0 ){
            tv_grow.setText("+" +newGrow);
            tv_grow.setTextColor(mContext.getResources().getColor(R.color.font_color_blue));
        }else {
            tv_grow.setTextColor(mContext.getResources().getColor(R.color.color_fb4545));
            tv_grow.setText("-" +newGrow);
        }
        if (position % 2 == 0)
            helper.getConvertView().setBackgroundResource(R.color.white);
        else
            helper.getConvertView().setBackgroundResource(R.color.color_f6fbfd);
    }

}
