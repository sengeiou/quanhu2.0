package com.rz.circled.ui.fragment;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.rz.circled.R;
import com.rz.circled.adapter.RewardAdapter;
import com.rz.circled.adapter.SearchRewardAdapter;
import com.rz.circled.presenter.IPresenter;
import com.rz.circled.presenter.impl.PersonInfoPresenter;
import com.rz.circled.presenter.impl.SearchPresenter;
import com.rz.common.cache.preference.Session;
import com.rz.common.ui.fragment.BaseFragment;
import com.rz.httpapi.bean.MyRewardBean;
import com.rz.httpapi.bean.RewardModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/9/14 0014.
 */

public class MyRewardFragment extends BaseFragment {

    @BindView(R.id.refresh)
    SwipyRefreshLayout mRefresh;
    @BindView(R.id.lv_search_content)
    ListView lvReward;

    private RewardAdapter rewardAdapter;
    private List<MyRewardBean> rewardBeanList = new ArrayList<>();
    protected IPresenter presenter;

    public static MyRewardFragment newInstance() {
        MyRewardFragment frg = new MyRewardFragment();
        return frg;
    }


    @Nullable
    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_search_reward, null);
    }

    @Override
    public void initView() {
        rewardAdapter = new RewardAdapter(getActivity(), R.layout.item_search_reward);
        rewardAdapter.setData(rewardBeanList);
        lvReward.setAdapter(rewardAdapter);
    }

    @Override
    public void initData() {
        initRefresh();

        ((PersonInfoPresenter) presenter).getMyreward(false, Session.getUserId(),1);

    }

    private void initRefresh() {
        mRefresh.setDirection(SwipyRefreshLayoutDirection.BOTTOM);
        mRefresh.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                ((PersonInfoPresenter) presenter).getMyreward(true, Session.getUserId(),0);
                mRefresh.setRefreshing(false);
            }
        });
    }

    @Override
    public void initPresenter() {
        super.initPresenter();
        //搜索接口
        presenter = new PersonInfoPresenter();
        presenter.attachView(this);

    }

    @Override
    public <T> void updateViewWithLoadMore(T t, boolean loadMore) {
        super.updateViewWithLoadMore(t, loadMore);
        if (t != null) {
            List<MyRewardBean> mDatas = (List<MyRewardBean>) t;
            if (null != mDatas && !mDatas.isEmpty()) {
                if (!loadMore) {
                    rewardBeanList.clear();
                }
                rewardBeanList.addAll(mDatas);
                rewardAdapter.setData(rewardBeanList);
                rewardAdapter.notifyDataSetChanged();
            } else {
                if (!loadMore) {
                    rewardBeanList.clear();
                }
                rewardAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected boolean needLoadingView() {
        return false;
    }

    @Override
    protected boolean hasDataInPage() {
        return rewardAdapter != null && rewardAdapter.getCount() != 0;
    }

}
