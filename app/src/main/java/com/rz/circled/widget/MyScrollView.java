package com.rz.circled.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by Gsm on 2017/9/11.
 */
public class MyScrollView extends ScrollView {
    private OnScrollChanged mOnScrollChanged;

    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mOnScrollChanged != null)
            mOnScrollChanged.onScroll(l, t, oldl, oldt);
    }

    public void setOnScrollChanged(OnScrollChanged onScrollChanged) {
        this.mOnScrollChanged = onScrollChanged;
    }

    public interface OnScrollChanged {
        void onScroll(int scrollX, int scrollY, int oldX, int oldY);
    }
}
