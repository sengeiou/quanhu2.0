package com.rz.circled.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.rz.circled.R;
import com.rz.circled.adapter.MyPagerAdapter;
import com.rz.common.cache.preference.Session;
import com.rz.common.ui.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.rz.common.constant.Constants.FIRST_BLOOD;
import static com.rz.common.constant.IntentKey.JUMP_FIND_FIRST;


/**
 * Created by Administrator on 2017/8/11/011.
 */

public class GuideActivity extends BaseActivity {
    @BindView(R.id.guide_vp)
    ViewPager mGuideVp;
    List<View> guideList = new ArrayList<>();
    private int[] imageView = { R.drawable.guide_one, R.drawable.guide_two,
            R.drawable.guide_three };
    @Override
    protected boolean needSwipeBack() {
        return false;
    }

    @Override
    protected boolean needShowTitle() {
        return false;
    }

    @Nullable
    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_guide, null);
    }

    @Override
    public void initView() {
        addView();
        ViewPager.SimpleOnPageChangeListener listener = new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {

                if (position == guideList.size() - 1) {
                    View iv = guideList.get(position);
                    if (iv!=null) {
                        iv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Session.setUserIsFirstGuide(false);
//                                Intent intent = new Intent(GuideActivity.this, LoginActivity.class);
//                                intent.putExtra(IntentKey.GUIDE_KEY, Type.TYPE_LOGIN_GUIDE);
//                                startActivity(intent);
                                Bundle intent =new Bundle();
                                intent.putString(JUMP_FIND_FIRST,FIRST_BLOOD);
                                skipActivity(aty, MainActivity.class,intent);
                                finish();
                            }
                        });
                    }
                }
            }

        };
        mGuideVp.setAdapter(new MyPagerAdapter(guideList));
        mGuideVp.addOnPageChangeListener(listener);


    }

    private void addView() {
        guideList = new ArrayList<>();
        // 将imageview添加到view
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT);
        for (int i = 0; i < imageView.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(params);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            iv.setImageResource(imageView[i]);
            guideList.add(iv);
        }
    }
    @Override
    public void initData() {

    }

    @Override
    public void refreshPage() {

    }
}
