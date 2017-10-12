package com.rz.circled.adapter;

import android.content.Context;
import android.text.TextUtils;
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
        PrivateGroupResourceBean.Cust cust = item.getCust();
        helper.setText(R.id.tv_title, cust.getCustNname() + "  " + (TextUtils.isEmpty(cust.getCustDesc()) ? "" : cust.getCustDesc()));
        helper.setText(R.id.tv_desc, item.getTitle());
        helper.setText(R.id.tv_scan, String.format(mContext.getString(R.string.private_group_essence_scan), item.getReadNum()));
        if (Protect.checkLoadImageStatus(mContext))
            Glide.with(mContext).load(cust.getCustImg()).error(R.mipmap.ic_default_avatar_small).into((ImageView) helper.getView(R.id.avatar));
    }
}
