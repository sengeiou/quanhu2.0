package com.rz.circled.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rz.circled.R;
import com.rz.circled.presenter.impl.ProveInfoPresenter;
import com.rz.common.constant.CommonCode;
import com.rz.common.constant.IntentKey;
import com.rz.common.event.BaseEvent;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.utils.NetUtils;
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
    private ProveInfoPresenter proveInfoPresenter;

    public static void startProveIdentity(Context context, ProveStatusBean proveStatusBean) {
        Intent i = new Intent(context, ChooseProveIdentityActivity.class);
        if (proveStatusBean != null) {
            i.putExtra(IntentKey.EXTRA_SERIALIZABLE, proveStatusBean);
        }
        context.startActivity(i);
    }

    @Override
    protected boolean needLoadingView() {
        return true;
    }

    @Override
    protected boolean hasDataInPage() {
        return false;
    }

    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_choose_prove_identity, null);
    }

    @Override
    public void initPresenter() {
        super.initPresenter();
        proveInfoPresenter = new ProveInfoPresenter();
        proveInfoPresenter.attachView(this);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey(IntentKey.EXTRA_SERIALIZABLE)) {
            proveStatusBean = (ProveStatusBean) extras.getSerializable(IntentKey.EXTRA_SERIALIZABLE);
            if (!NetUtils.isNetworkConnected(mContext)) {
                onLoadingStatus(CommonCode.General.UN_NETWORK, mContext.getString(R.string.no_net_work));
                return;
            }
            onLoadingStatus(CommonCode.General.DATA_LOADING);
            proveInfoPresenter.getProveStatus();
        } else {
            llStatus.setVisibility(View.GONE);
            setTitleText(R.string.choose_prove_identity);
        }
    }

    @OnClick({R.id.iv_choose_prove_oneself, R.id.iv_choose_prove_agency, R.id.tv_choose_prove_status_change})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_choose_prove_oneself:
                Intent oneSelfIntent = new Intent(this, ProveWriteInfoActivity.class);
                oneSelfIntent.putExtra(IntentKey.EXTRA_BOOLEAN, true);
                startActivity(oneSelfIntent);
                break;
            case R.id.iv_choose_prove_agency:
                Intent agencyIntent = new Intent(this, ProveWriteInfoActivity.class);
                agencyIntent.putExtra(IntentKey.EXTRA_BOOLEAN, false);
                startActivity(agencyIntent);
                break;
            case R.id.tv_choose_prove_status_change:
                if (proveStatusBean != null) {
                    if (proveStatusBean.getAuthStatus() == ProveStatusBean.STATUS_FAIL || proveStatusBean.getAuthStatus() == ProveStatusBean.STATUS_CANCEL) {//失败
                        startActivity(new Intent(mContext, ChooseProveIdentityActivity.class));
                    } else {
                        Intent intent = new Intent(this, ProveWriteInfoActivity.class);
                        intent.putExtra(IntentKey.EXTRA_BOOLEAN, proveStatusBean.isOneSelf());
                        intent.putExtra(ProveWriteInfoActivity.EXTRA_CHANGE, true);
                        if (proveStatusBean != null) {
                            intent.putExtra(IntentKey.EXTRA_SERIALIZABLE, proveStatusBean);
                        }
                        startActivity(intent);
                    }
                }

                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BaseEvent baseEvent) {
        if (baseEvent.type == CommonCode.EventType.PROVE_UPDATE) {
            if (baseEvent.getData() == null)
                finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public <T> void updateViewWithFlag(T t, int flag) {
        super.updateViewWithFlag(t, flag);
        if (flag == ProveInfoPresenter.FLAG_PROVE_STATUS_SUCCESS) {//获得达人信息申请状态成功
            onLoadingStatus(CommonCode.General.DATA_SUCCESS);
            proveStatusBean = (ProveStatusBean) t;
            processStatus();
            BaseEvent baseEvent = new BaseEvent(CommonCode.EventType.PROVE_UPDATE);
            baseEvent.setData(proveStatusBean);
            EventBus.getDefault().post(baseEvent);
        }
        if (flag == ProveInfoPresenter.FLAG_PROVE_STATUS_ERROR) {
            onLoadingStatus(CommonCode.General.UN_NETWORK);
        }
    }

    private void processStatus() {
        setTitleText(R.string.qh_prove);
        llStatus.setVisibility(View.VISIBLE);
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
    }

    @Override
    public void refreshPage() {

    }
}
