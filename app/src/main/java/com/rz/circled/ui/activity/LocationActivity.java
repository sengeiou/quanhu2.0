package com.rz.circled.ui.activity;

import android.Manifest;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.rz.circled.R;
import com.rz.common.permission.EasyPermissions;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.sgt.jsbridge.JsEvent;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/3/8 0008.
 */

public class LocationActivity extends BaseActivity {

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    @Nullable
    @Override
    public View loadView(LayoutInflater inflater) {
        return null;
    }

    @Override
    public void initView() {
    }

    @Override
    protected boolean needShowTitle() {
        return false;
    }

    @Override
    public void initData() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (EasyPermissions.hasPermissions(this, perms)) {
            initLocation();
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.location_permission), RC_LOCATION_CONTACTS_PERM, perms);
        }
    }

    private void initLocation() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.setOnceLocation(true);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    /**
     * 销毁定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void destroyLocation() {
        if (null != mLocationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            mLocationClient.onDestroy();
            mLocationClient = null;
            mLocationOption = null;
        }
        JsEvent.callJsEvent(null, false);
    }

    //声明定位回调监听器
    private AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("longitude", amapLocation.getLongitude());
                    hashMap.put("latitude", amapLocation.getLatitude());
                    hashMap.put("province", amapLocation.getProvince());
                    hashMap.put("city", amapLocation.getCity());
                    hashMap.put("region", amapLocation.getDistrict());
                    String adCode = amapLocation.getAdCode();
                    if (adCode.length() == 6) {
                        adCode = adCode.substring(0, 4) + "00";
                    }
                    hashMap.put("cityCode", adCode);
                    JsEvent.callJsEvent(hashMap, true);
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
            finish();
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        super.onPermissionsGranted(requestCode, perms);
        initLocation();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        finish();
//            new AppSettingsDialog.Builder(this, "悠然一指要使用定位权限，否则app可能无法正常运行")
//                    .setPositiveButton(getString(R.string.setting))
//                    .setNegativeButton(getString(R.string.cancel), null /* click listener */)
//                    .setRequestCode(RC_LOCATION_CONTACTS_PERM)
//                    .build()
//                    .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyLocation();
    }
}
