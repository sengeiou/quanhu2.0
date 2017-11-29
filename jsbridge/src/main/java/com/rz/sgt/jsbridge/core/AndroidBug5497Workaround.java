package com.rz.sgt.jsbridge.core;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

public class AndroidBug5497Workaround {

    // For more information, see https://code.google.com/p/android/issues/detail?id=5497
    // To use this class, simply invoke assistActivity() on an Activity that already has its content view set.

    public static AndroidBug5497Workaround assistActivity(Activity activity, ViewGroup view) {
        return new AndroidBug5497Workaround(activity, view);
    }

    private View mChildOfContent;
    private int usableHeightPrevious;
    private ViewGroup.LayoutParams frameLayoutParams;
    private OnUsableHeightChangeListen heightChangeListen;

    private AndroidBug5497Workaround(Activity activity, ViewGroup view) {
        mChildOfContent = view;
        mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                possiblyResizeChildOfContent();
            }
        });
        frameLayoutParams = mChildOfContent.getLayoutParams();
    }

    private void possiblyResizeChildOfContent() {
        int usableHeightNow = computeUsableHeight();
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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