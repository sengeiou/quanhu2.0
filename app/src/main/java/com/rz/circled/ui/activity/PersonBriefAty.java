package com.rz.circled.ui.activity;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.rz.circled.R;
import com.rz.circled.presenter.IPresenter;
import com.rz.circled.presenter.impl.PersonInfoPresenter;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.CommonCode;
import com.rz.common.event.BaseEvent;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xiayumo on 16/8/16.
 */
public class PersonBriefAty extends BaseActivity implements View.OnClickListener, TextWatcher {

    @BindView(R.id.id_person_brief_edit)
    EditText idPersonPriefEdit;
    @BindView(R.id.id_person_clear)
    ImageView idPersonClearImg;

    public static final String TYPE = "type";//区分个人签名和个人简介

    final int RESULT_CODE1 = 102;//退回到个人信息
    protected IPresenter presenter;

    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.aty_my_brief, null);
    }

    @Override
    public void initView() {
        setTitleText(getIntent().getExtras().getString(TYPE));
        setTitleRightText(getString(R.string.mine_person_save));
        idPersonPriefEdit.setText(getIntent().getExtras().getString("content", ""));
        idPersonPriefEdit.setSelection(idPersonPriefEdit.getText().toString().length());
        idPersonPriefEdit.addTextChangedListener(this);


        if (getIntent().getExtras().getString(TYPE).equals(getString(R.string.mine_person_sign))) {
            idPersonPriefEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)});
        } else {
            idPersonPriefEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
        }

        if (TextUtils.isEmpty(idPersonPriefEdit.getText().toString())) {
            idPersonClearImg.setVisibility(View.INVISIBLE);
        } else {
            idPersonClearImg.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void initPresenter() {
        super.initPresenter();
        presenter = new PersonInfoPresenter();
        presenter.attachView(this);
    }

    @Override
    public <T> void updateView(T t) {
        super.updateView(t);

        if (getIntent().getExtras().getString(TYPE).equals(getString(R.string.mine_person_sign))) {
            Session.setUser_signatrue(t.toString());
            EventBus.getDefault().post(new BaseEvent(CommonCode.EventType.TYPE_USER_UPDATE));
        } else {
            Session.setUser_desc(t.toString());
        }

        setResult(RESULT_CODE1);

        finish();

    }


    @Override
    public void initData() {

    }

    @OnClick({R.id.id_person_clear, R.id.tv_base_title_right})
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_base_title_right:

                String uid = Session.getUserId();

                String content = idPersonPriefEdit.getText().toString();

                String key = getIntent().getExtras().getString(TYPE).equals(getString(R.string.mine_person_sign)) ?
                        "signature" : "desc";

                if (!StringUtils.isEmpty(uid))
                    ((PersonInfoPresenter) presenter).savePersonInfo(uid, key, content, "");

                break;
            case R.id.id_person_clear:

                idPersonPriefEdit.setText("");

                break;
            default:
                break;

        }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if (TextUtils.isEmpty(s)) {
            idPersonClearImg.setVisibility(View.INVISIBLE);
        } else {
            idPersonClearImg.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    @Override
    public void refreshPage() {

    }
}
