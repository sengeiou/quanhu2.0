package com.rz.circled.ui.activity;

import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.litesuits.common.utils.HexUtil;
import com.litesuits.common.utils.MD5Util;
import com.rz.circled.R;
import com.rz.circled.presenter.impl.BankPresenter;
import com.rz.circled.presenter.impl.PayPresenter;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.IntentCode;
import com.rz.common.constant.Type;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.utils.CountDownTimer;
import com.rz.common.widget.svp.SVProgressHUD;
import com.rz.httpapi.bean.UserInfoModel;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/7/26 0026.
 * 添加银行卡
 */
public class AddBankCardAty extends BaseActivity {

    //持卡人
    @BindView(R.id.id_et_user_card_name_edit)
    EditText mEtUserName;

    @BindView(R.id.id_clear_card_name_img)
    ImageView mImgClearName;
    //银行卡号
    @BindView(R.id.id_card_bank_num_edit)
    EditText mEtBankNum;

    private String mUserName;
    private String mBankNum;
    @BindView(R.id.id_clear_card_num_img)
    ImageView mImgClearNum;

    private PayPresenter mPayPresenter;
    private BankPresenter presenter;

    @Override
    public boolean hasDataInPage() {
        return true;
    }

    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_add_bank, null);
    }

    @Override
    public void initPresenter() {
        presenter = new BankPresenter();
        presenter.attachView(this);
        mPayPresenter = new PayPresenter(false);
        mPayPresenter.attachView(this);
    }

    @Override
    public void initView() {
        setTitleText(getString(R.string.add_bank_card));
        //持卡人
        mEtUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mUserName = charSequence + "";
                if (TextUtils.isEmpty(mUserName)) {
                    mImgClearName.setVisibility(View.GONE);
                } else {
                    mImgClearName.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //银行卡号
        mEtBankNum.addTextChangedListener(new TextWatcher() {
            private int oldLength = 0;
            private boolean isChange = true;
            private int curLength = 0;
            private int emptyNumB = 0;
            private int emptyNumA = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                oldLength = s.length();
                emptyNumB = 0;
                for (int i = 0; i < s.toString().length(); i++) {
                    if (s.charAt(i) == ' ') emptyNumB++;
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                curLength = s.length();
                isChange = !(curLength == oldLength || curLength <= 3);

                if (TextUtils.isEmpty(s)) {
                    mImgClearNum.setVisibility(View.GONE);
                } else {
                    mImgClearNum.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isChange) {
                    int selectIndex = mEtBankNum.getSelectionEnd();//获取光标位置
                    String content = s.toString().replaceAll(" ", "");
                    Log.i(TAG, "content:" + content);
                    StringBuffer sb = new StringBuffer(content);
                    //遍历加空格
                    int index = 1;
                    emptyNumA = 0;
                    for (int i = 0; i < content.length(); i++) {
                        if ((i + 1) % 4 == 0) {
                            sb.insert(i + index, " ");
                            index++;
                            emptyNumA++;
                        }
                    }
                    Log.i(TAG, "result content:" + sb.toString());
                    String result = sb.toString();
                    if (result.endsWith(" ")) {
                        result = result.substring(0, result.length() - 1);
                    }
                    mEtBankNum.setText(result);
                    if (emptyNumA > emptyNumB)
                        selectIndex = selectIndex + (emptyNumA - emptyNumB);
                    //处理光标位置
                    if (selectIndex > result.length() || selectIndex + 1 == result.length()) {
                        selectIndex = result.length();
                    } else if (selectIndex < 0) {
                        selectIndex = 0;
                    }
                    mEtBankNum.setSelection(selectIndex);
                    isChange = false;
                }
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public <T> void updateView(T t) {
        if (null != t) {
            if (t instanceof Integer) {
                SVProgressHUD.showSuccessWithStatus(aty, getString(R.string.bind_success));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setResult(IntentCode.BankCard.BankCard_RESULT_CODE);
                        finish();
                    }
                }, 2000);
            } else if (t instanceof UserInfoModel) {
                UserInfoModel user = (UserInfoModel) t;
                if (null != user) {
                    if (Type.HAD_SET_PW == user.getIsPayPassword()) {
                        Session.setUserSetpaypw(true);
                    } else {
                        Session.setUserSetpaypw(false);
                    }
                    if (Type.OPEN_EASY_PAY == user.getSmallNopass()) {
                        Session.setIsOpenGesture(true);
                    } else {
                        Session.setIsOpenGesture(false);
                    }
                    mPayPresenter.checkIsSetPw();
                }
            } else if (t instanceof String) {
                String payPw = (String) t;
                if (TextUtils.isEmpty(payPw) || payPw.length() != 6) {
                    SVProgressHUD.showInfoWithStatus(aty, getString(R.string.bind_cancel));
                    return;
                }
                presenter.addBankCard(Session.getUserId(), mUserName, mBankNum, HexUtil.encodeHexStr(MD5Util.md5(payPw)));
            }
        }
    }

    @OnClick({R.id.id_card_submit_btn, R.id.id_clear_card_name_img, R.id.id_clear_card_num_img})
    public void onClick(View view) {
        switch (view.getId()) {
            //确定
            case R.id.id_card_submit_btn:
                if (CountDownTimer.isFastClick()) {
                    return;
                }
                mBankNum = mEtBankNum.getText().toString().trim().replace(" ", "");
                if (TextUtils.isEmpty(mUserName)
                        || TextUtils.isEmpty(mBankNum)) {
                    SVProgressHUD.showInfoWithStatus(aty, getString(R.string.name_or_card_cannot_null));
                    return;
                }

                if (mBankNum.length() < 16 || mBankNum.length() > 19) {
                    SVProgressHUD.showInfoWithStatus(aty, getString(R.string.confirm_card_sure));
                    return;
                }
                mPayPresenter.isSettingPw(true);
                break;
            //清除持卡人
            case R.id.id_clear_card_name_img:
                mEtUserName.setText("");
                break;
            //银行卡号
            case R.id.id_clear_card_num_img:
                mEtBankNum.setText("");
                break;
        }
    }
}
