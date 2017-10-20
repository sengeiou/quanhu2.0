package com.rz.circled.helper;

import com.rz.circled.event.EventConstant;
import com.rz.circled.ui.fragment.NewsCommonFragment;
import com.rz.common.cache.preference.Session;
import com.rz.common.event.BaseEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by rzw2 on 2017/10/20.
 */

public class NewsHelper {
    public static void clearUnreadByType(int type) {
        if (type == -1)
            return;
        switch (type) {
            case NewsCommonFragment.NEWS_ANNOUNCEMENT:
                if (Session.getNewsAnnouncementNum() != 0) {
                    Session.setNewsAnnouncementNum(0);
                    EventBus.getDefault().post(new BaseEvent(EventConstant.NEWS_UNREAD_CHANGE));
                }
                break;
            case NewsCommonFragment.NEWS_SYSTEM_INFORMATION:
                if (Session.getNewsSystemInformationNum() != 0) {
                    Session.setNewsSystemInformationNum(0);
                    EventBus.getDefault().post(new BaseEvent(EventConstant.NEWS_UNREAD_CHANGE));
                }
                break;
            case NewsCommonFragment.NEWS_RECOMMEND:
                if (Session.getNewsRecommendNum() != 0) {
                    Session.setNewsRecommendNum(0);
                    EventBus.getDefault().post(new BaseEvent(EventConstant.NEWS_UNREAD_CHANGE));
                }
                break;
            case NewsCommonFragment.NEWS_ACCOUNT:
                if (Session.getNewsAccountInformationNum() != 0) {
                    Session.setNewsAccountInformationNum(0);
                    EventBus.getDefault().post(new BaseEvent(EventConstant.NEWS_UNREAD_CHANGE));
                }
                break;
            case NewsCommonFragment.NEWS_COMMENT:
                if (Session.getNewsCommentNum() != 0) {
                    Session.setNewsCommentNum(0);
                    EventBus.getDefault().post(new BaseEvent(EventConstant.NEWS_UNREAD_CHANGE));
                }
                break;
            case NewsCommonFragment.NEWS_QA:
                if (Session.getNewsQaNum() != 0) {
                    Session.setNewsQaNum(0);
                    EventBus.getDefault().post(new BaseEvent(EventConstant.NEWS_UNREAD_CHANGE));
                }
                break;
            case NewsCommonFragment.NEWS_PRIVATE_GROUP:
                if (Session.getNewsGroupNum() != 0) {
                    Session.setNewsGroupNum(0);
                    EventBus.getDefault().post(new BaseEvent(EventConstant.NEWS_UNREAD_CHANGE));
                }
                break;
            case NewsCommonFragment.NEWS_ACTIVITY:
                if (Session.getNewsActivityNum() != 0) {
                    Session.setNewsActivityNum(0);
                    EventBus.getDefault().post(new BaseEvent(EventConstant.NEWS_UNREAD_CHANGE));
                }
                break;
        }
    }
}
