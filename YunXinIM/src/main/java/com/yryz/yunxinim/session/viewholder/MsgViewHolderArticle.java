package com.yryz.yunxinim.session.viewholder;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.rz.jsbridge.core.WebContainerAty;
import com.yryz.yunxinim.R;
import com.yryz.yunxinim.session.extension.ArticleAttachment;
import com.yryz.yunxinim.uikit.common.util.sys.ScreenUtil;
import com.yryz.yunxinim.uikit.session.viewholder.MsgViewHolderBase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhoujianghua on 2015/8/4.
 */
public class MsgViewHolderArticle extends MsgViewHolderBase {
    private ImageView shareImgContent;
    private ImageView shareImgType;
    private TextView shareTvTitle;
    private TextView shareTvContent;

    private static DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showStubImage(R.color.white)          // 设置图片下载期间显示的图片
            .showImageForEmptyUri(R.color.white)  // 设置图片Uri为空或是错误的时候显示的图片
            .showImageOnFail(R.color.white)       // 设置图片加载或解码过程中发生错误显示的图片
            .cacheInMemory(true)// 设置下载的图片是否缓存在内存中
            .cacheOnDisc(true)                          // 设置下载的图片是否缓存在SD卡中
            .build();

    private static DisplayImageOptions audio_option = new DisplayImageOptions.Builder()
            .showStubImage(R.drawable.pic_opus_default_audio)          // 设置图片下载期间显示的图片
            .showImageForEmptyUri(R.drawable.pic_opus_default_audio)  // 设置图片Uri为空或是错误的时候显示的图片
            .showImageOnFail(R.drawable.pic_opus_default_audio)       // 设置图片加载或解码过程中发生错误显示的图片
            .cacheInMemory(true)// 设置下载的图片是否缓存在内存中
            .cacheOnDisc(true)                          // 设置下载的图片是否缓存在SD卡中
            .build();

    private static DisplayImageOptions article_options = new DisplayImageOptions.Builder()
            .showStubImage(R.drawable.pic_opus_default_article)          // 设置图片下载期间显示的图片
            .showImageForEmptyUri(R.drawable.pic_opus_default_article)  // 设置图片Uri为空或是错误的时候显示的图片
            .showImageOnFail(R.drawable.pic_opus_default_article)       // 设置图片加载或解码过程中发生错误显示的图片
            .cacheInMemory(true)// 设置下载的图片是否缓存在内存中
            .cacheOnDisc(true)                          // 设置下载的图片是否缓存在SD卡中
            .build();

    @Override
    protected int getContentResId() {
        return R.layout.nim_message_item_share;
    }

    @Override
    protected void inflateContentView() {
        shareImgContent = (ImageView) view.findViewById(R.id.nim_message_item_img_content);
        shareImgType = (ImageView) view.findViewById(R.id.nim_message_item_img_type);
        shareTvTitle = (TextView) view.findViewById(R.id.nim_message_item_text_title);
        shareTvContent = (TextView) view.findViewById(R.id.nim_message_item_text_content);
    }

    @Override
    protected void bindContentView() {
        ArticleAttachment attachment = ((ArticleAttachment) message.getAttachment());
        if (attachment == null) {
            return;
        }

        int width = ScreenUtil.getDisplayWidth() - ScreenUtil.dip2px(52) * 2;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        setLayoutParams(width, height, contentContainer);

        shareTvTitle.setText(attachment.getTitle());
        shareTvContent.setText(attachment.getDesc());
        if (!TextUtils.isEmpty(attachment.getImgUrl()) && attachment.getImgUrl().contains("http")) {
            ImageLoader.getInstance().displayImage(attachment.getImgUrl(), shareImgContent, options);
        } else {
            ImageLoader.getInstance().displayImage("drawable://" + R.drawable.pic_opus_default_article, shareImgContent, article_options);
        }
    }

    @Override
    protected void onItemClick() {
        ArticleAttachment attachment = ((ArticleAttachment) message.getAttachment());
        Log.d("yeying", "MsgViewHolderDefCustom  onItemClick " + attachment.toString());
        attachment.checkData();
        if (TextUtils.isEmpty(attachment.getId()))
            WebContainerAty.startAty(context, attachment.getUrl());
        else {
            Pattern pattern = Pattern.compile("[0-9]*");
            Matcher isNum = pattern.matcher(attachment.getId());
            if (!isNum.matches()) {
                return;
            }
            Intent intent = new Intent();
            intent.setAction("com.rz.yryz.TRANSFER_DETAILS_ACTION");
            Bundle bundle = new Bundle();
            bundle.putInt("key_id", Integer.valueOf(attachment.getId()));
            intent.putExtras(bundle);
            context.startActivity(intent);
        }
    }

    @Override
    protected int rightBackground() {
        return R.drawable.nim_message_right_white_bg;
    }
}
