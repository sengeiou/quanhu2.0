package com.rz.circled.ui.view.circle_favite;


import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.rz.circled.R;
import com.rz.circled.application.QHApplication;



/**
 * @author wsf
 * @Description:
 * @date 16/3/10 16:54
 */
public class NameClickable extends ClickableSpan implements View.OnClickListener {
    private final ISpanClick mListener;
    private int mPosition;

    public NameClickable(ISpanClick l, int position) {
        mListener = l;
        mPosition = position;
    }

    @Override
    public void onClick(View widget) {
    	if(mListener!=null)
        mListener.onClick(mPosition);
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);

        int colorValue = QHApplication.getContext().getResources().getColor(
                R.color.color_29D1E2);
        ds.setColor(colorValue);
        ds.setUnderlineText(false);
        ds.clearShadowLayer();
    }
}
