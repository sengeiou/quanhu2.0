package com.rz.circled.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
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
import com.rz.circled.R;
import com.rz.circled.http.ApiYylService;
import com.rz.circled.widget.CommonAdapter;
import com.rz.circled.widget.GlideCenterRoundImage;
import com.rz.circled.widget.MListView;
import com.rz.circled.widget.ViewHolder;
import com.rz.common.constant.CommonCode;
import com.rz.common.constant.IntentKey;
import com.rz.common.ui.fragment.BaseFragment;
import com.rz.httpapi.api.Http;
import com.rz.httpapi.api.ResponseData.ResponseData;
import com.rz.httpapi.bean.ActivityBean;
import com.rz.httpapi.bean.EntitiesBean;
import com.rz.httpapi.constans.ReturnCode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
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
        return inflater.inflate(R.layout.activity_article, null);
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

        Http.getApiService(ApiYylService.class)
                .getMineActivityList(pageNo, 20, userid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ResponseData<ActivityBean>>() {
                    @Override
                    public void call(ResponseData<ActivityBean> res) {
                        List<EntitiesBean> entities = res.getData().entities;
                        if (res.getRet() == ReturnCode.SUCCESS) {

                            if (null != entities && !entities.isEmpty()) {
                                isNoData = false;
                                onLoadingStatus(CommonCode.General.DATA_SUCCESS);

                                bean.addAll(entities);
                                mEntitiesBeanCommonAdapter.notifyDataSetChanged();

                                pageNo++;
                            } else {
                                if (loadMore == false) {
                                    onLoadingStatus(CommonCode.General.DATA_EMPTY);
                                } else {
                                if(loadMore == false){
                                    onLoadingStatus(CommonCode.General.DATA_EMPTY,mActivity.getString(R.string.mine_activity_txt));
                                }else{
                                    onLoadingStatus(CommonCode.General.DATA_LACK);
                                }
                                isNoData = true;
                            }
                            return;
                        } else {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    onLoadingStatus(CommonCode.General.ERROR_DATA);
                                }
                            }, 2000);
                            isNoData = true;
                            return;
                        }
                    }
                });

    }


    @Override
    public void initView() {

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

            }
        });
    }

    @Override
    public void initData() {
        initPresenter();

    }

    @Override
    protected boolean hasDataInPage() {
        return mEntitiesBeanCommonAdapter.getCount() != 0;
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
}
