package com.rz.circled.presenter.impl;

import android.content.Context;
import android.text.TextUtils;

import com.litesuits.common.utils.HexUtil;
import com.litesuits.common.utils.MD5Util;
import com.rz.circled.R;
import com.rz.circled.http.ApiYylService;
import com.rz.circled.js.http.ActivityNumService;
import com.rz.circled.modle.CircleStatsModel;
import com.rz.circled.presenter.GeneralPresenter;
import com.rz.common.cache.preference.EntityCache;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.CommonCode;
import com.rz.common.ui.inter.IViewController;
import com.rz.common.utils.ACache;
import com.rz.common.utils.NetUtils;
import com.rz.common.utils.Protect;
import com.rz.common.utils.StringUtils;
import com.rz.common.widget.toasty.Toasty;
import com.rz.httpapi.api.ApiService;
import com.rz.httpapi.api.BaseCallback;
import com.rz.httpapi.api.CallManager;
import com.rz.httpapi.api.Http;
import com.rz.httpapi.api.ResponseData.ResponseData;
import com.rz.httpapi.bean.CircleDynamic;
import com.rz.httpapi.bean.CircleEntrModle;
import com.rz.httpapi.bean.DataStatisticsBean;
import com.rz.httpapi.bean.FamousModel;
import com.rz.httpapi.bean.ProveStatusBean;
import com.rz.httpapi.bean.RewardGiftModel;
import com.rz.httpapi.bean.Ticket;
import com.rz.httpapi.bean.TransferResultBean;
import com.rz.httpapi.bean.UserFamousBean;
import com.rz.httpapi.bean.UserSignBean;
import com.rz.httpapi.constans.ReturnCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class V3CirclePresenter extends GeneralPresenter<List<CircleDynamic>> {

    public static final String TAG_LIKE = "tag_like";
    public static final String TAG_COMMENT = "tag_comment";
    public static final String TAG_REWARD = "tag_reward";
    public static final String TAG_FORWARD = "tag_forward";
    public static final int TAG_COMMENT_LIST = 1001;
    public static final int TAG_ZAN = 1002;
    public static final int TAG_DELETE_COMMENT = 1003;
    public static final int TAG_REWARD_LIST = 1004;
    public static final int TAG_ZAN_LIST = 1005;
    public static final int TAG_SIGN = 1006;

    private IViewController mView;
    private Context mContext;
//    private APIService mCircleService;
    private ApiService mUserService;
    private ApiYylService numService;

    public int currentCircleId;

    //处理缓存
    private EntityCache<CircleDynamic> mCirclesCache;
    private List<CircleDynamic> currentData = new ArrayList<>();

    @Override
    public List<CircleDynamic> getCacheData() {
        return null;
    }

    private int dynamicPos = 0;
    private int collectPos = 0;
    private int custPos = 0;

    @Override
    public void attachView(IViewController view) {
        this.mView = view;
        mContext = getContext(mView);
        mCirclesCache = new EntityCache<CircleDynamic>(mContext, CircleDynamic.class);
//        mCircleService = Http.getCircleService(mContext);
        mUserService = Http.getApiService(ApiService.class);
        numService = Http.getApiService(ApiYylService.class);
    }

    @Override
    public void detachView() {

    }

//    public void getV3CircleByCust(String userId, final boolean showLoadingStatus) {
//        Call<ResponseData<List<CircleEntrModle>>> call = null;
//        if (TextUtils.isEmpty(userId)) {
//            userId = null;
//        }
//        call = mUserService.v3CircleByCust(userId);
//        CallManager.add(call);
//        call.enqueue(new BaseCallback<ResponseData<List<CircleEntrModle>>>() {
//            @Override
//            public void onResponse(Call<ResponseData<List<CircleEntrModle>>> call, Response<ResponseData<List<CircleEntrModle>>> response) {
//                super.onResponse(call, response);
//                if (response.isSuccessful()) {
//                    ResponseData res = response.body();
//                    if (res.getRet() == ReturnCode.SUCCESS) {
//                        List<CircleEntrModle> model = (List<CircleEntrModle>) res.getData();
//                        if (null != model && model.size() != 0) {
//                            //发送成功
//                            mView.updateView(model);
//                            if (showLoadingStatus) {
//                                mView.onLoadingStatus(CodeStatus.Gegeneral.DATA_SUCCESS_FULL, "");
//                            }
//                        } else {
//                            if (showLoadingStatus) {
//                                mView.onLoadingStatus(CodeStatus.Gegeneral.DATA_SUCCESS_NULL, "");
//                            }
//                        }
//                        return;
//                    } else if (res.getRet() == ReturnCode.FAIL_REMIND_1) {
//                        //发送失败
//                        if (showLoadingStatus) {
//                            mView.onLoadingStatus(CodeStatus.Gegeneral.ERROR_DATA, mContext.getString(R.string.load_fail));
//                        }
//                        return;
//                    }
//                }
//                if (showLoadingStatus) {
//                    mView.onLoadingStatus(CodeStatus.Gegeneral.ERROR_DATA, mContext.getString(R.string.load_fail));
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseData<List<CircleEntrModle>>> call, Throwable t) {
//                super.onFailure(call, t);
//                //发送验证码失败
//                if (showLoadingStatus) {
//                    mView.onLoadingStatus(CodeStatus.Gegeneral.ERROR_DATA, mContext.getString(R.string.load_fail));
//                }
//            }
//        });
//    }
//
//


    /**
     * 首页圈子入口列表
     *
     * @param status
     */
    public void getCircleEntranceList(final int status) {
        Call<ResponseData<List<CircleEntrModle>>> call = null;
        call = mUserService.getCircleEntrList(status);
        CallManager.add(call);
        call.enqueue(new BaseCallback<ResponseData<List<CircleEntrModle>>>() {
            @Override
            public void onResponse(Call<ResponseData<List<CircleEntrModle>>> call, Response<ResponseData<List<CircleEntrModle>>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    ResponseData res = response.body();
                    if (res.getRet() == ReturnCode.SUCCESS) {
                        List<CircleEntrModle> model = (List<CircleEntrModle>) res.getData();
                        if (null != model && model.size() != 0) {
                            //发送成功
                            mView.updateViewWithFlag(model, status);
                            if (status == 0) {
                                ACache mCache = ACache.get(mContext);
                                mCache.put("cache", (Serializable) model);
                            }
                            mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS, "");
                        } else {
                            mView.onLoadingStatus(CommonCode.General.DATA_EMPTY, "");
                        }
                        return;
                    } else if (res.getRet() == ReturnCode.FAIL_REMIND_1) {
                        //发送失败
                        mView.onLoadingStatus(CommonCode.General.LOAD_ERROR, res.getMsg());
                        return;
                    }
                }
                mView.onLoadingStatus(CommonCode.General.LOAD_ERROR, mContext.getString(R.string.load_fail));
            }

            @Override
            public void onFailure(Call<ResponseData<List<CircleEntrModle>>> call, Throwable t) {
                super.onFailure(call, t);
                //发送验证码失败
                mView.onLoadingStatus(CommonCode.General.LOAD_ERROR, mContext.getString(R.string.load_fail));
            }
        });
    }
//
//    /**
//     * 首页广告位展示
//     */
//    public void getBannerList(final int stats) {
//        if (!NetUtils.isNetworkConnected(mContext)) {
//            return;
//        }
//        Call<ResponseData<List<BannerAddSubjectModel>>> call = mUserService.getBanner(520);
//        CallManager.add(call);
//        call.enqueue(new BaseCallback<ResponseData<List<BannerAddSubjectModel>>>() {
//            @Override
//            public void onResponse(Call<ResponseData<List<BannerAddSubjectModel>>> call, Response<ResponseData<List<BannerAddSubjectModel>>> response) {
//                super.onResponse(call, response);
//                if (response.isSuccessful()) {
//                    ResponseData res = response.body();
//                    if (res.getRet() == ReturnCode.SUCCESS) {
//                        List<BannerAddSubjectModel> model = (List<BannerAddSubjectModel>) res.getData();
//                        mView.updateViewWithFlag(model, stats);
//
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseData<List<BannerAddSubjectModel>>> call, Throwable t) {
//                super.onFailure(call, t);
//            }
//        });
//    }

//    /**
//     * 首页话题展示
//     */
//    public void getSubjectList(final int stats) {
//        if (!NetUtils.isNetworkConnected(mContext)) {
//            return;
//        }
//        Call<ResponseData<List<HotSubjectModel>>> call = mUserService.getHotSubject("1");
//        CallManager.add(call);
//        call.enqueue(new BaseCallback<ResponseData<List<HotSubjectModel>>>() {
//            @Override
//            public void onResponse(Call<ResponseData<List<HotSubjectModel>>> call, Response<ResponseData<List<HotSubjectModel>>> response) {
//                super.onResponse(call, response);
//                if (response.isSuccessful()) {
//                    ResponseData res = response.body();
//                    if (res.getRet() == ReturnCode.SUCCESS) {
//                        List<HotSubjectModel> model = (List<HotSubjectModel>) res.getData();
//                        mView.updateViewWithFlag(model, stats);
//
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseData<List<HotSubjectModel>>> call, Throwable t) {
//                super.onFailure(call, t);
//            }
//        });
//    }

    /**
     * 首页圈子达人
     */
    public void getFamousList(final int stats) {
        if (!NetUtils.isNetworkConnected(mContext)) {
            return;
        }
        Call<ResponseData<List<FamousModel>>> call = mUserService.getFamous("2");
        CallManager.add(call);
        call.enqueue(new BaseCallback<ResponseData<List<FamousModel>>>() {
            @Override
            public void onResponse(Call<ResponseData<List<FamousModel>>> call, Response<ResponseData<List<FamousModel>>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    ResponseData res = response.body();
                    if (res.getRet() == ReturnCode.SUCCESS) {
                        List<FamousModel> model = (List<FamousModel>) res.getData();
                        mView.updateViewWithFlag(model, stats);

                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseData<List<FamousModel>>> call, Throwable t) {
                super.onFailure(call, t);
            }
        });
    }


//    /**
//     * 获取转发详情
//     *
//     * @param transferId
//     */
//    public void getCircleTransferDetail(int transferId) {
//        Call<ResponseData<TransferDetailBean>> call = null;
//        call = mUserService.v3CircleTransferDetail(transferId + "", Session.getUserId());
//        CallManager.add(call);
//        call.enqueue(new BaseCallback<ResponseData<TransferDetailBean>>() {
//            @Override
//            public void onResponse(Call<ResponseData<TransferDetailBean>> call, Response<ResponseData<TransferDetailBean>> response) {
//                super.onResponse(call, response);
//                if (response.isSuccessful()) {
//                    ResponseData res = response.body();
//                    if (res.getRet() == ReturnCode.SUCCESS) {
//                        mView.updateView(res.getData());
//                        mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS);
//                        return;
//                    } else {
//                        mView.onLoadingStatus(CommonCode.General.LOAD_ERROR);
//                        return;
//                    }
//                }
//                mView.onLoadingStatus(CommonCode.General.LOAD_ERROR);
//            }
//
//            @Override
//            public void onFailure(Call<ResponseData<TransferDetailBean>> call, Throwable t) {
//                super.onFailure(call, t);
//                mView.onLoadingStatus(CommonCode.General.LOAD_ERROR);
//            }
//        });
//    }

    //
//    /**
//     * 悠然圈列表
//     *
//     * @param loadMore
//     */
//    public void getCircleSquareList(final boolean loadMore) {
//        Call<ResponseData<List<V3CircleTransfer>>> call = null;
//        call = mUserService.getCircleSquareList(Session.getUserId(), loadMore ? currentCircleId : 0, Constants.PAGESIZE);
//        CallManager.add(call);
//        call.enqueue(new BaseCallback<ResponseData<List<V3CircleTransfer>>>() {
//            @Override
//            public void onResponse(Call<ResponseData<List<V3CircleTransfer>>> call, Response<ResponseData<List<V3CircleTransfer>>> response) {
//                super.onResponse(call, response);
//                if (response.isSuccessful()) {
//                    ResponseData res = response.body();
//                    if (!loadMore) {
//                        dynamicPos = 0;
//                    }
//                    dynamicPos += Constants.PAGESIZE;
//                    if (res.getRet() == ReturnCode.SUCCESS) {
//                        List<V3CircleTransfer> model = (List<V3CircleTransfer>) res.getData();
//                        if (null != model && model.size() != 0) {
//                            //发送成功
//                            mView.updateViewWithLoadMore(model, loadMore);
//                            if (model.get(model.size() - 1) != null) {
//                                currentCircleId = model.get(model.size() - 1).id;
//                            }
//                            mView.onLoadingStatus(CodeStatus.Gegeneral.DATA_SUCCESS_FULL, loadMore);
//                        } else {
//                            mView.onLoadingStatus(CodeStatus.Gegeneral.DATA_SUCCESS_NULL, loadMore);
//                        }
//                        return;
//                    } else if (res.getRet() == ReturnCode.FAIL_REMIND_1) {
//                        //发送失败
//                        mView.onLoadingStatus(CodeStatus.Gegeneral.ERROR_DATA, loadMore);
//                        return;
//                    }
//                }
//                mView.onLoadingStatus(CodeStatus.Gegeneral.ERROR_DATA, mContext.getString(R.string.load_fail));
//            }
//
//            @Override
//            public void onFailure(Call<ResponseData<List<V3CircleTransfer>>> call, Throwable t) {
//                super.onFailure(call, t);
//                //发送验证码失败
//                mView.onLoadingStatus(CodeStatus.Gegeneral.ERROR_DATA, loadMore);
//            }
//        });
//    }
//
//
//    /**
//     * 我的收藏
//     *
//     * @param
//     */
//    public void getCircleCollection(final boolean loadMore) {
//        Call<ResponseData<List<V3CircleTransfer>>> call = null;
//        call = mUserService.getCircleCollect(Session.getUserId(), loadMore ? collectPos : 0, Constants.PAGESIZE);
//        CallManager.add(call);
//        call.enqueue(new BaseCallback<ResponseData<List<V3CircleTransfer>>>() {
//            @Override
//            public void onResponse(Call<ResponseData<List<V3CircleTransfer>>> call, Response<ResponseData<List<V3CircleTransfer>>> response) {
//                super.onResponse(call, response);
//                if (response.isSuccessful()) {
//                    ResponseData res = response.body();
//                    if (!loadMore) {
//                        collectPos = 0;
//                    }
//                    collectPos += Constants.PAGESIZE;
//                    if (res.getRet() == ReturnCode.SUCCESS) {
//                        List<CircleDynamic> model = (List<CircleDynamic>) res.getData();
//                        if (null != model && model.size() > 0) {
//                            //发送成功
//                            mView.updateViewWithLoadMore(model, loadMore);
//                            mView.onLoadingStatus(CodeStatus.Gegeneral.DATA_SUCCESS_FULL, loadMore);
//                        } else {
//                            mView.onLoadingStatus(CodeStatus.Gegeneral.DATA_SUCCESS_NULL, loadMore);
//                        }
//                        return;
//                    } else if (res.getRet() == ReturnCode.FAIL_REMIND_1) {
//                        //发送失败
//                        mView.onLoadingStatus(CodeStatus.Gegeneral.ERROR_DATA, res.getMsg());
//                        return;
//                    }
//                }
//                mView.onLoadingStatus(CodeStatus.Gegeneral.ERROR_DATA, mContext.getString(R.string.load_fail));
//            }
//
//            @Override
//            public void onFailure(Call<ResponseData<List<V3CircleTransfer>>> call, Throwable t) {
//                super.onFailure(call, t);
//                //发送验证码失败
//                mView.onLoadingStatus(CodeStatus.Gegeneral.ERROR_DATA, mContext.getString(R.string.load_fail));
//            }
//        });
//    }
//
//    /**
//     * 取消收藏
//     *
//     * @param circleTransfer
//     */
//    public void requestDeleteCollected(final V3CircleTransfer circleTransfer) {
//        Call<ResponseData> call = null;
//        call = mUserService.deleteCircleCollect(Session.getUserId(), circleTransfer.cid);
//        CallManager.add(call);
//        call.enqueue(new BaseCallback<ResponseData>() {
//            @Override
//            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
//                super.onResponse(call, response);
//                if (response.isSuccessful()) {
//                    ResponseData res = response.body();
//                    if (res.getRet() == ReturnCode.SUCCESS) {
//                        mView.updateView(circleTransfer);
//                        mView.onLoadingStatus(CodeStatus.Gegeneral.DATA_SUCCESS_FULL, R.string.cancel_success);
//                    } else {
//                        mView.onLoadingStatus(CodeStatus.Gegeneral.ERROR_DATA, R.string.cancel_fail);
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseData> call, Throwable t) {
//                super.onFailure(call, t);
//                mView.onLoadingStatus(CodeStatus.Gegeneral.ERROR_DATA, R.string.cancel_fail);
//            }
//        });
//    }
//
//    /**
//     * 提交评论
//     */
//    public void requestComment(final String content, int circleId) {
//        if (!NetUtils.isNetworkConnected(mContext)) {
//            ToastUtil.showToastById(R.string.net_error);
//            return;
//        }
//        Call<ResponseData<V3CommentStatusModel>> call = null;
//        call = Http.getCircleService(mContext).v3Comment(
//                circleId + "",
//                Session.getUserId(),
//                content,
//                0
//        );
//        CallManager.add(call);
//        call.enqueue(new BaseCallback<ResponseData<V3CommentStatusModel>>() {
//            @Override
//            public void onResponse(Call<ResponseData<V3CommentStatusModel>> call, Response<ResponseData<V3CommentStatusModel>> response) {
//                super.onResponse(call, response);
//                if (response.isSuccessful()) {
//                    ResponseData res = response.body();
//                    if (res.getRet() == ReturnCode.SUCCESS) {
//                        V3CommentStatusModel statusModel = (V3CommentStatusModel) res.getData();
//                        mView.updateView(statusModel);
//                        mView.onLoadingStatus(CodeStatus.Gegeneral.DATA_SUCCESS_FULL, R.string.comment_success);
//                    } else {
//                        mView.onLoadingStatus(CodeStatus.Gegeneral.ERROR_DATA, R.string.comment_fail);
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseData<V3CommentStatusModel>> call, Throwable t) {
//                super.onFailure(call, t);
//                mView.onLoadingStatus(CodeStatus.Gegeneral.ERROR_DATA, R.string.comment_fail);
//            }
//        });
//    }
//
//    /**
//     * 提交回复
//     */
//    public void requestReply(String infoId, String content, String replyId, String replyBy) {
//        if (!NetUtils.isNetworkConnected(mContext)) {
//            ToastUtil.showToastById(R.string.net_error);
//            return;
//        }
//        Call<ResponseData<V3CommentStatusModel>> call = null;
//        call = Http.getCircleService(mContext).v3CommentReply(
//                infoId + "",
//                Session.getUserId(),
//                content,
//                0,
//                replyId,
//                replyBy
//        );
//
//        CallManager.add(call);
//        call.enqueue(new BaseCallback<ResponseData<V3CommentStatusModel>>() {
//            @Override
//            public void onResponse(Call<ResponseData<V3CommentStatusModel>> call, Response<ResponseData<V3CommentStatusModel>> response) {
//                super.onResponse(call, response);
//                if (response.isSuccessful()) {
//                    ResponseData res = response.body();
//                    if (res.getRet() == ReturnCode.SUCCESS) {
//                        V3CommentStatusModel statusModel = (V3CommentStatusModel) res.getData();
//                        mView.onLoadingStatus(CodeStatus.Gegeneral.DATA_SUCCESS_FULL, R.string.reply_success);
//                        mView.updateView(statusModel);
//                    } else {
//                        mView.onLoadingStatus(CodeStatus.Gegeneral.ERROR_DATA,  R.string.reply_fail);
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseData<V3CommentStatusModel>> call, Throwable t) {
//                super.onFailure(call, t);
//                mView.onLoadingStatus(CodeStatus.Gegeneral.ERROR_DATA, R.string.reply_fail);
//            }
//        });
//    }
//
//    /**
//     * 点赞与取消点赞
//     */
//    public void requestLike(final boolean likeIt, String circleId) {
//        Call<ResponseData> call = null;
//        call = Http.getCircleService(mContext).v3Zan(
//                circleId + "",
//                Session.getUserId(),
//                0,
//                (!likeIt) ? 1 : 0
//        );
//        CallManager.add(call);
//        call.enqueue(new BaseCallback<ResponseData>() {
//            @Override
//            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
//                super.onResponse(call, response);
//                if (response.isSuccessful()) {
//                    ResponseData<List<OpusData>> res = response.body();
//                    if (res.getRet() == ReturnCode.SUCCESS) {
//                        mView.updateViewWithFlag("", TAG_ZAN);
//                    } else {
//                        mView.onLoadingStatus(CodeStatus.Gegeneral.ERROR_DATA, R.string.like_fail);
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseData> call, Throwable t) {
//                super.onFailure(call, t);
//                mView.onLoadingStatus(CodeStatus.Gegeneral.ERROR_DATA,  R.string.like_fail);
//            }
//        });
//    }
//
//
    public void loadRewardGiftList() {
        if (!NetUtils.isNetworkConnected(mContext)) {
            Toasty.info(mContext, mContext.getString(R.string.status_un_network)).show();
            return;
        }
        mView.onLoadingStatus(CommonCode.General.DATA_LOADING, "");
        Call<ResponseData<List<RewardGiftModel>>> call = mUserService.v3CircleGetTransferPrice(
                Session.getUserId());
        CallManager.add(call);
        call.enqueue(new BaseCallback<ResponseData<List<RewardGiftModel>>>() {
            @Override
            public void onResponse(Call<ResponseData<List<RewardGiftModel>>> call, Response<ResponseData<List<RewardGiftModel>>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    ResponseData<List<RewardGiftModel>> res = response.body();
                    if (res.getRet() == ReturnCode.SUCCESS) {
                        List<RewardGiftModel> mGifts = res.getData();
                        if (mGifts != null && !mGifts.isEmpty()) {
                            mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS, "");
                            mView.updateView(mGifts);
                            try {
                                EntityCache<RewardGiftModel> entityCache = new EntityCache<RewardGiftModel>(mContext, RewardGiftModel.class);
                                entityCache.putListEntityAddTag(mGifts, "transfer");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return;
                        } else {
                            mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS, "");
                            return;
                        }
                    }
                }
                mView.onLoadingStatus(CommonCode.General.LOAD_ERROR, mContext.getString(R.string.load_fail));
            }

            @Override
            public void onFailure(Call<ResponseData<List<RewardGiftModel>>> call, Throwable t) {
                super.onFailure(call, t);
                mView.onLoadingStatus(CommonCode.General.LOAD_ERROR, mContext.getString(R.string.load_fail));
            }
        });
    }

    //
//
//    public void getCircleStats(String userId) {
//        Call<ResponseData<CircleStatsModel>> call = null;
//        call = mUserService.getCircleStats(userId);
//        CallManager.add(call);
//        call.enqueue(new BaseCallback<ResponseData<CircleStatsModel>>() {
//            @Override
//            public void onResponse(Call<ResponseData<CircleStatsModel>> call, Response<ResponseData<CircleStatsModel>> response) {
//                super.onResponse(call, response);
//                if (response.isSuccessful()) {
//                    ResponseData res = response.body();
//                    if (res.getRet() == ReturnCode.SUCCESS) {
//                        CircleStatsModel model = (CircleStatsModel) res.getData();
//                        if (null != model) {
//                            mView.updateView(model);
//                            return;
//                        } else {
//                        }
//                    } else if (res.getRet() == ReturnCode.FAIL_REMIND_1) {
//                        //发送失败
//                        return;
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseData<CircleStatsModel>> call, Throwable t) {
//                super.onFailure(call, t);
//                //发送验证码失败
//            }
//        });
//    }

//    /**
//     * 转发统计
//     *
//     * @param
//     */
//    public void getCircleStats() {
//        Call<ResponseData<CircleStatsModel>> call = null;
//        call = mUserService.getCircleStats(Session.getUserId());
//        CallManager.add(call);
//        call.enqueue(new BaseCallback<ResponseData<CircleStatsModel>>() {
//            @Override
//            public void onResponse(Call<ResponseData<CircleStatsModel>> call, Response<ResponseData<CircleStatsModel>> response) {
//                super.onResponse(call, response);
//                if (response.isSuccessful()) {
//                    ResponseData res = response.body();
//                    if (res.getRet() == ReturnCode.SUCCESS) {
//                        CircleStatsModel model = (CircleStatsModel) res.getData();
//                        if (null != model) {
//                            EntityCache<CircleStatsModel> entityCache = new EntityCache<CircleStatsModel>(mContext, CircleStatsModel.class);
//                            entityCache.putEntity(model);
//                            mView.updateView(model);
//                            return;
//                        } else {
//                            mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS, "");
//                        }
//                    } else if (res.getRet() == ReturnCode.FAIL_REMIND_1) {
//                        //发送失败
//                        mView.onLoadingStatus(CommonCode.General.ERROR_DATA, res.getMsg());
//                        return;
//                    }
//                }
//                mView.onLoadingStatus(CommonCode.General.ERROR_DATA, mContext.getString(R.string.load_fail));
//            }
//
//            @Override
//            public void onFailure(Call<ResponseData<CircleStatsModel>> call, Throwable t) {
//                super.onFailure(call, t);
//                //发送验证码失败
//                mView.onLoadingStatus(CommonCode.General.ERROR_DATA, mContext.getString(R.string.load_fail));
//            }
//        });
//    }

//    /**
//     * 点赞状态
//     */
//    public void getZanStatus(String infoId) {
//        Call<ResponseData<V3ZanStatusModel>> call = null;
//        call = mUserService.v3ZanStats(infoId, Session.getUserId());
//        CallManager.add(call);
//        call.enqueue(new BaseCallback<ResponseData<V3ZanStatusModel>>() {
//            @Override
//            public void onResponse(Call<ResponseData<V3ZanStatusModel>> call, Response<ResponseData<V3ZanStatusModel>> response) {
//                super.onResponse(call, response);
//                if (response.isSuccessful()) {
//                    ResponseData res = response.body();
//                    if (res.getRet() == ReturnCode.SUCCESS) {
//                        V3ZanStatusModel model = (V3ZanStatusModel) res.getData();
//                        mView.updateView(model);
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseData<V3ZanStatusModel>> call, Throwable t) {
//                super.onFailure(call, t);
//            }
//        });
//    }
//
//    /**
//     * 获得评论列表数据
//     *
//     * @param infoId
//     * @param start
//     * @param limit
//     * @param needMore false 刷新,true 加载更多
//     */
//    public void getCommentListData(String infoId, int start, int limit, final boolean needMore) {
//        Call<ResponseData<V3CommentRootModel>> call = null;
//        call = mUserService.getCircleCommentList(infoId, Session.getUserId(),
//                start,
//                limit);
//        CallManager.add(call);
//        call.enqueue(new BaseCallback<ResponseData<V3CommentRootModel>>() {
//            @Override
//            public void onResponse(Call<ResponseData<V3CommentRootModel>> call, Response<ResponseData<V3CommentRootModel>> response) {
//                super.onResponse(call, response);
//                if (response.isSuccessful()) {
//                    ResponseData res = response.body();
//                    if (res.getRet() == ReturnCode.SUCCESS) {
//                        V3CommentRootModel model = (V3CommentRootModel) res.getData();
//                        mView.updateViewWithLoadMore(model, needMore);
//                    } else {
//                        Toasty.info(mContext, R.string.no_more_data).show();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseData<V3CommentRootModel>> call, Throwable t) {
//                super.onFailure(call, t);
//            }
//        });
//    }
//
//    /**
//     * 删除评论
//     */
//    public void requestDeleteComment(final V3CommentModel commentModel) {
//        if (!NetUtils.isNetworkConnected(mContext)) {
//            ToastUtil.showToastById(R.string.net_error);
//            return;
//        }
//        Call<ResponseData<V3CommentStatusModel>> call = null;
//        call = mUserService.v3DeleteComment(commentModel.getCommentId(), Session.getUserId());
//        CallManager.add(call);
//        call.enqueue(new BaseCallback<ResponseData<V3CommentStatusModel>>() {
//            @Override
//            public void onResponse(Call<ResponseData<V3CommentStatusModel>> call, Response<ResponseData<V3CommentStatusModel>> response) {
//                super.onResponse(call, response);
//                if (response.isSuccessful()) {
//                    ResponseData res = response.body();
//                    if (res.getRet() == ReturnCode.SUCCESS) {
//                        mView.updateViewWithFlag(commentModel, TAG_DELETE_COMMENT);
//                        mView.onLoadingStatus(CodeStatus.Gegeneral.DATA_SUCCESS_FULL, R.string.del_success);
//                    } else {
//                        mView.onLoadingStatus(CodeStatus.Gegeneral.ERROR_DATA, R.string.del_fail);
//                    }
//                } else {
//                    mView.onLoadingStatus(CodeStatus.Gegeneral.ERROR_DATA, R.string.del_fail);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseData<V3CommentStatusModel>> call, Throwable t) {
//                super.onFailure(call, t);
//                mView.onLoadingStatus(CodeStatus.Gegeneral.ERROR_DATA, R.string.del_fail);
//            }
//        });
//    }
//
//    /**
//     * 获得评论状态
//     */
//    public void getCommentStatus(String infoId, String custId) {
//        Call<ResponseData<V3CommentStatusModel>> call = null;
//        call = mUserService.v3CommentStats(infoId, custId);
//        CallManager.add(call);
//        call.enqueue(new BaseCallback<ResponseData<V3CommentStatusModel>>() {
//            @Override
//            public void onResponse(Call<ResponseData<V3CommentStatusModel>> call, Response<ResponseData<V3CommentStatusModel>> response) {
//                super.onResponse(call, response);
//                if (response.isSuccessful()) {
//                    ResponseData res = response.body();
//                    if (res.getRet() == ReturnCode.SUCCESS) {
//                        V3CommentStatusModel statusModel = (V3CommentStatusModel) res.getData();
//                        mView.updateView(statusModel);
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseData<V3CommentStatusModel>> call, Throwable t) {
//                super.onFailure(call, t);
//            }
//        });
//    }
//
//    /**
//     * 获得打赏列表数据
//     */
//    public void getRewardListData(String id, int infoId, int limit) {
//        Call<ResponseData<List<V3RewardModel>>> call = null;
//        call = mUserService.v3CircleRewardList(Session.getUserId(), infoId, "", 0, limit);
//        CallManager.add(call);
//        call.enqueue(new BaseCallback<ResponseData<List<V3RewardModel>>>() {
//            @Override
//            public void onResponse(Call<ResponseData<List<V3RewardModel>>> call, Response<ResponseData<List<V3RewardModel>>> response) {
//                super.onResponse(call, response);
//                if (response.isSuccessful()) {
//                    ResponseData res = response.body();
//                    if (res.getRet() == ReturnCode.SUCCESS) {
//                        List<RewardModel> modelList = (List<RewardModel>) res.getData();
//                        mView.updateViewWithFlag(modelList, TAG_REWARD_LIST);
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseData<List<V3RewardModel>>> call, Throwable t) {
//                super.onFailure(call, t);
//            }
//        });
//    }
//
//    /**
//     * 获得点赞列表数据
//     */
//    public void getZanListData(String infoId, int limit) {
//        Call<ResponseData<List<V3ZanModel>>> call = null;
//        call = mUserService.v3ZanList(infoId, Session.getUserId(), 0, limit);
//        CallManager.add(call);
//        call.enqueue(new BaseCallback<ResponseData<List<V3ZanModel>>>() {
//            @Override
//            public void onResponse(Call<ResponseData<List<V3ZanModel>>> call, Response<ResponseData<List<V3ZanModel>>> response) {
//                super.onResponse(call, response);
//                if (response.isSuccessful()) {
//                    ResponseData res = response.body();
//                    if (res.getRet() == ReturnCode.SUCCESS) {
//                        List<V3ZanModel> modelList = (List<V3ZanModel>) res.getData();
//                        mView.updateViewWithFlag(modelList, TAG_ZAN_LIST);
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseData<List<V3ZanModel>>> call, Throwable t) {
//                super.onFailure(call, t);
//            }
//        });
//    }
//
//    /**
//     * 查询转发列表（我转别人）
//     *
//     * @param loadMore
//     */
//    public void getTransferListByUser(String custId, final boolean loadMore) {
//        Call<ResponseData<List<V3CircleTransfer>>> call = null;
//        call = mUserService.v3CircleTransferOthers(custId, loadMore ? currentCircleId : 0, Constants.PAGESIZE);
//        CallManager.add(call);
//        call.enqueue(new BaseCallback<ResponseData<List<V3CircleTransfer>>>() {
//            @Override
//            public void onResponse(Call<ResponseData<List<V3CircleTransfer>>> call, Response<ResponseData<List<V3CircleTransfer>>> response) {
//                super.onResponse(call, response);
//                if (response.isSuccessful()) {
//                    ResponseData res = response.body();
//                    if (!loadMore) {
//                        dynamicPos = 0;
//                    }
//                    dynamicPos += Constants.PAGESIZE;
//                    if (res.getRet() == ReturnCode.SUCCESS) {
//                        List<V3CircleTransfer> model = (List<V3CircleTransfer>) res.getData();
//                        if (null != model && model.size() != 0) {
//                            //发送成功
//                            mView.updateViewWithLoadMore(model, loadMore);
//                            if (model.get(model.size() - 1) != null) {
//                                currentCircleId = model.get(model.size() - 1).id;
//                            }
//                            mView.onLoadingStatus(CodeStatus.Gegeneral.DATA_SUCCESS_FULL, loadMore);
//                        } else {
//                            mView.onLoadingStatus(CodeStatus.Gegeneral.DATA_SUCCESS_NULL, loadMore);
//                        }
//                        return;
//                    } else if (res.getRet() == ReturnCode.FAIL_REMIND_1) {
//                        //发送失败
//                        mView.onLoadingStatus(CodeStatus.Gegeneral.ERROR_DATA, loadMore);
//                        return;
//                    }
//                }
//                mView.onLoadingStatus(CodeStatus.Gegeneral.ERROR_DATA, mContext.getString(R.string.load_fail));
//            }
//
//            @Override
//            public void onFailure(Call<ResponseData<List<V3CircleTransfer>>> call, Throwable t) {
//                super.onFailure(call, t);
//                //发送验证码失败
//                mView.onLoadingStatus(CodeStatus.Gegeneral.ERROR_DATA, loadMore);
//            }
//        });
//    }
//
//    /**
//     * 别人转我的（列表）
//     *
//     * @param loadMore
//     */
//    public void getBeTransferList(String custId, final boolean loadMore) {
//        Call<ResponseData<List<V3CircleTransfer>>> call = mUserService.v3CircleOthersTransferMe(custId, loadMore ? currentCircleId : 0, Constants.PAGESIZE);
//        CallManager.add(call);
//        call.enqueue(new BaseCallback<ResponseData<List<V3CircleTransfer>>>() {
//            @Override
//            public void onResponse(Call<ResponseData<List<V3CircleTransfer>>> call, Response<ResponseData<List<V3CircleTransfer>>> response) {
//                super.onResponse(call, response);
//                if (response.isSuccessful()) {
//                    ResponseData res = response.body();
//                    if (!loadMore) {
//                        dynamicPos = 0;
//                    }
//                    dynamicPos += Constants.PAGESIZE;
//                    if (res.getRet() == ReturnCode.SUCCESS) {
//                        List<V3CircleTransfer> model = (List<V3CircleTransfer>) res.getData();
//                        if (null != model && model.size() != 0) {
//                            //发送成功
//                            mView.updateViewWithLoadMore(model, loadMore);
//                            if (model.get(model.size() - 1) != null) {
//                                currentCircleId = model.get(model.size() - 1).id;
//                            }
//                            mView.onLoadingStatus(CodeStatus.Gegeneral.DATA_SUCCESS_FULL, loadMore);
//                        } else {
//                            mView.onLoadingStatus(CodeStatus.Gegeneral.DATA_SUCCESS_NULL, loadMore);
//                        }
//                        return;
//                    } else if (res.getRet() == ReturnCode.FAIL_REMIND_1) {
//                        //发送失败
//                        mView.onLoadingStatus(CodeStatus.Gegeneral.ERROR_DATA, loadMore);
//                        return;
//                    }
//                }
//                mView.onLoadingStatus(CodeStatus.Gegeneral.ERROR_DATA, mContext.getString(R.string.load_fail));
//            }
//
//            @Override
//            public void onFailure(Call<ResponseData<List<V3CircleTransfer>>> call, Throwable t) {
//                super.onFailure(call, t);
//                //发送验证码失败
//                mView.onLoadingStatus(CodeStatus.Gegeneral.ERROR_DATA, loadMore);
//            }
//        });
//    }
//

    /**
     * 圈子转发
     *
     * @param parentId
     * @param authorId
     * @param appId
     * @param moduleId
     * @param infoId
     * @param infoTitle
     * @param infoDesc
     * @param infoThumbnail
     * @param infoPic
     * @param infoVideo
     * @param infoVideoPic
     * @param price
     */
    public void circleTransfer(String parentId, String authorId, String appId, String moduleId, String infoId, String infoTitle, String infoDesc, String infoThumbnail, String infoPic, String infoVideo, String infoVideoPic, long price, String password, String createTime) {
        Call<ResponseData<TransferResultBean>> call = null;
        if (!TextUtils.isEmpty(infoId)) {
            String[] s = infoId.split("\\.");
            if (s.length > 0) {
                infoId = s[0];
            }
        }
        call = mUserService.v3CircleTransfer(Protect.toNull(Session.getUserId()), Protect.toNull(parentId), Protect.toNull(authorId), Protect.toNull(appId), Protect.toNull(moduleId), Protect.toNull(infoId), Protect.toNull(infoTitle), Protect.toNull(infoDesc), Protect.toNull(infoThumbnail), Protect.toNull(infoPic), Protect.toNull(infoVideo), Protect.toNull(infoVideoPic), price, StringUtils.isEmpty(password) ? "" : HexUtil.encodeHexStr(MD5Util.md5(password)), createTime);
        CallManager.add(call);
        call.enqueue(new BaseCallback<ResponseData<TransferResultBean>>() {
            @Override
            public void onResponse(Call<ResponseData<TransferResultBean>> call, Response<ResponseData<TransferResultBean>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    ResponseData res = response.body();
                    if (res.getRet() == ReturnCode.SUCCESS) {
                        TransferResultBean result = (TransferResultBean) res.getData();
                        if (result != null) {
                            if (result.id != 0) {
                                mView.updateView(result.id);
                                return;
                            } else {
                                mView.onLoadingStatus(CommonCode.General.DATA_EMPTY, "");
                            }
                        }
                    } else if (res.getRet() == ReturnCode.FAIL_REMIND_1) {
                        //发送失败
                        mView.onLoadingStatus(CommonCode.General.LOAD_ERROR, res.getMsg());
                        return;
                    }
                }
                mView.onLoadingStatus(CommonCode.General.LOAD_ERROR, mContext.getString(R.string.load_fail));
            }

            @Override
            public void onFailure(Call<ResponseData<TransferResultBean>> call, Throwable t) {
                super.onFailure(call, t);
                //发送验证码失败
                mView.onLoadingStatus(CommonCode.General.LOAD_ERROR, mContext.getString(R.string.load_fail));
            }
        });
    }


    public void matchVoucher(String cost, int type) {
        if (!NetUtils.isNetworkConnected(mContext)) {
            mView.onLoadingStatus(CommonCode.General.UN_NETWORK);
            return;
        }
        mView.onLoadingStatus(CommonCode.General.DATA_LOADING);
        Call<ResponseData<Ticket>> call = mUserService.MatchVoucher(cost, Session.getUserId(), type);
        CallManager.add(call);
        call.enqueue(new BaseCallback<ResponseData<Ticket>>() {
            @Override
            public void onResponse(Call<ResponseData<Ticket>> call, Response<ResponseData<Ticket>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    ResponseData<Ticket> res = response.body();
                    if (res.getRet() == ReturnCode.SUCCESS) {
                        Ticket model = res.getData();
                        if (null != model) {
                            mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS);
                            mView.updateView(model);
                        } else {
//                            mView.onLoadingStatus(CodeStatus.Gegeneral.NO_TICKET_MSG, "");
                            mView.onLoadingStatus(CommonCode.General.DATA_EMPTY);
                        }
                        return;
                    } else {
//                        mView.onLoadingStatus(CodeStatus.Gegeneral.DATA_SUCCESS_NULL, "");
//                        mView.onLoadingStatus(CodeStatus.Gegeneral.NO_TICKET_MSG, "");
                        mView.onLoadingStatus(CommonCode.General.DATA_EMPTY);
//                        if (HandleRetCode.handler(mContext, res)) {
//                            new Handler().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    mView.onLoadingStatus(CodeStatus.Gegeneral.ERROR_DATA, "");
//                                }
//                            }, 2000);
//                        }
                        return;
                    }
                }
                mView.onLoadingStatus(CommonCode.General.LOAD_ERROR);
            }

            @Override
            public void onFailure(Call<ResponseData<Ticket>> call, Throwable t) {
                super.onFailure(call, t);
                mView.onLoadingStatus(CommonCode.General.LOAD_ERROR);
            }
        });
    }
//
//
//    public void getCircleTransferCust(String custId, String transferId, String infoId, boolean loadMore) {
//        if (!loadMore) {
//            custPos = 0;
//        }
//        getCircleTransferCust(custId, transferId, infoId, custPos, Constants.PAGESIZE);
//    }
//
//    public void getCircleTransferCust(String custId, String transferId, String infoId, final int start, final int limit) {
//        Call<ResponseData<List<V3CircleTransfer.User>>> call = null;
//        call = mUserService.v3CircleGetCust(Protect.toNull(custId), (Protect.toNull(transferId)), Protect.toNull(infoId), start, limit);
//        CallManager.add(call);
//        call.enqueue(new BaseCallback<ResponseData<List<V3CircleTransfer.User>>>() {
//            @Override
//            public void onResponse(Call<ResponseData<List<V3CircleTransfer.User>>> call, Response<ResponseData<List<V3CircleTransfer.User>>> response) {
//                super.onResponse(call, response);
//                if (response.isSuccessful()) {
//                    ResponseData res = response.body();
//                    if (res.getRet() == ReturnCode.SUCCESS) {
//                        custPos += limit;
//                        List<V3CircleTransfer.User> result = (List<V3CircleTransfer.User>) res.getData();
//                        if (result != null && !result.isEmpty()) {
//                            mView.updateViewWithLoadMore(result, start != 0);
//                            mView.onLoadingStatus(CodeStatus.Gegeneral.DATA_SUCCESS_FULL, start != 0);
//                            return;
//                        } else {
//                            mView.onLoadingStatus(CodeStatus.Gegeneral.DATA_SUCCESS_NULL, start != 0);
//                            return;
//                        }
//                    } else if (res.getRet() == ReturnCode.FAIL_REMIND_1) {
//                        //发送失败
//                        mView.onLoadingStatus(CodeStatus.Gegeneral.ERROR_DATA, start != 0);
//                        return;
//                    }
//                }
//                mView.onLoadingStatus(CodeStatus.Gegeneral.ERROR_DATA, start != 0);
//            }
//
//            @Override
//            public void onFailure(Call<ResponseData<List<V3CircleTransfer.User>>> call, Throwable t) {
//                super.onFailure(call, t);
//                //发送验证码失败
//                mView.onLoadingStatus(CodeStatus.Gegeneral.ERROR_DATA, start != 0);
//            }
//        });
//    }
//
//
//    /**
//     * 全局搜索
//     *
//     * @param keyword
//     * @param loadMore
//     */
//    public void homeSearch(String keyword, final boolean loadMore) {
//        if (!NetUtils.isNetworkConnected(mContext)) {
//            mView.onLoadingStatus(CodeStatus.Gegeneral.ERROR_NET, loadMore);
//            return;
//        }
//        mView.onLoadingStatus(CodeStatus.Gegeneral.DATA_LOADING, loadMore);
//        Call<ResponseData<V3SearchData>> call = mCircleService.v3HomeSearch(
//                Protect.toNull(Session.getUserId()),
//                keyword);
//        CallManager.add(call);
//        call.enqueue(new BaseCallback<ResponseData<V3SearchData>>() {
//            @Override
//            public void onResponse(Call<ResponseData<V3SearchData>> call, Response<ResponseData<V3SearchData>> response) {
//                super.onResponse(call, response);
//                if (response.isSuccessful()) {
//                    ResponseData<V3SearchData> res = response.body();
//                    //FIXME 服务端修改字段
//                    if (res.getRet() == ReturnCode.SUCCESS) {
//                        V3SearchData searchData = res.getData();
//                        if (null != searchData) {
//                            mView.onLoadingStatus(CodeStatus.Gegeneral.DATA_SUCCESS_FULL, loadMore);
//                            mView.updateView(searchData);
//                            return;
//                        }
//                    }
//                }
//                mView.onLoadingStatus(CodeStatus.Gegeneral.ERROR_DATA, loadMore);
//            }
//
//            @Override
//            public void onFailure(Call<ResponseData<V3SearchData>> call, Throwable t) {
//                super.onFailure(call, t);
//                mView.onLoadingStatus(CodeStatus.Gegeneral.ERROR_DATA, loadMore);
//            }
//        });
//    }

    /**
     * 用户签到
     * @param custId
     * @param eventCode
     */

    public void signRequest(String custId, String eventCode) {
        if (!NetUtils.isNetworkConnected(mContext)) {
            mView.onLoadingStatus(CommonCode.General.UN_NETWORK);
            return;
        }
        mView.onLoadingStatus(CommonCode.General.DATA_LOADING);
        Call<ResponseData> call = mUserService.signRequest(custId, eventCode);
        CallManager.add(call);
        call.enqueue(new BaseCallback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    ResponseData res = response.body();
                    if(res != null){
                        mView.updateViewWithFlag(res,TAG_SIGN);
                    }
                }
                mView.onLoadingStatus(CommonCode.General.LOAD_ERROR);
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                super.onFailure(call, t);
                mView.onLoadingStatus(CommonCode.General.LOAD_ERROR);
            }
        });
    }


    /**
     * 获取用户签到状态
     * @param custId
     * @param eventCode
     */
    public void getSignStatus(String custId, String eventCode) {
        if (!NetUtils.isNetworkConnected(mContext)) {
            mView.onLoadingStatus(CommonCode.General.UN_NETWORK);
            return;
        }
        Call<ResponseData<UserSignBean>> call = mUserService.getSignStatus(custId, eventCode);
        CallManager.add(call);
        call.enqueue(new BaseCallback<ResponseData<UserSignBean>>() {
            @Override
            public void onResponse(Call<ResponseData<UserSignBean>> call, Response<ResponseData<UserSignBean>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    ResponseData<UserSignBean> res = response.body();
                    if (res.getRet() == ReturnCode.SUCCESS) {
                        UserSignBean model = res.getData();
                        mView.updateView(model);
                        return;
                    } else {

                        return;
                    }
                }
                mView.onLoadingStatus(CommonCode.General.LOAD_ERROR);
            }

            @Override
            public void onFailure(Call<ResponseData<UserSignBean>> call, Throwable t) {
                super.onFailure(call, t);
                mView.onLoadingStatus(CommonCode.General.LOAD_ERROR);
            }
        });
    }

    /**
     * 获取达人状态
     * @param custId
     */
    public void getFamousStatus(String custId) {
        if (!NetUtils.isNetworkConnected(mContext)) {
            mView.onLoadingStatus(CommonCode.General.UN_NETWORK);
            return;
        }
//        mView.onLoadingStatus(CommonCode.General.DATA_LOADING);
        Call<ResponseData<ProveStatusBean>> call = mUserService.getFamousStatus(custId);
        CallManager.add(call);
        call.enqueue(new BaseCallback<ResponseData<ProveStatusBean>>() {
            @Override
            public void onResponse(Call<ResponseData<ProveStatusBean>> call, Response<ResponseData<ProveStatusBean>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    ResponseData<ProveStatusBean> res = response.body();
                    if (res.getRet() == ReturnCode.SUCCESS) {
                        ProveStatusBean model = res.getData();
                        mView.updateView(model);
                        return;
                    } else {
                        return;
                    }
                }
                mView.onLoadingStatus(CommonCode.General.LOAD_ERROR);
            }

            @Override
            public void onFailure(Call<ResponseData<ProveStatusBean>> call, Throwable t) {
                super.onFailure(call, t);
//                mView.onLoadingStatus(CommonCode.General.LOAD_ERROR);
            }
        });
    }


    /**
     * 获取数据统计
     * @param custId
     */
    public void getUserStat(String custId) {
        if (!NetUtils.isNetworkConnected(mContext)) {
            mView.onLoadingStatus(CommonCode.General.UN_NETWORK);
            return;
        }
//        mView.onLoadingStatus(CommonCode.General.DATA_LOADING);
        Call<ResponseData<DataStatisticsBean>> call = mUserService.getUserStat(custId);
        CallManager.add(call);
        call.enqueue(new BaseCallback<ResponseData<DataStatisticsBean>>() {
            @Override
            public void onResponse(Call<ResponseData<DataStatisticsBean>> call, Response<ResponseData<DataStatisticsBean>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    ResponseData<DataStatisticsBean> res = response.body();
                    if (res.getRet() == ReturnCode.SUCCESS) {
                        DataStatisticsBean model = res.getData();
                        mView.updateView(model);
                        return;
                    } else {

                        return;
                    }
                }
                mView.onLoadingStatus(CommonCode.General.LOAD_ERROR);
            }

            @Override
            public void onFailure(Call<ResponseData<DataStatisticsBean>> call, Throwable t) {
                super.onFailure(call, t);
//                mView.onLoadingStatus(CommonCode.General.LOAD_ERROR);
            }
        });
    }

    /**
     * 获取活动数据统计
     * @param custId
     */
    public void getMylistCount(String custId) {
        if (!NetUtils.isNetworkConnected(mContext)) {
            mView.onLoadingStatus(CommonCode.General.UN_NETWORK);
            return;
        }
        Call<ResponseData> call = numService.mylistCount(custId);
        CallManager.add(call);
        call.enqueue(new BaseCallback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    ResponseData res = response.body();
                    if (res.getRet() == ReturnCode.SUCCESS) {
                        mView.updateView(res);
                        return;
                    } else {

                        return;
                    }
                }
                mView.onLoadingStatus(CommonCode.General.LOAD_ERROR);
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                super.onFailure(call, t);
            }
        });
    }

}
