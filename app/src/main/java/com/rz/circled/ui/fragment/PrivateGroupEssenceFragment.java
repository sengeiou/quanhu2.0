package com.rz.circled.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.rz.circled.BuildConfig;
import com.rz.circled.R;
import com.rz.circled.adapter.PrivateGroupEssenceAdapter;
import com.rz.circled.ui.activity.WebContainerActivity;
import com.rz.circled.widget.CommomUtils;
import com.rz.common.event.BaseEvent;
import com.rz.common.ui.fragment.BaseFragment;
import com.rz.common.utils.Utility;
import com.rz.common.widget.svp.SVProgressHUD;
import com.rz.httpapi.api.ApiPGService;
import com.rz.httpapi.api.BaseCallback;
import com.rz.httpapi.api.Http;
import com.rz.httpapi.api.ResponseData.ResponseData;
import com.rz.httpapi.bean.PrivateGroupBean;
import com.rz.httpapi.bean.PrivateGroupResourceBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

import static com.rz.circled.event.EventConstant.PRIVATE_GROUP_ESSENCE_MORE;
import static com.rz.circled.event.EventConstant.PRIVATE_GROUP_TAB_REFRESH;
import static com.rz.common.constant.CommonCode.Constant.PAGE_SIZE;

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
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        lv.setAdapter(mAdapter = new PrivateGroupEssenceAdapter(getContext(), R.layout.item_private_group_essence));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PrivateGroupResourceBean item = mAdapter.getItem(position);
                WebContainerActivity.startActivity(mActivity, BuildConfig.WebHomeBaseUrl + CommomUtils.getPrivateGroupResourceUrl(item.getCircleRoute(), item.getCoterieId(), item.getModuleEnum(), item.getResourceId()));
            }
        });
    }

    @Override
    public void initData() {
        loadData(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.tv)
    public void onClick() {
    }

    @Subscribe
    public void eventBus(BaseEvent event) {
        switch (event.getType()) {
            case PRIVATE_GROUP_ESSENCE_MORE:
                loadData(true);
                break;
            case PRIVATE_GROUP_TAB_REFRESH:
                loadData(false);
                break;
        }
    }

    private void loadData(final boolean loadMore) {
        Http.getApiService(ApiPGService.class).privateGroupEssence(loadMore ? mAdapter.getCount() : 0, PAGE_SIZE).enqueue(new BaseCallback<ResponseData<List<PrivateGroupResourceBean>>>() {
            @Override
            public void onResponse(Call<ResponseData<List<PrivateGroupResourceBean>>> call, Response<ResponseData<List<PrivateGroupResourceBean>>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    if (!response.body().isSuccessful()) {
                        SVProgressHUD.showErrorWithStatus(getContext(), response.body().getMsg());
                    } else {
                        List<PrivateGroupResourceBean> data = response.body().getData();
                        if (data != null && data.size() > 0) {
                            if (loadMore) {
                                mAdapter.addData(data);
                            } else {
                                mAdapter.setData(data);
                            }
                            tv.setVisibility(View.GONE);
                            Utility.setListViewHeightBasedOnChildren(lv);
                        } else {
                            tv.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    SVProgressHUD.showErrorWithStatus(getContext(), getString(R.string.request_failed));
                }
            }
        });
    }
}
