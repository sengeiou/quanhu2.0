package com.rz.circled.presenter.impl;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;

import com.rz.circled.R;
import com.rz.circled.presenter.GeneralPresenter;
import com.rz.common.application.BaseApplication;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.CommonCode;
import com.rz.common.constant.Constants;
import com.rz.common.event.BaseEvent;
import com.rz.common.ui.inter.IViewController;
import com.rz.common.utils.NetUtils;
import com.rz.common.widget.svp.SVProgressHUD;
import com.rz.httpapi.api.ApiService;
import com.rz.httpapi.api.BaseCallback;
import com.rz.httpapi.api.CallManager;
import com.rz.httpapi.api.Http;
import com.rz.httpapi.api.ResponseData.ResponseData;
import com.rz.httpapi.bean.BuyingBean;
import com.rz.httpapi.bean.CircleDynamic;
import com.rz.httpapi.bean.MineRewardBean;
import com.rz.httpapi.bean.MyBuyingModel;
import com.rz.httpapi.bean.MyRewardBean;
import com.rz.httpapi.bean.RewardStatBean;
import com.rz.httpapi.bean.SearchDataBean;
import com.rz.httpapi.constans.ReturnCode;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by xiayumo on 16/8/18.
 */
public class PersonInfoPresenter extends GeneralPresenter {

    private ApiService mUserService;
    private IViewController mView;
    private Context mContext;


    //每页分页标记
    private int start = 0;

    //记录每页分页标记
    private int record_start = 0;

    //是否没有数据
    private boolean isNoData;

    @Override
    public void attachView(IViewController view) {
        this.mView = view;
        mUserService = Http.getApiService(ApiService.class);
        mContext = getContext(mView);
    }

    @Override
    public Object getCacheData() {
        return null;
    }

    @Override
    public void loadData() {
        super.loadData();
    }

    @Override
    public void detachView() {

    }

    public void initForLocation(Activity activity) {
        mUserService = Http.getApiService(ApiService.class);
        mContext = activity;
    }

    /**
     * 保存个人信息
     *
     * @param id
     * @param paramasType
     * @param paramas
     * @param cityCode
     */
    public void savePersonInfo(String id, final String paramasType, final String paramas, String cityCode) {

        if (!NetUtils.isNetworkConnected(mContext)) {
            if (mView != null)
                mView.onLoadingStatus(CommonCode.General.UN_NETWORK, mContext.getString(R.string.status_un_network));
            return;
        }

        Call<ResponseData> call = null;

        switch (paramasType) {
            case "headImg":
                call = mUserService.editSaveHeadImg(1050, id, paramas);
                break;
            case "nickName":
                call = mUserService.editSaveNickName(1050, id, paramas);
                break;
            case "sex":
                call = mUserService.editSaveSex(1050, id, paramas);
                break;
            case "signature":
                call = mUserService.editSaveSignature(1050, id, paramas);
                break;
            case "location":
                call = mUserService.editSaveAress(1050, id, paramas, cityCode);
                break;
            case "desc":
                call = mUserService.editSaveDesc(1050, id, paramas);
                break;
            default:
                break;

        }
        CallManager.add(call);
        call.enqueue(new BaseCallback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    ResponseData res = response.body();
                    if (res.getRet() == ReturnCode.SUCCESS) {
                        if (mView != null)
                            mView.updateView(paramas);
                        if (paramasType.equals("location"))
                            Session.setUser_area(paramas);

                    } else if (res.getRet() == ReturnCode.FAIL_REMIND_1) {
                        if (mView != null)
                            SVProgressHUD.showErrorWithStatus(mContext, res.getMsg());

                    }
                } else {
                    if (mView != null)
                        mView.onLoadingStatus(CommonCode.General.LOAD_ERROR, mContext.getString(R.string.modify_fail));
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                super.onFailure(call, t);
                if (mView != null)
                    mView.onLoadingStatus(CommonCode.General.LOAD_ERROR, mContext.getString(R.string.modify_fail));
            }
        });
    }

    public void report(String sourceId, String custId, int infoType, String content) {
        if (!NetUtils.isNetworkConnected(BaseApplication.getContext())) {
            return;
        }
        Call<ResponseData> call = mUserService.report(custId, infoType, sourceId, content);
        CallManager.add(call);
        call.enqueue(new BaseCallback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    ResponseData res = response.body();
                    if (res.getRet() == ReturnCode.SUCCESS) {
                        mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS, mContext.getString(R.string.refunds_success));
                        mView.updateView(0);
                    } else {
                        mView.onLoadingStatus(CommonCode.General.LOAD_ERROR, mContext.getString(R.string.refunds_fail) + ":" + res.getMsg());
                    }
                } else {
                    mView.onLoadingStatus(CommonCode.General.LOAD_ERROR, mContext.getString(R.string.refunds_fail));
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                super.onFailure(call, t);
                mView.onLoadingStatus(CommonCode.General.LOAD_ERROR, mContext.getString(R.string.refunds_fail));
            }
        });
    }

    /**
     * 获取我的文章
     */

    public void getArticle(final boolean loadMore, String custId, String resourceType){

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

        Call<ResponseData<List<CircleDynamic>>> call = mUserService.getMyResource(
                custId,
                Constants.PAGESIZE,
                resourceType,
                start);


        CallManager.add(call);
        call.enqueue(new BaseCallback<ResponseData<List<CircleDynamic>>>() {
            @Override
            public void onResponse(Call<ResponseData<List<CircleDynamic>>> call, Response<ResponseData<List<CircleDynamic>>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {

                    ResponseData<List<CircleDynamic>> res = response.body();
                    if (res.getRet() == ReturnCode.SUCCESS) {
                        List<CircleDynamic> modelList = (List<CircleDynamic>) res.getData();

                        if (null != modelList && !modelList.isEmpty()) {
                            isNoData = false;
                            mView.updateViewWithLoadMore(modelList, loadMore);
                            mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS);
                        } else {
                            if(loadMore == false){
                                mView.onLoadingStatus(CommonCode.General.DATA_EMPTY);
                            }else{
                                mView.onLoadingStatus(CommonCode.General.DATA_LACK);
                            }
                            mView.updateViewWithLoadMore(modelList, loadMore);
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
            public void onFailure(Call<ResponseData<List<CircleDynamic>>> call, Throwable t) {
                super.onFailure(call, t);
                //发送验证码失败
                mView.onLoadingStatus(CommonCode.General.LOAD_ERROR);
            }
        });
    }


    /**
     * 获取我的悬赏
     */

    public void getMyreward(final boolean loadMore, String custId, int type) {

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

        Call<ResponseData<List<MyRewardBean>>> call = mUserService.getRewardNoList(
                custId,
                Constants.PAGESIZE,
                type);

        CallManager.add(call);
        call.enqueue(new BaseCallback<ResponseData<List<MyRewardBean>>>() {
            @Override
            public void onResponse(Call<ResponseData<List<MyRewardBean>>> call, Response<ResponseData<List<MyRewardBean>>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {

                    ResponseData<List<MyRewardBean>> res = response.body();
                    if (res.getRet() == ReturnCode.SUCCESS) {
                        List<MyRewardBean> modelList = (List<MyRewardBean>) res.getData();

                        if (null != modelList && !modelList.isEmpty()) {
                            isNoData = false;
                            mView.updateViewWithLoadMore(modelList, loadMore);
                            mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS);
                        } else {
                            if (loadMore == false) {
                                mView.onLoadingStatus(CommonCode.General.DATA_EMPTY);
                            } else {
                                mView.onLoadingStatus(CommonCode.General.DATA_LACK);
                            }
                            mView.updateViewWithLoadMore(modelList, loadMore);
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
            public void onFailure(Call<ResponseData<List<MyRewardBean>>> call, Throwable t) {
                super.onFailure(call, t);
                //发送验证码失败
                mView.onLoadingStatus(CommonCode.General.LOAD_ERROR);
            }
        });
    }

    /**
     * 获取我的购买
     */

    public void getMybuy(final boolean loadMore, String custId){

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

        Call<ResponseData<BuyingBean>> call = mUserService.getMyBuying(
                custId,
                1,
                10);


        CallManager.add(call);
        call.enqueue(new BaseCallback<ResponseData<BuyingBean>>() {
            @Override
            public void onResponse(Call<ResponseData<BuyingBean>> call, Response<ResponseData<BuyingBean>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {

                    ResponseData<BuyingBean> res = response.body();
                    if (res.getRet() == ReturnCode.SUCCESS) {
                        List<MyBuyingModel> modelList = (List<MyBuyingModel>) res.getData().getList();

                        if (null != modelList && !modelList.isEmpty()) {
                            isNoData = false;
                            mView.updateViewWithLoadMore(modelList, loadMore);
                            mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS);
                        } else {
                            if(loadMore == false){
                                mView.onLoadingStatus(CommonCode.General.DATA_EMPTY);
                            }else{
                                mView.onLoadingStatus(CommonCode.General.DATA_LACK);
                            }
                            mView.updateViewWithLoadMore(modelList, loadMore);
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
            public void onFailure(Call<ResponseData<BuyingBean>> call, Throwable t) {
                super.onFailure(call, t);
                //发送验证码失败
                mView.onLoadingStatus(CommonCode.General.LOAD_ERROR);
            }
        });
    }

    /**
     * 获取我的打赏作品
     */

    public void getMyReward(final boolean loadMore, String custId,int isReward,String rewardId){

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

        Call<ResponseData<List<MineRewardBean>>> call = mUserService.getMyReward(
                custId,
                isReward,
                10,
                rewardId,
                0);

        CallManager.add(call);
        call.enqueue(new BaseCallback<ResponseData<List<MineRewardBean>>>() {
            @Override
            public void onResponse(Call<ResponseData<List<MineRewardBean>>> call, Response<ResponseData<List<MineRewardBean>>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {

                    ResponseData<List<MineRewardBean>> res = response.body();
                    if (res.getRet() == ReturnCode.SUCCESS) {
                        List<MineRewardBean> modelList = (List<MineRewardBean>) res.getData();

                        if (null != modelList && !modelList.isEmpty()) {
                            isNoData = false;
                            mView.updateViewWithLoadMore(modelList, loadMore);
                            mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS);
                        } else {
                            if(loadMore == false){
                                mView.onLoadingStatus(CommonCode.General.DATA_EMPTY);
                            }else{
                                mView.onLoadingStatus(CommonCode.General.DATA_LACK);
                            }
                            mView.updateViewWithLoadMore(modelList, loadMore);
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
            public void onFailure(Call<ResponseData<List<MineRewardBean>>> call, Throwable t) {
                super.onFailure(call, t);
                //发送验证码失败
                mView.onLoadingStatus(CommonCode.General.LOAD_ERROR);
            }
        });
    }

    public void getMyRewardStat(String custId) {
        if (!NetUtils.isNetworkConnected(BaseApplication.getContext())) {
            return;
        }
        Call<ResponseData<RewardStatBean>> call = mUserService.getMyRewardStat(custId);
        CallManager.add(call);
        call.enqueue(new BaseCallback<ResponseData<RewardStatBean>>() {
            @Override
            public void onResponse(Call<ResponseData<RewardStatBean>> call, Response<ResponseData<RewardStatBean>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    ResponseData<RewardStatBean> res = response.body();
                    if (res.getRet() == ReturnCode.SUCCESS) {
                        RewardStatBean model = res.getData();
                        mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS, mContext.getString(R.string.refunds_success));
                        mView.updateView(model);
                    } else {
                        mView.onLoadingStatus(CommonCode.General.DATA_EMPTY);
                        mView.updateView(CommonCode.General.DATA_EMPTY);
                    }
                } else {
                    mView.onLoadingStatus(CommonCode.General.LOAD_ERROR);
                }
            }

            @Override
            public void onFailure(Call<ResponseData<RewardStatBean>> call, Throwable t) {
                super.onFailure(call, t);
                mView.onLoadingStatus(CommonCode.General.LOAD_ERROR);
            }
        });
    }

}
