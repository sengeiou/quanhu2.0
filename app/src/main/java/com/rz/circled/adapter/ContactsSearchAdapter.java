package com.rz.circled.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rz.circled.R;
import com.rz.circled.widget.GlideCircleImage;
import com.rz.circled.widget.pinyin.SearchPingyinEngine;
import com.rz.common.adapter.CommonAdapter;
import com.rz.common.adapter.ViewHolder;
import com.rz.common.utils.Protect;
import com.rz.common.utils.StringUtils;
import com.rz.common.utils.TextViewUtils;
import com.rz.httpapi.bean.FriendInformationBean;

/**
 * Created by rzw2 on 2017/9/19.
 */

public class ContactsSearchAdapter extends CommonAdapter<FriendInformationBean> {

    private String keyWord;

    public ContactsSearchAdapter(Context context, int layoutId) {
        super(context, layoutId);
    }

    @Override
    public void convert(ViewHolder helper, FriendInformationBean item, int position) {
        int[] indexs = SearchPingyinEngine.getkeyIndex(item, keyWord);
        String textStr = null;

        if (indexs[2] == 1) {
            textStr = item.getNameNotes();
        } else {
            textStr = item.getCustNname();
        }

        if (!StringUtils.isEmpty(textStr))
            if (indexs[0] != -1)
                TextViewUtils.setSpannableStyle(textStr, indexs[0], indexs[1], (TextView) helper.getViewById(R.id.id_search_key));
            else
                ((TextView) helper.getViewById(R.id.id_search_key)).setText(textStr);

        if (Protect.checkLoadImageStatus(mContext)) {
            Glide.with(mContext)
                    .load(item.getCustImg())
                    .transform(new GlideCircleImage(mContext))
                    .error(R.drawable.ic_default_head)
                    .into((ImageView) helper.getViewById(R.id.img_avatar));
        }
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }
}
