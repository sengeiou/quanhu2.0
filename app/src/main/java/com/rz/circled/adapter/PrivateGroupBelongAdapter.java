package com.rz.circled.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.rz.circled.R;
import com.rz.common.adapter.CommonAdapter;
import com.rz.common.adapter.ViewHolder;
import com.rz.common.utils.Protect;
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
        helper.setImageByUrl((ImageView) helper.getView(R.id.img), item.circleIcon, R.mipmap.icon_logo);
        if (TextUtils.equals(circleId, item.appId)) {
            ((CheckBox) helper.getView(R.id.cbx)).setChecked(true);
        } else {
            ((CheckBox) helper.getView(R.id.cbx)).setChecked(false);
        }
    }

    public void setCircleId(String circleId) {
        this.circleId = circleId;
    }
}
