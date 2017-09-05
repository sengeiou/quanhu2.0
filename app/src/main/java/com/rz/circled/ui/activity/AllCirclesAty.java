package com.rz.circled.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rz.circled.R;
import com.rz.circled.application.QHApplication;
import com.rz.circled.presenter.impl.CirclePresenter;
import com.rz.circled.widget.GlideCircleImage;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.utils.Protect;
import com.rz.httpapi.bean.CircleEntrModle;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.rz.circled.widget.CommomUtils.trackUser;


/**
 * Created by Administrator on 2017/3/30/030.
 */

public class AllCirclesAty extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.title_content)
    FrameLayout mTitleContent;
    @BindView(R.id.rv_allcircles_published)
    RecyclerView mRvAllcirclesPublished;
    @BindView(R.id.rv_allcircles_comingpublish)
    RecyclerView mRvAllcirclesComingpublish;

    List<CircleEntrModle> onLines = new ArrayList<>();
    List<CircleEntrModle> followOnLines = new ArrayList<>();
    List<CircleEntrModle> noFollow = new ArrayList<>();
    List<CircleEntrModle> waitUp = new ArrayList<>();
    PublishedAdapter publishedAdapter;
    ComingPublishedAdapter comingPublishedAdapter;
    ImageView mIvBaseTitleLeft;
    TextView mTvBaseTitleRight;
    private HashSet<String> mFollowCircle;

    @Nullable
    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.aty_allcircles, null);
    }

    @Override
    public void initView() {
        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(QHApplication.getContext(), 4);
        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(QHApplication.getContext(), 4);
        mRvAllcirclesPublished.setLayoutManager(gridLayoutManager1);
        mRvAllcirclesComingpublish.setLayoutManager(gridLayoutManager2);
        publishedAdapter = new PublishedAdapter(QHApplication.getContext());
        publishedAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int pos, int status) {
                if (isLogin()) {
                    trackUser("百圈纷呈", "圈子入口", onLines.get(pos).circleName);
                    CircleEntrModle circleEntrModle = onLines.get(pos);
                    WebContainerActivity.startActivity(AllCirclesAty.this, circleEntrModle.circleUrl);
                }
            }
        });
        comingPublishedAdapter = new ComingPublishedAdapter(QHApplication.getContext());
//        comingPublishedAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
//            @Override
//            public void onItemClick(View view, int pos, int status) {
//                if(isLogin()) {
//                    CircleEntrModle circleEntrModle = waitUp.get(pos);
//                    WebContainerAty.startAty(AllCirclesAty.this, circleEntrModle.circleUrl);
//                }
//            }
//        });
        mRvAllcirclesPublished.setAdapter(publishedAdapter);//马上发布
        mRvAllcirclesPublished.setHasFixedSize(true);
        mRvAllcirclesPublished.setNestedScrollingEnabled(false);
        mRvAllcirclesComingpublish.setAdapter(comingPublishedAdapter);//即将发布
        mRvAllcirclesComingpublish.setHasFixedSize(true);
        mRvAllcirclesComingpublish.setNestedScrollingEnabled(false);
//        setTitleRightBackground(R.drawable.all_circle_shape);
//        setTitleRightText("编辑");
//        setTitleRightTextColor(R.color.white);
//        setTitleRightListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
    }

    @Override
    protected boolean needShowTitle() {
        return false;
    }

    @Override
    public void initPresenter() {
        super.initPresenter();
        CirclePresenter presenter = new CirclePresenter();
        presenter.attachView(this);
        presenter.getCircleEntranceList(0);
        presenter.getCircleEntranceList(1);
    }

    @Override
    public void initData() {
        initTitleBar();
    }

    private void initTitleBar() {
        View v = View.inflate(this, R.layout.all_circle_head, null);
        mTitleContent.addView(v);
        mIvBaseTitleLeft = (ImageView) v.findViewById(R.id.all_circle__title_left);
        mTvBaseTitleRight = (TextView) v.findViewById(R.id.all_circle_title_right);
        mIvBaseTitleLeft.setOnClickListener(this);
        mTvBaseTitleRight.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    @Override
    public <T> void updateViewWithFlag(T t, int flag) {
        super.updateViewWithFlag(t, flag);
        if (t != null) {
            List<CircleEntrModle> circleEntrModleList = (List<CircleEntrModle>) t;
            if (flag == 0) {
                //上线
                onLines = circleEntrModleList;
                publishedAdapter.notifyDataSetChanged();
                return;
            }
            if (flag == 1) {
                //等待上线
                waitUp = circleEntrModleList;
                comingPublishedAdapter.notifyDataSetChanged();
                return;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.all_circle__title_left:
                finish();
                break;

            case R.id.all_circle_title_right:
                break;


        }
    }


    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int pos, int status);
    }

    //==================================已发布adapter=====================================
    class PublishedAdapter extends RecyclerView.Adapter<PublishedViewHolder> implements View.OnClickListener {
        private LayoutInflater mInflater;
        private OnRecyclerViewItemClickListener mOnItemClickListener = null;

        public PublishedAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                //注意这里使用getTag方法获取数据
                mOnItemClickListener.onItemClick(v, (Integer) v.getTag(), 0);//从这获取到点击的是哪个条目
            }
        }

        public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
            this.mOnItemClickListener = listener;
        }

        @Override
        public PublishedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.item_allcircles_circle, parent, false);
            PublishedViewHolder viewHolder = new PublishedViewHolder(view);
            view.setOnClickListener(this);
            viewHolder.civ_circleImg = (ImageView) view.findViewById(R.id.civ_circleimg);
            viewHolder.circle_follow = (ImageView) view.findViewById(R.id.circle_follow);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(PublishedViewHolder holder, int position) {
            CircleEntrModle circleEntrModle = onLines.get(position);
            if (mFollowCircle != null) {
                boolean isfind = false;
                for (String s : mFollowCircle) {
                    if (onLines.get(position).appId.equals(s)) {
                        isfind = true;
                        holder.circle_follow.setVisibility(View.VISIBLE);
                        break;
                    }

                }
                if (!isfind) {
                    holder.circle_follow.setVisibility(View.GONE);
                }
            }
            if (Protect.checkLoadImageStatus(AllCirclesAty.this)) {
                Glide.with(AllCirclesAty.this).load(circleEntrModle.circleIcon).into(holder.civ_circleImg);
            }
            holder.itemView.setTag(position);//holder和position对应,pos塞进去
        }

        @Override
        public int getItemCount() {
            return onLines.size();
        }
    }

    private static class PublishedViewHolder extends RecyclerView.ViewHolder {
        ImageView civ_circleImg;
        ImageView circle_follow;

        public PublishedViewHolder(View itemView) {
            super(itemView);
        }
    }

    //==================================即将发布adapter=====================================
    class ComingPublishedAdapter extends RecyclerView.Adapter<ComingPublishedViewHolder> implements View.OnClickListener {
        private LayoutInflater mInflater;
        private OnRecyclerViewItemClickListener mOnItemClickListener = null;

        public ComingPublishedAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public ComingPublishedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.item_allcircles_circle, parent, false);
            ComingPublishedViewHolder viewHolder = new ComingPublishedViewHolder(view);
            view.setOnClickListener(this);
            viewHolder.civ_circleImg = (ImageView) view.findViewById(R.id.civ_circleimg);
            return viewHolder;
        }

        public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
            this.mOnItemClickListener = listener;
        }

        @Override
        public void onBindViewHolder(ComingPublishedViewHolder holder, int position) {
            CircleEntrModle circleEntrModle = waitUp.get(position);
            if (Protect.checkLoadImageStatus(AllCirclesAty.this)) {
                Glide.with(AllCirclesAty.this).load(circleEntrModle.circleIcon).transform(new GlideCircleImage(AllCirclesAty.this)).
                        placeholder(R.drawable.ic_default_head).error(R.drawable.ic_default_head).into(holder.civ_circleImg);
            }
            holder.itemView.setTag(position);//holder和position对应,pos塞进去
        }

        @Override
        public int getItemCount() {
            return waitUp.size();
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                //注意这里使用getTag方法获取数据
                mOnItemClickListener.onItemClick(v, (Integer) v.getTag(), 1);//从这获取到点击的是哪个条目
            }
        }
    }

    private static class ComingPublishedViewHolder extends RecyclerView.ViewHolder {
        ImageView civ_circleImg;

        public ComingPublishedViewHolder(View itemView) {
            super(itemView);
        }
    }
}