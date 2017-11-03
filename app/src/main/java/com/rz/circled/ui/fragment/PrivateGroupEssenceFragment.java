package com.rz.circled.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.rz.circled.R;
import com.rz.circled.adapter.PrivateGroupEssenceAdapter;
import com.rz.circled.event.EventConstant;
import com.rz.circled.helper.CommonH5JumpHelper;
import com.rz.circled.presenter.impl.PrivateGroupPresenter;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.CommonCode;
import com.rz.common.event.BaseEvent;
import com.rz.common.ui.fragment.BaseFragment;
import com.rz.common.utils.Utility;
import com.rz.common.widget.MyListView;
import com.rz.common.widget.svp.SVProgressHUD;
import com.rz.httpapi.api.ApiPGService;
import com.rz.httpapi.api.BaseCallback;
import com.rz.httpapi.api.Http;
import com.rz.httpapi.api.ResponseData.ResponseData;
import com.rz.httpapi.bean.GroupBannerBean;
import com.rz.httpapi.bean.PrivateGroupBean;
import com.rz.httpapi.bean.PrivateGroupListBean;
import com.rz.httpapi.bean.PrivateGroupResourceBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

import static com.rz.circled.event.EventConstant.PRIVATE_GROUP_ESSENCE_MORE;
import static com.rz.circled.event.EventConstant.PRIVATE_GROUP_TAB_REFRESH;
import static com.rz.common.constant.CommonCode.Constant.PAGE_SIZE;

/**
 * Created by rzw2 on 2017/8/31.
 */

public class PrivateGroupEssenceFragment extends BaseFragment {

    @BindView(R.id.lv)
    MyListView lv;
    @BindView(R.id.tv)
    TextView tv;
    @BindView(R.id.line)
    View line;

    //私圈相关
    private PrivateGroupPresenter mPresenter;
    private PrivateGroupEssenceAdapter mAdapter;

    public static PrivateGroupEssenceFragment newInstance() {
        PrivateGroupEssenceFragment fragment = new PrivateGroupEssenceFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_essence_private_group, null);
    }

    @Override
    public void initView() {
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        tv.setText(R.string.private_group_essence_no_more);
        lv.setAdapter(mAdapter = new PrivateGroupEssenceAdapter(getContext(), R.layout.item_private_group_essence));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PrivateGroupResourceBean item = mAdapter.getItem(position);
                CommonH5JumpHelper.startResourceDetail(mActivity, item.getCircleRoute(), item.getCoterieId(), item.getModuleEnum(), item.getResourceId());
            }
        });
    }

    @Override
    public void initData() {
        loadData(false);
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
            if (_data.get(0) instanceof PrivateGroupResourceBean) {
                List<PrivateGroupResourceBean> data = (List<PrivateGroupResourceBean>) t;
                processData(data, loadMore);
            }
        }
    }

    @Override
    public <T> void updateView(T t) {
        super.updateView(t);
        if (t instanceof List) {
            List _data = (List) t;
            if (_data.get(0) instanceof PrivateGroupResourceBean) {
                List<PrivateGroupResourceBean> data = (List<PrivateGroupResourceBean>) t;
                if (mAdapter != null && mAdapter.getCount() == 0) {
                    processData(data, false);
                }
            }
        }
    }

    private void processData(List<PrivateGroupResourceBean> data, boolean loadMore) {
        if (tv == null || mAdapter == null) return;
        if (loadMore) {
            mAdapter.addData(data);
        } else {
            mAdapter.setData(data);
        }
        tv.setVisibility(View.GONE);
    }


    @Override
    public void onLoadingStatus(int loadingStatus, String string) {
        super.onLoadingStatus(loadingStatus, string);
        if (tv != null) {
            if (loadingStatus == CommonCode.General.DATA_EMPTY) {
                tv.setVisibility(View.VISIBLE);
                line.setVisibility(View.GONE);
            } else if (loadingStatus == CommonCode.General.DATA_LACK) {
                tv.setVisibility(View.VISIBLE);
                line.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.tv)
    public void onClick() {
    }

    @Subscribe
    public void eventBus(BaseEvent event) {
        switch (event.getType()) {
            case PRIVATE_GROUP_ESSENCE_MORE:
                loadData(true);
                break;
            case PRIVATE_GROUP_TAB_REFRESH:
                loadData(false);
                break;
        }
    }

    private void loadData(final boolean loadMore) {
        mPresenter.privateGroupEssence(loadMore ? mAdapter.getCount() : 0, loadMore);
    }

    @Override
    public void refreshPage() {

    }
}
