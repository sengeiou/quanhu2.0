package com.rz.circled.js.model;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/3/29/029.
 */

public class HttpRequestModel implements Serializable {

    /**
     * body :
     * headers :
     * method : post
     * url : http://gc.ditu.aliyun.com/regeocoding
     */

    private String body;
    private HashMap<String, String> headers;
    private String method;
    private String url;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "HttpRequestModel{" +
                "body='" + body + '\'' +
                ", headers='" + headers + '\'' +
                ", method='" + method + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
