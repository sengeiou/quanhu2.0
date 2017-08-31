package com.yryz.yunxinim.session.viewholder;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yryz.yunxinim.R;
import com.yryz.yunxinim.session.extension.ShowAttachment;
import com.yryz.yunxinim.uikit.common.util.sys.ScreenUtil;
import com.yryz.yunxinim.uikit.session.viewholder.MsgViewHolderBase;

/**
 * Created by zhoujianghua on 2015/8/4.
 */
public class MsgViewHolderShow extends MsgViewHolderBase {
    private ImageView shareImgContent;
    private ImageView shareImgType;
    private TextView shareTvContent;

    private static DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showStubImage(R.color.white)          // 设置图片下载期间显示的图片
            .showImageForEmptyUri(R.color.white)  // 设置图片Uri为空或是错误的时候显示的图片
            .showImageOnFail(R.color.white)       // 设置图片加载或解码过程中发生错误显示的图片
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
        return R.layout.nim_message_item_show;
    }

    @Override
    protected void inflateContentView() {
        shareImgContent = (ImageView) view.findViewById(R.id.nim_message_item_img_content);
        shareImgType = (ImageView) view.findViewById(R.id.nim_message_item_img_type);
        shareTvContent = (TextView) view.findViewById(R.id.nim_message_item_text_content);
    }

    @Override
    protected void bindContentView() {
        ShowAttachment attachment = ((ShowAttachment) message.getAttachment());
        if (attachment == null) {
            return;
        }

        int width = ScreenUtil.getDisplayWidth() - ScreenUtil.dip2px(52) * 2;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;

        setLayoutParams(width, height, contentContainer);

        shareTvContent.setText(attachment.getDesc());

        if ("3".equals(attachment.getMediaType())) {
            shareImgType.setVisibility(View.VISIBLE);
            shareImgType.setImageDrawable(shareImgType.getResources().getDrawable(R.drawable.ic_video_play));
        } else {
            shareImgType.setVisibility(View.INVISIBLE);
        }

        //FIXME 默认图片 android ios.
        if (!TextUtils.isEmpty(attachment.getImgUrl()) && attachment.getImgUrl().contains("http")) {
            ImageLoader.getInstance().displayImage(attachment.getImgUrl(), shareImgContent, options);
        } else {
            if ("3".equals(attachment.getMediaType())) {
                ImageLoader.getInstance().displayImage(attachment.getImgUrl(), shareImgContent, options);
            } else if ("2".equals(attachment.getMediaType())) {
                ImageLoader.getInstance().displayImage(attachment.getImgUrl(), shareImgContent, options);
            } else if ("4".equals(attachment.getMediaType())) {
                ImageLoader.getInstance().displayImage("drawable://" + R.drawable.ic_share_web, shareImgContent, article_options);
            } else {
                ImageLoader.getInstance().displayImage("drawable://" + R.drawable.pic_opus_default_article, shareImgContent, article_options);
            }
        }

    }

    @Override
    protected void onItemClick() {
        ShowAttachment attachment = ((ShowAttachment) message.getAttachment());
        attachment.checkData();
        String url = "yryz://opus/sharedetail?show_id=" + attachment.getClub_id();
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        view.getContext().startActivity(intent);
    }

    @Override
    protected int rightBackground() {
        return R.drawable.nim_message_right_white_bg;
    }
}
