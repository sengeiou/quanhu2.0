package com.rz.circled.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;

import com.rz.circled.R;
import com.rz.circled.ui.fragment.NewsCommonFragment;
import com.rz.common.constant.IntentKey;
import com.rz.common.ui.activity.BaseActivity;

/**
 * Created by rzw2 on 2017/9/7.
 */

public class NewsFragmentActivity extends BaseActivity {
    private NewsCommonFragment fragment;

    public static void startNewsFragment(Context context, int index) {
        Intent i = new Intent(context, NewsFragmentActivity.class);
        i.putExtra(IntentKey.KEY_TYPE, index);
        context.startActivity(i);
    }

    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_news_fram, null, false);
    }

    @Override
    public void initView() {
        switch (getIntent().getIntExtra(IntentKey.KEY_TYPE, 0)) {
            case NewsCommonFragment.NEWS_ANNOUNCEMENT:
                setTitleText(R.string.announcement);
                break;
            case NewsCommonFragment.NEWS_SYSTEM_INFORMATION:
                setTitleText(R.string.system_information);
                break;
            case NewsCommonFragment.NEWS_RECOMMEND:
                setTitleText(R.string.recommend_information);
                break;
            case NewsCommonFragment.NEWS_ACCOUNT:
                setTitleText(R.string.account_information);
                break;
        }
    }

    @Override
    public void initData() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.layout_frame, fragment = NewsCommonFragment.newInstance(getIntent().getIntExtra(IntentKey.KEY_TYPE, 0)));
        transaction.commit();
    }

    @Override
    public void refreshPage() {
    }
}
