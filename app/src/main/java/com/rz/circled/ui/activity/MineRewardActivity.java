package com.rz.circled.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cpoopc.scrollablelayoutlib.ScrollableHelper;
import com.cpoopc.scrollablelayoutlib.ScrollableLayout;
import com.rz.circled.R;
import com.rz.circled.adapter.MyFragmentStatePagerAdapter;
import com.rz.circled.presenter.IPresenter;
import com.rz.circled.presenter.impl.PersonInfoPresenter;
import com.rz.circled.ui.fragment.RewardArticalFragment;
import com.rz.circled.widget.PagerSlidingTabStripHome;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.CommonCode;
import com.rz.common.constant.Type;
import com.rz.common.event.BaseEvent;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.ui.fragment.BaseFragment;
import com.rz.httpapi.bean.RewardStatBean;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/9/19 0019.
 */

public class MineRewardActivity extends BaseActivity {


    @BindView(R.id.scrollableLayout)
    ScrollableLayout scrollableLayout;

    @BindView(R.id.tab_pager_search)
    PagerSlidingTabStripHome tabPagerSearch;

    @BindView(R.id.vp_view)
    ViewPager viewPager;

    @BindView(R.id.reward_top_txt)
    TextView rewardTxt;

    @BindView(R.id.rewarded_layout)
    LinearLayout rewardedLyout;

    private MyFragmentStatePagerAdapter infoAdapter;
    protected IPresenter presenter;

    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> mTitles = new ArrayList<>();
    private static final Integer[] TITLES = new Integer[]{R.string.reward_artical, R.string.rewarded_artical};



    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_mine_reward, null);
    }

    @Override
    public void initView() {
        setTitleText("我的打赏");

        for (int resId : TITLES) mTitles.add(getString(resId));
        BaseFragment fragment = RewardArticalFragment.newInstance(0);
        BaseFragment fragment1 = RewardArticalFragment.newInstance(1);
        fragmentList.add(fragment);
        fragmentList.add(fragment1);
        scrollableLayout.getHelper().setCurrentScrollableContainer((ScrollableHelper.ScrollableContainer) fragment);


        tabPagerSearch.setCustomLayoutParams(2);
        tabPagerSearch.setLineFitFont(true);
        viewPager.setAdapter(infoAdapter = new MyFragmentStatePagerAdapter(getSupportFragmentManager(), fragmentList, mTitles));
        viewPager.setOffscreenPageLimit(2);

        tabPagerSearch.setViewPager(viewPager);
        tabPagerSearch.notifyDataSetChanged();
        tabPagerSearch.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                ScrollableHelper.ScrollableContainer container = null;
                switch (position) {
                    case 0:
                        container = (RewardArticalFragment) infoAdapter.getItem(position);
                        break;
                    case 1:
                        container = (RewardArticalFragment) infoAdapter.getItem(position);
                        break;

                }
                scrollableLayout.getHelper().setCurrentScrollableContainer(container);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        rewardedLyout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RewardDetailAty.startAccountDetail(aty, Type.TYPE_SCORE);
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

    @Override
    public void refreshPage() {

    }

    @Override
    public <T> void updateView(T t) {

        if(t instanceof RewardStatBean){
            RewardStatBean model = (RewardStatBean) t;

            float price = Float.valueOf(model.getTotalRewardedAmount());
            DecimalFormat fnum = new DecimalFormat("##0.00");
            String  dd = fnum.format(price/100);
            rewardTxt.setText(dd);

            BaseEvent baseEvent = new BaseEvent(CommonCode.EventType.TYPE_REWARD_COUNT, model);
            EventBus.getDefault().post(baseEvent);

        }else{
            rewardTxt.setText("0.00");
            BaseEvent baseEvent = new BaseEvent(CommonCode.EventType.TYPE_EMPTY_COUNT, 0);
            EventBus.getDefault().post(baseEvent);
        }

    }
}
