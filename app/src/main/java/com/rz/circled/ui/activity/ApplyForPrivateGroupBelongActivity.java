package com.rz.circled.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.rz.circled.R;
import com.rz.circled.adapter.PrivateGroupBelongAdapter;
import com.rz.circled.event.EventConstant;
import com.rz.circled.presenter.impl.PrivateGroupPresenter;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.CommonCode;
import com.rz.common.constant.IntentKey;
import com.rz.common.event.BaseEvent;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.utils.Utility;
import com.rz.common.widget.svp.SVProgressHUD;
import com.rz.httpapi.api.ApiPGService;
import com.rz.httpapi.api.BaseCallback;
import com.rz.httpapi.api.Http;
import com.rz.httpapi.api.ResponseData.ResponseData;
import com.rz.httpapi.bean.CircleBelongBean;
import com.rz.httpapi.bean.CircleEntrModle;
import com.rz.httpapi.bean.PrivateGroupBean;
import com.rz.httpapi.bean.PrivateGroupListBean;

import org.greenrobot.eventbus.EventBus;

import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Response;

import static com.rz.circled.event.EventConstant.PRIVATE_GROUP_BELONG_ID;

/**
 * Created by rzw2 on 2017/9/5.
 */

public class ApplyForPrivateGroupBelongActivity extends BaseActivity {
    @BindView(R.id.gv)
    GridView gv;

    //私圈相关
    private PrivateGroupPresenter mPresenter;
    private PrivateGroupBelongAdapter mAdapter;
    private String selectId;

    public static void startPrivateGroupBelong(Context context, String id) {
        Intent i = new Intent(context, ApplyForPrivateGroupBelongActivity.class);
        i.putExtra(IntentKey.EXTRA_ID, id);
        context.startActivity(i);
    }

    @Nullable
    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_apply_for_private_group_belong, null);
    }

    @Override
    public void initPresenter() {
        mPresenter = new PrivateGroupPresenter();
        mPresenter.attachView(this);
    }

    @Override
    public void initView() {
        selectId = getIntent().getStringExtra(IntentKey.EXTRA_ID);
        gv.setAdapter(mAdapter = new PrivateGroupBelongAdapter(mContext, R.layout.item_private_group_belong));
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CircleEntrModle data = mAdapter.getItem(position);
                EventBus.getDefault().post(new BaseEvent(PRIVATE_GROUP_BELONG_ID, data.appId + "|" + data.circleName));
                finish();
            }
        });
    }

    @Override
    public void initData() {
        mPresenter.privateGroupBelong(Session.getUserId());
    }

    @Override
    public <T> void updateView(T t) {
        super.updateView(t);
        if (t instanceof CircleBelongBean) {
            CircleBelongBean data = (CircleBelongBean) t;
            mAdapter.setCircleId(selectId);
            mAdapter.setData(data.getCircleList());
        }
    }

    @Override
    protected boolean needLoadingView() {
        return true;
    }

    @Override
    public void refreshPage() {
        initData();
    }
}
