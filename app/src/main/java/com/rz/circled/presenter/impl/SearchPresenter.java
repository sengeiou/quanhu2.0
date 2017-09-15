package com.rz.circled.presenter.impl;

import android.content.Context;
import android.os.Handler;

import com.rz.circled.R;
import com.rz.circled.presenter.GeneralPresenter;
import com.rz.common.cache.preference.EntityCache;
import com.rz.common.constant.CommonCode;
import com.rz.common.constant.Constants;
import com.rz.common.ui.inter.IViewController;
import com.rz.common.utils.NetUtils;
import com.rz.httpapi.api.ApiService;
import com.rz.httpapi.api.BaseCallback;
import com.rz.httpapi.api.CallManager;
import com.rz.httpapi.api.Http;
import com.rz.httpapi.api.ResponseData.ResponseData;
import com.rz.httpapi.bean.CircleDynamic;
import com.rz.httpapi.bean.SearchDataBean;
import com.rz.httpapi.bean.StarListBean;
import com.rz.httpapi.constans.ReturnCode;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Gsm on 2017/9/2.
 */
public class SearchPresenter extends GeneralPresenter {

    /**
     * 搜索类型
     */
    public static final int SEARCH_CONTENT = 1;
    public static final int SEARCH_PERSION = 2;
    public static final int SEARCH_PERSION_CIRCLE = 3;
    public static final int SEARCH_CIRCLE = 4;
    public static final int SEARCH_REWARD = 5;


    /**
     * 搜索内容类型
     */
    public static final String SEARCH_TYPE_ARTICLE = "1000";
    public static final String SEARCH_TYPE_TOPIC = "1001";
    public static final String SEARCH_TYPE_POST = "1002";
    public static final String SEARCH_TYPE_CONTENT = "1003";
    public static final String SEARCH_TYPE_ANSWER = "1004";
    public static final String SEARCH_TYPE_ACTIVITY = "1005";


    private ApiService mUserService;
    private IViewController mView;
    private Context mContext;

//    private int dynamicPos = 0;
//    private int collectPos = 0;
//    private int custPos = 0;


    //每页分页标记
    private int start = 0;

    //记录每页分页标记
    private int record_start = 0;

    //是否没有数据
    private boolean isNoData;

    //处理缓存
    private EntityCache<CircleDynamic> mCirclesCache;
    private EntityCache<StarListBean> mStarListCache;

    private SearchDataBean searchDataBean = new SearchDataBean();

    private List<CircleDynamic> resoueces = new ArrayList<>();
    private List<StarListBean> custInfos = new ArrayList<>();
    private List coterieInfos = new ArrayList();
    private List circleInfos = new ArrayList();
    private List rewards = new ArrayList();

    @Override
    public Object getCacheData() {
        return null;
    }

    @Override
    public void attachView(IViewController view) {
        this.mView = view;
        mUserService = Http.getApiService(ApiService.class);
        mContext = getContext(mView);
    }

    @Override
    public void detachView() {

    }

    /**
     * 搜索内容
     */

    /**
     *
     *  @Field("circleId") String circleId,
        @Field("coterieId") String coterieId,
        @Field("keyWord") String keyWord,
        @Field("limit") int limit,
        @Field("resourceType") String resourceType,
        @Field("searchType") int searchType,
        @Field("start") int start
     */

    public void searchQH(final boolean loadMore, String keyWord, String circleId, String coterieId, String resourceType, final int searchType){
        if (!NetUtils.isNetworkConnected(mContext)) {
            mView.onLoadingStatus(CommonCode.General.WEB_ERROR, mContext.getString(R.string.no_net_work));
            return;
        }
        mView.onLoadingStatus(CommonCode.General.DATA_LOADING);
        if (!loadMore) {
            start = 0;
        } else {
            if (isNoData) {
                start = record_start;
            } else {
                start += Constants.PAGESIZE;
            }
            record_start = start;
        }

        Call<ResponseData<SearchDataBean>> call = mUserService.searchQH(
                null,
                null,
                keyWord,
                5,
                null,
                searchType,
                0);


        CallManager.add(call);
        call.enqueue(new BaseCallback<ResponseData<SearchDataBean>>() {
            @Override
            public void onResponse(Call<ResponseData<SearchDataBean>> call, Response<ResponseData<SearchDataBean>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {

                    ResponseData<SearchDataBean> res = response.body();
                    if (res.getRet() == ReturnCode.SUCCESS) {
                        SearchDataBean model = (SearchDataBean) res.getData();

                        List dataList = null;
                        if(searchType == 1){
                            dataList = model.getResoueces();
                        }else if(searchType == 2){
                            dataList = model.getCustInfos();
                        }else if(searchType == 3){
                            dataList = model.getCoterieInfos();
                        }else if(searchType == 4){
                            dataList = model.getCircleInfos();
                        }else if(searchType == 5){
                            dataList = model.getOffers();
                        }

                        if (null != dataList && !dataList.isEmpty()) {
                            isNoData = false;
                            mView.updateViewWithLoadMore(dataList, loadMore);
                            mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS);
                        } else {
                            mView.updateViewWithLoadMore(dataList, loadMore);
                            mView.onLoadingStatus(CommonCode.General.DATA_EMPTY);
                            isNoData = true;
                        }
                        return;
                    } else {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mView.onLoadingStatus(CommonCode.General.ERROR_DATA);
                            }
                        }, 2000);
                        isNoData = true;
                        return;
                    }
                }
                mView.onLoadingStatus(CommonCode.General.ERROR_DATA);
                isNoData = true;

            }

            @Override
            public void onFailure(Call<ResponseData<SearchDataBean>> call, Throwable t) {
                super.onFailure(call, t);
                //发送验证码失败
                mView.onLoadingStatus(CommonCode.General.LOAD_ERROR);
            }
        });
    }

}
