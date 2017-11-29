package com.rz.circled.dialog;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.rz.circled.R;
import com.rz.common.cache.preference.Session;
import com.rz.common.utils.AudioUtils;
import com.rz.common.utils.DensityUtils;
import com.rz.common.utils.ImageUtils;
import com.rz.common.widget.toasty.Toasty;
import com.zbar.lib.encoding.EncodingUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by rzw2 on 2017/9/4.
 */

public class InviteRewardScanDialog extends DialogFragment {

    @BindView(R.id.btn_close)
    ImageView btnClose;
    @BindView(R.id.iv_avatar)
    RoundedImageView ivAvatar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.iv_scan)
    ImageView ivScan;
    @BindView(R.id.btn_save)
    TextView btnSave;

    private AudioUtils audioUtils;

    @Override //在onCreate中设置对话框的风格、属性等
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog);
        //如果setCancelable()中参数为true，若点击dialog覆盖不到的activity的空白或者按返回键，则进行cancel，状态检测依次onCancel()和onDismiss()。如参数为false，则按空白处或返回键无反应。缺省为true
        setCancelable(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            Window dialogWindow = getDialog().getWindow();
            dialogWindow.setGravity(Gravity.CENTER);
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = (int) (DensityUtils.getScreenW(getContext()) * 0.8);
            dialogWindow.setAttributes(lp);
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_invite_scan, container, false);
        ButterKnife.bind(this, v);
        initView();
        return v;
    }

    private void initView() {
        audioUtils = new AudioUtils(getActivity());
        tvName.setText(Session.getUserName());
        Glide.with(getContext()).load(Session.getUserPicUrl()).error(R.mipmap.ic_default_avatar_big).into(ivAvatar);
        Bitmap bitmap = EncodingUtils.createQRCode(Session.getInviteLink(), 1000, 1000, ImageUtils.drawableToBitmap(getResources().getDrawable(R.mipmap.icon_logo)));
        if (bitmap != null && !TextUtils.isEmpty(Session.getInviteLink()))
            ivScan.setImageBitmap(bitmap);
    }

    @OnClick({R.id.btn_close, R.id.btn_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_close:
                dismiss();
                break;
            case R.id.btn_save:
                ivScan.buildDrawingCache();
                Bitmap bitmap = ivScan.getDrawingCache();
                if (audioUtils.saveImageToGallery(bitmap, "user_qr_code")) {
                    Toasty.info(getContext(), "保存成功").show();
                    dismiss();
                } else {
                    Toasty.info(getContext(), "保存失败").show();
                }
                break;
        }
    }
}
