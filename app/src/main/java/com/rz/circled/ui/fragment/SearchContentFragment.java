package com.rz.circled.ui.fragment;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.rz.circled.R;
import com.rz.circled.adapter.DynamicAdapter;
import com.rz.circled.presenter.impl.SearchPresenter;
import com.rz.common.constant.CommonCode;
import com.rz.common.event.BaseEvent;
import com.rz.common.event.NotifyEvent;
import com.rz.common.ui.fragment.BaseFragment;
import com.rz.httpapi.bean.CircleDynamic;
import com.rz.httpapi.bean.UserInfoBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.rz.circled.R.layout.fragment_search_content;

/**
 * Created by Gsm on 2017/9/2.
 */
public class SearchContentFragment extends BaseFragment {

    @BindView(R.id.lv_search_content)
    ListView lvContent;
    private DynamicAdapter dynamicAdapter;
    private List<CircleDynamic> circleDynamicList = new ArrayList<>();
    private SearchPresenter searchPresenter;

    public static SearchContentFragment newInstance() {
        SearchContentFragment frg = new SearchContentFragment();
        return frg;
    }

    @Nullable
    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_search_content, null);
    }

    @Override
    public void initView() {
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);


        //泛型要改
        dynamicAdapter = new DynamicAdapter(mActivity, circleDynamicList);
        lvContent.setAdapter(dynamicAdapter);
    }

    @Override
    public void initData() {

    }


    @Override
    public void initPresenter() {
        super.initPresenter();
        //搜索接口
        searchPresenter = new SearchPresenter();
    }

    /**
     * @param baseEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BaseEvent baseEvent) {
        if (baseEvent.type == CommonCode.EventType.SEARCH_KEYWORD && baseEvent.data != null && searchPresenter != null) {
            //去搜索

//            searchPresenter.searchPerson();
        }
    }

    @Override
    public <T> void updateView(T t) {
        super.updateView(t);
        if (null != t) {
            if (t instanceof UserInfoBean) {
                UserInfoBean model = (UserInfoBean) t;
                if (null != model) {

//                    NotifyEvent notifyEvent = new NotifyEvent("register", model, true);
//                    EventBus.getDefault().post(notifyEvent);

                    /**
                     * 更新界面，更新adapter数据
                     */

                }
            } else {
                BaseEvent event = new BaseEvent();
                event.info = "1";
                EventBus.getDefault().post(event);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }
}
