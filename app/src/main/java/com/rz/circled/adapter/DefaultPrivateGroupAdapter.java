package com.rz.circled.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rz.circled.R;
import com.rz.common.adapter.CommonAdapter;
import com.rz.common.adapter.ViewHolder;
import com.rz.common.utils.Protect;
import com.rz.common.utils.StringFormatUtil;
import com.rz.httpapi.bean.PrivateGroupBean;

/**
 * Created by rzw2 on 2017/8/31.
 */

public class DefaultPrivateGroupAdapter extends CommonAdapter<PrivateGroupBean> {

    StringFormatUtil stringFormatUtil;
    public String keyWord = "";

    public static final int TYPE_SCAN = 0;
    public static final int TYPE_DESC = 1;

    private int type;

    public DefaultPrivateGroupAdapter(Context context, int layoutId, int type) {
        super(context, layoutId);
        this.type = type;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    @Override
    public void convert(ViewHolder helper, PrivateGroupBean item, int position) {

        if (TextUtils.isEmpty(keyWord)) {
            helper.setText(R.id.tv_title, item.getName());
        } else {
            stringFormatUtil = new StringFormatUtil(mContext, item.getName(), keyWord, R.color.colorAccent).fillColor();
            if (stringFormatUtil != null && stringFormatUtil.getResult() != null) {
                helper.setText(R.id.tv_title, stringFormatUtil.getResult().toString());
            } else {
                helper.setText(R.id.tv_title, item.getName());
            }
        }

//        helper.setText(R.id.tv_title, item.getName());

        if (TextUtils.isEmpty(keyWord)) {
            helper.setText(R.id.tv_desc, item.getOwnerName() + "  " + item.getOwnerIntro());
        } else {
            stringFormatUtil = new StringFormatUtil(mContext, item.getOwnerName() + "  " + item.getOwnerIntro(), keyWord, R.color.colorAccent).fillColor();
            if (stringFormatUtil != null && stringFormatUtil.getResult() != null) {
//                helper.setText(R.id.tv_title, stringFormatUtil.getResult().toString());

                helper.setText(R.id.tv_desc, stringFormatUtil.getResult().toString());
            } else {
                helper.setText(R.id.tv_desc, item.getOwnerName() + "  " + item.getOwnerIntro());
            }
        }
//        helper.setText(R.id.tv_desc, item.getOwnerName() + "  " + item.getOwnerIntro());

        if (Protect.checkLoadImageStatus(mContext))
            Glide.with(mContext).load(item.getIcon()).error(R.mipmap.ic_default_private_group_icon).into((ImageView) helper.getView(R.id.avatar));
        helper.setText(R.id.tv_scan, type == TYPE_SCAN ? String.format(mContext.getString(R.string.private_group_joined_user), item.getMemberNum()) : item.getIntro());
        helper.setText(R.id.tv_from, String.format(mContext.getString(R.string.private_group_from_circled), TextUtils.isEmpty(item.getCircleName()) ? "" : item.getCircleName()));
        TextView tvStatus = helper.getView(R.id.tv_status);
        TextView tvTitle = helper.getView(R.id.tv_title);
        switch (item.getStatus()) {
            case 0:
                helper.getView(R.id.img_arrow).setVisibility(View.GONE);
                helper.setText(R.id.tv_status, mContext.getString(R.string.private_group_status_0));
                tvStatus.setTextColor(mContext.getResources().getColor(R.color.font_gray_m));
                tvTitle.setTextColor(mContext.getResources().getColor(R.color.font_gray_s));
                break;
            case 1:
                helper.getView(R.id.img_arrow).setVisibility(View.GONE);
                helper.setText(R.id.tv_status, mContext.getString(R.string.private_group_status_1));
                tvStatus.setTextColor(mContext.getResources().getColor(R.color.font_gray_m));
                tvTitle.setTextColor(mContext.getResources().getColor(R.color.font_gray_s));
                break;
            case 2:
                helper.getView(R.id.img_arrow).setVisibility(View.GONE);
                helper.setText(R.id.tv_status, mContext.getString(R.string.private_group_status_2));
                tvStatus.setTextColor(mContext.getResources().getColor(R.color.font_gray_m));
                tvTitle.setTextColor(mContext.getResources().getColor(R.color.font_gray_s));
                break;
            case 3:
                helper.getView(R.id.img_arrow).setVisibility(View.GONE);
                helper.setText(R.id.tv_status, mContext.getString(R.string.private_group_status_3));
                tvStatus.setTextColor(mContext.getResources().getColor(R.color.font_color_blue));
                tvTitle.setTextColor(mContext.getResources().getColor(R.color.font_gray_xl));
                break;
            case 4:
                helper.getView(R.id.img_arrow).setVisibility(View.GONE);
                helper.setText(R.id.tv_status, mContext.getString(R.string.private_group_status_4));
                tvStatus.setTextColor(mContext.getResources().getColor(R.color.font_gray_m));
                tvTitle.setTextColor(mContext.getResources().getColor(R.color.font_gray_s));
                break;
        }
    }

}
