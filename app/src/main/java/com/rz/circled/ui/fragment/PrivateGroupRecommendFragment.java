package com.rz.circled.ui.fragment;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.rz.circled.R;
import com.rz.circled.adapter.DefaultPricePrivateGroupAdapter;
import com.rz.circled.adapter.DefaultPrivateGroupAdapter;
import com.rz.circled.helper.CommonH5JumpHelper;
import com.rz.circled.presenter.impl.PrivateGroupPresenter;
import com.rz.circled.ui.activity.AllPrivateGroupActivity;
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
import com.rz.httpapi.bean.PrivateGroupListBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

import static com.rz.circled.event.EventConstant.PRIVATE_GROUP_TAB_REFRESH;

/**
 * Created by rzw2 on 2017/8/31.
 */

public class PrivateGroupRecommendFragment extends BaseFragment {

    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.tv)
    TextView tv;
    @BindView(R.id.line)
    View line;

    //私圈相关
    private PrivateGroupPresenter mPresenter;
    private DefaultPricePrivateGroupAdapter mAdapter;

    public static PrivateGroupRecommendFragment newInstance() {
        PrivateGroupRecommendFragment fragment = new PrivateGroupRecommendFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_recommend_private_group, null);
    }

    @Override
    public void initPresenter() {
        mPresenter = new PrivateGroupPresenter();
        mPresenter.attachView(this);
    }

    @Override
    public void initView() {
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        lv.setAdapter(mAdapter = new DefaultPricePrivateGroupAdapter(getContext(), R.layout.item_default_private_group, DefaultPrivateGroupAdapter.TYPE_SCAN));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PrivateGroupBean item = mAdapter.getItem(position);
                CommonH5JumpHelper.startGroupHome(mActivity, item.getCircleRoute(), item.getCoterieId());
            }
        });
    }

    @Override
    public void initData() {
        mPresenter.privateGroupRecommend(Session.getUserId());
    }

    @Override
    public <T> void updateView(T t) {
        super.updateView(t);
        if (t instanceof PrivateGroupListBean) {
            if (tv == null) return;
            PrivateGroupListBean _data = (PrivateGroupListBean) t;
            List<PrivateGroupBean> data = _data.getList();
            mAdapter.setData(data.size() > 3 ? data.subList(0, 3) : data);
            tv.setText(String.format(getString(R.string.private_group_total), _data.getCount()));
            tv.setTextColor(getResources().getColor(R.color.color_0185FF));
            Utility.setListViewHeightBasedOnChildren(lv);
            line.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void eventBus(BaseEvent event) {
        switch (event.getType()) {
            case PRIVATE_GROUP_TAB_REFRESH:
                initData();
                break;
        }
    }

    @OnClick(R.id.tv)
    public void onClick() {
        startActivity(new Intent(getContext(), AllPrivateGroupActivity.class));
    }

    @Override
    public void refreshPage() {

    }
}