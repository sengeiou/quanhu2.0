package com.rz.httpapi.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/9/19 0019.
 */

public class BuyingBean {

    private int count;
    private List<MyBuyingModel> list;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<MyBuyingModel> getList() {
        return list;
    }

    public void setList(List<MyBuyingModel> list) {
        this.list = list;
    }
}
