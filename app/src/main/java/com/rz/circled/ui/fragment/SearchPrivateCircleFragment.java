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
import com.rz.circled.adapter.DefaultPricePrivateGroupAdapter;
import com.rz.circled.adapter.DefaultPrivateGroupAdapter;
import com.rz.circled.helper.CommonH5JumpHelper;
import com.rz.circled.presenter.impl.SearchPresenter;
import com.rz.circled.ui.activity.WebContainerActivity;
import com.rz.circled.widget.CommomUtils;
import com.rz.common.constant.CommonCode;
import com.rz.common.event.BaseEvent;
import com.rz.common.ui.fragment.BaseFragment;
import com.rz.common.utils.StringUtils;
import com.rz.httpapi.bean.PrivateGroupBean;
import com.rz.httpapi.bean.SearchDataBean;
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
public class SearchPrivateCircleFragment extends BaseFragment {

    @BindView(R.id.refresh)
    SwipyRefreshLayout mRefresh;
    @BindView(R.id.lv_search_content)
    ListView lvContent;
    private DefaultPricePrivateGroupAdapter mAdapter;
//    private SearchDataBean searchDataBean = new SearchDataBean();
    private List<PrivateGroupBean> coterieInfosData = new ArrayList<>();
    private SearchPresenter searchPresenter;
    private String keyWord = "";

    public static SearchPrivateCircleFragment newInstance() {
        SearchPrivateCircleFragment frg = new SearchPrivateCircleFragment();
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


        lvContent.setAdapter(mAdapter = new DefaultPricePrivateGroupAdapter(getContext(), R.layout.item_default_private_group, DefaultPrivateGroupAdapter.TYPE_SCAN));
        lvContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PrivateGroupBean item = mAdapter.getItem(position);
                if (item.getStatus() == 3) {
                    CommonH5JumpHelper.startGroupHome(mActivity, item.getCircleRoute(), item.getCoterieId());
                }
            }
        });
    }

    @Override
    public void initData() {
        Log.e(TAG,"-----SearchPrivateCircleFragment------");

        initRefresh();

    }


    private void initRefresh() {
        mRefresh.setDirection(SwipyRefreshLayoutDirection.BOTTOM);
        mRefresh.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                if(!TextUtils.isEmpty(keyWord)){
                    searchPresenter.searchQH(true,keyWord,"","","",SearchPresenter.SEARCH_PERSION_CIRCLE);
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
                 searchPresenter.searchQH(false,keyWord,"","","",SearchPresenter.SEARCH_PERSION_CIRCLE);
            }

        }
    }

    @Override
    public <T> void updateViewWithLoadMore(T t, boolean loadMore) {
        super.updateViewWithLoadMore(t, loadMore);
        List<PrivateGroupBean> mDatas = (List<PrivateGroupBean>) t;
        if (null != mDatas && !mDatas.isEmpty()) {
            if (!loadMore) {
                coterieInfosData.clear();
            }
            coterieInfosData.addAll(mDatas);
            mAdapter.setKeyWord(keyWord);
            mAdapter.setData(coterieInfosData);
            mAdapter.notifyDataSetChanged();
        } else {
            if (!loadMore) {
                coterieInfosData.clear();
            }
            mAdapter.notifyDataSetChanged();
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
        return mAdapter != null && mAdapter.getCount() != 0;
    }

    @Override
    public void refreshPage() {
        searchPresenter.searchQH(false, keyWord, "", "", "", SearchPresenter.SEARCH_PERSION_CIRCLE);
    }
}
