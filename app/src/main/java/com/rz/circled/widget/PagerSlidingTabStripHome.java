/*
 * Copyright (C) 2013 Andreas Stuetz <andreas.stuetz@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rz.circled.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rz.circled.R;
import com.rz.common.utils.DensityUtils;

import java.util.Locale;


public class PagerSlidingTabStripHome extends HorizontalScrollView {

    private boolean mIsMain;

    public interface IconTabProvider {
        public int getPageIconResId(int position);
    }

    // @formatter:off
    private static final int[] ATTRS = new int[]{
            android.R.attr.textSize,
            android.R.attr.textColor
    };
    // @formatter:on

    private LinearLayout.LayoutParams defaultTabLayoutParams;
    private LinearLayout.LayoutParams expandedTabLayoutParams;
    private LinearLayout.LayoutParams customTabLayoutParams;

    private final PageListener pageListener = new PageListener();
    public OnPageChangeListener delegatePageListener;

    private LinearLayout tabsContainer;
    private ViewPager pager;

    private int tabCount;

    private int currentPosition = 0;
    private float currentPositionOffset = 0f;

    private Paint rectPaint;
    private Paint dividerPaint;
    private Paint fillPaint;

    private int indicatorColor = 0xFF666666;
    //    private int underlineColor = 0x1A000000;
    private int underlineColor = 0x1A000000;
    private int dividerColor = 0x1A000000;

    private boolean shouldExpand = false;
    private boolean textAllCaps = true;

    private int scrollOffset = 52;
    private int indicatorHeight = 8;
    private int underlineHeight = 2;
    private int dividerPadding = 12;
    private int tabPadding = 0;
    private int dividerWidth = 2;

    private int tabTextSize = 12;
    private int tabTextColor = 0xFF666666;
    private Typeface tabTypeface = null;
    private int tabTypefaceStyle = Typeface.NORMAL;

    private int lastScrollX = 0;

    //    private int tabBackgroundResId = R.drawable.background_tab;
    private int tabBackgroundResId = 0;

    private Locale locale;

    private int tempPosition;

    private boolean lineFitFont = false;
    public int sanJiaoType = 0; //1向上 2，向下

    private float[] textWidths;

    private Bitmap selectedBitmap;

    public PagerSlidingTabStripHome(Context context) {
        this(context, null);
    }

    public PagerSlidingTabStripHome(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressWarnings("ResourceType")
    public PagerSlidingTabStripHome(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setFillViewport(true);
        setWillNotDraw(false);

        tabsContainer = new LinearLayout(context);
        tabsContainer.setOrientation(LinearLayout.HORIZONTAL);
        tabsContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(tabsContainer);

        DisplayMetrics dm = getResources().getDisplayMetrics();

        scrollOffset = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, scrollOffset, dm);
        indicatorHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, indicatorHeight, dm);
        underlineHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, underlineHeight, dm);
        dividerPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dividerPadding, dm);
        tabPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, tabPadding, dm);
        dividerWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dividerWidth, dm);
        tabTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, tabTextSize, dm);

        // get system attrs (android:textSize and android:textColor)

        TypedArray a = context.obtainStyledAttributes(attrs, ATTRS);

        tabTextSize = a.getDimensionPixelSize(0, tabTextSize);
//
        tabTextColor = a.getColor(1, tabTextColor);
//
        a.recycle();

        // get custom attrs

        a = context.obtainStyledAttributes(attrs, R.styleable.PagerSlidingTabStrip);

        indicatorColor = a.getColor(R.styleable.PagerSlidingTabStrip_pstsIndicatorColor, indicatorColor);
        underlineColor = a.getColor(R.styleable.PagerSlidingTabStrip_pstsUnderlineColor, underlineColor);
        dividerColor = a.getColor(R.styleable.PagerSlidingTabStrip_pstsDividerColor, dividerColor);
        indicatorHeight = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsIndicatorHeight, indicatorHeight);
        underlineHeight = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsUnderlineHeight, underlineHeight);
        dividerPadding = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsDividerPadding, dividerPadding);
        tabPadding = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsTabPaddingLeftRight, tabPadding);
        tabBackgroundResId = a.getResourceId(R.styleable.PagerSlidingTabStrip_pstsTabBackground, tabBackgroundResId);
        shouldExpand = a.getBoolean(R.styleable.PagerSlidingTabStrip_pstsShouldExpand, shouldExpand);
        scrollOffset = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsScrollOffset, scrollOffset);
        textAllCaps = a.getBoolean(R.styleable.PagerSlidingTabStrip_pstsTextAllCaps, textAllCaps);

        a.recycle();

        rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Style.FILL);
        rectPaint.setColor(getResources().getColor(R.color.color_main_new));

        dividerPaint = new Paint();
        dividerPaint.setAntiAlias(true);
        dividerPaint.setStrokeWidth(getResources().getDimension(R.dimen.px2));

        fillPaint = new Paint();
        fillPaint.setStyle(Style.FILL_AND_STROKE);
        fillPaint.setAntiAlias(true);
        fillPaint.setAlpha(1);
        fillPaint.setColor(getResources().getColor(R.color.font_color_blue));
        fillPaint.setStrokeWidth(getResources().getDimension(R.dimen.px2));

        defaultTabLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        expandedTabLayoutParams = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f);

        if (locale == null) {
            locale = getResources().getConfiguration().locale;
        }

        selectedBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.bg_pic_tab_selected);
    }

    /**
     * 生成bitmap
     *
     * @param origin
     * @param width  指定bitmap正方形大小
     * @return
     */
    private Bitmap getBitmap(@DrawableRes int origin, int width, float ratio) {
        if (origin == -1) {
            return null;
        }
//        Log.d("bitmap", "width " + width);
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        BitmapFactory.decodeResource(getResources(), origin, options);
//        options.inJustDecodeBounds = false;
        Bitmap src = BitmapFactory.decodeResource(getResources(), origin);
//        return Bitmap.createScaledBitmap(src, width, (int) (width / ratio), false);
        return src;
    }

    public boolean isLineFitFont() {
        return lineFitFont;
    }

    public void setLineFitFont(boolean lineFitFont) {
        this.lineFitFont = lineFitFont;
    }


    public void changedTextColor(int position) {
        TextView temp = null;
        for (int i = 0; i < tabsContainer.getChildCount(); i++) {
            temp = (TextView) tabsContainer.getChildAt(i);
            if (mIsMain) {
//                temp.setTextSize(DensityUtils.px2sp(getContext(),40));
            }
            if (i == position) {
//                temp.setTypeface(tabTypeface, Typeface.BOLD);
//                temp.setTypeface(tabTypeface, Typeface.NORMAL);
                temp.setTextColor(getResources().getColor(R.color.font_color_blue));
//                rectPaint.setColor(getResources().getColor(R.color.color_main_new));
            } else {
//                temp.setTextColor(getResources().getColor(R.color.color_666666));
//                temp.setTypeface(tabTypeface, Typeface.NORMAL);
                temp.setTextColor(getResources().getColor(R.color.tab_strip_uncheck));
//                rectPaint.setColor(getResources().getColor(R.color.color_666666));
            }
        }
    }

    public void setViewPager(ViewPager pager) {
        this.pager = pager;

        if (pager.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }

        pager.setOnPageChangeListener(pageListener);
        notifyDataSetChanged();
    }

    public void setOnPageChangeListener(OnPageChangeListener listener) {
        this.delegatePageListener = listener;
    }

    public void notifyDataSetChanged() {

        tabsContainer.removeAllViews();

        tabCount = pager.getAdapter().getCount();

        textWidths = new float[tabCount];

        for (int i = 0; i < tabCount; i++) {

            if (pager.getAdapter() instanceof IconTabProvider) {
                addIconTab(i, ((IconTabProvider) pager.getAdapter()).getPageIconResId(i));
            } else {
                addTextTab(i, pager.getAdapter().getPageTitle(i).toString());
            }

        }

        updateTabStyles();

        getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @SuppressLint("NewApi")
            @Override
            public void onGlobalLayout() {

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }

                currentPosition = pager.getCurrentItem();
                scrollToChild(currentPosition, 0);
            }
        });

        changedTextColor(tempPosition);

    }

    private void addTextTab(final int position, String title) {

        TextView tab = new TextView(getContext());
        tab.setText(title);
        tab.setGravity(Gravity.CENTER);
        tab.setSingleLine();
        TextPaint tp = tab.getPaint();
        tp.setFakeBoldText(true);
        tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_46));

        addTab(position, tab);
    }

    private void addIconTab(final int position, int resId) {

        ImageButton tab = new ImageButton(getContext());
        tab.setImageResource(resId);

        addTab(position, tab);

    }

    private void addTab(final int position, View tab) {
        tab.setFocusable(true);
        tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(position);
            }
        });

        tab.setPadding(tabPadding, 0, tabPadding, 0);
        if (customTabLayoutParams == null) {
            tabsContainer.addView(tab, position, shouldExpand ? expandedTabLayoutParams : defaultTabLayoutParams);
        } else {
            tabsContainer.addView(tab, position, customTabLayoutParams);
        }
//		tabsContainer.addView(tab, position, shouldExpand ? expandedTabLayoutParams : defaultTabLayoutParams);
    }


    public void isMain(boolean isMain) {
        this.mIsMain = isMain;
    }

    private void updateTabStyles() {

        for (int i = 0; i < tabCount; i++) {

            View v = tabsContainer.getChildAt(i);

            v.setBackgroundResource(tabBackgroundResId);

            if (v instanceof TextView) {

                TextView tab = (TextView) v;
                if (mIsMain) {
//                    tab.setTextSize(DensityUtils.px2sp(getContext(),40));
                }
                tab.setTypeface(tabTypeface, tabTypefaceStyle);
                tab.setTextColor(tabTextColor);

                if (lineFitFont) {
                    textWidths[i] = tab.getPaint().measureText(tab.getText().toString());
                    Log.d("test", "test textWidth " + textWidths[i]);
                }
                // setAllCaps() is only available from API 14, so the upper case is made manually if we are on a
                // pre-ICS-build
//                if (textAllCaps) {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
//                        tab.setAllCaps(true);
//                    } else {
//                        tab.setText(tab.getText().toString().toUpperCase(locale));
//                    }
//                }

                if (tempPosition == i) {
                    tab.setTextColor(getResources().getColor(R.color.color_main_new));
                }
            }
        }

    }

    private void scrollToChild(int position, int offset) {

        if (tabCount == 0) {
            return;
        }

        int newScrollX = tabsContainer.getChildAt(position).getLeft() + offset;

        if (position > 0 || offset > 0) {
            newScrollX -= scrollOffset;
        }

        if (newScrollX != lastScrollX) {
            lastScrollX = newScrollX;
            scrollTo(newScrollX, 0);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (isInEditMode() || tabCount == 0) {
            return;
        }

        final int height = getHeight();

        // draw underline
        underlineColor = getResources().getColor(R.color.color_aaa);
        rectPaint.setColor(underlineColor);
        canvas.drawRect(getResources().getDimension(R.dimen.app_base_padding), height - underlineHeight, tabsContainer.getWidth() - getResources().getDimension(R.dimen.app_base_padding), height, rectPaint);

        // draw indicator line
        rectPaint.setColor(indicatorColor);


        // default: line below current tab
        View currentTab = tabsContainer.getChildAt(currentPosition);
        float lineLeft = currentTab.getLeft();
        float lineRight = currentTab.getRight();

        if (lineFitFont) {
            float offest = (lineRight - lineLeft - textWidths[currentPosition]) / 2;
            lineLeft = lineLeft + offest;
            lineRight = lineRight - offest;
        }

        // if there is an offset, start interpolating left and right coordinates between current and next tab
        if (currentPositionOffset > 0f && currentPosition < tabCount - 1) {

            View nextTab = tabsContainer.getChildAt(currentPosition + 1);
            float nextTabLeft = nextTab.getLeft();
            float nextTabRight = nextTab.getRight();

            if (lineFitFont) {
                float offest = (nextTabRight - nextTabLeft - textWidths[currentPosition + 1]) / 2;
                nextTabLeft = nextTabLeft + offest;
                nextTabRight = nextTabRight - offest;
                lineLeft = (nextTabLeft - lineLeft) * currentPositionOffset + lineLeft;
                lineRight = (nextTabRight - lineRight) * currentPositionOffset + lineRight;
            } else {
                lineLeft = (currentPositionOffset * nextTabLeft + (1f - currentPositionOffset) * lineLeft);
                lineRight = (currentPositionOffset * nextTabRight + (1f - currentPositionOffset) * lineRight);
            }
        }

        //画指示器
//        indicatorColor = getResources().getColor(R.color.underline_tab);//xiesixiesixiesixiesi!!
        indicatorColor = getResources().getColor(R.color.font_color_blue);//xiesixiesixiesixiesi!!
        rectPaint.setColor(indicatorColor);
//        canvas.drawRect(lineLeft + getResources().getDimension(R.dimen.px60),
//                height - indicatorHeight - getResources().getDimension(R.dimen.px20),
//                lineRight - getResources().getDimension(R.dimen.px60),
//                height - getResources().getDimension(R.dimen.px20), rectPaint);
        canvas.drawRect(lineLeft, height - indicatorHeight - getResources().getDimension(R.dimen.px20), lineRight, height - getResources().getDimension(R.dimen.px20), rectPaint);


//        if (sanJiaoType == 1) {
////            Rect rect = new Rect();
////            rect.set((int) lineLeft, height - 20, (int) lineRight, height);
////            canvas.drawBitmap(selectedBitmap, null, rect, null);
//            canvas.drawRect(lineLeft, height - 10, lineRight, height, fillPaint);
//            Path path = new Path();
//            path.moveTo((lineRight - lineLeft) / 2 - 15 + lineLeft, height - 10);
//            path.lineTo((lineRight - lineLeft) / 2 + 15 + lineLeft, height - 10);
//            path.lineTo((lineRight - lineLeft) / 2 + lineLeft, height - 20);
//            path.lineTo((lineRight - lineLeft) / 2 - 15 + lineLeft, height - 10);
//            path.close();
//            canvas.drawPath(path, fillPaint);
//        } else if (sanJiaoType == 2) {
//            canvas.drawRect(lineLeft, height - 20, lineRight, height - 10, fillPaint);
//            Path path = new Path();
//            path.moveTo((lineRight - lineLeft) / 2 - 15 + lineLeft, height - 10);
//            path.lineTo((lineRight - lineLeft) / 2 + 15 + lineLeft, height - 10);
//            path.lineTo((lineRight - lineLeft) / 2 + lineLeft, height);
//            path.lineTo((lineRight - lineLeft) / 2 - 15 + lineLeft, height - 10);
//            path.close();
//            canvas.drawPath(path, fillPaint);
//        } else {
//            canvas.drawRect(lineLeft + getResources().getDimension(R.dimen.px40),
//                    height - indicatorHeight - getResources().getDimension(R.dimen.px20),
//                    lineRight - getResources().getDimension(R.dimen.px40),
//                    height - getResources().getDimension(R.dimen.px20), rectPaint);
//        }


        // draw underline
//        rectPaint.setColor(underlineColor);
//        canvas.drawRect(getResources().getDimension(R.dimen.px20), height - underlineHeight, tabsContainer.getWidth() - getResources().getDimension(R.dimen.px20), height, rectPaint);


        // draw divider
//        if (!mIsMain) {
//            dividerColor = getResources().getColor(R.color.color_line);
//            dividerPaint.setColor(dividerColor);
//            for (int i = 0; i < tabCount - 1; i++) {
//                View tab = tabsContainer.getChildAt(i);
////            canvas.drawRect(tab.getRight() - 2, dividerPadding, tab.getRight() + 2, height - dividerPadding, dividerPaint);
//                canvas.drawLine(tab.getRight(), dividerPadding, tab.getRight(), height - dividerPadding, dividerPaint);
//            }
//        }
    }

    private class PageListener implements OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            Log.d("test", "onPageScrolled " + "position" + position + " positionOffset" + positionOffset + " positionOffsetPixels" + positionOffsetPixels);

            currentPosition = position;
            currentPositionOffset = positionOffset;

            scrollToChild(position, (int) (positionOffset * tabsContainer.getChildAt(position).getWidth()));

            invalidate();

            if (delegatePageListener != null) {
                delegatePageListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                scrollToChild(pager.getCurrentItem(), 0);
            }

            if (delegatePageListener != null) {
                delegatePageListener.onPageScrollStateChanged(state);
            }
        }

        @Override
        public void onPageSelected(int position) {
            tempPosition = position;
            changedTextColor(position);
            if (delegatePageListener != null) {
                delegatePageListener.onPageSelected(position);
            }
        }

    }

    public void setIndicatorColor(int indicatorColor) {
        this.indicatorColor = indicatorColor;
        invalidate();
    }

    public void setIndicatorColorResource(int resId) {
        this.indicatorColor = getResources().getColor(resId);
        invalidate();
    }

    public int getIndicatorColor() {
        return this.indicatorColor;
    }

    public void setIndicatorHeight(int indicatorLineHeightPx) {
        this.indicatorHeight = indicatorLineHeightPx;
        invalidate();
    }

    public int getIndicatorHeight() {
        return indicatorHeight;
    }

    public void setUnderlineColor(int underlineColor) {
        this.underlineColor = underlineColor;
        invalidate();
    }

    public void setUnderlineColorResource(int resId) {
        this.underlineColor = getResources().getColor(resId);
        invalidate();
    }

    public int getUnderlineColor() {
        return underlineColor;
    }

    public void setDividerColor(int dividerColor) {
        this.dividerColor = dividerColor;
        invalidate();
    }

    public void setDividerColorResource(int resId) {
        this.dividerColor = getResources().getColor(resId);
        invalidate();
    }

    public int getDividerColor() {
        return dividerColor;
    }

    public void setUnderlineHeight(int underlineHeightPx) {
        this.underlineHeight = underlineHeightPx;
        invalidate();
    }

    public int getUnderlineHeight() {
        return underlineHeight;
    }

    public void setDividerPadding(int dividerPaddingPx) {
        this.dividerPadding = dividerPaddingPx;
        invalidate();
    }

    public int getDividerPadding() {
        return dividerPadding;
    }

    public void setScrollOffset(int scrollOffsetPx) {
        this.scrollOffset = scrollOffsetPx;
        invalidate();
    }

    public int getScrollOffset() {
        return scrollOffset;
    }

    public void setShouldExpand(boolean shouldExpand) {
        this.shouldExpand = shouldExpand;
        requestLayout();
    }

    public boolean getShouldExpand() {
        return shouldExpand;
    }

    public boolean isTextAllCaps() {
        return textAllCaps;
    }

    public void setAllCaps(boolean textAllCaps) {
        this.textAllCaps = textAllCaps;
    }

    public void setTextSize(int textSizePx) {
        this.tabTextSize = textSizePx;
        updateTabStyles();
    }

    public int getTextSize() {
        return tabTextSize;
    }

    public void setTextColor(int textColor) {
        this.tabTextColor = textColor;
        updateTabStyles();
    }

    public void setTextColorResource(int resId) {
        this.tabTextColor = getResources().getColor(resId);
        updateTabStyles();
    }

    public int getTextColor() {
        return tabTextColor;
    }

    public void setTypeface(Typeface typeface, int style) {
        this.tabTypeface = typeface;
        this.tabTypefaceStyle = style;
        updateTabStyles();
    }

    public void setTabBackground(int resId) {
        this.tabBackgroundResId = resId;
    }

    public int getTabBackground() {
        return tabBackgroundResId;
    }

    public void setTabPaddingLeftRight(int paddingPx) {
        this.tabPadding = paddingPx;
        updateTabStyles();
    }

    public int getTabPaddingLeftRight() {
        return tabPadding;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        currentPosition = savedState.currentPosition;
        requestLayout();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.currentPosition = currentPosition;
        return savedState;
    }

    static class SavedState extends BaseSavedState {
        int currentPosition;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            currentPosition = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(currentPosition);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    public void setCustomLayoutParams(float halve) {
        customTabLayoutParams = new LinearLayout.LayoutParams((int) (DensityUtils.getScreenW(getContext()) / halve), LayoutParams.MATCH_PARENT);
    }

    public void setCustomLayoutParams(float halve, float offest) {
        customTabLayoutParams = new LinearLayout.LayoutParams((int) ((DensityUtils.getScreenW(getContext()) - offest) / halve), LayoutParams.MATCH_PARENT);
    }


    public void setCustomLayoutWealthParams(float halve) {
        customTabLayoutParams = new LinearLayout.LayoutParams((int) ((DensityUtils.getScreenW(getContext()) - 50) / halve), LayoutParams.MATCH_PARENT);
    }

    public void setCustomLayoutParams(LinearLayout.LayoutParams customLayoutParams) {
        this.customTabLayoutParams = customLayoutParams;
    }

    public void reset() {
        customTabLayoutParams = null;
        setLineFitFont(false);
    }

    public void setTempPosition(int tempPosition) {
        this.tempPosition = tempPosition;
    }
}
