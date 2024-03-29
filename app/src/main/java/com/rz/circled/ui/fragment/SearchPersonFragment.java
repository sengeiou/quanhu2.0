package com.rz.circled.ui.fragment;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.rz.circled.R;
import com.rz.circled.adapter.SearchUserAdapter;
import com.rz.circled.presenter.impl.SearchPresenter;
import com.rz.circled.ui.activity.UserInfoActivity;
import com.rz.common.constant.CommonCode;
import com.rz.common.event.BaseEvent;
import com.rz.common.ui.fragment.BaseFragment;
import com.rz.httpapi.bean.SearchDataBean;
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
public class SearchPersonFragment extends BaseFragment {

    @BindView(R.id.refresh)
    SwipyRefreshLayout mRefresh;

    @BindView(R.id.lv_search_content)
    ListView lvPerson;
    private SearchUserAdapter personAdapter;
    private SearchPresenter searchPresenter;
    private String keyWord = "";
    private List<StarListBean.CustInfoBean> dataCustInfos = new ArrayList<>();


    public static SearchPersonFragment newInstance() {
        SearchPersonFragment frg = new SearchPersonFragment();
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

        int basePadding = (int) getResources().getDimension(R.dimen.app_base_padding);
        lvPerson.setPadding(basePadding, 0, basePadding, 0);
        lvPerson.setDividerHeight(0);

        personAdapter = new SearchUserAdapter(getActivity(), R.layout.item_search_person);
        personAdapter.setData(dataCustInfos);
        lvPerson.setAdapter(personAdapter);

        lvPerson.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserInfoActivity.newFrindInfo(mActivity, dataCustInfos.get(position).getCustId());
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
                    searchPresenter.searchQH(true, keyWord, "", "", "", SearchPresenter.SEARCH_PERSION);
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
                searchPresenter.searchQH(false, keyWord, "", "", "", SearchPresenter.SEARCH_PERSION);
            }
        }
    }


    @Override
    public <T> void updateView(T t) {
        super.updateView(t);
        if (null != t) {
            if (t instanceof UserInfoBean) {
                SearchDataBean model = (SearchDataBean) t;
                if (null != model) {

//                    NotifyEvent notifyEvent = new NotifyEvent("register", model, true);
//                    EventBus.getDefault().post(notifyEvent);
//                    personAdapter.setData(moreFamousList);

                    /**
                     * 更新界面，更新adapter数据
                     */
                }
            }
        }
    }

    @Override
    public <T> void updateViewWithLoadMore(T t, boolean loadMore) {
        super.updateViewWithLoadMore(t, loadMore);
        List<StarListBean.CustInfoBean> mDatas = (List<StarListBean.CustInfoBean>) t;
        if (null != mDatas && !mDatas.isEmpty()) {
            if (!loadMore) {
                dataCustInfos.clear();
            }
            dataCustInfos.addAll(mDatas);
            personAdapter.setKeyWord(keyWord);
            personAdapter.setData(dataCustInfos);
            personAdapter.notifyDataSetChanged();
        } else {
            if (!loadMore) {
                dataCustInfos.clear();
            }
            personAdapter.setData(dataCustInfos);
            personAdapter.notifyDataSetChanged();
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
        return personAdapter != null && personAdapter.getCount() != 0;
    }

    @Override
    public void refreshPage() {
        searchPresenter.searchQH(false, keyWord, "", "", "", SearchPresenter.SEARCH_PERSION);
    }

}
