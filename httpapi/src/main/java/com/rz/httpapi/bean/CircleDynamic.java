package com.rz.httpapi.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/13 0013.
 */

public class CircleDynamic implements Serializable {


    /**
     * audio : audio
     * circleId : t2uvj7cmjh8i
     * circleName : 创业达人圈
     * circleRoute : cyq
     * cityCode : 110100
     * completeTime : 85848
     * content : content5
     * coterieId : coterieId
     * coterieName : 测试内容e1w5
     * createTime : 68756
     * cust : {"custDesc":1,"custId":"测试内容ommi","custImg":1,"custLevel":1,"custName":1,"custRole":1,"custSex":1,"custSignature":1,"nameNotes":1}
     * custId : yehao-test
     * custImg : 测试内容m66e
     * custNname : 测试内容6r8d
     * extjson : {"avtiviAddress":"湖北省武汉市洪山区"}
     * gps : 114.00265,36.325321
     * heat : 0
     * moduleEnum : 300-1001
     * orderby : 4
     * ownerIntro : 1
     * ownerName : 测试内容8462
     * partNum : 0
     * pics : https://www.baidu.com/logo.png,https://www.baidu.com/logo.png
     * readNum : 0
     * resourceId : 19441761d7ff48b0bd9930001617a0a3
     * resourceTag : 分类
     * resourceType : 1001
     * summary : summary5
     * talentType : 0
     * thumbnail : https://www.baidu.com/logo.png
     * title : title
     * updateTime : 18770
     * video : https://www.baidu.com/video.mp4
     * videoPic : https://www.baidu.com/logo.png
     */

    public String audio;
    public String circleId;
    public String circleName;
    public String circleRoute;
    public String cityCode;
    public long completeTime;
    public String content;
    public String coterieId;
    public String coterieName;
    public long createTime;
    public CustBean cust;
    public String custId;
    public String custImg;
    public String custNname;
    public String extjson;
    public String gps;
    public int heat;
    public String moduleEnum;
    public int orderby;
    public String ownerIntro;
    public String ownerName;
    public int partNum;
    public String pics;
    public int readNum;
    public String resourceId;
    public String resourceTag;
    public String resourceType;
    public String summary;
    public int talentType;
    public String thumbnail;
    public String title;
    public long updateTime;
    public String video;
    public String videoPic;

    public static class CustBean {
        /**
         * custDesc : 1
         * custId : 测试内容ommi
         * custImg : 1
         * custLevel : 1
         * custName : 1
         * custRole : 1
         * custSex : 1
         * custSignature : 1
         * nameNotes : 1
         */

        public String custDesc;
        public String custId;
        public String custImg;
        public int custLevel;
        public String custNname;
        public int custRole;
        public int custSex;
        public String custSignature;
        public String nameNotes;

    }
}
