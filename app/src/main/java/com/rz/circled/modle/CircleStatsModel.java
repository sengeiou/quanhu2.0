package com.rz.circled.modle;

/**
 * Created by rzw2 on 2017/4/19.
 */

public class CircleStatsModel {
    //用户id
    private String custId;
    //转发次数
    private int transferNum;
    //被跟转次数
    private int replayNum;
    //转发收益
    private int transferProfit;
    //作品被跟转次数
    private int opusReplayNum;
    //作品收益
    private int opusProfit;

    private int opusNum;

    private int collectionNum;

    private int circleNum;

    @Override
    public String toString() {
        return "CircleStatsModel{" +
                "custId='" + custId + '\'' +
                ", transferNum=" + transferNum +
                ", replayNum=" + replayNum +
                ", transferProfit=" + transferProfit +
                ", opusReplayNum=" + opusReplayNum +
                ", opusProfit=" + opusProfit +
                ", opusNum=" + opusNum +
                ", collectionNum=" + collectionNum +
                ", circleNum=" + circleNum +
                '}';
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public int getTransferNum() {
        return transferNum;
    }

    public void setTransferNum(int transferNum) {
        this.transferNum = transferNum;
    }

    public int getReplayNum() {
        return replayNum;
    }

    public void setReplayNum(int replayNum) {
        this.replayNum = replayNum;
    }

    public int getTransferProfit() {
        return transferProfit;
    }

    public void setTransferProfit(int transferProfit) {
        this.transferProfit = transferProfit;
    }

    public int getOpusReplayNum() {
        return opusReplayNum;
    }

    public void setOpusReplayNum(int opusReplayNum) {
        this.opusReplayNum = opusReplayNum;
    }

    public int getOpusProfit() {
        return opusProfit;
    }

    public void setOpusProfit(int opusProfit) {
        this.opusProfit = opusProfit;
    }

    public int getOpusNum() {
        return opusNum;
    }

    public void setOpusNum(int opusNum) {
        this.opusNum = opusNum;
    }

    public int getCollectionNum() {
        return collectionNum;
    }

    public void setCollectionNum(int collectionNum) {
        this.collectionNum = collectionNum;
    }

    public int getCircleNum() {
        return circleNum;
    }

    public void setCircleNum(int circleNum) {
        this.circleNum = circleNum;
    }
}
