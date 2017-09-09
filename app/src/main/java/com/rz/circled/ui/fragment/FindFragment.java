package com.rz.circled.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rz.circled.R;
import com.rz.circled.db.DatabaseHelper;
import com.rz.circled.presenter.impl.CirclePresenter;
import com.rz.circled.ui.activity.AllCirclesAty;
import com.rz.circled.ui.activity.MoreFamousActivity;
import com.rz.circled.ui.activity.MoreSubjectActivity;
import com.rz.circled.ui.activity.WebContainerActivity;
import com.rz.circled.widget.CommonAdapter;
import com.rz.circled.widget.GlideCircleImage;
import com.rz.circled.widget.MListView;
import com.rz.circled.widget.ViewHolder;
import com.rz.circled.widget.XGridView;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.Constants;
import com.rz.common.ui.fragment.BaseFragment;
import com.rz.common.widget.toasty.Toasty;
import com.rz.httpapi.bean.CircleEntrModle;
import com.rz.httpapi.bean.FamousModel;
import com.rz.httpapi.bean.HotSubjectModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    @BindView(R.id.layout_content)
    FrameLayout mLayoutContent;
    @BindView(R.id.iv_activity)
    ImageView mIvActivity;
    @BindView(R.id.find_refresh)
    SwipeRefreshLayout mFindRefresh;
    private List<CircleEntrModle> circleEntrModleList = new ArrayList();
    private CirclePresenter mPresenter;
    private List<FamousModel> famousList = new ArrayList<>();
    private List<HotSubjectModel> subjectList = new ArrayList<>();
    private List<HotSubjectModel> activityList = new ArrayList<>();
    private RecyclerView.Adapter mFamousAdapter;
    private List<CircleEntrModle> mLoveList = new ArrayList<>();
    private List<CircleEntrModle> allCircle = new ArrayList<>();
    private List<CircleEntrModle> noFollow = new ArrayList<>();
    private List<CircleEntrModle> Follow = new ArrayList<>();
    private RecyclerView.Adapter mSubjectAdapter;

    @Nullable
    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_find, null);
    }

    @Override
    public void initPresenter() {
        DatabaseHelper helper = DatabaseHelper.getHelper(getContext());
//        try {
//            helper.getUserDao().deleteBuilder()
//            List<User> users = helper.getUserDao().queryForAll();
//            int desc = users.get(0).getCount();
//            Log.i(TAG, "initPresenter: "+desc);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        mPresenter = new CirclePresenter();
        mPresenter.attachView(this);
        mPresenter.getCircleEntranceList(0);
        mPresenter.getUserLoveCircle(Session.getUserId());
        mPresenter.getFamousList(7);
        mPresenter.getSubjectList(2);

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
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            if (getString(R.string.FIND_MORE).equals(circleEntrModle.appId)) {
                viewHolder.icv_circle_img.setImageResource(R.drawable.resource_more);
            } else {
//                viewHolder.tv_circle_name.setText(circleEntrModle.circleName);
                Glide.with(FindFragment.this).load(circleEntrModle.circleIcon).into(viewHolder.icv_circle_img);
            }
            return convertView;
        }

        class ViewHolder {
            ImageView icv_circle_img;
            TextView tv_circle_name;
        }
    };

    public void initView() {
        initFamous();
        initSubject();
        initActivity();
        initTitleBar();
        mFindGv.setAdapter(findAdapter);
        mFindGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CircleEntrModle circleEntrModle = circleEntrModleList.get(position);
                if (getString(R.string.FIND_MORE).equals(circleEntrModle.appId)) {
                    Intent intent = new Intent(mActivity, AllCirclesAty.class);
                    getActivity().startActivity(intent);
                } else {
                    WebContainerActivity.startActivity(mActivity, circleEntrModleList.get(position).circleUrl);
                }
            }
        });
    }

    private void initTitleBar() {
        View v = View.inflate(getActivity(), R.layout.find_titlebar_transparent, null);
        mLayoutContent.addView(v);

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
        mSubjectAdapter = new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//                View view = View.inflate(mActivity, R.layout.subject_item, null);
                View view = LayoutInflater.from(mActivity).inflate(R.layout.subject_item, parent, false);
                SubjectVH vh = new SubjectVH(view);
                return vh;
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                HotSubjectModel hotSubjectModel = subjectList.get(position);
                SubjectVH vh = (SubjectVH) holder;
                Glide.with(mActivity).load(hotSubjectModel.getThumbnail()).placeholder(R.drawable.default_subject_bg).into(vh.topicIcon);
                vh.topicName.setText(hotSubjectModel.getTitle());
                vh.topicCount.setText(hotSubjectModel.getReadNum() + "人参与");

            }

            @Override
            public int getItemCount() {
                return subjectList == null ? 0 : subjectList.size();
            }

            class SubjectVH extends RecyclerView.ViewHolder {
                ImageView topicIcon;
                TextView topicName;
                TextView topicCount;

                public SubjectVH(View itemView) {
                    super(itemView);
                    topicIcon = (ImageView) itemView.findViewById(R.id.topic_iv);
                    topicName = (TextView) itemView.findViewById(R.id.topic_name);
                    topicCount = (TextView) itemView.findViewById(R.id.topic_count);
                }
            }
        };
        mSubjectRcv.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));
        mSubjectRcv.setAdapter(mSubjectAdapter);
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
                Glide.with(mActivity).load(famousModel.custInfo.getCustImg()).transform(new GlideCircleImage(mActivity)).into(fvh.famous_iv);
                fvh.famous_name.setText(famousModel.custInfo.getCustNname());
                fvh.famous_mark.setText(famousModel.starInfo.getTradeField());
                fvh.famous_level.setText("Lv" + String.valueOf(famousModel.custInfo.getCustLevel()));
                fvh.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isLogin()) {
//                            String url = famousList.get(position).getUrl();
//                            String starName = famousList.get(position).getStarName();
//                            trackUser("推广", "达人", starName);
//                            WebContainerActivity.startActivity(v.getContext(), url);
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
                TextView famous_level;

                public FamousViewHolder(View itemView) {
                    super(itemView);
                    famous_iv = (ImageView) itemView.findViewById(R.id.famous_iv);
                    famous_name = ((TextView) itemView.findViewById(R.id.famous_name));
                    famous_mark = ((TextView) itemView.findViewById(R.id.famous_mark));
                    famous_level = ((TextView) itemView.findViewById(R.id.famous_level));
                }
            }
        };
        mFamousRcv.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));
        mFamousRcv.setAdapter(mFamousAdapter);
    }

    @Override
    public void initData() {
        mFindRefresh.setColorSchemeColors(Constants.COLOR_SCHEMES);
        mFindRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getUserLoveCircle(Session.getUserId());
                mPresenter.getFamousList(7);
                mPresenter.getSubjectList(2);
                mFindRefresh.setRefreshing(false);
            }
        });

    }

    @Override
    public <T> void updateViewWithFlag(T t, int flag) {
        super.updateViewWithFlag(t, flag);
        if (flag == 0) {
            allCircle.clear();
            allCircle.addAll((List<CircleEntrModle>) t);
            for (int i = 0; i < allCircle.size(); i++) {
                boolean isfind = false;
                for (int j = 0; j < mLoveList.size(); j++) {
                    if (allCircle.get(i).appId.equals(mLoveList.get(j).appId)) {
                        Follow.add(allCircle.get(i));
                        isfind = true;
                        break;
                    }
                }
                if (!isfind) {
                    noFollow.add(allCircle.get(i));
                }
            }
            circleEntrModleList.clear();
            circleEntrModleList.addAll(Follow);
            circleEntrModleList.addAll(noFollow);
            addMore();
            return;
        }
        if (flag == 2) {
            subjectList.clear();
            subjectList.addAll((Collection<? extends HotSubjectModel>) t);
            mSubjectAdapter.notifyDataSetChanged();
        }
        if (flag == 7) {
            famousList.clear();
            famousList.addAll((List<FamousModel>) t);
            mFamousAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public <T> void updateView(T t) {
        mLoveList = (List<CircleEntrModle>) t;
    }

    private void addMore() {
//        if (mLoveList.size()>=7){
//            circleEntrModleList.addAll(mLoveList);
//        }else {
//            circleEntrModleList.addAll(mLoveList);
//            circleEntrModleList.addAll(allCircle);
//        }
        CircleEntrModle c = new CircleEntrModle();
        c.appId = getString(R.string.FIND_MORE);
        if (circleEntrModleList.size() >= 8) {
            circleEntrModleList.add(7, c);
        } else {
            circleEntrModleList.add(c);
        }
        findAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @OnClick({R.id.tv_famous_more, R.id.tv_subject_more, R.id.tv_activity_more, R.id.btn_resource, R.id.btn_quanle})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_famous_more:
                jump(MoreFamousActivity.class);
                break;
            case R.id.tv_subject_more:
                jump(MoreSubjectActivity.class);
                break;
            case R.id.tv_activity_more:
//                jump(MorePlayActivity.class);
                break;
            case R.id.btn_resource:
                Toasty.info(mActivity, "点啊").show();
                break;
            case R.id.btn_quanle:
                Toasty.info(mActivity, "点啊").show();
                break;
        }
    }
}
