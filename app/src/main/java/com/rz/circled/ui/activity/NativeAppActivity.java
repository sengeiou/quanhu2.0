package com.rz.circled.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;

import com.rz.circled.R;
import com.rz.common.ui.activity.BaseActivity;

import java.util.List;

/**
 * Created by Administrator on 2017/11/10 0010.
 */

public class NativeAppActivity extends BaseActivity {

    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(null, null);
    }

    @Override
    public void initView() {

        Uri uri = getIntent().getData();
        if (uri != null)
        {
            List<String> pathSegments = uri.getPathSegments();
            String uriQuery = uri.getQuery();
            Intent intent;
            if (pathSegments != null && pathSegments.size() > 0) {
                // 解析SCHEME
//                if (someif) {
//                    dosomething();
//                }
//                else {
//                    // 若解析不到SCHEME，则关闭NativeAppActivity；
//                    finish();
//                }
            } else {
                finish();
            }
        } else {
            finish();
        }

    }

    @Override
    public void initData() {

    }

    @Override
    public void refreshPage() {

    }
}
