package com.rz.circled.ui.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.model.CustomMessageConfig;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.rz.circled.R;
import com.rz.circled.modle.ShareModel;
import com.rz.common.constant.Constants;
import com.rz.common.constant.IntentCode;
import com.rz.common.event.BaseEvent;
import com.rz.common.utils.Protect;
import com.rz.common.widget.toasty.Toasty;
import com.rz.sgt.jsbridge.JsEvent;
import com.yryz.yunxinim.session.SessionHelper;
import com.yryz.yunxinim.session.extension.ArticleAttachment;
import com.yryz.yunxinim.session.extension.CustomAttachment;
import com.yryz.yunxinim.session.extension.ShowAttachment;
import com.yryz.yunxinim.uikit.CustomPushContentProvider;
import com.yryz.yunxinim.uikit.NimUIKit;
import com.yryz.yunxinim.uikit.common.util.sys.ScreenUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

/**
 * Created by rzw2 on 2017/1/12.
 */

public class WorkImShareDialog extends Dialog {
    private Activity context;
    private ShareModel mData;

    public WorkImShareDialog(Context context) {
        super(context, R.style.dialog_default_style);
    }

    public WorkImShareDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public WorkImShareDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public WorkImShareDialog(Activity context, ShareModel model) {
        super(context, R.style.dialog_default_style);
        this.mData = model;
        init(context);
    }

    private void init(Activity context) {
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_works_share);

        LinearLayout root = (LinearLayout) findViewById(R.id.layout_root);
        ViewGroup.LayoutParams params = root.getLayoutParams();
        params.width = (int) (ScreenUtil.getDisplayWidth() * 0.67);
        root.setLayoutParams(params);

        ImageView shareImgUserAvater = (ImageView) findViewById(R.id.share_user_avater);
        TextView shareTvUserName = (TextView) findViewById(R.id.share_user_name);
        TextView shareTvWorksTitle = (TextView) findViewById(R.id.share_works_title);
        TextView shareTvWorksDesc = (TextView) findViewById(R.id.share_works_desc);
        final EditText shareEtvLeaveMsg = (EditText) findViewById(R.id.share_leave_msg_edit);
        ImageView shareImgWorksContent = (ImageView) findViewById(R.id.share_media_img_content);
        ImageView shareImgWorksType = (ImageView) findViewById(R.id.share_media_img_type);
        TextView shareDialogTitle = (TextView) findViewById(R.id.tv_dialog_title);
        TextView confirmBtn = (TextView) findViewById(R.id.id_shine_sure_txt);

        if (null == mData)
            return;

        if (mData.fromPage == IntentCode.ShowShare.REQUEST_CODE_SHOW_SHARE) {
            shareImgWorksType.setVisibility(mData.getMediaType() == 3 ? View.VISIBLE : View.GONE);
        }

        if (Protect.checkLoadImageStatus(context)) {
            Glide.with(context).load(mData.getTumb())
                    .placeholder(R.drawable.pic_opus_default_article).into(shareImgWorksContent);
        }

        if (Protect.checkLoadImageStatus(context)) {
            Glide.with(context).load(!TextUtils.isEmpty(mData.getUserAvater()) ? mData.getUserAvater() : mData.userAvaterRes)
                    .placeholder(R.drawable.ic_default_head).into(shareImgUserAvater);
        }

        shareTvWorksDesc.setText(mData.getDesc());
        shareTvWorksTitle.setText(mData.getTitle());
        shareTvUserName.setText(mData.getUserName());

        findViewById(R.id.id_shine_cancel_txt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        findViewById(R.id.id_shine_sure_txt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(mData.getUserId())) {
                    Toasty.info(context, context.getString(R.string.im_link_error_hint)).show();
                    dismiss();
                    return;
                }

                CustomMessageConfig config = new CustomMessageConfig();
                config.enablePush = true; // 推送

                if (!TextUtils.isEmpty(shareEtvLeaveMsg.getText().toString().trim())) {
                    IMMessage textMessage = MessageBuilder.createTextMessage(mData.getUserId(), mData.getTypeEnum(), shareEtvLeaveMsg.getText().toString().trim());
                    textMessage.setConfig(config);
                    appendPushConfig(textMessage);
                    // send message to server and save to db
                    NIMClient.getService(MsgService.class).sendMessage(textMessage, false);
                }

                JSONObject attachmentData = new JSONObject();
                CustomAttachment attachment;
                String pushContent;
                if (mData.fromPage == IntentCode.ShowShare.REQUEST_CODE_SHOW_SHARE) {

                    attachmentData.put(ShowAttachment.KEY_DESC, mData.getTitle());
                    attachmentData.put(ShowAttachment.KEY_IMG_URL, mData.getTumb());
                    attachmentData.put(ShowAttachment.KEY_MEDIA_TYPE, mData.getType());
                    attachmentData.put(ShowAttachment.KEY_IMG_RES, mData.getDefaultPic());
                    attachmentData.put(ShowAttachment.KEY_CLUB_ID, mData.getClubId());
                    attachment = new ShowAttachment(attachmentData);
                    pushContent = "分享了悠·秀";

                } else {

                    attachmentData.put(ArticleAttachment.KEY_TITLE, mData.getTitle());
                    attachmentData.put(ArticleAttachment.KEY_DESC, mData.getDesc());
                    attachmentData.put(ArticleAttachment.KEY_IMG_URL, mData.getTumb());
                    attachmentData.put(ArticleAttachment.KEY_URL, mData.getUrl());
                    attachmentData.put(ArticleAttachment.KEY_ID, mData.getId());
                    attachment = new ArticleAttachment(attachmentData);
                    pushContent = "分享了一篇文章";

                }

                IMMessage msg = MessageBuilder.createCustomMessage(mData.getUserId(), mData.getTypeEnum(), attachment);

                msg.setConfig(config);
                msg.setPushContent(pushContent);

                appendPushConfig(msg);
                // send message to server and save to db
                NIMClient.getService(MsgService.class).sendMessage(msg, false);

                switch (mData.getTypeEnum()) {
                    case P2P:
                        SessionHelper.startP2PSession(context, mData.getUserId());
                        break;
                    case Team:
                        SessionHelper.startTeamSession(context, mData.getUserId());
                        break;
                    default:
                        break;
                }

                JsEvent.callJsEvent(null, true);

                BaseEvent event = new BaseEvent();
                event.info = Constants.SWITCH_SHARE_SUCCESS;
                EventBus.getDefault().post(event);

                dismiss();
                context.finish();
            }
        });
    }

    private void appendPushConfig(IMMessage message) {
        CustomPushContentProvider customConfig = NimUIKit.getCustomPushContentProvider();
        if (customConfig != null) {
            String content = customConfig.getPushContent(message);
            Map<String, Object> payload = customConfig.getPushPayload(message);
            message.setPushContent(content);
            message.setPushPayload(payload);
        }
    }
}
