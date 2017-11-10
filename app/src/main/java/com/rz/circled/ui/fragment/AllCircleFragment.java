package com.rz.circled.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.rz.circled.R;
import com.rz.circled.adapter.CircleAdapter;
import com.rz.circled.event.EventConstant;
import com.rz.circled.presenter.impl.CirclePresenter;
import com.rz.circled.ui.activity.AllCircleSearchActivity;
import com.rz.circled.ui.activity.WebContainerActivity;
import com.rz.circled.widget.SideBar;
import com.rz.circled.widget.pinyin.CharacterParser;
import com.rz.circled.widget.pinyin.CircleComparator;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.IntentKey;
import com.rz.common.event.BaseEvent;
import com.rz.common.ui.fragment.BaseFragment;
import com.rz.common.utils.StringUtils;
import com.rz.httpapi.bean.CircleEntrModle;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;

import static com.rz.common.constant.CommonCode.EventType.TYPE_CIRCLE_TATE;
import static com.rz.common.constant.CommonCode.EventType.TYPE_SET_CURRENT;
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
    private CircleComparator mPyComparator;
    private CirclePresenter mPresenter;
    List<CircleEntrModle> loveAllList;
    List<CircleEntrModle> loveList = new ArrayList<>();
    List<CircleEntrModle> noFollow = new ArrayList<>();
    static List<CircleEntrModle> loveChagelist = new ArrayList<>();
    static List<CircleEntrModle> recommendChangelist = new ArrayList<>();
    List<CircleEntrModle> recommendList;
    List<CircleEntrModle> delHs = new ArrayList<>();
    List<CircleEntrModle> addHs = new ArrayList<>();
    private boolean isEdit;
    int type;
    private CharacterParser mCharacterParser;

    @Nullable
    @Override
    public View loadView(LayoutInflater inflater) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
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
        mCharacterParser = CharacterParser.getInstance();
        mPresenter.attachView(this);
    }

    @Override
    public void initView() {
        loveAllList = (List<CircleEntrModle>) getActivity().getIntent().getSerializableExtra(LOVE_CIRCLE);
        mPyComparator = new CircleComparator();
        Bundle bundle = getArguments();
        if (bundle != null) {
            type = bundle.getInt(IntentKey.EXTRA_TYPE);
        }
        initHeadView();
        delLove();
        mListview.addHeaderView(mheadView);
        mListview.setDividerHeight(0);
        if (type == 0) {
            mCircleAdapter = new CircleAdapter(mActivity, R.layout.circle_adapter_item);
            mListview.setAdapter(mCircleAdapter);
            mCircleAdapter.setData(loveList);
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
        changeLetter(loveList);
        if (loveList.isEmpty()){
            EventBus.getDefault().post(new BaseEvent(TYPE_SET_CURRENT));
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BaseEvent event) {
        if (event.getType() == TYPE_CIRCLE_TATE) {
            this.isEdit = (boolean) event.getData();
            if (type == 0) {
                mCircleAdapter.setEdit(isEdit);
                mCircleAdapter.notifyDataSetChanged();

            } else {
                mRecommCircleAdapter.setEdit(isEdit);
                mRecommCircleAdapter.notifyDataSetChanged();
            }
            return;
        }
//        if (event.getType()==TYPE_FINISH_TATE){
            if (type==0){
                delHs.clear();
                for (int i = 0; i < loveList.size(); i++) {
                    if (loveList.get(i).isSeleced) {
                        loveList.get(i).setSeleced(false);
                        delHs.add(loveList.get(i));
                        loveChagelist.add(loveList.get(i));
                    }
                }
                loveList.removeAll(delHs);
                loveList.addAll(recommendChangelist);
                recommendChangelist.clear();
                mCircleAdapter.setData(loveList);
                mapPresenter(delHs);

            } else {
                addHs.clear();
                for (int i = 0; i < recommendList.size(); i++) {
                    if (recommendList.get(i).isSeleced) {
                        recommendList.get(i).setSeleced(false);
                        addHs.add(recommendList.get(i));
                        recommendChangelist.add(recommendList.get(i));
                    }
                }
//                if (addHs.isEmpty()){
//                    return;
//                }
                recommendList.addAll(loveChagelist);
                loveChagelist.clear();
                recommendList.removeAll(addHs);
                changeLetter(recommendList);
                mRecommCircleAdapter.setData(recommendList);
                mapPresenter(addHs);
            }
            return;
//        }

    }

    StringBuffer sb = new StringBuffer();

    private void mapPresenter(List<CircleEntrModle> list) {
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i).appId + ",");
        }
        if (type == 0) {
            mPresenter.removeLoveCircle(sb.toString(), Session.getUserId());
        } else {
            mPresenter.addLoveCircle(sb.toString(), 1);
        }
        sb.delete(0, sb.length());
    }

    private void initHeadView() {
        mheadView = LayoutInflater.from(mActivity).inflate(R.layout.all_circle_head_layout, null);
        View etSearch = mheadView.findViewById(R.id.id_search_key_rela1);
        etSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEdit) {
                    AllCircleSearchActivity.stratActivity(mActivity, type==0?loveList:recommendList);
                }
            }
        });
    }

    @Override
    public void initData() {
        mPresenter.getCircleEntranceList(0);
        // 设置右侧触摸监听
        mSidebar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                int position;
                if (type == 0) {
                    position = mCircleAdapter.getPositionForSection(s.charAt(0));
                } else {
                    position = mRecommCircleAdapter.getPositionForSection(s.charAt(0));
                }
                if (position != -1) {
                    mListview.setSelection(position);
                }
            }
        });
        mSidebar.setTextView(mTxtDialog);
        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position==0){
                    return;
                }
                if (type == 0) {
                    CircleEntrModle circleEntrModle = loveList.get(position - 1);
                    boolean isSeleced = circleEntrModle.isSeleced;
                    if (isEdit) {
                        if (isSeleced) {
                            circleEntrModle.isSeleced = false;
                        } else {
                            circleEntrModle.isSeleced = true;
                        }
                        mCircleAdapter.notifyDataSetChanged();
                    } else {
                        WebContainerActivity.startActivity(mActivity, circleEntrModle.getCircleUrl());
                    }
                } else {
                    CircleEntrModle circleEntrModle = recommendList.get(position - 1);
                    boolean isSeleced = circleEntrModle.isSeleced;
                    if (isEdit) {
                        if (isSeleced) {
                            circleEntrModle.isSeleced = false;
                        } else {
                            circleEntrModle.isSeleced = true;
                        }
                        mRecommCircleAdapter.notifyDataSetChanged();
                    } else {
                        WebContainerActivity.startActivity(mActivity, circleEntrModle.getCircleUrl());
                    }


                }

            }
        });
    }

    @Override
    public <T> void updateView(T t) {
        super.updateView(t);
        EventBus.getDefault().post(new BaseEvent(EventConstant.UPDATE_LOVE_CIRCLE));
    }

    @Override
    public <T> void updateViewWithFlag(T t, int flag) {
        super.updateViewWithFlag(t, flag);
        if (t != null && type == 1) {
            List<CircleEntrModle> circleEntrModleList = (List<CircleEntrModle>) t;
            if (flag == 0) {
                //全部圈子列表
                recommendList = circleEntrModleList;
                for (int i = 0; i < recommendList.size(); i++) {
                    boolean isfind = false;
                    for (int j = 0; j < loveList.size(); j++) {
                        if (recommendList.get(i).appId.equals(loveList.get(j).appId)) {
                            isfind = true;
                            break;
                        }
                    }
                    if (!isfind) {
                        noFollow.add(recommendList.get(i));
                    }
                }
                recommendList.clear();
                recommendList = noFollow;
                changeLetter(recommendList);
                mRecommCircleAdapter.setData(recommendList);
                return;
            }
        }
    }

    /**
     * 对数据进行字母排序，并保存字母的首字母
     *
     * @param friendList
     */
    public void changeLetter(List<CircleEntrModle> friendList) {
        for (int i = 0; i < friendList.size(); i++) {
            CircleEntrModle model = friendList.get(i);
            setModelFirstLetter(model);
        }
        Collections.sort(friendList, mPyComparator);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    /**
     * 单个model设置首字母
     *
     * @param model
     */
    public void setModelFirstLetter(CircleEntrModle model) {
        String mFirstLetter;
        if (!StringUtils.isEmpty(model.getCircleName())) {
            mFirstLetter = mCharacterParser.getSelling(model.getCircleName());
        } else {
            mFirstLetter = "#";
        }
        if (!StringUtils.isEmpty(mFirstLetter)) {
            String sortString = mFirstLetter.substring(0, 1).toUpperCase();

            if (sortString.matches("[A-Z]")) {
                model.setFirstLetter(sortString.toUpperCase());
            } else {
                model.setFirstLetter("#");
            }
        } else {
            model.setFirstLetter("#");
        }

    }

    @Override
    public void refreshPage() {

    }
}
