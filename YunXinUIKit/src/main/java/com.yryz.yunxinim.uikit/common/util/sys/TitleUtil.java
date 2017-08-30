package com.yryz.yunxinim.uikit.common.util.sys;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yryz.yunxinim.uikit.R;

/**
 * Created by rzw2 on 2017/1/19.
 */

public class TitleUtil {
    private View mViewTitle;
    private TextView mTvTitle;
    private ImageView mIvRight;
    private ImageView mIvLeft;

    public TitleUtil(Activity context) {
        mViewTitle = context.findViewById(R.id.nim_titlebar_root);
        if(mViewTitle!=null){
            mIvLeft = (ImageView) mViewTitle.findViewById(R.id.nim_titlebar_back);
            mIvRight = (ImageView) mViewTitle.findViewById(R.id.nim_titlebar_more);
            mTvTitle = (TextView) mViewTitle.findViewById(R.id.nim_titlebar_title);
        }

    }

    public TitleUtil(View view) {
        mViewTitle = view.findViewById(R.id.nim_titlebar_root);
        mIvLeft = (ImageView) mViewTitle.findViewById(R.id.nim_titlebar_back);
        mIvRight = (ImageView) mViewTitle.findViewById(R.id.nim_titlebar_more);
        mTvTitle = (TextView) mViewTitle.findViewById(R.id.nim_titlebar_title);
    }

    //title
    public TitleUtil setTitleText(String text) {
        mTvTitle.setVisibility(TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);
        mTvTitle.setText(text);
        return this;
    }

    public TitleUtil setTitleText(int text) {
        mTvTitle.setVisibility(text == -1 ? View.GONE : View.VISIBLE);
        mTvTitle.setText(text);
        return this;
    }

    //left
    public TitleUtil setLeftImage(int resId) {
        mIvLeft.setVisibility(resId > 0 ? View.VISIBLE : View.GONE);
        if (mIvLeft.getVisibility() == View.VISIBLE)
            mIvLeft.setImageResource(resId);
        return this;
    }

    public TitleUtil setLeftOnClickListener(View.OnClickListener listener) {
        if (mIvLeft.getVisibility() == View.VISIBLE) {
            mIvLeft.setOnClickListener(listener);
        }
        return this;
    }

    //right
    public TitleUtil setRightImage(int resId) {
        mIvRight.setVisibility(resId > 0 ? View.VISIBLE : View.GONE);
        if (mIvRight.getVisibility() == View.VISIBLE)
            mIvRight.setImageResource(resId);
        return this;
    }

    public TitleUtil setRightOnClickListener(View.OnClickListener listener) {
        if (mIvRight.getVisibility() == View.VISIBLE) {
            mIvRight.setOnClickListener(listener);
        }
        return this;
    }

    public TitleUtil setBackground(int color) {
        mViewTitle.setBackgroundColor(color);
        return this;
    }

    public View build() {
        return mViewTitle;
    }
}
