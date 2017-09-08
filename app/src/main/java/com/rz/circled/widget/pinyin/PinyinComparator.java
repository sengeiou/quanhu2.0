package com.rz.circled.widget.pinyin;


import com.rz.httpapi.bean.BaseInfo;

import java.util.Comparator;

/**
 *
 */
public class PinyinComparator implements Comparator<BaseInfo> {

    public int compare(BaseInfo o1, BaseInfo o2) {
        if (o1.getFirstLetter().equals("@") || o2.getFirstLetter().equals("#")) {
            return -1;
        } else if (o1.getFirstLetter().equals("#") || o2.getFirstLetter().equals("@")) {
            return 1;
        } else {
            return o1.getFirstLetter().compareTo(o2.getFirstLetter());
        }
    }

}
