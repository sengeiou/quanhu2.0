package com.rz.circled.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.rz.circled.R;

/**
 * Created by rzw2 on 2017/1/10.
 */

public class MsgHistoryClearDialog extends AlertDialog {
    private Context context;

    public MsgHistoryClearDialog(Context context) {
        super(context, R.style.dialog_default_style);
        init(context);
    }

    public MsgHistoryClearDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    public MsgHistoryClearDialog(Context context, int themeResId) {
        super(context, themeResId);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_msg_history_clear);

        TextView mBtnSure = (TextView) findViewById(R.id.id_shine_sure_txt);
        TextView mBtnCancel = (TextView) findViewById(R.id.id_shine_cancel_txt);

        mBtnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NIMClient.getService(MsgService.class).clearMsgDatabase(true);
                Toast.makeText(context, com.yryz.yunxinim.R.string.clear_msg_history_success, Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
