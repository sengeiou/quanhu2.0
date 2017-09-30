package com.rz.circled.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;
import com.rz.circled.BuildConfig;
import com.rz.circled.R;
import com.rz.circled.presenter.impl.CirclePresenter;
import com.rz.circled.widget.CommomUtils;
import com.rz.circled.widget.CommonAdapter;
import com.rz.circled.widget.GlideCenterRoundImage;
import com.rz.circled.widget.GlideCircleImage;
import com.rz.circled.widget.ViewHolder;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.httpapi.bean.HotSubjectModel;
import com.yryz.yunxinim.uikit.common.util.string.StringUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

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
        mLvSubject.addFooterView(View.inflate(mContext,R.layout.item_footer,null));
        mAdapter = new CommonAdapter<HotSubjectModel>(mContext, subjectList, R.layout.activity_item) {
            @Override
            public void convert(ViewHolder helper, final HotSubjectModel item) {
                helper.getView(R.id.ll_more_head).setVisibility(View.VISIBLE);
                helper.getView(R.id.ll_more_foot).setVisibility(View.VISIBLE);
                helper.getView(R.id.acitvity_des).setVisibility(View.VISIBLE);
                ImageView civ_head = helper.getView(R.id.civ_head);
                ImageView civ_thumbnail = helper.getView(R.id.iv_activity_icon);
                civ_thumbnail.setVisibility(StringUtil.isEmpty(item.getThumbnail())?View.GONE:View.VISIBLE);
                Glide.with(mContext).load(item.getThumbnail()).placeholder(R.drawable.ic_circle_img1).transform(new GlideCenterRoundImage(mContext, 10)).into(civ_thumbnail);
                Glide.with(mContext).load(item.getCustImg()).placeholder(R.drawable.ic_default_head).transform(new GlideCircleImage(mContext)).into(civ_head);
                TextView tv_name = helper.getView(R.id.tv_name);
                tv_name.setText(item.getCustNname());
                TextView tv_title = helper.getView(R.id.activity_title);
                tv_title.setText("#"+item.getTitle()+"#");
                TextView tv_des = helper.getView(R.id.acitvity_des);
                tv_des.setText(item.getContent());
                TextView tv_talk_count = helper.getView(R.id.tv_talk_count);
                tv_talk_count.setText(item.getPartNum() + " 讨论");
                TextView tv_where = helper.getView(R.id.tv_where);
                tv_where.setText("来自私圈" + item.getCoterieName());
                RxView.clicks(civ_head).subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        UserInfoActivity.newFrindInfo(mContext, item.getCustId());
                    }
                });
                RxView.clicks(tv_name).subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        UserInfoActivity.newFrindInfo(mContext, item.getCustId());
                    }
                });
                RxView.clicks(tv_where).subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        WebContainerActivity.startActivity(mContext, BuildConfig.WebHomeBaseUrl + "/" + item.circleRoute + "/coterie/" + item.getCoterieId());
                    }
                });

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
        mEtSearchKeywordBase.setHint("输入话题名称搜索");
        mEtSearchKeywordBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent itnent = new Intent();
                itnent.putExtra(SearchActivity.SEARCH_TYPE, SearchActivity.TYPE_PERSON);
                skipActivity(aty, SearchActivity.class);
            }
        });
        mLvSubject.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HotSubjectModel hotSubjectModel = subjectList.get(position);
                String coterieId = hotSubjectModel.getCoterieId();
                String coterieName = hotSubjectModel.getCoterieName();
                if (StringUtil.isEmpty(coterieId) || StringUtil.isEmpty(coterieName)) {
                    String circleUrl = CommomUtils.getCircleUrl(hotSubjectModel.circleRoute, hotSubjectModel.moduleEnum, hotSubjectModel.resourceId);
                    WebContainerActivity.startActivity(mContext, circleUrl);
                } else {
                    String url = CommomUtils.getDymanicUrl(hotSubjectModel.circleRoute, hotSubjectModel.moduleEnum, hotSubjectModel.getCoterieId(), hotSubjectModel.resourceId);
                    WebContainerActivity.startActivity(mContext, url);
                }
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

    @Override
    protected boolean needLoadingView() {
        return true;
    }

    @Override
    protected boolean hasDataInPage() {
        return mAdapter!=null&&mAdapter.getCount()!=0;
    }
}
