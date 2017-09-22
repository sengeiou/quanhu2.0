package com.rz.circled.ui.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.rz.circled.R;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.IntentKey;
import com.rz.common.constant.Type;
import com.rz.common.ui.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;



/**
 * Created by Administrator on 2017/8/11/011.
 */

public class GuideActivity extends BaseActivity {
    @BindView(R.id.guide_vp)
    ViewPager mGuideVp;
    List<Integer> guideList = new ArrayList<>();

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

        guideList.add(R.drawable.guide_one);
        guideList.add(R.drawable.guide_two);
        guideList.add(R.drawable.guide_three);
        final PagerAdapter adapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return guideList == null ? 0 : guideList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ImageView imageView = new ImageView(container.getContext());
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setImageResource(guideList.get(position));
                container.addView(imageView);
                return imageView;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

        };
        mGuideVp.setAdapter(adapter);
        ViewPager.SimpleOnPageChangeListener listener = new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {

                if (position == guideList.size() - 1) {
                    View iv = mGuideVp.getChildAt(position);
                    iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Session.setUserIsFirstGuide(false);
                            Intent intent = new Intent(GuideActivity.this, LoginActivity.class);
                            intent.putExtra(IntentKey.GUIDE_KEY, Type.TYPE_LOGIN_GUIDE);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            }

        };
        mGuideVp.addOnPageChangeListener(listener);

    }

    @Override
    public void initData() {

    }

    @Override
    public void refreshPage() {

    }
}
