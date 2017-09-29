package com.rz.common.event;

/**
 * Created by Gsm on 2017/8/14.
 */
public class BaseEvent {
    public String key;
    public int type;
    public boolean flag;
    public Object data;

    public BaseEvent(int type) {
        this.type = type;
    }

    public BaseEvent(boolean flag) {
        this.flag = flag;
    }

    public BaseEvent(Object data) {
        this.data = data;
    }

    public BaseEvent(int type, Object data) {
        this.type = type;
        this.data = data;
    }

    public BaseEvent() {

    }

    @Override
    public String toString() {
        return "BaseEvent{" +
                "type=" + type +
                ", flag=" + flag +
                ", data=" + data +
                '}';
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
