package com.rz.circled.adapter;

import android.content.Context;
import android.widget.TextView;

import com.rz.circled.R;
import com.rz.common.adapter.ViewHolder;
import com.rz.httpapi.bean.CircleEntrModle;

/**
 * Created by Gsm on 2017/9/2.
 */
public class SearchCircleAdapter extends SearchCommonAdapter {

    public SearchCircleAdapter(Context context, int layoutId) {
        super(context, layoutId);
    }

    @Override
    public void convert(ViewHolder helper, Object item, int position) {
        TextView tvName = (TextView) helper.getViewById(R.id.tv_choose_circle_item);
        CircleEntrModle model = (CircleEntrModle) item;
        tvName.setSelected(model.isSeleced());
        tvName.setText("");
        tvName.append(getSpan(model.circleName));
    }
}
