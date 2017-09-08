package com.rz.circled.presenter.impl;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.rz.circled.R;
import com.rz.circled.presenter.GeneralPresenter;
import com.rz.common.cache.preference.EntityCache;
import com.rz.common.cache.preference.Session;
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

    private int dynamicPos = 0;
    private int collectPos = 0;
    private int custPos = 0;

    //处理缓存
    private EntityCache<CircleDynamic> mCirclesCache;
    private List<CircleDynamic> currentData = new ArrayList<>();

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

    public void searchQH(final boolean loadMore, String keyWord, String circleId, String coterieId, String resourceType, int searchType){
        if (!NetUtils.isNetworkConnected(mContext)) {
            mView.onLoadingStatus(CommonCode.General.WEB_ERROR, mContext.getString(R.string.no_net_work));
        }
        mView.onLoadingStatus(CommonCode.General.DATA_LOADING, mContext.getString(R.string.check_loading));
        Call<ResponseData<List<CircleDynamic>>> call = mUserService.searchQH(
                circleId,
                coterieId,
                keyWord,
                10,
                resourceType,
                searchType,
                Constants.PAGESIZE);
        CallManager.add(call);
        call.enqueue(new BaseCallback<ResponseData<List<CircleDynamic>>>() {
            @Override
            public void onResponse(Call<ResponseData<List<CircleDynamic>>> call, Response<ResponseData<List<CircleDynamic>>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    ResponseData res = response.body();
                    if (!loadMore) {
                        dynamicPos = 0;
                    }
//                    dynamicPos += Constants.PAGESIZE;
                    dynamicPos += 50;
                    if (res.getRet() == ReturnCode.SUCCESS) {
                        List<CircleDynamic> model = (List<CircleDynamic>) res.getData();
                        if (null != model && model.size() != 0) {
                            //发送成功
                            mView.updateViewWithLoadMore(model, loadMore);
                            mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS);
                        } else {
                            mView.onLoadingStatus(CommonCode.General.DATA_EMPTY);
                        }
                        try {
                            if (loadMore) {
                                currentData.addAll(model);
                            } else {
                                currentData = new ArrayList<CircleDynamic>(model);
                            }
                            if (!loadMore) {
                                mCirclesCache.putListEntity(model);
                            } else {
                                mCirclesCache.putListEntity(currentData);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d("test", "cacheData failed " + e.getMessage());
                        }
                        return;
                    } else if (res.getRet() == ReturnCode.FAIL_REMIND_1) {
                        //发送失败
                        mView.onLoadingStatus(CommonCode.General.LOAD_ERROR);
                        return;
                    }
                }
                mView.onLoadingStatus(CommonCode.General.LOAD_ERROR, mContext.getString(R.string.load_fail));
            }

            @Override
            public void onFailure(Call<ResponseData<List<CircleDynamic>>> call, Throwable t) {
                super.onFailure(call, t);
                //发送验证码失败
                mView.onLoadingStatus(CommonCode.General.LOAD_ERROR);
            }
        });
    }

//    /**
//     *  搜索用户
//     */
//    public void searchPerson(String keyWord) {
//
//    }
//
//    /**
//     * 搜索私圈
//     */
//    public void searchPrivateCircle(String keyWord){
//
//
//    }
//
//    /**
//     * 搜索圈子
//     */
//    public void searchCircle(String keyWord){
//
//
//    }
//
//    /**
//     *  搜索悬赏
//     */
//    public void searchReward(String keyWord){
//
//
//    }


}
