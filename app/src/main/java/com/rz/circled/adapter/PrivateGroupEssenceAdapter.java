package com.rz.circled.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rz.circled.BuildConfig;
import com.rz.circled.R;
import com.rz.circled.ui.activity.WebContainerActivity;
import com.rz.common.adapter.CommonAdapter;
import com.rz.common.adapter.ViewHolder;
import com.rz.common.utils.Protect;
import com.rz.httpapi.bean.PrivateGroupResourceBean;

/**
 * Created by rzw2 on 2017/8/31.
 */

public class PrivateGroupEssenceAdapter extends CommonAdapter<PrivateGroupResourceBean> {

    public PrivateGroupEssenceAdapter(Context context, int layoutId) {
        super(context, layoutId);
    }

    @Override
    public void convert(ViewHolder helper, final PrivateGroupResourceBean item, int position) {
        PrivateGroupResourceBean.Cust cust = item.getCust();
        TextView tvContent = (TextView) helper.getViewById(R.id.tv_desc);
//        helper.setText(R.id.tv_title, cust.getCustNname() + "  " + (TextUtils.isEmpty(item.getOwnerIntro()) ? "" : item.getOwnerIntro()));
        helper.setText(R.id.tv_title, cust.getCustNname());
        tvContent.setVisibility(View.VISIBLE);
        tvContent.setText(TextUtils.isEmpty(item.getTitle()) ? item.getSummary() : item.getTitle());
        if (TextUtils.isEmpty(tvContent.getText().toString().trim())) {
            if (TextUtils.isEmpty(item.getAudio())) {
                tvContent.setVisibility(View.GONE);
            } else {
                tvContent.setVisibility(View.VISIBLE);
                tvContent.setText("[语音]");
            }
        }
        helper.setText(R.id.tv_scan, String.format(mContext.getString(R.string.private_group_essence_scan), item.getReadNum()));
        if (Protect.checkLoadImageStatus(mContext))
            Glide.with(mContext).load(cust.getCustImg()).error(R.mipmap.ic_default_avatar_small).into((ImageView) helper.getView(R.id.avatar));
        TextView tvFrom = (TextView) helper.getViewById(R.id.tv_private_group_essence_from);
        tvFrom.setText("来自私圈 " + item.getCoterieName());
        tvFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebContainerActivity.startActivity(mContext, BuildConfig.WebHomeBaseUrl + "/" + item.getCircleRoute() + "/coterie/" + item.getCoterieId());
            }
        });
    }
}
