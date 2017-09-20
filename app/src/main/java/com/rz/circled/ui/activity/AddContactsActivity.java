package com.rz.circled.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.rz.circled.R;
import com.rz.circled.constants.AgreementConstants;
import com.rz.circled.modle.ShareModel;
import com.rz.common.constant.H5Address;
import com.rz.common.constant.IntentCode;
import com.rz.common.permission.AfterPermissionGranted;
import com.rz.common.permission.AppSettingsDialog;
import com.rz.common.permission.EasyPermissions;
import com.rz.common.ui.activity.BaseActivity;
import com.zbar.lib.activity.CaptureActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.rz.circled.widget.CommomUtils.trackUser;


/**
 * Created by xiayumo on 16/8/20.
 */
public class AddContactsActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.id_search_key)
    TextView mSearchTexdt;

    public static String URL = "https://wap.yryz.com/inviteRegister.html?inviter=";

    @Nullable
    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_add_contacts, null);
    }

    @Override
    public void initView() {
        setTitleText(getString(R.string.add_friends));
        findViewById(R.id.id_search_key_rela).setOnClickListener(this);
        mSearchTexdt.setText(R.string.search_quanhu_person);
    }

    @Override
    public void initPresenter() {
//        presenter = new FriendPresenter();
    }

    @Override
    public void initData() {
        /**
         * 后台缓存我关注的数据
         */
//        ((FriendPresenter) presenter).requestOtherFriendtoCache(Type.custType_1);
    }

    @OnClick({R.id.id_scan_layout, R.id.id_contacts_layout, R.id.id_addfriend_layout})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_search_key_rela:
                SearchActivity.stratActivity(mContext, SearchActivity.TYPE_PERSON);
                break;
            case R.id.id_scan_layout:
                trackUser("我的", "添加好友", "扫一扫");
                String[] perms = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
                if (EasyPermissions.hasPermissions(this, perms)) {
                    Intent intent = new Intent();
                    intent.setClass(aty, CaptureActivity.class);
                    startActivityForResult(intent, 0);
                } else {
                    EasyPermissions.requestPermissions(this, getString(R.string.carme_sd_permission), RC_VIDEO_AND_EXTENER, perms);
                }
                break;
            case R.id.id_contacts_layout:
                trackUser("我的", "添加好友", "手机联系人");
                showActivity(aty, MobileContactsActivity.class);
                break;
            case R.id.id_addfriend_layout:
                trackUser("我的", "添加好友", "一键邀请好友");
                ShareNewsAty.startShareNews(aty, new ShareModel(
                                getString(R.string.app_name),
                                getString(R.string.app_name),
                                AgreementConstants.SHARE_APP_AGREEMENT),
                        IntentCode.PAGE_ADDFRIEND);
                break;
            default:
                break;
        }
    }

    @AfterPermissionGranted(RC_VIDEO_AND_EXTENER)
    private void toScan() {
        Intent intent = new Intent();
        intent.setClass(aty, CaptureActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String scanResult = data.getExtras().getString("result");
            try {
                JSONObject object = new JSONObject(scanResult);
                Bundle bundle = new Bundle();
                if (object.has("uid")) {
//                    FriendInfoAty.newFrindInfo(aty, object.getString("uid"));
                } else {
                    Toast.makeText(aty, scanResult, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                Toast.makeText(aty, scanResult, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        super.onPermissionsGranted(requestCode, perms);
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this, getString(R.string.carme_sd_permission))
                    .setPositiveButton(getString(R.string.setting))
                    .setNegativeButton(getString(R.string.cancel), null /* click listener */)
                    .setRequestCode(RC_VIDEO_AND_EXTENER)
                    .build()
                    .show();
        }
    }
}
