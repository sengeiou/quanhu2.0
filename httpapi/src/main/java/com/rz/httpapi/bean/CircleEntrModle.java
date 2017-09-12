package com.rz.httpapi.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/13 0013.
 */

public class CircleEntrModle implements Serializable{

//
//    “appId”:””,
//            “circleName”:””,
//            “circleIcon”:””,
//            “circleIntro”:””,
//            “status”:0,
//            “createTime”:5412211
//}]

    public String appId;
    public String circleName;
    public String circleIcon;
    public String circleIntro;
    public String circleUrl;
    public int status;
    public long createTime;
    public int isRecommend;
    private boolean isSeleced;
    public int click;
    public int type;

    public boolean isSeleced() {
        return isSeleced;
    }

    public void setSeleced(boolean seleced) {
        isSeleced = seleced;
    }
}
