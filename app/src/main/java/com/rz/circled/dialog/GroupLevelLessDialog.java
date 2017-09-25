package com.rz.circled.dialog;

import android.app.Dialog;
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
import com.rz.circled.ui.activity.CommonH5Activity;
import com.rz.common.constant.H5Address;
import com.rz.common.constant.IntentKey;
import com.rz.common.utils.DensityUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by rzw2 on 2017/9/4.
 */

public class GroupLevelLessDialog extends DialogFragment {

    @BindView(R.id.tv)
    TextView tv;

    public static GroupLevelLessDialog newInstance(String level) {
        GroupLevelLessDialog dialog = new GroupLevelLessDialog();
        Bundle bundle = new Bundle();
        bundle.putString(IntentKey.EXTRA_NUM, level);
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
        View v = inflater.inflate(R.layout.dialog_private_group_level_less, container, false);
        ButterKnife.bind(this, v);
        initView();
        return v;
    }

    private void initView() {
        tv.setText(String.format(getString(R.string.private_group_my_level), getArguments().getString(IntentKey.EXTRA_NUM)));
    }

    @OnClick({R.id.btn_cancel, R.id.btn_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.btn_submit:
                CommonH5Activity.startCommonH5(getContext(), getString(R.string.v3_customer_service), H5Address.CONECT_US);
                break;
        }
    }
}
