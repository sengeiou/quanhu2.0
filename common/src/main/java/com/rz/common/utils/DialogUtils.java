package com.rz.common.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.rz.common.R;


/**
 * Created by Administrator on 2016/8/12 0012.
 */
public class DialogUtils {

    public static Dialog selfDialog(Context context, View view, boolean canceledOnTouch) {
        if (view == null) {
            return null;
        }
        Dialog mDialog = null;
        mDialog = new Dialog(context, R.style.AppTheme_DialogStyle);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        params.width = (int) (DensityUtils.getScreenW(context) * 0.8);
        mDialog.setContentView(view, params);
        mDialog.setCanceledOnTouchOutside(canceledOnTouch);
        return mDialog;
    }

    /**
     * 根据指定位置弹出popupwindow
     *
     * @param v
     * @param resource
     * @return
     */
    public static PopupWindow showPopByLocation(final View v, View resource) {
        // 创建PopupWindow对象
        final PopupWindow pop = new PopupWindow(resource, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, false);
        resource.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popupWidth = resource.getMeasuredWidth();
        int popupHeight = resource.getMeasuredHeight();
        int[] location = new int[2];
        // 需要设置一下此参数，点击外边可消失
        pop.setBackgroundDrawable(new BitmapDrawable());
        //设置点击窗口外边窗口消失
        pop.setOutsideTouchable(true);
        // 设置此参数获得焦点，否则无法点击
        pop.setFocusable(true);
        //防止虚拟软键盘被弹出菜单遮住
        pop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        // 获得位置 这里的v是目标控件，就是你要放在这个v的上面还是下面
        v.getLocationOnScreen(location);
        //这里就可自定义在上方和下方了 ，这种方式是为了确定在某个位置，某个控件的左边，右边，上边，下边都可以
        pop.showAtLocation(v, Gravity.NO_GRAVITY, (location[0] + v.getWidth() / 2) - popupWidth / 2, location[1] - popupHeight);
        return pop;
    }


}
