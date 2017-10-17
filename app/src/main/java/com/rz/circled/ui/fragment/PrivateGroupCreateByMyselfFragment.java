package com.rz.circled.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.rz.circled.R;
import com.rz.circled.adapter.DefaultPrivateGroupAdapter;
import com.rz.circled.dialog.GroupLevelLessDialog;
import com.rz.circled.event.EventConstant;
import com.rz.circled.helper.CommonH5JumpHelper;
import com.rz.circled.presenter.impl.PrivateGroupPresenter;
import com.rz.circled.ui.activity.ApplyForCreatePrivateGroupActivity;
import com.rz.common.cache.preference.Session;
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
import com.rz.httpapi.bean.PrivateGroupListBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Response;

import static com.rz.circled.event.EventConstant.PRIVATE_GROUP_TAB_REFRESH;
import static com.rz.common.constant.CommonCode.Constant.PAGE_SIZE;
import static com.rz.common.constant.IntentKey.EXTRA_ID;
import static com.rz.common.constant.IntentKey.EXTRA_TYPE;

/**
 * Created by rzw2 on 2017/8/31.
 */

public class PrivateGroupCreateByMyselfFragment extends BaseFragment {

    public static final int TYPE_PART = 0;
    public static final int TYPE_ALL = 1;

    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.layout_refresh)
    SwipyRefreshLayout refreshLayout;

    //私圈相关
    private PrivateGroupPresenter mPresenter;
    private int type;
    private String userId;
    private DefaultPrivateGroupAdapter mAdapter;
    private int pageNo = 1;

    public static PrivateGroupCreateByMyselfFragment newInstance(int type) {
        return newInstance(type, Session.getUserId());
    }

    public static PrivateGroupCreateByMyselfFragment newInstance(int type, String userId) {
        PrivateGroupCreateByMyselfFragment fragment = new PrivateGroupCreateByMyselfFragment();
        Bundle args = new Bundle();
        args.putInt(EXTRA_TYPE, type);
        args.putString(EXTRA_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments() != null ? getArguments().getInt(EXTRA_TYPE) : 0;
        userId = getArguments() != null ? getArguments().getString(EXTRA_ID) : Session.getUserId();
    }

    @Nullable
    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_my_create_private_group, null);
    }

    @Override
    public void initPresenter() {
        mPresenter = new PrivateGroupPresenter();
        mPresenter.attachView(this);
    }

    @Override
    public void initView() {
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        if (type == TYPE_PART) {
            lv.setDivider(getResources().getDrawable(R.drawable.shape_private_group_divider));
            lv.setDividerHeight(getResources().getDimensionPixelOffset(R.dimen.px2));
            refreshLayout.setEnabled(false);
        }
        if (TextUtils.equals(userId, Session.getUserId())) {
            setFunctionText(R.string.private_group_apply_for);
            setFunctionClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String level = TextUtils.isEmpty(Session.getUserLevel()) ? "0" : Session.getUserLevel();
                    if (Integer.parseInt(level) < 5)
                        GroupLevelLessDialog.newInstance(level).show(getFragmentManager(), "");
                    else
                        startActivity(new Intent(getContext(), ApplyForCreatePrivateGroupActivity.class));
                }
            });
        } else {
            if (lv.getFooterViewsCount() <= 0) {
                LayoutInflater inflater = LayoutInflater.from(mActivity);
                View view = inflater.inflate(R.layout.foot_view, null);
                lv.addFooterView(view);
            }
        }

        lv.setAdapter(mAdapter = new DefaultPrivateGroupAdapter(getContext(), R.layout.item_default_private_group, DefaultPrivateGroupAdapter.TYPE_DESC));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PrivateGroupBean item = mAdapter.getItem(position);
                if (item.getStatus() == 3) {
                    CommonH5JumpHelper.startGroupHome(mActivity, item.getCircleRoute(), item.getCoterieId());
                }
            }
        });
        refreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
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
    protected boolean needLoadingView() {
        return true;
    }

    @Override
    protected boolean hasDataInPage() {
        return mAdapter != null && mAdapter.getCount() != 0;
    }

    @Override
    public <T> void updateViewWithLoadMore(T t, boolean loadMore) {
        super.updateViewWithLoadMore(t, loadMore);
        if (t instanceof PrivateGroupListBean) {
            PrivateGroupListBean _data = (PrivateGroupListBean) t;
            processData(_data, loadMore);
        }
    }

    @Override
    public <T> void updateView(T t) {
        super.updateView(t);
        if (t instanceof PrivateGroupListBean) {
            PrivateGroupListBean _data = (PrivateGroupListBean) t;
            if (!hasDataInPage()) {
                processData(_data, false);
            }
        }
    }

    private void processData(PrivateGroupListBean _data, boolean loadMore) {
        if (refreshLayout == null) return;
        List<PrivateGroupBean> data = _data.getList();
        if (type == TYPE_PART) {
            if (data.size() > 2) {
                mAdapter.setData(data.subList(0, 2));
            } else {
                mAdapter.setData(data);
            }
            Utility.setViewHeight(refreshLayout, Utility.setListViewHeightBasedOnChildren(lv));
        } else {
            if (loadMore) {
                mAdapter.addData(data);
            } else {
                mAdapter.setData(data);
            }
            pageNo++;
        }
        EventBus.getDefault().post(new BaseEvent(EventConstant.USER_CREATE_PRIVATE_GROUP_NUM, _data.getCount()));
    }

    @Override
    public void onLoadingStatus(int loadingStatus, String string) {
        if (type != TYPE_PART) {
            super.onLoadingStatus(loadingStatus, string);
            if (refreshLayout != null)
                refreshLayout.setRefreshing(false);
        } else {
            if (loadingStatus == CommonCode.General.DATA_EMPTY)
                EventBus.getDefault().post(new BaseEvent(EventConstant.USER_CREATE_PRIVATE_GROUP_NUM, mAdapter.getCount()));
        }
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
            case PRIVATE_GROUP_TAB_REFRESH:
                loadData(false);
                break;
        }
    }

    private void loadData(boolean loadMore) {
        if (!loadMore) pageNo = 1;
        mPresenter.privateGroupMyselfCreate(userId, pageNo, loadMore);
    }

    @Override
    public void refreshPage() {
        loadData(false);
    }

}
