package com.rz.common.cache;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class BitmapMemoryCache {
    private LruCache<String, Bitmap> mBitmapCache;
    private ArrayList<String> mCurrentTasks;

    private static Executor executor = Executors.newFixedThreadPool(5);

    public BitmapMemoryCache(int size) {

        mBitmapCache = new LruCache<String, Bitmap>(size) {
            @Override
            protected int sizeOf(String key, Bitmap b) {
                // Assuming that one pixel contains four bytes.
                return b.getHeight() * b.getWidth() * 4;
            }
        };

        mCurrentTasks = new ArrayList<String>();
    }

    private void addBitmapToCache(String key, Bitmap bitmap) {
        if (getBitmapFromCache(key) == null) {
            mBitmapCache.put(key, bitmap);
        }
    }

    private Bitmap getBitmapFromCache(String key) {
        return mBitmapCache.get(key);
    }

    public void loadBitmap(String imageKey, ImageView imageView) {
        final Bitmap bitmap = getBitmapFromCache(imageKey);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
//            imageView.setImageResource(R.drawable.ic_height_bg);
            BitmapLoaderTask task = new BitmapLoaderTask(imageKey, imageView);
            task.executeOnExecutor(executor);

//            if (!isScrolling && !mCurrentTasks.contains(imageKey)) {
//            }
        }
    }

    private class BitmapLoaderTask extends AsyncTask<Void, Void, Bitmap> {
        private String mImageKey;
        private ImageView iv;

        public BitmapLoaderTask(String imageKey, ImageView iv) {
            mImageKey = imageKey;
            this.iv = iv;
        }

        @Override
        protected void onPreExecute() {
            mCurrentTasks.add(mImageKey);
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            Bitmap bitmap = null;
            try {
                bitmap = ThumbnailUtils.createVideoThumbnail(mImageKey, MediaStore.Images.Thumbnails.MICRO_KIND);
                if (bitmap != null) {
//                    bitmap = Bitmap.createScaledBitmap(bitmap, mMaxWidth, mMaxWidth, false);
                    addBitmapToCache(mImageKey, bitmap);
                    return bitmap;
                }
                return null;
            } catch (Exception e) {
                if (e != null) {
                    e.printStackTrace();
                }
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap param) {
            mCurrentTasks.remove(mImageKey);
            if (iv.getTag() != null) {
                if (iv.getTag().equals(mImageKey)) {
                    iv.setImageBitmap(param);
                }
            }
        }
    }


}
