package com.rz.circled.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.SystemMessageService;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.rz.circled.R;
import com.rz.common.event.BaseEvent;
import com.rz.common.ui.activity.BaseActivity;
import com.yryz.yunxinim.main.reminder.ReminderManager;
import com.yryz.yunxinim.session.SessionHelper;
import com.yryz.yunxinim.session.extension.ArticleAttachment;
import com.yryz.yunxinim.session.extension.GuessAttachment;
import com.yryz.yunxinim.session.extension.LuckyAttachment;
import com.yryz.yunxinim.session.extension.LuckyTipAttachment;
import com.yryz.yunxinim.session.extension.RTSAttachment;
import com.yryz.yunxinim.session.extension.ShowAttachment;
import com.yryz.yunxinim.session.extension.SnapChatAttachment;
import com.yryz.yunxinim.session.extension.StickerAttachment;
import com.yryz.yunxinim.uikit.NimUIKit;
import com.yryz.yunxinim.uikit.common.ui.drop.DropCover;
import com.yryz.yunxinim.uikit.common.ui.drop.DropManager;
import com.yryz.yunxinim.uikit.common.util.log.LogUtil;
import com.yryz.yunxinim.uikit.recent.RecentContactsCallback;
import com.yryz.yunxinim.uikit.recent.RecentContactsFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by rzw2 on 2017/9/16.
 */

public class RecentContactActivity extends BaseActivity {
    private static final String FRAGMENT_TAG = "msgFragment";

    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_recent_contact, null);
    }

    @Override
    public void initView() {
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        initUnreadCover();
    }

    @Override
    public void initData() {
        addRecentContactsFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        enableMsgNotification(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        enableMsgNotification(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventUpdate(BaseEvent event) {
    }

    // 将最近联系人列表fragment动态集成进来。 开发者也可以使用在xml中配置的方式静态集成。
    private void addRecentContactsFragment() {
        RecentContactsFragment fragment = new RecentContactsFragment();
        FragmentManager fm = getSupportFragmentManager();
        Fragment previosFrg = fm.findFragmentByTag(FRAGMENT_TAG);
        FragmentTransaction tx = fm.beginTransaction();
        if (previosFrg != null) {
            fragment = (RecentContactsFragment) previosFrg;
        } else {
            tx.add(R.id.content, fragment, FRAGMENT_TAG);
            tx.commitAllowingStateLoss();
        }

        fragment.setCallback(new RecentContactsCallback() {
            @Override
            public void onRecentContactsLoaded() {
                // 最近联系人列表加载完毕
            }

            @Override
            public void onUnreadCountChange(int unreadCount) {
                ReminderManager.getInstance().updateSessionUnreadNum(unreadCount);
            }

            @Override
            public void onItemClick(RecentContact recent) {
                // 回调函数，以供打开会话窗口时传入定制化参数，或者做其他动作
                if (recent != null && recent.getContactId() != null && !TextUtils.isEmpty(recent.getContactId())) {
                    switch (recent.getSessionType()) {
                        case P2P:
                            SessionHelper.startP2PSession(mContext, recent.getContactId());
                            break;
                        case Team:
                            SessionHelper.startTeamSession(mContext, recent.getContactId());
                            break;
                        default:
                            break;
                    }
                }
            }

            @Override
            public String getDigestOfAttachment(MsgAttachment attachment) {
                // 设置自定义消息的摘要消息，展示在最近联系人列表的消息缩略栏上
                // 当然，你也可以自定义一些内建消息的缩略语，例如图片，语音，音视频会话等，自定义的缩略语会被优先使用。
                if (attachment instanceof GuessAttachment) {
                    GuessAttachment guess = (GuessAttachment) attachment;
                    return guess.getValue().getDesc();
                } else if (attachment instanceof RTSAttachment) {
                    return "[白板]";
                } else if (attachment instanceof StickerAttachment) {
                    return "[贴图]";
                } else if (attachment instanceof SnapChatAttachment) {
                    return "[阅后即焚]";
                } else if (attachment instanceof LuckyAttachment) {
                    LuckyAttachment a = (LuckyAttachment) attachment;
                    return "[红包] " + a.getUserName() + ":" + a.getTitle();
                } else if (attachment instanceof ArticleAttachment) {
                    return "[分享作品]";
                } else if (attachment instanceof ShowAttachment) {
                    return "[分享悠友圈]";
                } else if (attachment instanceof LuckyTipAttachment) {
                    LuckyTipAttachment lucky = (LuckyTipAttachment) attachment;

                    String text = "";
                    if (TextUtils.equals(lucky.getUserId(), NimUIKit.getAccount())) {
                        if (!TextUtils.equals(lucky.getReceiveId(), NimUIKit.getAccount()))
                            text = lucky.getReceiveName() + "领取了你的红包";
                        else
                            text = "你领取了自己的红包";
                    } else if (TextUtils.equals(lucky.getReceiveId(), NimUIKit.getAccount())) {
                        text = "你领取了" + lucky.getUserName() + "的红包";
                    }
                    return text;
                }

                return null;
            }

            @Override
            public String getDigestOfTipMsg(RecentContact recent) {
                String msgId = recent.getRecentMessageId();
                List<String> uuids = new ArrayList<>(1);
                uuids.add(msgId);
                List<IMMessage> msgs = NIMClient.getService(MsgService.class).queryMessageListByUuidBlock(uuids);
                if (msgs != null && !msgs.isEmpty()) {
                    IMMessage msg = msgs.get(0);
                    Map<String, Object> content = msg.getRemoteExtension();
                    if (content != null && !content.isEmpty()) {
                        return (String) content.get("content");
                    }
                }

                return null;
            }
        });
    }

    /**
     * 初始化未读红点动画
     */
    private void initUnreadCover() {
        DropManager.getInstance().init(aty, (DropCover) findViewById(R.id.unread_cover),
                new DropCover.IDropCompletedListener() {
                    @Override
                    public void onCompleted(Object id, boolean explosive) {
                        if (id == null || !explosive) {
                            return;
                        }

                        if (id instanceof RecentContact) {
                            RecentContact r = (RecentContact) id;
                            NIMClient.getService(MsgService.class).clearUnreadCount(r.getContactId(), r.getSessionType());
                            LogUtil.i("HomeFragment", "clearUnreadCount, sessionId=" + r.getContactId());
                        } else if (id instanceof String) {
                            if (((String) id).contentEquals("0")) {
                                List<RecentContact> recentContacts = NIMClient.getService(MsgService.class).queryRecentContactsBlock();
                                for (RecentContact r : recentContacts) {
                                    if (r.getUnreadCount() > 0) {
                                        NIMClient.getService(MsgService.class).clearUnreadCount(r.getContactId(), r.getSessionType());
                                    }
                                }
                                LogUtil.i("HomeFragment", "clearAllUnreadCount");
                            } else if (((String) id).contentEquals("1")) {
                                NIMClient.getService(SystemMessageService.class).resetSystemMessageUnreadCount();
                                LogUtil.i("HomeFragment", "clearAllSystemUnreadCount");
                            }
                        }
                    }
                });
    }

    private void enableMsgNotification(boolean enable) {
//        boolean msg = (pager.getCurrentItem() != MainTab.RECENT_CONTACTS.tabIndex);
        boolean msg = false;
        if (enable | msg) {
            /**
             * 设置最近联系人的消息为已读
             *
             * @param account,    聊天对象帐号，或者以下两个值：
             *                    {@link #MSG_CHATTING_ACCOUNT_ALL} 目前没有与任何人对话，但能看到消息提醒（比如在消息列表界面），不需要在状态栏做消息通知
             *                    {@link #MSG_CHATTING_ACCOUNT_NONE} 目前没有与任何人对话，需要状态栏消息通知
             */
            NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_NONE, SessionTypeEnum.None);
        } else {
            NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_ALL, SessionTypeEnum.None);
        }
    }

    private void clearFrg() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment previosFrg = fm.findFragmentByTag(FRAGMENT_TAG);
        FragmentTransaction tx = fm.beginTransaction();
        if (previosFrg != null) {
            tx.remove(previosFrg);
            tx.commit();
        }
    }

    @Override
    public void refreshPage() {

    }
}
