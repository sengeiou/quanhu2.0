package com.yryz.yunxinim.location.helper;


import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.yryz.yunxinim.uikit.common.util.log.LogUtil;
import com.yryz.yunxinim.common.infra.TaskExecutor;
import com.yryz.yunxinim.location.model.NimLocation;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class NimLocationManager implements AMapLocationListener {
    private static final String TAG = "NimLocationManager";
    private Context mContext;

    /**
     * msg handler
     */
    private static final int MSG_LOCATION_WITH_ADDRESS_OK = 1;
    private static final int MSG_LOCATION_POINT_OK = 2;
    private static final int MSG_LOCATION_ERROR = 3;

    private NimLocationListener mListener;

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    /**
     * google api
     */
//    private LocationManager mSysLocationMgr = null;
    private String mProvider;
    private Geocoder mGeocoder;

    private MsgHandler mMsgHandler = new MsgHandler();
    private TaskExecutor executor = new TaskExecutor(TAG, TaskExecutor.defaultConfig, true);

    public NimLocationManager(Context context, NimLocationListener oneShotListener) {
        mContext = context;
        mGeocoder = new Geocoder(mContext, Locale.getDefault());
        mListener = oneShotListener;
    }

    public static boolean isLocationEnable(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Criteria cri = new Criteria();
        cri.setAccuracy(Criteria.ACCURACY_COARSE);
        cri.setAltitudeRequired(false);
        cri.setBearingRequired(false);
        cri.setCostAllowed(false);
        String bestProvider = locationManager.getBestProvider(cri, true);
        return !TextUtils.isEmpty(bestProvider);

    }

    @Override
    public void onLocationChanged(final AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    getAMapLocationAddress(aMapLocation);
                }
            });
        } else {
            LogUtil.i(TAG, "receive system location failed");
            // 真的拿不到了
            onLocation(null, MSG_LOCATION_ERROR);
        }
    }

    public interface NimLocationListener {
        public void onLocationChanged(NimLocation location);
    }

    public Location getLastKnownLocation() {
        try {
            return mLocationClient.getLastKnownLocation();
        } catch (Exception e) {
            LogUtil.i(TAG, "get lastknown location failed: " + e.toString());
        }
        return null;
    }

    private void onLocation(NimLocation location, int what) {
        Message msg = mMsgHandler.obtainMessage();
        msg.what = what;
        msg.obj = location;
        mMsgHandler.sendMessage(msg);
    }

    private class MsgHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOCATION_WITH_ADDRESS_OK:
                    if (mListener != null && msg.obj != null) {
                        if (msg.obj != null) {
                            NimLocation loc = (NimLocation) msg.obj;
                            loc.setStatus(NimLocation.Status.HAS_LOCATION_ADDRESS);

                            // 记录地址信息
                            loc.setFromLocation(true);

                            mListener.onLocationChanged(loc);
                        } else {
                            NimLocation loc = new NimLocation();
                            mListener.onLocationChanged(loc);
                        }
                    }
                    break;
                case MSG_LOCATION_POINT_OK:
                    if (mListener != null) {
                        if (msg.obj != null) {
                            NimLocation loc = (NimLocation) msg.obj;
                            loc.setStatus(NimLocation.Status.HAS_LOCATION);
                            mListener.onLocationChanged(loc);
                        } else {
                            NimLocation loc = new NimLocation();
                            mListener.onLocationChanged(loc);
                        }
                    }
                    break;
                case MSG_LOCATION_ERROR:
                    if (mListener != null) {
                        NimLocation loc = new NimLocation();
                        mListener.onLocationChanged(loc);
                    }
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }

    }

    private void getAMapLocationAddress(final AMapLocation loc) {
        if (TextUtils.isEmpty(loc.getAddress())) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    getLocationAddress(new NimLocation(loc, NimLocation.AMap_Location));
                }
            });
        } else {
            NimLocation location = new NimLocation(loc, NimLocation.AMap_Location);
            location.setAddrStr(loc.getAddress());
            location.setProvinceName(loc.getProvince());
            location.setCityName(loc.getCity());
            location.setCityCode(loc.getCityCode());
            location.setDistrictName(loc.getDistrict());
            location.setStreetName(loc.getStreet());
            location.setStreetCode(loc.getAdCode());

            onLocation(location, MSG_LOCATION_WITH_ADDRESS_OK);
        }
    }

    private boolean getLocationAddress(NimLocation location) {
        List<Address> list;
        boolean ret = false;
        try {
            list = mGeocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 2);
            if (list != null && list.size() > 0) {
                Address address = list.get(0);
                if (address != null) {
                    location.setCountryName(address.getCountryName());
                    location.setCountryCode(address.getCountryCode());
                    location.setProvinceName(address.getAdminArea());
                    location.setCityName(address.getLocality());
                    location.setDistrictName(address.getSubLocality());
                    location.setStreetName(address.getThoroughfare());
                    location.setFeatureName(address.getFeatureName());
                }
                ret = true;
            }
        } catch (IOException e) {
            LogUtil.e(TAG, e + "");
        }

        int what = ret ? MSG_LOCATION_WITH_ADDRESS_OK : MSG_LOCATION_POINT_OK;
        onLocation(location, what);

        return ret;
    }

    public void deactive() {
        stopAMapLocation();
    }

    private void stopAMapLocation() {
        if (null != mLocationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            mLocationClient.onDestroy();
            mLocationClient = null;
            mLocationOption = null;
        }
    }

    public void activate() {
        requestAmapLocation();
    }

    private void requestAmapLocation() {
        //初始化定位
        mLocationClient = new AMapLocationClient(mContext);
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
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
}
