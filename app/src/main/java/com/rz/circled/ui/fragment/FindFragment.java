package com.rz.circled.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rz.circled.R;
import com.rz.circled.presenter.impl.CirclePresenter;
import com.rz.circled.ui.activity.AllCirclesAty;
import com.rz.circled.ui.activity.MoreFamousActivity;
import com.rz.circled.ui.activity.MorePlayActivity;
import com.rz.circled.ui.activity.MoreSubjectActivity;
import com.rz.circled.ui.activity.WebContainerActivity;
import com.rz.circled.widget.CommonAdapter;
import com.rz.circled.widget.MListView;
import com.rz.circled.widget.ViewHolder;
import com.rz.circled.widget.XGridView;
import com.rz.common.ui.fragment.BaseFragment;
import com.rz.common.widget.toasty.Toasty;
import com.rz.httpapi.bean.CircleEntrModle;
import com.rz.httpapi.bean.FamousModel;
import com.rz.httpapi.bean.HotSubjectModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.rz.circled.widget.CommomUtils.trackUser;

/**
 * Created by Gsm on 2017/8/29.
 */
public class FindFragment extends BaseFragment {

    @BindView(R.id.find_gv)
    XGridView mFindGv;
    @BindView(R.id.circle_famous_rcv)
    RecyclerView mFamousRcv;
    @BindView(R.id.circle_subject_rcv)
    RecyclerView mSubjectRcv;
    @BindView(R.id.new_activity_lv)
    MListView mNewActivityLv;
    @BindView(R.id.tv_famous_more)
    TextView mTvFamousMore;
    @BindView(R.id.tv_subject_more)
    TextView mTvSubjectMore;
    @BindView(R.id.tv_activity_more)
    TextView mTvActivityMore;
    private List<CircleEntrModle> circleEntrModleList = new ArrayList();
    private CirclePresenter mPresenter;
    private List<FamousModel> famousList = new ArrayList<>();
    private List<HotSubjectModel> subjectList = new ArrayList<>();
    private List<HotSubjectModel> activityList = new ArrayList<>();
    private RecyclerView.Adapter mFamousAdapter;

    @Nullable
    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_find, null);
    }

    @Override
    public void initPresenter() {
        mPresenter = new CirclePresenter();
        mPresenter.attachView(this);
        mPresenter.getCircleEntranceList(0);
        mPresenter.getFamousList(7);
    }

    BaseAdapter findAdapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return circleEntrModleList.size() > 8 ? 8 : circleEntrModleList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CircleEntrModle circleEntrModle = circleEntrModleList.get(position);
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = View.inflate(mActivity, R.layout.item_recyclev_homev3, null);
                viewHolder.icv_circle_img = (ImageView) convertView.findViewById(R.id.id_circle_civ);
                viewHolder.tv_circle_name = (TextView) convertView.findViewById(R.id.id_circle_name);
                viewHolder.tv_circle_peoplenums = (TextView) convertView.findViewById(R.id.id_circle_peoplenums);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tv_circle_name.setText(circleEntrModle.circleName);
            Glide.with(FindFragment.this).load(circleEntrModle.circleIcon).into(viewHolder.icv_circle_img);
            return convertView;
        }

        class ViewHolder {
            ImageView icv_circle_img;
            TextView tv_circle_name;
            TextView tv_circle_peoplenums;
        }
    };

    public void initView() {
        initFamous();
        initSubject();
        initActivity();
        mFindGv.setAdapter(findAdapter);
        mFindGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CircleEntrModle circleEntrModle = circleEntrModleList.get(position);
                if (circleEntrModle.circleName.equals("百圈纷呈")) {
                    Intent intent = new Intent(mActivity, AllCirclesAty.class);
                    getActivity().startActivity(intent);
                } else {
                    WebContainerActivity.startActivity(mActivity, circleEntrModleList.get(position).circleUrl);
                }
            }
        });
    }

    private void initActivity() {
        activityList.add(new HotSubjectModel());
        activityList.add(new HotSubjectModel());
        activityList.add(new HotSubjectModel());
        mNewActivityLv.setAdapter(new CommonAdapter<HotSubjectModel>(mActivity, activityList, R.layout.activity_item) {
            @Override
            public void convert(ViewHolder helper, HotSubjectModel item) {

            }
        });
    }

    private void initSubject() {
        RecyclerView.Adapter subjectAdapter = new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//                View view = View.inflate(mActivity, R.layout.subject_item, null);
                View view = LayoutInflater.from(mActivity).inflate(R.layout.subject_item, parent, false);
                SubjectVH vh = new SubjectVH(view);
                return vh;
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                SubjectVH vh = (SubjectVH) holder;

            }

            @Override
            public int getItemCount() {
//                return subjectList == null ? 0 : subjectList.size();
                return 10;
            }

            class SubjectVH extends RecyclerView.ViewHolder {
                ImageView topicIcon;
                TextView topicName;
                TextView topicCount;

                public SubjectVH(View itemView) {
                    super(itemView);
                }
            }
        };
        mSubjectRcv.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));
        mSubjectRcv.setAdapter(subjectAdapter);
    }

    private void initFamous() {
        //                            WebContainerAty.startAty(v.getContext(), url);
        mFamousAdapter = new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View inflate = View.inflate(mActivity, R.layout.famous_item, null);
                FamousViewHolder fvh = new FamousViewHolder(inflate);
                return fvh;
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
                FamousViewHolder fvh = (FamousViewHolder) holder;
                FamousModel famousModel = famousList.get(position);
                Glide.with(mActivity).load(famousModel.getStarImg()).into(fvh.famous_iv);
                fvh.famous_name.setText(famousModel.getStarName());
                fvh.famous_mark.setText(famousModel.getStarTag());
                fvh.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isLogin()) {
                            String url = famousList.get(position).getUrl();
                            String starName = famousList.get(position).getStarName();
                            trackUser("推广", "达人", starName);
//                            WebContainerAty.startAty(v.getContext(), url);
                        }
                    }
                });
            }

            @Override
            public int getItemCount() {
                return famousList == null ? 0 : famousList.size();
            }

            class FamousViewHolder extends RecyclerView.ViewHolder {
                ImageView famous_iv;
                TextView famous_name;
                TextView famous_mark;

                public FamousViewHolder(View itemView) {
                    super(itemView);
                    famous_iv = (ImageView) itemView.findViewById(R.id.famous_iv);
                    famous_name = ((TextView) itemView.findViewById(R.id.famous_name));
                    famous_mark = ((TextView) itemView.findViewById(R.id.famous_mark));
                }
            }
        };
        mFamousRcv.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));
        mFamousRcv.setAdapter(mFamousAdapter);
    }

    @Override
    public void initData() {

    }

    @Override
    public <T> void updateViewWithFlag(T t, int flag) {
        super.updateViewWithFlag(t, flag);
        if (flag == 0) {
            circleEntrModleList.addAll((List<CircleEntrModle>) t);
            CircleEntrModle c = new CircleEntrModle();
            c.circleName = getString(R.string.baiquanfencheng);
            if (circleEntrModleList.size() >= 8) {
                circleEntrModleList.add(7, c);
            } else {
                circleEntrModleList.add(c);
            }
            findAdapter.notifyDataSetChanged();
            return;
        }
        if (flag == 7) {
            famousList.addAll((List<FamousModel>) t);
            mFamousAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @OnClick({R.id.tv_famous_more, R.id.tv_subject_more, R.id.tv_activity_more,R.id.btn_resource, R.id.btn_quanle})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_famous_more:
                jump(MoreFamousActivity.class);
                break;
            case R.id.tv_subject_more:
                jump(MoreSubjectActivity.class);
                break;
            case R.id.tv_activity_more:
                jump(MorePlayActivity.class);
                break;
            case R.id.btn_resource:
                Toasty.info(mActivity,"点啊").show();
                break;
            case R.id.btn_quanle:
                Toasty.info(mActivity,"点啊").show();
                break;
        }
    }
}
