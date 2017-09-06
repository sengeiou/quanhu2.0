package com.rz.httpapi.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/8/22 0022.
 */
public class OpusDetail implements Serializable {
//    唯一标识	uid
//    作者名称	author
//    作者头像	headImg
//    作品描述	desc
//    Oss文件地址	urls
//    作品ID	authorid
//    是否系统推荐	recommand 	0否1是
//    是否收藏	isCollect  	0否1是
//    评论+回复总数	commentBonud
//    赞总数	likeCount
//    打赏总数	rewardCount


//    唯一标识	uid	varchar
//    作者名称	custNname	varchar
//    作者头像	headImg	varchar
//    作品描述	desc	varchar
//    用户签名	custSignature	varchar
//    用户ID	custId	varchar
//    简介	infoIntroduction	varchar
//    缩略图路径	urlThumbnail	varchar
//    作品地址	url	varchar
//    推荐状态	recommand	int
//    type	varchar
//    发布时间	createDate	date
//    收藏状态	collectStatus	int
//    阅读数	readCount	int
//    转发状态	forwardStatus	int
//    转发数	forwardCount	int
//    打赏状态	rewardStatus	int
//    打赏数	rewardCount	int
//    评论状态	commentStatus	int
//    评论数	commentCount	int
//    点赞状态	goodStatus	int
//    点赞数	goodCount	int


//
//    唯一标识	uid	varchar
//    作者名称	custNname	varchar
//    作者头像	headImg	varchar
//    作品描述	desc	varchar
//    用户签名	custSignature	varchar
//    用户ID	custId	varchar
//    简介	infoIntroduction	varchar
//    缩略图路径	urlThumbnail	varchar
//    作品地址	url	varchar
//    推荐状态	recommand	int
//    type	varchar
//    发布时间	createDate	date
//    收藏状态	collectStatus	int
//    阅读数	readCount	int
//    转发状态	forwardStatus	int
//    转发数	forwardCount	int
//    打赏状态	rewardStatus	int
//    打赏数	rewardCount	int
//    评论状态	commentStatus	int
//    评论数	commentCount	int
//    点赞状态	goodStatus	int
//    点赞数	goodCount	int
//    作品时长	infoTime	int
//    标签列表	tageList	list


    public String uid;
    public String custNname;
    public String custDesc;
    public String headImg;
    public String desc;
    public String custId;
    public String custSignature;
    public String infoIntroduction;
    public String urlThumbnail;
    public String url;
    public int recommand;
    public String type;
    public String createDate;
    public int collectStatus;
    public int readCount;
    public int readStatus;
    public int forwardStatus;
    public int forwardCount;
    public int forward1Count;
    public int forward2Count;
    public int forward3Count;
    public int rewardStatus;
    public int rewardCount;
    public int commentStatus;
    public int commentCount;
    public int goodStatus;
    public int goodCount;
    public long transferBonud;
    public long infoTime;
    public int redpacketStatus;
    public String redpacketId;
    public int infoForwardStatus;
    public List<OpusTag> tageList;
    public String nameNotes;
    public int auditStatus;
    //转发单价
    public int transferPrice;

    public int isTransfer;


//    public static class Level{
//
////        转发人id	custId	varchar
////        转发人签名	custSignature	varchar
////        转发人昵称	custNname	varchar
////        转发人头像	headImg	varchar
////        转发时间	createTime	long
//
//        public String custId;
//        public String custSignature;
//        public String custNname;
//        public String headImg;
//        public String createTime;
//
//        @Override
//        public String toString() {
//            return "Level{" +
//                    "custId='" + custId + '\'' +
//                    ", custSignature='" + custSignature + '\'' +
//                    ", custNname='" + custNname + '\'' +
//                    ", headImg='" + headImg + '\'' +
//                    ", createTime='" + createTime + '\'' +
//                    '}';
//        }
//    }


    @Override
    public String toString() {
        return "OpusDetail{" +
                "uid='" + uid + '\'' +
                ", custNname='" + custNname + '\'' +
                ", headImg='" + headImg + '\'' +
                ", desc='" + desc + '\'' +
                ", custId='" + custId + '\'' +
                ", custSignature='" + custSignature + '\'' +
                ", infoIntroduction='" + infoIntroduction + '\'' +
                ", urlThumbnail='" + urlThumbnail + '\'' +
                ", url='" + url + '\'' +
                ", recommand=" + recommand +
                ", type='" + type + '\'' +
                ", createDate='" + createDate + '\'' +
                ", collectStatus=" + collectStatus +
                ", readCount=" + readCount +
                ", forwardStatus=" + forwardStatus +
                ", forwardCount=" + forwardCount +
                ", forward1Count=" + forward1Count +
                ", forward2Count=" + forward2Count +
                ", forward3Count=" + forward3Count +
                ", rewardStatus=" + rewardStatus +
                ", rewardCount=" + rewardCount +
                ", commentStatus=" + commentStatus +
                ", commentCount=" + commentCount +
                ", goodStatus=" + goodStatus +
                ", goodCount=" + goodCount +
                ", transferBonud=" + transferBonud +
                ", infoTime=" + infoTime +
                ", redpacketStatus=" + redpacketStatus +
                ", infoForwardStatus=" + infoForwardStatus +
                ", tageList=" + tageList +
                '}';
    }
}
