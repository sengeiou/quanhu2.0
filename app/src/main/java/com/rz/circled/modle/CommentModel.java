package com.rz.circled.modle;

import java.io.Serializable;

public class CommentModel implements Serializable {

    //评论id
    public String cid;
    //作者id
    public String authorId;
    //作者昵称
    public String authorName;
    //作者备注名
    public String authorNameNote;
    //作者头像
    public String authorHeadImg;

    //用户id
    public String custId;
    //用户昵称
//    public String custName;
//    //用户备注名
//    public String custNameNote;
    public String custNname;

    public String nameNotes;

    //用户头像
    public String custImg;

    //评论内容
    public String content;
    //评论时间
    public String timeStamp;

    private boolean isMoreComment;

    public boolean isMoreComment() {
        return isMoreComment;
    }

    public void setMoreComment(boolean moreComment) {
        isMoreComment = moreComment;
    }
}
