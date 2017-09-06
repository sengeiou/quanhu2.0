package com.rz.circled.db.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by rzw2 on 2017/8/14.
 */

@DatabaseTable(tableName = "tb_system_information")
public class SystemInformation implements Serializable {
    @DatabaseField(generatedId = true)
    private int sid;
    @DatabaseField
    private String appChineseName;
    @DatabaseField
    private String appName;
    @DatabaseField
    private String columnCode;
    @DatabaseField
    private String content;
    @DatabaseField
    private int type;
    @DatabaseField
    private String sendTime;
    @DatabaseField
    private String page;
    @DatabaseField
    private String msgId;
    @DatabaseField
    private int msgType;
    @DatabaseField
    private String title;

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getAppChineseName() {
        return appChineseName;
    }

    public void setAppChineseName(String appChineseName) {
        this.appChineseName = appChineseName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getColumnCode() {
        return columnCode;
    }

    public void setColumnCode(String columnCode) {
        this.columnCode = columnCode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "SystemInformation{" +
                "sid=" + sid +
                ", appChineseName='" + appChineseName + '\'' +
                ", appName='" + appName + '\'' +
                ", columnCode='" + columnCode + '\'' +
                ", content='" + content + '\'' +
                ", type=" + type +
                ", sendTime='" + sendTime + '\'' +
                ", page='" + page + '\'' +
                ", msgId='" + msgId + '\'' +
                ", msgType=" + msgType +
                ", title='" + title + '\'' +
                '}';
    }
}
