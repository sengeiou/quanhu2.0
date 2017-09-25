package com.rz.circled.ui.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rz.circled.R;
import com.rz.circled.http.ApiYylService;
import com.rz.circled.ui.activity.WebContainerActivity;
import com.rz.circled.widget.CommomUtils;
import com.rz.circled.widget.CommonAdapter;
import com.rz.circled.widget.ViewHolder;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.Constants;
import com.rz.common.ui.fragment.BaseFragment;
import com.rz.common.utils.StringUtils;
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

public class MyActivityFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    List<EntitiesBean> bean = new ArrayList<>();
    @BindView(R.id.lv)
    ListView mLv;
    int pageNo = 1;
    @BindView(R.id.activity_refresh)
    SwipeRefreshLayout mActivityRefresh;
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
                .getMineActivityList(pageNo, 20, Session.getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ResponseData<ActivityBean>>() {
                    @Override
                    public void call(ResponseData<ActivityBean> res) {
                        if (res.getRet() == ReturnCode.SUCCESS) {
                            pageNo++;
                            List<EntitiesBean> entities = res.getData().entities;
                            bean.addAll(entities);
                            mEntitiesBeanCommonAdapter.notifyDataSetChanged();
                        }
                    }
                });

    }

    @Override
    public void initView() {
        mActivityRefresh.setColorSchemeColors(Constants.COLOR_SCHEMES);
        mActivityRefresh.setOnRefreshListener(this);
        mEntitiesBeanCommonAdapter = new CommonAdapter<EntitiesBean>(mActivity, bean, R.layout.activity_item) {
            @Override
            public void convert(ViewHolder helper, EntitiesBean item) {
                ((TextView) helper.getView(R.id.activity_title)).setText(item.getTitle());
                ImageView iv = (ImageView) helper.getView(R.id.iv_activity_icon);
                Glide.with(mContext).load(item.getCoverPlan()).into(iv);
            }
        };
        mLv.setAdapter(mEntitiesBeanCommonAdapter);

        mLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    @Override
    public void initData() {
        initPresenter();
    }

    @Override
    protected boolean hasDataInPage() {
        return mEntitiesBeanCommonAdapter.getCount() != 0;
    }
    @Override

    protected boolean needLoadingView() {
        return true;
    }

    @Override
    public void refreshPage() {
        initPresenter();
    }

    @Override
    public void onRefresh() {
        initPresenter();
        mActivityRefresh.setRefreshing(false);
    }


}
