package com.rz.circled.ui.activity;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;


import com.rz.circled.R;
import com.rz.circled.presenter.IPresenter;
import com.rz.circled.presenter.impl.PersonInfoPresenter;
import com.rz.common.cache.preference.Session;
import com.rz.common.ui.activity.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by xiayumo on 16/8/16.
 */
public class PersonSexActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.id_female_img)
    ImageView idFemaleImg;
    @BindView(R.id.id_male_img)
    ImageView idMaleImg;

    protected IPresenter presenter;
    final int RESULT_CODE1 = 102;//退回到个人信息

    //    int sex = 0;//0是男，1是女
    int sex = 0;//0是女，1是男

    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.aty_my_sex, null);
    }

    @OnClick({R.id.id_layout_person_female, R.id.id_layout_person_male, R.id.tv_base_title_right})
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.id_layout_person_male:

                idFemaleImg.setImageResource(R.drawable.ic_pay_unchecked);
                idMaleImg.setImageResource(R.drawable.ic_pay_checked);
//                sex = 0;
                sex = 1;
                break;

            case R.id.id_layout_person_female:
                idFemaleImg.setImageResource(R.drawable.ic_pay_checked);
                idMaleImg.setImageResource(R.drawable.ic_pay_unchecked);
//                sex = 1;
                sex = 0;
                break;

            case R.id.tv_base_title_right:

                ((PersonInfoPresenter) presenter).savePersonInfo(Session.getUserId(), "sex", sex + "");

                break;
            default:
                break;

        }

    }

    @Override
    public void initView() {
        setTitleText(getString(R.string.mine_person_sex_change));
        setTitleRightText(getString(R.string.mine_person_save));
        String sex = Session.getUser_sex();
        if (!TextUtils.isEmpty(sex)) {
            idFemaleImg.setImageResource(sex.equals("女") ? R.drawable.ic_pay_checked : R.drawable.ic_pay_unchecked);
            idMaleImg.setImageResource(sex.equals("男") ? R.drawable.ic_pay_checked : R.drawable.ic_pay_unchecked);
//            this.sex = sex.equals("女") ? 1 : 0;
            this.sex = sex.equals("女") ? 0 : 1;
        }
    }

    @Override
    public void initData() {
    }

    @Override
    public void initPresenter() {
        super.initPresenter();
        presenter = new PersonInfoPresenter();
        presenter.attachView(this);
    }

    @Override
    public <T> void updateView(T t) {
        super.updateView(t);
//        Session.setUser_sex(t.toString().equals("0") ? "男" : "女");
        Session.setUser_sex(t.toString().equals("0") ? "女" : "男");
        setResult(RESULT_CODE1);
        finish();
    }

}
