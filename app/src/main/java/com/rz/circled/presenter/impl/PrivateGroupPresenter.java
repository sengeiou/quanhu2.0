package com.rz.circled.presenter.impl;

import android.content.Context;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;

import com.rz.circled.R;
import com.rz.circled.dialog.ApplyForGroupSuccessDialog;
import com.rz.circled.presenter.GeneralPresenter;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.CommonCode;
import com.rz.common.ui.inter.IViewController;
import com.rz.common.utils.NetUtils;
import com.rz.common.utils.Utility;
import com.rz.common.widget.svp.SVProgressHUD;
import com.rz.httpapi.api.ApiPGService;
import com.rz.httpapi.api.ApiService;
import com.rz.httpapi.api.BaseCallback;
import com.rz.httpapi.api.HandleRetCode;
import com.rz.httpapi.api.Http;
import com.rz.httpapi.api.ResponseData.ResponseData;
import com.rz.httpapi.bean.CircleBelongBean;
import com.rz.httpapi.bean.CircleEntrModle;
import com.rz.httpapi.bean.GroupBannerBean;
import com.rz.httpapi.bean.LoginWayModel;
import com.rz.httpapi.bean.MyLevelAcountBean;
import com.rz.httpapi.bean.MyLevelBean;
import com.rz.httpapi.bean.PrivateGroupBean;
import com.rz.httpapi.bean.PrivateGroupListBean;
import com.rz.httpapi.bean.PrivateGroupResourceBean;
import com.rz.httpapi.constans.ReturnCode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.rz.common.constant.CommonCode.Constant.PAGE_SIZE;

/**
 * Created by Gsm on 2017/9/19.
 */
public class PrivateGroupPresenter extends GeneralPresenter {


    @Override
    public Object getCacheData() {
        return null;
    }

    private Context mContext;
    private IViewController mView;
    private ApiPGService mApiService;

    @Override
    public void attachView(IViewController view) {
        mView = view;
        mContext = getContext(mView);
        mApiService = Http.getApiService(ApiPGService.class);
    }

    @Override
    public void detachView() {

    }

    /**
     * 全部私圈列表
     *
     * @param pageNo
     * @param loadMore
     */
    public void privateGroupList(int pageNo, final boolean loadMore) {
        if (!NetUtils.isNetworkConnected(mContext)) {
            mView.onLoadingStatus(CommonCode.General.UN_NETWORK, mContext.getString(R.string.no_net_work));
            return;
        }
        Call<ResponseData<List<PrivateGroupBean>>> call = mApiService.privateGroupList(pageNo, PAGE_SIZE);
        call.enqueue(new BaseCallback<ResponseData<List<PrivateGroupBean>>>() {
            @Override
            public void onResponse(Call<ResponseData<List<PrivateGroupBean>>> call, Response<ResponseData<List<PrivateGroupBean>>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    ResponseData<List<PrivateGroupBean>> responseData = response.body();
                    if (responseData.isSuccessful()) {
                        List<PrivateGroupBean> data = response.body().getData();
                        if (null != data && !data.isEmpty()) {
                            mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS, "");
                            mView.updateViewWithLoadMore(data, loadMore);
                        } else {
                            mView.onLoadingStatus(CommonCode.General.DATA_EMPTY, "");
                        }
                    } else {
                        mView.onLoadingStatus(CommonCode.General.ERROR_DATA, response.body().getMsg());
                    }
                } else {
                    mView.onLoadingStatus(CommonCode.General.ERROR_DATA, mContext.getString(R.string.load_fail));
                }
            }

            @Override
            public void onFailure(Call<ResponseData<List<PrivateGroupBean>>> call, Throwable t) {
                super.onFailure(call, t);
                mView.onLoadingStatus(CommonCode.General.ERROR_DATA, mContext.getString(R.string.load_fail));
            }
        });
    }

    /**
     * 创建私圈
     *
     * @param circleId
     * @param icon
     * @param intro
     * @param joinCheck
     * @param joinFee
     * @param name
     * @param ownerId
     * @param ownerIntro
     * @param ownerName
     */
    public void privateGroupCreate(String circleId, String icon, String intro, int joinCheck, int joinFee, String name, String ownerId, String ownerIntro, String ownerName) {
        if (!NetUtils.isNetworkConnected(mContext)) {
            mView.onLoadingStatus(CommonCode.General.UN_NETWORK, mContext.getString(R.string.no_net_work));
            return;
        }
        Call<ResponseData> call = mApiService.privateGroupCreate(circleId, icon, intro, joinCheck, joinFee, name, ownerId, ownerIntro, ownerName);
        call.enqueue(new BaseCallback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    if (response.body().isSuccessful()) {
                        mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS, "");
                        mView.updateView(CommonCode.General.DATA_SUCCESS);
                    } else {
                        mView.onLoadingStatus(CommonCode.General.ERROR_DATA, response.body().getMsg());
                    }
                } else {
                    mView.onLoadingStatus(CommonCode.General.ERROR_DATA, mContext.getString(R.string.load_fail));
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                super.onFailure(call, t);
                mView.onLoadingStatus(CommonCode.General.ERROR_DATA, mContext.getString(R.string.load_fail));
            }
        });
    }

    /**
     * 私圈广告
     *
     * @param bannerType
     */
    public void privateGroupBanner(String bannerType) {
        if (!NetUtils.isNetworkConnected(mContext)) {
            mView.onLoadingStatus(CommonCode.General.UN_NETWORK, mContext.getString(R.string.no_net_work));
            return;
        }
        Call<ResponseData<List<GroupBannerBean>>> call = mApiService.privateGroupBanner(bannerType);
        call.enqueue(new BaseCallback<ResponseData<List<GroupBannerBean>>>() {
            @Override
            public void onResponse(Call<ResponseData<List<GroupBannerBean>>> call, Response<ResponseData<List<GroupBannerBean>>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful() && response.body().isSuccessful()) {
                    List<GroupBannerBean> data = response.body().getData();
                    if (null != data && !data.isEmpty()) mView.updateView(data);
                }
            }
        });
    }

    /**
     * 私圈精华
     *
     * @param start
     * @param loadMore
     */
    public void privateGroupEssence(int start, final boolean loadMore) {
        if (!NetUtils.isNetworkConnected(mContext)) {
            mView.onLoadingStatus(CommonCode.General.UN_NETWORK, mContext.getString(R.string.no_net_work));
            return;
        }
        Call<ResponseData<List<PrivateGroupResourceBean>>> call = mApiService.privateGroupEssence(start, PAGE_SIZE);
        call.enqueue(new BaseCallback<ResponseData<List<PrivateGroupResourceBean>>>() {
            @Override
            public void onResponse(Call<ResponseData<List<PrivateGroupResourceBean>>> call, Response<ResponseData<List<PrivateGroupResourceBean>>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    if (!response.body().isSuccessful()) {
                        mView.onLoadingStatus(CommonCode.General.ERROR_DATA, response.body().getMsg());
                    } else {
                        List<PrivateGroupResourceBean> data = response.body().getData();
                        if (null != data && !data.isEmpty()) {
                            mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS, "");
                            mView.updateViewWithLoadMore(data, loadMore);
                        } else {
                            mView.onLoadingStatus(CommonCode.General.DATA_EMPTY, "");
                        }
                    }
                } else {
                    mView.onLoadingStatus(CommonCode.General.ERROR_DATA, mContext.getString(R.string.load_fail));
                }
            }

            @Override
            public void onFailure(Call<ResponseData<List<PrivateGroupResourceBean>>> call, Throwable t) {
                super.onFailure(call, t);
                mView.onLoadingStatus(CommonCode.General.ERROR_DATA, mContext.getString(R.string.load_fail));
            }
        });
    }

    /**
     * 私圈推荐
     *
     * @param custId
     */
    public void privateGroupRecommend(String custId) {
        if (!NetUtils.isNetworkConnected(mContext)) {
            mView.onLoadingStatus(CommonCode.General.UN_NETWORK, mContext.getString(R.string.no_net_work));
            return;
        }
        Call<ResponseData<PrivateGroupListBean>> call = mApiService.privateGroupRecommend(custId);
        call.enqueue(new BaseCallback<ResponseData<PrivateGroupListBean>>() {
            @Override
            public void onResponse(Call<ResponseData<PrivateGroupListBean>> call, Response<ResponseData<PrivateGroupListBean>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    if (!response.body().isSuccessful()) {
                        mView.onLoadingStatus(CommonCode.General.ERROR_DATA, response.body().getMsg());
                    } else {
                        PrivateGroupListBean data = response.body().getData();
                        if (null != data && data.getList() != null && !data.getList().isEmpty()) {
                            mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS, "");
                            mView.updateView(data);
                        } else {
                            mView.onLoadingStatus(CommonCode.General.DATA_EMPTY, "");
                        }
                    }
                } else {
                    mView.onLoadingStatus(CommonCode.General.ERROR_DATA, mContext.getString(R.string.load_fail));
                }
            }

            @Override
            public void onFailure(Call<ResponseData<PrivateGroupListBean>> call, Throwable t) {
                super.onFailure(call, t);
                mView.onLoadingStatus(CommonCode.General.ERROR_DATA, mContext.getString(R.string.load_fail));
            }
        });
    }

    /**
     * 我加入的私圈
     *
     * @param custId
     * @param pageNo
     * @param loadMore
     */
    public void privateGroupMyselfJoin(String custId, int pageNo, final boolean loadMore) {
        if (!NetUtils.isNetworkConnected(mContext)) {
            mView.onLoadingStatus(CommonCode.General.UN_NETWORK, mContext.getString(R.string.no_net_work));
            return;
        }
        Call<ResponseData<PrivateGroupListBean>> call = mApiService.privateGroupMyselfJoin(custId, pageNo, PAGE_SIZE);
        call.enqueue(new BaseCallback<ResponseData<PrivateGroupListBean>>() {
            @Override
            public void onResponse(Call<ResponseData<PrivateGroupListBean>> call, Response<ResponseData<PrivateGroupListBean>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    ResponseData<PrivateGroupListBean> responseData = response.body();
                    if (responseData.isSuccessful()) {
                        PrivateGroupListBean data = response.body().getData();
                        if (null != data && data.getList() != null && !data.getList().isEmpty()) {
                            mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS, "");
                            mView.updateViewWithLoadMore(data, loadMore);
                        } else {
                            mView.onLoadingStatus(CommonCode.General.DATA_EMPTY, mContext.getString(R.string.private_group_no_join));
                        }
                    } else {
                        mView.onLoadingStatus(CommonCode.General.ERROR_DATA, response.body().getMsg());
                    }
                } else {
                    mView.onLoadingStatus(CommonCode.General.ERROR_DATA, mContext.getString(R.string.load_fail));
                }
            }

            @Override
            public void onFailure(Call<ResponseData<PrivateGroupListBean>> call, Throwable t) {
                super.onFailure(call, t);
                mView.onLoadingStatus(CommonCode.General.ERROR_DATA, mContext.getString(R.string.load_fail));
            }
        });
    }

    /**
     * 我创建的私圈
     *
     * @param custId
     * @param pageNo
     * @param loadMore
     */
    public void privateGroupMyselfCreate(String custId, int pageNo, final boolean loadMore) {
        if (!NetUtils.isNetworkConnected(mContext)) {
            mView.onLoadingStatus(CommonCode.General.UN_NETWORK, mContext.getString(R.string.no_net_work));
            return;
        }
        Call<ResponseData<PrivateGroupListBean>> call = mApiService.privateGroupMyselfCreate(custId, pageNo, PAGE_SIZE);
        call.enqueue(new BaseCallback<ResponseData<PrivateGroupListBean>>() {
            @Override
            public void onResponse(Call<ResponseData<PrivateGroupListBean>> call, Response<ResponseData<PrivateGroupListBean>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    ResponseData<PrivateGroupListBean> responseData = response.body();
                    if (responseData.isSuccessful()) {
                        PrivateGroupListBean data = response.body().getData();
                        if (null != data && data.getList() != null && !data.getList().isEmpty()) {
                            mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS, "");
                            mView.updateViewWithLoadMore(data, loadMore);
                        } else {
                            mView.onLoadingStatus(CommonCode.General.DATA_EMPTY, mContext.getString(R.string.private_group_no_create));
                        }
                    } else {
                        mView.onLoadingStatus(CommonCode.General.ERROR_DATA, response.body().getMsg());
                    }
                } else {
                    mView.onLoadingStatus(CommonCode.General.ERROR_DATA, mContext.getString(R.string.load_fail));
                }
            }

            @Override
            public void onFailure(Call<ResponseData<PrivateGroupListBean>> call, Throwable t) {
                super.onFailure(call, t);
                mView.onLoadingStatus(CommonCode.General.ERROR_DATA, mContext.getString(R.string.load_fail));
            }
        });
    }

    /**
     * 私圈所属圈子
     *
     * @param custId
     */
    public void privateGroupBelong(String custId) {
        if (!NetUtils.isNetworkConnected(mContext)) {
            mView.onLoadingStatus(CommonCode.General.UN_NETWORK, mContext.getString(R.string.no_net_work));
            return;
        }
        Call<ResponseData<CircleBelongBean>> call = mApiService.privateGroupBelong(custId);
        call.enqueue(new BaseCallback<ResponseData<CircleBelongBean>>() {
            @Override
            public void onResponse(Call<ResponseData<CircleBelongBean>> call, Response<ResponseData<CircleBelongBean>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    if (!response.body().isSuccessful()) {
                        mView.onLoadingStatus(CommonCode.General.ERROR_DATA, response.body().getMsg());
                    } else {
                        CircleBelongBean data = response.body().getData();
                        if (data != null) {
                            for (String circleId : data.getCircleIdList()) {
                                Iterator<CircleEntrModle> sListIterator = data.getCircleList().iterator();
                                while (sListIterator.hasNext()) {
                                    if (TextUtils.equals(circleId, sListIterator.next().appId)) {
                                        sListIterator.remove();
                                        break;
                                    }
                                }
                            }
                            mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS, "");
                            mView.updateView(data);
                        } else {
                            mView.onLoadingStatus(CommonCode.General.DATA_EMPTY, "");
                        }
                    }
                } else {
                    mView.onLoadingStatus(CommonCode.General.ERROR_DATA, mContext.getString(R.string.load_fail));
                }
            }

            @Override
            public void onFailure(Call<ResponseData<CircleBelongBean>> call, Throwable t) {
                super.onFailure(call, t);
                mView.onLoadingStatus(CommonCode.General.ERROR_DATA, mContext.getString(R.string.load_fail));
            }
        });
    }
}
