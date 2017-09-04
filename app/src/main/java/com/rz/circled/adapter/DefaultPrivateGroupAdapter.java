package com.rz.circled.adapter;

import android.content.Context;

import com.rz.circled.R;
import com.rz.common.adapter.CommonAdapter;
import com.rz.common.adapter.ViewHolder;
import com.rz.httpapi.bean.PrivateGroupBean;

/**
 * Created by rzw2 on 2017/8/31.
 */

public class DefaultPrivateGroupAdapter extends CommonAdapter<PrivateGroupBean> {

    public static final int TYPE_SCAN = 0;
    public static final int TYPE_DESC = 1;

    private int type;
    private String keyWord;

    public DefaultPrivateGroupAdapter(Context context, int layoutId, int type) {
        super(context, layoutId);
        this.type = type;
    }

    @Override
    public void convert(ViewHolder helper, PrivateGroupBean item, int position) {
//        helper.setText(R.id.tv_title, item.getName());
//        helper.setText(R.id.tv_desc, item.getOwnerName() + "  " + item.getOwnerIntro());
//        helper.setText(R.id.tv_scan, type == TYPE_SCAN ? String.format(mContext.getString(R.string.private_group_joined_user), item.getMemberNum()) : item.getIntro());
//        helper.setText(R.id.tv_from, item.getCircleId());
//        switch (item.getStatus()) {
//            case 0:
//                helper.setText(R.id.tv_status, mContext.getString(R.string.private_group_status_0));
//                break;
//            case 1:
//                helper.setText(R.id.tv_status, mContext.getString(R.string.private_group_status_1));
//                break;
//            case 2:
//                helper.setText(R.id.tv_status, mContext.getString(R.string.private_group_status_2));
//                break;
//            case 3:
//                helper.setText(R.id.tv_status, mContext.getString(R.string.private_group_status_3));
//                break;
//            case 4:
//                helper.setText(R.id.tv_status, mContext.getString(R.string.private_group_status_4));
//                break;
//        }
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
        notifyDataSetChanged();
    }
}
