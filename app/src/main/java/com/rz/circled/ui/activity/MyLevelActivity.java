package com.rz.circled.ui.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appyvet.rangebar.RangeBar;
import com.bumptech.glide.Glide;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.rz.circled.R;
import com.rz.circled.adapter.MyLevelAdapter;
import com.rz.circled.presenter.impl.LevelPersenter;
import com.rz.circled.widget.GlideCircleImage;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.H5Address;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.widget.MyListView;
import com.rz.httpapi.bean.MyLevelAcountBean;
import com.rz.httpapi.bean.MyLevelBean;

import java.util.List;

import butterknife.BindView;

import static com.rz.common.constant.CommonCode.Constant.PAGE_SIZE;

/**
 * Created by Administrator on 2017/9/16/016.
 */

public class MyLevelActivity extends BaseActivity {

    @BindView(R.id.iv_my_level_icon)
    ImageView ivMyLevelIcon;
    @BindView(R.id.tv_cur_level)
    TextView tvCurLevel;
    @BindView(R.id.tv_my_level_count)
    TextView tvMyLevelCount;
    @BindView(R.id.ll)
    LinearLayout ll;
    @BindView(R.id.tv_my_level_grow_count)
    TextView tvMyLevelGrowCount;
    @BindView(R.id.gourp_ll)
    LinearLayout gourpLl;
    @BindView(R.id.rangebar)
    RangeBar rangeBar;
    @BindView(R.id.lv_ten)
    TextView lvTen;
    @BindView(R.id.divider_one)
    View dividerOne;
    @BindView(R.id.detail)
    TextView detail;
    @BindView(R.id.time_ll)
    LinearLayout timeLl;
    @BindView(R.id.lv_level)
    MyListView lvLevel;
    @BindView(R.id.layout_refresh)
    SwipyRefreshLayout layoutRefresh;

    private LevelPersenter presenter;
    private MyLevelAdapter mAdapter;
    private int pageNum = 1;

    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.my_level_layout, null);
    }

    @Override
    public void initPresenter() {
        presenter = new LevelPersenter();
        presenter.attachView(this);
    }

    @Override
    public void initView() {
        setTitleText(R.string.my_level);
        setTitleRightText("升级攻略");
        Glide.with(mContext).load(Session.getUserPicUrl()).transform(new GlideCircleImage(mContext)).placeholder(R.drawable.ic_default_head).error(R.drawable.ic_default_head).into(ivMyLevelIcon);
        lvLevel.setAdapter(mAdapter = new MyLevelAdapter(mContext, R.layout.item_level));
        layoutRefresh.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                if (direction == SwipyRefreshLayoutDirection.TOP) {
                    pageNum = 1;
                    loadData(false);
                } else {
                    loadData(true);
                }
            }
        });
        setTitleRightListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonH5Activity.startCommonH5(mContext, "", H5Address.UPGRADE_STRATEGY);
            }
        });
    }

    @Override
    public void initData() {
        presenter.getLevelAcount();
        loadData(false);
    }

    @Override
    public <T> void updateViewWithFlag(T t, int flag) {
        super.updateViewWithFlag(t, flag);
        if (flag == presenter.FLAG_LEVEL_ACOUNT) {
            MyLevelAcountBean acountBean = (MyLevelAcountBean) t;
            if (acountBean == null) return;
            tvMyLevelCount.setText(acountBean.getGrowLevel());
            tvMyLevelGrowCount.setText(String.valueOf(acountBean.getGrow()));
            rangeBar.setSeekPinByIndex(Integer.parseInt(acountBean.getGrowLevel()));
        }
        if (flag == presenter.FLAG_LEVEL_LIST) {
            layoutRefresh.setRefreshing(false);
        }
    }

    @Override
    public <T> void updateViewWithLoadMore(T t, boolean loadMore) {
        super.updateViewWithLoadMore(t, loadMore);
        List<MyLevelBean> data = (List<MyLevelBean>) t;
        if (data != null && data.size() > 0) {
            if (loadMore) {
                mAdapter.addData(data);
            } else {
                mAdapter.setData(data);
            }
            pageNum++;
        }
    }

    private void loadData(boolean loadMore) {
        presenter.getLevelList(PAGE_SIZE, pageNum, loadMore);
    }

    @Override
    public void refreshPage() {

    }
}
