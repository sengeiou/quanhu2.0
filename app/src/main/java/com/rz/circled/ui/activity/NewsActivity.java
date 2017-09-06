package com.rz.circled.ui.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rz.circled.R;
import com.rz.common.ui.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by rzw2 on 2017/9/5.
 */

public class NewsActivity extends BaseActivity {
    @BindView(R.id.dot_announcement)
    ImageView dotAnnouncement;
    @BindView(R.id.tv_announcement_time)
    TextView tvAnnouncementTime;
    @BindView(R.id.tv_announcement_desc)
    TextView tvAnnouncementDesc;
    @BindView(R.id.btn_announcement)
    LinearLayout btnAnnouncement;
    @BindView(R.id.dot_system_information)
    ImageView dotSystemInformation;
    @BindView(R.id.tv_system_information_time)
    TextView tvSystemInformationTime;
    @BindView(R.id.tv_system_information_desc)
    TextView tvSystemInformationDesc;
    @BindView(R.id.btn_system_information)
    LinearLayout btnSystemInformation;
    @BindView(R.id.dot_account_information)
    ImageView dotAccountInformation;
    @BindView(R.id.tv_account_information_time)
    TextView tvAccountInformationTime;
    @BindView(R.id.tv_account_information_desc)
    TextView tvAccountInformationDesc;
    @BindView(R.id.btn_account_information)
    LinearLayout btnAccountInformation;
    @BindView(R.id.dot_interactive)
    ImageView dotInteractive;
    @BindView(R.id.tv_interactive_time)
    TextView tvInteractiveTime;
    @BindView(R.id.tv_interactive_desc)
    TextView tvInteractiveDesc;
    @BindView(R.id.btn_interactive)
    LinearLayout btnInteractive;
    @BindView(R.id.dot_recommend)
    ImageView dotRecommend;
    @BindView(R.id.tv_recommend_time)
    TextView tvRecommendTime;
    @BindView(R.id.tv_recommend_desc)
    TextView tvRecommendDesc;
    @BindView(R.id.btn_recommend)
    LinearLayout btnRecommend;

    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_news, null);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.btn_announcement, R.id.btn_system_information, R.id.btn_account_information, R.id.btn_interactive, R.id.btn_recommend})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_announcement:
                break;
            case R.id.btn_system_information:
                break;
            case R.id.btn_account_information:
                break;
            case R.id.btn_interactive:
                break;
            case R.id.btn_recommend:
                break;
        }
    }
}
