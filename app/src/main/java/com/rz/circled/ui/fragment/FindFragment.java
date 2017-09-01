package com.rz.circled.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rz.circled.R;
import com.rz.circled.presenter.impl.V3CirclePresenter;
import com.rz.common.ui.fragment.BaseFragment;
import com.rz.httpapi.bean.CircleEntrModle;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gsm on 2017/8/29.
 */
public class FindFragment extends BaseFragment {

    @BindView(R.id.find_gv)
    GridView mFindGv;
    private List<CircleEntrModle> circleEntrModleList = new ArrayList();
    private V3CirclePresenter mPresenter;

    @Nullable
    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_find, null);
    }

    @Override
    public void initPresenter() {
        mPresenter = new V3CirclePresenter();
        mPresenter.attachView(this);
        mPresenter.getCircleEntranceList(0);
    }

    BaseAdapter findAdapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return circleEntrModleList.size() > 12 ? 12 : circleEntrModleList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CircleEntrModle circleEntrModle = circleEntrModleList.get(position);
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = View.inflate(mActivity, R.layout.item_recyclev_homev3, null);
                viewHolder.icv_circle_img = (ImageView) convertView.findViewById(R.id.id_circle_civ);
                viewHolder.tv_circle_name = (TextView) convertView.findViewById(R.id.id_circle_name);
                viewHolder.tv_circle_peoplenums = (TextView) convertView.findViewById(R.id.id_circle_peoplenums);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tv_circle_name.setText(circleEntrModle.circleName);
            Glide.with(FindFragment.this).load(circleEntrModle.circleIcon).into(viewHolder.icv_circle_img);
            return convertView;
        }

        class ViewHolder {
            ImageView icv_circle_img;
            TextView tv_circle_name;
            TextView tv_circle_peoplenums;
        }
    };

    public void initView() {
        mFindGv.setAdapter(findAdapter);
        mFindGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (circleEntrModleList.get(position).circleName.equals("百圈纷呈")){

                }else {


                }
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public <T> void updateViewWithFlag(T t, int flag) {
        super.updateViewWithFlag(t, flag);
        if (flag == 0) {
            circleEntrModleList.addAll((List<CircleEntrModle>) t);
            CircleEntrModle c = new CircleEntrModle();
            c.circleName = getString(R.string.baiquanfencheng);
            if (circleEntrModleList.size() >= 12) {
                circleEntrModleList.add(11, c);
            } else {
                circleEntrModleList.add(c);
            }
            findAdapter.notifyDataSetChanged();

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
