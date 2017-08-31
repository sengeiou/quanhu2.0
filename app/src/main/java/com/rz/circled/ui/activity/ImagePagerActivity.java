package com.rz.circled.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;
import com.rz.circled.R;
import com.rz.circled.widget.MyViewPager;
import com.rz.circled.widget.photoview.IPhotoSaveListener;
import com.rz.circled.widget.photoview.PhotoView;
import com.rz.circled.widget.photoview.PhotoViewAttacher;
import com.rz.circled.widget.photoview.PicSaveDialog;
import com.rz.common.utils.ImageUtils;
import com.rz.httpapi.bean.PicModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by wsf on 2016/8/17.
 */
public class ImagePagerActivity extends Activity {
    public static final String INTENT_IMGURLS = "imgurls";
    public static final String INTENT_POSITION = "position";
    private List<View> guideViewList = new ArrayList<View>();
    private LinearLayout guideGroup;
    public static ImageSize imageSize;

    /**
     * 是否是本地图片
     */
    public static boolean isLocation;

    public static List<PicModel> mPics = new ArrayList<PicModel>();

    public static void startImagePagerActivity(Context context,
                                               List<String> imgUrls, int position) {
        Intent intent = new Intent(context, ImagePagerActivity.class);
        intent.putStringArrayListExtra(INTENT_IMGURLS, new ArrayList<String>(
                imgUrls));
        intent.putExtra(INTENT_POSITION, position);
        context.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        Intent intent = new Intent();
        if (mPics.isEmpty()) {
            intent.putExtra("num", 0);
        } else {
            intent.putExtra("num", 1);
            List<String> mlist = new ArrayList<String>();
            for (PicModel model : mPics) {
                if (!model.isSelect()) {
                    mlist.add(model.getPicPath());
                }
            }
            intent.putStringArrayListExtra("listPic", (ArrayList<String>) mlist);
        }
        setResult(1, intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagepager);
        MyViewPager viewPager = (MyViewPager) findViewById(R.id.pager);
        guideGroup = (LinearLayout) findViewById(R.id.guideGroup);

        int startPos = getIntent().getIntExtra(INTENT_POSITION, 0);
        ArrayList<String> imgUrls = getIntent().getStringArrayListExtra(
                INTENT_IMGURLS);
        if (imgUrls == null || imgUrls.size() == 0)
            return;
        ImageAdapter mAdapter = new ImageAdapter(this);
        mAdapter.setDatas(imgUrls);
        viewPager.setAdapter(mAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < guideViewList.size(); i++) {
                    guideViewList.get(i).setSelected(
                            i == position ? true : false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(startPos);

        addGuideView(guideGroup, startPos, imgUrls);
    }

    private void addGuideView(LinearLayout guideGroup, int startPos,
                              ArrayList<String> imgUrls) {
        if (imgUrls != null && imgUrls.size() > 0) {
            guideViewList.clear();
            for (int i = 0; i < imgUrls.size(); i++) {
                View view = new View(this);
                view.setBackgroundResource(R.drawable.selector_guide_bg);
                view.setSelected(i == startPos ? true : false);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        getResources().getDimensionPixelSize(R.dimen.gudieview_width), getResources()
                        .getDimensionPixelSize(R.dimen.gudieview_heigh));
                layoutParams.setMargins(10, 0, 0, 0);
                guideGroup.addView(view, layoutParams);
                guideViewList.add(view);
            }
        }
    }

    private class ImageAdapter extends PagerAdapter {

        private List<String> datas = new ArrayList<String>();
        private LayoutInflater inflater;
        private DisplayImageOptions options;
        private Context context;

        public void setDatas(List<String> datas) {
            if (datas != null)
                this.datas = datas;
            mPics.clear();
            for (String path : datas) {
                PicModel model = new PicModel();
                model.setPicPath(path);
                model.setSelect(false);
                mPics.add(model);
            }
        }

        public ImageAdapter(Context context) {
            this.context = context;
            this.inflater = LayoutInflater.from(context);
            options = new DisplayImageOptions.Builder()
                    .showImageForEmptyUri(R.mipmap.ic_height_bg)
                    .showImageOnFail(R.mipmap.ic_height_bg)
                    .resetViewBeforeLoading(true).cacheOnDisk(true)
                    .imageScaleType(ImageScaleType.NONE)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .considerExifParams(true)
                    .displayer(new FadeInBitmapDisplayer(300)).build();
        }

        @Override
        public int getCount() {
            if (datas == null)
                return 0;
            return datas.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View view = inflater.inflate(R.layout.item_pager_image, container,
                    false);
            if (view != null) {
                final PhotoView imageView = (PhotoView) view
                        .findViewById(R.id.image);

                imageView.setOnLongClickListener(new OnLongClickListener() {

                    @Override
                    public boolean onLongClick(View arg0) {
                        PicSaveDialog cd = new PicSaveDialog(
                                ImagePagerActivity.this,
                                new IPhotoSaveListener() {

                                    @Override
                                    public void onPhotoSave() { //

                                        ImageLoader
                                                .getInstance()
                                                .loadImage(
                                                        datas.get(position),
                                                        options,
                                                        new SimpleImageLoadingListener() {

                                                            @Override
                                                            public void onLoadingStarted(
                                                                    String imageUri,
                                                                    View view) {
                                                            }

                                                            @Override
                                                            public void onLoadingComplete(
                                                                    String imageUri,
                                                                    View view,
                                                                    Bitmap loadedImage) {
                                                                try {
                                                                    ImageUtils
                                                                            .saveFile(
                                                                                    loadedImage,
                                                                                    datas.get(position),
                                                                                    context);
                                                                } catch (IOException e) { // TODO
                                                                    // //
                                                                    // Auto-generated
                                                                    // //
                                                                    // catch
                                                                    // //
                                                                    // block
                                                                    e.printStackTrace();
                                                                }
                                                            }

                                                            @Override
                                                            public void onLoadingFailed(
                                                                    String imageUri,
                                                                    View view,
                                                                    FailReason failReason) {
                                                                super.onLoadingFailed(
                                                                        imageUri,
                                                                        view,
                                                                        failReason);
                                                            }
                                                        });
                                        /*ImageUtils.saveFile(bm, fileName,
                                                context);*/
                                    }
                                });
                        cd.show();
                        return false;
                    }
                });

                imageView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {

                    @Override
                    public void onPhotoTap(View arg0, float arg1, float arg2) {
                        finish();
                    }
                });

                // 预览imageView
                final ImageView smallImageView = new ImageView(context);
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                        imageSize.getWidth(), imageSize.getHeight());
                layoutParams.gravity = Gravity.CENTER;
                smallImageView.setLayoutParams(layoutParams);
                smallImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                ((FrameLayout) view).addView(smallImageView);

                if (isLocation) {
//                    final PicModel model = mPics.get(position);
//                    // 勾选
//                    final CheckBox mBox = new CheckBox(context);
//                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
//                            100, 100);
//                    params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
//                    params.topMargin = 30;
//                    params.bottomMargin = 100;
//                    mBox.setText("删除");
//                    mBox.setTextColor(context.getResources().getColor(
//                            R.color.white));
//                    mBox.setTextSize(DensityUtils.dip2px(context, 100));
//                    mBox.setChecked(model.isSelect());
//                    mBox.setLayoutParams(params);
//                    ((FrameLayout) view).addView(mBox);
//
//                    mBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//                        @Override
//                        public void onCheckedChanged(CompoundButton buttonView,
//                                                     boolean isChecked) {
//                            model.setSelect(isChecked);
//                        }
//                    });

//                    com.rz.rz_rrz.view.ImageLoader.getInstance(3, Type.LIFO)
//                            .loadImage(datas.get(position), imageView);
                } else {
                    // loading
                    final ProgressBar loading = new ProgressBar(context);
                    FrameLayout.LayoutParams loadingLayoutParams = new FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    loadingLayoutParams.gravity = Gravity.CENTER;
                    loading.setLayoutParams(loadingLayoutParams);
                    ((FrameLayout) view).addView(loading);
                    /*final String imgurl = GlobalConstants.URL_FLIE_NET
                            + datas.get(position);*/

                    String imgurl = datas.get(position);
                    if (!TextUtils.isEmpty(imgurl) && !imgurl.contains("http")) {
                        imgurl = "file://" + imgurl;
                    }
                    ImageLoader.getInstance().displayImage(imgurl, imageView,
                            options, new SimpleImageLoadingListener() {
                                @Override
                                public void onLoadingStarted(String imageUri,
                                                             View view) {
                                    // 获取内存中的缩略图
                                    String memoryCacheKey = MemoryCacheUtils
                                            .generateKey(imageUri, imageSize);
                                    Bitmap bmp = ImageLoader.getInstance()
                                            .getMemoryCache()
                                            .get(memoryCacheKey);
                                    if (bmp != null && !bmp.isRecycled()) {
                                        smallImageView
                                                .setVisibility(View.VISIBLE);
                                        smallImageView.setImageBitmap(bmp);
                                    }
                                    loading.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onLoadingComplete(String imageUri,
                                                              View view, Bitmap loadedImage) {
                                    loading.setVisibility(View.GONE);
                                    smallImageView.setVisibility(View.GONE);
                                }

                                @Override
                                public void onLoadingFailed(String imageUri,
                                                            View view, FailReason failReason) {
                                    super.onLoadingFailed(imageUri, view,
                                            failReason);
                                }
                            });
                }
                container.addView(view, 0);
            }
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

    }
}
