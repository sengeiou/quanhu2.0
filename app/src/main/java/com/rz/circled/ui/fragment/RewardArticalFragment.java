package com.rz.circled.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.cpoopc.scrollablelayoutlib.ScrollableHelper;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.rz.circled.R;
import com.rz.circled.adapter.MineRewardAdapter;
import com.rz.circled.helper.CommonH5JumpHelper;
import com.rz.circled.presenter.IPresenter;
import com.rz.circled.presenter.impl.PersonInfoPresenter;
import com.rz.circled.ui.activity.MyArticleActivity;
import com.rz.circled.ui.activity.WebContainerActivity;
import com.rz.circled.widget.CommomUtils;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.CommonCode;
import com.rz.common.constant.IntentKey;
import com.rz.common.event.BaseEvent;
import com.rz.common.ui.fragment.BaseFragment;
import com.rz.common.utils.StringUtils;
import com.rz.httpapi.bean.MineRewardBean;
import com.rz.httpapi.bean.RewardStatBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/9/14 0014.
 */

public class RewardArticalFragment extends BaseFragment implements ScrollableHelper.ScrollableContainer{

    protected IPresenter presenter;
    @BindView(R.id.refresh)
    SwipyRefreshLayout mRefresh;
    @BindView(R.id.lv_search_content)
    ListView lvReward;
    private MineRewardAdapter rewardAdapter;
    private List<MineRewardBean> rewardBeanList = new ArrayList<>();

    private int type;

    private TextView topTxt;

    public static RewardArticalFragment newInstance(int type) {
        RewardArticalFragment frg = new RewardArticalFragment();
        Bundle args = new Bundle();
        args.putInt(IntentKey.KEY_ID,type);
        frg.setArguments(args);

        return frg;
    }


    @Nullable
    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_mine_reward, null);
    }

    @Override
    public void initView() {
        type = getArguments().getInt(IntentKey.KEY_ID);

        View headView = View.inflate(mActivity, R.layout.mine_reward_top, null);
        topTxt = (TextView) headView.findViewById(R.id.reward_txt);
        lvReward.addHeaderView(headView);

        rewardAdapter = new MineRewardAdapter(getActivity(), R.layout.item_reward_mine);
        rewardAdapter.setData(rewardBeanList);
        lvReward.setAdapter(rewardAdapter);

        lvReward.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (StringUtils.isEmpty(rewardBeanList.get(position).getResourceInfo().getResourceId())||StringUtils.isEmpty(rewardBeanList.get(position).getResourceInfo())) {

                  if(position != 0){
                      String circleUrl = CommomUtils.getCircleUrl(rewardBeanList.get(position-1).getResourceInfo().getCircleRoute(),rewardBeanList.get(position-1).getResourceInfo().getModuleEnum(), rewardBeanList.get(position-1).getResourceInfo().getResourceId());
                      WebContainerActivity.startActivity(mActivity, circleUrl);
                  }

//                } else {
//                    String url = CommomUtils.getDymanicUrl(rewardBeanList.get(position).getResourceInfo().getCircleRoute(),rewardBeanList.get(position).getResourceInfo().getModuleEnum(), rewardBeanList.get(position).coterieId, rewardBeanList.get(position).getResourceInfo().getResourceId());
//                    WebContainerActivity.startActivity(mActivity.this, url);
//                }
            }
        });
    }

    @Override
    public void initData() {

        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);

        initRefresh();
        ((PersonInfoPresenter) presenter).getMyReward(false, Session.getUserId(),type,null);

    }

    private void initRefresh() {
        mRefresh.setDirection(SwipyRefreshLayoutDirection.BOTTOM);
        mRefresh.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {

                if(rewardBeanList.size() > 0){
                    ((PersonInfoPresenter) presenter).getMyReward(true, Session.getUserId(),type,rewardBeanList.get(rewardBeanList.size() -1).getRewardId());
                }else{
                    ((PersonInfoPresenter) presenter).getMyReward(false, Session.getUserId(),type,null);
                }

            mRefresh.setRefreshing(false);
            }
        });
    }

    @Override
    public void initPresenter() {
        super.initPresenter();
        presenter = new PersonInfoPresenter();
        presenter.attachView(this);

    }

    @Override
    public <T> void updateViewWithLoadMore(T t, boolean loadMore) {
        super.updateViewWithLoadMore(t, loadMore);
        if (t != null) {
            List<MineRewardBean> mDatas = (List<MineRewardBean>) t;
            if (null != mDatas && !mDatas.isEmpty()) {
                if (!loadMore) {
                    rewardBeanList.clear();
                }
                rewardBeanList.addAll(mDatas);
                rewardAdapter.setData(rewardBeanList);
                rewardAdapter.notifyDataSetChanged();

            } else {
                if (!loadMore) {
                    rewardBeanList.clear();
                }
                rewardAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected boolean needLoadingView() {
        return true;
    }

    @Override
    protected boolean hasDataInPage() {
        return rewardAdapter != null && rewardAdapter.getCount() != 0;
    }

    /**
     * @param baseEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BaseEvent baseEvent) {
        if (baseEvent.type == CommonCode.EventType.TYPE_REWARD_COUNT && baseEvent.data != null) {
            RewardStatBean model = (RewardStatBean) baseEvent.getData();

            if(type == 0){
                topTxt.setText("共"+model.getTotalRewardCount()+"条内容");
            }else{
                topTxt.setText("共"+model.getTotalRewardedCount()+"条内容");
            }

        }else if(baseEvent.type == CommonCode.EventType.TYPE_EMPTY_COUNT){
            topTxt.setText("共0条内容");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }


    @Override
    public void refreshPage() {
        ((PersonInfoPresenter) presenter).getMyReward(false, Session.getUserId(),type,null);
    }

    @Override
    public View getScrollableView() {
        return lvReward;
    }
}
