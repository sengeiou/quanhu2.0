package com.rz.circled.ui.view.circle_favite;


import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.view.View;

import com.rz.circled.R;
import com.rz.circled.application.QHApplication;
import com.rz.circled.modle.RateModel;
import com.rz.circled.widget.FavortListView;

import java.util.ArrayList;
import java.util.List;

public class FavortListAdapter {

    private FavortListView mListView;
    private List<RateModel> datas = new ArrayList<RateModel>();

    public List<RateModel> getDatas() {
        return datas;
    }

    private Context mContext;

    public FavortListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setDatas(List<RateModel> datas) {
        this.datas = datas;
    }

    @NonNull
    public void bindListView(FavortListView listview) {
        if (listview == null) {
            throw new IllegalArgumentException("FavortListView is null ....");
        }
        mListView = listview;
    }

    public int getCount() {
        if (datas != null && datas.size() > 0) {
            return datas.size();
        }
        return 0;
    }

    public Object getItem(int position) {
        if (datas != null && datas.size() > position) {
            return datas.get(position);
        }
        return null;
    }

    public long getItemId(int position) {
        return position;
    }

    public void notifyDataSetChanged() {
        if (mListView == null) {
            throw new NullPointerException("listview is null, please bindListView first...");
        }
        SpannableStringBuilder builder = new SpannableStringBuilder();
        if (datas != null && datas.size() > 0) {
            //添加点赞图标
            //     builder.append(setImageSpan());
            //builder.append("  ");
            for (int i = 0; i < datas.size(); i++) {
                final RateModel model = datas.get(i);
                if (model != null) {
                    SpannableString spanComName;
                    if (model.nameNotes != null && !model.nameNotes.equals("")) {
                        spanComName = new SpannableString(model.nameNotes);
                    } else {
                        spanComName = new SpannableString(model.custNname);
                    }
                    spanComName.setSpan(new ClickableSpan() {
                                            @Override
                                            public void onClick(View widget) {
                                            }

                                            @Override
                                            public void updateDrawState(TextPaint ds) {
                                                super.updateDrawState(ds);
                                                ds.setColor(Color.parseColor("#1bc2b8"));
                                                ds.setUnderlineText(false); //去掉下划线
                                            }
                                        }, 0, spanComName.length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    builder.append(spanComName);
                    if (i != datas.size() - 1) {
                        builder.append(", ");
                    }
                }
            }
        }
        mListView.setText(builder);
        //  mListView.setMovementMethod(new CircleMovementMethod(Color.parseColor("#1bc2b8")));
    }

    @NonNull
    private SpannableString setClickableSpan(String textStr, int position) {
        SpannableString subjectSpanText;
        if (textStr == null) return new SpannableString("");
        if (textStr.equals("")) {
            subjectSpanText = new SpannableString("");
        } else {
            subjectSpanText = new SpannableString(textStr);
        }

        subjectSpanText.setSpan(new NameClickable(mListView.getSpanClickListener(), position), 0, subjectSpanText.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return subjectSpanText;
    }

    private SpannableString setImageSpan() {
        String text = "  ";
        SpannableString imgSpanText = new SpannableString(text);
        imgSpanText.setSpan(new ImageSpan(QHApplication.getContext(), R.drawable.r_hom_love, DynamicDrawableSpan.ALIGN_BASELINE),
                0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return imgSpanText;
    }
}