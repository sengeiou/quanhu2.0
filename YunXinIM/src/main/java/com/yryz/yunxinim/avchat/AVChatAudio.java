package com.yryz.yunxinim.avchat;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;

import com.yryz.yunxinim.DemoCache;
import com.yryz.yunxinim.R;
import com.yryz.yunxinim.avchat.constant.CallStateEnum;
import com.yryz.yunxinim.avchat.widgets.ToggleListener;
import com.yryz.yunxinim.avchat.widgets.ToggleState;
import com.yryz.yunxinim.avchat.widgets.ToggleView;
import com.yryz.yunxinim.uikit.cache.NimUserInfoCache;
import com.yryz.yunxinim.uikit.common.ui.imageview.HeadImageView;
import com.yryz.yunxinim.uikit.common.util.sys.NetworkUtil;

/**
 * 音频管理器， 音频界面初始化和管理
 * Created by hzxuwen on 2015/4/24.
 */
public class AVChatAudio implements View.OnClickListener, ToggleListener{
    // constant
    private static final int[] NETWORK_GRADE_DRAWABLE = new int[]{R.drawable.network_grade_0,R.drawable.network_grade_1,R.drawable.network_grade_2,R.drawable.network_grade_3};
    private static final int[] NETWORK_GRADE_LABEL = new int[]{R.string.avchat_network_grade_0,R.string.avchat_network_grade_1,R.string.avchat_network_grade_2,R.string.avchat_network_grade_3};

    // view
    private View rootView ;
    private View switchVideo;
    private HeadImageView headImg;
    private TextView nickNameTV;
    private Chronometer time;
    private TextView wifiUnavailableNotifyTV;
    private TextView notifyTV;
    private TextView netUnstableTV;

    private View mute_speaker_hangup;
    private ToggleView muteToggle;
    private ToggleView speakerToggle;
    private ToggleView recordToggle;
    private View hangup;

    private View refuse_receive;
    private TextView refuseTV;
    private TextView receiveTV;

    //record
    private View recordView;
    private View recordTip;
    private View recordWarning;

    // data
    private AVChatUI manager;
    private AVChatUIListener listener;

    // state
    private boolean init = false;


    public AVChatAudio(View root, AVChatUIListener listener, AVChatUI manager) {
        this.rootView = root;
        this.listener = listener;
        this.manager = manager;
    }

    /**
     * 音视频状态变化及界面刷新
     * @param state
     */
    public void onCallStateChange(CallStateEnum state){
        if(CallStateEnum.isAudioMode(state))
            findViews();
        switch (state){
            case OUTGOING_AUDIO_CALLING: //拨打出的免费通话
                setSwitchVideo(false);
                showProfile();//对方的详细信息
                showNotify(R.string.avchat_wait_recieve);
                setWifiUnavailableNotifyTV(true);
                setMuteSpeakerHangupControl(true);
                setRefuseReceive(false);
                break;
            case INCOMING_AUDIO_CALLING://免费通话请求
                setSwitchVideo(false);
                showProfile();//对方的详细信息
                showNotify(R.string.avchat_audio_call_request);
                setMuteSpeakerHangupControl(false);
                setRefuseReceive(true);
                receiveTV.setText(R.string.avchat_pickup);
                break;
            case AUDIO:
                setWifiUnavailableNotifyTV(false);
                showNetworkCondition(1);
                showProfile();
                setSwitchVideo(true);
                setTime(true);
                hideNotify();
                setMuteSpeakerHangupControl(true);
                setRefuseReceive(false);
                enableToggle();
                break;
            case AUDIO_CONNECTING:
                showNotify(R.string.avchat_connecting);
                break;
            case INCOMING_AUDIO_TO_VIDEO:
                showNotify(R.string.avchat_audio_to_video_invitation);
                setMuteSpeakerHangupControl(false);
                setRefuseReceive(true);
                receiveTV.setText(R.string.avchat_receive);
                break;
            default:
                break;
        }
        setRoot(CallStateEnum.isAudioMode(state));
    }

    private boolean isEnabled = false;

    private void enableToggle() {
        if(!isEnabled) {
            recordToggle.enable();
        }
        isEnabled = true;
    }

    /**
     * 界面初始化
     */
    private void findViews() {
        if(init || rootView == null){
            return;
        }
        switchVideo = rootView.findViewById(R.id.avchat_audio_switch_video);
        switchVideo.setOnClickListener(this);

        headImg = (HeadImageView) rootView.findViewById(R.id.avchat_audio_head);
        nickNameTV = (TextView) rootView.findViewById(R.id.avchat_audio_nickname);
        time = (Chronometer) rootView.findViewById(R.id.avchat_audio_time);
        wifiUnavailableNotifyTV = (TextView) rootView.findViewById(R.id.avchat_audio_wifi_unavailable);
        notifyTV = (TextView) rootView.findViewById(R.id.avchat_audio_notify);
        netUnstableTV = (TextView) rootView.findViewById(R.id.avchat_audio_netunstable);

        mute_speaker_hangup = rootView.findViewById(R.id.avchat_audio_mute_speaker_huangup);
        View mute = mute_speaker_hangup.findViewById(R.id.avchat_audio_mute);
        muteToggle = new ToggleView(mute, ToggleState.OFF, this);
        View speaker = mute_speaker_hangup.findViewById(R.id.avchat_audio_speaker);
        speakerToggle = new ToggleView(speaker, ToggleState.OFF, this);
        View record = mute_speaker_hangup.findViewById(R.id.avchat_audio_record);
        recordToggle = new ToggleView(record, ToggleState.OFF, this);
        hangup = mute_speaker_hangup.findViewById(R.id.avchat_audio_hangup);
        hangup.setOnClickListener(this);

        recordToggle.disable(false);

        refuse_receive = rootView.findViewById(R.id.avchat_audio_refuse_receive);
        refuseTV = (TextView) refuse_receive.findViewById(R.id.refuse);
        receiveTV = (TextView) refuse_receive.findViewById(R.id.receive);
        refuseTV.setOnClickListener(this);
        receiveTV.setOnClickListener(this);

        recordView = rootView.findViewById(R.id.avchat_record_layout);
        recordTip = rootView.findViewById(R.id.avchat_record_tip);
        recordWarning = rootView.findViewById(R.id.avchat_record_warning);

        init = true;
    }

    /**
     * ********************************* 界面设置 *************************************
     */

    /**
     * 个人信息设置
     */
    private void showProfile(){
        String account = manager.getAccount();
        headImg.loadBuddyAvatar(account);
        nickNameTV.setText(NimUserInfoCache.getInstance().getUserDisplayName(account));
    }

    /**
     * 界面状态文案设置
     * @param resId 文案
     */
    private void showNotify(int resId) {
        notifyTV.setText(resId);
        notifyTV.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏界面文案
     */
    private void hideNotify(){
        notifyTV.setVisibility(View.GONE);
    }

    public void showRecordView(boolean show, boolean warning) {
        if(show) {
            recordView.setVisibility(View.VISIBLE);
            recordTip.setVisibility(View.VISIBLE);
            if(warning) {
                recordWarning.setVisibility(View.VISIBLE);
            } else {
                recordWarning.setVisibility(View.GONE);
            }
        } else {
            recordView.setVisibility(View.INVISIBLE);
            recordTip.setVisibility(View.INVISIBLE);
            recordWarning.setVisibility(View.GONE);
        }
    }

    /**
     * 显示网络状态
     * @param grade
     */
    public void showNetworkCondition(int grade){
        if(grade >= 0 && grade < NETWORK_GRADE_DRAWABLE.length){
            netUnstableTV.setText(NETWORK_GRADE_LABEL[grade]);
            Drawable drawable = DemoCache.getContext().getResources().getDrawable(NETWORK_GRADE_DRAWABLE[grade]);
            if(drawable != null){
                drawable.setBounds(0,0,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
                netUnstableTV.setCompoundDrawables(null,null,drawable,null);
            }
            netUnstableTV.setVisibility(View.VISIBLE);
        }
    }

    /**
     * ***************************** 布局显隐设置 ***********************************
     */

    private void setRoot(boolean visible){
        rootView.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    /**
     * 显示或隐藏音视频切换
     * @param visible
     */
    private void setSwitchVideo(boolean visible){
        switchVideo.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    /**
     * 显示或者隐藏是否为wifi的提示
     * @param show
     */
    private void setWifiUnavailableNotifyTV(boolean show){
        if(show && !NetworkUtil.isWifi(DemoCache.getContext())){
            wifiUnavailableNotifyTV.setVisibility(View.VISIBLE);
        }else {
            wifiUnavailableNotifyTV.setVisibility(View.GONE);
        }
    }

    /**
     * 显示或隐藏禁音，结束通话布局
     * @param visible
     */
    private void setMuteSpeakerHangupControl(boolean visible){
        mute_speaker_hangup.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    /**
     * 显示或隐藏拒绝，开启布局
     * @param visible
     */
    private void setRefuseReceive(boolean visible){
        refuse_receive.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    /**
     * 设置通话时间显示
     * @param visible
     */
    private void setTime(boolean visible){
        time.setVisibility(visible ? View.VISIBLE : View.GONE);
        if(visible){
            time.setBase(manager.getTimeBase());
            time.start();
        }
    }

    /**
     * 视频切换为音频时，禁音与扬声器按钮状态
     * @param muteOn
     * @param speakerOn
     */
    public void onVideoToAudio(boolean muteOn , boolean speakerOn, boolean recordOn, boolean recordWarning) {

        muteToggle.toggle(muteOn ? ToggleState.ON : ToggleState.OFF);
        speakerToggle.toggle(speakerOn ? ToggleState.ON : ToggleState.OFF);
        recordToggle.toggle(recordOn ? ToggleState.ON : ToggleState.OFF);

        showRecordView(recordOn, recordWarning);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.avchat_audio_hangup) {
            listener.onHangUp();

        } else if (i == R.id.refuse) {
            listener.onRefuse();

        } else if (i == R.id.receive) {
            listener.onReceive();

        } else if (i == R.id.avchat_audio_mute) {
            listener.toggleMute();

        } else if (i == R.id.avchat_audio_speaker) {
            listener.toggleSpeaker();

        } else if (i == R.id.avchat_audio_switch_video) {
            listener.audioSwitchVideo();

        } else if (i == R.id.avchat_audio_record) {
            listener.toggleRecord();

        } else {
        }
    }

    public void closeSession(int exitCode){
        if(init){
            time.stop();
            muteToggle.disable(false);
            speakerToggle.disable(false);
            recordToggle.disable(false);
            refuseTV.setEnabled(false);
            receiveTV.setEnabled(false);
            hangup.setEnabled(false);
        }
    }

    /******************************* toggle listener *************************/
    @Override
    public void toggleOn(View v) {
        onClick(v);
    }

    @Override
    public void toggleOff(View v) {
        onClick(v);
    }

    @Override
    public void toggleDisable(View v) {

    }
}
