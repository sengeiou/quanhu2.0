package com.rz.circled.js.model;

import java.io.Serializable;

/**
 * Created by KF on 2017/5/31.
 */
public class EditorConfigTwoModel implements Serializable {
    /**
     * 元素是否存在(0->不存在,1->存在)
     */
    private int enabled;
    /**
     * 错误提示信息
     */
    private String errorPrompt;
    /**
     * 一级分类主键id
     */
    private int firstStageId;
    /**
     * 配置表主键id
     */
    private int id;
    /**
     * 输入提示信息
     */
    private String inputPrompt;
    /**
     * 校验下限
     */
    private int lowerLimit;
    /**
     * 属性key
     */
    private String propertyKey;
    /**
     * 是否必填(0->非必填,1->必填)
     */
    private int required;
    /**
     * 校验上限
     */
    private int upperLimit;
    /**
     * 分类专用 true 可編輯
     */
    private boolean editable = true;

    private EditorAuthorityRootBean data;//权限使用

    public EditorAuthorityRootBean getData() {
        return data;
    }

    public void setData(EditorAuthorityRootBean data) {
        this.data = data;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public boolean getEnabled() {
        if (1 == enabled)
            return true;
        else
            return false;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    public int getId() {
        return id;
    }

    public String getErrorPrompt() {
        return errorPrompt;
    }

    public void setErrorPrompt(String errorPrompt) {
        this.errorPrompt = errorPrompt;
    }

    public int getFirstStageId() {
        return firstStageId;
    }

    public void setFirstStageId(int firstStageId) {
        this.firstStageId = firstStageId;
    }

    public String getInputPrompt() {
        return inputPrompt;
    }

    public void setInputPrompt(String inputPrompt) {
        this.inputPrompt = inputPrompt;
    }

    public int getLowerLimit() {
        return lowerLimit;
    }

    public void setLowerLimit(int lowerLimit) {
        this.lowerLimit = lowerLimit;
    }

    public String getPropertyKey() {
        return propertyKey;
    }

    public void setPropertyKey(String propertyKey) {
        this.propertyKey = propertyKey;
    }

    public int getUpperLimit() {
        return upperLimit;
    }

    public void setUpperLimit(int upperLimit) {
        this.upperLimit = upperLimit;
    }

    public void setId(int id) {
        this.id = id;
    }


    public boolean getRequired() {
        if (1 == required)
            return true;
        else
            return false;
    }

    public void setRequired(int required) {
        this.required = required;
    }

}
