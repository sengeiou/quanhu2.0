package com.rz.httpapi.bean;

/**
 * Created by Administrator on 2017/4/20 0020.
 */

public class TransferDetailBean {

    public long id;
    public long parentId;
    public int type;
    public String authorId;
    public String infoTitile;
    public String smallPic;
    public String infoPath;
    public long price;
    public int replayNum;
    public long replayProfit;
    public String createTime;
    public CircleDynamic.CustBean user;

    public String appId;
    //圈子ID
    public String circleName;
    //所属圈子名称（未完成）
    public String infoId;//信息Id

    public String moduleId;
    public String infoTitle;
    // 信息标题
    public String infoDesc;    //信息简介
    public String infoThumbnail;    //信息缩略图
    public String infoPic;    //图片列表，以逗号分隔
    public String infoVideo;    //视频地址
    public String infoVideoPic;//视频预览图片
    public String circleUrl;    //详情跳转地址，不带域名地址格式
    public String reateTime;    //创建时间

    public int isLike;
    public int isTransfer;

    public int mCommentTotal;//评论条数,传值需要
    public int mZanTotal;//同上


//
//            appId
//    String
//
//            圈子ID
//    moduleId	String	 	栏目ID
//    infoId	String	 	信息Id
//    opusId	String	 	全局作品ID
//    circleName	String	 	圈子名称
//    circleUrl	String	 	圈子主页地址
//
//            authorId
//    String
//
//            作者id
//    infoTitle
//            String
//
//    作品标题
//            smallPic
//    String
//
//            缩略图
//    infoDesc
//            String
//
//    简介
//            infoPath
//    String
//
//            主体内容路径
//    price
//    int
//
//            转发价格
//    replayNum
//    int
//
//            被跟转次数
//    replayProfit
//    int
//
//            分费收益
//
//    isLike	int	 	0未点赞、1已点赞
//    isTransfer	int	 	0未跟转、1已跟转
//            createTime
//    long
//
//            转发时间



    public static class User {
        public String custId;
        public String custNname;
        public String custImg;
        public String custNameNote;

        @Override
        public String toString() {
            return "User{" +
                    "custId='" + custId + '\'' +
                    ", custNname='" + custNname + '\'' +
                    ", custImg='" + custImg + '\'' +
                    ", custNameNote='" + custNameNote + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "V3TransferDetail{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", type=" + type +
                ", authorId='" + authorId + '\'' +
                ", infoTitile='" + infoTitile + '\'' +
                ", infoDesc='" + infoDesc + '\'' +
                ", smallPic='" + smallPic + '\'' +
                ", infoPath='" + infoPath + '\'' +
                ", price=" + price +
                ", replayNum=" + replayNum +
                ", replayProfit=" + replayProfit +
                ", createTIme=" + createTime +
                ", cust=" + user +
                '}';
    }
}
