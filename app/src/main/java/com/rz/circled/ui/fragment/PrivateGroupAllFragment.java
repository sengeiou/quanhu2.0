package com.rz.circled.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.rz.circled.R;
import com.rz.circled.adapter.DefaultPrivateGroupAdapter;
import com.rz.common.ui.fragment.BaseFragment;
import com.rz.httpapi.api.ApiService;
import com.rz.httpapi.api.BaseCallback;
import com.rz.httpapi.api.Http;
import com.rz.httpapi.api.ResponseData.ResponseData;
import com.rz.httpapi.bean.AnnouncementResponseBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by rzw2 on 2017/8/31.
 */

public class PrivateGroupAllFragment extends BaseFragment {

    @BindView(R.id.lv)
    ListView lv;

    private DefaultPrivateGroupAdapter mAdapter;

    @Nullable
    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_all_private_group, null);
    }

    @Override
    public void initView() {
        lv.setAdapter(mAdapter = new DefaultPrivateGroupAdapter(getContext(), R.layout.item_default_private_group, DefaultPrivateGroupAdapter.TYPE_SCAN));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    @Override
    public void initData() {
        Http.getApiService(ApiService.class).announcementList("").enqueue(new BaseCallback<ResponseData<AnnouncementResponseBean>>() {
            @Override
            public void onResponse(Call<ResponseData<AnnouncementResponseBean>> call, Response<ResponseData<AnnouncementResponseBean>> response) {
                super.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<ResponseData<AnnouncementResponseBean>> call, Throwable t) {
                super.onFailure(call, t);
            }
        });
    }

}
