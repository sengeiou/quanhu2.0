package com.rz.circled.ui.fragment;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.rz.circled.R;
import com.rz.circled.adapter.SearchRewardAdapter;
import com.rz.circled.helper.CommonH5JumpHelper;
import com.rz.circled.presenter.impl.SearchPresenter;
import com.rz.common.constant.CommonCode;
import com.rz.common.event.BaseEvent;
import com.rz.common.ui.fragment.BaseFragment;
import com.rz.httpapi.bean.PrivateGroupBean;
import com.rz.httpapi.bean.RewardModel;
import com.rz.httpapi.bean.UserInfoBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * Created by Gsm on 2017/9/2.
 */
public class SearchRewardFragment extends BaseFragment {


    @BindView(R.id.refresh)
    SwipyRefreshLayout mRefresh;
    @BindView(R.id.lv_search_content)
    ListView lvReward;

    private SearchRewardAdapter rewardAdapter;
    private List<RewardModel> rewardBeanList = new ArrayList<>();
    private SearchPresenter searchPresenter;
    private String keyWord = "";

    public SearchRewardFragment() {
    }

    public static SearchRewardFragment newInstance() {
        SearchRewardFragment frg = new SearchRewardFragment();
        return frg;
    }

    @Nullable
    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_search_reward, null);
    }

    @Override
    public void initView() {
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);

        rewardAdapter = new SearchRewardAdapter(getActivity(), R.layout.item_search_reward);
        rewardAdapter.setData(rewardBeanList);
        lvReward.setAdapter(rewardAdapter);

        lvReward.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CommonH5JumpHelper.startRewardDetail(mActivity,rewardBeanList.get(position).getOrderId()+"");
            }
        });

    }

    @Override
    public void initData() {
        initRefresh();
    }

    private void initRefresh() {
        mRefresh.setDirection(SwipyRefreshLayoutDirection.BOTTOM);
        mRefresh.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                if(!TextUtils.isEmpty(keyWord)){
                    ((SearchPresenter) searchPresenter).searchQH(true,keyWord,"","","",SearchPresenter.SEARCH_REWARD);
                }
                mRefresh.setRefreshing(false);
            }
        });
    }


    @Override
    public void initPresenter() {
        super.initPresenter();
        //搜索接口
        searchPresenter = new SearchPresenter();
        searchPresenter.attachView(this);

    }

    /**
     * @param baseEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BaseEvent baseEvent) {
        if (baseEvent.type == CommonCode.EventType.SEARCH_KEYWORD && baseEvent.data != null && searchPresenter != null) {
            //去搜索
            keyWord = (String) baseEvent.getData();
            if(!TextUtils.isEmpty(keyWord)){
                ((SearchPresenter) searchPresenter).searchQH(false,keyWord,"","","",SearchPresenter.SEARCH_REWARD);
            }

        }
    }

    @Override
    public <T> void updateViewWithLoadMore(T t, boolean loadMore) {
        super.updateViewWithLoadMore(t, loadMore);
        if (t != null) {
            List<RewardModel> mDatas = (List<RewardModel>) t;
            if (null != mDatas && !mDatas.isEmpty()) {
                if (!loadMore) {
                    rewardBeanList.clear();
                }
                rewardBeanList.addAll(mDatas);
                rewardAdapter.setKeyWord(keyWord);
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
    public <T> void updateView(T t) {
        super.updateView(t);
        if (null != t) {
            if (t instanceof UserInfoBean) {
                UserInfoBean model = (UserInfoBean) t;
                if (null != model) {

//                    NotifyEvent notifyEvent = new NotifyEvent("register", model, true);
//                    EventBus.getDefault().post(notifyEvent);

                    /**
                     * 更新界面，更新adapter数据
                     */

                }
            } else {
                BaseEvent event = new BaseEvent();
                event.info = "1";
                EventBus.getDefault().post(event);
            }
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    @Override
    protected boolean needLoadingView() {
        return true;
    }

    @Override
    protected boolean hasDataInPage() {
        return rewardAdapter != null && rewardAdapter.getCount() != 0;
    }

    @Override
    public void refreshPage() {

    }
}
