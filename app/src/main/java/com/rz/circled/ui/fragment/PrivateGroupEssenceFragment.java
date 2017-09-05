package com.rz.circled.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.rz.circled.R;
import com.rz.circled.adapter.DefaultPrivateGroupAdapter;
import com.rz.circled.adapter.PrivateGroupEssenceAdapter;
import com.rz.common.constant.CommonCode;
import com.rz.common.ui.fragment.BaseFragment;
import com.rz.common.utils.Utility;
import com.rz.common.widget.MyListView;
import com.rz.common.widget.svp.SVProgressHUD;
import com.rz.httpapi.api.ApiPGService;
import com.rz.httpapi.api.BaseCallback;
import com.rz.httpapi.api.Http;
import com.rz.httpapi.api.ResponseData.ResponseData;
import com.rz.httpapi.bean.PrivateGroupBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by rzw2 on 2017/8/31.
 */

public class PrivateGroupEssenceFragment extends BaseFragment {

    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.tv)
    TextView tv;

    private PrivateGroupEssenceAdapter mAdapter;

    public static PrivateGroupEssenceFragment newInstance() {
        PrivateGroupEssenceFragment fragment = new PrivateGroupEssenceFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_essence_private_group, null);
    }

    @Override
    public void initView() {
        lv.setAdapter(mAdapter = new PrivateGroupEssenceAdapter(getContext(), R.layout.item_private_group_essence));
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

                if (response.isSuccessful()) {
                    if (!response.body().isSuccessful()) {
                        SVProgressHUD.showErrorWithStatus(getContext(), response.body().getMsg());
                    } else {
                        List<PrivateGroupBean> data = response.body().getData();
                        if (data != null && data.size() > 0) {
                            if (data.size() > 3) {
                                mAdapter.setData(data.subList(0, 3));
                                tv.setText(String.format(getString(R.string.private_group_total), data.size()));
                                tv.setTextColor(getResources().getColor(R.color.color_0185FF));
                            } else {
                                mAdapter.setData(data);
                                tv.setText(R.string.private_group_no_more);
                                tv.setTextColor(getResources().getColor(R.color.font_gray_s));
                            }
                            Utility.setListViewHeightBasedOnChildren(lv);
                        } else {
                            onLoadingStatus(CommonCode.General.DATA_EMPTY);
                        }
                    }
                } else {
                    SVProgressHUD.showErrorWithStatus(getContext(), getString(R.string.request_failed));
                }
            }

            @Override
            public void onFailure(Call<ResponseData<List<PrivateGroupBean>>> call, Throwable t) {
                super.onFailure(call, t);
                SVProgressHUD.showErrorWithStatus(getContext(), getString(R.string.request_failed));
            }
        });

        List<PrivateGroupBean> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(new PrivateGroupBean());
        }
        mAdapter.setData(list);
        Utility.setListViewHeightBasedOnChildren(lv);
    }

    @OnClick(R.id.tv)
    public void onClick() {
    }
}
