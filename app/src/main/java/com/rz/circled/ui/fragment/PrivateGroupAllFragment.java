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
import com.rz.circled.adapter.DefaultPricePrivateGroupAdapter;
import com.rz.circled.adapter.DefaultPrivateGroupAdapter;
import com.rz.circled.helper.CommonH5JumpHelper;
import com.rz.circled.presenter.impl.PrivateGroupPresenter;
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
import com.rz.httpapi.bean.PrivateGroupResourceBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Response;

import static com.rz.circled.event.EventConstant.PRIVATE_GROUP_SEARCH_KEY;
import static com.rz.common.constant.CommonCode.Constant.PAGE_SIZE;
import static com.rz.common.constant.IntentKey.EXTRA_BOOLEAN;

/**
 * Created by rzw2 on 2017/8/31.
 */

public class PrivateGroupAllFragment extends BaseFragment {

    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.layout_refresh)
    SwipyRefreshLayout layoutRefresh;

    //私圈相关
    private PrivateGroupPresenter mPresenter;
    private int pageNo = 1;
    private DefaultPricePrivateGroupAdapter mAdapter;

    public static PrivateGroupAllFragment newInstance() {
        return newInstance(true);
    }

    public static PrivateGroupAllFragment newInstance(boolean initData) {
        PrivateGroupAllFragment fragment = new PrivateGroupAllFragment();
        Bundle args = new Bundle();
        args.putBoolean(EXTRA_BOOLEAN, initData);
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
        lv.setAdapter(mAdapter = new DefaultPricePrivateGroupAdapter(getContext(), R.layout.item_default_private_group, DefaultPrivateGroupAdapter.TYPE_SCAN));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PrivateGroupBean item = mAdapter.getItem(position);
                CommonH5JumpHelper.startGroupHome(mActivity, item.getCircleRoute(), item.getCoterieId());
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
        if (getArguments().getBoolean(EXTRA_BOOLEAN))
            loadData(false);
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
    public void initPresenter() {
        mPresenter = new PrivateGroupPresenter();
        mPresenter.attachView(this);
    }

    @Override
    public <T> void updateViewWithLoadMore(T t, boolean loadMore) {
        super.updateViewWithLoadMore(t, loadMore);
        if (t instanceof List) {
            List _data = (List) t;
            if (_data.get(0) instanceof PrivateGroupBean) {
                List<PrivateGroupBean> data = (List<PrivateGroupBean>) t;
                if (loadMore) {
                    mAdapter.addData(data);
                } else {
                    mAdapter.setData(data);
                }
                pageNo++;
            }
        }
    }

    @Override
    public void onLoadingStatus(int loadingStatus, String string) {
        super.onLoadingStatus(loadingStatus, string);
        if (layoutRefresh != null)
            layoutRefresh.setRefreshing(false);
    }

    @Subscribe
    public void eventBus(BaseEvent event) {
        switch (event.getType()) {
            case PRIVATE_GROUP_SEARCH_KEY:
                mAdapter.setKeyWord((String) event.getData());
                loadData(false);
                break;
        }
    }

    private void loadData(boolean loadMore) {
        if (!loadMore) pageNo = 1;
        mPresenter.privateGroupList(pageNo, loadMore);
    }

    @Override
    public void refreshPage() {

    }
}
