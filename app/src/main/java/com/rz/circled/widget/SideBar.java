package com.rz.circled.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.rz.circled.R;
import com.rz.common.utils.DensityUtils;

public class SideBar extends View {
    // 触摸事件
    private OnTouchingLetterChangedListener onTouchingLetterChangedListener;

    // 26个字母"↑","#",去除不可能存在的首字母
    public static String[] b = {"↑", "A", "B", "C", "D", "E", "F", "G", "H",
            "J", "K", "L", "M", "N", "P", "Q", "R", "S", "T", "W", "X", "Y", "Z", "#"};

//    public static String[] b = {"↑","#"};

    /**
     * 记录选中下标
     */
    private int choose = -1;

    private int count = 0;

    /**
     * 每个字母的画笔
     */
    private Paint paint = new Paint();

    /**
     * 画圆
     */
    private Paint p_c = new Paint();

    private TextView mTextDialog;

    public void setTextView(TextView mTextDialog) {
        this.mTextDialog = mTextDialog;
    }

    public SideBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SideBar(Context context) {
        super(context);
    }

    /**
     * 重写这个方法
     */
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 获取焦点改变背景颜色.
        int height = getHeight();// 获取对应高度
        int width = getWidth(); // 获取对应宽度
        int singleHeight = height / b.length;// 获取每一个字母的高度
        for (int i = 0; i < b.length; i++) {
            paint.setColor(getResources().getColor(R.color.color_999999));
            paint.setAntiAlias(true);
            paint.setFakeBoldText(false);
            paint.setTextSize(DensityUtils.dip2px(getContext(), 12));
            p_c.setColor(getResources().getColor(R.color.transparent));
            p_c.setTypeface(Typeface.DEFAULT_BOLD);
            p_c.setAntiAlias(true);
            paint.setAntiAlias(true);
            // 选中的状态
            if (choose == -1) {
                if (i == 0) {
                    paint.setColor(getResources().getColor(R.color.color_29D1E2));
//                    paint.setFakeBoldText(true);
                    p_c.setColor(getResources().getColor(R.color.transparent));
                }
            } else {
                if (i == 0) {
                    paint.setColor(getResources()
                            .getColor(R.color.color_999999));
//                    paint.setFakeBoldText(true);
                    p_c.setColor(getResources().getColor(R.color.transparent));
                }
            }
            if (i == choose) {
                paint.setColor(getResources().getColor(R.color.color_29D1E2));
//                paint.setFakeBoldText(true);
                p_c.setColor(getResources().getColor(R.color.transparent));
            }
            // x坐标等于中间-字符串宽度的一半.
            float xPos = width / 2 - paint.measureText(b[i]) / 2;
            float yPos = singleHeight * i + singleHeight;
            float x = width;
            float y = 4;
            canvas.drawCircle(x / 2, yPos - y / 2, 15, p_c);
            canvas.drawText(b[i], xPos, yPos - y / 2, paint);
            paint.reset();// 重置画笔
        }
    }

    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();// 点击y坐标
        final int oldChoose = choose;
        final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
        final int c = (int) (y / getHeight() * b.length);// 点击y坐标所占总高度的比例*b数组的长度就等于点击b中的个数.

        switch (action) {
            case MotionEvent.ACTION_UP:
                setBackgroundDrawable(new ColorDrawable(0x00000000));
                invalidate();
                if (mTextDialog != null) {
                    mTextDialog.setVisibility(View.INVISIBLE);
                }
                break;

            default:
                setBackgroundResource(R.drawable.sidebar_background);
                setAlpha((float) 0.7);
                if (oldChoose != c) {
                    if (c >= 0 && c < b.length) {
                        if (listener != null) {
                            listener.onTouchingLetterChanged(b[c]);
                        }
                        if (mTextDialog != null) {
                            mTextDialog.setText(b[c]);
                            mTextDialog.setVisibility(View.VISIBLE);
                        }

                        choose = c;
                        invalidate();
                    }
                }

                break;
        }
        return true;
    }

    /**
     * 获取左侧传过来的参数，字母栏作出相应的改变
     *
     * @param s
     */
    public void LetterChanged(String s) {
        for (int i = 0; i < b.length; i++) {
            if (s.equals(b[i])) {
                choose = i;
                invalidate();
            }
        }
    }


    public void setLetterBar(String[] letters) {
        if (letters.length > 0) {
            b = letters;
        }
        invalidate();

    }

    /**
     * 向外公开的方法
     *
     * @param onTouchingLetterChangedListener
     */
    public void setOnTouchingLetterChangedListener(
            OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
    }

    /**
     * 接口
     *
     * @author coder
     */
    public interface OnTouchingLetterChangedListener {
        public void onTouchingLetterChanged(String s);
    }

}