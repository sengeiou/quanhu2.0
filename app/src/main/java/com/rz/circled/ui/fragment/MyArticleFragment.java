package com.rz.circled.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.cpoopc.scrollablelayoutlib.ScrollableHelper;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.rz.circled.R;
import com.rz.circled.adapter.ArticleAdapter;
import com.rz.circled.adapter.DynamicAdapter;
import com.rz.circled.event.EventConstant;
import com.rz.circled.presenter.IPresenter;
import com.rz.circled.presenter.impl.FriendPresenter1;
import com.rz.circled.presenter.impl.PersonInfoPresenter;
import com.rz.circled.ui.activity.WebContainerActivity;
import com.rz.circled.widget.CommomUtils;
import com.rz.circled.widget.MListView;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.CommonCode;
import com.rz.common.constant.IntentKey;
import com.rz.common.event.BaseEvent;
import com.rz.common.ui.fragment.BaseFragment;
import com.rz.common.utils.StringUtils;
import com.rz.httpapi.bean.CircleDynamic;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.rz.common.constant.IntentKey.EXTRA_TYPE;

/**
 * Created by Administrator on 2017/9/14 0014.
 */

public class MyArticleFragment extends BaseFragment implements ScrollableHelper.ScrollableContainer {

    @BindView(R.id.swipyrefreshlayout)
    SwipyRefreshLayout refreshLayout;
    @BindView(R.id.my_listview)
    MListView mListView;

    private ArticleAdapter dynamicAdapter;
    private List<CircleDynamic> circleDynamicList = new ArrayList<>();
    private IPresenter presenter;
    private String userid = "";

    public static MyArticleFragment newInstance(String userid) {
        MyArticleFragment frg = new MyArticleFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_TYPE, userid);
        frg.setArguments(args);
        return frg;
    }

    @Nullable
    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_article, null);
    }

    @Override
    public void initPresenter() {
        super.initPresenter();
        presenter = new PersonInfoPresenter();
        presenter.attachView(this);
        userid = getArguments().getString(IntentKey.EXTRA_TYPE);
        ((PersonInfoPresenter) presenter).getPersionArticle(false, userid ,"1000");

    }
    private void initRefresh() {
        refreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTH);
        refreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                if (direction == SwipyRefreshLayoutDirection.TOP) {
                    ((PersonInfoPresenter) presenter).getPersionArticle(false, userid ,"1000");
                } else {
                    ((PersonInfoPresenter) presenter).getPersionArticle(true, userid ,"1000");
                }
                refreshLayout.setRefreshing(false);
            }
        });
    }


    @Override
    public void initView() {

//        if (!EventBus.getDefault().isRegistered(this))
//            EventBus.getDefault().register(this);

        dynamicAdapter = new ArticleAdapter(mActivity, circleDynamicList);
        mListView.setAdapter(dynamicAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(position != circleDynamicList.size()){
                    if (StringUtils.isEmpty(circleDynamicList.get(position).coterieId)||StringUtils.isEmpty(circleDynamicList.get(position).coterieName)) {
                        String circleUrl = CommomUtils.getCircleUrl(circleDynamicList.get(position).circleRoute,circleDynamicList.get(position).moduleEnum, circleDynamicList.get(position).resourceId);
                        WebContainerActivity.startActivity(mActivity, circleUrl);
                    } else {
                        String url = CommomUtils.getDymanicUrl(circleDynamicList.get(position).circleRoute,circleDynamicList.get(position).moduleEnum, circleDynamicList.get(position).coterieId, circleDynamicList.get(position).resourceId);
                        WebContainerActivity.startActivity(mActivity, url);
                    }
                }
            }
        });
    }

    @Override
    public void initData() {
        initRefresh();
    }

    @Override
    public <T> void updateViewWithLoadMore(T t, boolean loadMore) {
        super.updateViewWithLoadMore(t, loadMore);
        List<CircleDynamic> mDatas = (List<CircleDynamic>) t;
        if (null != mDatas && !mDatas.isEmpty()) {
            if (!loadMore) {
                circleDynamicList.clear();
            }
            circleDynamicList.addAll(mDatas);
            dynamicAdapter.notifyDataSetChanged();
        } else {
            if (!loadMore) {
                circleDynamicList.clear();
            }
            dynamicAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected boolean needLoadingView() {
        return true;
    }

    @Override
    protected boolean hasDataInPage() {
        return dynamicAdapter != null && dynamicAdapter.getCount() != 0;
    }

    @Override
    public void refreshPage() {
        ((PersonInfoPresenter) presenter).getPersionArticle(false, userid ,"1000");
    }

    @Override
    public View getScrollableView() {
        return mListView;
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEvent(BaseEvent baseEvent) {
//        if (baseEvent.type == CommonCode.EventType.TYPE_ADD_LAYOUT) {
//            if(mListView.getFooterViewsCount()<=0){
//
//                LayoutInflater inflater = LayoutInflater.from(mActivity);
//                View view = inflater.inflate(R.layout.foot_view, null);
//
////                View view = LayoutInflater.inflate()inflate(R.layout.foot_view, null);
////                mListView.addFooterView(view);
////                dynamicAdapter.notifyDataSetChanged();
////                mListView.setAdapter(dynamicAdapter);
//            }
//        }
//    }

//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        if (EventBus.getDefault().isRegistered(this)) {
//            EventBus.getDefault().unregister(this);
//        }
//    }
}
