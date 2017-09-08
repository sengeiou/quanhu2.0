package com.rz.httpapi.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by rzw2 on 2017/8/31.
 */

public class PrivateGroupListBean implements Serializable {
    private int count;
    private List<PrivateGroupBean> list;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<PrivateGroupBean> getList() {
        return list;
    }

    public void setList(List<PrivateGroupBean> list) {
        this.list = list;
    }
}
