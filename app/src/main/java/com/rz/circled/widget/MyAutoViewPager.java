package com.rz.circled.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
<<<<<<< HEAD
=======
import android.support.v4.view.PagerAdapter;
>>>>>>> 2540931ec03580503cb88e4fe7ef18497de3b69c
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

<<<<<<< HEAD
=======
import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

>>>>>>> 2540931ec03580503cb88e4fe7ef18497de3b69c
/**
 * Created by rzw2 on 2017/9/19.
 */

public class MyAutoViewPager extends ViewPager {
    public static final int DEFAULT_INTERVAL = 1500;
<<<<<<< HEAD
    public static final int SCROLL_WHAT = 0;
=======
>>>>>>> 2540931ec03580503cb88e4fe7ef18497de3b69c
    /**
     * auto scroll time in milliseconds, default is {@link #DEFAULT_INTERVAL}
     **/
    private long interval = DEFAULT_INTERVAL;
    private Handler handler;
<<<<<<< HEAD
=======
    public static final int SCROLL_WHAT = 0;
>>>>>>> 2540931ec03580503cb88e4fe7ef18497de3b69c
    private boolean isAutoScroll = false;

    private ScrollView parentScrollView;

    public MyAutoViewPager(Context paramContext) {
        super(paramContext);
        init();
    }

    public MyAutoViewPager(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        init();
    }

    private void init() {
        handler = new MyHandler();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            default:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    /**
     * 是否把滚动事件交给父scrollview
     *
     * @param flag
     */
    private void setParentScrollAble(boolean flag) {
        parentScrollView.requestDisallowInterceptTouchEvent(!flag);//这里的parentScrollView就是listview外面的那个scrollview
    }

    public ScrollView getParentScrollView() {
        return parentScrollView;
    }

    public void setParentScrollView(ScrollView parentScrollView) {
        this.parentScrollView = parentScrollView;
    }

<<<<<<< HEAD
=======
    private class MyHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case SCROLL_WHAT:
                    scrollOnce();
                    sendScrollMessage(interval);
                default:
                    break;
            }
        }
    }

>>>>>>> 2540931ec03580503cb88e4fe7ef18497de3b69c
    /**
     * scroll only once
     */
    public void scrollOnce() {
        int currentItem = getCurrentItem();
        int nextItem = ++currentItem;
        setCurrentItem(nextItem, true);
    }

    public void startAutoScroll() {
        isAutoScroll = true;
        sendScrollMessage(interval);
    }

    private void sendScrollMessage(long delayTimeInMills) {
        /** remove messages before, keeps one message is running at most **/
        handler.removeMessages(SCROLL_WHAT);
        handler.sendEmptyMessageDelayed(SCROLL_WHAT, delayTimeInMills);
    }
<<<<<<< HEAD

    private class MyHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case SCROLL_WHAT:
                    scrollOnce();
                    sendScrollMessage(interval);
                default:
                    break;
            }
        }
    }
=======
>>>>>>> 2540931ec03580503cb88e4fe7ef18497de3b69c
}
