package com.rz.circled.ui.fragment;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.ViewUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.rz.circled.BuildConfig;
import com.rz.circled.R;
import com.rz.circled.adapter.DefaultPrivateGroupAdapter;
import com.rz.circled.event.EventConstant;
import com.rz.circled.helper.CommonH5JumpHelper;
import com.rz.circled.ui.activity.WebContainerActivity;
import com.rz.circled.widget.CommomUtils;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

import static com.rz.circled.event.EventConstant.PRIVATE_GROUP_ESSENCE_MORE;
import static com.rz.circled.event.EventConstant.PRIVATE_GROUP_TAB_REFRESH;
import static com.rz.common.constant.CommonCode.Constant.PAGE_SIZE;
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

    private int type;
    private DefaultPrivateGroupAdapter mAdapter;
    private int pageNo = 1;

    public static PrivateGroupCreateByMyselfFragment newInstance(int type) {
        PrivateGroupCreateByMyselfFragment fragment = new PrivateGroupCreateByMyselfFragment();
        Bundle args = new Bundle();
        args.putInt(EXTRA_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments() != null ? getArguments().getInt(EXTRA_TYPE) : 0;
    }

    @Nullable
    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_my_create_private_group, null);
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

    private void loadData(final boolean loadMore) {
        Http.getApiService(ApiPGService.class).privateGroupMyselfCreate(Session.getUserId(), pageNo, PAGE_SIZE).enqueue(new BaseCallback<ResponseData<PrivateGroupListBean>>() {
            @Override
            public void onResponse(Call<ResponseData<PrivateGroupListBean>> call, Response<ResponseData<PrivateGroupListBean>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    if (!response.body().isSuccessful()) {
                        SVProgressHUD.showErrorWithStatus(getContext(), response.body().getMsg());
                    } else {
                        List<PrivateGroupBean> data = response.body().getData().getList();
                        if (type == TYPE_PART) {
                            if (data != null && data.size() > 0) {
                                if (data.size() > 2) {
                                    mAdapter.setData(data.subList(0, 2));
                                } else {
                                    mAdapter.setData(data);
                                }
                                Utility.setViewHeight(refreshLayout, Utility.setListViewHeightBasedOnChildren(lv));
                            }
                        } else {
                            if (data != null && data.size() > 0) {
                                if (loadMore) {
                                    mAdapter.addData(data);
                                } else {
                                    mAdapter.setData(data);
                                    pageNo = 1;
                                }
                                pageNo++;
                            }
                            if (mAdapter.getCount() == 0) {
                                onLoadingStatus(CommonCode.General.DATA_EMPTY);
                            }
                        }
                        EventBus.getDefault().post(new BaseEvent(EventConstant.USER_CREATE_PRIVATE_GROUP_NUM, data.size()));
                    }
                } else {
                    if (type != TYPE_PART)
                        SVProgressHUD.showErrorWithStatus(getContext(), getString(R.string.request_failed));
                }
            }

            @Override
            public void onFailure(Call<ResponseData<PrivateGroupListBean>> call, Throwable t) {
                super.onFailure(call, t);
                if (type != TYPE_PART)
                    SVProgressHUD.showErrorWithStatus(getContext(), getString(R.string.request_failed));
            }
        });
    }
}
