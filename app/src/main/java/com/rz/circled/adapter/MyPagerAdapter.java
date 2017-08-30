package com.rz.circled.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.List;

/**
 * 打赏礼物的adapter
 */
public class MyPagerAdapter extends PagerAdapter {
    //viewpager数据源
    private List<View> listViews;
    //页数
    private int size;

    public MyPagerAdapter(List<View> listViews) {
        // 初始化viewpager的时候给的一个页面
        this.listViews = listViews;
        size = listViews == null ? 0 : listViews.size();
    }

    public void setListViews(List<View> listViews) {// 自己写的一个方法用来添加数据
        this.listViews = listViews;
        size = listViews == null ? 0 : listViews.size();
    }

    public int getCount() {// 返回数量
        return size;
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public void destroyItem(View arg0, int arg1, Object arg2) {// 销毁view对象
        ((ViewPager) arg0).removeView(listViews.get(arg1 % size));
    }

    public void finishUpdate(View arg0) {
    }

    public Object instantiateItem(View arg0, int arg1) {// 返回view对象
        try {
            ((ViewPager) arg0).addView(listViews.get(arg1 % size), 0);

        } catch (Exception e) {
        }
        return listViews.get(arg1 % size);
    }

    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }
}
