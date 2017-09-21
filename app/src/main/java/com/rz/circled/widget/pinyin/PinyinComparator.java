package com.rz.circled.widget.pinyin;


import com.rz.httpapi.bean.FriendInformationBean;

import java.util.Comparator;

/**
 *
 */
public class PinyinComparator implements Comparator<FriendInformationBean> {

    public int compare(FriendInformationBean o1, FriendInformationBean o2) {
        if (o1.getFirstLetter().equals("@") || o2.getFirstLetter().equals("#")) {
            return -1;
        } else if (o1.getFirstLetter().equals("#") || o2.getFirstLetter().equals("@")) {
            return 1;
        } else {
            return o1.getFirstLetter().compareTo(o2.getFirstLetter());
        }
    }

}
