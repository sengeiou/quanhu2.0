package com.rz.circled.ui.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.rz.circled.R;
import com.rz.circled.presenter.impl.CirclePresenter;
import com.rz.circled.widget.CommonAdapter;
import com.rz.circled.widget.ViewHolder;
import com.rz.common.cache.preference.Session;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.httpapi.bean.CircleEntrModle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/9/6/006.
 */

public class FollowCircle extends BaseActivity {
    @BindView(R.id.btn_jump)
    Button mBtnJump;
    @BindView(R.id.btn_press)
    Button mBtnPress;
    @BindView(R.id.gv_follow)
    GridView mGvFollow;
    private List<CircleEntrModle> allCircle=new ArrayList<>();
    private CommonAdapter<CircleEntrModle> mAdapter;

    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.follow_circle_layout, null);
    }

    @Override
    public void initPresenter() {
        CirclePresenter presenter=new CirclePresenter();
        presenter.attachView(this);
        presenter.getCircleEntranceList(0);
    }

    @Override
    public void initView() {
        mAdapter = new CommonAdapter<CircleEntrModle>(mContext, allCircle, R.layout.follow_item) {
            @Override
            public void convert(ViewHolder helper, CircleEntrModle item) {
                ImageView ivFollow = (ImageView) helper.getView(R.id.iv_follow);
                Glide.with(mContext).load(item.circleIcon).into(ivFollow);
            }
        };
        mGvFollow.setAdapter(mAdapter);
        mGvFollow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

    }

    @Override
    protected boolean needShowTitle() {
        return false;
    }

    @Override
    public <T> void updateViewWithFlag(T t, int flag) {
        super.updateViewWithFlag(t, flag);
        if (t!=null){
            allCircle.addAll((Collection<? extends CircleEntrModle>) t);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void initData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_jump, R.id.btn_press})
    public void onClick(View view) {
        Session.setUserIsFirstDownload(false);
        switch (view.getId()) {
            case R.id.btn_jump:
                finish();
                break;
            case R.id.btn_press:

                break;

        }
    }
}
