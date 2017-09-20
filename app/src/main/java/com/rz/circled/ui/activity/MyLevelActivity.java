package com.rz.circled.ui.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rz.circled.R;
import com.rz.circled.presenter.impl.LevelPersenter;
import com.rz.common.cache.preference.Session;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.widget.MyListView;
import com.rz.httpapi.bean.MyLevelAcountBean;

import butterknife.BindView;

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
    @BindView(R.id.level_pb)
    ProgressBar levelPb;
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
    private LevelPersenter presenter;
    private final int pageSize = 20;
    private final int pageNum = 1;

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
        Glide.with(mContext).load(Session.getUserPicUrl()).placeholder(R.drawable.ic_default_head).error(R.drawable.ic_default_head).into(ivMyLevelIcon);
    }

    @Override
    public void initData() {
        presenter.getLevelAcount();
        presenter.getLevelList(pageSize, pageNum);
    }

    @Override
    public <T> void updateViewWithFlag(T t, int flag) {
        super.updateViewWithFlag(t, flag);
        if (flag == presenter.FLAG_LEVEL_ACOUNT) {
            MyLevelAcountBean acountBean = (MyLevelAcountBean) t;
            if (acountBean == null) return;
            tvMyLevelCount.setText(acountBean.getGrowLevel() + "");
        }
        if (flag == presenter.FLAG_LEVEL_LIST) {

        }
    }
}
