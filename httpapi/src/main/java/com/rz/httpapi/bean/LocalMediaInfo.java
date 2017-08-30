package com.rz.httpapi.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/1 0001.
 */
public class LocalMediaInfo implements Serializable {

    public String filePath;
    public String title;
    public String fileName;
    public long duration;
    public String thumbnails;
    public String endName;
    public long size;
    public String mimeType;

    public String tvDuration;
    public String tvSize;
    public String tvMime;


    @Override
    public String toString() {
        return "LocalMediaInfo{" +
                "filePath='" + filePath + '\'' +
                ", title='" + title + '\'' +
                ", fileName='" + fileName + '\'' +
                ", duration='" + duration + '\'' +
                ", thumbnails='" + thumbnails + '\'' +
                ", endName='" + endName + '\'' +
                ", size='" + size + '\'' +
                ", mimeType='" + mimeType + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LocalMediaInfo that = (LocalMediaInfo) o;

        return !(filePath != null ? !filePath.equals(that.filePath) : that.filePath != null);

    }

    @Override
    public int hashCode() {
        return filePath != null ? filePath.hashCode() : 0;
    }
}
