package com.rz.circled.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.rz.circled.R;
import com.rz.circled.presenter.impl.ProveInfoPresenter;
import com.rz.common.constant.CommonCode;
import com.rz.common.constant.IntentKey;
import com.rz.common.event.BaseEvent;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.utils.NetUtils;
import com.rz.common.widget.toasty.Toasty;
import com.rz.httpapi.bean.ProveStatusBean;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

public class ProveWriteInfoActivity extends BaseActivity {

    public static final String EXTRA_CHANGE = "extraChange";//是否为修改资料

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
    private boolean isChange = false;//是否为修改
    private ProveInfoPresenter proveInfoPresenter;
    private final int REQUEST_PAPERWORK = 5;
    private final int REQUEST_AREA = 6;
    private ProveStatusBean infoBean;

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
        isChange = getIntent().getBooleanExtra(EXTRA_CHANGE, false);
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
            etProveIdNumber.setKeyListener(DigitsKeyListener.getInstance(getString(R.string.edit_only_can_input)));
            etProveIdNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(18)});
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
            etProveIdNumber.setInputType(EditorInfo.TYPE_CLASS_PHONE);
            etProveIdNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
            //所在城市
            tvProveAreaTitle.setText(R.string.area_where_city);
            //个人自媒体账号
            tvProveMediaAccount.setText(R.string.oneself_media_account);
            setTitleRightText(R.string.forget_next);
        }
        if (getIntent().getExtras().containsKey(IntentKey.EXTRA_SERIALIZABLE)) {
            infoBean = (ProveStatusBean) getIntent().getSerializableExtra(IntentKey.EXTRA_SERIALIZABLE);
            if (infoBean != null) {
                if (isOneSelf) {
                    etProveTrueName.setText(infoBean.getRealName());
                    etProvePhone.setText(infoBean.getContactCall());
                    etProveIdNumber.setText(infoBean.getIdCard());
                    etProveIdNumber.setEnabled(false);
                    etProveIdNumber.setFocusable(false);
                    etProveIdNumber.setKeyListener(null);
                } else {
                    etProveTrueName.setText(infoBean.getOrganizationName());
                    etProvePhone.setText(infoBean.getRealName());
                    etProveIdNumber.setText(infoBean.getContactCall());
                }
                tvProveArea.setText(infoBean.getLocation());
                etProveIndustry.setText(infoBean.getTradeField());
                etProveMediaAccount.setText(infoBean.getOwnerAppId());
                etProveResources.setText(infoBean.getResourceDesc());
                tvProveResourcesNum.setText(infoBean.getResourceDesc().length() + "");
            }
        }
        setTitleRightListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toSubmit();
            }
        });
        etProveResources.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)) {
                    tvProveResourcesNum.setText(s.toString().length() + "");
                } else {
                    tvProveResourcesNum.setText("0");
                }
            }
        });
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

    @OnClick(R.id.rl_prove_area)
    public void onClick() {
        Intent locationIntent = new Intent(this, PersonAreaAty.class);
        locationIntent.putExtra(IntentKey.EXTRA_BOOLEAN, false);
        startActivityForResult(locationIntent, REQUEST_AREA);
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
        if (TextUtils.isEmpty(idNumber) || (isOneSelf && idNumber.length() != 16 && idNumber.length() != 18)) {
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
        if (infoBean == null)
            infoBean = new ProveStatusBean();
        infoBean.setAuthType(isOneSelf ? 0 : 1);
        infoBean.setContactCall(isOneSelf ? phone : idNumber);
        infoBean.setIdCard(idNumber);
        infoBean.setLocation(areaInfo);
        infoBean.setOrganizationName(trueName);
        infoBean.setOwnerAppId(mediaAccount);
        infoBean.setRealName(trueName);
        infoBean.setResourceDesc(resource);
        infoBean.setTradeField(industry);
        if (isOneSelf) {
            if (!NetUtils.isNetworkConnected(mContext)) {
                onLoadingStatus(CommonCode.General.UN_NETWORK, mContext.getString(R.string.no_net_work));
                return;
            }
            onLoadingStatus(CommonCode.General.DATA_LOADING);
            //提交
            if (isChange)
                proveInfoPresenter.changeProveInfo(isOneSelf, infoBean);
            else proveInfoPresenter.submitProveInfo(isOneSelf, infoBean);
        } else {
            //下一步
            Intent intent = new Intent(mContext, ProvePaperworkActivity.class);
            intent.putExtra(IntentKey.EXTRA_SERIALIZABLE, infoBean);
            intent.putExtra(IntentKey.EXTRA_BOOLEAN, isChange);
            startActivityForResult(intent, REQUEST_PAPERWORK);
        }
    }

    @Override
    public <T> void updateViewWithFlag(T t, int flag) {
        super.updateViewWithFlag(t, flag);
        if (flag == proveInfoPresenter.FLAG_PROVE_INFO_SUCCESS) {
            EventBus.getDefault().post(new BaseEvent(CommonCode.EventType.PROVE_UPDATE));
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_PAPERWORK) {
            EventBus.getDefault().post(new BaseEvent(CommonCode.EventType.PROVE_UPDATE));
            finish();
        } else if (requestCode == REQUEST_AREA) {
            if (data == null) return;
            String area = data.getStringExtra(IntentKey.EXTRA_POSITION);
            String areaId = data.getStringExtra(IntentKey.EXTRA_ID);
            tvProveArea.setText(area);
        }
    }

    @Override
    public void refreshPage() {

    }
}
