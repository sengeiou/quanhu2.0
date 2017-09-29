package com.rz.common.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.rz.common.R;
import com.rz.common.utils.CountDownTimer;

/**
 * Created by Gsm on 2017/8/16.
 */
public class CommonDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private View rootView;
    private TextView tvMessage;

    private OnCommonDialogConfirmListener confirmListener;

    public CommonDialog(Context context) {
        this(context, 0);
    }

    public CommonDialog(Context context, int themeResId) {
        super(context, R.style.AppTheme_DialogStyle);
        mContext = context;
        init();
    }

    private void init() {
        rootView = getLayoutInflater().inflate(R.layout.dialog_common, null);
        tvMessage = (TextView) rootView.findViewById(R.id.tv_common_dialog_message);
        rootView.findViewById(R.id.tv_common_dialog_confirm).setOnClickListener(this);
        rootView.findViewById(R.id.tv_common_dialog_cancel).setOnClickListener(this);
    }

    public void showDialog(int stringId, OnCommonDialogConfirmListener confirmListener) {
        showDialog(mContext.getString(stringId), confirmListener);
    }

    public void showDialog(String string, OnCommonDialogConfirmListener confirmListener) {
        this.confirmListener = confirmListener;
        tvMessage.setText(string);
        closeDialog();
        show();
        setContentView(rootView);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_common_dialog_cancel) {
            closeDialog();
        } else if (i == R.id.tv_common_dialog_confirm) {
            if (CountDownTimer.isFastClick()) return;
            if (confirmListener != null) confirmListener.onConfirmListener();
            closeDialog();
        }
    }

    public void closeDialog() {
        if (isShowing())
            this.dismiss();
    }

    public interface OnCommonDialogConfirmListener {
        void onConfirmListener();
    }
}
