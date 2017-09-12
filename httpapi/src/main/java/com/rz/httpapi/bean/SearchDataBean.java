package com.rz.httpapi.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/9/9 0009.
 */

public class SearchDataBean {

    private List<CircleDynamic> resoueces;              //内容
    private List<StarListBean.CustInfoBean> custInfos;                             //用户
    private List <PrivateGroupBean> coterieInfos;                          //私圈
    private List<CircleEntrModle> circleInfos;                           //圈子
    private List rewards;                               //悬赏


    public List<CircleDynamic> getResoueces() {
        return resoueces;
    }

    public void setResoueces(List<CircleDynamic> resoueces) {
        this.resoueces = resoueces;
    }

    public List getCustInfos() {
        return custInfos;
    }

    public void setCustInfos(List custInfos) {
        this.custInfos = custInfos;
    }

    public List getCoterieInfos() {
        return coterieInfos;
    }

    public void setCoterieInfos(List coterieInfos) {
        this.coterieInfos = coterieInfos;
    }

    public List getCircleInfos() {
        return circleInfos;
    }

    public void setCircleInfos(List circleInfos) {
        this.circleInfos = circleInfos;
    }

    public List getRewards() {
        return rewards;
    }

    public void setRewards(List rewards) {
        this.rewards = rewards;
    }


}
