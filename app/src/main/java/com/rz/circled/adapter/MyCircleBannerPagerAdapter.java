package com.rz.circled.adapter;

import android.support.v4.view.PagerAdapter;
<<<<<<< HEAD
import android.view.View;
import android.view.ViewGroup;
=======
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
>>>>>>> 2540931ec03580503cb88e4fe7ef18497de3b69c

import java.util.List;

/**
 * 打赏礼物的adapter
 */
public class MyCircleBannerPagerAdapter extends PagerAdapter {
    //viewpager数据源
    private List<View> listViews;
    //页数
    private int size;

    public MyCircleBannerPagerAdapter(List<View> listViews) {
        // 初始化viewpager的时候给的一个页面
        this.listViews = listViews;
        size = listViews == null ? 0 : listViews.size();
    }

    public void setListViews(List<View> listViews) {// 自己写的一个方法用来添加数据
        this.listViews = listViews;
        size = listViews == null ? 0 : listViews.size();
    }

    @Override
    public int getCount() {
        //告诉容器我们的数据长度为Integer.MAX_VALUE，这样就可以一直滚动
        return size;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //若position超过mDataList.size()，会发生越界异常，所以这里每次超过size又从0开始计算位置
        position = position % size;

        View view = listViews.get(position);
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        position = position % size;

        container.removeView(listViews.get(position));

    }
}
