package com.rz.circled.ui.fragment;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.rz.circled.R;
import com.rz.circled.adapter.SearchCircleAdapter;
import com.rz.circled.presenter.impl.SearchPresenter;
import com.rz.circled.ui.activity.AllCirclesAty;
import com.rz.circled.ui.activity.WebContainerActivity;
import com.rz.common.constant.CommonCode;
import com.rz.common.event.BaseEvent;
import com.rz.common.ui.fragment.BaseFragment;
import com.rz.httpapi.bean.CircleEntrModle;
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
    SwipyRefreshLayout mRefresh;
    @BindView(R.id.gv_search_circle)
    GridView gvCircle;
    private SearchCircleAdapter circleAdapter;
    private List<CircleEntrModle> circleBeanList = new ArrayList<>();
    private SearchPresenter searchPresenter;
    private String keyWord = "";

    public static SearchCircleFragment newInstance() {
        SearchCircleFragment frg = new SearchCircleFragment();
        return frg;
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

        gvCircle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CircleEntrModle circleEntrModle = circleBeanList.get(position);
                if (getString(R.string.FIND_MORE).equals(circleEntrModle.appId)) {
                    Intent intent = new Intent(mActivity, AllCirclesAty.class);
                    getActivity().startActivity(intent);
                } else {
                    circleEntrModle.click += 1;
                    WebContainerActivity.startActivity(mActivity, circleBeanList.get(position).circleUrl);
                }
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

                if (!TextUtils.isEmpty(keyWord)) {
                    searchPresenter.searchQH(true, keyWord, "", "", "", SearchPresenter.SEARCH_CIRCLE);
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
            if (!TextUtils.isEmpty(keyWord)) {
                searchPresenter.searchQH(false, keyWord, "", "", "", SearchPresenter.SEARCH_CIRCLE);
            }

        }
    }

    @Override
    public <T> void updateViewWithLoadMore(T t, boolean loadMore) {
        super.updateViewWithLoadMore(t, loadMore);
        List<CircleEntrModle> mDatas = (List<CircleEntrModle>) t;
        if (null != mDatas && !mDatas.isEmpty()) {
            if (!loadMore) {
                circleBeanList.clear();
            }
            circleBeanList.addAll(mDatas);
            circleAdapter.setKeyWord(keyWord);
            circleAdapter.setData(circleBeanList);
            circleAdapter.notifyDataSetChanged();
        } else {
            if (!loadMore) {
                circleBeanList.clear();
            }
            circleAdapter.notifyDataSetChanged();
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
        return circleAdapter != null && circleAdapter.getCount() != 0;
    }

    @Override
    public void refreshPage() {
        searchPresenter.searchQH(false, keyWord, "", "", "", SearchPresenter.SEARCH_CIRCLE);
    }
}
