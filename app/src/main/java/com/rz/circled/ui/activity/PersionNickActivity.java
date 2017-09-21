package com.rz.circled.ui.activity;

import android.text.Editable;
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
import com.rz.common.widget.svp.SVProgressHUD;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/9/6 0006.
 */

public class PersionNickActivity extends BaseActivity implements View.OnClickListener, TextWatcher {
    //昵称
    @BindView(R.id.id_person_nick_edit)
    EditText idPersonNickEdit;
    @BindView(R.id.id_person_clear)
    ImageView idPersonClearImg;

    final int RESULT_CODE1 = 102;//退回到个人信息
    protected IPresenter presenter;

    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.aty_my_nick, null);
    }


    @Override
    public void initView() {
        setTitleText(getString(R.string.mine_person_nick_change));
        setTitleRightText(getString(R.string.mine_person_save));
        idPersonNickEdit.setText(Session.getUserName());
        idPersonNickEdit.setSelection(idPersonNickEdit.getText().toString().length());
        idPersonNickEdit.addTextChangedListener(this);

        if (TextUtils.isEmpty(idPersonNickEdit.getText().toString())) {
            idPersonClearImg.setVisibility(View.INVISIBLE);
        } else {
            idPersonClearImg.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void initData() {

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
        Session.setUserName(t.toString());
        setResult(RESULT_CODE1);
        EventBus.getDefault().post(new BaseEvent(CommonCode.EventType.TYPE_USER_UPDATE));
        finish();
    }

    @OnClick({R.id.id_person_clear, R.id.tv_base_title_right})
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_base_title_right:
                //保存操作
                String nickName = idPersonNickEdit.getText().toString();

                if (StringUtils.isEmpty(nickName)) {
                    SVProgressHUD.showErrorWithStatus(this, "昵称不能为空");
                    return;
                }

                if (TextUtils.equals(nickName, Session.getUserName())) {
                    setResult(RESULT_CODE1);
                    finish();
                    return;
                }

                ((PersonInfoPresenter) presenter).savePersonInfo(Session.getUserId(), "nickName", nickName, "");

                break;
            case R.id.id_person_clear:

                idPersonNickEdit.setText("");

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

    }



}
