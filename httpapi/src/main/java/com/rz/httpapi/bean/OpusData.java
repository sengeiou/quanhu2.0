package com.rz.httpapi.bean;

/**
 * Created by Administrator on 2016/8/13 0013.
 */
public class OpusData {

//    "uid":"f1948c94c1a94b78b66ecc8ae2b0a997",
//            :   :   :   "headImg":"",
//            :   :   :   "custId":"1",
//            :   :   :   "desc":"雷凯测试",
//            :   :   :   "urlThumbnail":"infoThumbnail",
//            :   :   :   "url":"www.baidu.com",
//            :   :   :   "recommand":0,
//            :   :   :   "type":"1",
//            :   :   :   "transferCount":0,
//            :   :   :   "transferBonud":0,
//            :   :   :   "readStatus":0,
//            :   :   :   "forwardStatus":0,
//            :   :   :   "rewardStatus":0
//    阅读状态	readStatus	int			0无1有
//    转发状态	forwardStatus	int			0无1有
//    打赏状态	rewardStatus	int			0无1有

//    uid
//            headImg
//    custNname
//            custSignature
//    desc
//            transferCount
//    transferBonud
//            custId
//    recommand
//            type
//    url
//            urlThumbnail
//    readStatus
//            forwardStatus
//    rewardStatus
//            createDate


    //作品唯一标识
    public String uid;
    //作者头像
    public String headImg;
    //用户真实姓名
    public String userName;
    //用户的昵称
    public String custNname;
    //签名
    public String custSignature;
    //作者ID
    public String custId;
    //作品描述
    public String desc;

    public String custDesc;
    //缩图图地址
    public String urlThumbnail;
    //Oss文件地址
    public String url;
    //创建时间
    public String createDate;
    //是否推荐
    public int recommand;//0：未推荐1：推荐
    //文件类型 1图文 2视频 3音频
    public int type;
    //转发数
    public int transferCount;
    //作品收益
    public int transferBonud;
    //阅读状态 0无1有
    public int readStatus;
    //转发状态 0无1有
    public int forwardStatus;
    //打赏状态 0无1有
    public int rewardStatus;
    //作品简介
    public String infoIntroduction;

    public int readCount;

    //0:未审核，1:审核中，2:驳回3:通过4:下架
    public int auditStatus;

    public String nameNotes;

    public String authorid;
    public String imageUrl;
    //用来区分搜索类型 0 表示用户 1表示用户信息 2表示文章 3表示文章信息
    public int selfTye;
    //作品时长
    public long infoTime;
    //审核理由
    public String auditReason;
    //最后修改时间
    public String updateDate;

    //点赞状态
    public int goodStatus;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OpusData opusData = (OpusData) o;

        return uid != null ? uid.equals(opusData.uid) : opusData.uid == null;
    }

    @Override
    public int hashCode() {
        int result = uid.hashCode();
        result = 31 * result + custId.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "OpusData{" +
                "uid='" + uid + '\'' +
                ", headImg='" + headImg + '\'' +
                ", custNname='" + custNname + '\'' +
                ", custSignature='" + custSignature + '\'' +
                ", custId='" + custId + '\'' +
                ", desc='" + desc + '\'' +
                ", urlThumbnail='" + urlThumbnail + '\'' +
                ", url='" + url + '\'' +
                ", createDate='" + createDate + '\'' +
                ", recommand=" + recommand +
                ", type=" + type +
                ", transferCount=" + transferCount +
                ", transferBonud=" + transferBonud +
                ", readStatus=" + readStatus +
                ", forwardStatus=" + forwardStatus +
                ", rewardStatus=" + rewardStatus +
                ", authorid='" + authorid + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", selfTye=" + selfTye +
                '}';
    }
}
