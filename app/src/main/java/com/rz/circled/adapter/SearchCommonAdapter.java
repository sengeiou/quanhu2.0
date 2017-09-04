package com.rz.circled.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import com.rz.circled.R;
import com.rz.common.adapter.CommonAdapter;

/**
 * Created by Gsm on 2017/9/2.
 */
public abstract class SearchCommonAdapter extends CommonAdapter {

    private String keyWord = "";

    public SearchCommonAdapter(Context context, int layoutId) {
        super(context, layoutId);
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    protected SpannableString getSpan(String content) {
        SpannableString span = new SpannableString(content);
        if (content.contains(keyWord)) {
            String[] split = content.split(keyWord);
            int colorId = ContextCompat.getColor(mContext, R.color.font_color_blue);
            int startIndex = 0;
            if (content.startsWith(keyWord)) {
                span.setSpan(new ForegroundColorSpan(colorId), startIndex, keyWord.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }
            for (int i = 0; i < split.length; i++) {
                if (startIndex == 0)
                    startIndex = startIndex + split[i].length();
                else startIndex = startIndex + split[i].length() + keyWord.length();
                span.setSpan(new ForegroundColorSpan(colorId), startIndex, keyWord.length() + startIndex, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }
            return span;
        }
        return span;
    }
}
