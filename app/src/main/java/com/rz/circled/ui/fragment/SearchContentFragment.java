package com.rz.circled.ui.fragment;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.rz.circled.R;
import com.rz.common.ui.fragment.BaseFragment;

import butterknife.BindView;

import static com.rz.circled.R.layout.fragment_search_content;

/**
 * Created by Gsm on 2017/9/2.
 */
public class SearchContentFragment extends BaseFragment {

    @BindView(R.id.lv_search_content)
    ListView lvContent;

    public static SearchContentFragment newInstance() {
        SearchContentFragment frg = new SearchContentFragment();
        return frg;
    }

    @Nullable
    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(fragment_search_content, null);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }


    @Override
    public void initPresenter() {
        super.initPresenter();
        //搜索接口
    }
}
