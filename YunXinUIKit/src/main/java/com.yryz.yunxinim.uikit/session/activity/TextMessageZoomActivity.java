package com.yryz.yunxinim.uikit.session.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.TextView;

import com.yryz.yunxinim.uikit.NimUIKit;
import com.yryz.yunxinim.uikit.R;
import com.yryz.yunxinim.uikit.session.constant.Extras;
import com.yryz.yunxinim.uikit.session.emoji.MoonUtil;

/**
 * 文本消息文字放大
 * Created by rzw2 on 2017/3/21.
 */

public class TextMessageZoomActivity extends Activity {
    private TextView mTvContent;
    private String mContent;

    public static void start(Context context, String data) {
        Intent intent = new Intent();

        intent.putExtra(Extras.EXTRA_DATA, data);
        intent.setClass(context, TextMessageZoomActivity.class);

        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nim_text_zoom_activity);

        parseIntent();
        findViews();
        initViews();

    }

    private void parseIntent() {
        mContent = getIntent().getStringExtra(Extras.EXTRA_DATA);
    }

    private void findViews() {
        mTvContent = (TextView) findViewById(R.id.text_zoom_content);
    }

    private void initViews() {
        displayText();
        findViewById(R.id.root_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void displayText() {
        if (!TextUtils.isEmpty(mContent)) {
            MoonUtil.identifyFaceExpression(NimUIKit.getContext(), mTvContent, mContent, ImageSpan.ALIGN_BOTTOM);
        }
    }
}
