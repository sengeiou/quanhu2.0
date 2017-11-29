package com.rz.circled.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import com.rz.circled.R;
import com.rz.common.adapter.CommonAdapter;
import com.rz.common.adapter.ViewHolder;
import com.rz.httpapi.bean.CircleEntrModle;

/**
 * Created by rzw2 on 2017/9/11.
 */

public class PrivateGroupBelongAdapter extends CommonAdapter<CircleEntrModle> {

    private String circleId;

    public PrivateGroupBelongAdapter(Context context, int layoutId) {
        super(context, layoutId);
    }

    @Override
    public void convert(ViewHolder helper, CircleEntrModle item, int position) {
        helper.setText(R.id.img, item.circleName);
        if (TextUtils.equals(circleId, item.appId)) {
            ((TextView) helper.getView(R.id.img)).setSelected(true);
        } else {
            ((TextView) helper.getView(R.id.img)).setSelected(false);
        }
    }

    public void setCircleId(String circleId) {
        this.circleId = circleId;
    }
}
