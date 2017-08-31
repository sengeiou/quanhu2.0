package com.rz.circled.js.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by KF on 2017/5/31.
 */
public class EditorDataSourceTwoModel implements Serializable {
    private String address;
    private String audioUrl;
    private String city;
    private Long classifyId;
    private Long classifyItemId = null;
    private String content;
    private ArrayList<Map<String, Object>> contentSource;
    private String coverPlanUrl;
    private int createUserId;
    private String date;
    private String description;
    private int functionType = 0;//业务功能类型（1：匿名，2：投票）
    private String imgUrl;
    private String label;
    private String moduleEnum;
    private String province;
    private String title;
    private String videoThumbnailUrl;
    private String videoUrl;


    private String classifyItemName;


    public long getClassifyItemId() {
        return classifyItemId;
    }

    public void setClassifyItemId(long classifyItemId) {
        this.classifyItemId = classifyItemId;
    }

    public String getClassifyItemName() {
        return classifyItemName;
    }

    public void setClassifyItemName(String classifyItemName) {
        this.classifyItemName = classifyItemName;
    }

    public String getCoverPlanUrl() {
        return coverPlanUrl;
    }

    public void setCoverPlanUrl(String coverPlanUrl) {
        this.coverPlanUrl = coverPlanUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getFunctionType() {
        return functionType;
    }

    public void setFunctionType(int functionType) {
        this.functionType = functionType;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<Map<String, Object>> getContentSource() {
        return contentSource;
    }

    public void setContentSource(ArrayList<Map<String, Object>> contentSource) {
        this.contentSource = contentSource;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public Long getClassifyId() {
        return classifyId;
    }

    public void setClassifyId(Long classifyId) {
        this.classifyId = classifyId;
    }

    public void setClassifyItemId(Long classifyItemId) {
        this.classifyItemId = classifyItemId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(int createUserId) {
        this.createUserId = createUserId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getModuleEnum() {
        return moduleEnum;
    }

    public void setModuleEnum(String moduleEnum) {
        this.moduleEnum = moduleEnum;
    }

    public String getVideoThumbnailUrl() {
        return videoThumbnailUrl;
    }

    public void setVideoThumbnailUrl(String videoThumbnailUrl) {
        this.videoThumbnailUrl = videoThumbnailUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
