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
import com.rz.circled.adapter.RewardAdapter;
import com.rz.circled.helper.CommonH5JumpHelper;
import com.rz.circled.presenter.IPresenter;
import com.rz.circled.presenter.impl.PersonInfoPresenter;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.IntentKey;
import com.rz.common.ui.fragment.BaseFragment;
import com.rz.httpapi.bean.MyRewardBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/9/14 0014.
 */

public class MyRewardFragment extends BaseFragment implements ScrollableHelper.ScrollableContainer {

    @BindView(R.id.refresh)
    SwipyRefreshLayout mRefresh;
    @BindView(R.id.lv_search_content)
    ListView lvReward;

    @BindView(R.id.my_create_txt)
    TextView createTxt;

    @BindView(R.id.answer_txt)
    TextView answerTxt;

    private RewardAdapter rewardAdapter;
    private List<MyRewardBean> rewardBeanList = new ArrayList<>();
    protected IPresenter presenter;

//    private View headView;
//    private TextView createTxt;
//    private TextView answerTxt;
    private String type;
    private String userid = "";

    public static MyRewardFragment newInstance(String type,String userid) {
        MyRewardFragment frg = new MyRewardFragment();
        Bundle args = new Bundle();
        args.putString(IntentKey.KEY_ID,type);
        args.putString(IntentKey.KEY_TYPE,userid);
        frg.setArguments(args);

        return frg;
    }


    @Nullable
    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_user_reward, null);
    }

    @Override
    public void initView() {
        type = getArguments().getString(IntentKey.KEY_ID);
        userid = getArguments().getString(IntentKey.KEY_TYPE);

        if("1".equals(type)){
//            headView = View.inflate(mActivity,R.layout.mine_top_layout,null);
//            createTxt = (TextView) headView.findViewById(R.id.my_create_txt);
//            answerTxt = (TextView) headView.findViewById(R.id.answer_txt);

            createTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createTxt.setTextColor(getResources().getColor(R.color.tab_blue));
                    createTxt.setBackgroundResource(R.drawable.shape_blue_bg_stroke);

                    answerTxt.setTextColor(getResources().getColor(R.color.color_999999));
                    answerTxt.setBackgroundResource(R.drawable.shape_white_bg_stroke);
                    ((PersonInfoPresenter) presenter).getMyreward(false, userid,0 ,-100);
                }
            });

            answerTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createTxt.setTextColor(getResources().getColor(R.color.color_999999));
                    createTxt.setBackgroundResource(R.drawable.shape_white_bg_stroke);

                    answerTxt.setTextColor(getResources().getColor(R.color.tab_blue));
                    answerTxt.setBackgroundResource(R.drawable.shape_blue_bg_stroke);

                    ((PersonInfoPresenter) presenter).getMyreward(false, userid,0 ,-100);
                }
            });

//            lvReward.addHeaderView(headView);
        }

        rewardAdapter = new RewardAdapter(getActivity(), R.layout.item_search_reward);
        rewardAdapter.setData(rewardBeanList);
        lvReward.setAdapter(rewardAdapter);

        lvReward.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CommonH5JumpHelper.startRewardDetail(mActivity,rewardBeanList.get(position).getId()+"");
            }
        });

    }

    @Override
    public void initData() {
        initRefresh();

        ((PersonInfoPresenter) presenter).getMyreward(false, userid,0 ,-100);

    }

    private void initRefresh() {
        mRefresh.setDirection(SwipyRefreshLayoutDirection.BOTTOM);
        mRefresh.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {

                if(rewardBeanList.size()>0){
                    ((PersonInfoPresenter) presenter).getMyreward(true, userid,0, rewardBeanList.get(rewardBeanList.size()-1).getId());
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
            List<MyRewardBean> mDatas = (List<MyRewardBean>) t;
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

    @Override
    public void refreshPage() {
        ((PersonInfoPresenter) presenter).getMyreward(false, userid,0 ,-100);
    }

    @Override
    public View getScrollableView() {
        return lvReward;
    }
}
