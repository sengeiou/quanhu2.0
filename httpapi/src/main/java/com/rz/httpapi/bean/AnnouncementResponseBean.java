package com.rz.httpapi.bean;

import java.util.List;

/**
 * Created by rzw2 on 2017/8/14.
 */

public class AnnouncementResponseBean {

    private List<String> offlineNoticeIdList;
    private List<AnnouncementBean> onlineNoticeList;

    public List<AnnouncementBean> getOnlineNoticeList() {
        return onlineNoticeList;
    }

    public void setOnlineNoticeList(List<AnnouncementBean> onlineNoticeList) {
        this.onlineNoticeList = onlineNoticeList;
    }

    public List<String> getOfflineNoticeIdList() {
        return offlineNoticeIdList;
    }

    public void setOfflineNoticeIdList(List<String> offlineNoticeIdList) {
        this.offlineNoticeIdList = offlineNoticeIdList;
    }
}
