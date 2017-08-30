package com.rz.circled.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.rz.circled.modle.AreaModel;
import com.rz.common.adapter.CommonAdapter;
import com.rz.common.adapter.ViewHolder;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.IntentKey;
import com.rz.common.permission.AppSettingsDialog;
import com.rz.common.permission.EasyPermissions;
import com.rz.common.ui.activity.BaseActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.xiaomi.push.thrift.a.R;


/**
 * Created by xiayumo on 16/8/16.
 */
public class PersonAreaAty extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    @BindView(R.id.id_prov_list)
    ListView idProvListView;
    TextView mTvLocation;
    CommonAdapter<AreaModel> mAdapter;

    ArrayList<AreaModel> areaList = new ArrayList<>();

    String area;

    final int RESULT_CODE = 101;
    final int RESULT_CODE1 = 102;//退回到个人信息
    final int REQUEST_CODE = 1;

    String paramas = "";//二级页面返回

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    //上一个页面带过来的type
    private String keyType;

    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.aty_my_area, null);
    }

    @Override
    public void initView() {
        setTitleText(getString(R.string.mine_person_area), null);
        setTitleRightText(getString(R.string.mine_person_save), this);

        keyType = getIntent().getStringExtra(IntentKey.KEY_TYPE);

        View headView = LayoutInflater.from(this).inflate(R.layout.layout_area_head, null);
        mTvLocation = (TextView) headView.findViewById(R.id.tv_location);

        idProvListView.addHeaderView(headView);

        // 模拟一个选中城市

        mAdapter = new CommonAdapter<AreaModel>(this, areaList, R.layout.layout_area_item) {

            @Override
            public void convert(ViewHolder helper, AreaModel item, int position) {
                ((TextView) helper.getView(R.id.id_area_text)).setText(item.name);

                if (TextUtils.isEmpty(keyType))
                    ((TextView) helper.getView(R.id.id_area_check)).setText(item.isChecked ? getString(R.string.had_check) : "");
            }
        };

        idProvListView.setAdapter(mAdapter);
        idProvListView.setOnItemClickListener(this);

        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (EasyPermissions.hasPermissions(this, perms)) {
            initLocation();
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.location_permission), RC_LOCATION_CONTACTS_PERM, perms);
        }

    }

    @Override
    public void initPresenter() {
        super.initPresenter();
//        presenter = new PersonInfoPresenter();
    }

    @Override
    public <T> void updateView(T t) {
        super.updateView(t);
        //通用发布页面过来
        if (!TextUtils.isEmpty(keyType) && EditorTwoActivity.TYPE_EDITOR.equals(keyType)) {
            String area = t.toString();
            Intent mIntent = new Intent();
            mIntent.putExtra(IntentKey.KEY_POSITION, area);
            setResult(RESULT_CODE1, mIntent);
        } else {
            Session.setUser_area(t.toString());
            setResult(RESULT_CODE1);
        }
        finish();
    }

    @Override
    public void initData() {

        try {
            InputStreamReader reader = new InputStreamReader(getAssets().open("city.txt"));
            StringBuffer result = new StringBuffer();
            char[] c = new char[1024];
            int length = -1;
            while ((length = reader.read(c)) != -1) {
                result.append(c, 0, length);
            }
            reader.close();

            area = result.toString();

        } catch (Exception e) {

        }

        String area = Session.getUser_area();

        String[] array = area.split(" ");

        /**
         * 初始化
         */
        getAreaList(array);
        mAdapter.notifyDataSetChanged();

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.titlebar_right_text:
//                ((PersonInfoPresenter) presenter).savePersonInfo(Session.getUserId(), "location", paramas);
                break;
            default:
                break;

        }

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position > 0) {
            Intent intent = new Intent(this, PersonSecondAreaAty.class);
            Bundle data = new Bundle();
            data.putSerializable("areaModel", areaList.get(position - 1));
            data.putString(IntentKey.KEY_TYPE, keyType);
            intent.putExtras(data);
            startActivityForResult(intent, REQUEST_CODE);
        } else if (position == 0) {
            paramas = "";
            /**
             * 进入gps定位
             */
            String area = mTvLocation.getText().toString().trim();
            if (!TextUtils.equals(area, getString(R.string.is_location))) {
                if (!TextUtils.isEmpty(keyType) && EditorTwoActivity.TYPE_EDITOR.equals(keyType)) {
                    //通用发布页过来
                    Intent mIntent = new Intent();
                    mIntent.putExtra(IntentKey.General.KEY_POSITION, area);
                    setResult(RESULT_CODE1, mIntent);
                    finish();
                } else {
                    String[] array = area.split(" ");
                    String[] index = new String[array.length - 1];
                    for (int i = 0; i < index.length; i++) {
                        index[i] = array[i + 1].substring(0, array[i + 1].length() - 1);
                        paramas = paramas + " " + index[i];
                    }
                    paramas = paramas.trim();
//                    ((PersonInfoPresenter) presenter).savePersonInfo(Session.getUserId(), "location", paramas);
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_CODE) {
                if (!TextUtils.isEmpty(keyType) && EditorTwoActivity.TYPE_EDITOR.equals(keyType)) {
                    setResult(RESULT_CODE1, data);
                    finish();
                } else {
                    setResult(RESULT_CODE1);
                    finish();
                }

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyLocation();
    }

    void getAreaList(String[] fiters) {

        try {
            areaList.clear();

            JSONObject object = new JSONObject(area);
            JSONArray array = object.getJSONArray("data");
            ArrayList<AreaModel> cacheList = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                JSONArray array1 = obj.getJSONArray("cities");

                AreaModel model = new AreaModel();
                model.name = obj.getString("name");
                model.cities = new ArrayList<>();

                for (int j = 0; j < array1.length(); j++) {
                    AreaModel.CityModel model1 = new AreaModel.CityModel();
                    model1.name = (String) array1.get(j);
                    model.cities.add(model1);
                }
                cacheList.add(model);

            }

            areaList.addAll(cacheList);

        } catch (Exception e) {

        }

        /**
         * 更新被选中数据
         */
        if (fiters.length > 1) {
            for (int ii = 0; ii < areaList.size(); ii++) {

                AreaModel model = areaList.get(ii);
                if (model.name.equals(fiters[0])) model.isChecked = true;

                for (int jj = 0; jj < model.cities.size(); jj++) {
                    if (model.cities.get(jj).name.equals(fiters[1])) {
                        model.cities.get(jj).isChecked = true;
                    }
                }

            }
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
    }

    //声明定位回调监听器
    private AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。
                    Log.e("zxw", amapLocation.getAddress());
                    mTvLocation.setText(amapLocation.getCountry() + " " + amapLocation.getProvince() + " " + amapLocation.getCity());
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
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this, getString(R.string.location_permission_run))
                    .setPositiveButton(getString(R.string.setting))
                    .setNegativeButton(getString(R.string.cancel), null /* click listener */)
                    .setRequestCode(RC_LOCATION_CONTACTS_PERM)
                    .build()
                    .show();
        }
    }
}
