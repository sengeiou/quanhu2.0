package com.rz.circled.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cpoopc.scrollablelayoutlib.ScrollableHelper;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.rz.circled.BuildConfig;
import com.rz.circled.R;
import com.rz.circled.http.ApiYylService;
import com.rz.circled.ui.activity.WebContainerActivity;
import com.rz.circled.widget.CommonAdapter;
import com.rz.circled.widget.GlideCenterRoundImage;
import com.rz.circled.widget.MListView;
import com.rz.circled.widget.ViewHolder;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.CommonCode;
import com.rz.common.constant.IntentKey;
import com.rz.common.event.BaseEvent;
import com.rz.common.ui.fragment.BaseFragment;
import com.rz.common.utils.NetUtils;
import com.rz.httpapi.api.Http;
import com.rz.httpapi.api.ResponseData.ResponseData;
import com.rz.httpapi.bean.ActivityBean;
import com.rz.httpapi.bean.EntitiesBean;
import com.rz.httpapi.constans.ReturnCode;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.rz.common.constant.IntentKey.EXTRA_TYPE;

/**
 * Created by Administrator on 2017/9/14 0014.
 */

public class MyActivityFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, ScrollableHelper.ScrollableContainer {

    List<EntitiesBean> bean = new ArrayList<>();
    @BindView(R.id.my_listview)
    MListView mLv;
    int pageNo = 1;
    @BindView(R.id.swipyrefreshlayout)
    SwipyRefreshLayout mActivityRefresh;
    private CommonAdapter<EntitiesBean> mEntitiesBeanCommonAdapter;

    //每页分页标记
    private int start = 0;

    //记录每页分页标记
    private int record_start = 0;

    //是否没有数据
    private boolean isNoData;

    private String userid = "";

    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_mineactivity, null);
    }


    public static MyActivityFragment newInstance(String userid) {
        MyActivityFragment frg = new MyActivityFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_TYPE, userid);
        frg.setArguments(args);
        return frg;
    }

    @Override
    public void initPresenter() {
        userid = getArguments().getString(IntentKey.EXTRA_TYPE);
        getData(false);
    }

    private void getData(final boolean loadMore) {

        if (!NetUtils.isNetworkConnected(mActivity)) {
            onLoadingStatus(CommonCode.General.UN_NETWORK, mActivity.getString(R.string.no_net_work));
            return;
        }

        if (!loadMore) {
            pageNo = 1;
        } else {
            if (isNoData) {
                pageNo = record_start;
            } else {
                pageNo++;
            }
            record_start = pageNo;
        }

        Http.getApiService(ApiYylService.class)
                .getMineActivityList(pageNo, 20, userid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseData<ActivityBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        onLoadingStatus(CommonCode.General.LOAD_ERROR, mActivity.getString(R.string.load_fail));
                        return;
                    }

                    @Override
                    public void onNext(ResponseData<ActivityBean> res) {
                        List<EntitiesBean> entities = null;
                        if (res.getData() != null && res.getData().entities != null) {
                            entities = res.getData().entities;
                        }
                        if (res.getRet() == ReturnCode.SUCCESS) {

                            if (null != entities && !entities.isEmpty()) {
                                isNoData = false;
                                onLoadingStatus(CommonCode.General.DATA_SUCCESS);
                                bean.clear();

                                bean.addAll(entities);
                                mEntitiesBeanCommonAdapter.notifyDataSetChanged();

                            } else {
                                if (loadMore == false) {

                                    if (Session.getUserId().equals(userid)) {
                                        onLoadingStatus(CommonCode.General.DATA_EMPTY, getString(R.string.mine_activity_txt));
                                    } else {
                                        onLoadingStatus(CommonCode.General.DATA_EMPTY, getString(R.string.mine_activity_txt));
                                    }

                                } else {
                                    onLoadingStatus(CommonCode.General.DATA_LACK);
                                }
                                isNoData = true;
                            }
                            return;
                        } else {
                            onLoadingStatus(CommonCode.General.ERROR_DATA);
                            isNoData = true;
                            return;
                        }

                    }
                });
    }


    @Override
    public void initView() {

        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);

        mActivityRefresh.setDirection(SwipyRefreshLayoutDirection.BOTH);
        mActivityRefresh.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                if (direction == SwipyRefreshLayoutDirection.TOP) {
                    getData(false);
                } else {
                    getData(true);
                }
                mActivityRefresh.setRefreshing(false);
            }
        });


        mEntitiesBeanCommonAdapter = new CommonAdapter<EntitiesBean>(mActivity, bean, R.layout.activity_item) {
            @Override
            public void convert(ViewHolder helper, EntitiesBean item) {
                ((TextView) helper.getView(R.id.activity_title)).setText(item.getTitle());
                ImageView iv = (ImageView) helper.getView(R.id.iv_activity_icon);
                Glide.with(mContext).load(item.getCoverPlan()).placeholder(R.drawable.ic_circle_img1).transform(new GlideCenterRoundImage(mContext, 10)).into(iv);
            }
        };
        mLv.setAdapter(mEntitiesBeanCommonAdapter);

        mLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //解决魅族审核索引越界问题
                if (position>=bean.size()){
                    return;
                }
                EntitiesBean entitiesBean = bean.get(position);
                if (entitiesBean.getActivityType() == 1) {
                    WebContainerActivity.startActivity(mActivity, BuildConfig.WebHomeBaseUrl + "/activity/platform-activity/signup/" + entitiesBean.getId());
                } else {
                    WebContainerActivity.startActivity(mActivity, BuildConfig.WebHomeBaseUrl + "/activity/platform-activity/vote/" + entitiesBean.getId());

                }
            }
        });
    }

    @Override
    public void initData() {
//        initPresenter();

    }

    @Override
    protected boolean hasDataInPage() {
        return mEntitiesBeanCommonAdapter!=null&&mEntitiesBeanCommonAdapter.getCount() != 0;
    }

    @Override

    protected boolean needLoadingView() {
        return true;
    }

    @Override
    public void refreshPage() {
        initPresenter();
    }

    @Override
    public void onRefresh() {
        initPresenter();
        mActivityRefresh.setRefreshing(false);
    }


    @Override
    public View getScrollableView() {
        return mLv;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BaseEvent baseEvent) {
        if (baseEvent.type == CommonCode.EventType.TYPE_ADD_LAYOUT) {
            View view = View.inflate(mActivity, R.layout.foot_view, null);
            if (mLv.getFooterViewsCount() <= 0) {
                mLv.addFooterView(view);

                mEntitiesBeanCommonAdapter.notifyDataSetChanged();
                mLv.setAdapter(mEntitiesBeanCommonAdapter);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }


}
