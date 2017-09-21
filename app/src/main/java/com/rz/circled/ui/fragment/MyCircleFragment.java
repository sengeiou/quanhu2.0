package com.rz.circled.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.rz.circled.R;
import com.rz.circled.adapter.DefaultPricePrivateGroupAdapter;
import com.rz.circled.adapter.DefaultPrivateGroupAdapter;
import com.rz.circled.event.EventConstant;
import com.rz.circled.helper.CommonH5JumpHelper;
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

import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Response;

import static com.rz.common.constant.CommonCode.Constant.PAGE_SIZE;
import static com.rz.common.constant.IntentKey.EXTRA_TYPE;

/**
 * Created by Administrator on 2017/9/14 0014.
 */

public class MyCircleFragment extends BaseFragment {

    public static final int TYPE_PART = 0;
    public static final int TYPE_ALL = 1;
    private int typeCreateJoin = 1;

    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.layout_refresh)
    SwipyRefreshLayout refreshLayout;

    private int type;
    private DefaultPricePrivateGroupAdapter mAdapter;
    private int pageNo = 1;

    private View headView;
    private TextView createTxt;
    private TextView answerTxt;

    public static MyCircleFragment newInstance(int type) {
        MyCircleFragment fragment = new MyCircleFragment();
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
        return inflater.inflate(R.layout.fragment_my_join_private_group, null);
    }

    @Override
    public void initView() {

        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);

        headView = View.inflate(mActivity,R.layout.mine_top_layout,null);
        createTxt = (TextView) headView.findViewById(R.id.my_create_txt);
        answerTxt = (TextView) headView.findViewById(R.id.answer_txt);
        lv.addHeaderView(headView);

        createTxt.setText("创建的私圈");
        answerTxt.setText("加入的私圈");

        createTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeCreateJoin = 1;
                pageNo = 1;
                createTxt.setTextColor(getResources().getColor(R.color.tab_blue));
                createTxt.setBackgroundResource(R.drawable.shape_blue_bg_stroke);

                answerTxt.setTextColor(getResources().getColor(R.color.color_999999));
                answerTxt.setBackgroundResource(R.drawable.shape_white_bg_stroke);

                mAdapter.setData(null);
                loadDataCreate(false);
            }
        });

        answerTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeCreateJoin = 2;
                pageNo = 1;
                createTxt.setTextColor(getResources().getColor(R.color.color_999999));
                createTxt.setBackgroundResource(R.drawable.shape_white_bg_stroke);

                answerTxt.setTextColor(getResources().getColor(R.color.tab_blue));
                answerTxt.setBackgroundResource(R.drawable.shape_blue_bg_stroke);
                mAdapter.setData(null);
                loadDataJoin(false);
            }
        });


        if (type == TYPE_PART) {
            lv.setDivider(getResources().getDrawable(R.drawable.shape_private_group_divider));
            lv.setDividerHeight(getResources().getDimensionPixelOffset(R.dimen.px2));
            refreshLayout.setEnabled(false);
        }
        lv.setAdapter(mAdapter = new DefaultPricePrivateGroupAdapter(getContext(), R.layout.item_default_private_group, DefaultPrivateGroupAdapter.TYPE_DESC));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0){
                    PrivateGroupBean item = mAdapter.getItem(position);
                    CommonH5JumpHelper.startGroupHome(mActivity, item.getCircleRoute(), item.getCoterieId());
                }
            }
        });
        refreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                if (direction == SwipyRefreshLayoutDirection.TOP) {
                    if(typeCreateJoin == 1){
                        loadDataCreate(false);
                    }else{
                        loadDataJoin(false);
                    }
                } else {
                    if(typeCreateJoin == 1){
                        loadDataCreate(true);
                    }else{
                        loadDataJoin(true);
                    }

                }
            }
        });
    }

    @Override
    public void initData() {
        loadDataCreate(false);
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
//            case PRIVATE_GROUP_TAB_REFRESH:
//                loadData(false);
//                break;
        }
    }

    /**
     * 获取加入的私圈
     * @param loadMore
     */
    private void loadDataJoin(final boolean loadMore) {
        Http.getApiService(ApiPGService.class).privateGroupMyselfJoin(Session.getUserId(), pageNo, PAGE_SIZE).enqueue(new BaseCallback<ResponseData<PrivateGroupListBean>>() {
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
                        EventBus.getDefault().post(new BaseEvent(EventConstant.USER_JOIN_PRIVATE_GROUP_NUM, data.size()));
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

    /**
     * 获取创建的私圈
     * @param loadMore
     */
    private void loadDataCreate(final boolean loadMore) {
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

    }
}
