package com.rz.circled.ui.activity;

import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.rz.circled.R;
import com.rz.circled.presenter.impl.AccountPresenter;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.IntentCode;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.utils.StringUtils;
import com.rz.common.widget.svp.SVProgressHUD;
import com.rz.httpapi.bean.UserInfoModel;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 作者：Administrator on 2017/1/6 0006 16:57
 * 功能：设置或者查看用户密保信息
 * 说明：
 */
public class SettingSafeUserAty extends BaseActivity {

//    //是否提醒用户
//    @BindView(R.id.id_remind_user_txt)
//    TextView mTxtRemindSafe;

    //姓名
    @BindView(R.id.id_your_name_edit)
    EditText mEditName;

    //身份证号码
    @BindView(R.id.id_idcard_edit)
    EditText mEditIdCard;

    //错误信息提示
    @BindView(R.id.id_remind_error_txt)
    TextView mTxtError;

    //保存
    @BindView(R.id.id_save_userinfo_btn)
    Button mSaveBtn;
    private AccountPresenter mPresenter;

    @Nullable
    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.aty_set_safe_user, null);
    }

    @Override
    public void initPresenter() {
        mPresenter = new AccountPresenter();
        mPresenter.attachView(this);
    }

    @Override
    public void initView() {
        if (Session.getUserSafetyproblem()) {
            setTitleText(getString(R.string.safe_protect_info));
//            mTxtRemindSafe.setVisibility(View.GONE);
            mSaveBtn.setVisibility(View.GONE);
            mEditName.setFocusable(false);
            mEditName.setFocusableInTouchMode(false);
            mEditIdCard.setFocusable(false);
            mEditIdCard.setFocusableInTouchMode(false);

            mPresenter.isSettingPw();

        } else {
            setTitleText(getString(R.string.setting_safe_info));
//            mTxtRemindSafe.setVisibility(View.VISIBLE);
            mSaveBtn.setVisibility(View.VISIBLE);
            mEditName.setFocusable(true);
            mEditName.setFocusableInTouchMode(true);
            mEditIdCard.setFocusable(true);
            mEditIdCard.setFocusableInTouchMode(true);
        }
    }

    @Override
    public void initData() {
        if (!Session.getUserSafetyproblem()) {
            mEditName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String s = charSequence + "";
                    if (!StringUtils.isEmpty(s) && s.length() >= 2 && s.length() <= 20) {
                        mTxtError.setText("");
                    } else {
                        mTxtError.setText(R.string.name_length);
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });
            mEditIdCard.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String idCard = charSequence + "";
                    if (StringUtils.isEmpty(idCard) || idCard.length() != 18) {
                        mTxtError.setText(R.string.id_length);
                    } else {
                        mTxtError.setText("");
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });
            mEditIdCard.setKeyListener(new DigitsKeyListener() {
                @Override
                public int getInputType() {
                    return InputType.TYPE_TEXT_VARIATION_PASSWORD;
                }

                @Override
                protected char[] getAcceptedChars() {
                    char[] data = getResources().getString(
                            R.string.edit_only_can_input).toCharArray();
                    return data;
                }
            });
        }
    }

    @OnClick({R.id.id_save_userinfo_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            //设置安全保护信息
            case R.id.id_save_userinfo_btn:
                String name = mEditName.getText().toString().trim();
                String idCard = mEditIdCard.getText().toString().trim();
                if (!StringUtils.isEmpty(name) && name.length() >= 2 && name.length() <= 15) {
                    mTxtError.setText("");
                    if (!StringUtils.isEmpty(idCard) && idCard.length() == 18) {
                        mTxtError.setText("");
                        mPresenter.setSecurityProblem(name, idCard);
                    } else {
                        mTxtError.setText(R.string.id_length);
                    }
                } else {
                    mTxtError.setText(R.string.name_length);
                }
                break;
        }
    }

    @Override
    public <T> void updateView(T t) {
        super.updateView(t);
        SVProgressHUD.showInfoWithStatus(aty, getString(R.string.setting_sucess));
        Session.setUserSafetyproblem(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setResult(IntentCode.SafeUserCode.SAFEUSERCODE_RESULT_CODE);
                finish();
            }
        }, 2000);
    }

    @Override
    public <T> void updateViewWithFlag(T t, int flag) {
        super.updateViewWithFlag(t, flag);
        UserInfoModel userInfoModel = (UserInfoModel) t;
        String name = userInfoModel.getPhyName();
        String idCard = userInfoModel.getPhyCardNo();
        if (!StringUtils.isEmpty(name)) {
            mEditName.setText("*" + name.substring(name.length() - 1, name.length()));
//            mEditName.setText(StringUtils.replaceSubString(custNname, 0, 1));
        } else {
            mEditName.setText("null");
        }
        if (!StringUtils.isEmpty(idCard)) {
            mEditIdCard.setText(StringUtils.replaceSubString(idCard, 2, 2));
        }
    }

    @Override
    public void refreshPage() {

    }
}
