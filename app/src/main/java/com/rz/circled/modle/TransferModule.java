package com.rz.circled.modle;

import com.rz.httpapi.bean.TransferDetailBean;

import java.io.Serializable;

/**
 * Created by Gsm on 2017/9/4.
 */
public class TransferModule implements Serializable {
    public String custId;//*
    public String parentId;
    public String authorId;//*
    public String appId;//*
    public String moduleId;//*
    public String infoId;
    public String infoTitle;
    public String infoDesc;
    public String infoThumbnail;
    public String infoPic;
    public String infoVideo;
    public String infoVideoPic;
    public long price;//*
    public String custNname;
    public int id;
    public String opusId;
    public String circleName;
    public String circleUrl;
    public String infoCreateTime;


    @Override
    public String toString() {
        return "TransferModule{" +
                "custId='" + custId + '\'' +
                ", parentId='" + parentId + '\'' +
                ", authorId='" + authorId + '\'' +
                ", appId='" + appId + '\'' +
                ", moduleId='" + moduleId + '\'' +
                ", infoId='" + infoId + '\'' +
                ", infoTitle='" + infoTitle + '\'' +
                ", infoDesc='" + infoDesc + '\'' +
                ", infoThumbnail='" + infoThumbnail + '\'' +
                ", infoPic='" + infoPic + '\'' +
                ", infoVideo='" + infoVideo + '\'' +
                ", infoVideoPic='" + infoVideoPic + '\'' +
                ", price=" + price +
                ", custNname='" + custNname + '\'' +
                ", id=" + id +
                ", opusId='" + opusId + '\'' +
                '}';
    }

    public static TransferModule getInstance(CircleTransferBean v3CircleTransfer) {
        TransferModule transferModule = new TransferModule();
        transferModule.appId = v3CircleTransfer.circleId;
        if (v3CircleTransfer.cust != null) {
            transferModule.custNname = v3CircleTransfer.cust.custNname;
        }
        transferModule.authorId = v3CircleTransfer.authorId;
        transferModule.infoDesc = v3CircleTransfer.content;
        transferModule.infoId = v3CircleTransfer.custId;
        transferModule.infoPic = v3CircleTransfer.pics;
        transferModule.infoThumbnail = v3CircleTransfer.thumbnail;
        transferModule.infoVideo = v3CircleTransfer.video;
        transferModule.infoVideoPic = v3CircleTransfer.videoPic;
        transferModule.moduleId = v3CircleTransfer.moduleEnum;
        transferModule.parentId = v3CircleTransfer.id == 0 ? null : v3CircleTransfer.id + "";
        transferModule.price = v3CircleTransfer.price;
        transferModule.infoTitle = v3CircleTransfer.title;
        transferModule.id = v3CircleTransfer.id;
        transferModule.opusId = v3CircleTransfer.opusId;
        transferModule.circleName = v3CircleTransfer.circleName;
        transferModule.circleUrl = v3CircleTransfer.circleRoute;
        return transferModule;
    }

    public static TransferModule getInstance(TransferDetailBean transferDetail) {
        TransferModule transferModule = new TransferModule();
        transferModule.appId = transferDetail.appId;
        if (transferDetail.user != null) {
            transferModule.custNname = transferDetail.user.custNname;
        }
        transferModule.authorId = transferDetail.authorId;
        transferModule.infoDesc = transferDetail.infoDesc;
        transferModule.infoId = transferDetail.infoId;
        transferModule.infoPic = transferDetail.infoPic;
        transferModule.infoThumbnail = transferDetail.infoThumbnail;
        transferModule.infoVideo = transferDetail.infoVideo;
        transferModule.infoVideoPic = transferDetail.infoVideoPic;
        transferModule.moduleId = transferDetail.moduleId;
        transferModule.parentId = transferDetail.id == 0 ? null : transferDetail.id + "";
        transferModule.price = transferDetail.price;
        transferModule.infoTitle = transferDetail.infoTitle;
        transferModule.id = (int) transferDetail.id;
        transferModule.circleName = transferDetail.circleName;
        transferModule.circleUrl = transferDetail.circleUrl;
        return transferModule;
    }

}
