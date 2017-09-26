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
import com.rz.circled.adapter.DynamicAdapter;
import com.rz.circled.presenter.impl.SearchPresenter;
import com.rz.circled.ui.activity.UserInfoActivity;
import com.rz.circled.ui.activity.WebContainerActivity;
import com.rz.circled.widget.CommomUtils;
import com.rz.common.constant.CommonCode;
import com.rz.common.event.BaseEvent;
import com.rz.common.ui.fragment.BaseFragment;
import com.rz.common.utils.StringUtils;
import com.rz.httpapi.bean.CircleDynamic;
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
public class SearchContentFragment extends BaseFragment{

    @BindView(R.id.refresh)
    SwipyRefreshLayout mRefresh;

    @BindView(R.id.lv_search_content)
    ListView lvContent;
    private DynamicAdapter dynamicAdapter;
    private List<CircleDynamic> circleDynamicList = new ArrayList<>();
    private SearchPresenter searchPresenter;
    public  String keyWord = "";

    public static SearchContentFragment newInstance() {
        SearchContentFragment frg = new SearchContentFragment();
        return frg;
    }

    @Nullable
    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_search_content, null);
    }

    @Override
    public void initView() {
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);

        mRefresh.setDirection(SwipyRefreshLayoutDirection.BOTH);

        //泛型要改
        dynamicAdapter = new DynamicAdapter(mActivity, circleDynamicList);
        lvContent.setAdapter(dynamicAdapter);

        lvContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (StringUtils.isEmpty(circleDynamicList.get(position).coterieId)||StringUtils.isEmpty(circleDynamicList.get(position).coterieName)) {
                    String circleUrl = CommomUtils.getCircleUrl(circleDynamicList.get(position).circleRoute,circleDynamicList.get(position).moduleEnum, circleDynamicList.get(position).resourceId);
                    WebContainerActivity.startActivity(mActivity, circleUrl);
                } else {
                    String url = CommomUtils.getDymanicUrl(circleDynamicList.get(position).circleRoute,circleDynamicList.get(position).moduleEnum, circleDynamicList.get(position).coterieId, circleDynamicList.get(position).resourceId);
                    WebContainerActivity.startActivity(mActivity, url);
                }
            }
        });
    }

    @Override
    public void initData() {
        Log.e(TAG,"-----SearchContentFragment------");
        initRefresh();

    }

    private void initRefresh() {
        mRefresh.setDirection(SwipyRefreshLayoutDirection.BOTTOM);
        mRefresh.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                if(!TextUtils.isEmpty(keyWord)){
                    ((SearchPresenter) searchPresenter).searchQH(direction != SwipyRefreshLayoutDirection.TOP,keyWord,"","","",SearchPresenter.SEARCH_CONTENT);
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

            ((SearchPresenter) searchPresenter).searchQH(false,keyWord,"","","",SearchPresenter.SEARCH_CONTENT);
        }
    }


    @Override
    public <T> void updateViewWithLoadMore(T t, boolean loadMore) {
        super.updateViewWithLoadMore(t, loadMore);
        List<CircleDynamic> mDatas = (List<CircleDynamic>) t;
        if (null != mDatas && !mDatas.isEmpty()) {
            if (!loadMore) {
                circleDynamicList.clear();
            }
            circleDynamicList.addAll(mDatas);
            dynamicAdapter.setKeyWord(keyWord);
            dynamicAdapter.notifyDataSetChanged();
        } else {
            if (!loadMore) {
                circleDynamicList.clear();
            }
            dynamicAdapter.notifyDataSetChanged();
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
        return dynamicAdapter != null && dynamicAdapter.getCount() != 0;
    }

    @Override
    public void refreshPage() {
        ((SearchPresenter) searchPresenter).searchQH(false,keyWord,"","","",SearchPresenter.SEARCH_CONTENT);
    }

    @Override
    public void setFunctionText(String string) {


    }
}
