package com.rz.circled.ui.activity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.rz.circled.R;
import com.rz.circled.adapter.NewsOverviewAdapter;
import com.rz.circled.constants.NewsTypeConstants;
import com.rz.circled.event.EventConstant;
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
import com.rz.httpapi.bean.NewsUnreadBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by rzw2 on 2017/9/5.
 */

public class NewsActivity extends BaseActivity {

    @BindView(R.id.lv)
    ListView lv;

    private static Integer[] IMAGE = {
            R.mipmap.ic_news_announcement,
            R.mipmap.ic_news_system_information,
            R.mipmap.ic_news_interactive,
            R.mipmap.ic_news_recommend,
            R.mipmap.ic_news_account_information};
    private List<Integer> mImage = Arrays.asList(IMAGE);
    private static Integer[] TITLE = {
            R.string.announcement,
            R.string.system_information,
            R.string.interactive_information,
            R.string.recommend_information,
            R.string.account_information,};
    private List<Integer> mTitle = Arrays.asList(TITLE);
    private static Integer[] TYPEID = {
            NewsTypeConstants.NEWS_ANNOUNCEMENT,
            NewsTypeConstants.NEWS_SYSTEM,
            NewsTypeConstants.NEWS_INTERACTIVE,
            NewsTypeConstants.NEWS_RECOMMEND,
            NewsTypeConstants.NEWS_ACCOUNT,};
    private List<Integer> mTypeId = Arrays.asList(TYPEID);
    private NewsOverviewAdapter mAdapter;
    private EntityCache<NewsOverviewBean> mCache;

    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_news, null);
    }

    @Override
    public void initView() {
        lv.setAdapter(mAdapter = new NewsOverviewAdapter(mContext, R.layout.item_news));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        NewsFragmentActivity.startNewsFragment(mContext, NewsCommonFragment.NEWS_ANNOUNCEMENT);
                        break;
                    case 1:
                        NewsFragmentActivity.startNewsFragment(mContext, NewsCommonFragment.NEWS_SYSTEM_INFORMATION);
                        break;
                    case 2:
                        startActivity(new Intent(mContext, NewsInteractiveActivity.class));
                        break;
                    case 3:
                        NewsFragmentActivity.startNewsFragment(mContext, NewsCommonFragment.NEWS_RECOMMEND);
                        break;
                    case 4:
                        NewsFragmentActivity.startNewsFragment(mContext, NewsCommonFragment.NEWS_ACCOUNT);
                        break;
                }
            }
        });
    }

    @Override
    public void initData() {
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        loadCacheData();
        loadData();
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
            case EventConstant.NEWS_COME_UNREAD:
                loadData();
                break;
            case EventConstant.NEWS_ANNOUNCEMENT_UNREAD_CHANGE:
            case EventConstant.NEWS_ACCOUNT_INFORMATION_UNREAD_CHANGE:
            case EventConstant.NEWS_SYSTEM_INFORMATION_UNREAD_CHANGE:
            case EventConstant.NEWS_RECOMMEND_UNREAD_CHANGE:
            case EventConstant.NEWS_COMMENT_UNREAD_CHANGE:
            case EventConstant.NEWS_QA_UNREAD_CHANGE:
            case EventConstant.NEWS_GROUP_UNREAD_CHANGE:
            case EventConstant.NEWS_ACTIVITY_UNREAD_CHANGE:
                initDotView();
                break;
        }
    }

    private void loadCacheData() {
        mCache = new EntityCache<>(mContext, NewsOverviewBean.class);
        List<NewsOverviewBean> data = new ArrayList<>();
        if (mCache.getListEntity(NewsOverviewBean.class) == null) {
            for (int i = 0; i < mTypeId.size(); i++) {
                NewsOverviewBean item = new NewsOverviewBean();
                item.setTitle(getString(mTitle.get(i)));
                item.setDesc(getString(R.string.news_no_data_now));
                item.setResId(mImage.get(i));
                item.setTypeId(mTypeId.get(i));
                data.add(item);
            }
        } else {
            List<NewsOverviewBean> mCacheData = mCache.getListEntity(NewsOverviewBean.class);
            for (int i = 0; i < mCacheData.size(); i++) {
                NewsOverviewBean item = mCacheData.get(i);
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
            data.addAll(mCacheData);
        }
        mCache.putListEntity(data);
        mAdapter.setData(data);
    }

    private void loadData() {
        Http.getApiService(ApiNewsService.class).newsUnread(Session.getUserId()).enqueue(new BaseCallback<ResponseData<HashMap<String, String>>>() {
            @Override
            public void onResponse(Call<ResponseData<HashMap<String, String>>> call, Response<ResponseData<HashMap<String, String>>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful() && response.body().isSuccessful()) {
                    HashMap<String, String> data = response.body().getData();
                    parsesData(data);
                    initDotView();
                }
            }
        });
        Http.getApiService(ApiNewsService.class).newsOverview(Session.getUserId()).enqueue(new BaseCallback<ResponseData<HashMap<String, NewsBean>>>() {
            @Override
            public void onResponse(Call<ResponseData<HashMap<String, NewsBean>>> call, Response<ResponseData<HashMap<String, NewsBean>>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful() && response.body().isSuccessful()) {
                    initMessageOverview(response.body().getData());
                }
            }
        });
    }

    private void parsesData(HashMap<String, String> data) {
        List<NewsUnreadBean> unreadBeanList = new ArrayList<>();
        for (String key : data.keySet()) {
            int num = Integer.parseInt(data.get(key));
            String[] types = key.split("\\|");
            int type = 0;
            int label = 0;
            if (types.length > 1) {
                type = Integer.parseInt(types[0]);
                label = Integer.parseInt(types[1]);
            } else {
                type = Integer.parseInt(types[0]);
            }
            unreadBeanList.add(new NewsUnreadBean(type, label, num));
        }

        Map<String, Integer> map = new HashMap<>();
        for (NewsUnreadBean item : unreadBeanList) {
            Integer val = map.get(item.getType());
            if (item.getType() == NewsTypeConstants.NEWS_INTERACTIVE) {
                if (val == null) {
                    map.put(String.valueOf(item.getLabel()), item.getNum());
                } else {
                    map.put(String.valueOf(item.getLabel()), val + item.getNum());
                }
            } else {
                if (val == null) {
                    map.put(String.valueOf(item.getType()), item.getNum());
                } else {
                    map.put(String.valueOf(item.getType()), val + item.getNum());
                }
            }
        }

        Session.setNewsAnnouncementNum(map.get(NewsTypeConstants.NEWS_ANNOUNCEMENT) != null ? map.get(NewsTypeConstants.NEWS_ANNOUNCEMENT) : Session.getNewsAnnouncementNum());
        Session.setNewsSystemInformationNum(map.get(NewsTypeConstants.NEWS_SYSTEM) != null ? map.get(NewsTypeConstants.NEWS_SYSTEM) : Session.getNewsSystemInformationNum());
        Session.setNewsAccountInformationNum(map.get(NewsTypeConstants.NEWS_ACCOUNT) != null ? map.get(NewsTypeConstants.NEWS_ACCOUNT) : Session.getNewsAccountInformationNum());
        Session.setNewsRecommendNum(map.get(NewsTypeConstants.NEWS_RECOMMEND) != null ? map.get(NewsTypeConstants.NEWS_RECOMMEND) : Session.getNewsRecommendNum());
        Session.setNewsCommentNum(map.get(NewsTypeConstants.NEWS_COMMENT) != null ? map.get(NewsTypeConstants.NEWS_COMMENT) : Session.getNewsCommentNum());
        Session.setNewsQaNum(map.get(NewsTypeConstants.NEWS_ANSWER) != null ? map.get(NewsTypeConstants.NEWS_ANSWER) : Session.getNewsQaNum());
        Session.setNewsGroupNum(map.get(NewsTypeConstants.NEWS_GROUP) != null ? map.get(NewsTypeConstants.NEWS_GROUP) : Session.getNewsGroupNum());
        Session.setNewsActivityNum(map.get(NewsTypeConstants.NEWS_ACTIVITY) != null ? map.get(NewsTypeConstants.NEWS_ACTIVITY) : Session.getNewsActivityNum());
    }

    private void initDotView() {
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

    private void initMessageOverview(HashMap<String, NewsBean> data) {
        List<NewsOverviewBean> mCacheData = mCache.getListEntity(NewsOverviewBean.class);
        for (NewsOverviewBean cache : mCacheData) {
            Integer id = cache.getTypeId();
            if (data.containsKey(String.valueOf(id))) {
                NewsBean item = data.get(String.valueOf(id));
                for (NewsOverviewBean bean : mAdapter.getData()) {
                    if (bean.getTypeId() == id) {
                        bean.setDesc(item.getTitle());
                        bean.setTime(item.getCreateTime());
                        cache.setDesc(item.getTitle());
                        cache.setTime(item.getCreateTime());
                    }
                }
            }
        }
        mCache.putListEntity(mCacheData);
        mAdapter.notifyDataSetChanged();
    }
}
