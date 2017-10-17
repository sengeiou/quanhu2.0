package com.rz.circled.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.rz.circled.R;
import com.rz.circled.dialog.MsgHistoryClearDialog;
import com.rz.circled.presenter.impl.FriendPresenter1;
import com.rz.circled.widget.GlideRoundImage;
import com.rz.common.cache.preference.Session;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.utils.Protect;
import com.rz.httpapi.bean.FriendInformationBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by rzw2 on 2017/9/22.
 */

public class ChatSettingActivity extends BaseActivity {

    @BindView(R.id.img_avatar)
    RoundedImageView imgAvatar;
    @BindView(R.id.tv_nick)
    TextView tvNick;
    @BindView(R.id.img_sex)
    ImageView imgSex;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.layout_name)
    LinearLayout layoutName;
    @BindView(R.id.tv_note)
    TextView tvNote;
    @BindView(R.id.tv_signature)
    TextView tvSignature;
    @BindView(R.id.user_info_to_edit_layout)
    RelativeLayout userInfoToEditLayout;
    @BindView(R.id.btn_clear_history_msg)
    TextView btnClearHistoryMsg;

    private FriendPresenter1 presenter;

    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_chat_setting, null);
    }

    @Override
    public boolean hasDataInPage() {
        return true;
    }

    @Override
    public void initPresenter() {
        super.initPresenter();
        presenter = new FriendPresenter1();
        presenter.attachView(this);
    }

    @Override
    public void initView() {
        setTitleText(R.string.chat_setting);
    }

    @Override
    public void initData() {
        presenter.getFriendInfoDetail(Session.getUserId());
    }

    @Override
    public void refreshPage() {
        initData();
    }

    @Override
    public <T> void updateView(T t) {
        super.updateView(t);
        if (t instanceof FriendInformationBean) {
            FriendInformationBean data = (FriendInformationBean) t;

            if (!TextUtils.isEmpty(data.getCustImg()) && Protect.checkLoadImageStatus(aty)) {
                Glide.with(aty).load(data.getCustImg()).error(R.mipmap.ic_default_avatar_big).into(imgAvatar);
            } else {
                imgAvatar.setImageResource(R.drawable.ic_default_head);
            }

            tvNick.setText(data.getCustNname());

            if (!TextUtils.isEmpty(data.getCustSignature())) {
                tvSignature.setText(getString(R.string.introduction) + data.getCustDesc());
            } else {
                tvSignature.setText(R.string.introduction);
            }

            if (!TextUtils.isEmpty(data.getCustLocation())) {
                tvLocation.setText(data.getCustLocation());
            }

            if (TextUtils.equals("1", data.getCustSex()))
                imgSex.setImageResource(R.mipmap.ic_male);
            else
                imgSex.setImageResource(R.mipmap.ic_female);

        }
    }

    @OnClick(R.id.btn_clear_history_msg)
    public void onClick() {
        new MsgHistoryClearDialog(this).show();
    }
}
