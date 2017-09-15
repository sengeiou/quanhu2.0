package com.rz.circled.ui.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.rz.circled.R;
import com.rz.circled.ui.fragment.MyActivityFragment;
import com.rz.circled.ui.fragment.MyArticleFragment;
import com.rz.circled.ui.fragment.MyCircleFragment;
import com.rz.circled.ui.fragment.MyRewardFragment;
import com.rz.common.ui.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/9/12 0012.
 */

public class UserInfoActivity extends BaseActivity{

    @BindView(R.id.tabs)
    TabLayout tabs;

    @BindView(R.id.vp_view)
    ViewPager viewPager;

    private List<Fragment> fragmentList;
    private MyFragmentPagerAdapter myPagerAdapter;

    private String pageTitle[] = {"文章","悬赏","私圈","活动"};

    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_user_info, null);
    }

    @Override
    public void initView() {

        fragmentList = new ArrayList<Fragment>();
    }

    @Override
    public void initData() {

        myPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        //给ViewPager绑定Adapter
        viewPager.setAdapter(myPagerAdapter);

        //把TabLayout和ViewPager关联起来
        tabs.setupWithViewPager(viewPager);


    }

    @Override
    protected boolean needShowTitle() {
        return false;
    }

    class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment = null;
            switch (position){
                case 0:
                    fragment = MyArticleFragment.newInstance();
                    break;
                case 1:
                    fragment = MyRewardFragment.newInstance();
                    break;
                case 2:
                    fragment = MyCircleFragment.newInstance();
                    break;
                case 3:
                    fragment = MyActivityFragment.newInstance();
                    break;
            }


            return  fragment;
        }

        @Override
        public int getCount() {
            return pageTitle.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return pageTitle[position];
        }
    }

    @Override
    protected boolean needSwipeBack() {
        return false;
    }
}
