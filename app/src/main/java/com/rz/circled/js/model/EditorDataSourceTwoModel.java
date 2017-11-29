package com.rz.circled.js.model;

import java.io.Serializable;

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
    //    private ArrayList<Map<String, Object>> contentSource;
    private String contentSource;

    private String coverPlanUrl;
    private String createUserId;
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
    private int allowGeneralizeFlag;
    private int allowShareFlag;
    private int contentPrice;
    private String coterieId;


    private String classifyItemName;

    public int getAllowGeneralizeFlag() {
        return allowGeneralizeFlag;
    }

    public void setAllowGeneralizeFlag(int allowGeneralizeFlag) {
        this.allowGeneralizeFlag = allowGeneralizeFlag;
    }

    public int getAllowShareFlag() {
        return allowShareFlag;
    }

    public void setAllowShareFlag(int allowShareFlag) {
        this.allowShareFlag = allowShareFlag;
    }

    public int getContentPrice() {
        return contentPrice;
    }

    public void setContentPrice(int contentPrice) {
        this.contentPrice = contentPrice;
    }

    public String getCoterieId() {
        return coterieId;
    }

    public void setCoterieId(String coterieId) {
        this.coterieId = coterieId;
    }

    public Long getClassifyItemId() {
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

//    public ArrayList<Map<String, Object>> getContentSource() {
//        return contentSource;
//    }
//
//    public void setContentSource(ArrayList<Map<String, Object>> contentSource) {
//        this.contentSource = contentSource;
//    }


    public String getContentSource() {
        return contentSource;
    }

    public void setContentSource(String contentSource) {
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

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
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
