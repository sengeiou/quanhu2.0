package com.rz.circled.widget;

import android.content.Context;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ProgressBar;

/**
 * Created by Administrator on 2016/8/16 0016.
 */
public class CustomProgressbar extends ProgressBar {

    public CustomProgressbar(Context context) {
        super(context);
    }

    public CustomProgressbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomProgressbar(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs, defStyle, 0);

    }

    public CustomProgressbar(Context context, AttributeSet attrs, int defStyle, int styleRes) {

        super(context, attrs, defStyle);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public final int roundCorners = 15;//就是改变这个值，就可以改变自定义progressbar左右两端的圆角大小了，使用于自定义图片的情况，

    Shape getDrawableShape() {
        final float[] roundedCorners = new float[]{0, 0, 0, 0, 0, 0, 0, 0};
        for (int i = 0; i < roundedCorners.length; i++) {
//            roundedCorners[i] = DensityUtils.dip2px(getContext(), roundCorners);
        }
        Log.d("progress", "getDrawableShape");
        return new RoundRectShape(roundedCorners, null, null);
    }
}