package com.rz.circled.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.rz.circled.R;
import com.rz.common.adapter.CommonAdapter;
import com.rz.common.adapter.ViewHolder;
import com.rz.common.permission.EasyPermissions;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.widget.MyListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 跳转打开第三方地图app
 */
public class NavigateOpenActivity extends BaseActivity {
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    @BindView(R.id.ll_navigate_map)
    LinearLayout llMap;
    @BindView(R.id.id_popup_listview)
    MyListView mListView;

    private double endLatitude;
    private double endlongitude;
    private double startLatitude;
    private double startlongitude;
    private String startAddress;
    private String endName;


    private static final String EXTRA_LATITUDE = "extraLatitude";
    private static final String EXTRA_LONGITUDE = "extraLongitude";
    private static final String EXTRA_NAME = "extraName";

    /**
     * 百度地图
     */
    private final String BAIDU_MAP = "com.baidu.BaiduMap";

    /**
     * 高德地图
     */
    private final String GAODE_MAP = "com.autonavi.minimap";

    /**
     * 下载app根路径
     */
    private final String loadAppHost = "http://a.app.qq.com/o/simple.jsp?pkgname=";
    private final String[] hintItems = new String[]{"高德地图", "百度地图", "下载高德地图", "下载百度地图"};
    private ArrayList<String> mapList;

    /**
     * 启动h5界面
     */
    public static void startNavigateOpen(Activity activity, double latitude, double longitude, String name) {
        Intent intent = new Intent(activity, NavigateOpenActivity.class);
        intent.putExtra(EXTRA_LATITUDE, latitude);
        intent.putExtra(EXTRA_LONGITUDE, longitude);
        intent.putExtra(EXTRA_NAME, name);
        activity.startActivity(intent);
    }

    @Nullable
    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_navigate_open, null, false);
    }

    @Override
    protected boolean needShowTitle() {
        return false;
    }

    @Override
    public void initView() {
        checkMapApp();
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            endLatitude = intent.getDoubleExtra(EXTRA_LATITUDE, -1);
            endlongitude = intent.getDoubleExtra(EXTRA_LONGITUDE, -1);
            endName = intent.getStringExtra(EXTRA_NAME);
        }

        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (EasyPermissions.hasPermissions(this, perms)) {
            initLocation();
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.location_permission), RC_LOCATION_CONTACTS_PERM, perms);
        }

    }

    //----------------------------------------------------定位-------------------------------//

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


    //声明定位回调监听器
    private AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    startLatitude = amapLocation.getLatitude();
                    startlongitude = amapLocation.getLongitude();
                    startAddress = amapLocation.getAddress();
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyLocation();
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
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        checkMapApp();
    }

    //---------------------------------跳转------------------------//

    private void checkMapApp() {
        mapList = new ArrayList<>();
        if (haveApp(GAODE_MAP)) {
            mapList.add(hintItems[0]);
        }
        if (haveApp(BAIDU_MAP)) {
            mapList.add(hintItems[1]);
        }
        if (mapList.isEmpty()) {
            mapList.add(hintItems[2]);
            mapList.add(hintItems[3]);
        }
        if (!mapList.isEmpty()) {
            showPop();
        }
    }


    /**
     * 显示弹窗
     */
    private void showPop() {
        CommonAdapter mAdapter = new CommonAdapter<String>(this, R.layout.popup_textview) {

            @Override
            public void convert(ViewHolder helper, String item, int position) {
                TextView mTxt = (TextView) helper.getViewById(R.id.id_popup_txt);
                mTxt.setText(item);
            }
        };
        mListView.setAdapter(mAdapter);
        mAdapter.setData(mapList);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openMapApp(position);
            }
        });
        llMap.setVisibility(View.VISIBLE);
    }

    /**
     * 打开第三方地图进程导航
     */
    private void openMapApp(int position) {
        if (position >= mapList.size())
            return;
        String item = mapList.get(position);
        for (int i = 0; i < hintItems.length; i++) {
            if (item.equalsIgnoreCase(hintItems[i])) {
                openApp(i);
                return;
            }
        }
    }

    /**
     * 打开app
     *
     * @param index
     */
    private void openApp(int index) {
        switch (index) {
            case 0://高德
                openGaoDeMap();
                break;
            case 1://百度
                openBaiDuMap();
                break;
            case 2://下载高德
                downloadApp(GAODE_MAP);
                break;
            case 3://下载百度
                downloadApp(BAIDU_MAP);
                break;
        }
        finish();
    }

    /**
     * 打开高德地图
     */
    private void openGaoDeMap() {
        Intent gaodeIntent = new Intent();
        gaodeIntent.setAction(Intent.ACTION_VIEW);
        gaodeIntent.addCategory(Intent.CATEGORY_DEFAULT);
        /**
         * sid 起点的POIID
         * slat起点纬度。如果不填写此参数则自动将用户当前位置设为起点纬度。
         * slon起点经度。 如果不填写此参数则自动将用户当前位置设为起点经度。
         * sname起点名称
         * did终点的POIID
         * dlat 终点纬度
         * dlon 终点经度
         * dname 终点名称
         */
        gaodeIntent.setData(Uri.parse("amapuri://route/plan/?sid=BGVIS1&did=BGVIS2&dlat=" + endLatitude + "&dlon=" + endlongitude + "&dname=" + endName + "&dev=0&t=0"));
//        gaodeIntent.setData(Uri.parse("amapuri://route/plan/?dlat=" + endLatitude + "&dlon=" + endlongitude + "&dname=" + endName + "&dev=0&t=0"));
        startActivity(gaodeIntent);
    }

    /**
     * 打开百度地图
     */
    private void openBaiDuMap() {
        /**
         * mode	导航模式：transit（公交）、driving（驾车）、walking（步行）和riding（骑行）.默认:driving
         * http://lbsyun.baidu.com/index.php?title=uri/api/android
         * coord_type 通用参数
         * 坐标类型，可选参数，默认为bd09经纬度坐标。允许的值为bd09ll、bd09mc、gcj02、wgs84。bd09ll表示百度经纬度坐标，bd09mc表示百度墨卡托坐标，gcj02表示经过国测局加密的坐标，wgs84表示gps获取的坐标
         * origin 起点
         * destination 终点
         */
        Intent baiduIntent = new Intent();
        // 驾车
        baiduIntent.setData(Uri.parse("baidumap://map/direction?&origin=name:" + startAddress + "|latlng:" + startLatitude + "," + startlongitude +
                "&destination=name:" + endName + "|latlng:" + endLatitude + "," + endlongitude + "&mode=driving&coord_type=gcj02"));
        startActivity(baiduIntent);
    }

    private void downloadApp(String appPackageName) {
        Uri uri = Uri.parse(loadAppHost + appPackageName);
        Intent it = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(it);
        finish();
    }

    private boolean haveApp(String packageName) {
        if (TextUtils.isEmpty(packageName))
            return false;
        try {
            getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    @OnClick({R.id.id_popup_cancel_btn, R.id.rl_navigate_root})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_popup_cancel_btn:
            case R.id.rl_navigate_root:
                finish();
                break;
        }
    }
}
