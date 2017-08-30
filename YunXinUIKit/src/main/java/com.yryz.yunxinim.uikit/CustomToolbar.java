package com.yryz.yunxinim.uikit;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;

import android.view.ViewGroup;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.yryz.yunxinim.uikit.common.util.sys.ScreenUtil;


/**
 * Created by Administrator on 2017/1/19 0019.
 */
public class CustomToolbar extends Toolbar {

    private TextView tv_title;
    private ImageView iv;


    public CustomToolbar(Context context) {
        super(context);
        init();
    }

    public CustomToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

//    @Override
//    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//        if (Constants.topBarHeight!=0){
//            b = (int) Constants.topBarHeight;
//        }
//        super.onLayout(changed, l, t, r, b);
//    }

    private void init() {
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int layoutHightPix = (int) Constants.topBarHeight;
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(layoutHightPix, mode);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void setTitle(@StringRes int resId) {
        setTitle(getContext().getString(resId));
    }

    @Override
    public void setTitle(CharSequence title) {
        if (tv_title == null) {
            tv_title = new TextView(getContext());
            tv_title.setGravity(Gravity.CENTER);
            tv_title.setTextColor(getResources().getColor(R.color.color_black_333333));
//            tv_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, Constants.topBarTitleSize);
            tv_title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            tv_title.setLines(1);
            tv_title.setEllipsize(TextUtils.TruncateAt.END);
            Log.d("yeying", "settextSize is " + Constants.topBarTitleSize);
            this.addView(tv_title, new Toolbar.LayoutParams(ScreenUtil.getDisplayWidth() - ScreenUtil.dip2px(120), ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER | Gravity.CENTER));
        }
        tv_title.setText(title);
    }

    public void setNavigationIcon(@Nullable Drawable icon) {
        if (icon != null) {
            if (iv == null) {
                iv = new ImageView(getContext());
                iv.setPadding(0, 0, 40, 0);
                this.addView(iv, new Toolbar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.LEFT | Gravity.CENTER));
            }
            iv.setImageDrawable(icon);
        }
    }

    public void setNavigationOnClickListener(OnClickListener listener) {
        if (iv != null) {
            iv.setOnClickListener(listener);
        }
    }

}
