package com.rz.circled.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.rz.circled.R;
import com.rz.circled.adapter.CircleAdapter;
import com.rz.circled.presenter.impl.CirclePresenter;
import com.rz.circled.widget.SideBar;
import com.rz.circled.widget.pinyin.PinyinComparator;
import com.rz.common.constant.IntentKey;
import com.rz.common.ui.fragment.BaseFragment;
import com.rz.httpapi.bean.CircleEntrModle;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;

import static com.rz.common.constant.Constants.LOVE_CIRCLE;

/**
 * Created by Administrator on 2017/11/8/008.
 */

public class AllCircleFragment extends BaseFragment {
    /**
     * 内容展示
     */
    @BindView(R.id.id_content_listv)
    ListView mListview;

    /**
     * 右侧字母导航栏
     */

    @BindView(R.id.id_sidrbar)
    SideBar mSidebar;
    private CircleAdapter mCircleAdapter;
    private CircleAdapter mRecommCircleAdapter;
    /**
     * 字母提醒框
     */
    /**
     * 头布局
     */
    private View mheadView;
    @BindView(R.id.id_letter_dialog)
    TextView mTxtDialog;
    private PinyinComparator mPyComparator;
    private CirclePresenter mPresenter;
    List<CircleEntrModle> loveAllList;
    List<CircleEntrModle> loveList=new ArrayList<>();
    List<String> loveListaaaaa=new ArrayList<>();
    List<CircleEntrModle> recommendList;
    boolean isEdit = false;
    int type;

    @Nullable
    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_circle_layout, null);
    }

    public static AllCircleFragment newInstance(int type) {
        AllCircleFragment frg = new AllCircleFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(IntentKey.EXTRA_TYPE, type);
        frg.setArguments(bundle);
        return frg;
    }

    @Override
    public void initPresenter() {
        mPresenter = new CirclePresenter();
        mPresenter.attachView(this);
    }

    @Override
    public void initView() {
        loveAllList = (List<CircleEntrModle>) getActivity().getIntent().getSerializableExtra(LOVE_CIRCLE);
        mPyComparator=new PinyinComparator();
        Bundle bundle = getArguments();
        if (bundle != null) {
            type = bundle.getInt(IntentKey.EXTRA_TYPE);
        }
        initHeadView();
        mListview.addHeaderView(mheadView);
        mListview.setDividerHeight(0);
        if (type == 0) {
            mCircleAdapter = new CircleAdapter(mActivity,R.layout.circle_adapter_item);
            mListview.setAdapter(mCircleAdapter);
            delLove();
        } else {
            mRecommCircleAdapter = new CircleAdapter(mActivity, R.layout.circle_adapter_item);
            mListview.setAdapter(mRecommCircleAdapter);
        }

    }
    private void delLove() {
        for (int i = 0; i < loveAllList.size(); i++) {
            if (loveAllList.get(i).type == 1) {
                loveList.add(loveAllList.get(i));
            }
        }
//        Collections.sort(loveList, mPyComparator);
//        Comparator<Object> comparator = Collator.getInstance(java.util.Locale.CHINA);
//
//        Collections.sort(loveList,comparator);
        mCircleAdapter.setData(loveList);
    }
    private void initHeadView() {
        mheadView = LayoutInflater.from(mActivity).inflate(R.layout.all_circle_head_layout, null);
    }

    @Override
    public void initData() {
        mPresenter.getCircleEntranceList(0);
        // 设置右侧触摸监听
        mSidebar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
//                int position = mCircleAdapter.getPositionForSection(s.charAt(0));
//                if (position != -1) {
//                    mListview.setSelection(position);
//                }
            }
        });
        mSidebar.setTextView(mTxtDialog);
    }

    @Override
    public <T> void updateViewWithFlag(T t, int flag) {
        super.updateViewWithFlag(t, flag);
        if (t != null&&type==1) {
            List<CircleEntrModle> circleEntrModleList = (List<CircleEntrModle>) t;
            if (flag == 0) {
                //全部圈子列表
                recommendList = circleEntrModleList;
//                for (int i = 0; i < onLines.size(); i++) {
//                    boolean isfind = false;
//                    for (int j = 0; j < loveList.size(); j++) {
//                        if (onLines.get(i).appId.equals(loveList.get(j).appId)) {
//                            isfind = true;
//                            break;
//                        }
//                    }
//                    if (!isfind) {
//                        noFollow.add(onLines.get(i));
//                    }
//                }
//                onLines.clear();
//                onLines = noFollow;
                Comparator<Object> comparator = Collator.getInstance(java.util.Locale.CHINA);
                for (int i = 0; i < recommendList.size(); i++) {
                    loveListaaaaa.add(recommendList.get(i).getCircleName());
                }
                Collections.sort(loveListaaaaa,comparator);
                mRecommCircleAdapter.setData(recommendList);
                return;
            }
        }
    }

    @Override
    public void refreshPage() {

    }
}
