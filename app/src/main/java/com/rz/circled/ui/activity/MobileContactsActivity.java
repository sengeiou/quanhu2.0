package com.rz.circled.ui.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.rz.circled.R;
import com.rz.circled.adapter.LetterContactsAdapter;
import com.rz.circled.presenter.IPresenter;
import com.rz.circled.presenter.impl.FriendPresenter1;
import com.rz.circled.widget.SideBar;
import com.rz.common.constant.H5Address;
import com.rz.common.constant.Type;
import com.rz.common.event.BaseEvent;
import com.rz.common.permission.AfterPermissionGranted;
import com.rz.common.permission.AppSettingsDialog;
import com.rz.common.permission.EasyPermissions;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.utils.DialogUtils;
import com.rz.httpapi.bean.FriendInformationBean;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MobileContactsActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    @BindView(R.id.id_content_listv)
    ListView mListview;
    @BindView(R.id.layout_main)
    LinearLayout mLayoutMain;
    @BindView(R.id.layout_nopress)
    LinearLayout mLayoutNoPress;
    @BindView(R.id.id_sidrbar)
    SideBar mSidebar;
    @BindView(R.id.id_letter_dialog)
    TextView mTxtDialog;

    /**
     * 所有被匹配过的本地通讯录好友
     */
    private List<FriendInformationBean> mMoblieAllFriends = new ArrayList<>();


    private Dialog dialog;
    protected IPresenter presenter;
    private LetterContactsAdapter mAdapter;

    @Nullable
    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.layout_my_follow, null);
    }

    @Override
    public void initPresenter() {
        presenter = new FriendPresenter1();
        presenter.attachView(this);
    }

    @Override
    public void initView() {
        mLayoutMain.setVisibility(View.VISIBLE);
        mLayoutNoPress.setVisibility(View.GONE);
        setTitleText(getString(R.string.phone_contacts));
        findViewById(R.id.id_search_key_rela).setOnClickListener(this);
        mAdapter = new LetterContactsAdapter(aty, R.layout.item_mobile_contacts);
        mListview.setAdapter(mAdapter);
        mListview.setOnItemClickListener(this);
        mSidebar.setTextView(mTxtDialog);
        // 设置右侧触摸监听
        mSidebar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                int position = mAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    mListview.setSelection(position);
                }
            }
        });
    }

    @Override
    public <T> void updateView(T t) {
        super.updateView(t);

        mMoblieAllFriends.clear();
        mMoblieAllFriends.addAll((List<FriendInformationBean>) t);
        mAdapter.setData(mMoblieAllFriends);
        mAdapter.notifyDataSetChanged();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventUpdate(final BaseEvent event) {
        if (FriendPresenter1.FRIEND_INVITE_EVENT == event.getType()) {
            if (dialog != null && dialog.isShowing())
                return;

            View dialogView = LayoutInflater.from(aty).inflate(R.layout.comm_dialog, null);
            dialog = DialogUtils.selfDialog(aty, dialogView, false);
            dialog.show();
            ((TextView) dialogView.findViewById(R.id.id_tv_message)).setText(R.string.un_quanhu_user_invite_register);
            dialogView.findViewById(R.id.id_tv_confirm).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FriendInformationBean info = (FriendInformationBean) event.getData();
                    Uri uri = Uri.parse("smsto:" + info.getCustPhone());
                    Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);
                    //                    sendIntent.putExtra("sms_body",
//                            "我在使用好看、好玩、又能赚钱的“圈乎”APP，这是一个好友越多，越好玩，越赚钱的平台！现分享并邀请你加入“圈乎”，请在手机应用商店中下载“圈乎”。\n"
//                                    + "我在“圈乎”的昵称是" + Session.getUserName() + "，你可在“圈乎---我的---通讯录---添加好友”中搜索我的昵称，添加我为好友。\n"
//                                    + "记得也去邀请你的好友来加入“圈乎”哦！\n"
//                                    + "让我们在娱乐中一起去赚钱吧！");
                    sendIntent.putExtra("sms_body",
                            String.format(mContext.getString(R.string.at_add_download), H5Address.APP_DOWNLOAD));
                    startActivity(sendIntent);
                    dialog.dismiss();
                }
            });
            dialogView.findViewById(R.id.id_tv_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
        } else if (FriendPresenter1.FRIEND_CONTACTS_EVENT == event.getType()) {
            ((FriendPresenter1) presenter).getCacheFriends(true);
        } else if (FriendPresenter1.FRIEND_BACKBROUND_EVENT == event.getType()) {
            Log.e("tag", "通讯录刷新");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ((FriendPresenter1) presenter).queryAndFilterContacts();
                }
            }, 500);
        }
    }

    @AfterPermissionGranted(RC_LOCATION_CONTACTS_PERM)
    @Override
    public void initData() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.READ_CONTACTS)) {
            ((FriendPresenter1) presenter).queryAndFilterContacts();
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.contacts_permissiongs_run), RC_LOCATION_CONTACTS_PERM, Manifest.permission.READ_CONTACTS);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //关键字搜索
            case R.id.id_search_key_rela:
                Bundle bundle = new Bundle();
                bundle.putInt("type", Type.custType_0);
                showActivity(aty, ContactsSearchAty.class, bundle);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        /**
         * 进入好友详情
         */
        FriendInformationBean item = mAdapter.getItem(i);
        if (item.getRelation() == Type.relation_friend) {
            UserInfoActivity.newFrindInfo(mContext, item.getCustId());
        } else {
            ((FriendPresenter1) presenter).requestSaveFriendByFriend(item);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        super.onPermissionsGranted(requestCode, perms);
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            mLayoutMain.setVisibility(View.GONE);
            mLayoutNoPress.setVisibility(View.VISIBLE);
            new AppSettingsDialog.Builder(this, getString(R.string.contacts_permissiongs_run))
                    .setPositiveButton(getString(R.string.setting))
                    .setNegativeButton(getString(R.string.cancel), null /* click listener */)
                    .setRequestCode(RC_SETTINGS_SCREEN)
                    .build()
                    .show();
        }
    }

    @Override
    public void refreshPage() {

    }
}

