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

import com.rz.circled.R;
import com.rz.common.utils.DensityUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by rzw2 on 2017/9/4.
 */

public class ApplyForGroupSuccessDialog extends DialogFragment {

    public static ApplyForGroupSuccessDialog newInstance() {
        ApplyForGroupSuccessDialog dialog = new ApplyForGroupSuccessDialog();
        Bundle bundle = new Bundle();
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
        View v = inflater.inflate(R.layout.dialog_apply_for_private_group_success, container, false);
        ButterKnife.bind(this, v);
        initView();
        return v;
    }

    private void initView() {

    }

    @Override
    public void dismiss() {
        super.dismiss();
        getActivity().finish();
    }

    @OnClick(R.id.id_set_pay_pw_txt)
    public void onClick() {
        dismiss();
    }
}
