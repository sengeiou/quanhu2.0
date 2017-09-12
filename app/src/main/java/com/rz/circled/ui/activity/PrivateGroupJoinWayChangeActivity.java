package com.rz.circled.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.rz.circled.R;
import com.rz.common.constant.IntentKey;
import com.rz.common.event.BaseEvent;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.widget.toasty.Toasty;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

import static com.rz.circled.event.EventConstant.PRIVATE_GROUP_JOIN_WAY;

/**
 * Created by rzw2 on 2017/9/9.
 */

public class PrivateGroupJoinWayChangeActivity extends BaseActivity {
    @BindView(R.id.cbx_free)
    CheckBox cbxFree;
    @BindView(R.id.cbx_pay)
    CheckBox cbxPay;
    @BindView(R.id.btn_less)
    TextView btnLess;
    @BindView(R.id.etv_num)
    EditText etvNum;
    @BindView(R.id.btn_add)
    TextView btnAdd;

    public static void startJoinWay(Context context, int money) {
        Intent i = new Intent(context, PrivateGroupJoinWayChangeActivity.class);
        i.putExtra(IntentKey.EXTRA_MONEY, money);
        context.startActivity(i);
    }

    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_join_private_group_way_change, null, false);
    }

    @Override
    public void initView() {
        int price = getIntent().getExtras().getInt(IntentKey.EXTRA_MONEY);
        if (price == 0) {
            cbxFree.setChecked(true);
            cbxPay.setChecked(false);
        } else {
            cbxFree.setChecked(false);
            cbxPay.setChecked(true);
            etvNum.setText(String.valueOf(price));
        }
        setTitle(R.string.private_group_join_way_change);
        setTitleRightText(R.string.submit);
        setTitleRightListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseEvent event = new BaseEvent();
                event.setType(PRIVATE_GROUP_JOIN_WAY);
                if (cbxFree.isChecked()) {
                    event.setData(0);
                } else {
                    int num = TextUtils.isEmpty(etvNum.getText().toString().trim()) ? 1 : Integer.parseInt(etvNum.getText().toString().trim());
                    event.setData(num);
                }
                EventBus.getDefault().post(event);
                finish();
            }
        });
        cbxFree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cbxPay.setChecked(false);
                } else {
                    if (!cbxPay.isChecked()) {
                        cbxFree.setChecked(true);
                    }
                }
            }
        });
        cbxPay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cbxFree.setChecked(false);
                } else {
                    if (!cbxFree.isChecked()) {
                        cbxPay.setChecked(true);
                    }
                }
            }
        });
        etvNum.setSelection(etvNum.getText().length());
        etvNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Integer.parseInt(s.toString()) > 500) {
                    Toasty.warning(mContext, getString(R.string.private_group_join_price_max)).show();
                    etvNum.setText(String.valueOf(500));
                }
                if (Integer.parseInt(s.toString()) < 1) {
                    Toasty.warning(mContext, getString(R.string.private_group_join_price_min)).show();
                    etvNum.setText(String.valueOf(1));
                }
            }
        });
    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.btn_less, R.id.btn_add})
    public void onClick(View view) {
        int num = TextUtils.isEmpty(etvNum.getText().toString().trim()) ? 1 : Integer.parseInt(etvNum.getText().toString().trim());
        switch (view.getId()) {
            case R.id.btn_less:
                num--;
                etvNum.setText(num + "");
                break;
            case R.id.btn_add:
                num++;
                etvNum.setText(num + "");
                break;
        }
    }
}
