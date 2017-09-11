package com.rz.circled.ui.fragment;

import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.rz.circled.R;
import com.rz.circled.adapter.SearchPersonAdapter;
import com.rz.circled.adapter.SearchRewardAdapter;
import com.rz.circled.presenter.impl.SearchPresenter;
import com.rz.common.constant.CommonCode;
import com.rz.common.constant.Constants;
import com.rz.common.event.BaseEvent;
import com.rz.common.ui.fragment.BaseFragment;
import com.rz.httpapi.bean.SearchRewardBean;
import com.rz.httpapi.bean.StarListBean;
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
    SwipeRefreshLayout mRefresh;
    @BindView(R.id.lv_search_content)
    ListView lvReward;

    private SearchRewardAdapter rewardAdapter;
    private List<SearchRewardBean> rewardBeanList = new ArrayList<>();
    private SearchPresenter searchPresenter;
    public  static String keyWord = "";

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
    }

    @Override
    public void initData() {
        Log.e(TAG,"-----SearchRewardFragment------");

        mRefresh.setColorSchemeColors(Constants.COLOR_SCHEMES);
        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ((SearchPresenter) searchPresenter).searchQH(false,"ti","","","",SearchPresenter.SEARCH_PERSION);
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

            ((SearchPresenter) searchPresenter).searchQH(false,"ti","","","",SearchPresenter.SEARCH_PERSION);
        }
    }

    @Override
    public <T> void updateViewWithLoadMore(T t, boolean loadMore) {
        super.updateViewWithLoadMore(t, loadMore);
        if (t != null) {
            List<SearchRewardBean> mDatas = (List<SearchRewardBean>) t;
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

}
