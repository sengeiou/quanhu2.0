package com.rz.circled.ui.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.rz.circled.R;
import com.rz.circled.widget.CommonAdapter;
import com.rz.circled.widget.ViewHolder;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.httpapi.bean.HotSubjectModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/9/4/004.
 */

public class MoreSubjectActivity extends BaseActivity {
    @BindView(R.id.lv_subject)
    ListView mLvSubject;
    private List<HotSubjectModel> subjectList = new ArrayList<>();

    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.more_subject_layout, null);
    }

    @Override
    public void initView() {
        setTitleText("话题");
        subjectList.add(new HotSubjectModel());
        subjectList.add(new HotSubjectModel());
        subjectList.add(new HotSubjectModel());
        mLvSubject.setAdapter(new CommonAdapter<HotSubjectModel>(mContext,subjectList,R.layout.activity_item) {
            @Override
            public void convert(ViewHolder helper, HotSubjectModel item) {
               helper.getView(R.id.ll_more_head).setVisibility(View.VISIBLE);
               helper.getView(R.id.ll_more_foot).setVisibility(View.VISIBLE);
               helper.getView(R.id.acitvity_des).setVisibility(View.VISIBLE);


            }
        });

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
}
