package com.rz.circled.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rz.circled.R;
import com.rz.circled.ui.fragment.NewsCommonFragment;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.CommonCode;
import com.rz.common.event.BaseEvent;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.widget.svp.SVProgressHUD;
import com.rz.httpapi.api.ApiNewsService;
import com.rz.httpapi.api.BaseCallback;
import com.rz.httpapi.api.Http;
import com.rz.httpapi.api.ResponseData.ResponseData;
import com.rz.httpapi.bean.NewsUnreadBean;
import com.rz.httpapi.bean.PrivateGroupBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

import static com.rz.circled.event.EventConstant.NEWS_ACCOUNT_INFORMATION_UNREAD_CHANGE;
import static com.rz.circled.event.EventConstant.NEWS_ACTIVITY_UNREAD_CHANGE;
import static com.rz.circled.event.EventConstant.NEWS_ANNOUNCEMENT_UNREAD_CHANGE;
import static com.rz.circled.event.EventConstant.NEWS_COMMENT_UNREAD_CHANGE;
import static com.rz.circled.event.EventConstant.NEWS_GROUP_UNREAD_CHANGE;
import static com.rz.circled.event.EventConstant.NEWS_QA_UNREAD_CHANGE;
import static com.rz.circled.event.EventConstant.NEWS_RECOMMEND_UNREAD_CHANGE;
import static com.rz.circled.event.EventConstant.NEWS_SYSTEM_INFORMATION_UNREAD_CHANGE;

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
        dotAnnouncement.setVisibility(Session.getNewsAnnouncementNum() == 0 ? View.GONE : View.VISIBLE);
        dotAccountInformation.setVisibility(Session.getNewsAccountInformationNum() == 0 ? View.GONE : View.VISIBLE);
        dotSystemInformation.setVisibility(Session.getNewsSystemInformationNum() == 0 ? View.GONE : View.VISIBLE);
        dotRecommend.setVisibility(Session.getNewsRecommendNum() == 0 ? View.GONE : View.VISIBLE);
        dotInteractive.setVisibility(Session.getNewsCommentNum()
                + Session.getNewsQaNum()
                + Session.getNewsGroupNum()
                + Session.getNewsActivityNum() == 0 ? View.GONE : View.VISIBLE);
    }

    @Override
    public void initData() {
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        Http.getApiService(ApiNewsService.class).newsUnread(Session.getUserId()).enqueue(new BaseCallback<ResponseData<NewsUnreadBean>>() {
            @Override
            public void onResponse(Call<ResponseData<NewsUnreadBean>> call, Response<ResponseData<NewsUnreadBean>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful() && !response.body().isSuccessful()) {
                    NewsUnreadBean data = response.body().getData();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void eventBus(BaseEvent event) {
        switch (event.getType()) {
            case NEWS_ANNOUNCEMENT_UNREAD_CHANGE:
                dotAnnouncement.setVisibility(Session.getNewsAnnouncementNum() == 0 ? View.GONE : View.VISIBLE);
                break;
            case NEWS_ACCOUNT_INFORMATION_UNREAD_CHANGE:
                dotAccountInformation.setVisibility(Session.getNewsAccountInformationNum() == 0 ? View.GONE : View.VISIBLE);
                break;
            case NEWS_SYSTEM_INFORMATION_UNREAD_CHANGE:
                dotSystemInformation.setVisibility(Session.getNewsSystemInformationNum() == 0 ? View.GONE : View.VISIBLE);
                break;
            case NEWS_RECOMMEND_UNREAD_CHANGE:
                dotRecommend.setVisibility(Session.getNewsRecommendNum() == 0 ? View.GONE : View.VISIBLE);
                break;
            case NEWS_COMMENT_UNREAD_CHANGE:
            case NEWS_QA_UNREAD_CHANGE:
            case NEWS_GROUP_UNREAD_CHANGE:
            case NEWS_ACTIVITY_UNREAD_CHANGE:
                dotInteractive.setVisibility(Session.getNewsCommentNum()
                        + Session.getNewsQaNum()
                        + Session.getNewsGroupNum()
                        + Session.getNewsActivityNum() == 0 ? View.GONE : View.VISIBLE);
                break;
        }
    }

    @OnClick({R.id.btn_announcement, R.id.btn_system_information, R.id.btn_account_information, R.id.btn_interactive, R.id.btn_recommend})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_announcement:
                NewsFragmentActivity.startNewsFragment(mContext, NewsCommonFragment.NEWS_ANNOUNCEMENT);
                break;
            case R.id.btn_system_information:
                NewsFragmentActivity.startNewsFragment(mContext, NewsCommonFragment.NEWS_SYSTEM_INFORMATION);
                break;
            case R.id.btn_account_information:
                NewsFragmentActivity.startNewsFragment(mContext, NewsCommonFragment.NEWS_ACCOUNT);
                break;
            case R.id.btn_interactive:
                startActivity(new Intent(mContext, NewsInteractiveActivity.class));
                break;
            case R.id.btn_recommend:
                NewsFragmentActivity.startNewsFragment(mContext, NewsCommonFragment.NEWS_RECOMMEND);
                break;
        }
    }
}
