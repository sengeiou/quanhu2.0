package com.yryz.yunxinim.file.browser;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.TextView;

import com.yryz.yunxinim.DemoCache;
import com.yryz.yunxinim.R;
import com.yryz.yunxinim.file.browser.FileBrowserAdapter.FileManagerItem;
import com.yryz.yunxinim.uikit.common.adapter.TViewHolder;

import java.io.File;

/**
 * Created by hzxuwen on 2015/4/17.
 */
public class FileBrowserViewHolder extends TViewHolder{
    private ImageView fileImage;
    private TextView fileName;
    private FileManagerItem fileItem;

    private Bitmap directoryBitmap;
    private Bitmap fileBitmap;

    @Override
    protected int getResId() {
        return R.layout.file_browser_list_item;
    }

    @Override
    protected void inflate() {
        directoryBitmap = BitmapFactory.decodeResource(DemoCache.getContext().getResources(),R.drawable.directory);
        fileBitmap = BitmapFactory.decodeResource(DemoCache.getContext().getResources(), R.drawable.file);
        fileImage = (ImageView) view.findViewById(R.id.file_image);
        fileName = (TextView) view.findViewById(R.id.file_name);
    }

    @Override
    protected void refresh(Object item) {
        fileItem = (FileManagerItem) item;

        File f = new File(fileItem.getPath());
        if(fileItem.getName().equals("@1")) {
            fileName.setText("/");
            fileImage.setImageBitmap(directoryBitmap);
        } else if(fileItem.getName().equals("@2")) {
            fileName.setText("..");
            fileImage.setImageBitmap(directoryBitmap);
        } else {
            fileName.setText(fileItem.getName());
            if(f.isDirectory()) {
                fileImage.setImageBitmap(directoryBitmap);
            } else if (f.isFile()) {
                fileImage.setImageBitmap(fileBitmap);
            }
        }

    }
}
