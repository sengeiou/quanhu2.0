package com.rz.circled.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.StatusCode;
import com.rz.circled.R;
import com.rz.circled.adapter.MyPagerAdapter;
import com.rz.circled.modle.ShareModel;
import com.rz.circled.modle.ShareNewsModel;
import com.rz.circled.widget.CirclePageIndicator;
import com.rz.circled.widget.MyGridView;
import com.rz.circled.widget.MyViewPager;
import com.rz.common.adapter.CommonAdapter;
import com.rz.common.adapter.ViewHolder;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.Constants;
import com.rz.common.constant.H5Address;
import com.rz.common.constant.IntentCode;
import com.rz.common.constant.IntentKey;
import com.rz.common.permission.EasyPermissions;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.widget.toasty.Toasty;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.zhuge.analysis.stat.ZhugeSDK;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者：Administrator on 2016/8/15 0015 17:39
 * 功能：分享
 * 说明：
 */
public class ShareNewsAty extends BaseActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.vp_share_news)
    MyViewPager mViewPager;
    @BindView(R.id.cpi_share_news)
    CirclePageIndicator mIndicator;
    @BindView(R.id.tv_title)
    TextView mTv_title;

    List<ShareNewsModel> mShares = new ArrayList<ShareNewsModel>();

    private ShareModel mData;
    private String url, title, desc;
    private UMImage image;
    private ShareAction shareAction;
    private UMWeb web;
    private UMShareAPI shareAPI;
    private View mBaseView;
    private int page;

    Bitmap bitmap;

    public static void startShareNews(Activity activity, ShareModel data) {
        startShareNews(activity, data, Constants.DEFAULTVALUE);
    }

    public static void startShareNews(Activity activity, ShareModel data, int page) {
        if (null == data)
            return;

        Intent intent = new Intent(activity, ShareNewsAty.class);
        intent.putExtra(IntentKey.EXTRA_SERIALIZABLE, data);
        intent.putExtra(IntentKey.EXTRA_PAGE, page);
        activity.startActivity(intent);
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
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.aty_share_news, null);
    }

    @Override
    public void initView() {
        mData = (ShareModel) getIntent().getSerializableExtra(IntentKey.EXTRA_SERIALIZABLE);
        initD(getIntent().getIntExtra(IntentKey.EXTRA_PAGE, Constants.DEFAULTVALUE));
    }

    @Override
    public void initData() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            mBaseView.setVisibility(View.VISIBLE);

            if (null != mShares && mShares.size() > 8) {
                mIndicator.setVisibility(View.VISIBLE);
            } else {
                mIndicator.setVisibility(View.GONE);
            }

            List<View> views = new ArrayList<>();
            for (int i = 0; i < (mShares.size() - 1) / 8 + 1; i++) {
                View view = getLayoutInflater().inflate(R.layout.item_share_vp, mViewPager, false);
                MyGridView myGridView = (MyGridView) view.findViewById(R.id.id_share_news_gridview);

                List<ShareNewsModel> data;
                if (mShares.size() < (i + 1) * 8) {
                    data = mShares.subList(i * 8, mShares.size());
                } else {
                    data = mShares.subList(i * 8, (i + 1) * 8);
                }
                CommonAdapter shareAdapter = new CommonAdapter<ShareNewsModel>(this, R.layout.popup_share_news) {
                    @Override
                    public void convert(ViewHolder helper, ShareNewsModel item, int position) {
                        helper.setImageResource(R.id.id_share_icon_img, item.getDrawable());
                        helper.setText(R.id.id_share_txt, item.getName());
                    }
                };
                myGridView.setAdapter(shareAdapter);
                shareAdapter.setData(data);
                myGridView.setOnItemClickListener(this);
                views.add(view);
            }

            mViewPager.setAdapter(new MyPagerAdapter(views));// 设置填充ViewPager页面的适配器
            mViewPager.setCurrentItem(0);
            mIndicator.setViewPager(mViewPager);
        } else {
            mBaseView.setVisibility(View.GONE);
            EasyPermissions.requestPermissions(this, getString(R.string.sd_card_permissions), RC_EXTENER, perms);
        }
    }


    /**
     * 初始化
     */
    public void initD(int page) {
        this.page = page;

        shareAPI = UMShareAPI.get(aty);
        UMShareConfig config = new UMShareConfig();
        config.isOpenShareEditActivity(false);
        shareAPI.setShareConfig(config);
        mShares.clear();

        ShareNewsModel model1 = new ShareNewsModel(R.drawable.ic_share_wx, getString(R.string.wx_friend));
        ShareNewsModel model2 = new ShareNewsModel(R.drawable.ic_share_friends, getString(R.string.firend_circle));
        ShareNewsModel model3 = new ShareNewsModel(R.drawable.ic_share_sina, getString(R.string.sina_weibo));
        ShareNewsModel model4 = new ShareNewsModel(R.drawable.ic_share_qq, getString(R.string.qq_friend));
        ShareNewsModel model5 = new ShareNewsModel(R.drawable.ic_share_zone, getString(R.string.qq_zone));
        ShareNewsModel model6 = new ShareNewsModel(R.mipmap.ic_im_friend, getString(R.string.youren_firend));
        ShareNewsModel model7 = new ShareNewsModel(R.mipmap.ic_report, getString(R.string.report));
//        ShareNewsModel model8 = new ShareNewsModel(R.drawable.ic_share_show, getString(R.string.youyouquan));

        if (page != IntentCode.PAGE_ADDFRIEND
                && page != IntentCode.Setting.SETTING_RESULT_CODE
                && page != IntentCode.PAGE_CODE_BANNER
                && page != IntentCode.Notice.NOTICE_RESULT_CODE) {
            mShares.add(model6);
//            mShares.add(model8);
        }

        mShares.add(model1);
        mShares.add(model2);
        mShares.add(model3);
        mShares.add(model4);
        mShares.add(model5);

        if (mData != null && !mData.isHideReport()) mShares.add(model7);

        url = mData.getUrl();
        title = mData.getTitle();
        desc = mData.getDesc();

        if (page == IntentCode.Notice.NOTICE_RESULT_CODE
                || page == IntentCode.PAGE_CODE_BANNER
                || page == IntentCode.Setting.SETTING_RESULT_CODE
                || page == IntentCode.PAGE_ADDFRIEND
                || page == IntentCode.MyPromotioin.MY_PROMOTION_CODE) {

            image = new UMImage(aty, R.mipmap.icon_logo);

            if (page == IntentCode.Setting.SETTING_RESULT_CODE) {
                mShares.remove(model7);
                mTv_title.setText(R.string.share_qh_tofriends);
                findViewById(R.id.tv_desc).setVisibility(View.GONE);
            }

            if (page == IntentCode.PAGE_ADDFRIEND || page == IntentCode.MyPromotioin.MY_PROMOTION_CODE) {
                mShares.remove(model7);
                findViewById(R.id.tv_desc).setVisibility(View.GONE);
//                ((TextView) findViewById(R.id.tv_desc)).setText("您的好友在受邀页面完成注册后,\n会自动成为你的悠然好友");
//                ((TextView) findViewById(R.id.tv_title)).setText("邀请好友加入悠然一指,共享资源,拓展人脉!");
            }

        } else {

            if (mData.getTumb().startsWith("http")) {
                image = new UMImage(aty, mData.getTumb());
            } else {
                image = new UMImage(aty, Integer.valueOf(mData.getTumb()));
            }

        }

        web = new UMWeb(url);
        web.setTitle(title);//标题
        web.setDescription(desc);//描述
        web.setThumb(image);  //缩略图

        shareAction = new ShareAction(aty)
                .withMedia(web)
                .setCallback(umShareListener);

        Log.e(TAG, "initD: " + url);
        Log.e(TAG, "mData.getTumb(): " + mData.getTumb());

    }

    SimpleTarget target = new SimpleTarget<Bitmap>() {
        @Override
        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
            web.setThumb(new UMImage(aty, resource));  //缩略图
            shareAction.setPlatform(SHARE_MEDIA.SINA).share();
        }
    };

    @OnClick({R.id.id_share_cancel_btn, R.id.id_share_layout})
    public void onFinish() {
        if (bitmap != null) {
            bitmap.recycle();
            bitmap = null;
        }
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ShareNewsModel shareNewsModel = mShares.get(8 * mIndicator.getCurrentPage() + i);
        switch (shareNewsModel.getName()) {
            case "悠友圈":
//                trackUser("分享", "悠然广场分享", "悠友圈");
                Intent intent = new Intent(aty, TestAty.class);
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, url + "&transferId=" + mData.getId());
                intent.putExtra(Intent.EXTRA_SUBJECT, desc);
                intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, mData.getTumb());
                startActivity(intent);
                finish();
                break;
            //微信分享
            case "微信好友":
                if (shareAPI.isInstall(aty, SHARE_MEDIA.WEIXIN)) {
//                    trackUser("分享", "悠然广场分享", "微信好友");
                    shareAction.setPlatform(SHARE_MEDIA.WEIXIN).share();
                } else {
                    SVProgressHUD.showInfoWithStatus(aty, "沒有安装微信客户端");
                }
                break;
            //微信朋友圈分享
            case "朋友圈":
                if (shareAPI.isInstall(aty, SHARE_MEDIA.WEIXIN)) {
//                    trackUser("分享", "悠然广场分享", "朋友圈");
                    web.setTitle(desc);//标题
                    shareAction.setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).share();
                } else {
                    SVProgressHUD.showInfoWithStatus(aty, "沒有安装微信客户端");
                }
                break;
            //新浪微博分享
            case "新浪微博":
//                trackUser("分享", "悠然广场分享", "新浪微博");
                shareAction.withMedia(image);
                shareAction.withText(title + url);
                shareAction.setPlatform(SHARE_MEDIA.SINA).share();

//                if (page != IntentCode.Notice.NOTICE_RESULT_CODE
//                        && page != IntentCode.PAGE_CODE_BANNER
//                        && page != IntentCode.Setting.SETTING_RESULT_CODE
//                        && page != IntentCode.PAGE_ADDFRIEND
//                        && page != IntentCode.MyPromotioin.MY_PROMOTION_CODE) {
//                    if (mData.getTumb().startsWith("http")) {
//                        Glide.with(aty)
//                                .load(mData.getTumb())
//                                .asBitmap()
//                                .into(target);
//                    } else {
//                        Glide.with(aty)
//                                .load(Integer.valueOf(mData.getTumb()))
//                                .asBitmap()
//                                .into(target);
//                    }
//                } else {
//                shareAction.setPlatform(SHARE_MEDIA.SINA).share();
//                }
                break;
            //QQ好友分享
            case "QQ好友":
                if (shareAPI.isInstall(aty, SHARE_MEDIA.QQ)) {
//                    trackUser("分享", "悠然广场分享", "QQ好友");
                    shareAction.setPlatform(SHARE_MEDIA.QQ).share();
                } else {
                    SVProgressHUD.showInfoWithStatus(aty, "沒有安装QQ客户端");
                }
                break;
            //QQ空间分析
            case "QQ空间":
                if (shareAPI.isInstall(aty, SHARE_MEDIA.QQ)) {
//                    trackUser("分享", "悠然广场分享", "QQ空间");
                    shareAction.setPlatform(SHARE_MEDIA.QZONE).share();
                } else {
                    SVProgressHUD.showInfoWithStatus(aty, "沒有安装QQ客户端");
                }
                break;
            case "悠然好友":
                if (!isLogin() || NIMClient.getStatus() != StatusCode.LOGINED) {
                    Toasty.info(mContext, getString(R.string.im_link_error_hint)).show();
                    return;
                }
                if (isLogin()) {
//                    trackUser("分享", "悠然广场分享", "悠然好友");
                    Log.d("yeying", "shareNewsAty " + mData.toString());
                    mData.fromPage = page;
                    ShareSwitchActivity.start(aty, mData);
                    finish();
                }
                break;
            //举报
            case "举报":
                if (isLogin()) {
//                    trackUser("分享", "悠然广场分享", "举报");
                    ReportActivity.startAty(this, H5Address.ONLINE_REPORT);
//                    CommH5Aty.startCommonH5(this, H5Address.ONLINE_REPORT);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        shareAPI.onActivityResult(requestCode, resultCode, data);
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.d("plat", "platform" + platform);
            if (platform.name().equals("WEIXIN_FAVORITE")) {
                Toast.makeText(aty, R.string.coll_suc, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(aty, R.string.share_suc, Toast.LENGTH_SHORT).show();
            }
            if (platform.equals(SHARE_MEDIA.WEIXIN_CIRCLE)) {
                zhugeTrack();
            }
            finish();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(aty, R.string.share_fail, Toast.LENGTH_SHORT).show();
            if (t != null) {
                Log.d("throw", "throw:" + t.getMessage());
            }
            finish();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            finish();
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        super.onPermissionsGranted(requestCode, perms);
        initData();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        finish();
    }

    private void zhugeTrack() {
        //定义与事件相关的属性信息
        JSONObject eventObject = new JSONObject();
        //记录事件
        ZhugeSDK.getInstance().track(getApplicationContext(), "发到朋友圈",
                eventObject);
    }

    /**
     * 用户是否登录
     */
    public boolean isLogin() {
        if (Session.getUserIsLogin()) {
            return true;
        } else {
            Intent login = new Intent(aty, LoginActivity.class);
            startActivityForResult(login, IntentCode.Login.LOGIN_REQUEST_CODE);
//            showLoginDialog();
            return false;
        }
    }
}
