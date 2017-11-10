package com.rz.circled.widget.pinyin;

import com.rz.httpapi.bean.CircleEntrModle;

import java.util.Comparator;

/**
 * Created by Administrator on 2017/11/9/009.
 */

public class CircleComparator implements Comparator<CircleEntrModle>{


    @Override
    public int compare(CircleEntrModle o1, CircleEntrModle o2) {
        if (o1.getFirstLetter().equals("@") || o2.getFirstLetter().equals("#")) {
            return -1;
        } else if (o1.getFirstLetter().equals("#") || o2.getFirstLetter().equals("@")) {
            return 1;
        } else {
            return o1.getFirstLetter().compareTo(o2.getFirstLetter());
        }
    }
}


