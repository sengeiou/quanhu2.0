package com.rz.circled.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.msg.SystemMessageService;
import com.netease.nimlib.sdk.msg.constant.SystemMessageType;
import com.rz.circled.R;
import com.rz.circled.modle.CircleStatsModel;
import com.rz.circled.modle.CustormServiceModel;
import com.rz.circled.modle.MineFragItemModel;
import com.rz.circled.presenter.IPresenter;
import com.rz.circled.presenter.impl.V3CirclePresenter;
import com.rz.circled.ui.activity.LoginActivity;
import com.rz.circled.ui.activity.PersonInfoAty;
import com.rz.circled.ui.activity.SearchActivity;
import com.rz.circled.ui.activity.SettingActivity;
import com.rz.circled.widget.GlideCircleImage;
import com.rz.circled.widget.GlideRoundImage;
import com.rz.common.adapter.CommonAdapter;
import com.rz.common.adapter.ViewHolder;
import com.rz.common.cache.preference.EntityCache;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.Constants;
import com.rz.common.constant.IntentCode;
import com.rz.common.event.BaseEvent;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.ui.fragment.BaseFragment;
import com.rz.common.utils.Protect;
import com.rz.common.utils.StringUtils;
import com.rz.circled.widget.MListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action1;

import static com.rz.circled.widget.CommomUtils.trackUser;

/**
 * Created by Gsm on 2017/8/29.
 */
public class MineFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    /**
     * 用户头像
     */
    @BindView(R.id.id_person_head_img)
    ImageView mImgPersonHead;
    @BindView(R.id.id_person_news_rela)
    RelativeLayout idPersonNewsRela;
    @BindView(R.id.id_person_login_days)
    TextView idPersonLoginDays;
    /**
     * 用户昵称
     */
    @BindView(R.id.id_person_name_txt)
    TextView mTxtPersonName;
    @BindView(R.id.title_content)
    FrameLayout mTitleContent;
    /**
     * 展示内容
     */
    @BindView(R.id.id_contacts_listv)
    MListView mListView;

    public static String URL = "https://wap.yryz.com/inviteRegister.html?inviter=";
    public static String MINEFRGFOCUS = "mine_focus_push";

    List<MineFragItemModel> mModelList;
    CommonAdapter adapter;
    @BindView(R.id.tv_circle_count)
    TextView tvCircleCount;
    @BindView(R.id.tv_transfer_count)
    TextView tvTransferCount;
    @BindView(R.id.tv_collect_count)
    TextView tvCollectCount;
//    private SplashPresenter mSplashPresenter;
    protected IPresenter presenter;
    private MessageReceiver receiver;
    private CustormServiceModel mCustormServiceModel;
    private SharedPreferences mSp;

    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_mine, null);
    }


    @Override
    public void initPresenter() {
//        mSplashPresenter = new SplashPresenter();
//        mSplashPresenter.attachView(this);
        presenter = new V3CirclePresenter();
//        mSplashPresenter.getCustomerService();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity.unregisterReceiver(receiver);
    }

    @Override
    public void initView() {
        mSp = getContext().getSharedPreferences("", Context.MODE_PRIVATE);
        initUserNews();
        View newtitilbar = View.inflate(getActivity(), R.layout.titlebar_mine, null);
        newtitilbar.setBackgroundColor(getResources().getColor(R.color.color_main));
        TextView tv = (TextView) newtitilbar.findViewById(R.id.titlebar_main_tv);
        tv.setText("我");
        mTitleContent.addView(newtitilbar);
        idPersonNewsRela.setBackgroundColor(getResources().getColor(R.color.color_main));
    }


//    private void initTitleBar() {
//        View v = View.inflate(getActivity(), R.layout.titlebar_transparent, null);
//        mLayoutContent.addView(v);
//        RxView.clicks(v.findViewById(R.id.et_search_keyword)).throttleFirst(2, TimeUnit.SECONDS).subscribe(new Action1<Void>() {
//            @Override
//            public void call(Void aVoid) {
//                //跳搜索界面
//                startActivity(new Intent(mActivity, SearchActivity.class));
//            }
//        });
//        RxView.clicks(v.findViewById(R.id.iv_mess)).throttleFirst(2, TimeUnit.SECONDS).subscribe(new Action1<Void>() {
//            @Override
//            public void call(Void aVoid) {
//                //跳消息界面
//            }
//        });
//
//    }


    //初始化用户信息
    public void initUserNews() {
        if (Session.getUserIsLogin()) {
            mTxtPersonName.setText(Session.getUserName());
            if (TextUtils.isEmpty(Session.getUser_signatrue()))
                idPersonLoginDays.setText("");
            else
                idPersonLoginDays.setText("个性签名：" + Session.getUser_signatrue());
        } else {
            mTxtPersonName.setText(getString(R.string.mine_no_login));
            idPersonLoginDays.setText("");
        }
        if (Protect.checkLoadImageStatus(mActivity)) {
            Glide.with(mActivity).load(Session.getUserPicUrl()).transform(new GlideCircleImage(mActivity)).
                    placeholder(R.drawable.ic_default_head).error(R.drawable.ic_default_head).crossFade().into(mImgPersonHead);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onVisible() {
        super.onVisible();
        if (NIMClient.getStatus() == StatusCode.LOGINED) {
            List<SystemMessageType> types = new ArrayList<>();
            types.add(SystemMessageType.ApplyJoinTeam);
            types.add(SystemMessageType.DeclineTeamInvite);
            types.add(SystemMessageType.RejectTeamApply);
            types.add(SystemMessageType.TeamInvite);
            types.add(SystemMessageType.undefined);
            int unreadNum = NIMClient.getService(SystemMessageService.class).querySystemMessageUnreadCountByType(types) +
                    (TextUtils.isEmpty(Session.getUserFocusNum()) ? 0 : Integer.parseInt(Session.getUserFocusNum()));
            if (Session.getUserIsLogin() && unreadNum != 0 && null != mModelList && !mModelList.isEmpty() && null != adapter) {
                mModelList.get(0).setContacts(true);
                mModelList.get(0).setmFocusNum(String.valueOf(unreadNum));
                adapter.notifyDataSetChanged();
            } else {
                if (mModelList.isEmpty())
                    return;
                mModelList.get(0).setContacts(false);
                mModelList.get(0).setmFocusNum("");
                adapter.notifyDataSetChanged();
            }
        } else {
            if (mModelList == null)
                return;
            mModelList.get(0).setContacts(false);
            mModelList.get(0).setmFocusNum("");
            adapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (null != mTxtPersonName && null != mImgPersonHead) {
            initUserNews();
        }
        if (!TextUtils.isEmpty(Session.getUserId())) {
            ((V3CirclePresenter) presenter).getCircleStats("");
        }
    }

    @Override
    public void onLoadingStatus(int loadingStatus, String string) {
        return;
    }


    @Override
    public void initData() {
        EventBus.getDefault().register(this);

        mModelList = new ArrayList<MineFragItemModel>();
//        MineFragItemModel model8 = new MineFragItemModel(getString(R.string.mine_my_contacts), "管理好友", R.drawable.ic_mine_contacts_new, false);
        MineFragItemModel model8 = new MineFragItemModel(getString(R.string.mine_my_contacts), R.mipmap.icon_code, false);
        MineFragItemModel model0 = new MineFragItemModel(getString(R.string.mine_my_account), R.mipmap.icon_code, false);
        MineFragItemModel model2 = new MineFragItemModel(getString(R.string.mine_my_ticket), R.mipmap.icon_code, false);
        MineFragItemModel model3 = new MineFragItemModel(getString(R.string.mine_my_collection), R.mipmap.icon_code, false);
        //增加我的推广fengan
//        MineFragItemModel model9 = new MineFragItemModel(getString(R.string.mine_my_invite), R.drawable.ic_mine_promotion_new, false);

        MineFragItemModel model4 = new MineFragItemModel(getString(R.string.mine_my_qrcode), R.mipmap.icon_code, false);
        MineFragItemModel model6 = new MineFragItemModel(getString(R.string.mine_my_invite_friend), R.mipmap.icon_code, true);
//        MineFragItemModel model4 = new MineFragItemModel(getString(R.string.mine_notice), R.drawable.ic_mine_accouncement, false);
        MineFragItemModel model5 = new MineFragItemModel(getString(R.string.mine_my_setting), R.mipmap.icon_code, false);
        MineFragItemModel model7 = new MineFragItemModel(getString(R.string.v3_customer_service), R.mipmap.icon_code, false);


        mModelList.add(model8);//通讯录
        mModelList.add(model0);//我的账户
        mModelList.add(model2);//我的卡卷

//        mModelList.add(model3);//我的收藏
        mModelList.add(model4);//我的二维码
        mModelList.add(model6);//一键分享
//        mModelList.add(model9);//我的推广

        mModelList.add(model7);//在线留言
        mModelList.add(model5);//设置

        mListView.setAdapter(adapter = new CommonAdapter<MineFragItemModel>(mActivity, mModelList, R.layout.adp_mine_frg) {

            @Override
            public void convert(ViewHolder helper, MineFragItemModel item, int position) {

                View mDivider = helper.getViewById(R.id.id_divider_v);
                View mLine = helper.getViewById(R.id.id_line_v);
                ImageView isUpdate = (ImageView) helper.getViewById(R.id.version_update);
                TextView mContactNum = (TextView) helper.getViewById(R.id.id_contact_num_update);

                if (item.isDivider()) {
                    mDivider.setVisibility(View.VISIBLE);
                    mLine.setVisibility(View.GONE);
                } else {
                    mDivider.setVisibility(View.GONE);
                    mLine.setVisibility(View.VISIBLE);
                }

                if (item.isUpdate()) {
                    isUpdate.setVisibility(View.VISIBLE);
                } else {
                    isUpdate.setVisibility(View.GONE);
                }

                if (item.isContacts()) {
                    mContactNum.setVisibility(View.VISIBLE);
                    long num = StringUtils.isEmpty(item.getmFocusNum()) ? 0 : Long.parseLong(item.getmFocusNum());
                    mContactNum.setText(num > 99 ? "99+" : (num + ""));
                } else {
                    mContactNum.setVisibility(View.GONE);
                }
                TextView tv_remark = (TextView) helper.getViewById(R.id.id_remark);
                if (TextUtils.isEmpty(item.remark)) {
                    tv_remark.setVisibility(View.GONE);
                } else {
                    tv_remark.setVisibility(View.VISIBLE);
                    tv_remark.setText(item.remark);
                }

                helper.setImageResource(R.id.id_icon_img, item.getDrawable());
                helper.setText(R.id.id_name_txt, item.getName());
            }
        });
        mListView.setOnItemClickListener(this);

        receiver = new MessageReceiver();
        IntentFilter filter_dynamic = new IntentFilter();
        filter_dynamic.addAction(MINEFRGFOCUS);
        mActivity.registerReceiver(receiver, filter_dynamic);

//        checkUpdate();

        if (Session.getUserIsLogin()) {
            EntityCache<CircleStatsModel> entityCache = new EntityCache<>(mActivity, CircleStatsModel.class);
            CircleStatsModel circleStatsModel = entityCache.getEntity(CircleStatsModel.class);
            if (circleStatsModel != null) {
                updateView(circleStatsModel);
            }
        }

    }

    @Override
    public <T> void updateView(T t) {
        if (t instanceof CustormServiceModel) {
            mCustormServiceModel = (CustormServiceModel) t;
            if (null != mCustormServiceModel) {
                String messageUrl = mCustormServiceModel.getMessageUrl();
                mSp.edit().putString(Constants.CUSTOMER_SERVICE, messageUrl).commit();
            }
        } else {
            if (null != t) {
                CircleStatsModel data = (CircleStatsModel) t;
                tvCircleCount.setText(data.getCircleNum() + "");
                tvCollectCount.setText(data.getCollectionNum() + "");
                tvTransferCount.setText(data.getTransferNum() + "");
            }
        }
    }


    private void checkUpdate() {
        /***** 获取升级信息 *****/
//        UpgradeInfo upgradeInfo = Beta.getUpgradeInfo();
//
//        if (upgradeInfo != null) {
//            BaseEvent event = new BaseEvent();
//            event.info = "versionUpdate";
//            EventBus.getDefault().post(event);
//        } else {
//            BaseEvent event = new BaseEvent();
//            event.info = "noVersionUpdate";
//            EventBus.getDefault().post(event);
//        }
    }

    @OnClick({R.id.id_person_news_rela, R.id.btn_my_transfer, R.id.id_person_head_img, R.id.btn_my_collect, R.id.btn_my_circle})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_my_collect:
                if (isLogin()) {
                    trackUser("我的","入口名称","我的收藏");
//                    showActivity(mActivity, V3MyCollectedAty.class);
                }
                break;
            case R.id.btn_my_transfer:
                if (isLogin()) {
//                    trackUser("我的","入口名称","我的打赏");
//                    BaseInfo baseInfo = new BaseInfo();
//                    baseInfo.setCustId(Session.getUserId());
//                    baseInfo.setCustNname(Session.getUserName());
//                    baseInfo.setCustLocation(Session.getUser_area());
//                    baseInfo.setCustSignature(Session.getUser_signatrue());
//                    baseInfo.setCustImg(Session.getUserPicUrl());
//                    V3UserShareActivity.toUserShare(mActivity, baseInfo);
                }
                break;
            case R.id.btn_my_circle:
                if (isLogin()) {
//                    trackUser("我的","入口名称","我的圈子");
//                    BaseInfo baseInfo = new BaseInfo();
//                    baseInfo.setCustId(Session.getUserId());
//                    baseInfo.setCustNname(Session.getUserName());
//                    baseInfo.setCustLocation(Session.getUser_area());
//                    baseInfo.setCustSignature(Session.getUser_signatrue());
//                    baseInfo.setCustImg(Session.getUserPicUrl());
//                    V3UserCircleActivity.toUserCircle(frg, Session.getUserId(), baseInfo);
                }
                break;
            case R.id.id_person_head_img:
                if (isLogin()) {
                    trackUser("我的","入口名称","我的头像");
//                    V3FriendInfoActivity.newFrindInfo(frg, Session.getUserId());
                }
                break;
            //查看个人详情信息
            case R.id.id_person_news_rela:
                if (Session.getUserIsLogin()) {
                    trackUser("我的","入口名称","个人详情");
                    jump(PersonInfoAty.class);

                } else {
                    Intent login = new Intent(mActivity, LoginActivity.class);
                    startActivityForResult(login, IntentCode.Login.LOGIN_REQUEST_CODE);
                }
                break;
            //原创作品
//            case R.id.btn_my_work:
//                if (isLogin()) {
//                    MyOriginalWorksAty.startOriginalAty(frg, Session.getUserId(), Session.getUserPicUrl(), Session.getUserName(), Session.getUser_desc());
//                }
//                break;
            //圈子转发
//            case R.id.btn_my_transfer:
//                if (isLogin()) {
//                    MyForwardAty.startMyCircleAty(frg, Session.getUserId(), Session.getUserPicUrl(), Session.getUserName());
//                }
////                startActivity(new Intent(getActivity().getApplicationContext(), WelcomeActivity.class));
//                break;
            //晒一晒
//            case R.id.btn_my_show:
//                if (isLogin()) {
//                    Intent intent = new Intent(getActivity(), MyShowUpAty.class);
//                    intent.putExtra(IntentKey.General.KEY_NAME, Session.getUserName());
////                    intent.putExtra(IntentKey.General.KEY_DESC, Session.getUser_signatrue());
//                    intent.putExtra(IntentKey.General.KEY_HEAD, Session.getUserPicUrl());
//                    intent.putExtra(IntentKey.General.KEY_ID, Session.getUserId());
//                    startActivity(intent);
//                }
//                break;
            //红包广告
//            case R.id.id_mine_adv_ll:
//                if (isLogin()) {
//                    jump(MyRedBagAdvAty.class);
//                }
//                break;
        }
    }

//    mModelList.add(model8);//通讯录
//    mModelList.add(model0);//我的账户
//    mModelList.add(model2);//我的卡卷
//
//    mModelList.add(model3);//我的收藏
//    mModelList.add(model9);//我的推广
//
//    mModelList.add(model4);//我的二维码
//    mModelList.add(model5);//设置
//    mModelList.add(model7);//在线留言

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            //通讯录
            case 0:
                if (isLogin()) {
                    trackUser("我的", "入口名称", "通讯录");
//                    showActivity(frg, ContactsAty.class);
                }
                break;
            //我的账户
            case 1:
                if (isLogin()) {
                    trackUser("我的", "入口名称", "我的账户");
//                    showActivity(frg, MyAccountAty.class);
                }
                break;
            //我的二维码
            case 3:
//                if (isLogin()) {
//                    showActivity(frg, MyCollectionAty.class);
//                }
                if (isLogin()) {
                    trackUser("我的", "入口名称", "我的二维码");
//                    showActivity(frg, PersonScanAty.class);
                }
                break;
            //转发券
            case 2:
                if (isLogin()) {
                    trackUser("我的", "入口名称", "我的卡券");
//                    Intent intent = new Intent(getActivity(), AwesomeTabsAty.class);
//                    intent.putExtra(IntentKey.KEY_TYPE, Type.TYPE_TICKET);
//                    startActivity(intent);
                }
                break;
            case 4:
                if (isLogin()) {
//                    trackUser("我的","入口名称","一键邀请好友");
//                    ShareNewsAty.startShareNews(frg, new ShareModel(
//                                    "悠然一指，一指进入你的圈子",
//                                    "悠然一指(www.yryz.com)，国内首创的一站式大型社群资源平台。平台自主创新，自主研发，精心打造并陆续推出300个各具特色的社群资源圈，汇聚了丰富的资源与人脉，展示了用户发布和分享的各类知识、经验、技能、专业服务以及商业资源。",
//                                    H5Address.ONLINE_TUIGUANG),
//                            IntentCode.PAGE_ADDFRIEND);
                }
                break;
            //设置
            case 6:
                trackUser("我的","入口名称","设置");
                Intent intent = new Intent(mActivity, SettingActivity.class);
                startActivityForResult(intent, IntentCode.MineFrg.MINE_REQUEST_CODE);
                break;
            //联系客服
            case 5:
                if (isLogin() && null != mCustormServiceModel) {
                    trackUser("我的", "入口名称", "联系客服");
                    starCustormService();
                } else {
//                    String customer_url = mSp.getString(Constants.CUSTOMER_SERVICE, "");
//                    CommH5Aty.startCommonH5(frg, customer_url);
//                }
                    break;
                }
        }
    }

    private void starCustormService() {
        int startHour = mCustormServiceModel.getStartHour();
        int startMinute = mCustormServiceModel.getStartMinute();
        int endHour = mCustormServiceModel.getEndHour();
        int endMinute = mCustormServiceModel.getEndMinute();
        boolean isWrokDate = isCurrentInTimeScope(startHour, startMinute, endHour, endMinute);
        if (mCustormServiceModel.getStatus() == 1 && isWrokDate == true) {

//            CommH5Aty.startCommonH5(frg, mCustormServiceModel.getCustomUrl());
        } else {

//            CommH5Aty.startCommonH5(frg, mCustormServiceModel.getMessageUrl());
        }
    }

    public static boolean isCurrentInTimeScope(int beginHour, int beginMin, int endHour, int endMin) {
        boolean isWorkDate = false;
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int minuteOfDay = hour * 60 + minute;
        int currentStartDate = beginHour * 60 + beginMin;
        int currentEndDate = endHour * 60 + endMin;
        if (minuteOfDay > currentStartDate && minuteOfDay < currentEndDate) {
            return true;
        }
        return isWorkDate;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentCode.MineFrg.MINE_REQUEST_CODE) {
            if (resultCode == IntentCode.Setting.SETTING_RESULT_CODE) {
                tvCircleCount.setText("0");
                tvCollectCount.setText("0");
                tvTransferCount.setText("0");

                //刷新消息和随手晒界面
//                if (((MsgFragment) getActivity().getSupportFragmentManager().findFragmentByTag("聊天") != null))
//                    ((MsgFragment) getActivity().getSupportFragmentManager().findFragmentByTag("聊天")).clearFrg();

                Session.clearShareP();
                mTxtPersonName.setText(getString(R.string.mine_no_login));
                if (Protect.checkLoadImageStatus(mActivity)) {
                    Glide.with(mActivity).load(Session.getUserPicUrl()).transform(new GlideRoundImage(mActivity)).
                            placeholder(R.drawable.ic_default_head).error(R.drawable.ic_default_head).crossFade().into(mImgPersonHead);
                }

                Intent focus = new Intent(MineFragment.MINEFRGFOCUS);
                mActivity.sendBroadcast(focus);

//                BaseEvent event = new BaseEvent();
//                event.key = LOGIN_OUT_SUCCESS;
//                EventBus.getDefault().post(event);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventUpdate(BaseEvent event) {
//        if (TextUtils.equals("versionUpdate", event.key)) {
//            mModelList.get(6).setUpdate(true);
//            adapter.notifyDataSetChanged();
//        } else if (TextUtils.equals("noVersionUpdate", event.key)) {
//            mModelList.get(6).setUpdate(false);
//            adapter.notifyDataSetChanged();
//        } else if (TextUtils.equals(LOGIN_OUT_SUCCESS, event.key)) {
//            tvCircleCount.setText("0");
//            tvCollectCount.setText("0");
//            tvTransferCount.setText("0");
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        return rootView;
    }

    public class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (TextUtils.equals(MINEFRGFOCUS, intent.getAction())) {
                onVisible();
            }
        }
    }


}
