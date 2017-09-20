package com.rz.circled.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.rz.circled.R;
import com.rz.circled.presenter.IPresenter;
import com.rz.circled.presenter.impl.FriendPresenter1;
import com.rz.circled.widget.GlideCircleImage;
import com.rz.common.adapter.CommonAdapter;
import com.rz.common.adapter.ViewHolder;
import com.rz.common.cache.preference.EntityCache;
import com.rz.common.constant.CommonCode;
import com.rz.common.constant.Type;
import com.rz.common.event.BaseEvent;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.utils.Protect;
import com.rz.httpapi.bean.FriendInformationBean;
import com.rz.httpapi.bean.FriendRequireModel;
import com.yryz.yunxinim.uikit.common.ui.dialog.CustomAlertDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;


/**
 * 作者：Administrator on 2016/7/8 0008 17:50
 * 功能：关注我，网络获取，分页加载
 * 说明：
 */
public class FollowMeActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener,
        SwipyRefreshLayout.OnRefreshListener {

    @BindView(R.id.id_search_key)
    TextView mSearchTxt;
    /**
     * 内容展示
     */
    @BindView(R.id.id_content_listv)
    ListView mListview;
    @BindView(R.id.id_swipyRefreshLayout)
    SwipyRefreshLayout mSwipyRefreshLayout;

    /**
     * 存储所有我关注的朋友信息
     */
    private List<FriendRequireModel> mFansAllFriends = new ArrayList<>();
    protected IPresenter presenter;
    protected CommonAdapter mAdapter;

    @Nullable
    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.layout_follow_me, null);
    }

    @Override
    public <T> void updateView(T t) {
        super.updateView(t);
        if (t instanceof Integer) {

            Integer rid = (Integer) t;
            for (Iterator iter = mFansAllFriends.iterator(); iter.hasNext(); ) {
                FriendRequireModel item = (FriendRequireModel) iter.next();
                if (item.getRid() == rid) {
                    iter.remove();
                    break;
                }
            }
            mAdapter.notifyDataSetChanged();

        }
    }


    @Override
    public <T> void updateViewWithFlag(T t, int flag) {
        super.updateViewWithFlag(t, flag);

        if (null == t)
            return;

        mFansAllFriends.clear();
        mFansAllFriends.addAll((List<FriendRequireModel>) t);
        mAdapter.notifyDataSetChanged();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventUpdate(BaseEvent event) {

        if (FriendPresenter1.FRIEND_REMARK_EVENT.equals(event.key)) {
            FriendRequireModel data = (FriendRequireModel) event.getData();
            for (FriendRequireModel info : mFansAllFriends) {
                if (TextUtils.equals(info.getUser().getCustId(), data.getUser().getCustId())) {
                    info.getUser().setCustSignature(data.getUser().getCustId());
                }
            }
            mAdapter.notifyDataSetChanged();

        } else if (FriendPresenter1.FRIEND_APPLY_EVENT.equals(event.key)) {
            FriendRequireModel data = (FriendRequireModel) event.getData();
            for (FriendRequireModel info : mFansAllFriends) {
                if (TextUtils.equals(info.getUser().getCustId(), data.getUser().getCustId())) {
                    info.setStatus(CommonCode.requireFriendStatus.require_status_agree);
                }
            }
            mAdapter.notifyDataSetChanged();

        }

    }

    @Override
    public void initPresenter() {
        presenter = new FriendPresenter1();
        presenter.attachView(this);
    }

    @Override
    public void initView() {
        setTitleText(getString(R.string.follow_me));
        mSwipyRefreshLayout.setEnabled(false);
        mSearchTxt.setText(R.string.search_current_user);
        findViewById(R.id.id_search_key_rela).setVisibility(View.GONE);
        mAdapter = new CommonAdapter<FriendRequireModel>(this, mFansAllFriends, R.layout.item_follow_me) {

            @Override
            public void convert(ViewHolder helper, final FriendRequireModel data, int position) {
                if (null != data) {
                    final FriendInformationBean item = data.getUser();
                    if (Protect.checkLoadImageStatus(mContext)) {
                        Glide.with(mContext).load(item.getCustImg())
                                .transform(new GlideCircleImage(aty))
                                .placeholder(R.drawable.ic_default_head).into((ImageView) helper.getViewById(R.id.id_friends_img));
                    }
                    if (!TextUtils.isEmpty(item.getCustNname())) {
                        ((TextView) helper.getViewById(R.id.id_friends_name)).setText(item.getCustNname());
                    } else {
                        ((TextView) helper.getViewById(R.id.id_friends_name)).setText(item.getCustPhone());
                    }

                    if (!TextUtils.isEmpty(item.getCustSignature())) {
                        ((TextView) helper.getViewById(R.id.id_friends_brief)).setText(item.getCustSignature());
                    } else {
                        ((TextView) helper.getViewById(R.id.id_friends_brief)).setText("");
                    }

                    /**
                     * 申请好友列表状态
                     */

                    TextView stateText = (TextView) helper.getViewById(R.id.id_friends_state);

                    if (data.getStatus() == CommonCode.requireFriendStatus.require_status_agree) {

                        stateText.setText(R.string.already_friend);
                        stateText.setTextColor(getResources().getColor(R.color.color_666666));
                        stateText.setBackgroundResource(android.R.color.transparent);

                    } else if (data.getStatus() == CommonCode.requireFriendStatus.require_status_wait) {


                        if (data.getIsRequire() == CommonCode.requireFriendStatus.is_require_false) {

                            stateText.setText(R.string.agree);
                            stateText.setTextColor(getResources().getColor(R.color.white));
                            stateText.setBackgroundResource(R.drawable.selector_btn_blue);

                            helper.getViewById(R.id.frame_state).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ((FriendPresenter1) presenter).requireFriend(item.getCustId(), data.getRid(), CommonCode.requireFriend.require_type_agree);
                                }
                            });

                        } else if (data.getIsRequire() == CommonCode.requireFriendStatus.is_require_true) {

                            stateText.setText(R.string.waiting_agree);
                            stateText.setTextColor(getResources().getColor(R.color.color_666666));
                            stateText.setBackgroundResource(android.R.color.transparent);

                        }

                    } else {

                        stateText.setText(R.string.has_been_rejected);
                        stateText.setTextColor(getResources().getColor(R.color.color_666666));
                        stateText.setBackgroundResource(android.R.color.transparent);

                    }
                }
            }
        };

        mListview.setAdapter(mAdapter);
        mListview.setOnItemClickListener(this);
        mListview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                CustomAlertDialog alertDialog = new CustomAlertDialog(aty);
                alertDialog.setCancelable(true);
                alertDialog.setCanceledOnTouchOutside(true);
                alertDialog.addItem(getString(R.string.delete), new CustomAlertDialog.onSeparateItemClickListener() {
                    @Override
                    public void onClick() {
                        FriendRequireModel item = (FriendRequireModel) mAdapter.getItem(position);
                        ((FriendPresenter1) presenter).deteleRequireFriend(item.getRid());
                    }
                });
                alertDialog.show();
                return true;
            }
        });

        mSwipyRefreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTH);
        mSwipyRefreshLayout.setOnRefreshListener(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initData() {

        EventBus.getDefault().register(this);

        EntityCache<FriendRequireModel> friendRequireModelEntityCache = new EntityCache<>(aty, FriendRequireModel.class);
        List<FriendRequireModel> caches = friendRequireModelEntityCache.getListEntity(FriendRequireModel.class);

        mFansAllFriends.clear();
        if (caches != null)
            mFansAllFriends.addAll(caches);
        mAdapter.notifyDataSetChanged();

        ((FriendPresenter1) presenter).requestRequireList(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //关键字搜索
            case R.id.id_search_key_rela:
                Bundle bundle = new Bundle();
                bundle.putInt("type", Type.custType_2);
                showActivity(aty, ContactsSearchAty.class, bundle);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        /**
         * 进入好友详情
         */
        FriendRequireModel item = (FriendRequireModel) mAdapter.getItem(i);
        if (item != null) {
//            FriendInfoAty.newFrindInfo(aty, item.getCustId());
        }
    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {

        ((FriendPresenter1) presenter).requestRequireList((direction != SwipyRefreshLayoutDirection.TOP));

    }
}