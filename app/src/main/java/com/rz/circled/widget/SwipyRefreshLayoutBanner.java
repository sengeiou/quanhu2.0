package com.rz.circled.widget;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.rz.common.swiperefresh.SwipyRefreshLayout;

/**
 * Created by Gsm on 2017/9/7.
 */
public class SwipyRefreshLayoutBanner extends SwipyRefreshLayout {


    float lastx = 0;
    float lasty = 0;
    boolean ismovepic = false;

    public SwipyRefreshLayoutBanner(Context context) {
        super(context);
    }

    public SwipyRefreshLayoutBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {


        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            lastx = ev.getX();
            lasty = ev.getY();
            ismovepic = false;
            return super.onInterceptTouchEvent(ev);
        }

        final int action = MotionEventCompat.getActionMasked(ev);
        Log.v("ResfreshLayoutBanner", ev.getX() + "---" + ev.getY());

        int x2 = (int) Math.abs(ev.getX() - lastx);
        int y2 = (int) Math.abs(ev.getY() - lasty);

        //滑动图片最小距离检查
        Log.v("ResfreshLayoutBanner", "滑动差距 - >" + x2 + "--" + y2);
        if (x2 > y2) {
            if (x2 >= 100) ismovepic = true;
            return false;
        }

        //是否移动图片(下拉刷新不处理)
        if (ismovepic) {
            Log.v("ResfreshLayoutBanner", "滑动差距 - >" + x2 + "--" + y2);
            return false;
        }

        boolean isok = super.onInterceptTouchEvent(ev);

        Log.v("ResfreshLayoutBanner", "isok ->" + isok);

        return isok;
    }
}
