package com.rz.circled.ui.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.rz.circled.R;
import com.rz.circled.widget.PagerSlidingTabStrip;
import com.rz.circled.widget.PopupView;
import com.rz.common.constant.IntentKey;
import com.rz.common.constant.Type;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.httpapi.bean.ChannelMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class AwesomeTabsAty extends BaseActivity implements ViewPager.OnPageChangeListener {

    /**
     * 内容展示
     */
    @BindView(R.id.id_comm_viewpager)
    ViewPager mPages;
    /**
     * 头部展示
     */
    @BindView(R.id.id_comm_pager_tab)
    PagerSlidingTabStrip mTab;
    /**
     * 整体布局
     */
    @BindView(R.id.id_comm_root_ll)
    LinearLayout root;

    private VideoPagesAdapter adapter;

    private PopupView mPopupView;

    private PopupView mPopupView1;

    public static final int SYSTEM_REQUEST_VIDEO = 1220;

    public static final int SYSYEM_REQUEST_AUDIO = 1320;

    /**
     * 存储头部栏目数据
     */
    private List<ChannelMode> mChannelModes = new ArrayList<ChannelMode>();

    /**
     * 列表类型
     */
    public int type = Type.TYPE_VIDEO;


    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.comm_pagersliding_tab, null);
    }


    @Override
    public void initPresenter() {
        super.initPresenter();
        initType();
//        mOpusChannelPresenter = new OpusChannelPresenter(type);
    }

    @Override
    public void initView() {
        setTitleText(getString(R.string.mine_my_ticket));
        adapter = new VideoPagesAdapter(getSupportFragmentManager());
        mPages.setAdapter(adapter);
        mTab.setViewPager(mPages);
        mTab.setLineFitFont(true);
        mTab.notifyDataSetChanged();

    }


    @Override
    public void initData() {
    }


    private void initType() {
        Intent intent = getIntent();
        if (intent != null && intent.getIntExtra(IntentKey.KEY_TYPE, -1) != -1) {
            type = intent.getIntExtra(IntentKey.KEY_TYPE, -1);
        }
    }

    @Override
    public <T> void updateView(T t) {
        super.updateView(t);
        List<ChannelMode> channelModes = (List<ChannelMode>) t;
        if (channelModes != null && channelModes.size() > 0) {
            Log.d("ChannelMode", "hasDataInPage" + hasDataInPage());
            Log.d("ChannelMode", "mChannelModes.size() != 0" + String.valueOf(mChannelModes.size() != 0));
            if (hasDataInPage() && mChannelModes.size() != 0) {
                if (!channelModes.equals(mChannelModes)) {
                    mChannelModes = channelModes;
                    adapter.notifyDataSetChanged();
                    initTabLayoutParams();
                    mTab.notifyDataSetChanged();
                    Log.d("ChannelMode", "adapter.getCount()" + adapter.getCount());
                    Log.d("ChannelMode", "mChannelModes.size()" + mChannelModes.size());
                    Log.d("ChannelMode", "mPages" + mPages.getChildCount());
                }
            } else {
                mChannelModes = channelModes;
                Log.d("ChannelMode", "this is updateView" + mChannelModes.size());
                initTabLayoutParams();
                mPages.setOffscreenPageLimit(4);
                mTab.notifyDataSetChanged();
            }
        }
    }

    /**
     * 计算头部栏目所占宽度
     */
    private void initTabLayoutParams() {
        if (mChannelModes != null) {
            mTab.reset();
            boolean hasLargeTitle = false;
            for (int i = 0; i < mChannelModes.size(); i++) {
                ChannelMode channelMode = mChannelModes.get(i);
                if (channelMode.channelName != null && channelMode.channelName.length() > 2) {
                    hasLargeTitle = true;
                    break;
                }
            }
            //如果有大于长度2的标题
            if (hasLargeTitle) {
                if (mChannelModes.size() > 5) {
                    mTab.setTabPaddingLeftRight((int) getResources().getDimension(R.dimen.px50));
//                    mTab.setLineFitFont(true);
                } else {
                    mTab.setCustomLayoutParams(mChannelModes.size());
                    if (type == Type.TYPE_RECORD_BAG)
                        mTab.setLineFitFont(true);
                }
            } else {
                //如果没有大于长度2的标题
                mTab.setCustomLayoutParams(mChannelModes.size() > 4 ? 4.5f : mChannelModes.size());
//                mTab.setLineFitFont(true);
            }
        }
        if (type == Type.TYPE_TICKET) {
            mTab.setLineFitFont(true);
        }
    }

    @Override
    public boolean hasDataInPage() {
        return adapter != null && adapter.getCount() != 0;
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private final String[] titles = {"卡券", "奖品"};

    @Override
    public void refreshPage() {

    }

    private class VideoPagesAdapter extends FragmentStatePagerAdapter {

        public VideoPagesAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public Fragment getItem(int position) {
//                return MyTicketsFrag.newInstace(mChannelModes.get(position).channelType);
            return null;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return mChannelModes.size();
        }
    }
}
