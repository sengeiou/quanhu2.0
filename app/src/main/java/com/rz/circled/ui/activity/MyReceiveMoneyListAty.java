package com.rz.circled.ui.activity;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.rz.common.ui.activity.BaseActivity;
import com.rz.rz_rrz.R;
import com.rz.rz_rrz.constant.Type;
import com.rz.rz_rrz.view.base.BaseCommonAty;
import com.rz.rz_rrz.view.fragment.LuckyRecordFrag;
import com.rz.rz_rrz.widget.SegmentButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.xiaomi.push.thrift.a.R;


public class MyReceiveMoneyListAty extends BaseActivity {

    //我收到的和我发出的红包按钮自定义控件
    @BindView(R.id.segment_myreceiverred)
    SegmentButton mSegment_myreceiverred;
    @BindView(R.id.vp_replace)
    ViewPager mVp_replace;
    @BindView(R.id.iv_myreveiver_close)
    ImageView mIvMyreceiverClose;

    List<Fragment> views = new ArrayList<>();

    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_my_receive_money_list_aty, null);
    }
    //    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_my_receive_money_list_aty);
//        mSegment_myreceiverred = (SegmentButton) findViewById(R.id.segment_myreceiverred);
//        mVp_replace = (ViewPager) findViewById(R.id.vp_replace);
//    }

    @Override
    public void initView() {
        mIvMyreceiverClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        hideTitleBar();

        views.add(LuckyRecordFrag.newInstace(Type.TYPE_RECEIVE));
        views.add(LuckyRecordFrag.newInstace(Type.TYPE_SEND));


        mVp_replace.setAdapter(new VideoPagesAdapter(getSupportFragmentManager(), views));
        mVp_replace.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mSegment_myreceiverred.setLeftSelected(position == 0 ? true : false);
            }
        });
        //设置收到和发出红包的监听
        SegmentButton.OnSegmentButtonSelectedListener onSegmentButtonSelectedListener = new SegmentButton.OnSegmentButtonSelectedListener() {
            @Override
            public void onLeftSelected() {
//                ToastUtil.showToast("左边备选了");
                mVp_replace.setCurrentItem(0);

            }

            @Override
            public void onRightSelected() {
//                ToastUtil.showToast("右边备选了");
                mVp_replace.setCurrentItem(1);
            }
        };
        mSegment_myreceiverred.setOnSegmentButtonSelectedListener(onSegmentButtonSelectedListener);

    }

    private class VideoPagesAdapter extends FragmentStatePagerAdapter {

        private List<Fragment> list;

        public VideoPagesAdapter(FragmentManager fm, List<Fragment> list) {
            super(fm);
            this.list = list;
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }

    @Override
    public void initData() {

    }

}
