package com.yryz.yunxinim.session.action;

import android.content.Intent;

import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.yryz.yunxinim.R;
import com.yryz.yunxinim.uikit.cache.TeamDataCache;
import com.yryz.yunxinim.uikit.session.actions.BaseAction;
import com.yryz.yunxinim.uikit.session.constant.RequestCode;

/**
 * Created by hzxuwen on 2015/6/11.
 */
public class LuckyAction extends BaseAction {

    public LuckyAction() {
        super(R.drawable.btn_lucky, R.string.input_panel_lucky);
    }

    @Override
    public void onClick() {
        Intent intent = new Intent("com.rz.yryz.LUCKY_MONEY_SEND_IM");
        intent.putExtra("accountId", getAccount());
        intent.putExtra("sessionType", getSessionType());
        if (getSessionType() == SessionTypeEnum.Team) {
            intent.putExtra("team", TeamDataCache.getInstance().getTeamById(getAccount()));
        }
        getActivity().startActivityForResult(intent, makeRequestCode(RequestCode.LUCKY_MONEY_SEND));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestCode.LUCKY_MONEY_SEND) {
            if (null != data && data.hasExtra("data")) {
                sendMessage((IMMessage) data.getExtras().getSerializable("data"));
            }
        }
    }
}
