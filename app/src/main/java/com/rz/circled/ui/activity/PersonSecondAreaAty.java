package com.rz.circled.ui.activity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.rz.circled.R;
import com.rz.circled.modle.AreaModel;
import com.rz.circled.presenter.impl.PersonInfoPresenter;
import com.rz.common.adapter.CommonAdapter;
import com.rz.common.adapter.ViewHolder;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.IntentKey;
import com.rz.common.ui.activity.BaseActivity;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by xiayumo on 16/8/17
 */
public class PersonSecondAreaAty extends BaseActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.id_city_list)
    ListView idCityListView;

    CommonAdapter<AreaModel.CityModel> mAdapter;

    AreaModel model;

    public static final int RESULT_CODE = 101;
    private boolean isUserInfo = true;//是否为从个人资料页面进来
    private PersonInfoPresenter personInfoPresenter;

    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_my_second_area, null);
    }

    @Override
    public void initView() {
        setTitleText(R.string.mine_person_city);
    }

    @Override
    public void initData() {

        model = (AreaModel) getIntent().getExtras().getSerializable("areaModel");

        isUserInfo = getIntent().getBooleanExtra(IntentKey.EXTRA_BOOLEAN, true);

        ArrayList<AreaModel.CityModel> cityList = model.children;

        View headView = LayoutInflater.from(this).inflate(R.layout.layout_area_head1, null);

        idCityListView.addHeaderView(headView);

        mAdapter = new CommonAdapter<AreaModel.CityModel>(this, R.layout.layout_area_item) {
            @Override
            public void convert(ViewHolder helper, AreaModel.CityModel item, int position) {
                if (isUserInfo)
                    ((TextView) helper.getView(R.id.id_area_check)).setText(item.isChecked ? getString(R.string.had_check) : "");

                ((TextView) helper.getView(R.id.id_area_text)).setText(item.name);
            }
        };
        idCityListView.setAdapter(mAdapter);
        idCityListView.setOnItemClickListener(this);
        mAdapter.setData(cityList);
    }

    @Override
    public void initPresenter() {
        super.initPresenter();
        personInfoPresenter = new PersonInfoPresenter();
        personInfoPresenter.attachView(this);
    }

    @Override
    public <T> void updateView(T t) {
        super.updateView(t);
        //其他页面过来
        if (!isUserInfo) {
            String area = t.toString();
            Intent mIntent = new Intent();
            mIntent.putExtra(IntentKey.EXTRA_POSITION, area);
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
            String paramas = model.name + " " + model.children.get(position - 1).name;
            paramas = paramas.trim();
            if (!isUserInfo) {
                //通用发布过来
                Intent mIntent = new Intent();
                mIntent.putExtra(IntentKey.EXTRA_POSITION, paramas);
                mIntent.putExtra(IntentKey.EXTRA_ID, model.children.get(position - 1).code);
                setResult(RESULT_CODE, mIntent);
                finish();
            } else {
                personInfoPresenter.savePersonInfo(Session.getUserId(), "location", paramas, model.children.get(position - 1).code);
            }
        }
    }

    @Override
    public void refreshPage() {

    }
}
