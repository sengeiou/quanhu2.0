package com.rz.circled.ui.fragment;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.rz.circled.R;
import com.rz.circled.presenter.impl.V3CirclePresenter;
import com.rz.circled.ui.activity.SearchActivity;
import com.rz.circled.ui.activity.WebContainerActivity;
import com.rz.circled.widget.AutoRollLayout;
import com.rz.common.cache.preference.Session;
import com.rz.common.ui.fragment.BaseFragment;
import com.rz.httpapi.bean.BannerAddSubjectModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Gsm on 2017/8/29.
 */
public class HomeFragment extends BaseFragment {
    @BindView(R.id.auto_viewpager)
    AutoRollLayout mAuto_viewpager;
    @BindView(R.id.et_home_custId)
    EditText etCustId;
    private List<BannerAddSubjectModel> bannerList = new ArrayList<>();

    @Nullable
    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_home, null);
    }

    @Override
    public void initPresenter() {
        V3CirclePresenter presenter = new V3CirclePresenter();
        presenter.attachView(this);
        presenter.getBannerList(5);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public <T> void updateViewWithFlag(T t, int flag) {
        super.updateViewWithFlag(t, flag);
        if (flag == 5) {
            bannerList.clear();
            bannerList.addAll((List<BannerAddSubjectModel>) t);
            mAuto_viewpager.setItems(bannerList);
            if (bannerList.size() == 1) {
                mAuto_viewpager.setAutoRoll(false);
                mAuto_viewpager.setScrollble(false);
            } else {
                mAuto_viewpager.setAutoRoll(true);
            }
            mAuto_viewpager.notifyData();
        }
    }

    @OnClick({R.id.tv_home_web, R.id.tv_home_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_home_web:
                String s = etCustId.getText().toString();
                if (!TextUtils.isEmpty(s))
                    Session.setUserId(s);
                startActivity(new Intent(getActivity(), WebContainerActivity.class));
                break;
            case R.id.tv_home_search:
                startActivity(new Intent(getActivity(), SearchActivity.class));
                break;
        }
    }
}
