package com.rz.httpapi.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/18 0018.
 */

public class DataStatisticsBean implements Serializable {

    private String custId;
    private int articleNum;    //文章数量
    private int coterieNum;   //私圈数量
    private int offerNum;       //悬赏数量
    private int score;          //积分
    private String custLevel;

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public int getArticleNum() {
        return articleNum;
    }

    public void setArticleNum(int articleNum) {
        this.articleNum = articleNum;
    }

    public int getCoterieNum() {
        return coterieNum;
    }

    public void setCoterieNum(int coterieNum) {
        this.coterieNum = coterieNum;
    }

    public int getOfferNum() {
        return offerNum;
    }

    public void setOfferNum(int offerNum) {
        this.offerNum = offerNum;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getCustLevel() {
        return custLevel;
    }

    public void setCustLevel(String custLevel) {
        this.custLevel = custLevel;
    }
}
