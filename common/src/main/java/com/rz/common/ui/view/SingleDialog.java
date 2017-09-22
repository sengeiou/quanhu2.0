package com.rz.common.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.rz.common.R;

/**
 * Created by Gsm on 2017/9/22.
 */
public abstract class SingleDialog extends Dialog implements View.OnClickListener {
    private Context mContext;
    private View rootView;
    private TextView tvMessage;

    public SingleDialog(Context context) {
        super(context);
    }

    public SingleDialog(Context context, int themeResId) {
        super(context, R.style.AppTheme_DialogStyle);
        mContext = context;
        init();
    }

    private void init() {
        rootView = getLayoutInflater().inflate(R.layout.dialog_single, null);
        tvMessage = (TextView) rootView.findViewById(R.id.tv_single_dialog_message);
        rootView.findViewById(R.id.tv_single_dialog_confirm).setOnClickListener(this);
    }

    public void showDialog(int stringId) {
        tvMessage.setText(stringId);
        closeDialog();
        show();
        setContentView(rootView);
    }

    public void closeDialog() {
        if (isShowing())
            this.dismiss();
    }

}
