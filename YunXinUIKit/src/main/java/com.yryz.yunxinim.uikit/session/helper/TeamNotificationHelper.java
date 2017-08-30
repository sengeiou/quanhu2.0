package com.yryz.yunxinim.uikit.session.helper;

import android.text.TextUtils;

import com.netease.nimlib.sdk.NIMClient;
import com.yryz.yunxinim.uikit.NimUIKit;
import com.yryz.yunxinim.uikit.R;
import com.yryz.yunxinim.uikit.cache.TeamDataCache;
import com.netease.nimlib.sdk.msg.attachment.NotificationAttachment;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.team.constant.TeamAllMuteModeEnum;
import com.netease.nimlib.sdk.team.constant.TeamFieldEnum;
import com.netease.nimlib.sdk.team.constant.TeamTypeEnum;
import com.netease.nimlib.sdk.team.constant.VerifyTypeEnum;
import com.netease.nimlib.sdk.team.model.MemberChangeAttachment;
import com.netease.nimlib.sdk.team.model.MuteMemberAttachment;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.team.model.UpdateTeamAttachment;

import java.util.List;
import java.util.Map;

/**
 * 系统消息描述文本构造器。主要是将各个系统消息转换为显示的文本内容。<br>
 * Created by huangjun on 2015/3/11.
 */
public class TeamNotificationHelper {
    private static ThreadLocal<String> teamId = new ThreadLocal<>();

    public static String getMsgShowText(final IMMessage message) {
        String content = "";
        String messageTip = message.getMsgType().getSendMessageTip();
        if (messageTip.length() > 0) {
            content += "[" + messageTip + "]";
        } else {
            if (message.getSessionType() == SessionTypeEnum.Team && message.getAttachment() != null) {
                content += getTeamNotificationText(message, message.getSessionId());
            } else {
                content += message.getContent();
            }
        }

        return content;
    }

    public static String getTeamNotificationText(IMMessage message, String tid) {
        return getTeamNotificationText(message.getSessionId(), message.getFromAccount(), (NotificationAttachment) message.getAttachment());
    }

    public static String getTeamNotificationText(String tid, String fromAccount, NotificationAttachment attachment) {
        teamId.set(tid);
        String text = buildNotification(tid, fromAccount, attachment);
        teamId.set(null);
        return text;
    }

    private static String buildNotification(String tid, String fromAccount, NotificationAttachment attachment) {
        String text;
        switch (attachment.getType()) {
            case InviteMember:
                text = buildInviteMemberNotification(((MemberChangeAttachment) attachment), fromAccount);
                break;
            case KickMember:
                text = buildKickMemberNotification(((MemberChangeAttachment) attachment));
                break;
            case LeaveTeam:
                text = buildLeaveTeamNotification(fromAccount);
                break;
            case DismissTeam:
                text = buildDismissTeamNotification(fromAccount);
                break;
            case UpdateTeam:
                text = buildUpdateTeamNotification(tid, fromAccount, (UpdateTeamAttachment) attachment);
                break;
            case PassTeamApply:
                text = buildManagerPassTeamApplyNotification((MemberChangeAttachment) attachment);
                break;
            case TransferOwner:
                text = buildTransferOwnerNotification(fromAccount, (MemberChangeAttachment) attachment);
                break;
            case AddTeamManager:
                text = buildAddTeamManagerNotification((MemberChangeAttachment) attachment);
                break;
            case RemoveTeamManager:
                text = buildRemoveTeamManagerNotification((MemberChangeAttachment) attachment);
                break;
            case AcceptInvite:
                text = buildAcceptInviteNotification(fromAccount, (MemberChangeAttachment) attachment);
                break;
            case MuteTeamMember:
                text = buildMuteTeamNotification((MuteMemberAttachment) attachment);
                break;
            default:
                text = getTeamMemberDisplayName(fromAccount) + ": unknown message";
                break;
        }

        return text;
    }

    private static String getTeamMemberDisplayName(String account) {
        return TeamDataCache.getInstance().getTeamMemberDisplayNameYou(teamId.get(), account);
    }

    private static String buildMemberListString(List<String> members, String fromAccount) {
        StringBuilder sb = new StringBuilder();
        for (String account : members) {
            if (!TextUtils.isEmpty(fromAccount) && fromAccount.equals(account)) {
                continue;
            }
            sb.append(getTeamMemberDisplayName(account));
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }

    private static String buildInviteMemberNotification(MemberChangeAttachment a, String fromAccount) {
        StringBuilder sb = new StringBuilder();
        String selfName = getTeamMemberDisplayName(fromAccount);

        sb.append(selfName);
        sb.append(NimUIKit.getContext().getString(R.string.team_invite1));
        sb.append(buildMemberListString(a.getTargets(), fromAccount));
        Team team = TeamDataCache.getInstance().getTeamById(teamId.get());
        if (team.getType() == TeamTypeEnum.Advanced) {
            sb.append(NimUIKit.getContext().getString(R.string.add_team1));
        } else {
            sb.append(NimUIKit.getContext().getString(R.string.add_normal_team));
        }

        return sb.toString();
    }

    private static String buildKickMemberNotification(MemberChangeAttachment a) {
        StringBuilder sb = new StringBuilder();
        sb.append(buildMemberListString(a.getTargets(), null));
        Team team = TeamDataCache.getInstance().getTeamById(teamId.get());
        if (team.getType() == TeamTypeEnum.Advanced) {
            sb.append(NimUIKit.getContext().getString(R.string.remove_team1));
        } else {
            sb.append(NimUIKit.getContext().getString(R.string.remove_normal_team1));
        }


        return sb.toString();
    }

    private static String buildLeaveTeamNotification(String fromAccount) {
        String tip;
        Team team = TeamDataCache.getInstance().getTeamById(teamId.get());
        if (team.getType() == TeamTypeEnum.Advanced) {
            tip = NimUIKit.getContext().getString(R.string.leave_team1);
        } else {
            tip = NimUIKit.getContext().getString(R.string.leave_normal_team);
        }
        return getTeamMemberDisplayName(fromAccount) + tip;
    }

    private static String buildDismissTeamNotification(String fromAccount) {
        return getTeamMemberDisplayName(fromAccount) + NimUIKit.getContext().getString(R.string.dismiss_team1);
    }

    private static String buildUpdateTeamNotification(String tid, String account, UpdateTeamAttachment a) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<TeamFieldEnum, Object> field : a.getUpdatedFields().entrySet()) {
            if (field.getKey() == TeamFieldEnum.Name) {
                sb.append(NimUIKit.getContext().getString(R.string.team_name_changed) + field.getValue());
            } else if (field.getKey() == TeamFieldEnum.Introduce) {
                sb.append(NimUIKit.getContext().getString(R.string.team_introduce_change) + field.getValue());
            } else if (field.getKey() == TeamFieldEnum.Announcement) {
                sb.append(TeamDataCache.getInstance().getTeamMemberDisplayNameYou(tid, account) + NimUIKit.getContext().getString(R.string.team_change_announce));
            } else if (field.getKey() == TeamFieldEnum.VerifyType) {
                VerifyTypeEnum type = (VerifyTypeEnum) field.getValue();
                String authen = NimUIKit.getContext().getString(R.string.team_verification_authority);
                if (type == VerifyTypeEnum.Free) {
                    sb.append(authen + NimUIKit.getContext().getString(R.string.team_allow_anyone_join));
                } else if (type == VerifyTypeEnum.Apply) {
                    sb.append(authen + NimUIKit.getContext().getString(R.string.team_need_authentication));
                } else {
                    sb.append(authen + NimUIKit.getContext().getString(R.string.team_not_allow_anyone_join));
                }
            } else if (field.getKey() == TeamFieldEnum.Extension) {
                sb.append(NimUIKit.getContext().getString(R.string.team_expand_field_change) + field.getValue());
            } else if (field.getKey() == TeamFieldEnum.Ext_Server) {
                sb.append(NimUIKit.getContext().getString(R.string.team_expand_field_service_change) + field.getValue());
            } else if (field.getKey() == TeamFieldEnum.ICON) {
                sb.append(NimUIKit.getContext().getString(R.string.team_icon_change));
            } else if (field.getKey() == TeamFieldEnum.InviteMode) {
                sb.append(NimUIKit.getContext().getString(R.string.team_invite_somebody_authority) + field.getValue());
            } else if (field.getKey() == TeamFieldEnum.TeamUpdateMode) {
                sb.append(NimUIKit.getContext().getString(R.string.team_info_change_authority) + field.getValue());
            } else if (field.getKey() == TeamFieldEnum.BeInviteMode) {
                sb.append(NimUIKit.getContext().getString(R.string.team_invited_user_info_verification_authority) + field.getValue());
            } else if (field.getKey() == TeamFieldEnum.TeamExtensionUpdateMode) {
                sb.append(NimUIKit.getContext().getString(R.string.team_expand_field_authority) + field.getValue());
            } else if (field.getKey() == TeamFieldEnum.AllMute) {
                TeamAllMuteModeEnum teamAllMuteModeEnum = (TeamAllMuteModeEnum) field.getValue();
                if (teamAllMuteModeEnum == TeamAllMuteModeEnum.Cancel) {
                    sb.append(NimUIKit.getContext().getString(R.string.team_all_prohibited_word_cancel));
                } else {
                    sb.append(NimUIKit.getContext().getString(R.string.team_all_prohibited_word));
                }
            } else {
                sb.append(NimUIKit.getContext().getString(R.string.default_team1) + field.getKey() + NimUIKit.getContext().getString(R.string.team_name_change_to) + field.getValue());
            }
            sb.append("\r\n");
        }
        if (sb.length() < 2) {
            return NimUIKit.getContext().getString(R.string.team_unknown_notice);
        }
        return sb.delete(sb.length() - 2, sb.length()).toString();
    }

    private static String buildManagerPassTeamApplyNotification(MemberChangeAttachment a) {
        StringBuilder sb = new StringBuilder();
        sb.append(NimUIKit.getContext().getString(R.string.manager_pass));
        sb.append(buildMemberListString(a.getTargets(), null));
        sb.append(NimUIKit.getContext().getString(R.string.join_team_apply));

        return sb.toString();
    }

    private static String buildTransferOwnerNotification(String from, MemberChangeAttachment a) {
        StringBuilder sb = new StringBuilder();
        sb.append(getTeamMemberDisplayName(from));
        sb.append(NimUIKit.getContext().getString(R.string.transfer_somebody));
        sb.append(buildMemberListString(a.getTargets(), null));

        return sb.toString();
    }

    private static String buildAddTeamManagerNotification(MemberChangeAttachment a) {
        StringBuilder sb = new StringBuilder();

        sb.append(buildMemberListString(a.getTargets(), null));
        sb.append(NimUIKit.getContext().getString(R.string.appoint_manager));

        return sb.toString();
    }

    private static String buildRemoveTeamManagerNotification(MemberChangeAttachment a) {
        StringBuilder sb = new StringBuilder();

        sb.append(buildMemberListString(a.getTargets(), null));
        sb.append(NimUIKit.getContext().getString(R.string.revoked_manager));

        return sb.toString();
    }

    private static String buildAcceptInviteNotification(String from, MemberChangeAttachment a) {
        StringBuilder sb = new StringBuilder();

        sb.append(getTeamMemberDisplayName(from));
        sb.append(NimUIKit.getContext().getString(R.string.team_accept)).append(buildMemberListString(a.getTargets(), null)).append(NimUIKit.getContext().getString(R.string.team_invitation));

        return sb.toString();
    }

    private static String buildMuteTeamNotification(MuteMemberAttachment a) {
        StringBuilder sb = new StringBuilder();

        sb.append(buildMemberListString(a.getTargets(), null));
        sb.append(NimUIKit.getContext().getString(R.string.by_manager));
        sb.append(a.isMute() ? NimUIKit.getContext().getString(R.string.prohibited_words) : NimUIKit.getContext().getString(R.string.to_lift_the_prohibition));

        return sb.toString();
    }
}
