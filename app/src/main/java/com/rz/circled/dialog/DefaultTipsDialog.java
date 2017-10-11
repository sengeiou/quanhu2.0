package com.rz.circled.dialog;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.rz.circled.R;
import com.rz.circled.ui.activity.LoginActivity;
import com.rz.circled.ui.activity.MainActivity;
import com.rz.common.constant.CommonCode;
import com.rz.common.constant.IntentKey;
import com.rz.common.event.BaseEvent;
import com.rz.common.utils.DensityUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by rzw2 on 2017/9/4.
 */

public class DefaultTipsDialog extends DialogFragment {

    @BindView(R.id.tv_tip)
    TextView tvTip;

    public static DefaultTipsDialog newInstance(String title) {
        DefaultTipsDialog dialog = new DefaultTipsDialog();
        Bundle bundle = new Bundle();
        bundle.putString(IntentKey.EXTRA_TITLE, title);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override //在onCreate中设置对话框的风格、属性等
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog);
        //如果setCancelable()中参数为true，若点击dialog覆盖不到的activity的空白或者按返回键，则进行cancel，状态检测依次onCancel()和onDismiss()。如参数为false，则按空白处或返回键无反应。缺省为true
        setCancelable(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            Window dialogWindow = getDialog().getWindow();
            dialogWindow.setGravity(Gravity.CENTER);
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = (int) (DensityUtils.getScreenW(getContext()) * 0.9);
            dialogWindow.setAttributes(lp);
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_default_tip, container, false);
        ButterKnife.bind(this, v);
        initView();
        return v;
    }

    private void initView() {
        tvTip.setText(getArguments().getString(IntentKey.EXTRA_TITLE));
    }

    @Override
    public void dismiss() {
        super.dismiss();

        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.putExtra(IntentKey.EXTRA_TYPE, CommonCode.Constant.TAB_MAIN_HOME);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        EventBus.getDefault().post(new BaseEvent(CommonCode.EventType.TYPE_LOGOUT));
    }

    @OnClick(R.id.btn_submit)
    public void onClick() {
        dismiss();
    }
}
