//package com.rz.circled.widget;
//
//import android.content.Context;
//import android.util.AttributeSet;
//import android.view.View;
//import android.widget.ScrollView;
//
///**
// * Created by Administrator on 2017/9/14 0014.
// */
//
//public class ScrollBottomView extends ScrollView {
//
//    private int downX;
//    private int downY;
//    private int mTouchSlop;
//    private int type;
//
//    public int getType() {
//        return type;
//    }
//
//    public void setType(int type) {
//        this.type = type;
//    }
//
//    private ScrollViewToBottomListener scrollViewListener = null;
//    private OnScrollViewToBottomLiatener onScrollViewToBottomLiatener = null;
//
//    public ScrollBottomView(Context context) {
//        super(context);
//    }
//
//    public ScrollBottomView(Context context, AttributeSet attrs, int defStyle) {
//        super(context, attrs, defStyle);
//    }
//
//    public ScrollBottomView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//    public void setScrollViewListener(ScrollViewToBottomListener scrollViewListener) {
//        this.scrollViewListener = scrollViewListener;
//    }
//
//    public void setOnScrollViewToBottomLiatener(OnScrollViewToBottomLiatener onScrollViewToBottomLiatener){
//        this.onScrollViewToBottomLiatener = onScrollViewToBottomLiatener;
//    }
//
//    @Override
//    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
//        super.onScrollChanged(l, t, oldl, oldt);
//
//        View view = (View) getChildAt(getChildCount() - 1);
//
//        int d = view.getBottom();
//
//        d -= (getHeight() + getScrollY());
//
////        Log.e("---------->","d"+d);
//        if (d == 0) {
//
//            if (onScrollViewToBottomLiatener != null) {
//                onScrollViewToBottomLiatener.onScrollViewToBottomListener(type);
//
//            }
//
//        } else {
//
//            if (scrollViewListener != null) {
//                scrollViewListener.onScrollChanged(this, l, t, oldl, oldt);
//            }
//        }
//    }
//
//    interface OnScrollViewToBottomLiatener {
//        void onScrollViewToBottomListener();
//    }
//
//    interface ScrollViewToBottomListener {
//        void onScrollChanged(ScrollBottomView scrollView,
//        int x, int y, int oldx, int oldy);
//    }
//}