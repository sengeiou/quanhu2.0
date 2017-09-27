package com.rz.circled.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.litesuits.common.utils.HexUtil;
import com.litesuits.common.utils.MD5Util;
import com.rz.circled.R;
import com.rz.circled.presenter.impl.AccountPresenter;
import com.rz.circled.presenter.impl.PayPresenter;
import com.rz.circled.presenter.impl.SnsAuthPresenter;
import com.rz.circled.widget.CommonAdapter;
import com.rz.circled.widget.MListView;
import com.rz.circled.widget.SwitchButton;
import com.rz.circled.widget.ViewHolder;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.CommonCode;
import com.rz.common.constant.IntentCode;
import com.rz.common.constant.IntentKey;
import com.rz.common.constant.Type;
import com.rz.common.event.BaseEvent;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.utils.CountDownTimer;
import com.rz.common.utils.DialogUtils;
import com.rz.common.utils.NetUtils;
import com.rz.common.utils.StringUtils;
import com.rz.common.widget.svp.SVProgressHUD;
import com.rz.httpapi.bean.LoginWayModel;
import com.rz.httpapi.bean.UserInfoModel;
import com.umeng.socialize.UMShareAPI;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
/**
 * 账户与安全
 */
public class AccountSafeAty extends BaseActivity {

    //当前账号相关
    @BindView(R.id.id_loginway_bg_img)
    ImageView mImgLoginPic;
    @BindView(R.id.id_loginway_name_txt)
    TextView mTxtLoginName;
    @BindView(R.id.id_loginway_id_txt)
    TextView mTxtLoginId;

    //登录方式
    @BindView(R.id.id_loginway_list)
    MListView mWayListV;
    CommonAdapter<LoginWayModel> mAdpLoginWay;
    List<LoginWayModel> mLoginWays = new ArrayList<LoginWayModel>();

    //是否设置登录密码
    @BindView(R.id.id_login_pw_txt)
    TextView mTxtLoginPw;

    //是否设置支付密码
    @BindView(R.id.id_issetting_pw_txt)
    TextView mTxtIsSetPw;

    //是否显示忘记支付免密
    @BindView(R.id.id_froget_pay_ll)
    LinearLayout mShowForgetPwLL;

    //是否设置安全保护信息
    @BindView(R.id.id_issetting_user_txt)
    TextView mTxtIsSetUser;

    //小额免密开关
    @BindView(R.id.id_easy_pay_btn)
    SwitchButton mEasyPayBtn;

    //错误代码
    private int loadingStatus;
    //错误信息
    private String mError;
    //记录设置支付密码的状态
    private int isSetPayPw;
    //第三方账号信息
    private SnsAuthPresenter mAuthPresenter;

    private AccountPresenter mAccountPresenter;

    private PayPresenter mPayPresenter;

    //记录微信的openId
    private String mRecordWxId;
    //记录QQ的openId
    private String mRecordQQId;
    //记录微博的openId
    private String mRecordSinaId;

    @Nullable
    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_account_safe, null);
    }

    @Override
    public void initPresenter() {
        mAuthPresenter = new SnsAuthPresenter();
        mAuthPresenter.attachView(this);

        mAccountPresenter = new AccountPresenter();
        mAccountPresenter.attachView(this);
        mAccountPresenter.setmAccountAty(this);

        mPayPresenter = new PayPresenter(false);
        mPayPresenter.attachView(this);
    }

    public void isSetLoginPw() {
        if (!Session.getUserLoginPw()) {
            //没有设置登录密码
            mTxtLoginPw.setText(getString(R.string.go_to_set_pay_pw));
            mTxtLoginPw.setTextColor(Color.parseColor("#FF0000"));
        } else {
            //设置了登录密码
            mTxtLoginPw.setText(getString(R.string.safe_modify));
            mTxtLoginPw.setTextColor(Color.parseColor("#999999"));
        }
    }

    //记录小额免密支付的状态
    private boolean mRecordCheckEasyPay;

    @Override
    public void initView() {
        setTitleText(getString(R.string.account_and_safe));
        isSetLoginPw();
//        setOnRefreshDataListener(this);
        int type = Session.getLoginWay();
        setItemByStatus(mImgLoginPic, mTxtLoginName, mTxtLoginId, type, Session.getUserName());
        mEasyPayBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (Session.getUserSetpaypw()) {
                    mPayPresenter.checkPayPw();
                    mRecordCheckEasyPay = isChecked;
                } else {
                    mEasyPayBtn.setCheckedNoEvent(false);
                    //未设置，去设置
                    View mSetPayPw = LayoutInflater.from(aty).inflate(R.layout.dialog_to_set_pay_passw, null);
                    final Dialog mSetDialog = DialogUtils.selfDialog(aty, mSetPayPw, true);
                    mSetPayPw.findViewById(R.id.id_set_pay_pw_txt).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mSetDialog.dismiss();
                            Intent intent = new Intent(aty, SetPayPassAty.class);
                            intent.putExtra(IntentKey.KEY_TYPE, Type.HAD_NO_SET_PW);
                            showActivity(aty, intent);
                        }
                    });
                    mSetDialog.show();
                }
            }
        });
        mAdpLoginWay = new CommonAdapter<LoginWayModel>(aty, mLoginWays, R.layout.adp_bind_status) {
            @Override
            public void convert(ViewHolder helper, final LoginWayModel item) {
                if (null != item) {
                    final int type = item.getType();
                    ImageView img = (ImageView) helper.getViewById(R.id.id_way_img);
                    TextView name = (TextView) helper.getViewById(R.id.id_way_name_txt);
                    TextView tvIdName = (TextView) helper.getViewById(R.id.id_way_id_txt);
                    final TextView isBind = (TextView) helper.getViewById(R.id.id_band_status_txt);
                    isBind.setEnabled(false);
                    setItemByStatus(img, name, tvIdName, type, item.getNickName());
                    //微博 微信
                    if (StringUtils.isEmpty(item.getThirdId())) {
                        //表示未绑定
                        isBind.setBackgroundResource(R.drawable.shape_bg_binglogin_sel);
                        isBind.setText(getString(R.string.bind));
                        isBind.setTextColor(Color.parseColor("#ffffff"));
                        isBind.setEnabled(true);
                    } else {
                        isBind.setBackgroundResource(R.drawable.rolle_f5f5f5_stroke_e5e5e5);
                        isBind.setText(getString(R.string.cancel_bind));
                        isBind.setTextColor(Color.parseColor("#666666"));
                        isBind.setEnabled(true);
                    }
                    //手机号
                    if (type == Type.LOGIN_PHONE && !TextUtils.isEmpty(Session.getUserPhone())) {
                        isBind.setBackgroundResource(R.color.white);
                        isBind.setText(getString(R.string.had_bind));
                        isBind.setTextColor(Color.parseColor("#666666"));
                        tvIdName.setText(StringUtils.replaceSubString(item.getThirdId(), 3, 4));
                    }
                    isBind.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (StringUtils.isEmpty(item.getThirdId())) {
                                //去绑定
                                if (item.getType() == Type.LOGIN_PHONE) {
                                    //手机绑定
                                    Intent bind = new Intent(aty, RegisterActivity.class);
                                    bind.putExtra(IntentKey.EXTRA_PAGE, IntentCode.Register.REGISTER_RESULT_CODE);
                                    showActivity(aty, bind);
                                } else {
                                    if (CountDownTimer.isFastClick()) {
                                        return;
                                    }
                                    //第三方绑定
                                    mAuthPresenter.setActionBind(0);
                                    if (type == Type.LOGIN_QQ) {
                                        mAuthPresenter.qqAuth(false);
                                    } else if (type == Type.LOGIN_WX) {
                                        mAuthPresenter.wxAuth(false);
                                    } else if (type == Type.LOGIN_SINA) {
                                        mAuthPresenter.wbAuth(false);
                                    }
                                }
                            } else {
                                //解绑
                                mAuthPresenter.setActionBind(1);
                                if (type == Type.LOGIN_QQ) {
                                    mAuthPresenter.delQQAuth(item.getThirdId());
//                                    mAuthPresenter.bindThirdAccout(type, item.getThirdId(), 1, "");
                                } else if (type == Type.LOGIN_WX) {
                                    mAuthPresenter.delWXAuth(item.getThirdId());
//                                    mAuthPresenter.bindThirdAccout(type, item.getThirdId(), 1, "");
                                } else if (type == Type.LOGIN_SINA) {
                                    mAuthPresenter.delWBAuth(item.getThirdId());
//                                    mAuthPresenter.bindThirdAccout(type, item.getThirdId(), 1, "");
                                }
                            }
                        }
                    });
                }
            }
        };
        mWayListV.setAdapter(mAdpLoginWay);
    }

    /**
     * 设置登录显示状态
     *
     * @param img
     * @param name
     * @param id
     * @param type
     * @param mID
     */
    public void setItemByStatus(ImageView img, TextView name, TextView id, int type, String mID) {
        if (StringUtils.isEmpty(mID)) {
            id.setVisibility(View.INVISIBLE);
        } else {
            id.setVisibility(View.VISIBLE);
            id.setText("ID:" + mID);
        }
        if (type == Type.LOGIN_PHONE) {
            img.setBackgroundResource(R.drawable.ic_phone);
            name.setText(getString(R.string.auth_phone));
            id.setVisibility(View.VISIBLE);
            id.setText(StringUtils.replaceSubString(Session.getUserPhone(), 3, 4));
        } else if (type == Type.LOGIN_QQ) {
            img.setBackgroundResource(R.drawable.ic_qq);
            name.setText(getString(R.string.qq));
        } else if (type == Type.LOGIN_WX) {
            img.setBackgroundResource(R.drawable.ic_wx);
            name.setText(getString(R.string.weixin));
        } else if (type == Type.LOGIN_SINA) {
            img.setBackgroundResource(R.drawable.ic_webo);
            name.setText(getString(R.string.weibo));
        }
    }

    @Override
    public void initData() {
        //获取登录方式
        List<LoginWayModel> mWays = (List<LoginWayModel>) mAccountPresenter.getCacheData();
        if (null != mWays && !mWays.isEmpty()) {
            updateView(mWays);
        }
        mAccountPresenter.getLoginWay();
        mAccountPresenter.isSettingPw();
    }

    @Override
    protected void onPause() {
        super.onPause();}

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean hasDataInPage() {
        return !mLoginWays.isEmpty();
    }

    @Override
    public void onLoadingStatus(int loadingStatus, String string) {
        if (loadingStatus == CommonCode.General.ERROR_DATA) {
            mEasyPayBtn.setCheckedNoEvent(!mRecordCheckEasyPay);
        }
        super.onLoadingStatus(loadingStatus, string);
    }

    @Override
    public <T> void updateView(T t) {
        if (null != t) {
            if (t instanceof String) {
                String pw = (String) t;
                if (StringUtils.isEmpty(pw)) {
                    mEasyPayBtn.setCheckedNoEvent(!mRecordCheckEasyPay);
                } else {
                    pw = HexUtil.encodeHexStr(MD5Util.md5(pw));
                    if (mRecordCheckEasyPay) {
                        mAccountPresenter.openOrClosePay(Type.OPEN_EASY_PAY, pw);
                    } else {
                        mAccountPresenter.openOrClosePay(Type.CLOSE_EASY_PAY, pw);
                    }
                }
            } else {
                List<LoginWayModel> mWays = (List<LoginWayModel>) t;
                if (null != mWays && !mWays.isEmpty()) {
                    mLoginWays.clear();
                    for (LoginWayModel model : mWays) {
                        if (model.getType() != Session.getLoginWay() && model.getType() != Type.LOGIN_QQ) {
                            mLoginWays.add(model);
                        }
                        if (!StringUtils.isEmpty(model.getThirdId()) && model.getType() == Session.getLoginWay()) {
                            if (model.getType() == Type.LOGIN_PHONE) {
                                mTxtLoginId.setText("ID:" + StringUtils.replaceSubString(Session.getUserPhone(), 3, 4));
                            } else {
                                mTxtLoginId.setText("ID:" + model.getNickName());
                            }
                        }
                        if (Session.getLoginWay() != Type.LOGIN_PHONE) {
                            if (model.getType() == Type.LOGIN_PHONE) {
                                if (StringUtils.isEmpty(model.getThirdId())) {
                                    Session.setUserLoginPw(false);
                                } else {
                                    //设置了登录密码
                                    Session.setUserLoginPw(true);
                                }
                            }
                        } else {
                            Session.setUserLoginPw(true);
                        }
                    }
                    mAdpLoginWay.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public <T> void updateViewWithLoadMore(T t, boolean loadMore) {
        if (null != t) {
            if (loadMore) {
                //第三方绑定操作
                mAccountPresenter.getLoginWay();
            } else {
                //免密支付操作
                int status = (Integer) t;
                if (status == Type.OPEN_EASY_PAY) {
                    mEasyPayBtn.setCheckedNoEvent(true);
                } else if (status == Type.CLOSE_EASY_PAY) {
                    mEasyPayBtn.setCheckedNoEvent(false);
                } else {
                    mEasyPayBtn.setCheckedNoEvent(!mRecordCheckEasyPay);
                }
            }
        }
    }

    @OnClick({R.id.id_loginway_pw_ll, R.id.id_loginway_pay_ll, R.id.id_easy_pay_btn, R.id.id_froget_pay_ll, R.id.id_user_info_ll})
    public void onClick(View view) {
        switch (view.getId()) {
            //修改登录密码
            case R.id.id_loginway_pw_ll:
                if (NetUtils.isNetworkConnected(aty)) {
                    String mStr = mTxtLoginPw.getText().toString().trim();
                    if (!StringUtils.isEmpty(mStr)) {
                        if (Session.getUserLoginPw()) {
                            //设置了登录密码-去修改登录密码
                            showActivity(aty,ModifyPwdAty.class);
                        } else {
                            //未设置登录密码-去设置登录密码
                            //手机绑定
                            Intent bind = new Intent(aty, RegisterActivity.class);
                            bind.putExtra(IntentKey.EXTRA_PAGE, IntentCode.Register.REGISTER_RESULT_CODE);
                            showActivity(aty, bind);
                        }
                    }
                }
                break;
            //支付密码
            case R.id.id_loginway_pay_ll:
                if (loadingStatus == CommonCode.General.DATA_SUCCESS) {
                    
                    Intent intent = new Intent(aty, SetPayPassAty.class);
                    intent.putExtra(IntentKey.KEY_TYPE, isSetPayPw);
                    showActivity(aty, intent);
                } else {
                    SVProgressHUD.showInfoWithStatus(aty, getString(R.string.load_fail));
                }
                break;
            //免密支付
            case R.id.id_easy_pay_btn:
                if (loadingStatus == CommonCode.General.DATA_SUCCESS) {
//                    if (mEasyPayBtn.isChecked()) {
//                        (mAccountPresenter).openOrClosePay(Type.CLOSE_EASY_PAY);
//                    } else {
//                        (mAccountPresenter).openOrClosePay(Type.OPEN_EASY_PAY);
//                    }
                    Log.d("Account", mEasyPayBtn.isChecked() + "-----------3");
                } else {
                    SVProgressHUD.showErrorWithStatus(aty, getString(R.string.load_fail));
                }
                break;
            //忘记支付密码
            case R.id.id_froget_pay_ll:
                showActivity(aty, ResetPayPwAty.class);
                break;
            //安全保护信息
            case R.id.id_user_info_ll:
                if (loadingStatus == CommonCode.General.DATA_SUCCESS) {
                    Intent intent = new Intent(aty, SettingSafeUserAty.class);
                    startActivityForResult(intent, IntentCode.SafeUserCode.SAFEUSERCODE_REQUEST_CODE);
                } else {
                    SVProgressHUD.showErrorWithStatus(aty, getString(R.string.load_fail));
                }
                break;
        }
    }

    /**
     * 处理支付密码或者小额免密获取错误的信息
     */
    public void handPwNews(int loadingStatus, String error) {
        this.loadingStatus = loadingStatus;
        this.mError = error;
    }

    /**
     * 处理支付密码,用户是否设置安全保护信息
     */
    public void handPw(UserInfoModel model) {
        if (null != model) {
            isSetPayPw = model.getIsPayPassword();
            if (isSetPayPw == Type.HAD_SET_PW) {
                //设置了支付密码
                mTxtIsSetPw.setText(getString(R.string.safe_modify));
                mTxtIsSetPw.setTextColor(Color.parseColor("#999999"));
                Session.setUserSetpaypw(true);
                mShowForgetPwLL.setVisibility(View.VISIBLE);
            } else {
                //没有设置了支付密码
                mTxtIsSetPw.setText(getString(R.string.safe_is_setting));
                mTxtIsSetPw.setTextColor(Color.parseColor("#FF0000"));
                Session.setUserSetpaypw(false);
                mShowForgetPwLL.setVisibility(View.GONE);
            }
            if (StringUtils.isEmpty(model.getPhyName())) {
                //没有设置安全保护信息
                mTxtIsSetUser.setText(getString(R.string.safe_is_setting));
                mTxtIsSetUser.setTextColor(Color.parseColor("#FF0000"));
                Session.setUserSafetyproblem(false);
            } else {
                //设置安全保护信息
                mTxtIsSetUser.setText(getString(R.string.safe_had_setting));
                mTxtIsSetUser.setTextColor(Color.parseColor("#999999"));
                Session.setUserSafetyproblem(true);
            }
            if (model.getSmallNopass() == Type.OPEN_EASY_PAY) {
                //开启了免密支付
                mEasyPayBtn.setCheckedNoEvent(true);
                Session.setIsOpenGesture(true);
            } else {
                //没有开启了免密支付
                mEasyPayBtn.setCheckedNoEvent(false);
                Session.setIsOpenGesture(false);
            }
        }
    }

    @Subscribe
    public void evenUpdateNews(BaseEvent event) {
        isSetLoginPw();
        if (TextUtils.equals("0", event.key)) {
            //设置支付密码
            mAccountPresenter.isSettingPw();
        } else if (TextUtils.equals("1", event.key)) {
            //绑定手机号
            SVProgressHUD.showSuccessWithStatus(aty, getString(R.string.bind_success));
            mAccountPresenter.getLoginWay();
//            (mAccountPresenter).isSettingPw();
        } else if (TextUtils.equals("2", event.key)) {
//            //修改支付密码
//            if (!isFinishing()) {
//                SVProgressHUD.showSuccessWithStatus(aty, getString(R.string.modify_success));
//            }
//            if (SVProgressHUD.isShowing(aty)) {
//                SVProgressHUD.dismiss(aty);
//            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentCode.SafeUserCode.SAFEUSERCODE_REQUEST_CODE) {
            if (resultCode == IntentCode.SafeUserCode.SAFEUSERCODE_RESULT_CODE) {
                Session.setUserSafetyproblem(true);
                //设置安全保护信息
                mTxtIsSetUser.setText(getString(R.string.safe_had_setting));
                mTxtIsSetUser.setTextColor(Color.parseColor("#999999"));
                Session.setUserSafetyproblem(true);
            }
        }
    }

    @Override
    public void refreshPage() {

    }
}
