package com.rz.circled.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


import com.rz.rz_rrz.R;
import com.rz.rz_rrz.cache.preference.Session;
import com.rz.rz_rrz.constant.IntentKey;
import com.rz.rz_rrz.model.AreaModel;
import com.rz.rz_rrz.presenter.impl.PersonInfoPresenter;
import com.rz.rz_rrz.view.base.BaseActivity;
import com.rz.rz_rrz.view.base.BaseCommonAty;
import com.rz.rz_rrz.widget.CommonAdapter;
import com.rz.rz_rrz.widget.ViewHolder;
import com.sdicons.json.validator.impl.predicates.Str;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by xiayumo on 16/8/17
 */
public class PersonSecondAreaAty extends BaseCommonAty implements AdapterView.OnItemClickListener {

    @BindView(R.id.id_city_list)
    ListView idCityListView;

    CommonAdapter<AreaModel.CityModel> mAdapter;

    AreaModel model;

    public static final int RESULT_CODE = 101;
    private String keyType;

    @Override
    public View loadView(LayoutInflater inflater, View childView) {
        return super.loadView(inflater, inflater.inflate(R.layout.aty_my_second_area, null));
    }

    @Override
    public void initView() {
        setTitleText(getString(R.string.mine_person_city), null);
    }

    @Override
    public void initData() {

        model = (AreaModel) getIntent().getExtras().getSerializable("areaModel");

        keyType = getIntent().getExtras().getString(IntentKey.KEY_TYPE);

        ArrayList<AreaModel.CityModel> cityList = model.cities;

        View headView = LayoutInflater.from(this).inflate(R.layout.layout_area_head1, null);

        idCityListView.addHeaderView(headView);


        mAdapter = new CommonAdapter<AreaModel.CityModel>(this, cityList, R.layout.layout_area_item) {

            @Override
            public void convert(ViewHolder helper, AreaModel.CityModel item) {
                if (TextUtils.isEmpty(keyType))
                    ((TextView) helper.getView(R.id.id_area_check)).setText(item.isChecked ? getString(R.string.had_check) : "");

                ((TextView) helper.getView(R.id.id_area_text)).setText(item.name);

            }
        };
        idCityListView.setAdapter(mAdapter);
        idCityListView.setOnItemClickListener(this);

    }

    @Override
    public void initPresenter() {
        super.initPresenter();
        presenter = new PersonInfoPresenter();
    }

    @Override
    public <T> void updateView(T t) {
        super.updateView(t);
        //通用页面过来
        if (!TextUtils.isEmpty(keyType) && EditorTwoActivity.TYPE_EDITOR.equals(keyType)) {
            String area = t.toString();
            Intent mIntent = new Intent();
            mIntent.putExtra(IntentKey.General.KEY_POSITION, area);
            setResult(RESULT_CODE, mIntent);
        } else {
            Session.setUser_area(t.toString());
            setResult(RESULT_CODE);
        }
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position != 0) {
            String paramas = model.name + " " + model.cities.get(position - 1).name;
            paramas = paramas.trim();
            if (!TextUtils.isEmpty(keyType) && EditorTwoActivity.TYPE_EDITOR.equals(keyType)) {
                //通用发布过来
                Intent mIntent = new Intent();
                mIntent.putExtra(IntentKey.General.KEY_POSITION, paramas);
                setResult(RESULT_CODE, mIntent);
                finish();
            } else {
                ((PersonInfoPresenter) presenter).savePersonInfo(Session.getUserId(), "location", paramas);
            }
        }
    }
}
