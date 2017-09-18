package com.rz.circled.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.msg.SystemMessageService;
import com.netease.nimlib.sdk.msg.constant.SystemMessageType;
import com.rz.circled.R;
import com.rz.circled.adapter.ContactsAdp;
import com.rz.circled.presenter.impl.FriendPresenter1;
import com.rz.circled.ui.fragment.MineFragment;
import com.rz.circled.widget.SideBar;
import com.rz.common.cache.preference.EntityCache;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.IntentCode;
import com.rz.common.constant.Type;
import com.rz.common.event.BaseEvent;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.utils.StringUtils;
import com.rz.common.widget.svp.SVProgressHUD;
import com.rz.common.widget.toasty.Toasty;
import com.rz.httpapi.bean.BaseInfo;
import com.rz.httpapi.bean.FriendRequireModel;
import com.yryz.yunxinim.main.activity.SystemMessageActivity;
import com.yryz.yunxinim.main.activity.TeamListActivity;
import com.yryz.yunxinim.uikit.LoginSyncDataStatusObserver;
import com.yryz.yunxinim.uikit.UIKitLogTag;
import com.yryz.yunxinim.uikit.cache.FriendDataCache;
import com.yryz.yunxinim.uikit.contact.core.item.ItemTypes;
import com.yryz.yunxinim.uikit.uinfo.UserInfoObservable;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 好友列表
 */
public class ContactsAty extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private static final Handler handler = new Handler();

    /**
     * 内容展示
     */
    @BindView(R.id.id_content_listv)
    ListView mListview;

    /**
     * 右侧字母导航栏
     */

    @BindView(R.id.id_sidrbar)
    SideBar mSidebar;

    /**
     * 字母提醒框
     */
    @BindView(R.id.id_letter_dialog)
    TextView mTxtDialog;

    /**
     * 空布局
     */
    @BindView(R.id.layout_none)
    View mLayoutNone;

    @BindView(R.id.id_search_key_rela2)
    View mLayoutSearch2;

    @BindView(R.id.id_search_key_rela1)
    View mLayoutSearch1;

    /**
     * 头布局
     */
    private View mheadView;

    /**
     * 关注我的消息数目提醒
     */
    private TextView mTxtFollowNum;
    /**
     * 群验证消息数目提醒
     */
    private TextView mTvVerifyNum;

    private List<BaseInfo> mSaveAllFriends = new ArrayList<>();

    ContactsAdp mContactsAdp;

    //从哪里进入 0 从通讯录 1从消息
    public static int page = 0;
    private FriendPresenter1 presenter;

    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.aty_contacts, null);
    }

    @Override
    public void initPresenter() {
        presenter = new FriendPresenter1();
        presenter.attachView(this);
    }

    @Override
    public void initView() {
        Log.d("contacts--", page + "---------------");
        setTitleText(getString(R.string.mine_my_contacts));
        if (page == 1) {
            setTitleRightText(R.string.contacts_phone);
        } else {
            setTitleRightText(R.string.add_friends);
        }
        setTitleRightListener(this);
        mLayoutSearch1.setVisibility(View.GONE);
        mLayoutSearch2.setVisibility(View.VISIBLE);
        initHeadView();
        mLayoutSearch2.setOnClickListener(this);
        mContactsAdp = new ContactsAdp(aty, R.layout.adp_contacts);
        mListview.addHeaderView(mheadView);
        mListview.setAdapter(mContactsAdp);
        mListview.setOnItemClickListener(this);

        setFocusNum(Session.getUserFocusNum());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventUpdate(BaseEvent event) {
        if (FriendPresenter1.FRIEND_APPLY_EVENT.equals(event.info)
                || FriendPresenter1.CANCEL_FRIEND_EVENT.equals(event.info)) {
            Log.e("tag", "好友列表更新并缓存");
            presenter.getCacheFriends(false);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(IntentCode.Contacts.Contacts_RESULT_CODE);
        finish();
    }

    /**
     * 加载头布局
     */
    private void initHeadView() {
        mheadView = LayoutInflater.from(aty).inflate(R.layout.head_contacts, null);
        mheadView.findViewById(R.id.id_follow_me_rela).setOnClickListener(this);
        mheadView.findViewById(R.id.id_my_follow_txt).setOnClickListener(this);
        mheadView.findViewById(R.id.id_group_chat_rela).setOnClickListener(this);
        mheadView.findViewById(R.id.id_group_discuss).setOnClickListener(this);
        mheadView.findViewById(R.id.id_verify_message_rela).setOnClickListener(this);
        mheadView.findViewById(R.id.id_black_name_txt).setOnClickListener(this);

        mTxtFollowNum = (TextView) mheadView.findViewById(R.id.id_remind_num_follow_txt);
        mTvVerifyNum = (TextView) mheadView.findViewById(R.id.id_remind_num_verify_message_txt);

        // 设置右侧触摸监听
        mSidebar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                int position = mContactsAdp.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    mListview.setSelection(position);
                }
            }
        });
        mSidebar.setTextView(mTxtDialog);
    }

    @Override
    public void initData() {

        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "reloadWhenDataChanged: contacts onresume");
        //缓存部分新的好友列表，以便进入新的好友列表没有卡顿
       presenter.requestRequireList(false);
       presenter.getCacheFriends(false);
    }

    @Override
    public <T> void updateView(T t) {
        super.updateView(t);
        mLayoutNone.setVisibility(View.GONE);

        if (null == t)
            return;

        showUnreadMsg();

        mSaveAllFriends.clear();
        mSaveAllFriends.addAll((List<BaseInfo>) t);
        mContactsAdp.setData(mSaveAllFriends);
        mContactsAdp.notifyDataSetChanged();
    }

    @Override
    public <T> void updateViewWithFlag(T t, int flag) {
        super.updateViewWithFlag(t, flag);

        if (null == t)
            return;

        List<FriendRequireModel> infos = (List<FriendRequireModel>) t;
        EntityCache<FriendRequireModel> friendRequireModelEntityCache = new EntityCache<>(aty, FriendRequireModel.class);
        friendRequireModelEntityCache.putListEntity(infos);
    }

    private void updateUnreadNum(int unreadCount) {
        // 2.*版本viewholder复用问题
        if (unreadCount > 0) {
            mTvVerifyNum.setVisibility(View.VISIBLE);
            mTvVerifyNum.setText("" + unreadCount);
        } else {
            mTvVerifyNum.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoadingStatus(int loadingStatus, String string) {
        super.onLoadingStatus(loadingStatus, string);
        mLayoutNone.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //关键字搜索
            case R.id.id_search_key_rela2:
                Bundle bundle = new Bundle();
                bundle.putInt("type", Type.custType_3);
                showActivity(aty, ContactsSearchAty.class, bundle);
                break;
            //关注我的
            case R.id.id_follow_me_rela:
                if (checkLogin()) {
                    return;
                }
                Session.setUserFocusNum("");
//                Intent focus = new Intent(MineFrg.MINEFRGFOCUS);
//                sendBroadcast(focus);
//                showActivity(aty, FollowMeAty.class);
                break;
            //我关注的
            case R.id.id_my_follow_txt:
                if (checkLogin()) {
                    return;
                }
//                showActivity(aty, MyFollowAty.class);
                break;
            //群聊
            case R.id.id_group_chat_rela:
                if (checkLogin()) {
                    return;
                }
                TeamListActivity.start(getApplicationContext(), ItemTypes.TEAMS.ADVANCED_TEAM);
                break;
            //讨论组
            case R.id.id_group_discuss:
                if (checkLogin()) {
                    return;
                }
                TeamListActivity.start(getApplicationContext(), ItemTypes.TEAMS.NORMAL_TEAM);
                break;
            //验证消息
            case R.id.id_verify_message_rela:
                if (checkLogin()) {
                    return;
                }
                SystemMessageActivity.start(getApplicationContext(), true);
                break;
            //黑名单
            case R.id.id_black_name_txt:
                if (checkLogin()) {
                    return;
                }
//                showActivity(aty, BlackListAty.class);
                break;
            //添加好友
//            case R.id.titlebar_right_text:
//                showActivity(aty, AddContactsAty.class);
//                break;
            default:
                break;
        }
    }

    private boolean checkLogin() {
        if (!Session.getUserIsLogin() || NIMClient.getStatus() != StatusCode.LOGINED) {
//        if (!isLogin()) {
            Toasty.info(mContext, getString(R.string.im_link_error_hint)).show();
            return true;
        }
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (i - 1 < 0) {
            return;
        }
        BaseInfo item = (BaseInfo) mContactsAdp.getItem(i - 1);
        if (page == 1) {
            if (null != item && !StringUtils.isEmpty(item.getCustId())) {
            } else {
                SVProgressHUD.showErrorWithStatus(this, mContext.getString(R.string.user_not_exsit));
            }
        } else {
            /**
             * 进入好友详情
             */
            if (i > 0) {
                if (item != null) {
//                    FriendInfoAty.newFrindInfo(aty, item.getCustId());
                }
            }
        }
    }

    public void setFocusNum(String focusNum) {
        if (!StringUtils.isEmpty(focusNum)) {
            mTxtFollowNum.setVisibility(View.VISIBLE);
            long num = Long.parseLong(focusNum);
            mTxtFollowNum.setText(num > 99 ? "99+" : (num + ""));
        } else {
            mTxtFollowNum.setVisibility(View.GONE);
        }
    }

    private void showUnreadMsg() {
        List<SystemMessageType> types = new ArrayList<>();
        types.add(SystemMessageType.ApplyJoinTeam);
        types.add(SystemMessageType.DeclineTeamInvite);
        types.add(SystemMessageType.RejectTeamApply);
        types.add(SystemMessageType.TeamInvite);
        types.add(SystemMessageType.undefined);
        int unreadCount = NIMClient.getService(SystemMessageService.class).querySystemMessageUnreadCountByType(types);
        updateUnreadNum(unreadCount);
    }

    protected final Handler getHandler() {
        return handler;
    }

}
