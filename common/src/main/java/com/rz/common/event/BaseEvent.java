package com.rz.common.event;

/**
 * Created by Gsm on 2017/8/14.
 */
public class BaseEvent {

    public int type;
    public boolean flag;
    public String info;
    public Object data;

    public BaseEvent(int type) {
        this.type = type;
    }

    public BaseEvent(boolean flag) {
        this.flag = flag;
    }

    public BaseEvent(String info) {
        this.info = info;
    }

    public BaseEvent(Object data) {
        this.data = data;
    }

    public BaseEvent(int type, Object data) {
        this.type = type;
        this.data = data;
    }

    public BaseEvent(){

    }


    @Override
    public String toString() {
        return "BaseEvent{" +
                "type=" + type +
                ", flag=" + flag +
                ", info='" + info + '\'' +
                ", data=" + data +
                '}';
    }
}
