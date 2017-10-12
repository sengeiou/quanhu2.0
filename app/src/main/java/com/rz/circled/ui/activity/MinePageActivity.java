package com.rz.circled.ui.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rz.circled.R;
import com.rz.circled.http.ApiYylService;
import com.rz.circled.widget.CommonAdapter;
import com.rz.circled.widget.GlideCenterRoundImage;
import com.rz.circled.widget.SwipyRefreshLayoutBanner;
import com.rz.circled.widget.ViewHolder;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.CommonCode;
import com.rz.common.constant.Constants;
import com.rz.common.swiperefresh.SwipyRefreshLayout;
import com.rz.common.swiperefresh.SwipyRefreshLayoutDirection;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.utils.NetUtils;
import com.rz.httpapi.api.Http;
import com.rz.httpapi.api.ResponseData.ResponseData;
import com.rz.httpapi.bean.ActivityBean;
import com.rz.httpapi.bean.EntitiesBean;
import com.rz.httpapi.constans.ReturnCode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/9/16/016.
 */

public class MinePageActivity extends BaseActivity implements SwipyRefreshLayout.OnRefreshListener {
    List<EntitiesBean> bean = new ArrayList<>();
    @BindView(R.id.lv)
    ListView mLv;
    int pageNo = 1;
    @BindView(R.id.activity_refresh)
    SwipyRefreshLayoutBanner mActivityRefresh;
    private boolean loadMore=false;
    private CommonAdapter<EntitiesBean> mEntitiesBeanCommonAdapter;

    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.mine_activity, null);
    }

    @Override
    public void initPresenter() {
        if (!NetUtils.isNetworkConnected(mContext)) {
            onLoadingStatus(CommonCode.General.UN_NETWORK);
            return;
        }
        Http.getApiService(ApiYylService.class)
                .getMineActivityList(loadMore?pageNo:1, 20, Session.getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseData<ActivityBean>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResponseData<ActivityBean> res) {
                        if (res.getRet() == ReturnCode.SUCCESS) {
                            List<EntitiesBean> entities = res.getData().entities;
                            if (!entities.isEmpty()) {
                                if (!loadMore){
                                    pageNo=1;
                                    bean.clear();
                                }
                                pageNo++;
                                bean.addAll(entities);
                                mEntitiesBeanCommonAdapter.notifyDataSetChanged();
                            } else {
                                onLoadingStatus(CommonCode.General.DATA_EMPTY,loadMore?"没有更多的活动":"");
                            }
                        } else {
                            onLoadingStatus(CommonCode.General.ERROR_DATA);
                        }

                    }
                });

    }

    @Override
    public void initView() {
        mActivityRefresh.setColorSchemeColors(Constants.COLOR_SCHEMES);
        mActivityRefresh.setDirection(SwipyRefreshLayoutDirection.BOTH);
        mActivityRefresh.setOnRefreshListener(this);
        setTitleText("活动");
        mEntitiesBeanCommonAdapter = new CommonAdapter<EntitiesBean>(mContext, bean, R.layout.activity_item) {
            @Override
            public void convert(ViewHolder helper, EntitiesBean item) {
                ((TextView) helper.getView(R.id.activity_title)).setText(item.getTitle());
                ImageView iv = (ImageView) helper.getView(R.id.iv_activity_icon);
                Glide.with(mContext).load(item.getCoverPlan()).placeholder(R.drawable.ic_circle_img1).transform(new GlideCenterRoundImage(mContext,10)).into(iv);
            }
        };
        mLv.setAdapter(mEntitiesBeanCommonAdapter);

    }

    @Override
    public void initData() {


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
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
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        loadMore=direction==SwipyRefreshLayoutDirection.TOP?false:true;
        initPresenter();
        mActivityRefresh.setRefreshing(false);
    }
}
