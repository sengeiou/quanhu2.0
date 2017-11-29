package com.rz.circled.adapter;

import android.content.Context;

import com.rz.circled.R;
import com.rz.common.adapter.CommonAdapter;
import com.rz.common.adapter.ViewHolder;
import com.rz.common.utils.StringUtils;
import com.rz.httpapi.bean.InviteRecordBean;
import com.rz.httpapi.bean.PrivateGroupBean;

/**
 * Created by rzw2 on 2017/11/20.
 */

public class InviteRecordAdapter extends CommonAdapter<InviteRecordBean.InviterDetailBean> {
    public InviteRecordAdapter(Context context, int layoutId) {
        super(context, layoutId);
    }

    @Override
    public void convert(ViewHolder helper, InviteRecordBean.InviterDetailBean item, int position) {
        helper.setText(R.id.tv_name, item.getCustNname());
        helper.setText(R.id.tv_time, StringUtils.stampToDate(item.getCustRegTime(), "yyyy-MM-dd HH:mm"));
    }
}
