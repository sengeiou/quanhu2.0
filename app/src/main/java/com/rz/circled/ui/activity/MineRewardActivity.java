package com.rz.circled.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rz.circled.R;
import com.rz.circled.presenter.IPresenter;
import com.rz.circled.presenter.impl.PersonInfoPresenter;
import com.rz.circled.presenter.impl.SearchPresenter;
import com.rz.circled.ui.fragment.MyArticleFragment;
import com.rz.circled.ui.fragment.MyRewardFragment;
import com.rz.circled.ui.fragment.RewardArticalFragment;
import com.rz.circled.widget.PagerSlidingTabStripHome;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.CommonCode;
import com.rz.common.event.BaseEvent;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.httpapi.bean.RewardStatBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/9/19 0019.
 */

public class MineRewardActivity extends BaseActivity {

    @BindView(R.id.tab_pager_search)
    PagerSlidingTabStripHome tabPagerSearch;

    @BindView(R.id.vp_view)
    ViewPager viewPager;

    @BindView(R.id.reward_top_txt)
    TextView rewardTxt;

    @BindView(R.id.rewarded_layout)
    LinearLayout rewardedLyout;

    private InfoAdapter infoAdapter;
    protected IPresenter presenter;


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

        rewardedLyout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }


    @Override
    public void initPresenter() {
        super.initPresenter();
        presenter = new PersonInfoPresenter();
        presenter.attachView(this);

    }


    @Override
    protected boolean needShowTitle() {
        return true;
    }

    @Override
    public void initData() {
        ((PersonInfoPresenter) presenter).getMyRewardStat(Session.getUserId());

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

    @Override
    public <T> void updateView(T t) {

        if(t instanceof RewardStatBean){
            RewardStatBean model = (RewardStatBean) t;

            rewardTxt.setText(model.getTotalRewardedAmount()+"");

            BaseEvent baseEvent = new BaseEvent(CommonCode.EventType.TYPE_REWARD_COUNT, model);
            EventBus.getDefault().post(baseEvent);

        }

    }
}
