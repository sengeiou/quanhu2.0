package com.rz.circled.ui.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rz.circled.R;
import com.rz.circled.http.ApiYylService;
import com.rz.circled.widget.CommonAdapter;
import com.rz.circled.widget.ViewHolder;
import com.rz.common.cache.preference.Session;
import com.rz.common.ui.fragment.BaseFragment;
import com.rz.httpapi.api.Http;
import com.rz.httpapi.api.ResponseData.ResponseData;
import com.rz.httpapi.bean.ActivityBean;
import com.rz.httpapi.bean.EntitiesBean;
import com.rz.httpapi.constans.ReturnCode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/9/14 0014.
 */

public class MyActivityFragment extends BaseFragment {

    List<EntitiesBean> bean = new ArrayList<>();
    @BindView(R.id.lv)
    ListView mLv;
    @BindView(R.id.iv_no_data)
    ImageView mIvNoData;
    private CommonAdapter<EntitiesBean> mEntitiesBeanCommonAdapter;

    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.mine_activity, null);
    }


    public static MyActivityFragment newInstance() {
        MyActivityFragment frg = new MyActivityFragment();
        return frg;
    }

    @Override
    public void initPresenter() {
        Http.getApiService(ApiYylService.class)
                .getMineActivityList(1, 20, Session.getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ResponseData<ActivityBean>>() {
                    @Override
                    public void call(ResponseData<ActivityBean> res) {
                        if (res.getAct() == ReturnCode.SUCCESS) {
                            List<EntitiesBean> entities = res.getData().entities;
                            bean.addAll(entities);
                            mIvNoData.setVisibility(bean.size() == 0 ? View.VISIBLE : View.GONE);
                            mLv.setVisibility(bean.size() == 0 ? View.GONE : View.VISIBLE);
                            mEntitiesBeanCommonAdapter.notifyDataSetChanged();
                        }
                    }
                });

    }

    @Override
    public void initView() {
        mEntitiesBeanCommonAdapter = new CommonAdapter<EntitiesBean>(mActivity, bean, R.layout.activity_item) {
            @Override
            public void convert(ViewHolder helper, EntitiesBean item) {
                ((TextView) helper.getView(R.id.activity_title)).setText(item.getTitle());
                ImageView iv = (ImageView) helper.getView(R.id.iv_activity_icon);
                Glide.with(mContext).load(item.getCoverPlan()).into(iv);
            }
        };
        mLv.setAdapter(mEntitiesBeanCommonAdapter);
    }

    @Override
    public void initData() {

    }
}
