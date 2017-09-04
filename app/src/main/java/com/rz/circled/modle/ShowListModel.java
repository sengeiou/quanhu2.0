package com.rz.circled.modle;


import com.rz.common.utils.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wsf on 2016/8/25 0025.
 */
public class ShowListModel implements Serializable {
    //本地回显唯一标示符
    public String publishUUID;
    //晒一晒id
    public int sid;
    //晒一晒正文
    public String content;
    //图文列表
    public List<PicPublishModel> pics;
    //视频图片
    public String videoPic;
    //视频播放地址
    public String videoUrl;
    //发布时间戳
    public String timeStamp;
    //发送时间（正常日期格式）
    public String sendTime;
    //用户id
    public String custId;
    //用户昵称
    public String custNname;
    //用户头像
    public String custImg;
    //用户备注名
    public String nameNotes;
    //是否点赞  0表示未点赞、1表示已经点赞
    public int isLike;
    //点赞数目
    public int likeCount;
    //评论数目
    public String commentCount;
    //打赏礼物数
    public String giftsCount;
    //详见点赞列表
    private List<RateModel> likes;
    //打赏礼物信息
    public List<DetailRewardGiftModel> rewards;
    //详见评论列表
    public List<CommentModel> comments;
    // 0/文字，1/图片， 2/视频 4/悠然广场分享 7/网页分享
    public int type;
    //0正常、1删除、2屏蔽
    public int status;
    //链接地址
    public String link;
    //标题
    public String title;
    //预览内容
    public String preContent;

    public String preImage;

    public String getContent() {
        if(StringUtils.isEmpty(content)){
            content = "";
        }
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<RateModel> getLikes() {
        if (null == likes) {
            likes = new ArrayList<RateModel>();
        }
        return likes;
    }

    public void setLikes(List<RateModel> likes) {
        this.likes = likes;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public List<DetailRewardGiftModel> getGifts() {
        if (null == rewards) {
            rewards = new ArrayList<DetailRewardGiftModel>();
        }
        return rewards;
    }

    public void setGifts(List<DetailRewardGiftModel> gifts) {
        this.rewards = gifts;
    }

    public List<CommentModel> getComments() {
        if (null == comments) {
            comments = new ArrayList<CommentModel>();
        }
        return comments;
    }

    public void setComments(List<CommentModel> comments) {
        this.comments = comments;
    }

    public String getCommentCount() {
        if (StringUtils.isEmpty(commentCount)) {
            return "0";
        }
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public String getNameNotes() {
        return nameNotes;
    }

    public void setNameNotes(String nameNotes) {
        this.nameNotes = nameNotes;
    }

    @Override
    public String toString() {
        return "ShowListModel{" +
                "publishUUID='" + publishUUID + '\'' +
                ", sid=" + sid +
                ", content='" + content + '\'' +
                ", pics=" + pics +
                ", videoPic='" + videoPic + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                ", sendTime='" + sendTime + '\'' +
                ", custId='" + custId + '\'' +
                ", custNname='" + custNname + '\'' +
                ", custImg='" + custImg + '\'' +
                ", nameNotes='" + nameNotes + '\'' +
                ", isLike=" + isLike +
                ", likeCount=" + likeCount +
                ", commentCount='" + commentCount + '\'' +
                ", giftsCount='" + giftsCount + '\'' +
                ", likes=" + likes +
                ", rewards=" + rewards +
                ", comments=" + comments +
                ", type=" + type +
                ", status=" + status +
                ", link='" + link + '\'' +
                ", title='" + title + '\'' +
                ", preContent='" + preContent + '\'' +
                '}';
    }
}
