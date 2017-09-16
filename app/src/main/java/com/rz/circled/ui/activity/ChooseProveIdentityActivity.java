package com.rz.circled.ui.activity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import com.rz.circled.R;
import com.rz.common.constant.IntentKey;
import com.rz.common.ui.activity.BaseActivity;

import butterknife.OnClick;

public class ChooseProveIdentityActivity extends BaseActivity {

    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_choose_prove_identity, null);
    }

    @Override
    public void initView() {
        setTitleText(R.string.choose_prove_identity);
    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.iv_choose_prove_oneself, R.id.iv_choose_prove_agency})
    public void onClick(View view) {
        Intent intent = new Intent(this, ProveWriteInfoActivity.class);
        switch (view.getId()) {
            case R.id.iv_choose_prove_oneself:
                intent.putExtra(IntentKey.EXTRA_BOOLEAN, true);
                break;
            case R.id.iv_choose_prove_agency:
                intent.putExtra(IntentKey.EXTRA_BOOLEAN, false);
                break;
        }
        startActivity(intent);
    }
}
