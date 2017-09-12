package com.rz.httpapi.bean;

import java.util.List;

/**
 * Created by rzw2 on 2017/9/11.
 */

public class CircleBelongBean {
    private List<String> circleIdList;
    private List<CircleEntrModle> circleList;

    public List<String> getCircleIdList() {
        return circleIdList;
    }

    public void setCircleIdList(List<String> circleIdList) {
        this.circleIdList = circleIdList;
    }

    public List<CircleEntrModle> getCircleList() {
        return circleList;
    }

    public void setCircleList(List<CircleEntrModle> circleList) {
        this.circleList = circleList;
    }
}
