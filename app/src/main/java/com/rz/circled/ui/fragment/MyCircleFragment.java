package com.rz.circled.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cpoopc.scrollablelayoutlib.ScrollableHelper;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.rz.circled.R;
import com.rz.circled.event.EventConstant;
import com.rz.common.constant.CommonCode;
import com.rz.common.constant.IntentKey;
import com.rz.common.event.BaseEvent;
import com.rz.common.ui.fragment.BaseFragment;
import com.rz.common.utils.Utility;
import com.rz.common.widget.svp.SVProgressHUD;
import com.rz.httpapi.api.ApiPGService;
import com.rz.httpapi.api.BaseCallback;
import com.rz.httpapi.api.Http;
import com.rz.httpapi.api.ResponseData.ResponseData;
import com.rz.httpapi.bean.PrivateGroupBean;
import com.rz.httpapi.bean.PrivateGroupListBean;

import net.lucode.hackware.magicindicator.FragmentContainerHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

import static com.rz.common.constant.CommonCode.Constant.PAGE_SIZE;
import static com.rz.common.constant.IntentKey.EXTRA_ID;

/**
 * Created by Administrator on 2017/9/14 0014.
 */

public class MyCircleFragment extends BaseFragment implements ScrollableHelper.ScrollableContainer {

    @BindView(R.id.my_create_txt)
    TextView myCreateTxt;
    @BindView(R.id.answer_txt)
    TextView answerTxt;
    @BindView(R.id.fragment)
    FrameLayout fragment;

    private String userId;
    private List<Fragment> mFragments = new ArrayList<>();

    public static MyCircleFragment newInstance(String userId) {
        MyCircleFragment fragment = new MyCircleFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userId = getArguments().getString(IntentKey.EXTRA_ID);
    }

    @Nullable
    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_my_private_group, null);
    }

    @Override
    public void initView() {
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        myCreateTxt.setText("创建的私圈");
        answerTxt.setText("加入的私圈");

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
                switchPages(0);
                break;
            case R.id.answer_txt:
                switchPages(1);
                break;
        }
    }

    private void initFragments() {
        BaseFragment privateGroupJoinByMyselfFragment = PrivateGroupJoinByMyselfFragment.newInstance(PrivateGroupJoinByMyselfFragment.TYPE_ALL, userId);
        BaseFragment privateGroupCreateByMyselfFragment = PrivateGroupCreateByMyselfFragment.newInstance(PrivateGroupCreateByMyselfFragment.TYPE_ALL, userId);
        mFragments.add(privateGroupCreateByMyselfFragment);
        mFragments.add(privateGroupJoinByMyselfFragment);
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
            fragmentTransaction.add(R.id.fragment, fragment);
        }
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public View getScrollableView() {
        return null;
    }
}
