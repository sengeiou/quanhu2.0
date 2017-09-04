package com.rz.circled.ui.fragment;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;

import com.rz.circled.R;
import com.rz.circled.adapter.SearchCircleAdapter;
import com.rz.common.ui.fragment.BaseFragment;

import butterknife.BindView;

/**
 * Created by Gsm on 2017/9/2.
 */
public class SearchCircleFragment extends BaseFragment {

    @BindView(R.id.gv_search_circle)
    GridView gvCircle;
    private SearchCircleAdapter circleAdapter;

    public static SearchCircleFragment newInstance() {
        SearchCircleFragment frg = new SearchCircleFragment();
        return frg;
    }

    @Override
    protected boolean needLoadingView() {
        return true;
    }


    @Nullable
    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_seach_circle, null);
    }

    @Override
    public void initView() {
        circleAdapter = new SearchCircleAdapter(getActivity(), R.layout.item_choose_circle);
        gvCircle.setAdapter(circleAdapter);
    }

    @Override
    public void initData() {

    }
}
