package com.rz.httpapi.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/11/30 0030.
 */
public class TransferDetail {

    public OpusDetail infoDetails;
    public List<ClubDetail> clubDetails;

    @Override
    public String toString() {
        return "TransferDetail{" +
                "infoDetails=" + infoDetails +
                ", clubDetails=" + clubDetails +
                '}';
    }

    public static class ClubDetail {


        public int clubId;
        public String custId;
        public String headImg;

        /**
         * 用户昵称
         */
        public String custNname;

        /**
         * 用户备注
         */
        public String nameNotes;

        /**
         * 用户真实姓名
         */
        public String custName;

        public String lotteryId;

        public String lotteryNumber;
        public String lotteryPeriod;

        public int levelType;

        public int level1ForwardCount;
        public int level2ForwardCount;
        public int level3ForwardCount;
        public long level1ForwardBonud;
        public long level2ForwardBonud;
        public long level3ForwardBonud;

        /**
         * 作品标题
         */
        public String infoTitle;

        /**
         * 作品类型
         */
        public String infoType;

        /**
         * 作品缩略图
         */
        public String infoThumbnail;

        /**
         * 作品描述
         */
        public String infoIntroduction;

        /**
         * 创建时间
         */
        public String createDate;

        /**
         * 发布倒计时
         */
        public String timeDif;

        /**
         * 转发状态 0无1有
         */
        public int forwardStatus;

        public int transferPrice;

        @Override
        public String toString() {
            return "ClubDetail{" +
                    "clubId=" + clubId +
                    ", custId='" + custId + '\'' +
                    ", headImg='" + headImg + '\'' +
                    ", custNname='" + custNname + '\'' +
                    ", nameNotes='" + nameNotes + '\'' +
                    ", custName='" + custName + '\'' +
                    ", lotteryId='" + lotteryId + '\'' +
                    ", lotteryNumber='" + lotteryNumber + '\'' +
                    ", lotteryPeriod='" + lotteryPeriod + '\'' +
                    ", levelType=" + levelType +
                    ", level1ForwardCount=" + level1ForwardCount +
                    ", level2ForwardCount=" + level2ForwardCount +
                    ", level3ForwardCount=" + level3ForwardCount +
                    ", level1ForwardBonud=" + level1ForwardBonud +
                    ", level2ForwardBonud=" + level2ForwardBonud +
                    ", level3ForwardBonud=" + level3ForwardBonud +
                    ", infoTitle='" + infoTitle + '\'' +
                    ", infoType='" + infoType + '\'' +
                    ", infoThumbnail='" + infoThumbnail + '\'' +
                    ", infoIntroduction='" + infoIntroduction + '\'' +
                    ", createDate='" + createDate + '\'' +
                    ", timeDif='" + timeDif + '\'' +
                    ", forwardStatus=" + forwardStatus +
                    '}';
        }
    }
}
