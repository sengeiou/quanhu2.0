package com.rz.httpapi.bean;

/**
 * Created by Administrator on 2017/9/7/007.
 */

public class MoreFamousModel<T> {

    /**
     * starList : [{"custInfo":{"custId":"","custImg":"","custLevel":1,"custNname":"","nameNotes":""},"starInfo":{"tradeField":""}}]
     * starTotal : 10
     */

    public int starTotal;
    public T starList;
}
