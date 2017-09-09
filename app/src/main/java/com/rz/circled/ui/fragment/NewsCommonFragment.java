package com.rz.circled.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.rz.circled.R;
import com.rz.circled.adapter.NewsMultiTypeAdapter;
import com.rz.circled.event.EventConstant;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.CommonCode;
import com.rz.common.event.BaseEvent;
import com.rz.common.ui.fragment.BaseFragment;
import com.rz.common.widget.svp.SVProgressHUD;
import com.rz.httpapi.api.ApiNews;
import com.rz.httpapi.api.ApiNewsService;
import com.rz.httpapi.api.BaseCallback;
import com.rz.httpapi.api.Http;
import com.rz.httpapi.api.ResponseData.ResponseData;
import com.rz.httpapi.bean.NewsBean;
import com.rz.httpapi.bean.PrivateGroupBean;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Response;

import static com.rz.common.constant.CommonCode.Constant.PAGE_SIZE;
import static com.rz.common.constant.IntentKey.EXTRA_TYPE;

/**
 * Created by rzw2 on 2017/9/7.
 */

public class NewsCommonFragment extends BaseFragment {

    public static final int NEWS_ANNOUNCEMENT = 0;
    public static final int NEWS_SYSTEM_INFORMATION = 1;
    public static final int NEWS_RECOMMEND = 2;
    public static final int NEWS_ACCOUNT = 3;
    public static final int NEWS_COMMENT = 4;
    public static final int NEWS_QA = 5;
    public static final int NEWS_PRIVATE_GROUP = 6;
    public static final int NEWS_ACTIVITY = 7;

    @BindView(R.id.list)
    RecyclerView list;
    @BindView(R.id.layout_refresh)
    SwipyRefreshLayout layoutRefresh;

    private NewsMultiTypeAdapter mAdapter;
    private int type;

    public static NewsCommonFragment newInstance(int type) {
        NewsCommonFragment fragment = new NewsCommonFragment();
        Bundle args = new Bundle();
        args.putInt(EXTRA_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments() != null ? getArguments().getInt(EXTRA_TYPE) : 0;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) switch (type) {
            case NEWS_ANNOUNCEMENT:
                if (Session.getNewsAnnouncementNum() != 0) {
                    Session.setNewsAnnouncementNum(0);
                    EventBus.getDefault().post(new BaseEvent(EventConstant.NEWS_ANNOUNCEMENT_UNREAD_CHANGE));
                }
                break;
            case NEWS_SYSTEM_INFORMATION:
                if (Session.getNewsSystemInformationNum() != 0) {
                    Session.setNewsSystemInformationNum(0);
                    EventBus.getDefault().post(new BaseEvent(EventConstant.NEWS_SYSTEM_INFORMATION_UNREAD_CHANGE));
                }
                break;
            case NEWS_RECOMMEND:
                if (Session.getNewsRecommendNum() != 0) {
                    Session.setNewsRecommendNum(0);
                    EventBus.getDefault().post(new BaseEvent(EventConstant.NEWS_RECOMMEND_UNREAD_CHANGE));
                }
                break;
            case NEWS_ACCOUNT:
                if (Session.getNewsAccountInformationNum() != 0) {
                    Session.setNewsAccountInformationNum(0);
                    EventBus.getDefault().post(new BaseEvent(EventConstant.NEWS_ACCOUNT_INFORMATION_UNREAD_CHANGE));
                }
                break;
            case NEWS_COMMENT:
                if (Session.getNewsCommentNum() != 0) {
                    Session.setNewsCommentNum(0);
                    EventBus.getDefault().post(new BaseEvent(EventConstant.NEWS_COMMENT_UNREAD_CHANGE));
                }
                break;
            case NEWS_QA:
                if (Session.getNewsQaNum() != 0) {
                    Session.setNewsQaNum(0);
                    EventBus.getDefault().post(new BaseEvent(EventConstant.NEWS_QA_UNREAD_CHANGE));
                }
                break;
            case NEWS_PRIVATE_GROUP:
                if (Session.getNewsGroupNum() != 0) {
                    Session.setNewsGroupNum(0);
                    EventBus.getDefault().post(new BaseEvent(EventConstant.NEWS_GROUP_UNREAD_CHANGE));
                }
                break;
            case NEWS_ACTIVITY:
                if (Session.getNewsActivityNum() != 0) {
                    Session.setNewsActivityNum(0);
                    EventBus.getDefault().post(new BaseEvent(EventConstant.NEWS_ACTIVITY_UNREAD_CHANGE));
                }
                break;
        }
    }

    @Nullable
    @Override
    public View loadView(LayoutInflater inflater) {
        switch (type) {
            case NEWS_ANNOUNCEMENT:
                return inflater.inflate(R.layout.activity_news_announcement, null);
            case NEWS_SYSTEM_INFORMATION:
                return inflater.inflate(R.layout.activity_news_system_information, null);
            case NEWS_RECOMMEND:
                return inflater.inflate(R.layout.activity_news_recommend, null);
            case NEWS_ACCOUNT:
                return inflater.inflate(R.layout.activity_news_account_information, null);
            case NEWS_COMMENT:
                return inflater.inflate(R.layout.fragment_news_interactive, null);
            case NEWS_QA:
                return inflater.inflate(R.layout.fragment_news_interactive, null);
            case NEWS_PRIVATE_GROUP:
                return inflater.inflate(R.layout.fragment_news_interactive, null);
            case NEWS_ACTIVITY:
                return inflater.inflate(R.layout.fragment_news_interactive, null);
            default:
                return inflater.inflate(R.layout.activity_news_announcement, null);
        }
    }

    @Override
    public void initView() {
        list.setAdapter(mAdapter = new NewsMultiTypeAdapter());
        layoutRefresh.setDirection(SwipyRefreshLayoutDirection.BOTH);
        layoutRefresh.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                if (direction == SwipyRefreshLayoutDirection.TOP) {
                    loadData(false);
                } else {
                    loadData(true);
                }
            }
        });
    }

    @Override
    public void initData() {
        List<NewsBean> data = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            data.add(new NewsBean(i));
        }
        mAdapter.setItems(data);
        mAdapter.notifyDataSetChanged();

        loadData(false);
    }

    private void loadData(final boolean loadMore) {
        int ontType;
        int twoType = 0;
        switch (type) {
            case NEWS_ANNOUNCEMENT:
                ontType = ApiNews.NEWS_ANNOUNCEMENT;
                break;
            case NEWS_SYSTEM_INFORMATION:
                ontType = ApiNews.NEWS_SYSTEM;
                break;
            case NEWS_RECOMMEND:
                ontType = ApiNews.NEWS_RECOMMEND;
                break;
            case NEWS_ACCOUNT:
                ontType = ApiNews.NEWS_ACCOUNT;
                break;
            case NEWS_COMMENT:
                twoType = ApiNews.NEWS_COMMENT;
                ontType = ApiNews.NEWS_INTERACTIVE;
                break;
            case NEWS_QA:
                twoType = ApiNews.NEWS_ANSWER;
                ontType = ApiNews.NEWS_INTERACTIVE;
                break;
            case NEWS_PRIVATE_GROUP:
                twoType = ApiNews.NEWS_GROUP;
                ontType = ApiNews.NEWS_INTERACTIVE;
                break;
            case NEWS_ACTIVITY:
                twoType = ApiNews.NEWS_ACTIVITY;
                ontType = ApiNews.NEWS_INTERACTIVE;
                break;
            default:
                ontType = ApiNews.NEWS_ANNOUNCEMENT;
                break;
        }

        Http.getApiService(ApiNewsService.class).newsMulitList(Session.getUserId(), ontType, twoType, mAdapter.getItemCount(), PAGE_SIZE).enqueue(new BaseCallback<ResponseData<List<NewsBean>>>() {
            @Override
            public void onResponse(Call<ResponseData<List<NewsBean>>> call, Response<ResponseData<List<NewsBean>>> response) {
                super.onResponse(call, response);
                layoutRefresh.setRefreshing(false);
                if (response.isSuccessful()) {
                    if (!response.body().isSuccessful()) {
                        SVProgressHUD.showErrorWithStatus(getContext(), response.body().getMsg());
                    } else {
                        List<NewsBean> data = response.body().getData();
                        if (data != null && data.size() > 0) {
                            if (loadMore) {
                                mAdapter.setItems(data);
                            } else {
                                mAdapter.setItems(data);
                            }
                        } else {
                            onLoadingStatus(CommonCode.General.DATA_EMPTY);
                        }
                    }
                } else {
                    SVProgressHUD.showErrorWithStatus(getContext(), getString(R.string.request_failed));
                }
            }

            @Override
            public void onFailure(Call<ResponseData<List<NewsBean>>> call, Throwable t) {
                super.onFailure(call, t);
                layoutRefresh.setRefreshing(false);
                SVProgressHUD.showErrorWithStatus(getContext(), getString(R.string.request_failed));
            }
        });
    }

}
