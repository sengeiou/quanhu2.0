package com.rz.circled.ui.activity;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.rz.circled.R;
import com.rz.circled.adapter.NewsOverviewAdapter;
import com.rz.circled.constants.NewsTypeConstants;
import com.rz.circled.event.EventConstant;
import com.rz.circled.helper.NewsHelper;
import com.rz.circled.ui.fragment.NewsCommonFragment;
import com.rz.common.cache.preference.EntityCache;
import com.rz.common.cache.preference.Session;
import com.rz.common.event.BaseEvent;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.httpapi.api.ApiNewsService;
import com.rz.httpapi.api.BaseCallback;
import com.rz.httpapi.api.Http;
import com.rz.httpapi.api.ResponseData.ResponseData;
import com.rz.httpapi.bean.NewsBean;
import com.rz.httpapi.bean.NewsOverviewBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by rzw2 on 2017/9/5.
 */

public class NewsActivity extends BaseActivity {

    @BindView(R.id.lv)
    ListView lv;

    private static Integer[] TYPES = {
            NewsTypeConstants.NEWS_ANNOUNCEMENT,
            NewsTypeConstants.NEWS_SYSTEM,
            NewsTypeConstants.NEWS_INTERACTIVE,
            NewsTypeConstants.NEWS_RECOMMEND,
            NewsTypeConstants.NEWS_ACCOUNT};
    private List<Integer> mTypeId = Arrays.asList(TYPES);
    private static Integer[] TITLES = {
            R.string.announcement,
            R.string.system_information,
            R.string.interactive_information,
            R.string.recommend_information,
            R.string.account_information};
    private List<Integer> mTitleId = Arrays.asList(TITLES);
    private static Integer[] IMAGES = {
            R.mipmap.ic_news_announcement,
            R.mipmap.ic_news_system_information,
            R.mipmap.ic_news_interactive,
            R.mipmap.ic_news_recommend,
            R.mipmap.ic_news_account_information};
    private List<Integer> mImageId = Arrays.asList(IMAGES);
    private NewsOverviewAdapter mAdapter;
    private EntityCache<NewsOverviewBean> mCache;

    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_news, null);
    }

    @Override
    public void initView() {
        setTitleText(R.string.news);
        lv.setAdapter(mAdapter = new NewsOverviewAdapter(mContext, R.layout.item_news));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        NewsFragmentActivity.startNewsFragment(mContext, NewsCommonFragment.NEWS_ANNOUNCEMENT);
                        NewsHelper.clearUnreadByType(NewsCommonFragment.NEWS_ANNOUNCEMENT);
                        break;
                    case 1:
                        NewsFragmentActivity.startNewsFragment(mContext, NewsCommonFragment.NEWS_SYSTEM_INFORMATION);
                        NewsHelper.clearUnreadByType(NewsCommonFragment.NEWS_SYSTEM_INFORMATION);
                        break;
                    case 2:
                        startActivity(new Intent(mContext, NewsInteractiveActivity.class));
                        break;
                    case 3:
                        NewsFragmentActivity.startNewsFragment(mContext, NewsCommonFragment.NEWS_RECOMMEND);
                        NewsHelper.clearUnreadByType(NewsCommonFragment.NEWS_RECOMMEND);
                        break;
                    case 4:
                        NewsFragmentActivity.startNewsFragment(mContext, NewsCommonFragment.NEWS_ACCOUNT);
                        NewsHelper.clearUnreadByType(NewsCommonFragment.NEWS_ACCOUNT);
                        break;
                }
            }
        });
    }

    @Override
    public void initData() {
        loadCacheData();
        loadData();
        EventBus.getDefault().post(new BaseEvent(EventConstant.NEWS_COME_UNREAD));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Subscribe
    public void eventBus(BaseEvent event) {
        switch (event.getType()) {
            case EventConstant.NEWS_OVERVIEW_CHANGE:
                loadData();
                break;
            case EventConstant.NEWS_UNREAD_CHANGE:
                initDotView();
                break;
        }
    }

    private void loadCacheData() {
        Log.e(TAG, "loadCacheData: ");
        mCache = new EntityCache<>(mContext, NewsOverviewBean.class);
        List<NewsOverviewBean> mCacheData = mCache.getListEntity(NewsOverviewBean.class);
        if (mCacheData != null) {
            for (NewsOverviewBean cache : mCacheData) {
                switch (cache.getTypeId()) {
                    case NewsTypeConstants.NEWS_ANNOUNCEMENT:
                        cache.setResId(R.mipmap.ic_news_announcement);
                        cache.setNum(Session.getNewsAnnouncementNum());
                        break;
                    case NewsTypeConstants.NEWS_SYSTEM:
                        cache.setResId(R.mipmap.ic_news_system_information);
                        cache.setNum(Session.getNewsSystemInformationNum());
                        break;
                    case NewsTypeConstants.NEWS_INTERACTIVE:
                        cache.setResId(R.mipmap.ic_news_interactive);
                        cache.setNum(Session.getNewsCommentNum() + Session.getNewsQaNum() + Session.getNewsGroupNum() + Session.getNewsActivityNum());
                        break;
                    case NewsTypeConstants.NEWS_RECOMMEND:
                        cache.setResId(R.mipmap.ic_news_recommend);
                        cache.setNum(Session.getNewsRecommendNum());
                        break;
                    case NewsTypeConstants.NEWS_ACCOUNT:
                        cache.setResId(R.mipmap.ic_news_account_information);
                        cache.setNum(Session.getNewsAccountInformationNum());
                        break;
                }
            }
        } else {
            mCacheData = new ArrayList<>();
            for (int i = 0; i < mTypeId.size(); i++) {
                NewsOverviewBean item = new NewsOverviewBean();
                item.setDesc(getString(R.string.news_no_data_now));
                item.setTypeId(mTypeId.get(i));
                item.setTitle(getString(mTitleId.get(i)));
                item.setResId(mImageId.get(i));
                mCacheData.add(item);
            }
        }
        mAdapter.setData(mCacheData);
        mCache.putListEntity(mCacheData);
    }

    private void loadData() {
        Log.e(TAG, "loadData: ");
        Http.getApiService(ApiNewsService.class).newsOverview(Session.getUserId()).enqueue(new BaseCallback<ResponseData<HashMap<String, NewsBean>>>() {
            @Override
            public void onResponse(Call<ResponseData<HashMap<String, NewsBean>>> call, Response<ResponseData<HashMap<String, NewsBean>>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful() && response.body().isSuccessful()) {
                    initMessageOverview(response.body().getData());
                    initDotView();
                }
            }
        });
    }

    private void initMessageOverview(HashMap<String, NewsBean> data) {
        Log.e(TAG, "initMessageOverview: ");
        List<NewsOverviewBean> mCacheData = mCache.getListEntity(NewsOverviewBean.class);
        for (NewsOverviewBean cache : mCacheData) {
            Integer id = cache.getTypeId();
            if (data.containsKey(String.valueOf(id))) {
                NewsBean item = data.get(String.valueOf(id));
                for (NewsOverviewBean bean : mAdapter.getData()) {
                    if (bean.getTypeId() == id) {
                        switch (id) {
                            case NewsTypeConstants.NEWS_ANNOUNCEMENT:
                            case NewsTypeConstants.NEWS_RECOMMEND:
                                bean.setDesc(item.getTitle());
                                cache.setDesc(item.getTitle());
                                break;
                            case NewsTypeConstants.NEWS_SYSTEM:
                            case NewsTypeConstants.NEWS_INTERACTIVE:
                            case NewsTypeConstants.NEWS_ACCOUNT:
                                bean.setDesc(item.getContent());
                                cache.setDesc(item.getContent());
                                break;
                        }
                        bean.setTime(item.getCreateTime());
                        cache.setTime(item.getCreateTime());
                    }
                }
            }
        }
        mCache.putListEntity(mCacheData);
        mAdapter.notifyDataSetChanged();
    }

    private void initDotView() {
        Log.e(TAG, "initDotView: ");
        List<NewsOverviewBean> data = mAdapter.getData();
        for (int i = 0; i < data.size(); i++) {
            NewsOverviewBean item = data.get(i);
            switch (item.getTypeId()) {
                case NewsTypeConstants.NEWS_ANNOUNCEMENT:
                    item.setNum(Session.getNewsAnnouncementNum());
                    break;
                case NewsTypeConstants.NEWS_SYSTEM:
                    item.setNum(Session.getNewsSystemInformationNum());
                    break;
                case NewsTypeConstants.NEWS_INTERACTIVE:
                    item.setNum(Session.getNewsCommentNum() + Session.getNewsQaNum() + Session.getNewsGroupNum() + Session.getNewsActivityNum());
                    break;
                case NewsTypeConstants.NEWS_RECOMMEND:
                    item.setNum(Session.getNewsRecommendNum());
                    break;
                case NewsTypeConstants.NEWS_ACCOUNT:
                    item.setNum(Session.getNewsAccountInformationNum());
                    break;
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void refreshPage() {

    }
}
