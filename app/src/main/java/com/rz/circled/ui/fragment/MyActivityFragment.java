package com.rz.circled.ui.fragment;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.rz.circled.R;
import com.rz.circled.http.ApiYylService;
import com.rz.circled.presenter.impl.PersonInfoPresenter;
import com.rz.circled.widget.CommonAdapter;
import com.rz.circled.widget.MListView;
import com.rz.circled.widget.ViewHolder;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.CommonCode;
import com.rz.common.ui.fragment.BaseFragment;
import com.rz.httpapi.api.Http;
import com.rz.httpapi.api.ResponseData.ResponseData;
import com.rz.httpapi.bean.ActivityBean;
import com.rz.httpapi.bean.CircleDynamic;
import com.rz.httpapi.bean.EntitiesBean;
import com.rz.httpapi.constans.ReturnCode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/9/14 0014.
 */

public class MyActivityFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

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


    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_article, null);
    }


    public static MyActivityFragment newInstance() {
        MyActivityFragment frg = new MyActivityFragment();
        return frg;
    }

    @Override
    public void initPresenter() {

        getData(false);
    }

    private void getData(final boolean loadMore){

        Http.getApiService(ApiYylService.class)
                .getMineActivityList(pageNo, 20, Session.getUserId())
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
//                        if (res.getRet() == ReturnCode.SUCCESS) {
//                            pageNo++;
//                            List<EntitiesBean> entities = res.getData().entities;
//                            if(entities.size() == 0 || entities == null){
//                                onLoadingStatus(CommonCode.General.DATA_SUCCESS,"没有你您要的内容");
//                            }else{
//
//                            }
//
//                            bean.addAll(entities);
//                            mEntitiesBeanCommonAdapter.notifyDataSetChanged();
//
//                        }

                        List<EntitiesBean> entities = res.getData().entities;
                        if (res.getRet() == ReturnCode.SUCCESS) {
                            List<CircleDynamic> modelList = (List<CircleDynamic>) res.getData();

                            if (null != modelList && !modelList.isEmpty()) {
                                isNoData = false;
                                onLoadingStatus(CommonCode.General.DATA_SUCCESS);

                                bean.addAll(entities);
                                mEntitiesBeanCommonAdapter.notifyDataSetChanged();

                                pageNo++;
                            } else {
                                if(loadMore == false){
                                    onLoadingStatus(CommonCode.General.DATA_EMPTY);
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
                Glide.with(mContext).load(item.getCoverPlan()).into(iv);
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



}
