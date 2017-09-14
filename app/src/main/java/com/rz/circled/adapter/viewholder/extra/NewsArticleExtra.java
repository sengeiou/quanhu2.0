package com.rz.circled.adapter.viewholder.extra;

/**
 * Created by rzw2 on 2017/9/12.
 */

public class NewsArticleExtra {
    /**
     * 圈子ID
     */
    private String circleId;
    /**
     * 圈子名称
     */
    private String circleName;

    /**
     * 私圈ID
     */
    private String coterieId;

    /**
     * 私圈名称
     */
    private String coterieName;

    /**
     * 相关内容图片
     */
    private String bodyImg;

    /**
     * 相关内容标题
     */
    private String bodyTitle;

    public String getCircleId() {
        return circleId;
    }

    public void setCircleId(String circleId) {
        this.circleId = circleId;
    }

    public String getCircleName() {
        return circleName;
    }

    public void setCircleName(String circleName) {
        this.circleName = circleName;
    }

    public String getCoterieId() {
        return coterieId;
    }

    public void setCoterieId(String coterieId) {
        this.coterieId = coterieId;
    }

    public String getCoterieName() {
        return coterieName;
    }

    public void setCoterieName(String coterieName) {
        this.coterieName = coterieName;
    }

    public String getBodyImg() {
        return bodyImg;
    }

    public void setBodyImg(String bodyImg) {
        this.bodyImg = bodyImg;
    }

    public String getBodyTitle() {
        return bodyTitle;
    }

    public void setBodyTitle(String bodyTitle) {
        this.bodyTitle = bodyTitle;
    }
}
