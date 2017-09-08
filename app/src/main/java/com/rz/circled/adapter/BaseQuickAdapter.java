package com.rz.circled.adapter;


import android.animation.Animator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.chad.library.adapter.base.animation.AlphaInAnimation;
import com.chad.library.adapter.base.animation.BaseAnimation;
import com.chad.library.adapter.base.animation.ScaleInAnimation;
import com.chad.library.adapter.base.animation.SlideInBottomAnimation;
import com.chad.library.adapter.base.animation.SlideInLeftAnimation;
import com.chad.library.adapter.base.animation.SlideInRightAnimation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseQuickAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private boolean mNextLoadEnable;
    private boolean mLoadingMoreEnable;
    private boolean mFirstOnlyEnable;
    private boolean mOpenAnimationEnable;
    private boolean mEmptyEnable;
    private boolean mHeadAndEmptyEnable;
    private Interpolator mInterpolator;
    private int mDuration;
    private int mLastPosition;
    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;
    private OnRecyclerViewItemLongClickListener onRecyclerViewItemLongClickListener;
    private RequestLoadMoreListener mRequestLoadMoreListener;
    private BaseAnimation mCustomAnimation;
    private BaseAnimation mSelectAnimation;
    private View mHeaderView;
    private View mFooterView;
    private int pageSize;
    private View mContentView;
    private View mEmptyView;
    protected static final String TAG = BaseQuickAdapter.class.getSimpleName();
    protected Context mContext;
    protected int mLayoutResId;
    protected LayoutInflater mLayoutInflater;
    protected List<T> mData;
    protected static final int HEADER_VIEW = 273;
    protected static final int LOADING_VIEW = 546;
    protected static final int FOOTER_VIEW = 819;
    protected static final int EMPTY_VIEW = 1365;
    private View mLoadingView;
    public static final int ALPHAIN = 1;
    public static final int SCALEIN = 2;
    public static final int SLIDEIN_BOTTOM = 3;
    public static final int SLIDEIN_LEFT = 4;
    public static final int SLIDEIN_RIGHT = 5;
    private OnRecyclerViewItemChildClickListener mChildClickListener;

    /** @deprecated */
    @Deprecated
    public void setOnLoadMoreListener(int pageSize, RequestLoadMoreListener requestLoadMoreListener) {
        this.setOnLoadMoreListener(requestLoadMoreListener);
    }

    public void setOnLoadMoreListener(RequestLoadMoreListener requestLoadMoreListener) {
        this.mRequestLoadMoreListener = requestLoadMoreListener;
    }

    public void setDuration(int duration) {
        this.mDuration = duration;
    }

    public void openLoadMore(int pageSize, boolean enable) {
        this.pageSize = pageSize;
        this.mNextLoadEnable = enable;
    }

    public void openLoadMore(boolean enable) {
        this.mNextLoadEnable = enable;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    public void setOnRecyclerViewItemLongClickListener(OnRecyclerViewItemLongClickListener onRecyclerViewItemLongClickListener) {
        this.onRecyclerViewItemLongClickListener = onRecyclerViewItemLongClickListener;
    }

    public void setOnRecyclerViewItemChildClickListener(OnRecyclerViewItemChildClickListener childClickListener) {
        this.mChildClickListener = childClickListener;
    }

    public BaseQuickAdapter(Context context, int layoutResId, List<T> data) {
        this.mNextLoadEnable = false;
        this.mLoadingMoreEnable = false;
        this.mFirstOnlyEnable = true;
        this.mOpenAnimationEnable = false;
        this.mInterpolator = new LinearInterpolator();
        this.mDuration = 300;
        this.mLastPosition = -1;
        this.mSelectAnimation = new AlphaInAnimation();
        this.pageSize = -1;
        this.mData = data == null?new ArrayList():new ArrayList(data);
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        if(layoutResId != 0) {
            this.mLayoutResId = layoutResId;
        }

    }

    public BaseQuickAdapter(Context context, List<T> data) {
        this(context, 0, data);
    }

    public BaseQuickAdapter(Context context, View contentView, List<T> data) {
        this(context, 0, data);
        this.mContentView = contentView;
    }

    public BaseQuickAdapter(Context context) {
        this(context, (List)null);
    }

    public void remove(int position) {
        this.mData.remove(position);
        this.notifyItemRemoved(position);
    }

    public void add(int position, T item) {
        this.mData.add(position, item);
        this.notifyItemInserted(position);
    }

    public void setNewData(List<T> data) {
        this.mData = data;
        if(this.mRequestLoadMoreListener != null) {
            this.mNextLoadEnable = true;
            this.mFooterView = null;
        }

        this.notifyDataSetChanged();
    }

    public void addData(List<T> data) {
        this.mData.addAll(data);
        this.notifyDataSetChanged();
    }

    public void setLoadingView(View loadingView) {
        this.mLoadingView = loadingView;
    }

    public List getData() {
        return this.mData;
    }

    public T getItem(int position) {
        return this.mData.get(position);
    }

    public int getHeaderViewsCount() {
        return this.mHeaderView == null?0:1;
    }

    public int getFooterViewsCount() {
        return this.mFooterView == null?0:1;
    }

    public int getmEmptyViewCount() {
        return this.mEmptyView == null?0:1;
    }

    public int getItemCount() {
        int i = this.isLoadMore()?1:0;
        int count = this.mData.size() + i + this.getHeaderViewsCount() + this.getFooterViewsCount();
        this.mEmptyEnable = false;
        if(this.mHeadAndEmptyEnable && this.getHeaderViewsCount() == 1 && count == 1 || count == 0) {
            this.mEmptyEnable = true;
            count += this.getmEmptyViewCount();
        }

        return count;
    }

    public int getItemViewType(int position) {
        return this.mHeaderView != null && position == 0?273:(this.mEmptyView != null && this.getItemCount() == (this.mHeadAndEmptyEnable?2:1) && this.mEmptyEnable?1365:(position == this.mData.size() + this.getHeaderViewsCount()?(this.mNextLoadEnable?546:819):this.getDefItemViewType(position - this.getHeaderViewsCount())));
    }

    protected int getDefItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder baseViewHolder = null;
        switch(viewType) {
            case 273:
                baseViewHolder = new BaseViewHolder(this.mContext, this.mHeaderView);
                break;
            case 546:
                baseViewHolder = this.getLoadingView(parent);
                this.initItemClickListener(baseViewHolder);
                break;
            case 819:
                baseViewHolder = new BaseViewHolder(this.mContext, this.mFooterView);
                break;
            case 1365:
                baseViewHolder = new BaseViewHolder(this.mContext, this.mEmptyView);
                break;
            default:
                baseViewHolder = this.onCreateDefViewHolder(parent, viewType);
                this.initItemClickListener(baseViewHolder);
        }

        return baseViewHolder;
    }

    private BaseViewHolder getLoadingView(ViewGroup parent) {
        return this.mLoadingView == null?this.createBaseViewHolder(parent, com.chad.library.R.layout.def_loading):new BaseViewHolder(this.mContext, this.mLoadingView);
    }

    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        int type = holder.getItemViewType();
        if(type == 1365 || type == 273 || type == 819 || type == 546) {
            this.setFullSpan(holder);
        }

    }

    protected void setFullSpan(RecyclerView.ViewHolder holder) {
        if(holder.itemView.getLayoutParams() instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams)holder.itemView.getLayoutParams();
            params.setFullSpan(true);
        }

    }

    public void onBindViewHolder(RecyclerView.ViewHolder holder, int positions) {
        switch(holder.getItemViewType()) {
            case 0:
                this.convert((BaseViewHolder)holder, this.mData.get(holder.getLayoutPosition() - this.getHeaderViewsCount()));
                this.addAnimation(holder);
            case 273:
            case 819:
            case 1365:
                break;
            case 546:
                this.addLoadMore(holder);
                break;
            default:
                this.convert((BaseViewHolder)holder, this.mData.get(holder.getLayoutPosition() - this.getHeaderViewsCount()));
                this.onBindDefViewHolder((BaseViewHolder)holder, this.mData.get(holder.getLayoutPosition() - this.getHeaderViewsCount()));
        }

    }

    protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        return this.createBaseViewHolder(parent, this.mLayoutResId);
    }

    protected BaseViewHolder createBaseViewHolder(ViewGroup parent, int layoutResId) {
        return this.mContentView == null?new BaseViewHolder(this.mContext, this.getItemView(layoutResId, parent)):new BaseViewHolder(this.mContext, this.mContentView);
    }

    public void addHeaderView(View header) {
        this.mHeaderView = header;
        this.notifyDataSetChanged();
    }

    public void addFooterView(View footer) {
        this.mNextLoadEnable = false;
        this.mFooterView = footer;
        this.notifyDataSetChanged();
    }

    public void setEmptyView(View emptyView) {
        this.setEmptyView(false, emptyView);
    }

    public void setEmptyView(boolean isHeadAndEmpty, View emptyView) {
        this.mHeadAndEmptyEnable = isHeadAndEmpty;
        this.mEmptyView = emptyView;
    }

    public View getEmptyView() {
        return this.mEmptyView;
    }

    /** @deprecated */
    @Deprecated
    public void isNextLoad(boolean isNextLoad) {
        this.mNextLoadEnable = isNextLoad;
        this.mLoadingMoreEnable = false;
        this.notifyDataSetChanged();
    }

    public void notifyDataChangedAfterLoadMore(boolean isNextLoad) {
        this.mNextLoadEnable = isNextLoad;
        this.mLoadingMoreEnable = false;
        this.notifyDataSetChanged();
    }

    public void notifyDataChangedAfterLoadMore(List<T> data, boolean isNextLoad) {
        this.mData.addAll(data);
        this.notifyDataChangedAfterLoadMore(isNextLoad);
    }

    private void addLoadMore(RecyclerView.ViewHolder holder) {
        if(this.isLoadMore()) {
            this.mLoadingMoreEnable = true;
            this.mRequestLoadMoreListener.onLoadMoreRequested();
        }

    }

    private void initItemClickListener(final BaseViewHolder baseViewHolder) {
        if(this.onRecyclerViewItemClickListener != null) {
            baseViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    BaseQuickAdapter.this.onRecyclerViewItemClickListener.onItemClick(v, baseViewHolder.getLayoutPosition() - BaseQuickAdapter.this.getHeaderViewsCount());
                }
            });
        }

        if(this.onRecyclerViewItemLongClickListener != null) {
            baseViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                public boolean onLongClick(View v) {
                    return BaseQuickAdapter.this.onRecyclerViewItemLongClickListener.onItemLongClick(v, baseViewHolder.getLayoutPosition() - BaseQuickAdapter.this.getHeaderViewsCount());
                }
            });
        }

    }

    private void addAnimation(RecyclerView.ViewHolder holder) {
        if(this.mOpenAnimationEnable && (!this.mFirstOnlyEnable || holder.getLayoutPosition() > this.mLastPosition)) {
            BaseAnimation animation = null;
            if(this.mCustomAnimation != null) {
                animation = this.mCustomAnimation;
            } else {
                animation = this.mSelectAnimation;
            }

            Animator[] arr$ = animation.getAnimators(holder.itemView);
            int len$ = arr$.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                Animator anim = arr$[i$];
                this.startAnim(anim, holder.getLayoutPosition());
            }

            this.mLastPosition = holder.getLayoutPosition();
        }

    }

    protected void startAnim(Animator anim, int index) {
        anim.setDuration((long)this.mDuration).start();
        anim.setInterpolator(this.mInterpolator);
    }

    private boolean isLoadMore() {
        return this.mNextLoadEnable && this.pageSize != -1 && !this.mLoadingMoreEnable && this.mRequestLoadMoreListener != null && this.mData.size() >= this.pageSize;
    }

    protected View getItemView(int layoutResId, ViewGroup parent) {
        return this.mLayoutInflater.inflate(layoutResId, parent, false);
    }

    /** @deprecated */
    @Deprecated
    protected void onBindDefViewHolder(BaseViewHolder holder, T item) {
    }

    public void openLoadAnimation(int animationType) {
        this.mOpenAnimationEnable = true;
        this.mCustomAnimation = null;
        switch(animationType) {
            case 1:
                this.mSelectAnimation = new AlphaInAnimation();
                break;
            case 2:
                this.mSelectAnimation = new ScaleInAnimation();
                break;
            case 3:
                this.mSelectAnimation = new SlideInBottomAnimation();
                break;
            case 4:
                this.mSelectAnimation = new SlideInLeftAnimation();
                break;
            case 5:
                this.mSelectAnimation = new SlideInRightAnimation();
        }

    }

    public void openLoadAnimation(BaseAnimation animation) {
        this.mOpenAnimationEnable = true;
        this.mCustomAnimation = animation;
    }

    public void openLoadAnimation() {
        this.mOpenAnimationEnable = true;
    }

    public void isFirstOnly(boolean firstOnly) {
        this.mFirstOnlyEnable = firstOnly;
    }

    protected abstract void convert(BaseViewHolder var1, T var2);

    public long getItemId(int position) {
        return (long)position;
    }

    public interface RequestLoadMoreListener {
        void onLoadMoreRequested();
    }

    public class OnItemChildClickListener implements View.OnClickListener {
        public int position;

        public OnItemChildClickListener() {
        }

        public void onClick(View v) {
            if(BaseQuickAdapter.this.mChildClickListener != null) {
                BaseQuickAdapter.this.mChildClickListener.onItemChildClick(BaseQuickAdapter.this, v, this.position - BaseQuickAdapter.this.getHeaderViewsCount());
            }

        }
    }

    public interface OnRecyclerViewItemChildClickListener {
        void onItemChildClick(BaseQuickAdapter var1, View var2, int var3);
    }

    public interface OnRecyclerViewItemLongClickListener {
        boolean onItemLongClick(View var1, int var2);
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View var1, int var2);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface AnimationType {
    }
}

