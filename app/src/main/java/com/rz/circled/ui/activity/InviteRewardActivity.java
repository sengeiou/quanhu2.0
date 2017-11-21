package com.rz.circled.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.StatusCode;
import com.rz.circled.R;
import com.rz.circled.dialog.InviteRewardScanDialog;
import com.rz.circled.modle.ShareModel;
import com.rz.circled.modle.ShareNewsModel;
import com.rz.circled.widget.MyGridView;
import com.rz.common.adapter.CommonAdapter;
import com.rz.common.adapter.ViewHolder;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.H5Address;
import com.rz.common.permission.EasyPermissions;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.widget.svp.SVProgressHUD;
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
import butterknife.ButterKnife;

/**
 * Created by rzw2 on 2017/11/20.
 */

public class InviteRewardActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    @BindView(R.id.iv_reward)
    ImageView ivReward;
    @BindView(R.id.tv)
    TextView tv;
    @BindView(R.id.textView4)
    TextView textView4;
    @BindView(R.id.gridview)
    MyGridView gridview;

    private List<ShareNewsModel> mShares = new ArrayList<ShareNewsModel>();
    private ShareModel mData;
    private UMImage image;
    private UMWeb web;
    private UMShareAPI shareAPI;
    private ShareAction shareAction;
    SimpleTarget target = new SimpleTarget<Bitmap>() {
        @Override
        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
            web.setThumb(new UMImage(aty, resource));  //缩略图
            shareAction.setPlatform(SHARE_MEDIA.SINA).share();
        }
    };

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
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_invite_reward, null);
    }

    @Override
    public void initView() {
        mData = new ShareModel(Session.getUserName() +
                getString(R.string.invite_title),
                getString(R.string.invite_desc),
                Session.getInviteLink());
        setTitleText("邀请有奖");
        setTitleRightText("我的邀请");
        setTitleRightTextColor(R.color.font_color_blue);
        setTitleRightListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showActivity(aty, InviteRecordActivity.class);
            }
        });
    }

    @Override
    public void initData() {
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
        ShareNewsModel model6 = new ShareNewsModel(R.mipmap.ic_invite_scan, getString(R.string.qr_code));

        mShares.add(model1);
        mShares.add(model2);
        mShares.add(model4);
        mShares.add(model5);
        mShares.add(model3);
        mShares.add(model6);

        if (TextUtils.isEmpty(mData.getTumb())) {
            image = new UMImage(aty, R.mipmap.share_logo);
        } else {
            if (mData.getTumb().startsWith("http")) {
                image = new UMImage(aty, mData.getTumb());
            } else {
                image = new UMImage(aty, Integer.valueOf(mData.getTumb()));
            }
        }

        UMImage thumb = new UMImage(this, R.mipmap.share_logo);
        image.setThumb(thumb);

        web = new UMWeb(mData.getUrl());
        web.setTitle(mData.getTitle());//标题
        web.setDescription(mData.getDesc());//描述
        web.setThumb(image);  //缩略图

        shareAction = new ShareAction(aty)
                .withMedia(web)
                .setCallback(umShareListener);
        initGridview();
    }

    private void initGridview() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            CommonAdapter<ShareNewsModel> shareAdapter = new CommonAdapter<ShareNewsModel>(this, R.layout.popup_share_news) {
                @Override
                public void convert(ViewHolder helper, ShareNewsModel item, int position) {
                    helper.setImageResource(R.id.id_share_icon_img, item.getDrawable());
                    helper.setText(R.id.id_share_txt, item.getName());
                }
            };
            gridview.setAdapter(shareAdapter);
            shareAdapter.setData(mShares);
            gridview.setOnItemClickListener(this);
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.sd_card_permissions), RC_EXTENER, perms);
        }
    }

    @Override
    public void refreshPage() {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ShareNewsModel shareNewsModel = mShares.get(position);
        switch (shareNewsModel.getName()) {
            case "悠友圈":
//                trackUser("分享", "悠然广场分享", "悠友圈");
                Intent intent = new Intent(aty, TestAty.class);
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, mData.getUrl() + "&transferId=" + mData.getId());
                intent.putExtra(Intent.EXTRA_SUBJECT, mData.getDesc());
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
                    shareAction.setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).share();
                } else {
                    SVProgressHUD.showInfoWithStatus(aty, "沒有安装微信客户端");
                }
                break;
            //新浪微博分享
            case "新浪微博":
//                trackUser("分享", "悠然广场分享", "新浪微博");
                shareAction.withMedia(image);
                shareAction.withText(mData.getTitle() + mData.getUrl());
                shareAction.setPlatform(SHARE_MEDIA.SINA).share();
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
            //举报
            case "举报":
                if (isLogin()) {
//                    trackUser("分享", "悠然广场分享", "举报");
                    ReportActivity.startAty(this, H5Address.ONLINE_REPORT);
                }
                break;
            case "二维码":
                new InviteRewardScanDialog().show(getSupportFragmentManager(), "");
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        shareAPI.onActivityResult(requestCode, resultCode, data);
    }

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
}
