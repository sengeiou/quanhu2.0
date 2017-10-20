package com.rz.circled.adapter.viewholder.extra;

/**
 * Created by rzw2 on 2017/9/12.
 */

public class NewsGroupExtra {
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
     * 圈主名
     */
    private String ownerName;

    /**
     * 圈主简介
     */
    private String ownerIntro;

    /**
     * 私圈简介
     */
    private String intro;

    /**
     * 加入私圈价格
     */
    private int joinFee;

    /**
     * 私圈成员数
     */
    private int memberNum;

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

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getOwnerIntro() {
        return ownerIntro;
    }

    public void setOwnerIntro(String ownerIntro) {
        this.ownerIntro = ownerIntro;
    }

    public int getMemberNum() {
        return memberNum;
    }

    public void setMemberNum(int memberNum) {
        this.memberNum = memberNum;
    }

    public int getJoinFee() {
        return joinFee;
    }

    public void setJoinFee(int joinFee) {
        this.joinFee = joinFee;
    }
}
