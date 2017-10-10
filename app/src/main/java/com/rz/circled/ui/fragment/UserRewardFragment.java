package com.rz.circled.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
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
import com.rz.common.constant.CommonCode;
import com.rz.common.constant.IntentKey;
import com.rz.common.event.BaseEvent;
import com.rz.common.ui.fragment.BaseFragment;
import com.rz.httpapi.bean.MyRewardBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/9/14 0014.
 */

public class UserRewardFragment extends BaseFragment implements ScrollableHelper.ScrollableContainer {

    @BindView(R.id.my_create_txt)
    TextView myCreateTxt;
    @BindView(R.id.fragment1)
    FrameLayout fragment;
    @BindView(R.id.answer_txt)
    TextView answerTxt;

    private List<Fragment> mFragments = new ArrayList<>();

    private String userId = "";

    public static UserRewardFragment newInstance(String userid) {
        UserRewardFragment frg = new UserRewardFragment();
        Bundle args = new Bundle();
        args.putString(IntentKey.KEY_TYPE,userid);
        frg.setArguments(args);

        return frg;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userId = getArguments().getString(IntentKey.KEY_TYPE);
    }

    @Nullable
    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_my_user_reward, null);
    }

    @Override
    public void initView() {
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);

        if(!Session.getUserId().equals(userId)){
            myCreateTxt.setText("他发起的悬赏");
            answerTxt.setText("他回答的悬赏");
        }else{
            myCreateTxt.setText("我发起的悬赏");
            answerTxt.setText("我回答的悬赏");
        }
        initFragments();
    }

    @Override
    public void initData() {
        switchPages(0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void eventBus(BaseEvent event) {
    }

    @Override
    public void refreshPage() {
    }

    @OnClick({R.id.my_create_txt, R.id.answer_txt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.my_create_txt:
                myCreateTxt.setTextColor(getResources().getColor(R.color.tab_blue));
                myCreateTxt.setBackgroundResource(R.drawable.shape_blue_bg_stroke);

                answerTxt.setTextColor(getResources().getColor(R.color.color_999999));
                answerTxt.setBackgroundResource(R.drawable.shape_white_bg_stroke);

                switchPages(0);
                break;
            case R.id.answer_txt:

                myCreateTxt.setTextColor(getResources().getColor(R.color.color_999999));
                myCreateTxt.setBackgroundResource(R.drawable.shape_white_bg_stroke);

                answerTxt.setTextColor(getResources().getColor(R.color.tab_blue));
                answerTxt.setBackgroundResource(R.drawable.shape_blue_bg_stroke);
                switchPages(1);
                break;
        }
    }

    private void initFragments() {
        BaseFragment publicRewardFragment = PublicRewardFragment.newInstance("0", userId);
        BaseFragment answerRewardFragment = PublicRewardFragment.newInstance("1", userId);
        mFragments.add(publicRewardFragment);
        mFragments.add(answerRewardFragment);
    }

    private void switchPages(int index) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment;
        for (int i = 0, j = mFragments.size(); i < j; i++) {
            if (i == index) {
                continue;
            }
            fragment = mFragments.get(i);
            if (fragment.isAdded()) {
                fragmentTransaction.hide(fragment);
            }
        }
        fragment = mFragments.get(index);
        if (fragment.isAdded()) {
            fragmentTransaction.show(fragment);
        } else {
            fragmentTransaction.add(R.id.fragment1, fragment);
        }
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public View getScrollableView() {
        return mFragments.get(0).getView().findViewById(R.id.lv_search_content);
    }
}
