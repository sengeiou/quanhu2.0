package com.rz.circled.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import com.rz.circled.R;
import com.rz.common.constant.IntentKey;
import com.rz.common.ui.activity.BaseActivity;

/**
 * Created by rzw2 on 2017/9/7.
 */

public class NewsAnnounceDetailsActivity extends BaseActivity {

    public static void startAnnouncementDetails(Context context, String url) {
        Intent i = new Intent(context, NewsAnnounceDetailsActivity.class);
        i.putExtra(IntentKey.EXTRA_URL, url);
        context.startActivity(i);
    }

    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_my_private_group, null, false);
    }

    @Override
    public void initView() {
        setTitleText(R.string.announcement_details);
    }

    @Override
    public void initData() {

    }

    @Override
    public void refreshPage() {

    }
}
