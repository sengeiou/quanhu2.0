package com.rz.circled.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.rz.circled.R;
import com.rz.circled.adapter.DefaultPrivateGroupAdapter;
import com.rz.circled.event.EventConstant;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.CommonCode;
import com.rz.common.event.BaseEvent;
import com.rz.common.ui.fragment.BaseFragment;
import com.rz.common.utils.Utility;
import com.rz.common.widget.svp.SVProgressHUD;
import com.rz.httpapi.api.ApiPGService;
import com.rz.httpapi.api.BaseCallback;
import com.rz.httpapi.api.Http;
import com.rz.httpapi.api.ResponseData.ResponseData;
import com.rz.httpapi.bean.PrivateGroupBean;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

import static com.rz.common.constant.IntentKey.EXTRA_TYPE;

/**
 * Created by rzw2 on 2017/8/31.
 */

public class PrivateGroupJoinByMyselfFragment extends BaseFragment {
    public static final int TYPE_PART = 0;
    public static final int TYPE_ALL = 1;

    @BindView(R.id.lv)
    ListView lv;

    private int type;
    private DefaultPrivateGroupAdapter mAdapter;

    public static PrivateGroupJoinByMyselfFragment newInstance(int type) {
        PrivateGroupJoinByMyselfFragment fragment = new PrivateGroupJoinByMyselfFragment();
        Bundle args = new Bundle();
        args.putInt(EXTRA_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments() != null ? getArguments().getInt(EXTRA_TYPE) : 0;
    }

    @Nullable
    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_my_join_private_group, null);
    }

    @Override
    public void initView() {
        if (type == TYPE_PART) {
            lv.setDivider(getResources().getDrawable(R.drawable.shape_private_group_divider));
            lv.setDividerHeight(getResources().getDimensionPixelOffset(R.dimen.px2));
        }
        lv.setAdapter(mAdapter = new DefaultPrivateGroupAdapter(getContext(), R.layout.item_default_private_group, DefaultPrivateGroupAdapter.TYPE_DESC));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    @Override
    public void initData() {
        Http.getApiService(ApiPGService.class).privateGroupMyselfJoin(Session.getUserId()).enqueue(new BaseCallback<ResponseData<List<PrivateGroupBean>>>() {
            @Override
            public void onResponse(Call<ResponseData<List<PrivateGroupBean>>> call, Response<ResponseData<List<PrivateGroupBean>>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    if (!response.body().isSuccessful()) {
                        SVProgressHUD.showErrorWithStatus(getContext(), response.body().getMsg());
                    } else {
                        List<PrivateGroupBean> data = response.body().getData();
                        if (type == TYPE_PART) {
                            if (data != null && data.size() > 0) {
                                if (data.size() > 2) {
                                    mAdapter.setData(data.subList(0, 2));
                                } else {
                                    mAdapter.setData(data);
                                }
                                Utility.setListViewHeightBasedOnChildren(lv);
                            }
                        } else {
                            if (data != null && data.size() > 0) {
                                mAdapter.setData(data);
                            } else {
                                onLoadingStatus(CommonCode.General.DATA_EMPTY);
                            }
                        }
                        EventBus.getDefault().post(new BaseEvent(EventConstant.USER_JOIN_PRIVATE_GROUP_NUM, data.size()));
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
    }
}
