package com.rz.circled.ui.fragment;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.rz.circled.R;
import com.rz.circled.adapter.SearchPersonAdapter;
import com.rz.common.ui.fragment.BaseFragment;

import butterknife.BindView;

/**
 * Created by Gsm on 2017/9/2.
 */
public class SearchPersonFragment extends BaseFragment {

    @BindView(R.id.lv_search_content)
    ListView lvPerson;
    private SearchPersonAdapter personAdapter;

    @Nullable
    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_search_content, null);
    }

    @Override
    public void initView() {
        int basePadding = (int) getResources().getDimension(R.dimen.app_base_padding);
        lvPerson.setPadding(basePadding, 0, basePadding, 0);

        personAdapter = new SearchPersonAdapter(getActivity(), R.layout.item_search_person);

        lvPerson.setAdapter(personAdapter);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initPresenter() {
        super.initPresenter();
    }
}
