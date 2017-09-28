package com.rz.circled.ui.activity;

import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;

import com.rz.circled.R;
import com.rz.circled.ui.fragment.MyCardCouponsFragment;
import com.rz.circled.widget.PagerSlidingTabStripHome;
import com.rz.common.ui.activity.BaseActivity;

import butterknife.BindView;

/**
 * Created by Gsm on 2017/9/19.
 */
public class MyCouponsActivity extends BaseActivity {

    @BindView(R.id.tab_pager_coupons)
    PagerSlidingTabStripHome tabPagerCoupons;
    @BindView(R.id.vp_coupons)
    ViewPager vpCoupons;
    private CouponsAdapter couponsAdapter;
    private MyCardCouponsFragment[] fragments;

    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_my_coupons, null);
    }

    @Override
    public void initView() {

        setTitleText(R.string.my_coupons);

        fragments = new MyCardCouponsFragment[]{MyCardCouponsFragment.newInstance(0), MyCardCouponsFragment.newInstance(1)};

        tabPagerCoupons.setCustomLayoutParams(2);
        tabPagerCoupons.setLineFitFont(true);
        couponsAdapter = new CouponsAdapter(getSupportFragmentManager());
        vpCoupons.setAdapter(couponsAdapter);
        vpCoupons.setOffscreenPageLimit(2);

        tabPagerCoupons.setViewPager(vpCoupons);
        tabPagerCoupons.notifyDataSetChanged();
        tabPagerCoupons.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void refreshPage() {

    }

    private class CouponsAdapter extends FragmentStatePagerAdapter {

        private final int[] itemName;

        public CouponsAdapter(FragmentManager fm) {
            super(fm);
            itemName = new int[]{R.string.tab_coupons, R.string.tab_award};
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // 转换字符串的类型
            SpannableStringBuilder ssb = new SpannableStringBuilder(getString(itemName[position]));
            StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
            ssb.setSpan(styleSpan,0,2, Spannable. SPAN_INCLUSIVE_INCLUSIVE);
            setTitleRightText(ssb.toString());
            return ssb.toString();
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
            return itemName.length;
        }
    }
}
