package com.rz.circled.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.rz.circled.R;
import com.rz.circled.adapter.PrivateGroupBelongAdapter;
import com.rz.circled.adapter.SearchCircleAdapter;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.CommonCode;
import com.rz.common.constant.IntentKey;
import com.rz.common.event.BaseEvent;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.widget.svp.SVProgressHUD;
import com.rz.httpapi.api.ApiPGService;
import com.rz.httpapi.api.BaseCallback;
import com.rz.httpapi.api.Http;
import com.rz.httpapi.api.ResponseData.ResponseData;
import com.rz.httpapi.bean.CircleBelongBean;
import com.rz.httpapi.bean.CircleEntrModle;
import com.rz.httpapi.bean.NewsBean;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

import static com.rz.circled.event.EventConstant.PRIVATE_GROUP_BELONG_ID;

/**
 * Created by rzw2 on 2017/9/5.
 */

public class ApplyForPrivateGroupBelongActivity extends BaseActivity {
    @BindView(R.id.gv)
    GridView gv;

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
        Http.getApiService(ApiPGService.class).privateGroupBelong(Session.getUserId()).enqueue(new BaseCallback<ResponseData<CircleBelongBean>>() {
            @Override
            public void onResponse(Call<ResponseData<CircleBelongBean>> call, Response<ResponseData<CircleBelongBean>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    if (!response.body().isSuccessful()) {
                        SVProgressHUD.showErrorWithStatus(mContext, response.body().getMsg());
                    } else {
                        CircleBelongBean circleBelongBean = response.body().getData();
                        for (String circleId :
                                circleBelongBean.getCircleIdList()) {
                            Iterator<CircleEntrModle> sListIterator = circleBelongBean.getCircleList().iterator();
                            while (sListIterator.hasNext()) {
                                if (TextUtils.equals(circleId, sListIterator.next().appId)) {
                                    sListIterator.remove();
                                    break;
                                }
                            }
                        }
                        if (circleBelongBean.getCircleList().size() > 0) {
                            mAdapter.setCircleId(selectId);
                            mAdapter.setData(circleBelongBean.getCircleList());
                        } else {
                            onLoadingStatus(CommonCode.General.DATA_EMPTY);
                        }
                    }
                } else {
                    SVProgressHUD.showErrorWithStatus(mContext, getString(R.string.request_failed));
                }
            }

            @Override
            public void onFailure(Call<ResponseData<CircleBelongBean>> call, Throwable t) {
                super.onFailure(call, t);
                SVProgressHUD.showErrorWithStatus(mContext, getString(R.string.request_failed));
            }
        });
    }
}
