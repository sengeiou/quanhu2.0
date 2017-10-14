package com.rz.httpapi.bean;

/**
 * Created by rzw2 on 2017/8/17.
 */

public class JPushExtraBean {
    private String message;

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
