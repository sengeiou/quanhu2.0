package com.rz.circled.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.rz.circled.R;
import com.rz.circled.adapter.MineRewardAdapter;
import com.rz.circled.adapter.RewardAdapter;
import com.rz.circled.presenter.IPresenter;
import com.rz.circled.presenter.impl.PersonInfoPresenter;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.IntentKey;
import com.rz.common.ui.fragment.BaseFragment;
import com.rz.httpapi.bean.MyRewardBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/9/14 0014.
 */

public class RewardArticalFragment extends BaseFragment {

    @BindView(R.id.refresh)
    SwipyRefreshLayout mRefresh;
    @BindView(R.id.lv_search_content)
    ListView lvReward;

    private MineRewardAdapter rewardAdapter;
    private List<MyRewardBean> rewardBeanList = new ArrayList<>();
    protected IPresenter presenter;

    private View headView;
    private TextView createTxt;
    private TextView answerTxt;
    private int type;

    public static RewardArticalFragment newInstance(int type) {
        RewardArticalFragment frg = new RewardArticalFragment();
        Bundle args = new Bundle();
        args.putInt(IntentKey.KEY_ID,type);
        frg.setArguments(args);

        return frg;
    }


    @Nullable
    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_search_reward, null);
    }

    @Override
    public void initView() {
        type = getArguments().getInt(IntentKey.KEY_ID);

        TextView textView = new TextView(mActivity);
        textView.setText("共12条内容");
        lvReward.addHeaderView(textView);

        rewardAdapter = new MineRewardAdapter(getActivity(), R.layout.item_reward_mine);
        rewardAdapter.setData(rewardBeanList);
        lvReward.setAdapter(rewardAdapter);
    }

    @Override
    public void initData() {
        initRefresh();

        ((PersonInfoPresenter) presenter).getMyReward(false, Session.getUserId(),type,null);

    }

    private void initRefresh() {
        mRefresh.setDirection(SwipyRefreshLayoutDirection.BOTTOM);
        mRefresh.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                ((PersonInfoPresenter) presenter).getMyReward(false, Session.getUserId(),type,null);
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
