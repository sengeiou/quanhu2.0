package com.rz.common.utils;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;

/**
 * Created by Administrator on 2016/9/2 0002.
 */
public class FileUtils {

    public static final int SIZETYPE_B = 1;// 获取文件大小单位为B的double值
    public static final int SIZETYPE_KB = 2;// 获取文件大小单位为KB的double值
    public static final int SIZETYPE_M = 3;// 获取文件大小单位为M的double值
    public static final int SIZETYPE_G = 4;// 获取文件大小单位为G的double值

    public static boolean copyFile(String srcFileName, String destFileName,
                                   boolean overlay) {
        File srcFile = new File(srcFileName);

        if (!srcFile.exists()) {
            return false;
        } else if (!srcFile.isFile()) {
            return false;
        }

        File destFile = new File(destFileName);
        if (destFile.exists()) {
            if (overlay) {
                new File(destFileName).delete();
            }
        } else {
            if (!destFile.getParentFile().exists()) {
                if (!destFile.getParentFile().mkdirs()) {
                    return false;
                }
            }
        }

        int byteread = 0;
        InputStream in = null;
        OutputStream out = null;

        try {
            in = new FileInputStream(srcFile);
            out = new FileOutputStream(destFile);
            byte[] buffer = new byte[1024];

            while ((byteread = in.read(buffer)) != -1) {
                out.write(buffer, 0, byteread);
            }
            return true;
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        } finally {
            try {
                if (out != null)
                    out.close();
                if (in != null)
                    in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static double getFileSize(String filePath, int sizeType) {
        File file = new File(filePath);
        if (file.exists()) {
            return formatFileSize(getFilesSize(file), sizeType);
        }
        return 0;
    }

    private static long getFilesSize(File file) {
        if (file.isDirectory()) {
            long fileSize = 0;
            File[] files = file.listFiles();
            for (File currentFile : files) {
                fileSize = fileSize + currentFile.length();
            }
            return fileSize;
        } else return getFileSize(file);
    }

    private static long getFileSize(File file) {
        return file.length();
    }

    private static double formatFileSize(long fileSize, int sizeType) {
        DecimalFormat format = new DecimalFormat("#.00");
        String sizeStr = "0";
        switch (sizeType) {
            case SIZETYPE_B:
                sizeStr = format.format((double) fileSize);
                break;
            case SIZETYPE_KB:
                sizeStr = format.format((double) fileSize / 1024);
                break;
            case SIZETYPE_M:
                sizeStr = format.format((double) fileSize / 1024 / 1024);
                break;
            case SIZETYPE_G:
                sizeStr = format.format((double) fileSize / 1024 / 1024 / 1024);
                break;
            default:
                break;
        }
        return Double.valueOf(sizeStr);
    }

    /**
     * 检查sd卡是否可用
     *
     * @return
     */
    public static boolean checkSDCardAvaliable() {

        if (Environment.getExternalStorageState() == Environment.MEDIA_REMOVED) {

            return false;
        }

        return true;

    }

}
