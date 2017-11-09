package com.rz.circled.ui.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.rz.circled.R;
import com.rz.circled.adapter.AllCircleAdapter;
import com.rz.circled.ui.fragment.AllCircleFragment;
import com.rz.circled.widget.PagerSlidingTabStripHome;
import com.rz.common.ui.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/11/8/008.
 */

public class AllCirclesActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.tab_allcircle)
    PagerSlidingTabStripHome tabPagerCircle;
    @BindView(R.id.vp_allcircle_main)
    ViewPager mViewPager;
    @BindView(R.id.title_content)
    FrameLayout mTitleContent;
    AllCircleFragment[] fragments;
    AllCircleAdapter mAdapter;
    ImageView mIvBaseTitleLeft;
    TextView mTvBaseTitleRight;
    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.allcircles_layout, null);
    }
    @Override
    protected boolean needShowTitle() {
        return false;
    }
    @Override
    public void initView() {
        fragments = new AllCircleFragment[]{AllCircleFragment.newInstance(0), AllCircleFragment.newInstance(1)};

        tabPagerCircle.setCustomLayoutParams(2);
        tabPagerCircle.setLineFitFont(true);
        mAdapter = new AllCircleAdapter(getSupportFragmentManager(),fragments);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(2);

        tabPagerCircle.setViewPager(mViewPager);
        tabPagerCircle.notifyDataSetChanged();
    }

    @Override
    public void initData() {
        View v = View.inflate(this, R.layout.all_circle_head, null);
        mTitleContent.addView(v);
        mIvBaseTitleLeft = (ImageView) v.findViewById(R.id.all_circle__title_left);
        mTvBaseTitleRight = (TextView) v.findViewById(R.id.all_circle_title_right);
        mIvBaseTitleLeft.setOnClickListener(this);
        mTvBaseTitleRight.setOnClickListener(this);
    }

    @Override
    public void refreshPage() {

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.all_circle__title_left:
                finish();
                break;
            case R.id.all_circle_title_right:
//                if (isEdit) {
//                    isEdit = false;
//                    mTvBaseTitleRight.setText(R.string.edit);
//                    mapFavCircle();
//                } else {
//                    isEdit = true;
//                    mTvBaseTitleRight.setText(R.string.finish);
//                }
//                publishedAdapter.notifyDataSetChanged();
                break;


        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
