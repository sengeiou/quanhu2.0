package com.rz.circled.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rz.circled.R;
import com.rz.circled.ui.activity.AddContactsActivity;
import com.rz.circled.ui.activity.ChatSettingActivity;
import com.rz.circled.ui.activity.ContactsAty;
import com.rz.circled.ui.activity.ContactsSelectAty;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.IntentCode;
import com.yryz.yunxinim.team.activity.AdvancedTeamSearchActivity;
import com.yryz.yunxinim.uikit.common.util.sys.ScreenUtil;

import java.util.ArrayList;

/**
 * Created by rzw2 on 2017/1/10.
 */

public class MsgSettringDialog extends AlertDialog {
    private Context context;

    public MsgSettringDialog(Context context) {
        super(context, R.style.dialog_default_style);
        init(context);
    }

    public MsgSettringDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    public MsgSettringDialog(Context context, int themeResId) {
        super(context, themeResId);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_chat_menu);
        LinearLayout root = (LinearLayout) findViewById(R.id.layout_root);
        ViewGroup.LayoutParams params = root.getLayoutParams();
        params.width = (int) ScreenUtil.getDialogWidth();
        root.setLayoutParams(params);

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.BOTTOM);

        TextView mBtnAddFrient = (TextView) findViewById(R.id.btn_add_friend);
        TextView mBtnContact = (TextView) findViewById(R.id.btn_contact);
        TextView mBtnCreateDiscussionGroup = (TextView) findViewById(R.id.btn_create_discussion_group);
        TextView mBtnCreateGroup = (TextView) findViewById(R.id.btn_create_group);
        TextView mBtnSearchGroup = (TextView) findViewById(R.id.btn_search_group);
        TextView mBtnSetting = (TextView) findViewById(R.id.btn_setting);
        View view = findViewById(R.id.layout);
        if (Session.isNeedTeam())
            view.setVisibility(View.VISIBLE);
        else
            view.setVisibility(View.GONE);

        mBtnAddFrient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, AddContactsActivity.class));
                dismiss();
            }
        });

        mBtnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ContactsAty.class));
                dismiss();
            }
        });

        mBtnCreateDiscussionGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ContactSelectActivity.Option option = TeamHelper.getCreateContactSelectOption(null, 50);
//                NimUIKit.startContactSelect(context, option, IntentCode.TeamCreate.REQUEST_CODE_NORMAL);
                ContactsSelectAty.startActivityForResult(context, new ArrayList<String>(), IntentCode.TeamCreate.REQUEST_CODE_NORMAL);
                dismiss();
            }
        });

        mBtnCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ContactSelectActivity.Option advancedOption = TeamHelper.getCreateContactSelectOption(null, 50);
//                NimUIKit.startContactSelect(context, advancedOption, IntentCode.TeamCreate.REQUEST_CODE_ADVANCED);
                ContactsSelectAty.startActivityForResult(context, new ArrayList<String>(), IntentCode.TeamCreate.REQUEST_CODE_ADVANCED);
                dismiss();
            }
        });

        mBtnSearchGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdvancedTeamSearchActivity.start(context);
                dismiss();
            }
        });

        mBtnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ChatSettingActivity.class));
                dismiss();
            }
        });

        findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
