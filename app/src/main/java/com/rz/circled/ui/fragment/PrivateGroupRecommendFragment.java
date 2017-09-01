package com.rz.circled.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.rz.circled.R;
import com.rz.circled.adapter.DefaultPrivateGroupAdapter;
import com.rz.circled.adapter.PrivateGroupEssenceAdapter;
import com.rz.common.constant.CommonCode;
import com.rz.common.ui.fragment.BaseFragment;
import com.rz.common.widget.svp.SVProgressHUD;
import com.rz.httpapi.api.ApiPGService;
import com.rz.httpapi.api.BaseCallback;
import com.rz.httpapi.api.Http;
import com.rz.httpapi.api.ResponseData.ResponseData;
import com.rz.httpapi.bean.PrivateGroupBean;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by rzw2 on 2017/8/31.
 */

public class PrivateGroupRecommendFragment extends BaseFragment {

    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.tv)
    TextView tv;

    private DefaultPrivateGroupAdapter mAdapter;

    public static PrivateGroupRecommendFragment newInstance() {
        PrivateGroupRecommendFragment fragment = new PrivateGroupRecommendFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_recommend_private_group, null);
    }

    @Override
    public void initView() {
        lv.setAdapter(mAdapter = new DefaultPrivateGroupAdapter(getContext(), R.layout.item_private_group_essence, DefaultPrivateGroupAdapter.TYPE_SCAN));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    @Override
    public void initData() {
        Http.getApiService(ApiPGService.class).privateGroupEssence().enqueue(new BaseCallback<ResponseData<List<PrivateGroupBean>>>() {
            @Override
            public void onResponse(Call<ResponseData<List<PrivateGroupBean>>> call, Response<ResponseData<List<PrivateGroupBean>>> response) {
                super.onResponse(call, response);
                if (!response.body().isSuccessful()) {
                    SVProgressHUD.showErrorWithStatus(getContext(), response.body().getMsg());
                } else {
                    List<PrivateGroupBean> data = response.body().getData();
                    if (data != null && data.size() > 0) {
                        if (data.size() > 3) {
                            mAdapter.setData(data.subList(0, 3));
                        } else {
                            mAdapter.setData(data);
                        }
                    } else {
                        onLoadingStatus(CommonCode.General.DATA_EMPTY);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseData<List<PrivateGroupBean>>> call, Throwable t) {
                super.onFailure(call, t);
                SVProgressHUD.showErrorWithStatus(getContext(), getString(R.string.request_failed));
            }
        });
    }

    @OnClick(R.id.tv)
    public void onClick() {
    }
}