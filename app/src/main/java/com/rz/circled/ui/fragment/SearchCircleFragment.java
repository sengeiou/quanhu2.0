package com.rz.circled.ui.fragment;

import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;

import com.rz.circled.R;
import com.rz.circled.adapter.SearchCircleAdapter;
import com.rz.circled.adapter.SearchRewardAdapter;
import com.rz.circled.presenter.impl.SearchPresenter;
import com.rz.common.constant.CommonCode;
import com.rz.common.constant.Constants;
import com.rz.common.event.BaseEvent;
import com.rz.common.ui.fragment.BaseFragment;
import com.rz.httpapi.bean.CircleEntrModle;
import com.rz.httpapi.bean.SearchRewardBean;
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
public class SearchCircleFragment extends BaseFragment {


    @BindView(R.id.refresh)
    SwipeRefreshLayout mRefresh;
    @BindView(R.id.gv_search_circle)
    GridView gvCircle;
    private SearchCircleAdapter circleAdapter;
    private List<CircleEntrModle> circleBeanList = new ArrayList<>();
    private SearchPresenter searchPresenter;
    public  static String keyWord = "";

    public static SearchCircleFragment newInstance() {
        SearchCircleFragment frg = new SearchCircleFragment();
        return frg;
    }

    @Override
    protected boolean needLoadingView() {
        return true;
    }


    @Nullable
    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_seach_circle, null);
    }

    @Override
    public void initView() {
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);

        circleAdapter = new SearchCircleAdapter(getActivity(), R.layout.item_choose_circle);
        circleAdapter.setData(circleBeanList);
        gvCircle.setAdapter(circleAdapter);

    }

    @Override
    public void initData() {
        Log.e(TAG,"-----SearchCircleFragment------");

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

            ((SearchPresenter) searchPresenter).searchQH(false,"ti","","","",SearchPresenter.SEARCH_CIRCLE);
        }
    }

    @Override
    public <T> void updateViewWithLoadMore(T t, boolean loadMore) {
        super.updateViewWithLoadMore(t, loadMore);
        if (t != null) {
            List<CircleEntrModle> mDatas = (List<CircleEntrModle>) t;
            if (null != mDatas && !mDatas.isEmpty()) {
                if (!loadMore) {
                    circleBeanList.clear();
                }
                circleBeanList.addAll(mDatas);
                circleAdapter.setData(circleBeanList);
                circleAdapter.notifyDataSetChanged();
            } else {
                if (!loadMore) {
                    circleBeanList.clear();
                }
                circleAdapter.notifyDataSetChanged();
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
