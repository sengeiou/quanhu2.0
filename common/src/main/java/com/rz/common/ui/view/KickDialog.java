package com.rz.common.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.rz.common.R;

/**
 * Created by Gsm on 2017/9/22.
 */
public abstract class KickDialog extends Dialog implements View.OnClickListener {
    private Context mContext;
    private View rootView;
    private TextView tvMessage;

    public KickDialog(Context context) {
        this(context, 0);
    }

    public KickDialog(Context context, int themeResId) {
        super(context, R.style.AppTheme_DialogStyle);
        mContext = context;
        init();
    }

    private void init() {
        rootView = getLayoutInflater().inflate(R.layout.dialog_kick, null);
        rootView.findViewById(R.id.tv_kick_dialog_left).setOnClickListener(this);
        rootView.findViewById(R.id.tv_kick_dialog_right).setOnClickListener(this);
    }

    public void showDialog() {
        closeDialog();
        show();
        setContentView(rootView);
    }

    public void closeDialog() {
        if (isShowing())
            this.dismiss();
    }

}
