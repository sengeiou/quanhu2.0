package com.rz.circled.adapter;

import android.content.Context;
import android.view.View;

import com.rz.circled.R;
import com.rz.common.adapter.CommonAdapter;
import com.rz.common.adapter.ViewHolder;
import com.rz.httpapi.bean.NewsOverviewBean;

/**
 * Created by rzw2 on 2017/9/12.
 */

public class NewsOverviewAdapter extends CommonAdapter<NewsOverviewBean> {

    public NewsOverviewAdapter(Context context, int layoutId) {
        super(context, layoutId);
    }

    @Override
    public void convert(ViewHolder helper, NewsOverviewBean item, int position) {
        helper.setImageResource(R.id.img, item.getResId());
        helper.setText(R.id.tv_title, item.getTitle());
        helper.setText(R.id.tv_desc, item.getDesc());
        helper.setText(R.id.tv_time, item.getTime());
        if (item.getNum() == 0) {
            helper.getView(R.id.dot).setVisibility(View.GONE);
        } else {
            helper.getView(R.id.dot).setVisibility(View.VISIBLE);
        }
    }
}

