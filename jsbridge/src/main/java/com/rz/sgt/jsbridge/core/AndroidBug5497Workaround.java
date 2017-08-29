package com.rz.sgt.jsbridge.core;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

public class AndroidBug5497Workaround {

    // For more information, see https://code.google.com/p/android/issues/detail?id=5497
    // To use this class, simply invoke assistActivity() on an Activity that already has its content view set.

    public static AndroidBug5497Workaround assistActivity(Activity activity) {
        return new AndroidBug5497Workaround(activity);
    }

    private View mChildOfContent;
    private int usableHeightPrevious;
    private FrameLayout.LayoutParams frameLayoutParams;
    private OnUsableHeightChangeListen heightChangeListen;

    private AndroidBug5497Workaround(Activity activity) {
        FrameLayout content = (FrameLayout) activity.findViewById(android.R.id.content);
        mChildOfContent = content.getChildAt(0);
        mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                possiblyResizeChildOfContent();
            }
        });
        frameLayoutParams = (FrameLayout.LayoutParams) mChildOfContent.getLayoutParams();
    }

    private void possiblyResizeChildOfContent() {
        int usableHeightNow = computeUsableHeight();
        Log.d("yeying", "possiblyResizeChildOfContent usableHeightNow " + usableHeightNow);
        if (usableHeightNow != usableHeightPrevious) {
            int usableHeightSansKeyboard = mChildOfContent.getRootView().getHeight();
            int heightDifference = usableHeightSansKeyboard - usableHeightNow;
            if (heightDifference > (usableHeightSansKeyboard / 4)) {
                // keyboard probably just became visible
                frameLayoutParams.height = usableHeightSansKeyboard - heightDifference;
            } else {
                // keyboard probably just became hidden
                frameLayoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            }
            mChildOfContent.requestLayout();
            usableHeightPrevious = usableHeightNow;
            if (heightChangeListen != null)
                heightChangeListen.onUsableHeightChangeListen(usableHeightNow);
        }
    }

    private int computeUsableHeight() {
        Rect r = new Rect();
        mChildOfContent.getWindowVisibleDisplayFrame(r);
//        return (r.bottom - r.top);// 全屏模式下： return r.bottom
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return r.bottom;
        } else {
            return (r.bottom - r.top);
        }
    }

    public void setUsableHeightChangeListen(OnUsableHeightChangeListen heightChangeListen) {
        this.heightChangeListen = heightChangeListen;
    }

    public interface OnUsableHeightChangeListen {
        void onUsableHeightChangeListen(int usableHeightNow);
    }

}