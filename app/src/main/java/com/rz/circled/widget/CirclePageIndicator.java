package com.rz.circled.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.rz.circled.R;

/**
 * Created by rzw2 on 2017/1/9.
 */

public class CirclePageIndicator extends LinearLayout implements ViewPager.OnPageChangeListener {

    private int page;

    public CirclePageIndicator(Context context) {
        super(context);
    }

    public CirclePageIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CirclePageIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setViewPager(ViewPager pager) {
        pager.setOnPageChangeListener(this);
        addIndicator(pager.getAdapter().getCount());
    }

    private void addIndicator(int count) {
        for (int i = 0; i < count; i++) {
            ImageView img = new ImageView(getContext());
            LayoutParams params = new LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params.leftMargin = getContext().getResources().getDimensionPixelOffset(R.dimen.px15);
            params.rightMargin = getContext().getResources().getDimensionPixelOffset(R.dimen.px15);
            img.setImageResource(R.drawable.selector_share_indicator);
            addView(img, params);
        }

        if (count > 0) {
            getChildAt(page).setSelected(true);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        getChildAt(page).setSelected(false);
        this.page = position;
        getChildAt(page).setSelected(true);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public int getCurrentPage() {
        return page;
    }
}
