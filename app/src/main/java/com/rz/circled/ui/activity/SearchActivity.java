package com.rz.circled.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.rz.circled.R;
import com.rz.circled.ui.fragment.SearchCircleFragment;
import com.rz.circled.ui.fragment.SearchContentFragment;
import com.rz.circled.ui.fragment.SearchPersonFragment;
import com.rz.circled.ui.fragment.SearchPrivateCircleFragment;
import com.rz.circled.ui.fragment.SearchRewardFragment;
import com.rz.circled.widget.PagerSlidingTabStripHome;
import com.rz.common.constant.CommonCode;
import com.rz.common.constant.IntentKey;
import com.rz.common.event.BaseEvent;
import com.rz.common.ui.activity.BaseActivity;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

public class SearchActivity extends BaseActivity {

    @BindView(R.id.et_search_keyword)
    EditText etKeyword;
    @BindView(R.id.iv_search_clear_keyword)
    ImageView ivClearKeyword;
    @BindView(R.id.tab_pager_search)
    PagerSlidingTabStripHome tabPagerSearch;
    @BindView(R.id.vp_search)
    ViewPager vpSearch;
    private SearchAdapter searchAdapter;


    public static final int TYPE_CONTENT = 0;
    public static final int TYPE_PERSON = 1;
    public static final int TYPE_PRIVATE = 2;
    public static final int TYPE_CIRCLE = 3;
    public static final int TYPE_REWARD = 4;

    public static final void stratActivity(Context context, int type) {
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra(IntentKey.EXTRA_TYPE, type);
        context.startActivity(intent);
    }

    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_search, null);
    }

    @Override
    public void initView() {
        etKeyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && !TextUtils.isEmpty(s.toString())) {
                    ivClearKeyword.setVisibility(View.VISIBLE);
                } else {
                    ivClearKeyword.setVisibility(View.GONE);
                }
            }
        });
        tabPagerSearch.setCustomLayoutParams(5);
        tabPagerSearch.setLineFitFont(true);
        searchAdapter = new SearchAdapter(getSupportFragmentManager());
        vpSearch.setAdapter(searchAdapter);
        vpSearch.setOffscreenPageLimit(5);
        tabPagerSearch.setViewPager(vpSearch);
        tabPagerSearch.notifyDataSetChanged();
        tabPagerSearch.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void initData() {

    }

    @Override
    protected View getTitleView() {
        return getLayoutInflater().inflate(R.layout.layout_search_title, null);
    }

    @OnClick({R.id.iv_search_title_back, R.id.iv_search_clear_keyword, R.id.tv_search_to_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_search_title_back:
                finish();
                break;
            case R.id.iv_search_clear_keyword:
                etKeyword.setText("");
                break;
            case R.id.tv_search_to_search:
                toSearch();
                break;
        }
    }

    /**
     * 去搜索
     */
    private void toSearch() {
        String keyWord = etKeyword.getText().toString();
        if (TextUtils.isEmpty(keyWord)) return;
        BaseEvent baseEvent = new BaseEvent(CommonCode.EventType.SEARCH_KEYWORD, keyWord);
        EventBus.getDefault().post(baseEvent);
    }


    private class SearchAdapter extends FragmentStatePagerAdapter {

        private final int[] itemName;

        public SearchAdapter(FragmentManager fm) {
            super(fm);
            itemName = new int[]{R.string.tab_content, R.string.tab_person, R.string.tab_private_circle, R.string.tab_circle, R.string.tab_reward};
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getString(itemName[position]);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == TYPE_CONTENT)
                return SearchContentFragment.newInstance();     //搜索内容
            if (position == TYPE_PERSON)
                return SearchPersonFragment.newInstance();      //搜索用户
            if (position == TYPE_PRIVATE)
                return SearchPrivateCircleFragment.newInstance();     //搜索私圈
            if (position == TYPE_CIRCLE)
                return SearchCircleFragment.newInstance();            //搜索圈子
            if (position == TYPE_REWARD)
                return SearchRewardFragment.newInstance();      //搜索悬赏



            return SearchContentFragment.newInstance();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return itemName.length;
        }
    }
}
