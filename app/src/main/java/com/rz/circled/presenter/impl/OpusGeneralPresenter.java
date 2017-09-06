package com.rz.circled.presenter.impl;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.rz.circled.presenter.GeneralPresenter;
import com.rz.circled.widget.pinyin.CharacterParser;
import com.rz.circled.widget.pinyin.PinyinComparator;
import com.rz.common.application.BaseApplication;
import com.rz.common.cache.preference.EntityCache;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.CommonCode;
import com.rz.common.constant.Constants;
import com.rz.common.ui.inter.IViewController;
import com.rz.common.utils.NetUtils;
import com.rz.common.utils.StringUtils;
import com.rz.httpapi.api.ApiService;
import com.rz.httpapi.api.BaseCallback;
import com.rz.httpapi.api.CallManager;
import com.rz.httpapi.api.Http;
import com.rz.httpapi.api.ResponseData.ResponseData;
import com.rz.httpapi.bean.BaseInfo;
import com.rz.httpapi.bean.CircleMemberModel;
import com.rz.httpapi.bean.ClubStats;
import com.rz.httpapi.bean.OpusData;
import com.rz.httpapi.bean.OpusTag;
import com.rz.httpapi.bean.TransferDetail;
import com.rz.httpapi.constans.ReturnCode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class OpusGeneralPresenter extends GeneralPresenter<List<OpusData>> {

    EntityCache<OpusData> opusDataEntityCache;
    private IViewController iViewController;
    private Context mContext;
    private ApiService mCircleService;

    int actionCode = 0;
    int type = 1;
    String authorid = null;
    public String categroy;
    String tag;

    int pageCount = 0;

    private List<OpusData> currentData = new ArrayList<>();

    private CharacterParser mCharacterParser;
    private PinyinComparator mPyComparator;

    public OpusGeneralPresenter(String categroy, int type) {
        this.categroy = categroy;
        this.type = type;
    }

    public OpusGeneralPresenter() {
    }


    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public void attachView(IViewController view) {
        if (view instanceof Context) {
            opusDataEntityCache = new EntityCache<OpusData>((Context) view, OpusData.class);
        }
        if (view instanceof android.support.v4.app.Fragment) {
            opusDataEntityCache = new EntityCache<OpusData>(getContext(view), OpusData.class);
        }
        iViewController = view;
        mContext = getContext(iViewController);
        mCircleService = Http.getApiService(ApiService.class);
        mCharacterParser = CharacterParser.getInstance();
        mPyComparator = new PinyinComparator();
    }

    public void init(String authorid, int actionCode) {
        this.authorid = authorid;
        this.actionCode = actionCode;
    }

    public List<OpusData> handleData(List<OpusData> opusDatas) {
        List<OpusData> result = new ArrayList<>();
        if (opusDatas != null && opusDatas.size() > 0) {
            for (int i = 0; i < opusDatas.size(); i++) {
                OpusData opusData = opusDatas.get(i);
                if (opusData.auditStatus == 3) {
                    result.add(opusData);
                }
            }
        }
        return result;

    }


    /**
     * @param loadMore true 表示下拉加载  false 表示上啦加载
     */
    @Override
    public void loadData(final boolean loadMore) {
        final String tempCategory = categroy;
        Log.d("yeying", "this is OpusGeneralPresenter" + "loaddata categroy is " + tempCategory);
        if (!NetUtils.isNetworkConnected(BaseApplication.getContext())) {
            iViewController.onLoadingStatus(CommonCode.General.UN_NETWORK);
            return;
        }
        pageCount = loadMore ? pageCount + 1 : 0;
        int start = pageCount * Constants.PAGESIZE;

        iViewController.onLoadingStatus(CommonCode.General.DATA_LOADING);
        Call<ResponseData<List<OpusData>>> call = null;
//        if (Type.TYPE_ARTICLE == type) {
//            call = mCircleService.getOpusArticleList(
//                    2001,
//                    categroy + "",
//                    Session.getUserId(),
//                    start,
//                    Constants.PAGESIZE);
//        } else {
        call = mCircleService.getOpusList(
                2004,
                "",
                tempCategory,
                Session.getUserId(),
                type,
                start,
                Constants.PAGESIZE);
//        }
        CallManager.add(call);
//        calls.add(call);
        call.enqueue(new BaseCallback<ResponseData<List<OpusData>>>() {
            @Override
            public void onResponse(Call<ResponseData<List<OpusData>>> call, Response<ResponseData<List<OpusData>>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    ResponseData<List<OpusData>> res = response.body();
                    if (res.getRet() == ReturnCode.SUCCESS) {
                        List<OpusData> opusDatas = res.getData();
                        if (opusDatas != null) {
                            opusDatas = handleData(opusDatas);
                            if (loadMore) {
                                currentData.addAll(opusDatas);
                            } else {
                                currentData = new ArrayList<OpusData>(opusDatas);
                            }
                            if (opusDatas.size() > 0) {
                                iViewController.onLoadingStatus(CommonCode.General.DATA_SUCCESS);
                            } else {
                                iViewController.onLoadingStatus(CommonCode.General.DATA_EMPTY);
                            }
                        } else {
                            iViewController.onLoadingStatus(CommonCode.General.DATA_EMPTY);
                        }
//                        iViewController.updateViewWithLoadMore(opusDatas, loadMore, tempCategory);
                        try {
                            if (!loadMore) {
                                opusDataEntityCache.putListEntityAddTag(opusDatas, tempCategory + "");
                            } else {
                                opusDataEntityCache.putListEntityAddTag(currentData, tempCategory + "");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d("test", "cacheData failed " + e.getMessage());
                        }
                    } else {
                        iViewController.onLoadingStatus(CommonCode.General.ERROR_DATA);
                    }
                } else {
                    iViewController.onLoadingStatus(CommonCode.General.ERROR_DATA);
                }
            }

            @Override
            public void onFailure(Call<ResponseData<List<OpusData>>> call, Throwable t) {
                super.onFailure(call, t);
                iViewController.onLoadingStatus(CommonCode.General.ERROR_DATA);
            }
        });
    }


    /**
     * @param loadMore true 表示下拉加载  false 表示上啦加载
     */
    public void loadData(final boolean loadMore, int type) {
        final String tempCategory = categroy;
        Log.d("yeying", "this is OpusGeneralPresenter" + "loaddata categroy is " + tempCategory);
        if (!NetUtils.isNetworkConnected(BaseApplication.getContext())) {
            iViewController.onLoadingStatus(CommonCode.General.UN_NETWORK);
            return;
        }
        pageCount = loadMore ? pageCount + 1 : 0;
        int start = pageCount * Constants.PAGESIZE;

        iViewController.onLoadingStatus(CommonCode.General.DATA_LOADING);
        Call<ResponseData<List<OpusData>>> call = null;
//        if (Type.TYPE_ARTICLE == type) {
//            call = mCircleService.getOpusArticleList(
//                    2001,
//                    categroy + "",
//                    Session.getUserId(),
//                    start,
//                    Constants.PAGESIZE);
//        } else {
        call = mCircleService.getOpusList(
                2004,
                "",
                tempCategory,
                Session.getUserId(),
                null,
                start,
                Constants.PAGESIZE);
//        }
        CallManager.add(call);
//        calls.add(call);
        call.enqueue(new BaseCallback<ResponseData<List<OpusData>>>() {
            @Override
            public void onResponse(Call<ResponseData<List<OpusData>>> call, Response<ResponseData<List<OpusData>>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    ResponseData<List<OpusData>> res = response.body();
                    if (res.getRet() == ReturnCode.SUCCESS) {
                        List<OpusData> opusDatas = res.getData();
                        if (opusDatas != null) {
                            opusDatas = handleData(opusDatas);
                            if (loadMore) {
                                currentData.addAll(opusDatas);
                            } else {
                                currentData = new ArrayList<OpusData>(opusDatas);
                            }
                            if (opusDatas.size() > 0) {
                                iViewController.onLoadingStatus(CommonCode.General.DATA_SUCCESS);
                            } else {
                                iViewController.onLoadingStatus(CommonCode.General.DATA_EMPTY);
                            }
                        } else {
                            iViewController.onLoadingStatus(CommonCode.General.DATA_EMPTY);
                        }
//                        iViewController.updateViewWithLoadMore(opusDatas, loadMore, tempCategory);
                        try {
                            if (!loadMore) {
                                opusDataEntityCache.putListEntityAddTag(opusDatas, tempCategory + "");
                            } else {
                                opusDataEntityCache.putListEntityAddTag(currentData, tempCategory + "");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d("test", "cacheData failed " + e.getMessage());
                        }
                    } else {
                        iViewController.onLoadingStatus(CommonCode.General.ERROR_DATA);
                    }
                } else {
                    iViewController.onLoadingStatus(CommonCode.General.ERROR_DATA);
                }
            }

            @Override
            public void onFailure(Call<ResponseData<List<OpusData>>> call, Throwable t) {
                super.onFailure(call, t);
                iViewController.onLoadingStatus(CommonCode.General.ERROR_DATA);
            }
        });
    }


    @Override
    public void detachView() {
        mCircleService = null;
//        CallManager.cancelAll();
    }

    private List<OpusData> getTestChannel() {
        List<OpusData> localOpusDatas = new ArrayList<OpusData>();
        for (int i = 0; i < 20; i++) {
            OpusData OpusData = new OpusData();
            localOpusDatas.add(OpusData);
        }
        return localOpusDatas;
    }


    public List<OpusData> getTagCacheData() {
//        return opusDataEntityCache.getListEntityAddTag(OpusData.class, "tag" + tag);
        return null;
    }

    @Override
    public List<OpusData> getCacheData() {
//        return null;
        return opusDataEntityCache.getListEntityAddTag(OpusData.class, categroy + "");
//        List<OpusData> localOpusDatas = new ArrayList<OpusData>();
//        for (int i = 0; i < 20; i++) {
//            OpusData OpusData = new OpusData();
//            localOpusDatas.add(OpusData);
//        }
//        if (type == Type.TYPE_VIDEO && localOpusDatas != null && localOpusDatas.size() > 0) {
//            for (int i = 0; i < localOpusDatas.size(); i++) {
//                if (i % 5 == 0) {
//                    localOpusDatas.get(i).imageUrl = url1;
//                } else if (i % 5 == 1) {
//                    localOpusDatas.get(i).imageUrl = url2;
//                } else if (i % 5 == 2) {
//                    localOpusDatas.get(i).imageUrl = url3;
//                } else if (i % 5 == 3) {
//                    localOpusDatas.get(i).imageUrl = url4;
//                } else {
//                    localOpusDatas.get(i).imageUrl = url5;
//                }
//            }
//        }
//        return localOpusDatas;
    }

    public void searchTagList(final boolean loadMore) {
        Log.d("yeying", "this is OpusGeneralPresenter" + "loaddata categroy is loadMore " + loadMore);
        if (!NetUtils.isNetworkConnected(BaseApplication.getContext())) {
            iViewController.onLoadingStatus(CommonCode.General.UN_NETWORK);
            return;
        }
        iViewController.onLoadingStatus(CommonCode.General.DATA_LOADING);
        Call<ResponseData<List<OpusData>>> call = mCircleService.searchOpusTagInfoList(
                2015,
                Session.getUserId(),
                type,
                tag,
                loadMore ? currentData.size() : 0,
                Constants.PAGESIZE);
        CallManager.add(call);
        call.enqueue(new BaseCallback<ResponseData<List<OpusData>>>() {
            @Override
            public void onResponse(Call<ResponseData<List<OpusData>>> call, Response<ResponseData<List<OpusData>>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    ResponseData<List<OpusData>> res = response.body();
                    if (res.getRet() == ReturnCode.SUCCESS) {
                        List<OpusData> opusDatas = res.getData();
                        if (opusDatas != null && opusDatas.size() > 0) {
                            if (loadMore) {
                                currentData.addAll(opusDatas);
                            } else {
                                currentData = new ArrayList<OpusData>(opusDatas);
                            }
                            iViewController.onLoadingStatus(CommonCode.General.DATA_EMPTY);
                        } else {
                            iViewController.onLoadingStatus(CommonCode.General.DATA_EMPTY);
                        }
                        iViewController.updateViewWithLoadMore(opusDatas, loadMore);
                    } else {
                        iViewController.onLoadingStatus(CommonCode.General.ERROR_DATA);
                    }
                } else {
                    iViewController.onLoadingStatus(CommonCode.General.ERROR_DATA);
                }
            }

            @Override
            public void onFailure(Call<ResponseData<List<OpusData>>> call, Throwable t) {
                super.onFailure(call, t);
                iViewController.onLoadingStatus(CommonCode.General.ERROR_DATA);
            }
        });
    }

    public void loadTagList() {
        if (!NetUtils.isNetworkConnected(BaseApplication.getContext())) {
            iViewController.onLoadingStatus(CommonCode.General.UN_NETWORK);
            return;
        }

        iViewController.onLoadingStatus(CommonCode.General.DATA_LOADING);
        Call<ResponseData<List<OpusTag>>> call = mCircleService.getOpusTag(2004, 1 + "");
        CallManager.add(call);
        call.enqueue(new BaseCallback<ResponseData<List<OpusTag>>>() {
            @Override
            public void onResponse(Call<ResponseData<List<OpusTag>>> call, Response<ResponseData<List<OpusTag>>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    ResponseData<List<OpusTag>> res = response.body();
                    if (res.getRet() == ReturnCode.SUCCESS) {
                        List<OpusTag> opusDatas = res.getData();
                        if (opusDatas != null && opusDatas.size() > 0) {
                            iViewController.onLoadingStatus(CommonCode.General.DATA_SUCCESS);
                        } else {
                            iViewController.onLoadingStatus(CommonCode.General.DATA_EMPTY);
                        }
                        iViewController.updateViewWithLoadMore(opusDatas, false);
//                        try {
//                            if (!loadMore) {
//                                opusDataEntityCache.putListEntityAddTag(opusDatas, categroy + "");
//                            } else {
//                                opusDataEntityCache.putListEntityAddTag(currentData, categroy + "");
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            Log.d("test", "cacheData failed " + e.getMessage());
//                        }
                    } else {
                        iViewController.onLoadingStatus(CommonCode.General.ERROR_DATA);
                    }
                } else {
                    iViewController.onLoadingStatus(CommonCode.General.ERROR_DATA);
                }
            }

            @Override
            public void onFailure(Call<ResponseData<List<OpusTag>>> call, Throwable t) {
                super.onFailure(call, t);
                iViewController.onLoadingStatus(CommonCode.General.ERROR_DATA);
            }
        });
    }

    public void getTransferClubDetail(String clubId) {
        if (!NetUtils.isNetworkConnected(BaseApplication.getContext())) {
            iViewController.onLoadingStatus(CommonCode.General.UN_NETWORK);
            return;
        }

        iViewController.onLoadingStatus(CommonCode.General.DATA_LOADING);
        Call<ResponseData<TransferDetail>> call = mCircleService.transferClubDetail(2001 + "", Session.getUserId(), clubId);
        CallManager.add(call);
        call.enqueue(new BaseCallback<ResponseData<TransferDetail>>() {
            @Override
            public void onResponse(Call<ResponseData<TransferDetail>> call, Response<ResponseData<TransferDetail>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    ResponseData<TransferDetail> res = response.body();
                    if (res.getRet() == ReturnCode.SUCCESS) {
                        TransferDetail opusDatas = res.getData();
                        if (opusDatas != null) {
                            iViewController.onLoadingStatus(CommonCode.General.DATA_SUCCESS);
                        } else {
                            iViewController.onLoadingStatus(CommonCode.General.DATA_EMPTY);
                        }
                        iViewController.updateView(opusDatas);
//                        try {
//                            if (!loadMore) {
//                                opusDataEntityCache.putListEntityAddTag(opusDatas, categroy + "");
//                            } else {
//                                opusDataEntityCache.putListEntityAddTag(currentData, categroy + "");
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            Log.d("test", "cacheData failed " + e.getMessage());
//                        }
                    } else {
                        iViewController.onLoadingStatus(CommonCode.General.ERROR_DATA);
                    }
                } else {
                    iViewController.onLoadingStatus(CommonCode.General.ERROR_DATA);
                }
            }

            @Override
            public void onFailure(Call<ResponseData<TransferDetail>> call, Throwable t) {
                super.onFailure(call, t);
                iViewController.onLoadingStatus(CommonCode.General.ERROR_DATA);
            }
        });
    }


    /**
     * 圈子收益统计
     *
     * @param custId
     * @param clubId
     */
    public void getTransferBound(String custId, String clubId) {
        if (!NetUtils.isNetworkConnected(BaseApplication.getContext())) {
            iViewController.onLoadingStatus(CommonCode.General.UN_NETWORK, "");
            return;
        }

        iViewController.onLoadingStatus(CommonCode.General.DATA_LOADING, "");
        Call<ResponseData<ClubStats>> call = mCircleService.getClubStats(2118 + "", custId, clubId, Session.getUserId());
        CallManager.add(call);
        call.enqueue(new BaseCallback<ResponseData<ClubStats>>() {
            @Override
            public void onResponse(Call<ResponseData<ClubStats>> call, Response<ResponseData<ClubStats>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    ResponseData<ClubStats> res = response.body();
                    if (res.getRet() == ReturnCode.SUCCESS) {
                        ClubStats opusDatas = res.getData();
                        if (opusDatas != null) {
                            iViewController.onLoadingStatus(CommonCode.General.DATA_SUCCESS, "");
                        } else {
                            iViewController.onLoadingStatus(CommonCode.General.DATA_EMPTY, "");
                        }
                        iViewController.updateView(opusDatas);
//                        try {
//                            if (!loadMore) {
//                                opusDataEntityCache.putListEntityAddTag(opusDatas, categroy + "");
//                            } else {
//                                opusDataEntityCache.putListEntityAddTag(currentData, categroy + "");
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            Log.d("test", "cacheData failed " + e.getMessage());
//                        }
                    } else {
                        iViewController.onLoadingStatus(CommonCode.General.ERROR_DATA, "");
                    }
                } else {
                    iViewController.onLoadingStatus(CommonCode.General.ERROR_DATA, "");
                }
            }

            @Override
            public void onFailure(Call<ResponseData<ClubStats>> call, Throwable t) {
                super.onFailure(call, t);
                iViewController.onLoadingStatus(CommonCode.General.ERROR_DATA);
            }
        });
    }

    public void getCircleMember(String appId) {
        if (!NetUtils.isNetworkConnected(BaseApplication.getContext())) {
            iViewController.onLoadingStatus(CommonCode.General.UN_NETWORK);
            return;
        }

        iViewController.onLoadingStatus(CommonCode.General.DATA_LOADING);
        Call<ResponseData<List<CircleMemberModel>>> call = mCircleService.getCircleMember(appId, null);
        CallManager.add(call);
        call.enqueue(new BaseCallback<ResponseData<List<CircleMemberModel>>>() {
            @Override
            public void onResponse(Call<ResponseData<List<CircleMemberModel>>> call, Response<ResponseData<List<CircleMemberModel>>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    ResponseData<List<CircleMemberModel>> res = response.body();
                    if (res.getRet() == ReturnCode.SUCCESS) {
                        List<CircleMemberModel> opusDatas = res.getData();
                        if (opusDatas != null) {
                            iViewController.onLoadingStatus(CommonCode.General.DATA_SUCCESS);
                        } else {
                            iViewController.onLoadingStatus(CommonCode.General.DATA_EMPTY);
                        }

                        List<BaseInfo> friends = new ArrayList<>();
                        for (int i = 0; i < opusDatas.size(); i++) {
                            CircleMemberModel item = opusDatas.get(i);

                            if (null != item && null != item.getUser() && !TextUtils.isEmpty(item.getUser().getCustId())) {
                                BaseInfo baseInfo = new BaseInfo();
                                baseInfo.setCustNname(item.getUser().getCustNname());
                                baseInfo.setCustId(item.getUser().getCustId());
                                baseInfo.setCustImg(item.getUser().getCustImg());
                                baseInfo.setNameNotes(item.getUser().getCustNameNote());
                                friends.add(baseInfo);
                            }
                        }

                        //排序
                        changeLetter(friends);
                        iViewController.updateView(friends);
                    } else {
                        iViewController.onLoadingStatus(CommonCode.General.ERROR_DATA);
                    }
                } else {
                    iViewController.onLoadingStatus(CommonCode.General.ERROR_DATA);
                }
            }

            @Override
            public void onFailure(Call<ResponseData<List<CircleMemberModel>>> call, Throwable t) {
                super.onFailure(call, t);
                iViewController.onLoadingStatus(CommonCode.General.ERROR_DATA);
            }
        });
    }

    /**
     * 对数据进行字母排序，并保存字母的首字母
     *
     * @param friendList
     */
    public void changeLetter(List<BaseInfo> friendList) {
        for (int i = 0; i < friendList.size(); i++) {
            BaseInfo model = friendList.get(i);
            setModelFirstLetter(model);
        }
        Collections.sort(friendList, mPyComparator);
    }

    /**
     * 单个model设置首字母
     *
     * @param model
     */
    public void setModelFirstLetter(BaseInfo model) {
        String mFirstLetter;
        if (!StringUtils.isEmpty(model.getCustNname())) {
            mFirstLetter = mCharacterParser.getSelling(model.getCustNname());
            if (!TextUtils.isEmpty(model.getNameNotes()))
                mFirstLetter = mCharacterParser.getSelling(model.getNameNotes());
        } else {
            mFirstLetter = "#";
        }
        if (!StringUtils.isEmpty(mFirstLetter)) {
            String sortString = mFirstLetter.substring(0, 1).toUpperCase();

            if (sortString.matches("[A-Z]")) {
                model.setFirstLetter(sortString.toUpperCase());
            } else {
                model.setFirstLetter("#");
            }
        } else {
            model.setFirstLetter("#");
        }

    }

}
