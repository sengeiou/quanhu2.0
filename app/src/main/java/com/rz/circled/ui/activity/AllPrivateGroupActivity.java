package com.rz.circled.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.rz.circled.R;
import com.rz.circled.adapter.DefaultPrivateGroupAdapter;
import com.rz.circled.ui.fragment.NewsCommonFragment;
import com.rz.circled.ui.fragment.PrivateGroupAllFragment;
import com.rz.circled.ui.fragment.PrivateGroupCreateByMyselfFragment;
import com.rz.common.event.BaseEvent;
import com.rz.common.ui.activity.BaseActivity;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.rz.circled.event.EventConstant.PRIVATE_GROUP_SEARCH_KEY;
import static com.rz.circled.ui.fragment.NewsCommonFragment.NEWS_ANNOUNCEMENT;
import static com.rz.common.constant.IntentKey.EXTRA_TYPE;

/**
 * Created by rzw2 on 2017/9/2.
 */

public class AllPrivateGroupActivity extends BaseActivity {

    @BindView(R.id.etv_search)
    EditText etvSearch;
    @BindView(R.id.layout_frame)
    FrameLayout layoutFrame;

    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_all_private_group, null, false);
    }

    @Override
    public void initView() {
        etvSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH && event != null) {
                    EventBus.getDefault().post(new BaseEvent(PRIVATE_GROUP_SEARCH_KEY, etvSearch.getText().toString().trim()));
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void initData() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.layout_frame, NewsCommonFragment.newInstance(NEWS_ANNOUNCEMENT));
        transaction.replace(R.id.layout_frame, PrivateGroupAllFragment.newInstance());
        transaction.commit();
    }
}
