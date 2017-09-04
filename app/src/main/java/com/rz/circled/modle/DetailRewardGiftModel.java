package com.rz.circled.modle;

import java.io.Serializable;

/**
 * 晒一晒详情礼物model
 */
public class DetailRewardGiftModel implements Serializable {

    //用户id
    public String custId;
    //用户头像
    public String headImg;
    public String custImg;
    public String custNname;
    public String nameNotes;
    public String remark;
    //用户昵称
    public String nickName;
    //打赏礼物的价格
    public String rewardPrice;
    //打赏礼物的id
    public String giftId;
    //打赏礼物的图片地址
    public String img;
    //礼物昵称
    public String giftName;
    //打赏时间
    public String time;


    private boolean isMoreGift;

    public boolean isMoreGift() {
        return isMoreGift;
    }

    public void setMoreGift(boolean moreGift) {
        isMoreGift = moreGift;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getCustImg() {
        return custImg;
    }

    public void setCustImg(String custImg) {
        this.custImg = custImg;
    }

    public String getCustNname() {
        return custNname;
    }

    public void setCustNname(String custNname) {
        this.custNname = custNname;
    }

    public String getNameNotes() {
        return nameNotes;
    }

    public void setNameNotes(String nameNotes) {
        this.nameNotes = nameNotes;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getRewardPrice() {
        return rewardPrice;
    }

    public void setRewardPrice(String rewardPrice) {
        this.rewardPrice = rewardPrice;
    }

    public String getGiftId() {
        return giftId;
    }

    public void setGiftId(String giftId) {
        this.giftId = giftId;
    }

    public String getImage() {
        return img;
    }

    public void setImage(String image) {
        this.img = image;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
