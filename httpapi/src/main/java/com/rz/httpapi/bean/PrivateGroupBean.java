package com.rz.httpapi.bean;

import java.io.Serializable;

/**
 * Created by rzw2 on 2017/8/31.
 */

public class PrivateGroupBean implements Serializable {

    /**
     * circleId : 圈子id
     * consultingFee : 咨询费，0表示免费
     * coterieId :平台私圈id
     * createDate :创建时间
     * custIcon :用户头像
     * icon :封面图
     * intro :私圈简介
     * joinCheck : 	成员加入是否需要审核（0不审核，1审核）
     * joinFee : 加入私圈金额(悠然币)，0表示免费
     * maxMemberNum : 最大成员数
     * memberNum : 成员数量
     * name :	私圈名称
     * newMemberNum : 新成员数量
     * ownerId :	圈主userid
     * ownerIntro :	圈主简介
     * ownerName :圈主名称
     * qrUrl :	私圈名片(二维码)
     * status : 	状态：0待审核，1审批通过，2审批未通过，3上架，4下架
     */
    private String circleId;
    private String circleName;
    private String circleRoute;
    private int consultingFee;
    private String coterieId;
    private String createDate;
    private String custIcon;
    private String icon;
    private String intro;
    private int joinCheck;
    private int joinFee;
    private int maxMemberNum;
    private int memberNum;
    private String name;
    private int newMemberNum;
    private String ownerId;
    private String ownerIntro;
    private String ownerName;
    private String qrUrl;
    private int status;

    public String getCircleRoute() {
        return circleRoute;
    }

    public void setCircleRoute(String circleRoute) {
        this.circleRoute = circleRoute;
    }

    public String getCircleName() {
        return circleName;
    }

    public void setCircleName(String circleName) {
        this.circleName = circleName;
    }

    public String getCircleId() {
        return circleId;
    }

    public void setCircleId(String circleId) {
        this.circleId = circleId;
    }

    public int getConsultingFee() {
        return consultingFee;
    }

    public void setConsultingFee(int consultingFee) {
        this.consultingFee = consultingFee;
    }

    public String getCoterieId() {
        return coterieId;
    }

    public void setCoterieId(String coterieId) {
        this.coterieId = coterieId;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCustIcon() {
        return custIcon;
    }

    public void setCustIcon(String custIcon) {
        this.custIcon = custIcon;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public int getJoinCheck() {
        return joinCheck;
    }

    public void setJoinCheck(int joinCheck) {
        this.joinCheck = joinCheck;
    }

    public int getJoinFee() {
        return joinFee;
    }

    public void setJoinFee(int joinFee) {
        this.joinFee = joinFee;
    }

    public int getMaxMemberNum() {
        return maxMemberNum;
    }

    public void setMaxMemberNum(int maxMemberNum) {
        this.maxMemberNum = maxMemberNum;
    }

    public int getMemberNum() {
        return memberNum;
    }

    public void setMemberNum(int memberNum) {
        this.memberNum = memberNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNewMemberNum() {
        return newMemberNum;
    }

    public void setNewMemberNum(int newMemberNum) {
        this.newMemberNum = newMemberNum;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerIntro() {
        return ownerIntro;
    }

    public void setOwnerIntro(String ownerIntro) {
        this.ownerIntro = ownerIntro;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getQrUrl() {
        return qrUrl;
    }

    public void setQrUrl(String qrUrl) {
        this.qrUrl = qrUrl;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
