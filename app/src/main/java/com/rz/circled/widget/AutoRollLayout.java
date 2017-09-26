package com.rz.circled.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.rz.circled.R;
import com.rz.circled.application.QHApplication;
import com.rz.circled.ui.activity.LoginActivity;
import com.rz.circled.ui.activity.VideoH5Aty;
import com.rz.circled.ui.activity.WebContainerActivity;
import com.rz.common.cache.preference.Session;
import com.rz.httpapi.bean.BannerAddSubjectModel;
import com.zhuge.analysis.stat.ZhugeSDK;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 轮播图的目的是展示一些图片和文字
 * <p/>
 * 方法设计
 * 1 传入数据
 * // public void setItems(String[] picUrls ,String[] titles)
 * public class RollItem{
 * String picUrl;
 * String title;
 * }
 * public void setItems(List<RollItem> items);
 * <p/>
 * 2 开始或停止轮播
 * public void setAutoRoll(boolean allow)
 */

public class AutoRollLayout extends FrameLayout implements View.OnClickListener {

    private ViewPager mViewPager;
    private LinearLayout mDotContainer;
    private List<BannerAddSubjectModel> mItems;
    private Context context;

    public AutoRollLayout(Context context) {
        this(context, null);
    }

    public AutoRollLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        View.inflate(getContext(), R.layout.arl_arl_layout, this);

        mViewPager = (ViewPager) findViewById(R.id.arl_arl_vp);
        mDotContainer = (LinearLayout) findViewById(R.id.arl_dot_container);

    }


    public void notifyData() {
        mPagerAdapter.notifyDataSetChanged();
    }

    public void setItems(List<BannerAddSubjectModel> items) {
        mItems = items;
        // ViewPager

        // dots , 添加点  点变色  点被点击
        mDotContainer.removeAllViews();
        if (mItems == null || mItems.isEmpty()) {
            return;
        }
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(scrollble ? 100 : 0);
        // TextView
        mViewPager.setOnPageChangeListener(mOnPageChangeListener);
        addDots();
        // 默认状态的问题
        mOnPageChangeListener.onPageSelected(0);
    }

    private void addDots() {

        int size = mItems.size();
        if (size == 1) {
            return;
        }
        int pxFor10dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
        for (int i = 0; i < size; i++) {
            View dot = new View(getContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(pxFor10dp, pxFor10dp);
            lp.setMargins(0, 0, pxFor10dp, 0);
            dot.setLayoutParams(lp);
            dot.setBackgroundResource(R.drawable.arl_white_dot);
            dot.setOnClickListener(this);
//            <View
//            android:layout_marginRight="10dp"
//            android:background="@drawable/arl_red_dot"
//            android:layout_width="10dp"
//            android:layout_height="10dp" />
            mDotContainer.addView(dot);
        }
    }

    ;
    static Handler sHandler = new Handler();
    boolean mAllowAutoRoll;

    public void setAutoRoll(boolean allow) {
        if (mAllowAutoRoll == allow) {
            return;
        }
        if (allow) {
            sHandler.postDelayed(mGoNextPageRunnable, 1000);
        } else {
            sHandler.removeCallbacks(mGoNextPageRunnable);
        }
        mAllowAutoRoll = allow;

    }

    Runnable mGoNextPageRunnable = new Runnable() {
        @Override
        public void run() {
            goNextPage();
            sHandler.postDelayed(this, 3000);
        }
    };

    boolean mIsGoRight = true;

    private void goNextPage() {
        int currentIndex = mViewPager.getCurrentItem();
        if (currentIndex == 0) {
            mIsGoRight = true;
        }
        if (currentIndex == mPagerAdapter.getCount() - 1) {
            mIsGoRight = false;
        }
        int nextIndex = 0;
        if (mIsGoRight) {
            nextIndex = currentIndex + 1;
        } else {
            nextIndex = currentIndex - 1;
        }
        // 当只有一个页面，而且在自动滚动的时候，nextIndex是-1 ，但代码没有崩，因为 ViewPager的setCurrentItem 有容错处理，小于0为0，大于最大为最大
        mViewPager.setCurrentItem(nextIndex);
    }


    PagerAdapter mPagerAdapter = new PagerAdapter() {

        @Override
        public int getCount() {
            return scrollble ? 200 : mItems.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            ImageView imageView = new ImageView(container.getContext());
            final int i = position % mItems.size();
            Glide.with(container.getContext()).load(mItems.get(i).getPicUrl()).placeholder(R.drawable.banner_default).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = mItems.get(i).getUrl();
                    trackUser("推广", "Banner图", url);
                    if (url.contains("opus")) {
                        if (url.contains("opus-h")) {
                            VideoH5Aty.startCommonH5((Activity) v.getContext(), url, context.getString(R.string.app_name));
                        } else {
                            WebContainerActivity.startActivity(v.getContext(), url, true);
                        }
                    } else {
                        if (Session.getUserIsLogin()) {
                            WebContainerActivity.startActivity(v.getContext(), url, true);
                        } else {
                            getContext().startActivity(new Intent(v.getContext(), LoginActivity.class));
                        }
                    }
                }
            });
            container.addView(imageView);
            return imageView;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    };

    private void trackUser(String text, String name, String value) {
        //定义与事件相关的属性信息
        JSONObject eventObject = new JSONObject();
        try {
            eventObject.put(name, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }


//记录事件
        ZhugeSDK.getInstance().track(QHApplication.getContext(), text, eventObject);

    }

    ViewPager.SimpleOnPageChangeListener mOnPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            int j = position % mItems.size();
            int childCount = mDotContainer.getChildCount();
            for (int i = 0; i < childCount; i++) {
                mDotContainer.getChildAt(i).setBackgroundResource(i == j ? R.drawable.arl_red_dot : R.drawable.arl_white_dot);
            }
        }
    };

    @Override
    public void onClick(View v) {
        int index = mDotContainer.indexOfChild(v);
        mViewPager.setCurrentItem(index);
    }

    boolean scrollble = true;

    public void setScrollble(boolean scrollble) {
        this.scrollble = scrollble;
    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        return scrollble?super.onInterceptTouchEvent(ev):true;
//    }
}
