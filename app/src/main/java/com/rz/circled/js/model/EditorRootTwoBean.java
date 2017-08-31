package com.rz.circled.js.model;

import java.io.Serializable;
import java.util.HashMap;

public class EditorRootTwoBean implements Serializable {
    private HashMap<String, EditorConfigTwoModel> config;
    private EditorCategoryRootTwoModel category;
    private EditorDataSourceTwoModel dataSource;
    private HttpRequestModel request;

    public HashMap<String, EditorConfigTwoModel> getConfig() {
        return config;
    }

    public void setConfig(HashMap<String, EditorConfigTwoModel> config) {
        this.config = config;
    }

    public EditorCategoryRootTwoModel getCategory() {
        return category;
    }

    public void setCategory(EditorCategoryRootTwoModel category) {
        this.category = category;
    }

    public EditorDataSourceTwoModel getDataSource() {
        return dataSource;
    }

    public void setDataSource(EditorDataSourceTwoModel dataSource) {
        this.dataSource = dataSource;
    }

    public HttpRequestModel getRequest() {
        return request;
    }

    public void setRequest(HttpRequestModel request) {
        this.request = request;
    }
}
