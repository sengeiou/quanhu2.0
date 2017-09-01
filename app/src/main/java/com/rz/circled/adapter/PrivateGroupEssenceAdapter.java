package com.rz.circled.adapter;

import android.content.Context;

import com.rz.circled.R;
import com.rz.common.adapter.CommonAdapter;
import com.rz.common.adapter.ViewHolder;
import com.rz.httpapi.bean.PrivateGroupBean;

/**
 * Created by rzw2 on 2017/8/31.
 */

public class PrivateGroupEssenceAdapter extends CommonAdapter<PrivateGroupBean> {

    public PrivateGroupEssenceAdapter(Context context, int layoutId) {
        super(context, layoutId);
    }

    @Override
    public void convert(ViewHolder helper, PrivateGroupBean item, int position) {
        helper.setText(R.id.tv_title, item.getName());
        helper.setText(R.id.tv_desc, item.getOwnerName() + "  " + item.getOwnerIntro());
        helper.setText(R.id.tv_scan, String.format(mContext.getString(R.string.private_group_joined_user), item.getMemberNum()));
    }
}
