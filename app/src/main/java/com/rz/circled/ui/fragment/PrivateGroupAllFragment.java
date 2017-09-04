package com.rz.circled.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.rz.circled.R;
import com.rz.circled.adapter.DefaultPrivateGroupAdapter;
import com.rz.common.constant.CommonCode;
import com.rz.common.event.BaseEvent;
import com.rz.common.ui.fragment.BaseFragment;
import com.rz.common.utils.Utility;
import com.rz.common.widget.svp.SVProgressHUD;
import com.rz.httpapi.api.ApiPGService;
import com.rz.httpapi.api.BaseCallback;
import com.rz.httpapi.api.Http;
import com.rz.httpapi.api.ResponseData.ResponseData;
import com.rz.httpapi.bean.PrivateGroupBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Response;

import static com.rz.circled.event.EventConstant.PRIVATE_GROUP_SEARCH_KEY;
import static com.rz.common.constant.CommonCode.Constant.PAGE_SIZE;

/**
 * Created by rzw2 on 2017/8/31.
 */

public class PrivateGroupAllFragment extends BaseFragment {

    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.layout_refresh)
    SwipyRefreshLayout layoutRefresh;

    private int pageNo;
    private DefaultPrivateGroupAdapter mAdapter;

    public static PrivateGroupAllFragment newInstance() {
        PrivateGroupAllFragment fragment = new PrivateGroupAllFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_all_private_group, null);
    }

    @Override
    public void initView() {
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        lv.setAdapter(mAdapter = new DefaultPrivateGroupAdapter(getContext(), R.layout.item_default_private_group, DefaultPrivateGroupAdapter.TYPE_SCAN));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        layoutRefresh.setDirection(SwipyRefreshLayoutDirection.BOTH);
        layoutRefresh.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                if (direction == SwipyRefreshLayoutDirection.TOP) {
                    loadData(false);
                } else {
                    loadData(true);
                }
            }
        });
    }

    @Override
    public void initData() {
        loadData(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void eventBus(BaseEvent event) {
        switch (event.getType()) {
            case PRIVATE_GROUP_SEARCH_KEY:
                mAdapter.setKeyWord((String) event.getData());
                break;
        }
    }

    private void loadData(final boolean loadMore) {
        Http.getApiService(ApiPGService.class).privateGroupList(pageNo, PAGE_SIZE).enqueue(new BaseCallback<ResponseData<List<PrivateGroupBean>>>() {
            @Override
            public void onResponse(Call<ResponseData<List<PrivateGroupBean>>> call, Response<ResponseData<List<PrivateGroupBean>>> response) {
                super.onResponse(call, response);
                layoutRefresh.setRefreshing(false);
                if (response.isSuccessful()) {
                    if (!response.body().isSuccessful()) {
                        SVProgressHUD.showErrorWithStatus(getContext(), response.body().getMsg());
                    } else {
                        List<PrivateGroupBean> data = response.body().getData();
                        if (data != null && data.size() > 0) {
                            if (loadMore) {
                                mAdapter.addData(data);
                                pageNo++;
                            } else {
                                mAdapter.setData(data);
                                pageNo = 1;
                            }
                        } else {
                            onLoadingStatus(CommonCode.General.DATA_EMPTY);
                        }
                    }
                } else {
                    SVProgressHUD.showErrorWithStatus(getContext(), getString(R.string.request_failed));
                }
            }

            @Override
            public void onFailure(Call<ResponseData<List<PrivateGroupBean>>> call, Throwable t) {
                super.onFailure(call, t);
                layoutRefresh.setRefreshing(false);
                SVProgressHUD.showErrorWithStatus(getContext(), getString(R.string.request_failed));
            }
        });

        List<PrivateGroupBean> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(new PrivateGroupBean());
        }
        mAdapter.setData(list);
        Utility.setListViewHeightBasedOnChildren(lv);
    }
}
