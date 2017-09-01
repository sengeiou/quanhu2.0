package com.rz.common.utils;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.rz.common.R;
import com.rz.common.application.BaseApplication;
/**
 * 作者：Administrator on 2016/8/17 0017 11:55
 * 功能：文字处理工具
 * 说明：
 */
public class TextViewUtils {

    private static SpannableString spannableString;

    public static int mColor = R.color.color_main;

    /**
     * 初始化spannableString
     *
     * @param str
     * @return
     */
    public static void getInstance(String str) {
        if (!StringUtils.isEmpty(str)) {
            spannableString = new SpannableString(str);
        }
    }

    public static void setSpannableStyle(String str, int start, int end, TextView textView) {
        Spannable span = new SpannableString(str);
        span.setSpan(new ForegroundColorSpan(BaseApplication.getContext().getResources().getColor(R.color.color_23CDC3)), start,
                end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(span);
    }

    public static void setSpannableStyle01(String str, int start, int end, TextView textView) {
        Spannable span = new SpannableString(str);
        span.setSpan(new ForegroundColorSpan(BaseApplication.getContext().getResources().getColor(R.color.F7B85D)), start,
                end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(span);
    }


    public static void setSpannableStyleColor(String str, int start, int end, TextView textView, int color) {
        Spannable span = new SpannableString(str);
        span.setSpan(new ForegroundColorSpan(BaseApplication.getContext().getResources().getColor(color)), start,
                end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(span);
    }

    /**
     * 设置文字下标变色范围
     */
    public static void setTxtIndex(int start, int end, final TextViewOnClickListener mListener) {
        if (null != spannableString) {
            spannableString.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View view) {
                    if (null != mListener) {
                        mListener.onTextClick(view);
                    }
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(BaseApplication.getContext().getResources().getColor(mColor));
                    ds.setUnderlineText(false);
                }
            }, start, end, Spannable.SPAN_MARK_MARK);
        }
    }

    /**
     * 设置文字下标变色范围
     */
    public static void setTxtIndexColor(int start, int end, final TextViewOnClickListener mListener, final int color) {
        if (null != spannableString) {
            spannableString.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View view) {
                    if (null != mListener) {
                        mListener.onTextClick(view);
                    }
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(BaseApplication.getContext().getResources().getColor(color));
                    ds.setUnderlineText(false);
                }
            }, start, end, Spannable.SPAN_MARK_MARK);
        }
    }

    /**
     * 设置textview显示样式
     *
     * @param textView
     */
    public static void setSpannableStyle(TextView textView) {
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setHighlightColor(BaseApplication.getContext().getResources().getColor(android.R.color.transparent));
        textView.setText(spannableString);
    }

    public TextViewOnClickListener mListener;

    public interface TextViewOnClickListener {
        void onTextClick(View view);
    }
}
