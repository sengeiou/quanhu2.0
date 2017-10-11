package com.yryz.yunxinim.session.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.team.constant.TeamFieldEnum;
import com.netease.nimlib.sdk.uinfo.UserInfoProvider;
import com.rz.common.cache.preference.Session;
import com.rz.common.utils.DialogUtils;
import com.yryz.yunxinim.DemoCache;
import com.yryz.yunxinim.R;
import com.yryz.yunxinim.contact.activity.UserProfileActivity;
import com.yryz.yunxinim.team.TeamCreateHelper;
import com.yryz.yunxinim.uikit.Constants;
import com.yryz.yunxinim.uikit.NimUIKit;
import com.yryz.yunxinim.uikit.cache.FriendDataCache;
import com.yryz.yunxinim.uikit.cache.NimUserInfoCache;
import com.yryz.yunxinim.uikit.common.activity.UI;
import com.yryz.yunxinim.uikit.common.ui.dialog.EasyAlertDialogHelper;
import com.yryz.yunxinim.uikit.common.ui.imageview.HeadImageView;
import com.yryz.yunxinim.uikit.common.ui.widget.SwitchButton;
import com.yryz.yunxinim.uikit.common.util.sys.NetworkUtil;
import com.yryz.yunxinim.uikit.contact_selector.activity.ContactSelectActivity;
import com.yryz.yunxinim.uikit.model.ToolBarOptions;
import com.yryz.yunxinim.uikit.session.helper.MessageListPanelHelper;
import com.yryz.yunxinim.uikit.team.activity.NormalTeamInfoActivity;
import com.yryz.yunxinim.uikit.team.activity.TeamPropertySettingActivity;
import com.yryz.yunxinim.uikit.team.helper.TeamHelper;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.friend.FriendService;

import java.util.ArrayList;

/**
 * Created by hzxuwen on 2015/10/13.
 */
public class MessageInfoActivity extends UI implements View.OnClickListener {
    private final static String EXTRA_ACCOUNT = "EXTRA_ACCOUNT";
    private static final int REQUEST_CODE_NORMAL = 1;
    // data
    private String account;
    // view
    private SwitchButton switchButton;

    public static void startActivity(Context context, String account) {
        Intent intent = new Intent();
        intent.setClass(context, MessageInfoActivity.class);
        intent.putExtra(EXTRA_ACCOUNT, account);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_info_activity);

        ToolBarOptions options = new ToolBarOptions();
        options.titleId = R.string.message_info;
        options.navigateId = R.mipmap.icon_arrow_back_black;
        setToolBar(R.id.toolbar, options);

        account = getIntent().getStringExtra(EXTRA_ACCOUNT);
        findViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateSwitchBtn();
    }

    private void findViews() {

        HeadImageView userHead = (HeadImageView) findViewById(R.id.user_layout).findViewById(R.id.imageViewHeader);
        TextView userName = (TextView) findViewById(R.id.user_layout).findViewById(R.id.textViewName);
        userHead.loadBuddyAvatar(account);
        userName.setText(NimUserInfoCache.getInstance().getUserDisplayName(account));
        userHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUserProfile();
            }
        });

        ((TextView) findViewById(R.id.create_team_layout).findViewById(R.id.textViewName)).setText("");
        HeadImageView addImage = (HeadImageView) findViewById(R.id.create_team_layout).findViewById(R.id.imageViewHeader);
        if (Session.isNeedTeam())
            addImage.setVisibility(View.VISIBLE);
        else
            addImage.setVisibility(View.GONE);
        addImage.setBackgroundResource(R.drawable.smiley_add_btn);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTeamMsg();
            }
        });

        ((TextView) findViewById(R.id.toggle_layout).findViewById(R.id.user_profile_title)).setText(R.string.msg_notice);
        switchButton = (SwitchButton) findViewById(R.id.toggle_layout).findViewById(R.id.user_profile_toggle);
        switchButton.setOnChangedListener(onChangedListener);

        View chatRecordView = findViewById(R.id.settings_chat_record_search);
        chatRecordView.setVisibility(View.GONE);
        chatRecordView.setOnClickListener(this);
        TextView chatRecordLabel = (TextView) chatRecordView.findViewById(R.id.item_title);
        chatRecordLabel.setText("聊天记录");
        chatRecordView.findViewById(R.id.item_more).setVisibility(View.INVISIBLE);

        View clearView = findViewById(R.id.settings_chat_record_clear);
        clearView.setOnClickListener(this);
        TextView clearLabel = (TextView) clearView.findViewById(R.id.item_title);
        clearLabel.setText("清空本地聊天记录");
        clearView.findViewById(R.id.item_more).setVisibility(View.VISIBLE);
    }

    private void updateSwitchBtn() {
        boolean notice = NIMClient.getService(FriendService.class).isNeedMessageNotify(account);
        switchButton.setCheck(notice);
    }

    private SwitchButton.OnChangedListener onChangedListener = new SwitchButton.OnChangedListener() {
        @Override
        public void OnChanged(View v, final boolean checkState) {
            if (!NetworkUtil.isNetAvailable(MessageInfoActivity.this)) {
                Toast.makeText(MessageInfoActivity.this, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
                switchButton.setCheck(!checkState);
                return;
            }

            NIMClient.getService(FriendService.class).setMessageNotify(account, checkState).setCallback(new RequestCallback<Void>() {
                @Override
                public void onSuccess(Void param) {
                    if (checkState) {
                        Toast.makeText(MessageInfoActivity.this, "开启消息提醒成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MessageInfoActivity.this, "关闭消息提醒成功", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailed(int code) {
                    if (code == 408) {
                        Toast.makeText(MessageInfoActivity.this, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MessageInfoActivity.this, "on failed:" + code, Toast.LENGTH_SHORT).show();
                    }
                    switchButton.setCheck(!checkState);
                }

                @Override
                public void onException(Throwable exception) {

                }
            });
        }
    };

    private void openUserProfile() {
        UserProfileActivity.start(this, account);
    }

    /**
     * 创建群聊
     */
    private void createTeamMsg() {
        ArrayList<String> memberAccounts = new ArrayList<>();
        memberAccounts.add(account);
//        ContactSelectActivity.Option option = TeamHelper.getCreateContactSelectOption(memberAccounts, 50);
//        NimUIKit.startContactSelect(this, option, REQUEST_CODE_NORMAL);// 创建群
        Intent intent = new Intent();
        intent.setAction(Constants.CONTACTS_SELECT_ACTION);
        intent.putStringArrayListExtra("EXTRA_DATA", memberAccounts);
        startActivityForResult(intent, REQUEST_CODE_NORMAL);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_NORMAL) {
                final ArrayList<String> selected = data.getStringArrayListExtra(ContactSelectActivity.RESULT_DATA);
                if (selected != null && !selected.isEmpty()) {

                    boolean isContain = false;
                    for (String item : selected) {
                        if (TextUtils.equals(item, account)) {
                            isContain = true;
                        }
                    }
                    if (!isContain) {
                        selected.add(account);
                    }

                    TeamCreateHelper.createNormalTeam(MessageInfoActivity.this, selected, true, new RequestCallback<Void>() {
                        @Override
                        public void onSuccess(Void param) {
                            finish();
                        }

                        @Override
                        public void onFailed(int code) {

                        }

                        @Override
                        public void onException(Throwable exception) {

                        }
                    });
                } else {
                    Toast.makeText(DemoCache.getContext(), "请选择至少一个联系人！", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.settings_chat_record_search) {
            Intent intent = new Intent("com.rz.yryz.MESSAGE_HISTORY_ACTION");
            intent.putExtra("EXTRA_DATA_ACCOUNT", account);
            intent.putExtra("EXTRA_DATA_SESSION_TYPE", SessionTypeEnum.P2P);
            startActivity(intent);
        } else if (i == R.id.settings_chat_record_clear) {
            clearMessage();
        }
    }

    private void clearMessage() {
//        EasyAlertDialogHelper.createOkCancelDiolag(this, null, "确定要清空吗？", true, new EasyAlertDialogHelper.OnDialogActionListener() {
//            @Override
//            public void doCancelAction() {
//
//            }
//
//            @Override
//            public void doOkAction() {
//                NIMClient.getService(MsgService.class).clearChattingHistory(account, SessionTypeEnum.P2P);
//                MessageListPanelHelper.getInstance().notifyClearMessages(account);
//                Toast.makeText(MessageInfoActivity.this, "清空成功！", Toast.LENGTH_SHORT).show();
//
//            }
//        }).show();

        View dialogView = LayoutInflater.from(this).inflate(R.layout.comm_dialog, null);
        final Dialog dialog = DialogUtils.selfDialog(this, dialogView, false);

        dialog.show();
        ((TextView) dialogView.findViewById(R.id.id_tv_message)).setText(getString(R.string.clear_chach));
        dialogView.findViewById(R.id.id_tv_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NIMClient.getService(MsgService.class).clearChattingHistory(account, SessionTypeEnum.P2P);
                MessageListPanelHelper.getInstance().notifyClearMessages(account);
                Toast.makeText(MessageInfoActivity.this, "清空成功！", Toast.LENGTH_SHORT).show();

            }
        });
        dialogView.findViewById(R.id.id_tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
}
