package com.rz.circled.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rz.circled.R;
import com.rz.common.adapter.CommonAdapter;
import com.rz.common.adapter.ViewHolder;
import com.rz.common.utils.Protect;
import com.rz.common.utils.TextViewUtils;
import com.rz.httpapi.bean.PrivateGroupBean;

/**
 * Created by rzw2 on 2017/8/31.
 */

public class DefaultPricePrivateGroupAdapter extends DefaultPrivateGroupAdapter {
    private String keyWord;

    public DefaultPricePrivateGroupAdapter(Context context, int layoutId, int type) {
        super(context, layoutId, type);
    }

    @Override
    public void convert(ViewHolder helper, PrivateGroupBean item, int position) {
        super.convert(helper, item, position);
        TextView tvStatus = helper.getView(R.id.tv_status);
        if (item.getConsultingFee() == 0) {
            tvStatus.setText(R.string.private_group_free);
            tvStatus.setTextColor(mContext.getResources().getColor(R.color.font_color_blue));
        } else {
            tvStatus.setText(String.format(mContext.getString(R.string.private_group_price), item.getConsultingFee()));
            tvStatus.setTextColor(mContext.getResources().getColor(R.color.color_F5CD45));
        }
//        ((TextView) helper.getView(R.id.tv_title)).setText(TextUtils.isEmpty(item.getName()) ? "" : TextViewUtils.getSpan(item.getName(), keyWord));
        ((TextView) helper.getView(R.id.tv_title)).setText(item.getName());
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }
}
