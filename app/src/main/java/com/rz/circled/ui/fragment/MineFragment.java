package com.rz.circled.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
import com.rz.circled.ui.activity.AwesomeTabsAty;
import com.rz.circled.ui.activity.ContactsAty;
import com.rz.circled.ui.activity.LoginActivity;
import com.rz.circled.ui.activity.MyAccountAty;
import com.rz.circled.ui.activity.MyArticleActivity;
import com.rz.circled.ui.activity.MyPrivateGroupActivity;
import com.rz.circled.ui.activity.MyCollectionActivity;
import com.rz.circled.ui.activity.PersonInfoAty;
import com.rz.circled.ui.activity.PersonScanAty;
import com.rz.circled.ui.activity.SettingActivity;
import com.rz.circled.ui.activity.UserInfoActivity;
import com.rz.circled.widget.GlideCircleImage;
import com.rz.circled.widget.GlideRoundImage;
import com.rz.circled.widget.ObservableListView;
import com.rz.circled.widget.observable.ObservableScrollViewCallbacks;
import com.rz.circled.widget.observable.ScrollState;
import com.rz.common.adapter.CommonAdapter;
import com.rz.common.adapter.ViewHolder;
import com.rz.common.cache.preference.EntityCache;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.Constants;
import com.rz.common.constant.IntentCode;
import com.rz.common.constant.IntentKey;
import com.rz.common.constant.Type;
import com.rz.common.event.BaseEvent;
import com.rz.common.swiperefresh.SwipyRefreshLayout;
import com.rz.common.ui.fragment.BaseFragment;
import com.rz.common.utils.Protect;
import com.rz.common.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;

import static com.rz.circled.widget.CommomUtils.trackUser;

/**
 * Created by Gsm on 2017/8/29.
 */
public class MineFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    /**
     * 用户头像
     */
    ImageView mImgPersonHead;
    RelativeLayout idPersonNewsRela;
    TextView idPersonLoginDays;

    /**
     * 用户昵称
     */
    @BindView(R.id.title_content)
    RelativeLayout mTitleContent;
    /**
     * 展示内容
     */
    @BindView(R.id.id_comm_listview)
    ObservableListView mListView;

    @BindView(R.id.id_comm_refresh_ll)
    SwipyRefreshLayout swipeRefreshLayout;


    public static String URL = "https://wap.yryz.com/inviteRegister.html?inviter=";
    public static String MINEFRGFOCUS = "mine_focus_push";

    List<MineFragItemModel> mModelList;
    CommonAdapter adapter;
    TextView tvCircleCount;
    TextView tvTransferCount;
    TextView tvCollectCount;
    TextView tvActivityCount;

//    private SplashPresenter mSplashPresenter;
    protected IPresenter presenter;
    private MessageReceiver receiver;
    private CustormServiceModel mCustormServiceModel;
    private SharedPreferences mSp;

    private TextView mTxtPersonName;

    View header;
    View newTitilbar;
    private int headHight;


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
        newTitilbar = View.inflate(getActivity(), R.layout.titlebar_mine, null);
        newTitilbar.setBackgroundColor(getResources().getColor(R.color.color_main));
        TextView tv = (TextView) newTitilbar.findViewById(R.id.titlebar_main_tv);
        ImageView iv = (ImageView) newTitilbar.findViewById(R.id.titlebar_login_icon_img);
        iv.setImageResource(R.mipmap.ic_message);
        tv.setText("我的");
        mTitleContent.addView(newTitilbar);
//        idPersonNewsRela.setBackgroundColor(getResources().getColor(R.color.color_main));
        swipeRefreshLayout.setRefreshing(false);
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

        if (mListView.getHeaderViewsCount() == 0) {
            Log.d("yeying", "addview refreshUserInfoView");
            header = View.inflate(getActivity(), R.layout.header_show_frag, null);
            //个人中心
            header.findViewById(R.id.id_person_news_rela).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isLogin()) {jump(UserInfoActivity.class);

                    }
                }
            });

            //文章
            header.findViewById(R.id.btn_my_article).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isLogin()) {
                        jump(MyArticleActivity.class);
                    }
                }
            });

            //悬赏
            header.findViewById(R.id.btn_my_transfer).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isLogin()) {
//                        jump(Pci.class);
                    }
                }
            });

            //私圈
            header.findViewById(R.id.btn_my_circle).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isLogin()) {
                        MyPrivateGroupActivity.startMyPrivateGroup(mActivity,0);
                    }
                }
            });

            header.findViewById(R.id.btn_activity_collect).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isLogin()) {
                    }
                }
            });

            mImgPersonHead = (ImageView) header.findViewById(R.id.id_person_head_img);
            RelativeLayout bgRlyout = (RelativeLayout) header.findViewById(R.id.bg_rl_head);
            idPersonLoginDays = (TextView) header.findViewById(R.id.id_person_login_days);
            tvCircleCount = (TextView) header.findViewById(R.id.tv_circle_count);
            tvTransferCount = (TextView) header.findViewById(R.id.tv_transfer_count);
            tvCollectCount = (TextView) header.findViewById(R.id.tv_collect_count);
            tvActivityCount = (TextView) header.findViewById(R.id.tv_activity_count);

            bgRlyout.getBackground().setAlpha(77);
            if (Protect.checkLoadImageStatus(mActivity)) {
                Glide.with(mActivity).load(Session.getUserPicUrl()).transform(new GlideCircleImage(mActivity)).
                        placeholder(R.drawable.ic_default_head).error(R.drawable.ic_default_head).crossFade().into(mImgPersonHead);
            }

            mTxtPersonName = (TextView) header.findViewById(R.id.id_person_name_txt);
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

//            ImageView iv = (ImageView) header.findViewById(R.id.iv_show_top);
//            iv.getLayoutParams().height = ScreenUtil.getDisplayWidth() * 288 / 720;
//            iv.requestLayout();

//            ImageView iv = (ImageView) header.findViewById(R.id.id_person_news_rela);
//            iv.getLayoutParams().height = ScreenUtil.getDisplayWidth() * 288 / 720;
//            iv.requestLayout();
//            mListView.addHeaderView(header);
//            headHight = iv.getLayoutParams().height + DensityUtils.dip2px(mActivity, 20);
            mListView.addHeaderView(header);

            mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                }
            });

//            headHight = iv.getLayoutParams().height + DensityUtils.dip2px(mActivity, 20);
            mListView.setScrollViewCallbacks(new ObservableScrollViewCallbacks() {
                @Override
                public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
                    if (scrollY <= 0) {
                        newTitilbar.getBackground().mutate().setAlpha(0);
                    } else if (scrollY > 0 && scrollY <= headHight) {
                        float scale = (float) scrollY / headHight;
                        float alpha = (255 * scale);
                        // 只是layout背景透明(仿知乎滑动效果)
                        newTitilbar.getBackground().mutate().setAlpha((int) alpha);
                    } else {
                        newTitilbar.getBackground().mutate().setAlpha(255);
                    }
                }

                @Override
                public void onDownMotionEvent() {

                }

                @Override
                public void onUpOrCancelMotionEvent(ScrollState scrollState) {

                }
            });
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
//            ((V3CirclePresenter) presenter).getCircleStats("");
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
        MineFragItemModel model = new MineFragItemModel(getString(R.string.my_purchase), R.mipmap.ic_buy, false);
        MineFragItemModel mode2 = new MineFragItemModel(getString(R.string.v3_my_reward), R.mipmap.ic_reward, false);
        MineFragItemModel mode3 = new MineFragItemModel(getString(R.string.my_collect), R.mipmap.ic_colection, true);
        MineFragItemModel mode4 = new MineFragItemModel(getString(R.string.my_level), R.mipmap.ic_level, false);
        MineFragItemModel mode5 = new MineFragItemModel(getString(R.string.mine_my_account), R.mipmap.ic_count, false);
        MineFragItemModel mode6 = new MineFragItemModel(getString(R.string.mine_my_ticket), R.mipmap.ic_ticket, true);
        MineFragItemModel mode7 = new MineFragItemModel(getString(R.string.mine_my_contacts), R.mipmap.ic_addlist, false);
        MineFragItemModel mode8 = new MineFragItemModel(getString(R.string.mine_my_qrcode), R.mipmap.ic_code, false);
        MineFragItemModel mode9 = new MineFragItemModel(getString(R.string.mine_my_invite_friend), R.mipmap.ic_friend, true);

        MineFragItemModel mode10 = new MineFragItemModel(getString(R.string.v3_customer_service), R.mipmap.ic_custom_service, false);
        MineFragItemModel mode11 = new MineFragItemModel(getString(R.string.mine_my_setting), R.mipmap.ic_setting, false);


        mModelList.add(model);//我的购买
        mModelList.add(mode2);//我的打赏
        mModelList.add(mode3);//我的收藏

        mModelList.add(mode4);//我的等级
        mModelList.add(mode5);//我的账户

        mModelList.add(mode6);//我的卡卷
        mModelList.add(mode7);//通讯录

        mModelList.add(mode8);//我的二维码
        mModelList.add(mode9);//一键邀请好友
        mModelList.add(mode10);//联系客服
        mModelList.add(mode11);//设置

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

//    @OnClick()
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.btn_my_collect:
//                if (isLogin()) {
//                    trackUser("我的","入口名称","我的收藏");
////                    showActivity(mActivity, V3MyCollectedAty.class);
//                }
//                break;
//            case R.id.btn_my_transfer:
//                if (isLogin()) {
////                    trackUser("我的","入口名称","我的打赏");
////                    BaseInfo baseInfo = new BaseInfo();
////                    baseInfo.setCustId(Session.getUserId());
////                    baseInfo.setCustNname(Session.getUserName());
////                    baseInfo.setCustLocation(Session.getUser_area());
////                    baseInfo.setCustSignature(Session.getUser_signatrue());
////                    baseInfo.setCustImg(Session.getUserPicUrl());
////                    V3UserShareActivity.toUserShare(mActivity, baseInfo);
//                }
//                break;
//            case R.id.btn_my_circle:
//                if (isLogin()) {
////                    trackUser("我的","入口名称","我的圈子");
////                    BaseInfo baseInfo = new BaseInfo();
////                    baseInfo.setCustId(Session.getUserId());
////                    baseInfo.setCustNname(Session.getUserName());
////                    baseInfo.setCustLocation(Session.getUser_area());
////                    baseInfo.setCustSignature(Session.getUser_signatrue());
////                    baseInfo.setCustImg(Session.getUserPicUrl());
////                    V3UserCircleActivity.toUserCircle(frg, Session.getUserId(), baseInfo);
//                }
//                break;
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
            case 1:
                if (isLogin()) {
                    trackUser("我的", "入口名称", "我的购买");
//                    showActivity(frg, ContactsAty.class);

                }
                break;
            //我的账户
            case 2:
                if (isLogin()) {
                    trackUser("我的", "入口名称", "我的打赏");
//                    showActivity(frg, MyAccountAty.class);

                }
                break;

            case 3:
                if (isLogin()) {
                    trackUser("我的", "入口名称", "我的收藏");
                    jump(MyCollectionActivity.class);
                }
                break;

            //我的二维码
            case 4:
//                if (isLogin()) {
//                    showActivity(frg, MyCollectionAty.class);
//                }
                if (isLogin()) {
                    trackUser("我的", "入口名称", "我的等级");
//                    showActivity(frg, PersonScanAty.class);
                }
                break;
            //转发券

            case 5:
                if (isLogin()) {
                    trackUser("我的", "入口名称", "我的二维码");
                    jump(MyAccountAty.class);

                }
                break;

            case 6:
                if (isLogin()) {
                    trackUser("我的","入口名称","我的卡卷");
                    Intent intent = new Intent(getActivity(), AwesomeTabsAty.class);
                    intent.putExtra(IntentKey.KEY_TYPE, Type.TYPE_TICKET);
                    startActivity(intent);

                }
                break;

            case 7:
                if (isLogin()) {
                    trackUser("我的","入口名称","通讯录");
                    jump(ContactsAty.class);

                }
                break;

            case 8:
                if (isLogin()) {
                    trackUser("我的","入口名称","我的二维码");
                    jump(PersonScanAty.class);

                }
                break;

            case 9:
                if (isLogin()) {
                    trackUser("我的","入口名称","一键邀请好友");

                }
                break;

            //联系客服
            case 10:
                if (isLogin() && null != mCustormServiceModel) {
                    trackUser("我的", "入口名称", "联系客服");
                    starCustormService();
                } else {
//                    String customer_url = mSp.getString(Constants.CUSTOMER_SERVICE, "");
//                    CommH5Aty.startCommonH5(frg, customer_url);
//                }
                    break;
                }
                //设置
            case 11:
                trackUser("我的","入口名称","设置");
                Intent intent = new Intent(mActivity, SettingActivity.class);
                startActivityForResult(intent, IntentCode.MineFrg.MINE_REQUEST_CODE);
                break;
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
