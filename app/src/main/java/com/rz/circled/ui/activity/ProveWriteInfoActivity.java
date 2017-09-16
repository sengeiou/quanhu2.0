package com.rz.circled.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.rz.circled.R;
import com.rz.circled.presenter.impl.ProveInfoPresenter;
import com.rz.common.constant.IntentKey;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.widget.toasty.Toasty;
import com.rz.httpapi.bean.ProveInfoBean;

import butterknife.BindView;
import butterknife.OnClick;

public class ProveWriteInfoActivity extends BaseActivity {

    @BindView(R.id.tv_prove_true_name)
    TextView tvProveTrueName;
    @BindView(R.id.et_prove_true_name)
    EditText etProveTrueName;
    @BindView(R.id.tv_prove_phone)
    TextView tvProvePhone;
    @BindView(R.id.et_prove_phone)
    EditText etProvePhone;
    @BindView(R.id.tv_prove_id_number)
    TextView tvProveIdNumber;
    @BindView(R.id.et_prove_id_number)
    EditText etProveIdNumber;
    @BindView(R.id.tv_prove_area_title)
    TextView tvProveAreaTitle;
    @BindView(R.id.tv_prove_area)
    TextView tvProveArea;
    @BindView(R.id.et_prove_industry)
    EditText etProveIndustry;
    @BindView(R.id.et_prove_media_account)
    EditText etProveMediaAccount;
    @BindView(R.id.et_prove_resources)
    EditText etProveResources;
    @BindView(R.id.tv_prove_resources_num)
    TextView tvProveResourcesNum;
    @BindView(R.id.tv_prove_media_account)
    TextView tvProveMediaAccount;


    private boolean isOneSelf = true;//true个人认证,false企业认证
    private ProveInfoPresenter proveInfoPresenter;
    private final int REQUEST_PAPERWORK = -100;

    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_prove_write_info, null);
    }

    @Override
    protected boolean needLoadingView() {
        return true;
    }

    @Override
    protected boolean hasDataInPage() {
        return true;
    }

    @Override
    public void initView() {
        isOneSelf = getIntent().getBooleanExtra(IntentKey.EXTRA_BOOLEAN, true);
        setTitleRightTextColor(R.color.font_color_blue);
        setTitleText(R.string.complete_info);
        if (isOneSelf) {
            //真实姓名
            tvProveTrueName.setText(R.string.true_name);
            //手机号
            tvProvePhone.setText(R.string.phone_number);
            etProvePhone.setInputType(EditorInfo.TYPE_CLASS_PHONE);
            etProvePhone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
            //身份证号
            tvProveIdNumber.setText(R.string.id_number);
            tvProveIdNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(18)});
            tvProveIdNumber.setKeyListener(DigitsKeyListener.getInstance(getString(R.string.edit_only_can_input)));
            //所在地区
            tvProveAreaTitle.setText(R.string.area_where);
            //自媒体账号
            tvProveMediaAccount.setText(R.string.self_media_account);
            setTitleRightText(R.string.submit);
        } else {
            //机构名称
            tvProveTrueName.setText(R.string.agency_name);
            //运营者姓名
            tvProvePhone.setText(R.string.operator_name);
            etProvePhone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
            //联系电话
            tvProveIdNumber.setText(R.string.contact_phone);
            etProvePhone.setInputType(EditorInfo.TYPE_CLASS_PHONE);
            //所在城市
            tvProveAreaTitle.setText(R.string.area_where_city);
            //个人自媒体账号
            tvProveMediaAccount.setText(R.string.oneself_media_account);
            setTitleRightText(R.string.forget_next);
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public void initPresenter() {
        super.initPresenter();
        proveInfoPresenter = new ProveInfoPresenter();
        proveInfoPresenter.attachView(this);
    }

    @OnClick(R.id.tv_prove_area)
    public void onClick() {
        toSubmit();
    }

    private void toSubmit() {
        String trueName = etProveTrueName.getText().toString();
        String trueNameErrorHint = isOneSelf ? getString(R.string.true_name_error_hint) : getString(R.string.agency_name_error_hint);
        if (TextUtils.isEmpty(trueName) || trueName.length() < 2) {
            Toasty.info(mContext, trueNameErrorHint).show();
            return;
        }
        String phone = etProvePhone.getText().toString();
        String phoneErrorHint = isOneSelf ? getString(R.string.prove_phone_error_hint) : getString(R.string.prove_phone_error_hint);
        if (TextUtils.isEmpty(phone) || (isOneSelf && phone.length() != 11) || (!isOneSelf && phone.length() < 2)) {
            Toasty.info(mContext, phoneErrorHint).show();
            return;
        }
        String idNumber = etProveIdNumber.getText().toString();
        String idNumberErrorInfo = isOneSelf ? getString(R.string.prove_id_number_error_hint) : getString(R.string.prove_contact_phone_error_hint);
        if (TextUtils.isEmpty(idNumber) || (isOneSelf && (idNumber.length() != 16 || idNumber.length() != 18))) {
            Toasty.info(mContext, idNumberErrorInfo).show();
            return;
        }
        String areaInfo = tvProveArea.getText().toString();
        if (TextUtils.isEmpty(areaInfo)) {
            Toasty.info(mContext, getString(R.string.prove_area_error_hint)).show();
            return;
        }
        String industry = etProveIndustry.getText().toString();
        if (TextUtils.isEmpty(industry)) {
            Toasty.info(mContext, getString(R.string.prove_industry_error_hint)).show();
            return;
        }
        String mediaAccount = etProveMediaAccount.getText().toString();
        if (!TextUtils.isEmpty(mediaAccount) && mediaAccount.length() < 2) {
            Toasty.info(mContext, getString(R.string.prove_media_error_hint)).show();
            return;
        }
        String resource = etProveResources.getText().toString();
        if (TextUtils.isEmpty(resource) || resource.length() < 10) {
            Toasty.info(mContext, getString(R.string.prove_resource_error_hint)).show();
            return;
        }
        ProveInfoBean proveInfoBean = new ProveInfoBean();
        proveInfoBean.setAuthType(isOneSelf ? 0 : 1);
        proveInfoBean.setContactCall(isOneSelf ? phone : idNumber);
        proveInfoBean.setIdCard(idNumber);
        proveInfoBean.setLocation(areaInfo);
        proveInfoBean.setOrganizationName(trueName);
        proveInfoBean.setOwnerAppId(mediaAccount);
        proveInfoBean.setRealName(trueName);
        proveInfoBean.setResourceDesc(resource);
        proveInfoBean.setTradeField(industry);
        if (isOneSelf) {
            //提交
            Intent intent = new Intent(mContext, ProvePaperworkActivity.class);
            intent.putExtra(IntentKey.EXTRA_SERIALIZABLE, proveInfoBean);
            startActivityForResult(intent, REQUEST_PAPERWORK);
        } else {
            //下一步
            proveInfoPresenter.submitProveInfo(isOneSelf, proveInfoBean);
        }
    }

    @Override
    public <T> void updateViewWithFlag(T t, int flag) {
        super.updateViewWithFlag(t, flag);
        if (flag == proveInfoPresenter.FLAG_PROVE_INFO_SUCCESS) {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_PAPERWORK)
            finish();
    }
}
