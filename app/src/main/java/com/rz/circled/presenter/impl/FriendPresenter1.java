package com.rz.circled.presenter.impl;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;

import com.j256.ormlite.table.TableUtils;
import com.rz.circled.R;
import com.rz.circled.db.DBHelper;
import com.rz.circled.db.dao.FriendInformationDao;
import com.rz.circled.event.ResponseDataEven;
import com.rz.circled.http.HandleRetCode;
import com.rz.circled.presenter.GeneralPresenter;
import com.rz.circled.widget.pinyin.CharacterParser;
import com.rz.circled.widget.pinyin.PinyinComparator;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.CommonCode;
import com.rz.common.constant.Constants;
import com.rz.common.constant.Type;
import com.rz.common.event.BaseEvent;
import com.rz.common.ui.inter.IViewController;
import com.rz.common.utils.NetUtils;
import com.rz.common.utils.StringUtils;
import com.rz.common.widget.svp.SVProgressHUD;
import com.rz.common.widget.toasty.Toasty;
import com.rz.httpapi.api.ApiService;
import com.rz.httpapi.api.BaseCallback;
import com.rz.httpapi.api.CallManager;
import com.rz.httpapi.api.Http;
import com.rz.httpapi.api.ResponseData.ResponseData;
import com.rz.httpapi.bean.FriendInformationBean;
import com.rz.httpapi.bean.FriendRelationModel;
import com.rz.httpapi.bean.FriendRequireModel;
import com.rz.httpapi.bean.RequireFriendByPhoneModel;
import com.rz.httpapi.constans.ReturnCode;

import org.greenrobot.eventbus.EventBus;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by xiayumo on 16/9/2.
 * <p/>
 * 由于接口返回数据存在相互包含，创建三张表
 * 分别保存黑名单，我关注的，好友以及这三种
 * 数据的相互状态变化，通用的model是BaseInfo
 * 进入数据库分别对应三种model，优先获取缓存
 * 然后同步，关注取关还有黑名单先上报然后通过
 * 数据库操作成功后通过消息机制刷新页面
 */
public class FriendPresenter1 extends GeneralPresenter {

<<<<<<< HEAD
=======
    private ApiService service;
    private IViewController mView;
    private Context mContext;
    private FriendInformationDao friendInformationDao;
    private CharacterParser mCharacterParser;
    private PinyinComparator mPyComparator;

    /**
     * 当前刷新到UI的好友列表
     */
    private List<FriendInformationBean> mSaveAllFriends = new ArrayList<>();

    /**
     * 粉丝列表分页
     */
    int start = 0;
    int limit = Constants.PAGESIZE;

>>>>>>> 2540931ec03580503cb88e4fe7ef18497de3b69c
    /**
     *  通知好友页面刷新
     */
    public static final String FRIEND_EVENT = "5";
    /**
     *  通知好友申请页面刷新
     */
    public static final String FRIEND_APPLY_EVENT = "4";
    /**
     * 详情页面关注成功及取消关注
     */
    public static final String CANCEL_FRIEND_EVENT = "7";
    /**
     * 举报
     */
    public static final String REPORT_EVENT = "11";
    /**
     * 手机通讯录邀请
     */
    public static final String FRIEND_INVITE_EVENT = "12";
    /**
     * 后台插入数据标示
     */
    public static final String FRIEND_BACKBROUND_EVENT = "13";
    /**
     * 通讯录里面的好友
     */
    public static final String FRIEND_CONTACTS_EVENT = "14";
    /**
     * 通讯录里面修改备注
     *
     * @param view
     */
    public static final String FRIEND_REMARK_EVENT = "15";
    /**
     * 粉丝列表分页
     */
    int start = 0;
    int limit = Constants.PAGESIZE;
    private ApiService service;
    private IViewController mView;
    private Context mContext;
    private FriendInformationDao friendInformationDao;
    private CharacterParser mCharacterParser;
    private PinyinComparator mPyComparator;
    /**
     * 当前刷新到UI的好友列表
     */
    private List<FriendInformationBean> mSaveAllFriends = new ArrayList<>();
    private long lastClickTime;
    private String currentUserId;
    private String custPhone;

    @Override
    public void attachView(IViewController view) {

        this.mView = view;

        service = Http.getApiService(ApiService.class);
        mCharacterParser = CharacterParser.getInstance();
        mPyComparator = new PinyinComparator();
        mContext = getContext(mView);
        friendInformationDao = new FriendInformationDao(mContext);
    }

    @Override
    public void detachView() {

    }

    @Override
    public Object getCacheData() {
        return null;
    }

    public void getCacheFriends(boolean isBackStage) {

        /**
         * 初次加载的策略是通过缓存判断出来的
         */
        List<FriendInformationBean> mLocalAllFriends = new ArrayList<>();
        mLocalAllFriends.addAll(friendInformationDao.queryForAll());
        Log.e("tag", "查询当前缓存数据 size=" + mLocalAllFriends.size());
        if (!mLocalAllFriends.isEmpty()) {
            mSaveAllFriends.clear();
            mSaveAllFriends.addAll(mLocalAllFriends);
            mView.updateView(mSaveAllFriends);
            requestMyFriends(mLocalAllFriends, isBackStage);
        } else {
            requestMyFriends(mLocalAllFriends, isBackStage);
        }
    }

    /**
     * 获取所有的好友并同步缓存
     *
     * @param mLocalAllFriends
     */
    private synchronized void requestMyFriends(final List<FriendInformationBean> mLocalAllFriends, final boolean isBackStage) {

        if (!NetUtils.isNetworkConnected(mContext)) {
            SVProgressHUD.showErrorWithStatus(mContext, mContext.getString(R.string.no_net_work));
            return;
        }

        Call<ResponseData<List<FriendInformationBean>>> call = service
                .friendList(Session.getUserId());

        CallManager.add(call);
        call.enqueue(new BaseCallback<ResponseData<List<FriendInformationBean>>>() {
            @Override
            public void onResponse(Call<ResponseData<List<FriendInformationBean>>> call, Response<ResponseData<List<FriendInformationBean>>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
//                    mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS_FULL);
                    ResponseData<List<FriendInformationBean>> res = response.body();
                    if (res.getRet() == ReturnCode.SUCCESS) {
                        List<FriendInformationBean> dataList = res.getData();

                        if (dataList == null) {
                            return;
                        }

                        if (dataList.size() > 0) {

                            //排序
                            changeLetter(dataList);

                            /**
                             * 删除当前类型的好友列表，并插入最新的好友列表
                             * 注意逻辑顺序，必须先删除，然后插入最新匹配数据
                             */
                            friendInformationDao.delete(mLocalAllFriends);
                            friendInformationDao.save(dataList);

                            if (!isBackStage) {
                                /**
                                 * 清除内存里的所有好友，刷新ui
                                 */

                                mSaveAllFriends.clear();

                                mSaveAllFriends.addAll(dataList);

                                mView.updateView(mSaveAllFriends);

                            } else {

                                BaseEvent event7 = new BaseEvent();
                                event7.info = FRIEND_BACKBROUND_EVENT;
                                EventBus.getDefault().post(event7);

                            }

                        } else {
                            if (!isBackStage) {
                                /**
                                 * 服务器清除好友，刷新空列表
                                 */
                                mSaveAllFriends.clear();
                                mView.updateView(mSaveAllFriends);
                                mView.onLoadingStatus(CommonCode.General.DATA_LACK, res.getMsg());
                                try {
                                    TableUtils.dropTable(DBHelper.getHelper(mContext).getConnectionSource(), FriendInformationBean.class, true);
                                    TableUtils.createTable(DBHelper.getHelper(mContext).getConnectionSource(), FriendInformationBean.class);
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                    } else if (res.getRet() == ReturnCode.FAIL_REMIND_1) {
                        mView.onLoadingStatus(CommonCode.General.ERROR_DATA, res.getMsg());

                    }
                } else {
                    mView.onLoadingStatus(CommonCode.General.ERROR_DATA);

                }
            }

            @Override
            public void onFailure(Call<ResponseData<List<FriendInformationBean>>> call, Throwable t) {
                super.onFailure(call, t);
                mView.onLoadingStatus(CommonCode.General.ERROR_DATA);

            }
        });

    }

    /**
     * 根据关键字搜索陌生人列表
     *
     * @param keyword
     * @param loadMore
     */
    public void searchFriend(String keyword, boolean loadMore) {

        if (!NetUtils.isNetworkConnected(mContext)) {
            SVProgressHUD.showErrorWithStatus(mContext, mContext.getString(R.string.no_net_work));
            return;
        }
//        mView.onLoadingStatus(CommonCode.General.DATA_LOADING, "正在加载");
        if (loadMore) {
            start += limit;
        } else {
            start = 0;
            mSaveAllFriends.clear();
        }
        Call<ResponseData<List<FriendInformationBean>>> call = service
                .searchFriend(Session.getUserId(), keyword, start, limit);
        CallManager.add(call);
        call.enqueue(new BaseCallback<ResponseData<List<FriendInformationBean>>>() {
            @Override
            public void onResponse(Call<ResponseData<List<FriendInformationBean>>> call, Response<ResponseData<List<FriendInformationBean>>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
//                    mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS_FULL);
                    ResponseData<List<FriendInformationBean>> res = response.body();
                    if (res.getRet() == ReturnCode.SUCCESS) {

                        List<FriendInformationBean> dataList = res.getData();

                        mSaveAllFriends.addAll(dataList);

                        mView.updateView(mSaveAllFriends);

                    } else if (res.getRet() == ReturnCode.FAIL_REMIND_1) {

                        mView.onLoadingStatus(CommonCode.General.ERROR_DATA, res.getMsg());
                    }
                } else {
                    mView.onLoadingStatus(CommonCode.General.ERROR_DATA);
                }
            }

            @Override
            public void onFailure(Call<ResponseData<List<FriendInformationBean>>> call, Throwable t) {
                super.onFailure(call, t);
                mView.onLoadingStatus(CommonCode.General.ERROR_DATA);

            }
        });

    }

    /**
     * 查询数据库对本地通讯录进行过滤
     */
    public void queryAndFilterContacts() {
        /**
         * 从三个表里查询三种数据后合并，进入通讯录匹配
         */
        List<FriendInformationBean> filterList = new ArrayList<>();
//        mView.onLoadingStatus(CommonCode.General.DATA_LOADING, "正在加载");
        /**
         * 注意顺序，好友包含于我关注，过滤的时候可以覆盖我关注的
         */
        filterList.addAll(friendInformationDao.queryForAll());
        try {
            Cursor cursor = mContext.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null, null, null, ContactsContract.CommonDataKinds.Phone.SORT_KEY_PRIMARY);
            List<FriendInformationBean> dataList = new ArrayList<>();
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor
                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

                String number = cursor.getString(cursor
                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                number = number.replaceAll("[^0-9]", "");
                number = number.replaceAll(" ", "");

                if (number.length() >= 11)
                    number = number.substring(number.length() - 11, number.length());
                else
                    continue;

                if (!StringUtils.isMobile(number.trim()) || TextUtils.equals(number.trim(), Session.getUserPhone()))
                    continue;

                FriendInformationBean info = new FriendInformationBean();

                /**
                 * type0是从未被操作的本地通讯录好友
                 */
                info.setCustNname(name);
                info.setCustPhone(number.replace(" ", ""));
                info.setRelation(Type.relation_stranger);

                for (FriendInformationBean friend : filterList) {
                    if ((number.replace(" ", "")).equals(friend.getCustPhone())) {
                        Log.e("zxw", "custNname=" + friend.getCustNname() + "  relation=" + friend.getRelation());
                        info.setCustPhone(number);
                        info.setCustImg(friend.getCustImg());
                        info.setCustId(friend.getCustId());
                        info.setCustNname(friend.getCustNname());
                        info.setRelation(Type.relation_friend);

                        if (!StringUtils.isEmpty(friend.getCustNname())) {
                            info.setCustNname(name + "(" + friend.getCustNname() + ")");
                        }
                        if (!StringUtils.isEmpty(friend.getCustSignature())) {
                            info.setCustSignature(friend.getCustSignature());
                        }
                    }
                }

                dataList.add(info);
            }
            cursor.close();
            changeLetter(dataList);
            mSaveAllFriends.clear();
            mSaveAllFriends.addAll(dataList);
            mView.updateView(mSaveAllFriends);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void requireFriend(String fid, int type) {
        requireFriend(fid, "", type);
    }

    public void requireFriend(String fid, String msg, int type) {
        requireFriend(fid, msg, 0, type);
    }

    public void requireFriend(String fid, int rid, int type) {
        requireFriend(fid, "", rid, type);
    }

    /**
     * 加好友（申请、同意、拒绝）
     *
     * @param fid  加好友接受者custId（是）
     * @param msg  加好友对应的请求消息，最长256字符（否）
     * @param type 1申请加好友，2同意加好友，3拒绝加好友（是）
     * @param rid  请求ID，用于同意、拒绝申请时传参（否）
     */
    public void requireFriend(final String fid, String msg, int rid, final int type) {

        if (!NetUtils.isNetworkConnected(mContext)) {
            SVProgressHUD.showErrorWithStatus(mContext, mContext.getString(R.string.no_net_work));
            return;
        }

        if (isFastClick(2000)) {
            if (TextUtils.equals(currentUserId, fid)) {
                currentUserId = fid;
                return;
            }
        }

        currentUserId = fid;

        Call<ResponseData> call = service
                .requireFriend(Session.getUserId(), fid, msg, type, rid);

        CallManager.add(call);
        call.enqueue(new BaseCallback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call,
                                   Response<ResponseData> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS);
                    ResponseData<FriendRelationModel> res = response.body();
                    if (res.getRet() == ReturnCode.SUCCESS) {
                        BaseEvent event = new BaseEvent();
                        switch (type) {
                            case CommonCode.requireFriend.require_type_add:
                                event.info = FRIEND_EVENT;
                                break;
                            case CommonCode.requireFriend.require_type_agree:
                            case CommonCode.requireFriend.require_type_refuse:
                                event.info = FRIEND_APPLY_EVENT;
                                break;
                            default:
                                break;
                        }
//                        event.infoId = fid;
//                        EventBus.getDefault().post(event);
                    } else if (res.getRet() == ReturnCode.FAIL_REMIND_1) {
                        mView.onLoadingStatus(CommonCode.General.ERROR_DATA, res.getMsg());
                    }
                } else {
                    mView.onLoadingStatus(CommonCode.General.ERROR_DATA);
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                super.onFailure(call, t);
                mView.onLoadingStatus(CommonCode.General.ERROR_DATA);
            }
        });
    }

    /**
     * 申请加好友列表
     */
    public void requestRequireList(final boolean loadMore) {
        if (!NetUtils.isNetworkConnected(mContext)) {
            SVProgressHUD.showErrorWithStatus(mContext, mContext.getString(R.string.no_net_work));
            return;
        }

        Call<ResponseData<List<FriendRequireModel>>> call = service
                .requireList(Session.getUserId(), Constants.PAGESIZE_MAX, start);
        CallManager.add(call);
        call.enqueue(new BaseCallback<ResponseData<List<FriendRequireModel>>>() {
            @Override
            public void onResponse(Call<ResponseData<List<FriendRequireModel>>> call, Response<ResponseData<List<FriendRequireModel>>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {

                    if (loadMore) {
                        start += Constants.PAGESIZE_MAX;
                    } else {
                        start = 0;
                    }

                    ResponseData<List<FriendRequireModel>> res = response.body();
                    if (res.getRet() == ReturnCode.SUCCESS) {

                        List<FriendRequireModel> dataList = res.getData();

                        mView.updateViewWithFlag(dataList, 0);

                        if (loadMore && dataList.size() == Constants.PAGESIZE_MAX) {
                            requestRequireList(loadMore);
                        }

                    } else if (res.getRet() == ReturnCode.FAIL_REMIND_1) {

//                        mView.onLoadingStatus(CommonCode.General.ERROR_DATA, res.getMsg());

                    }
                } else {

//                    mView.onLoadingStatus(CommonCode.General.ERROR_DATA, "加载失败");

                }
            }

            @Override
            public void onFailure(Call<ResponseData<List<FriendRequireModel>>> call, Throwable t) {
                super.onFailure(call, t);
                mView.onLoadingStatus(CommonCode.General.ERROR_DATA);
            }
        });

    }

    /**
     * 删除申请加好友信息
     */
    public void deteleRequireFriend(final Integer rid) {
        if (!NetUtils.isNetworkConnected(mContext)) {
            SVProgressHUD.showErrorWithStatus(mContext, mContext.getString(R.string.no_net_work));
            return;
        }

        Call<ResponseData> call = service
                .requireDetele(Session.getUserId(), rid);

        call.enqueue(new BaseCallback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS);
                    ResponseData res = response.body();
                    if (res.getRet() == ReturnCode.SUCCESS) {
                        mView.updateView(rid);
                    } else if (res.getRet() == ReturnCode.FAIL_REMIND_1) {
                        mView.onLoadingStatus(CommonCode.General.ERROR_DATA, res.getMsg());
                    }
                } else {
                    mView.onLoadingStatus(CommonCode.General.ERROR_DATA);
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                super.onFailure(call, t);
                mView.onLoadingStatus(CommonCode.General.ERROR_DATA);
            }
        });
    }

    /**
     * 保存备注
     *
     * @param fid
     */
    public void saveFriendNote(String fid, final String remark) {

        if (!NetUtils.isNetworkConnected(mContext)) {
            mView.onLoadingStatus(CommonCode.General.UN_NETWORK, mContext.getString(R.string.no_net_work));
            return;
        }
        Call<ResponseData> call = service
                .remarkFriend(1070, Session.getUserId(), fid, remark);

        CallManager.add(call);
        call.enqueue(new BaseCallback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                super.onResponse(call, response);
                System.out.println("sssss");
                if (response.isSuccessful()) {
                    ResponseData<FriendInformationBean> res = response.body();
                    if (res.getRet() == ReturnCode.SUCCESS) {

                        mView.updateView(remark);

                    } else if (res.getRet() == ReturnCode.FAIL_REMIND_1) {

                        SVProgressHUD.showErrorWithStatus(mContext, mContext.getString(R.string.remark_fail));

                    } else if (res.getRet() == ReturnCode.FAIL_REMIND_2) {

                        SVProgressHUD.showErrorWithStatus(mContext, mContext.getString(R.string.remark_fail));
                    }
                } else {

                    SVProgressHUD.showErrorWithStatus(mContext, mContext.getString(R.string.remark_fail));
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                super.onFailure(call, t);
                SVProgressHUD.showErrorWithStatus(mContext, mContext.getString(R.string.remark_fail));

            }
        });
    }


    /**
     * 删除好友
     */
    public void deteleFriend(final String fid) {
        if (!NetUtils.isNetworkConnected(mContext)) {
            SVProgressHUD.showErrorWithStatus(mContext, mContext.getString(R.string.no_net_work));
            return;
        }

        Call<ResponseData> call = service
                .friendDetele(Session.getUserId(), fid);

        call.enqueue(new BaseCallback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS);
                    ResponseData res = response.body();
                    if (res.getRet() == ReturnCode.SUCCESS) {
                        /**
                         * 取消好友通知好友详情更新
                         */
                        BaseEvent event1 = new BaseEvent();
                        event1.info = CANCEL_FRIEND_EVENT;
                        EventBus.getDefault().post(event1);

                    } else if (res.getRet() == ReturnCode.FAIL_REMIND_1) {
                        mView.onLoadingStatus(CommonCode.General.ERROR_DATA, res.getMsg());
                    }
                } else {
                    mView.onLoadingStatus(CommonCode.General.ERROR_DATA, mContext.getString(R.string.action_fail));
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                super.onFailure(call, t);
                mView.onLoadingStatus(CommonCode.General.ERROR_DATA);
            }
        });
    }

    /**
     * 只针对本地通讯录的加关注，分为非平台加关注，平台陌生人加关注以及粉丝加关注
     *
     * @param info
     */
    public void requestSaveFriendByFriend(final FriendInformationBean info) {

        if (!NetUtils.isNetworkConnected(mContext)) {
            SVProgressHUD.showErrorWithStatus(mContext, mContext.getString(R.string.no_net_work));
            return;
        }

        if (isFastClick(2000)) {
            if (TextUtils.equals(custPhone, info.getCustPhone())) {
                custPhone = info.getCustPhone();
                return;
            } else {
                custPhone = info.getCustPhone();
            }
        } else {
            custPhone = info.getCustPhone();
        }

        Call<ResponseData<RequireFriendByPhoneModel>> call = service.requireFriendByPhone(Session.getUserId(), info.getCustPhone());
        CallManager.add(call);
        call.enqueue(new Callback<ResponseData<RequireFriendByPhoneModel>>() {
            @Override
            public void onResponse(Call<ResponseData<RequireFriendByPhoneModel>> call, Response<ResponseData<RequireFriendByPhoneModel>> response) {
                if (response.isSuccessful()) {
                    ResponseData<RequireFriendByPhoneModel> res = response.body();
                    if (res.getRet() == ReturnCode.SUCCESS) {
                        RequireFriendByPhoneModel data = res.getData();
                        if (null != data && data.getCode() == -1) {

                            /**
                             * 关注跟平台不相关的人，比如手机本地联系人，关注不成功返回－1
                             */
                            BaseEvent event7 = new BaseEvent();
                            event7.info = FRIEND_INVITE_EVENT;
                            event7.data = info;
                            EventBus.getDefault().post(event7);

                        } else {

                            Toasty.info(mContext, mContext.getString(R.string.inval_sucess)).show();
                            BaseEvent event = new BaseEvent();
                            event.info = FRIEND_EVENT;
                            EventBus.getDefault().post(event);

                        }
                    } else if (res.getRet() == ReturnCode.FAIL_REMIND_1) {
                        mView.onLoadingStatus(CommonCode.General.ERROR_DATA, res.getMsg());
                    } else {
                        mView.onLoadingStatus(CommonCode.General.ERROR_DATA, mContext.getString(R.string.action_fail));
                    }
                } else {
                    mView.onLoadingStatus(CommonCode.General.ERROR_DATA, mContext.getString(R.string.action_fail));
                }
            }

            @Override
            public void onFailure(Call<ResponseData<RequireFriendByPhoneModel>> call, Throwable t) {
                mView.onLoadingStatus(CommonCode.General.ERROR_DATA, mContext.getString(R.string.action_fail));
            }
        });

    }

    /**
     * 根据好友id获取好友详情
     *
     * @param fid
     */
    public void getFriendInfoDetail(String fid) {

        if (!NetUtils.isNetworkConnected(mContext)) {
//            SVProgressHUD.showErrorWithStatus(mContext, mContext.getString(R.string.no_net_work));
            mView.onLoadingStatus(CommonCode.General.UN_NETWORK);
            return;
        }
        mView.onLoadingStatus(CommonCode.General.DATA_LOADING);
        Call<ResponseData<FriendInformationBean>> call = service
                .getFriendDetail(1003, Session.getUserId(), fid);

        CallManager.add(call);
        call.enqueue(new BaseCallback<ResponseData<FriendInformationBean>>() {
            @Override
            public void onResponse(Call<ResponseData<FriendInformationBean>> call, Response<ResponseData<FriendInformationBean>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    ResponseData<FriendInformationBean> res = response.body();
                    if (res.getRet() == ReturnCode.SUCCESS) {
                        mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS);
                        FriendInformationBean model = res.getData();
                        mView.updateView(model);
                    } else if (HandleRetCode.handler(mContext, res)) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mView.onLoadingStatus(CommonCode.General.ERROR_DATA);
                            }
                        }, 2000);
                    }
                } else {
                    mView.onLoadingStatus(CommonCode.General.ERROR_DATA);
                }
            }

            @Override
            public void onFailure(Call<ResponseData<FriendInformationBean>> call, Throwable t) {
                super.onFailure(call, t);
                mView.onLoadingStatus(CommonCode.General.ERROR_DATA);
            }
        });
    }

    /**
     * 根据好友id获取好友圈子图片
     *
     * @param custId
     */
    public void getFriendInfoImgs(String custId) {

        if (!NetUtils.isNetworkConnected(mContext)) {
            SVProgressHUD.showErrorWithStatus(mContext, mContext.getString(R.string.no_net_work));
            return;
        }
//        mView.onLoadingStatus(CommonCode.General.DATA_LOADING, "正在加载");
        Call<ResponseData<String[]>> call = Http.getApiService(ApiService.class)
                .getCircleImgs(2301, custId, 4);

        CallManager.add(call);
        call.enqueue(new BaseCallback<ResponseData<String[]>>() {
            @Override
            public void onResponse(Call<ResponseData<String[]>> call, Response<ResponseData<String[]>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
//                    mView.onLoadingStatus(CommonCode.General.DATA_SUCCESS_FULL);
                    ResponseData<String[]> res = response.body();
                    if (res.getRet() == ReturnCode.SUCCESS) {

                        ResponseDataEven event = new ResponseDataEven();
                        event.responseData = res;
                        event.act = 2301;
                        EventBus.getDefault().post(event);

                    } else if (res.getRet() == ReturnCode.FAIL_REMIND_1) {
                        mView.onLoadingStatus(CommonCode.General.ERROR_DATA, res.getMsg());
                    }
                } else {
                    mView.onLoadingStatus(CommonCode.General.ERROR_DATA);
                }
            }

            @Override
            public void onFailure(Call<ResponseData<String[]>> call, Throwable t) {
                super.onFailure(call, t);
                mView.onLoadingStatus(CommonCode.General.ERROR_DATA);

            }
        });
    }

    /**
     * 对数据进行字母排序，并保存字母的首字母
     *
     * @param friendList
     */
    public void changeLetter(List<FriendInformationBean> friendList) {
        for (int i = 0; i < friendList.size(); i++) {
            FriendInformationBean model = friendList.get(i);
            setModelFirstLetter(model);
        }
        Collections.sort(friendList, mPyComparator);
    }

    /**
     * 单个model设置首字母
     *
     * @param model
     */
    public void setModelFirstLetter(FriendInformationBean model) {
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

    private boolean isFastClick(long timeMillis) {
        long time = System.currentTimeMillis();
        Log.d("yeying", "time " + time);
        Log.d("yeying", "lastClickTime " + lastClickTime);
        if (time - lastClickTime < timeMillis) {
            return true;
        }
        Log.d("yeying", "time " + time);
        lastClickTime = time;
        return false;
    }
}
