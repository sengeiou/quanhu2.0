package com.rz.circled.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.rz.circled.R;
import com.rz.circled.adapter.SearchCircleAdapter;
import com.rz.common.constant.IntentKey;
import com.rz.common.event.BaseEvent;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.httpapi.bean.CircleEntrModle;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.rz.circled.event.EventConstant.PRIVATE_GROUP_BELONG_ID;

/**
 * Created by rzw2 on 2017/9/5.
 */

public class ApplyForPrivateGroupBelongActivity extends BaseActivity {
    @BindView(R.id.gv)
    GridView gv;

    private SearchCircleAdapter mAdapter;
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
        gv.setAdapter(mAdapter = new SearchCircleAdapter(mContext, R.layout.item_choose_circle));
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EventBus.getDefault().post(new BaseEvent(PRIVATE_GROUP_BELONG_ID, ((CircleEntrModle) mAdapter.getItem(position)).appId));
                finish();
            }
        });
    }

    @Override
    public void initData() {

    }
}
