package com.rz.circled.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.rz.circled.R;
import com.rz.circled.adapter.NewsMultiTypeAdapter;
import com.rz.circled.constants.NewsTypeConstants;
import com.rz.circled.event.EventConstant;
import com.rz.circled.helper.NewsHelper;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.CommonCode;
import com.rz.common.event.BaseEvent;
import com.rz.common.ui.fragment.BaseFragment;
import com.rz.common.widget.svp.SVProgressHUD;
import com.rz.httpapi.api.ApiNewsService;
import com.rz.httpapi.api.BaseCallback;
import com.rz.httpapi.api.Http;
import com.rz.httpapi.api.ResponseData.ResponseData;
import com.rz.httpapi.bean.NewsBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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
    private int refreshType = -1;

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
    public void onDestroyView() {
        super.onDestroyView();
        Log.e(TAG, "onDestroyView: " + getUserVisibleHint());
        if (getUserVisibleHint())
            NewsHelper.clearUnreadByType(refreshType);
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
        loadData(false);
    }

    @Override
    protected boolean needLoadingView() {
        return true;
    }

    @Subscribe
    public void eventBus(BaseEvent event) {
        switch (event.getType()) {
            case EventConstant.NEWS_COME_UNREAD:
                String[] data = ((String) event.getData()).split("-");
                if (data.length == 2) {
                    int _type = Integer.parseInt(data[0]);
                    int _label = Integer.parseInt(data[1]);
                    int mType = 0;
                    switch (_type) {
                        case NewsTypeConstants.NEWS_ANNOUNCEMENT:
                            mType = NEWS_ANNOUNCEMENT;
                            break;
                        case NewsTypeConstants.NEWS_SYSTEM:
                            mType = NEWS_SYSTEM_INFORMATION;
                            break;
                        case NewsTypeConstants.NEWS_ACCOUNT:
                            mType = NEWS_ACCOUNT;
                            break;
                        case NewsTypeConstants.NEWS_INTERACTIVE:
                            switch (_label) {
                                case NewsTypeConstants.NEWS_COMMENT:
                                    mType = NEWS_COMMENT;
                                    break;
                                case NewsTypeConstants.NEWS_ANSWER:
                                    mType = NEWS_QA;
                                    break;
                                case NewsTypeConstants.NEWS_GROUP:
                                    mType = NEWS_PRIVATE_GROUP;
                                    break;
                                case NewsTypeConstants.NEWS_ACTIVITY:
                                    mType = NEWS_ACTIVITY;
                                    break;
                            }
                            break;
                        case NewsTypeConstants.NEWS_RECOMMEND:
                            mType = NEWS_RECOMMEND;
                            break;
                    }
                    if (type == mType) {
                        refreshType = mType;
                        loadData(false);
                    }
                }
                break;
        }
    }

    private void loadData(final boolean loadMore) {
        int ontType;
        Integer twoType = null;
        switch (type) {
            case NEWS_ANNOUNCEMENT:
                ontType = NewsTypeConstants.NEWS_ANNOUNCEMENT;
                break;
            case NEWS_SYSTEM_INFORMATION:
                ontType = NewsTypeConstants.NEWS_SYSTEM;
                break;
            case NEWS_RECOMMEND:
                ontType = NewsTypeConstants.NEWS_RECOMMEND;
                break;
            case NEWS_ACCOUNT:
                ontType = NewsTypeConstants.NEWS_ACCOUNT;
                break;
            case NEWS_COMMENT:
                twoType = NewsTypeConstants.NEWS_COMMENT;
                ontType = NewsTypeConstants.NEWS_INTERACTIVE;
                break;
            case NEWS_QA:
                twoType = NewsTypeConstants.NEWS_ANSWER;
                ontType = NewsTypeConstants.NEWS_INTERACTIVE;
                break;
            case NEWS_PRIVATE_GROUP:
                twoType = NewsTypeConstants.NEWS_GROUP;
                ontType = NewsTypeConstants.NEWS_INTERACTIVE;
                break;
            case NEWS_ACTIVITY:
                twoType = NewsTypeConstants.NEWS_ACTIVITY;
                ontType = NewsTypeConstants.NEWS_INTERACTIVE;
                break;
            default:
                ontType = NewsTypeConstants.NEWS_ANNOUNCEMENT;
                break;
        }

        Http.getApiService(ApiNewsService.class).newsMultiList(Session.getUserId(), ontType, twoType, loadMore ? mAdapter.getItemCount() : 0, PAGE_SIZE).enqueue(new BaseCallback<ResponseData<List<NewsBean>>>() {
            @Override
            public void onResponse(Call<ResponseData<List<NewsBean>>> call, Response<ResponseData<List<NewsBean>>> response) {
                super.onResponse(call, response);
                if (layoutRefresh == null) return;
                layoutRefresh.setRefreshing(false);
                if (response.isSuccessful()) {
                    if (!response.body().isSuccessful()) {
                        SVProgressHUD.showErrorWithStatus(getContext(), response.body().getMsg());
                    } else {
                        List<NewsBean> data = response.body().getData();
                        if (data != null && data.size() > 0) {
                            if (loadMore) {
                                List<NewsBean> oldData = mAdapter.getItems() == null ? new ArrayList<NewsBean>() : (List<NewsBean>) mAdapter.getItems();
                                oldData.addAll(data);
                                mAdapter.setItems(oldData);
                            } else {
                                mAdapter.setItems(data);
                            }
                            mAdapter.notifyDataSetChanged();
                        }
                        if (mAdapter.getItemCount() == 0)
                            onLoadingStatus(CommonCode.General.NEWS_EMPTY);
                    }
                } else {
                    SVProgressHUD.showErrorWithStatus(getContext(), getString(R.string.request_failed));
                }
            }

            @Override
            public void onFailure(Call<ResponseData<List<NewsBean>>> call, Throwable t) {
                super.onFailure(call, t);
                if (layoutRefresh == null) return;
                layoutRefresh.setRefreshing(false);
                SVProgressHUD.showErrorWithStatus(getContext(), getString(R.string.request_failed));
            }
        });
    }

    @Override
    public void refreshPage() {
        initData();
    }
}
