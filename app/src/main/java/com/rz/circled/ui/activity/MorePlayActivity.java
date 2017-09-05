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

public class MorePlayActivity extends BaseActivity {
    @BindView(R.id.lv_play)
    ListView mLvPlay;
    private List<HotSubjectModel> subjectList = new ArrayList<>();

    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.more_activity_layout, null);
    }

    @Override
    public void initView() {
        subjectList.add(new HotSubjectModel());
        subjectList.add(new HotSubjectModel());
        subjectList.add(new HotSubjectModel());
        mLvPlay.setAdapter(new CommonAdapter<HotSubjectModel>(mContext,subjectList,R.layout.activity_item) {
            @Override
            public void convert(ViewHolder helper, HotSubjectModel item) {

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
