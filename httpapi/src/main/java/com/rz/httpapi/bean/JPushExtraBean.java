package com.rz.httpapi.bean;

/**
 * Created by rzw2 on 2017/8/17.
 */

public class JPushExtraBean {
    private String message;

    public class JPushExtraMessage<T> {

        private MyPushInfoBean<T> msg_content;

        public MyPushInfoBean<T> getMsg_content() {
            return msg_content;
        }

        public void setMsg_content(MyPushInfoBean<T> msg_content) {
            this.msg_content = msg_content;
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "JPushExtraModel{" +
                "message='" + message + '\'' +
                '}';
    }
}
