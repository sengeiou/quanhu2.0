package com.rz.circled.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rz.circled.R;
import com.rz.common.constant.CommonCode;
import com.rz.common.constant.IntentKey;
import com.rz.common.event.BaseEvent;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.httpapi.bean.ProveStatusBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

public class ChooseProveIdentityActivity extends BaseActivity {

    @BindView(R.id.iv_choose_prove_status_verify)
    ImageView ivStatusVerify;
    @BindView(R.id.tv_choose_prove_status_title)
    TextView tvStatusTitle;
    @BindView(R.id.tv_choose_prove_status_hint)
    TextView tvStatusHint;
    @BindView(R.id.tv_choose_prove_status_content)
    TextView tvStatusContent;
    @BindView(R.id.tv_choose_prove_status_change)
    TextView tvStatusChange;
    @BindView(R.id.ll_choose_prove_status)
    LinearLayout llStatus;


    private ProveStatusBean proveStatusBean;

    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_choose_prove_identity, null);
    }

    @Override
    public void initView() {
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    public void initData() {
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey(IntentKey.EXTRA_SERIALIZABLE)) {
            setTitleText(R.string.qh_prove);
            llStatus.setVisibility(View.VISIBLE);
            proveStatusBean = (ProveStatusBean) extras.getSerializable(IntentKey.EXTRA_SERIALIZABLE);
            switch (proveStatusBean.getAuthStatus()) {
                case ProveStatusBean.STATUS_ING:
                    tvStatusTitle.setText(R.string.prove_info_submit_success);
                    tvStatusHint.setText(R.string.prove_info_verify_ing);
                    tvStatusHint.setVisibility(View.VISIBLE);
                    break;
                case ProveStatusBean.STATUS_FAIL:
                case ProveStatusBean.STATUS_CANCEL:
                    tvStatusTitle.setText(R.string.prove_fail);
                    tvStatusChange.setVisibility(View.VISIBLE);
                    tvStatusChange.setText(R.string.prove_again);
                    break;
                case ProveStatusBean.STATUS_SUCCESS:
                    tvStatusTitle.setText(getString(R.string.prove_identity_hint) +
                            (proveStatusBean.isOneSelf() ? getString(R.string.prove_oneself) : getString(R.string.prove_agency)));
                    tvStatusContent.setText(getString(R.string.prove_industry_sector) + proveStatusBean.getTradeField());
                    tvStatusContent.setVisibility(View.VISIBLE);
                    tvStatusChange.setVisibility(View.VISIBLE);
                    tvStatusChange.setText(R.string.change_prove_info_hint);
                    break;
            }
        } else {
            llStatus.setVisibility(View.GONE);
            setTitleText(R.string.choose_prove_identity);
        }
    }

    @OnClick({R.id.iv_choose_prove_oneself, R.id.iv_choose_prove_agency, R.id.tv_choose_prove_status_change})
    public void onClick(View view) {
        Intent intent = new Intent(this, ProveWriteInfoActivity.class);
        switch (view.getId()) {
            case R.id.iv_choose_prove_oneself:
                intent.putExtra(IntentKey.EXTRA_BOOLEAN, true);
                break;
            case R.id.iv_choose_prove_agency:
                intent.putExtra(IntentKey.EXTRA_BOOLEAN, false);
                break;
            case R.id.tv_choose_prove_status_change:
                intent.putExtra(IntentKey.EXTRA_BOOLEAN, proveStatusBean != null ? proveStatusBean.isOneSelf() : true);
                intent.putExtra(ProveWriteInfoActivity.EXTRA_CHANGE, true);
                break;
        }
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BaseEvent baseEvent) {
        if (baseEvent.type == CommonCode.EventType.PROVE_UPDATE)
            finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }
}
