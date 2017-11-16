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
import com.rz.circled.BuildConfig;
import com.rz.circled.R;
import com.rz.circled.event.EventConstant;
import com.rz.circled.presenter.impl.CirclePresenter;
import com.rz.circled.ui.activity.AllCirclesActivity;
import com.rz.circled.ui.activity.MoreFamousActivity;
import com.rz.circled.ui.activity.MoreSubjectActivity;
import com.rz.circled.ui.activity.UserInfoActivity;
import com.rz.circled.ui.activity.WebContainerActivity;
import com.rz.circled.widget.CommomUtils;
import com.rz.circled.widget.GlideCenterRoundImage;
import com.rz.circled.widget.GlideCircleImage;
import com.rz.circled.widget.MListView;
import com.rz.circled.widget.XGridView;
import com.rz.common.cache.preference.EntityCache;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.CommonCode;
import com.rz.common.constant.Constants;
import com.rz.common.event.BaseEvent;
import com.rz.common.ui.fragment.BaseFragment;
import com.rz.httpapi.bean.CircleEntrModle;
import com.rz.httpapi.bean.EntitiesBean;
import com.rz.httpapi.bean.FamousModel;
import com.rz.httpapi.bean.HotSubjectModel;
import com.yryz.yunxinim.uikit.common.util.string.StringUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.rz.common.constant.Constants.FIND_LOVE_CACHE;
import static com.rz.common.constant.Constants.LOVE_CIRCLE;
import static com.rz.common.utils.SystemUtils.trackUser;

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
    private List<EntitiesBean> activityList = new ArrayList<>();
    private RecyclerView.Adapter mFamousAdapter;
    private RecyclerView.Adapter mSubjectAdapter;
    private BaseAdapter mActivityAdapter1;
    private int[] resourceColors={R.drawable.resource_blue,R.drawable.resource_red,R.drawable.resource_yellow,R.drawable.resource_green,R.drawable.resource_pink,R.drawable.resource_purple,R.drawable.resource_cyan};

    @Nullable
    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_find, null);
    }

    @Override
    public void initPresenter() {
        mPresenter = new CirclePresenter();
        mPresenter.attachView(this);
        mPresenter.getUserLoveCircle(Session.getUserId());
        mPresenter.getFamousList(7);
        mPresenter.getSubjectList(2);
        mPresenter.getActivityList(3);

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
                viewHolder.tv_circle_name.setText(R.string.find_more);
            } else {
                viewHolder.tv_circle_name.setText(circleEntrModle.circleName);
                viewHolder.icv_circle_img.setImageResource(resourceColors[position]);
//                Glide.with(FindFragment.this).load(circleEntrModle.circleIcon).into(viewHolder.icv_circle_img);
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
                        Intent intent = new Intent(mActivity, AllCirclesActivity.class);
                        intent.putExtra(LOVE_CIRCLE, (Serializable) circleEntrModleList);
                        getActivity().startActivity(intent);
                } else {
                    circleEntrModle.click += 1;
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
        mNewActivityLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EntitiesBean entitiesBean = activityList.get(position);
                if (entitiesBean.getActivityType() == 1) {
                    WebContainerActivity.startActivity(mActivity, BuildConfig.WebHomeBaseUrl + "/activity/platform-activity/signup/" + entitiesBean.getId());
                } else {
                    WebContainerActivity.startActivity(mActivity, BuildConfig.WebHomeBaseUrl + "/activity/platform-activity/vote/" + entitiesBean.getId());

                }
            }
        });
        mActivityAdapter1 = new BaseAdapter() {
            @Override
            public int getCount() {
                return activityList == null ? 0 : activityList.size();
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
                EntitiesBean entitiesBean = activityList.get(position);
                if (convertView == null) {
                    convertView = View.inflate(mActivity, R.layout.activity_item, null);
                }
                TextView title = (TextView) convertView.findViewById(R.id.activity_title);
                ImageView iv = (ImageView) convertView.findViewById(R.id.iv_activity_icon);
                title.setText(entitiesBean.getTitle());
                Glide.with(mActivity).load(entitiesBean.getCoverPlan()).placeholder(R.drawable.ic_circle_img1).transform(new GlideCenterRoundImage(mActivity, 10)).into(iv);
                return convertView;
            }
        };
        mNewActivityLv.setAdapter(mActivityAdapter1);
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
                final HotSubjectModel hotSubjectModel = subjectList.get(position);
                final String coterieId = hotSubjectModel.getCoterieId();
                final String coterieName = hotSubjectModel.getCoterieName();
                SubjectVH vh = (SubjectVH) holder;
                Glide.with(mActivity)
                        .load(hotSubjectModel.getThumbnail())
                        .placeholder(R.drawable.subject_default)
                        .error(R.drawable.subject_default)
                        .into(vh.topicIcon);
                vh.topicName.setText("#"+hotSubjectModel.getTitle()+"#");
                vh.topicCount.setText(hotSubjectModel.getPartNum() + " 讨论");
                vh.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (StringUtil.isEmpty(coterieId) || StringUtil.isEmpty(coterieName)) {
                            String circleUrl = CommomUtils.getCircleUrl(hotSubjectModel.circleRoute, hotSubjectModel.moduleEnum, hotSubjectModel.resourceId);
                            WebContainerActivity.startActivity(mActivity, circleUrl);
                        } else {
                            String url = CommomUtils.getDymanicUrl(hotSubjectModel.circleRoute, hotSubjectModel.moduleEnum, hotSubjectModel.getCoterieId(), hotSubjectModel.resourceId);
                            WebContainerActivity.startActivity(mActivity, url);
                        }
                    }
                });

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
                final FamousModel famousModel = famousList.get(position);
                Glide.with(mActivity).load(famousModel.custInfo.getCustImg()).placeholder(R.drawable.iv_famous_defaule).transform(new GlideCircleImage(mActivity)).into(fvh.famous_iv);
                fvh.famous_name.setText(famousModel.custInfo.getCustNname());
                fvh.famous_mark.setText(famousModel.starInfo.getTradeField());
                fvh.famous_level.setText("Lv" + String.valueOf(famousModel.custInfo.getCustLevel()));
                fvh.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            String starName = famousModel.custInfo.getCustNname();
                            String custId = famousModel.custInfo.getCustId();
                            trackUser("推广", "达人", starName);
                            UserInfoActivity.newFrindInfo(mActivity, custId);

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
        EntityCache<HotSubjectModel> entityCache = new EntityCache<>(mActivity, HotSubjectModel.class);
        EntityCache<FamousModel> FamousModelCache = new EntityCache<>(mActivity, FamousModel.class);
        EntityCache<EntitiesBean> EntitiesBeanCache = new EntityCache<>(mActivity, EntitiesBean.class);
        List<CircleEntrModle> circleEntrModleList = (List<CircleEntrModle>) mACache.getAsObject(FIND_LOVE_CACHE);
        List<HotSubjectModel> subjectList = entityCache.getListEntity(HotSubjectModel.class);
        List<FamousModel> FamousModel = FamousModelCache.getListEntity(FamousModel.class);
        ;
        List<EntitiesBean> EntitiesBean = EntitiesBeanCache.getListEntity(EntitiesBean.class);
        ;
        updateView(circleEntrModleList);
        updateViewWithFlag(subjectList, 2);
        updateViewWithFlag(FamousModel, 7);
        updateViewWithFlag(EntitiesBean, 3);
        mFindRefresh.setColorSchemeColors(Constants.COLOR_SCHEMES);
        mFindRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getUserLoveCircle(Session.getUserId());
                mPresenter.getFamousList(7);
                mPresenter.getSubjectList(2);
                mFindRefresh.setRefreshing(false);
                mPresenter.getActivityList(3);
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BaseEvent event) {
        switch (event.getType()){
            case EventConstant.UPDATE_LOVE_CIRCLE:
                mPresenter.getUserLoveCircle(Session.getUserId());
                break;
            case CommonCode.EventType.TYPE_BACKLOGIN_REFRESH:
                initPresenter();
                break;
            case CommonCode.EventType.TYPE_LOGOUT:
                initPresenter();
                break;
        }

    }


    @Override
    public <T> void updateViewWithFlag(T t, int flag) {
        super.updateViewWithFlag(t, flag);
        if (t != null) {
            if (flag == 2) {
                subjectList.clear();
                subjectList.addAll((Collection<? extends HotSubjectModel>) t);
                mSubjectAdapter.notifyDataSetChanged();
                return;
            }
            if (flag == 7) {
                famousList.clear();
                famousList.addAll((List<FamousModel>) t);
                mFamousAdapter.notifyDataSetChanged();
                return;
            }
            if (flag == 3) {
                activityList.clear();
                activityList = (List<EntitiesBean>) t;
                mActivityAdapter1.notifyDataSetChanged();
                return;
            }
        }
    }

    @Override
    public <T> void updateView(T t) {
        if (t != null) {
            circleEntrModleList.clear();
            circleEntrModleList = (List<CircleEntrModle>) t;
            mACache.put(FIND_LOVE_CACHE, (Serializable) circleEntrModleList);
            CircleEntrModle c = new CircleEntrModle();
            c.appId = getString(R.string.FIND_MORE);
            if (circleEntrModleList.size() >= 8) {
                circleEntrModleList.add(7, c);
            } else {
                circleEntrModleList.add(c);
            }
            findAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)){
        EventBus.getDefault().register(this);}
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @OnClick({R.id.tv_famous_more, R.id.tv_subject_more, R.id.tv_activity_more, R.id.btn_quanle})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_famous_more:
                jump(MoreFamousActivity.class);
                break;
            case R.id.tv_subject_more:
                jump(MoreSubjectActivity.class);
                break;
            case R.id.tv_activity_more:
                WebContainerActivity.startActivity(mActivity, BuildConfig.WebHomeBaseUrl + "/activity/platform-activity");
                break;
            case R.id.btn_quanle:
                WebContainerActivity.startActivity(mActivity, BuildConfig.OpusBaseUrl + "/activity/qql");
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void refreshPage() {

    }
}
