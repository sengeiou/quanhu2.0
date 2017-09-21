package com.rz.circled.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rz.circled.R;
import com.rz.circled.presenter.impl.CirclePresenter;
import com.rz.circled.widget.CommonAdapter;
import com.rz.circled.widget.ViewHolder;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.httpapi.bean.HotSubjectModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/9/4/004.
 */

public class MoreSubjectActivity extends BaseActivity {
    @BindView(R.id.lv_subject)
    ListView mLvSubject;
    @BindView(R.id.et_search_keyword_base)
    EditText mEtSearchKeywordBase;
    private List<HotSubjectModel> subjectList = new ArrayList<>();
    private CommonAdapter<HotSubjectModel> mAdapter;

    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.more_subject_layout, null);
    }

    @Override
    public void initPresenter() {
        CirclePresenter presenter = new CirclePresenter();
        presenter.attachView(this);
        presenter.getMoreSubjectList(50, 0);
    }

    @Override
    public void initView() {
        setTitleText("话题");
        mAdapter = new CommonAdapter<HotSubjectModel>(mContext, subjectList, R.layout.activity_item) {
            @Override
            public void convert(ViewHolder helper, HotSubjectModel item) {
                helper.getView(R.id.ll_more_head).setVisibility(View.VISIBLE);
                helper.getView(R.id.ll_more_foot).setVisibility(View.VISIBLE);
                helper.getView(R.id.acitvity_des).setVisibility(View.VISIBLE);
                ImageView civ_head = helper.getView(R.id.civ_head);
                ImageView civ_thumbnail = helper.getView(R.id.iv_activity_icon);
                Glide.with(mContext).load(item.getThumbnail()).into(civ_thumbnail);
                TextView tv_name = helper.getView(R.id.tv_name);
                tv_name.setText(item.getCustNname());
                TextView tv_title = helper.getView(R.id.activity_title);
                tv_title.setText(item.getTitle());
                TextView tv_des = helper.getView(R.id.acitvity_des);
                tv_des.setText(item.getContent());
                TextView tv_talk_count = helper.getView(R.id.tv_talk_count);
                tv_talk_count.setText(item.getPartNum() + "讨论");
                TextView tv_where = helper.getView(R.id.tv_where);
                tv_where.setText("来自私圈" + item.getCoterieName());

            }
        };
        mLvSubject.setAdapter(mAdapter);

    }

    @Override
    public <T> void updateView(T t) {
        if (null != t) {
            subjectList.addAll((Collection<? extends HotSubjectModel>) t);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void initData() {
        mEtSearchKeywordBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent itnent = new Intent();
                itnent.putExtra(SearchActivity.SEARCH_TYPE,SearchActivity.TYPE_PERSON);
                skipActivity(aty,SearchActivity.class);
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void refreshPage() {

    }
}
