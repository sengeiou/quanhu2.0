package com.rz.common.event;

/**
 * Created by Administrator on 2016/9/8 0008.
 */
public class NotifyEvent {

    public String tag;
    public Object object;
    public boolean add;
    public String id;
    public int type;

    public NotifyEvent(String tag, Object object, boolean add) {
        this.tag = tag;
        this.object = object;
        this.add = add;
    }

    @Override
    public String toString() {
        return "NotifyEvent{" +
                "tag='" + tag + '\'' +
                ", object=" + object +
                ", add=" + add +
                '}';
    }
}
