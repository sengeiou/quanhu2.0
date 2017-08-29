package com.rz.httpapi.api.ResponseData;

/**
 * Created by JS01 on 2016/6/8.
 * 响应数据结构 MVP Model层
 */
public class ResponseData<T> {

    /**
     * 鉴权
     */
    private int act;

    /**
     * 返回code 0表示成功
     */
    private int ret;

    /**
     * 提示信息
     */
    private String msg;

    /**
     * 返回数据
     */
    private T data;

    public int getAct() {
        return act;
    }

    public void setAct(int act) {
        this.act = act;
    }

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccessful() {
        return getRet() == 1;
    }

    @Override
    public String toString() {
        return "ResponseData{" +
                "act=" + act +
                ", ret=" + ret +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
