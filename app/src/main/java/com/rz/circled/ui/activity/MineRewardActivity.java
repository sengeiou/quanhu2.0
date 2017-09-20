package com.rz.circled.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.rz.circled.R;
import com.rz.circled.ui.fragment.MyArticleFragment;
import com.rz.circled.ui.fragment.MyRewardFragment;
import com.rz.circled.ui.fragment.RewardArticalFragment;
import com.rz.circled.widget.PagerSlidingTabStripHome;
import com.rz.common.ui.activity.BaseActivity;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/9/19 0019.
 */

public class MineRewardActivity extends BaseActivity {

    @BindView(R.id.tab_pager_search)
    PagerSlidingTabStripHome tabPagerSearch;

    @BindView(R.id.vp_view)
    ViewPager viewPager;

    private InfoAdapter infoAdapter;

    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_mine_reward, null);
    }

    @Override
    public void initView() {
        setTitleText("我的打赏");

        tabPagerSearch.setCustomLayoutParams(2);
        tabPagerSearch.setLineFitFont(true);
        infoAdapter = new InfoAdapter(getSupportFragmentManager());
        viewPager.setAdapter(infoAdapter);
        viewPager.setOffscreenPageLimit(2);

//        tabPagerSearch.setTempPosition(type);

        tabPagerSearch.setViewPager(viewPager);
        tabPagerSearch.notifyDataSetChanged();
        tabPagerSearch.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                scrollableLayout.getHelper().setCurrentScrollableContainer(fragmentList.get(i));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    protected boolean needShowTitle() {
        return true;
    }

    @Override
    public void initData() {

    }

    private class InfoAdapter extends FragmentStatePagerAdapter {

        private final int[] itemName;

        public InfoAdapter(FragmentManager fm) {
            super(fm);
            itemName = new int[]{R.string.reward_artical, R.string.rewarded_artical};
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getString(itemName[position]);
        }



        @Override
        public Fragment getItem(int position) {
            if (position == 0)
                return RewardArticalFragment.newInstance(0);
            if (position == 1)
                return RewardArticalFragment.newInstance(1);

            return MyArticleFragment.newInstance();
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
