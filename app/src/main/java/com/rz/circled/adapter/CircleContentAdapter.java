package com.rz.circled.adapter;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rz.circled.R;
import com.rz.circled.application.QHApplication;
import com.rz.circled.widget.CommonAdapter;
import com.rz.circled.widget.GlideCenterRoundImage;
import com.rz.circled.widget.ViewHolder;
import com.rz.common.utils.DensityUtils;
import com.rz.common.utils.ImageAdaptationUtils;
import com.rz.common.utils.StringFormatUtil;
import com.rz.common.utils.TextViewUtils;
import com.rz.httpapi.bean.CircleDynamic;

import java.util.List;


/**
 * Created by Administrator on 2017/4/13 0013.
 */

public abstract class CircleContentAdapter extends CommonAdapter<CircleDynamic> {

    private String keyWord = "";
    public StringFormatUtil stringFormatUtil;

    public void setKeyWord(String keyWord){
        this.keyWord = keyWord;
    }

    public CircleContentAdapter(Context context, List mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
        this.mContext = context;

    }

    public  void bindCircleContent(ViewHolder helper, CircleDynamic item) {
        if (item != null) {
            LinearLayout mll_circleinfo_content = helper.getView(R.id.ll_circle_info_content);//展位图,标题,描述 content
            ImageView mIvThumbnail = helper.getView(R.id.iv_thumbnail);//展位图
            TextView mTitle = helper.getView(R.id.tv_title);//标题
            TextView mdes = helper.getView(R.id.tv_des);//描俗
            ViewGroup mImageLayout = (ViewGroup) helper.getViewById(R.id.layout_image);
            ImageView iv_circle_1img = helper.getView(R.id.iv_circle_1img);//图片,一张大图
            LinearLayout ll_circle_3imgs = helper.getView(R.id.ll_circle_3imgs);//LL,三张小图的线性布局
            ImageView iv_circle_img01 = helper.getView(R.id.iv_circle_img01);//iv 第一张小图
            ImageView iv_circle_img02 = helper.getView(R.id.iv_circle_img02);//iv 第二张小图
            ImageView iv_circle_img03 = helper.getView(R.id.iv_circle_img03);//iv 第三张小图
            RelativeLayout rl_circle_video_content = helper.getView(R.id.rl_circle_video_content);//rl 视频content
            ImageView iv_video_preview = helper.getView(R.id.iv_video_preview);//iv 视频预览大图
            ViewGroup rl_otherinfo_content = helper.getView(R.id.rl_otherinfo_content);//rl 其他信息 content
            int newWidth = (int) (DensityUtils.getScreenW(QHApplication.getContext()) - QHApplication.getContext().getResources().getDimension(R.dimen.px88));
            rl_circle_video_content.getLayoutParams().height = newWidth * 559 / 994;
            rl_circle_video_content.getLayoutParams().width = newWidth;
            rl_circle_video_content.requestLayout();
                mIvThumbnail.setVisibility(View.GONE);
            if (TextUtils.isEmpty(item.thumbnail)) {
            } else {
                rl_circle_video_content.setVisibility(View.GONE);
                ll_circle_3imgs.setVisibility(View.GONE);
                String url = ImageAdaptationUtils.getZoomByWH(QHApplication.getContext(), item.thumbnail,R.dimen.px288,R.dimen.px260);
                helper.setImageByUrlTransger(mIvThumbnail, url, R.drawable.ic_default_thumbnail);
            }

            if(TextUtils.isEmpty(keyWord)){
                mTitle.setText(item.title);
            }else {
                stringFormatUtil = new StringFormatUtil(mContext, item.title, keyWord, R.color.colorAccent).fillColor();
                if(stringFormatUtil != null && stringFormatUtil.getResult() != null){
                    mTitle.setText(stringFormatUtil.getResult());
                }else{
                    mTitle.setText(item.title);
                }
            }

            if (TextUtils.isEmpty(item.content)) {
                mdes.setVisibility(View.GONE);
            } else {
                if (TextUtils.isEmpty(item.title)) {
                    mdes.setVisibility(View.GONE);
                    mTitle.setVisibility(View.VISIBLE);

                    mTitle.setMaxLines(2);

                    stringFormatUtil = new StringFormatUtil(mContext, item.content, keyWord, R.color.colorAccent).fillColor();
                    if(stringFormatUtil != null && stringFormatUtil.getResult() != null){
                        mTitle.setText(stringFormatUtil.getResult());
                    }else{
                        mTitle.setText(item.content);
                    }

//                    mTitle.setText(item.content);
                } else {
                    mdes.setVisibility(View.VISIBLE);
                    String s = item.content.trim().replaceAll("\\t", "");
                    Log.d("yeying", "adapter infoDesc " + s);

//                    StringStaticFormatUtil.fillColor(s, SearchActivity.searchWord,R.color.colorAccent);

                    if(TextUtils.isEmpty(keyWord)){
                        mdes.setText(s);
                    }else{

                        stringFormatUtil = new StringFormatUtil(mContext, s, keyWord, R.color.colorAccent).fillColor();
                        if(stringFormatUtil != null && stringFormatUtil.getResult() != null){
                            mdes.setText(stringFormatUtil.getResult());
                        }else{
                            mdes.setText(s);
                        }
                    }
                }
            }

            //多图区域
            if (TextUtils.isEmpty(item.pics)) {
                mImageLayout.setVisibility(View.GONE);
            } else {
                mImageLayout.setVisibility(View.VISIBLE);
                String[] pics = item.pics.split(",");
                if (pics.length == 1) {
                    iv_circle_1img.setVisibility(View.VISIBLE);
                    ll_circle_3imgs.setVisibility(View.GONE);
                    String url = ImageAdaptationUtils.getZoomByWH(QHApplication.getContext(), item.pics,R.dimen.px994,R.dimen.px558);
//                    helper.setImageByUrlTransger(iv_circle_1img, url, R.drawable.ic_circle_img1);
                    Glide.with(mContext).load(url).transform(new GlideCenterRoundImage(mContext,10)).
                            placeholder(R.drawable.ic_circle_img1).error(R.drawable.ic_circle_img1).into(iv_circle_1img);
                } else {
                    iv_circle_1img.setVisibility(View.GONE);
                    ll_circle_3imgs.setVisibility(View.VISIBLE);
                    iv_circle_img01.setVisibility(View.INVISIBLE);
                    iv_circle_img02.setVisibility(View.INVISIBLE);
                    iv_circle_img03.setVisibility(View.INVISIBLE);
                    ImageView iv = null;
                    for (int i = 0; i < pics.length; i++) {
                        if (i >= 3) {
                            break;
                        }
                        if (i == 0) {
                            iv_circle_img01.setVisibility(View.VISIBLE);
                            iv = iv_circle_img01;
                        } else if (i == 1) {
                            iv_circle_img02.setVisibility(View.VISIBLE);
                            iv = iv_circle_img02;
                        } else if (i == 2) {
                            iv_circle_img03.setVisibility(View.VISIBLE);
                            iv = iv_circle_img03;
                        }
                        String url = ImageAdaptationUtils.getZoomByWH(QHApplication.getContext(), pics[i],R.dimen.px320,R.dimen.px320);
                        Glide.with(mContext).load(url).transform(new GlideCenterRoundImage(mContext,10)).
                                placeholder(R.drawable.ic_circle_img3).error(R.drawable.ic_circle_img3).crossFade().into(iv);
                    }
                }
            }

            if (!TextUtils.isEmpty(item.videoPic)) {
                rl_circle_video_content.setVisibility(View.VISIBLE);
                ll_circle_3imgs.setVisibility(View.GONE);
//                helper.setImageByUrlTransger(iv_video_preview, item.videoPic, R.color.black);
                Glide.with(mContext).load(item.videoPic).transform(new GlideCenterRoundImage(mContext,10)).
                        placeholder(R.color.black).error(R.color.black).into(iv_video_preview);
            } else {
                rl_circle_video_content.setVisibility(View.GONE);
            }

        }
    }



    public static void bindCircleContent(ViewHolder helper, CircleDynamic item, String searchBox) {
        if (item != null) {
            LinearLayout mll_circleinfo_content = helper.getView(R.id.ll_circle_info_content);//展位图,标题,描述 content
            ImageView mIvThumbnail = helper.getView(R.id.iv_thumbnail);//展位图
            TextView mTitle = helper.getView(R.id.tv_title);//标题
            TextView mdes = helper.getView(R.id.tv_des);//描俗
            ViewGroup mImageLayout = (ViewGroup) helper.getViewById(R.id.layout_image);
            ImageView iv_circle_1img = helper.getView(R.id.iv_circle_1img);//图片,一张大图
            LinearLayout ll_circle_3imgs = helper.getView(R.id.ll_circle_3imgs);//LL,三张小图的线性布局
            ImageView iv_circle_img01 = helper.getView(R.id.iv_circle_img01);//iv 第一张小图
            ImageView iv_circle_img02 = helper.getView(R.id.iv_circle_img02);//iv 第二张小图
            ImageView iv_circle_img03 = helper.getView(R.id.iv_circle_img03);//iv 第三张小图
            RelativeLayout rl_circle_video_content = helper.getView(R.id.rl_circle_video_content);//rl 视频content
            ImageView iv_video_preview = helper.getView(R.id.iv_video_preview);//iv 视频预览大图
            ViewGroup rl_otherinfo_content = helper.getView(R.id.rl_otherinfo_content);//rl 其他信息 content
            int newWidth = (int) (DensityUtils.getScreenW(QHApplication.getContext()) - QHApplication.getContext().getResources().getDimension(R.dimen.px88));
            rl_circle_video_content.getLayoutParams().height = newWidth * 559 / 994;
            rl_circle_video_content.getLayoutParams().width = newWidth;
            rl_circle_video_content.requestLayout();

            if (TextUtils.isEmpty(item.thumbnail)) {
                mIvThumbnail.setVisibility(View.GONE);
            } else {
                mIvThumbnail.setVisibility(View.VISIBLE);
                String url = ImageAdaptationUtils.getZoomByWH(QHApplication.getContext(), item.thumbnail,R.dimen.px288,R.dimen.px260);
                helper.setImageByUrlTransger(mIvThumbnail,url, R.drawable.ic_default_thumbnail);
            }
            if (TextUtils.isEmpty(item.title)) {
                mTitle.setVisibility(View.GONE);
            } else {
                mTitle.setVisibility(View.VISIBLE);
                int index = item.title.indexOf(searchBox);
                if (index != -1) {
                    TextViewUtils.setSpannableStyle(item.title, index, searchBox.length() + index, mTitle);
                } else {
                    mTitle.setText(item.title);
                }

            }

            if (TextUtils.isEmpty(item.content)) {
                mdes.setVisibility(View.GONE);
            } else {
                mdes.setVisibility(View.VISIBLE);
                int index = item.content.indexOf(searchBox);
                if (index != -1) {
                    TextViewUtils.setSpannableStyle(item.content, index, searchBox.length() + index, mdes);
                } else {
                    mdes.setText(item.content);
                }
            }

            //多图区域
            if (TextUtils.isEmpty(item.pics)) {
                mImageLayout.setVisibility(View.GONE);
            } else {
                mImageLayout.setVisibility(View.VISIBLE);
                String[] pics = item.pics.split(",");
                if (pics.length == 1) {
                    iv_circle_1img.setVisibility(View.VISIBLE);
                    ll_circle_3imgs.setVisibility(View.GONE);
                    String url = ImageAdaptationUtils.getZoomByWH(QHApplication.getContext(), item.pics,R.dimen.px994,R.dimen.px558);
                    helper.setImageByUrlTransger(iv_circle_1img, url, R.drawable.ic_circle_img1);
                } else {
                    iv_circle_1img.setVisibility(View.GONE);
                    ll_circle_3imgs.setVisibility(View.VISIBLE);
                    iv_circle_img01.setVisibility(View.INVISIBLE);
                    iv_circle_img02.setVisibility(View.INVISIBLE);
                    iv_circle_img03.setVisibility(View.INVISIBLE);
                    ImageView iv = null;
                    for (int i = 0; i < pics.length; i++) {
                        if (i >= 3) {
                            break;
                        }
                        if (i == 0) {
                            iv_circle_img01.setVisibility(View.VISIBLE);
                            iv = iv_circle_img01;
                        } else if (i == 1) {
                            iv_circle_img02.setVisibility(View.VISIBLE);
                            iv = iv_circle_img02;
                        } else if (i == 2) {
                            iv_circle_img03.setVisibility(View.VISIBLE);
                            iv = iv_circle_img03;
                        }
                        String url = ImageAdaptationUtils.getZoomByWH(QHApplication.getContext(), pics[i],R.dimen.px320,R.dimen.px320);
                        helper.setImageByUrlTransger(iv, url, R.drawable.ic_circle_img3);
                    }
                }
            }

            if (!TextUtils.isEmpty(item.videoPic)) {
                rl_circle_video_content.setVisibility(View.VISIBLE);
                ll_circle_3imgs.setVisibility(View.GONE);
                mIvThumbnail.setVisibility(View.GONE);
                helper.setImageByUrlTransger(iv_video_preview, item.videoPic, R.color.black);
            } else {
                rl_circle_video_content.setVisibility(View.GONE);
            }

        }
    }
}
