package com.rz.httpapi.bean;

/**
 * Created by Administrator on 2016/9/28 0028.
 */
public class ClubStats {

//    " level1Count ":"0",
//            :   :   " level2Count ":"0",
//            :   :   " level3Count ":"0",
//            :   :   " level1Bonud":"0",
//            :   :   " level2Bonud":"0",
//            :   :   " level3Bonud":"0"

    public int level1Count;
    public int level2Count;
    public int level3Count;
    public long level1Bonud;
    public long level2Bonud;
    public long level3Bonud;

    @Override
    public String toString() {
        return "ClubStats{" +
                "level1Count='" + level1Count + '\'' +
                ", level2Count='" + level2Count + '\'' +
                ", level3Count='" + level3Count + '\'' +
                ", level1Bonud='" + level1Bonud + '\'' +
                ", level2Bonud='" + level2Bonud + '\'' +
                ", level3Bonud='" + level3Bonud + '\'' +
                '}';
    }
}
