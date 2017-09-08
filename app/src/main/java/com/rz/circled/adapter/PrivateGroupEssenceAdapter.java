package com.rz.circled.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.rz.circled.R;
import com.rz.common.adapter.CommonAdapter;
import com.rz.common.adapter.ViewHolder;
import com.rz.common.utils.Protect;
import com.rz.httpapi.bean.PrivateGroupBean;
import com.rz.httpapi.bean.PrivateGroupResourceBean;

/**
 * Created by rzw2 on 2017/8/31.
 */

public class PrivateGroupEssenceAdapter extends CommonAdapter<PrivateGroupResourceBean> {

    public PrivateGroupEssenceAdapter(Context context, int layoutId) {
        super(context, layoutId);
    }

    @Override
    public void convert(ViewHolder helper, PrivateGroupResourceBean item, int position) {
        helper.setText(R.id.tv_title, item.getOwnerName() + "  " + item.getOwnerIntro());
        helper.setText(R.id.tv_desc, item.getTitle());
        helper.setText(R.id.tv_scan, String.format(mContext.getString(R.string.private_group_essence_scan), item.getReadNum()));
        if (Protect.checkLoadImageStatus(mContext))
            Glide.with(mContext).load(item.getCustImg()).error(R.mipmap.icon_logo).into((ImageView) helper.getView(R.id.avatar));
    }
}
