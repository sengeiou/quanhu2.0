package com.rz.circled.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rz.circled.R;
import com.rz.circled.presenter.impl.CirclePresenter;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.IntentKey;
import com.rz.common.constant.Type;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.utils.Protect;
import com.rz.common.utils.StringUtils;
import com.rz.common.utils.SystemUtils;
import com.rz.httpapi.bean.BannerAddSubjectModel;
import com.yryz.yunxinim.uikit.common.util.string.StringUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.rz.common.utils.SystemUtils.trackUser;


/**
 * 作者：Administrator on 2016/6/22 0022 11:07
 * 功能：欢迎页+广告页
 * 说明：展示欢迎页+广告页
 */
public class SplashAty extends BaseActivity {

    /**
     * 跳过
     */
    private TextView mTxtjumpTo;

    /**
     * 启动页背景
     */
    private ImageView mImgBg;

    /**
     * 界面广告停留时间
     */
    private int recLen;

    /**
     * 倒计时类
     */
    private MyCount mc;

    //是否点击广告
    private boolean isClickAdv;

    //时间是否结束
    private boolean isTimeOver;
    private List<BannerAddSubjectModel> bannerList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View loadView(LayoutInflater inflater) {
        trackUser("入口", "启动", "");
//        initMainRequest();
        return inflater.inflate(R.layout.activity_splash, null);
    }

    @Override
    protected boolean needShowTitle() {
        return false;
    }

    @Override
    protected boolean needSwipeBack() {
        return false;
    }

    @Override
    public void initPresenter() {
        CirclePresenter presenter = new CirclePresenter();
        presenter.attachView(this);
        presenter.getBannerList("1");
    }

    @Override
    public void initView() {

        mImgBg = (ImageView) findViewById(R.id.id_start_page_bg_img);
        mImgBg.setImageResource(R.drawable.page_bg_qq_new);
        if (!TextUtils.equals(StringUtils.isEmpty(Session.getAppVersion()) ? "" : Session.getAppVersion(), SystemUtils.getVersionName(this))) {
//            ClearCacheUtil.clearCache(aty, 0, "");
            Session.clearMust();
            Session.setAppVersion(SystemUtils.getVersionName(this));
        }
        // 判断用户是否是第一次安装
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 判断用户是否是第一次安装
                if (!Session.getUserIsFirstDownload()) {
                    try {
                        initV();
                    } catch (ParseException e) {
                        jumpTo();
                    }
                } else {
                    if (!Session.getUserIsFirstGuide()) {
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        intent.putExtra(IntentKey.GUIDE_KEY, Type.TYPE_LOGIN_GUIDE);
                        startActivity(intent);
                        finish();
                    }else{
                        skipActivity(aty, GuideActivity.class);
                    }
                }
            }
        }, 2000);
    }

    @Override
    public <T> void updateViewWithFlag(T t, int flag) {
        super.updateViewWithFlag(t, flag);
        bannerList.addAll((Collection<? extends BannerAddSubjectModel>) t);


    }

    @Override
    public void initData() {
        SystemUtils.checkSystem(this);
    }

    private void initV() throws ParseException {
//        loadRewardGiftList();
        //当前日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long currentTime = System.currentTimeMillis();
        //上刊日期
        String adv_upIngDate = Session.getAdv_upIngDate();
        String adv_exDate = Session.getAdv_expireDate();
        if (!StringUtil.isEmpty(adv_exDate)&&!StringUtil.isEmpty(adv_exDate)){
        long startTime = sdf.parse(adv_upIngDate).getTime();
        long endTime = sdf.parse(adv_exDate).getTime();
        long upIngDate = StringUtils.isEmpty(Session.getAdv_upIngDate()) ? 0 : startTime;
        //过期日期
        long expireDate = StringUtils.isEmpty(Session.getAdv_expireDate()) ? 0 :endTime ;
        Log.d("time-----", "当前时间" + currentTime + "上刊时间" + upIngDate + "过期日期" + expireDate);
        if (currentTime >= upIngDate && currentTime < expireDate) {
            recLen = 1000 * 5;
            if (Session.getAdv_pic_url().endsWith(".gif")) {
                if (Protect.checkLoadImageStatus(aty)) {
                    Glide.with(aty).load(Session.getAdv_pic_url()).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(mImgBg);
                }
            } else {
                if (Protect.checkLoadImageStatus(aty)) {
                    Glide.with(aty).load(Session.getAdv_pic_url()).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.ALL).into(mImgBg);
                }
            }
            if (!StringUtils.isEmpty(Session.getAdv_url()) && isNetUrl(Session.getAdv_url())) {
                mImgBg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        isClickAdv = true;
                        if (!TextUtils.isEmpty(Session.getAdv_url())) {
                            CommonH5Activity.startCommonH5(aty, "", Session.getAdv_url(),true);
                        }
                    }
                });
            }
        } else {
//            recLen = 1000 * 4;
//
//            mImgBg.setImageResource(R.drawable.bg_splash_activity);
            jumpTo();
        }
        }else{
            jumpTo();
        }

        mTxtjumpTo = (TextView) findViewById(R.id.id_jump_txt);
        mTxtjumpTo.setVisibility(View.VISIBLE);
        mTxtjumpTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isTimeOver = true;
                jumpTo();
            }
        });
        //界面广告停留时间
        mc = new MyCount(recLen, 1000);
        mc.start();
    }


    public static boolean isNetUrl(String s) {
        try {
            return Patterns.WEB_URL.matcher(s).matches();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isClickAdv = false;
        if (isTimeOver && !isFinishing()) {
            jumpTo();
        }
    }

    /**
     * 跳转
     */
    private void jumpTo() {
        if (!isClickAdv){
        if (Session.getUserIsLogin()) {
            skipActivity(aty, MainActivity.class);
        } else {
            skipActivity(aty, LoginActivity.class);
        }}
    }

    @Override
    public void refreshPage() {

    }

    /**
     * 倒计时类
     */
    public class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            mTxtjumpTo.setText(getString(R.string.start_page_jump) + "(" + l / 1000
                    + getString(R.string.second) + ")");
        }

        @Override
        public void onFinish() {
            isTimeOver = true;
            Log.i(TAG, "onFinish: "+"lixiang");
            jumpTo();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mc) {
            mc.cancel();
        }
    }


//    public void loadRewardGiftList() {
//        Call<ResponseData<List<RewardGiftModel>>> call = Http.getNewService(this).v3CircleGetTransferPrice(
//                Session.getUserId());
//        CallManager.add(call);
//        call.enqueue(new BaseCallback<ResponseData<List<RewardGiftModel>>>() {
//            @Override
//            public void onResponse(Call<ResponseData<List<RewardGiftModel>>> call, Response<ResponseData<List<RewardGiftModel>>> response) {
//                super.onResponse(call, response);
//                if (response.isSuccessful()) {
//                    ResponseData<List<RewardGiftModel>> res = response.body();
//                    if (res.getRet() == ReturnCode.SUCCESS) {
//                        List<RewardGiftModel> mGifts = res.getData();
//                        if (mGifts != null && !mGifts.isEmpty()) {
//                            try {
//                                EntityCache<RewardGiftModel> entityCache = new EntityCache<RewardGiftModel>(SplashAty.this, RewardGiftModel.class);
//                                entityCache.putListEntityAddTag(mGifts, "transfer");
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                            return;
//                        } else {
//                            return;
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseData<List<RewardGiftModel>>> call, Throwable t) {
//                super.onFailure(call, t);
//            }
//        });
//    }

}
