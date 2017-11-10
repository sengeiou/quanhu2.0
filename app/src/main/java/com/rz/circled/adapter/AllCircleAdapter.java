package com.rz.circled.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.rz.circled.ui.fragment.AllCircleFragment;

/**
 * Created by Administrator on 2017/11/8/008.
 */

public class AllCircleAdapter extends FragmentStatePagerAdapter {
    private AllCircleFragment[] fragments;
    String[] tabTitle;
    public AllCircleAdapter(FragmentManager fm, AllCircleFragment[] fragments) {
        super(fm);
        this.fragments = fragments;
        tabTitle = new String[]{"我的圈子","推荐圈子"};
    }
    @Override
    public CharSequence getPageTitle(int position) {
        // 转换字符串的类型
        return tabTitle[position];
    }


    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return tabTitle.length;
    }

}
