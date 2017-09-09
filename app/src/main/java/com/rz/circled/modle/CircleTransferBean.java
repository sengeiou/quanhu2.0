package com.rz.circled.modle;

import com.rz.httpapi.bean.CircleDynamic;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/20 0020.
 */

public class CircleTransferBean extends CircleDynamic implements Serializable {

    public int id;//转发id
    public String opusId;//全局作品id

    public long price = 0;
//    replayNum	int	 	跟转数量
//    likeNum	int	 	点赞数量
//    commentNum	int	 	评论数量
//    rewardNum	int	 	打赏数量
//    createTime	String	 	创建时间

    public int replayNum = 0;
    public int replayProfit;
    public int likeNum;
    public int commentNum;
    public int rewardNum;

    public int transferNum;
    public int transferProfit;
    public int parentId;

    public String authorId;

    public int mCommentTotal;//评论条数,传值需要
    public int mZanTotal;//同上

    public int cid;

    public int isTransfer;//0 fou 1shi


    @Override
    public String toString() {
        return "CircleTransferBean{" +
                "id=" + id +
                ", opusId=" + opusId +
                ", price=" + price +
                ", replayNum=" + replayNum +
                ", likeNum=" + likeNum +
                ", commentNum=" + commentNum +
                ", rewardNum=" + rewardNum +
                ", createTime='" + createTime + '\'' +
                ", transferNum=" + transferNum +
                ", transferProfit=" + transferProfit +
                '}';
    }


    public static CircleTransferBean getInstanceFromTransfer(int transferId, TransferModule transferModule) {
        CircleTransferBean CircleTransferBean = new CircleTransferBean();
        CustBean user = new CustBean();
//        user.custId = Session.getUserId();
//        user.custImg = Session.getUserPicUrl();
//        user.custNname = Session.getUserName();
//        user.custPhone = Session.getUserPhone();
//        user.custSignature = Session.getUser_signatrue();
        CircleTransferBean.cust = user;
        CircleTransferBean.id = transferId;
        CircleTransferBean.parentId = transferModule.id;
        CircleTransferBean.authorId = transferModule.authorId;
        CircleTransferBean.circleId = transferModule.appId;
        CircleTransferBean.custId = transferModule.infoId;
        CircleTransferBean.title = transferModule.infoTitle;
        CircleTransferBean.moduleEnum = transferModule.moduleId;
        CircleTransferBean.price = transferModule.price;
        CircleTransferBean.videoPic = transferModule.infoVideoPic;
        CircleTransferBean.video = transferModule.infoVideo;
        CircleTransferBean.pics = transferModule.infoPic;
        CircleTransferBean.thumbnail = transferModule.infoThumbnail;
        CircleTransferBean.content = transferModule.infoDesc;
//        CircleTransferBean.createTime = System.currentTimeMillis() + "";
        CircleTransferBean.circleName = transferModule.circleName;
        CircleTransferBean.transferNum = 0;
        CircleTransferBean.circleRoute = transferModule.circleUrl;
        return CircleTransferBean;
    }


}
