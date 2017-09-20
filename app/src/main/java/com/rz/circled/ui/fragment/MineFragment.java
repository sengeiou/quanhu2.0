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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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
import com.rz.circled.presenter.impl.ProveInfoPresenter;
import com.rz.circled.presenter.impl.V3CirclePresenter;
import com.rz.circled.ui.activity.AwesomeTabsAty;
import com.rz.circled.ui.activity.ChooseProveIdentityActivity;
import com.rz.circled.ui.activity.ContactsAty;
import com.rz.circled.ui.activity.LoginActivity;
import com.rz.circled.ui.activity.MinePageActivity;
import com.rz.circled.ui.activity.MineRewardActivity;
import com.rz.circled.ui.activity.MyAccountAty;
import com.rz.circled.ui.activity.MyArticleActivity;
import com.rz.circled.ui.activity.MyBuyActivity;
import com.rz.circled.ui.activity.MyCollectionActivity;
import com.rz.circled.ui.activity.MyCouponsActivity;
import com.rz.circled.ui.activity.MyLevelActivity;
import com.rz.circled.ui.activity.MyPrivateGroupActivity;
import com.rz.circled.ui.activity.MyCollectionActivity;
import com.rz.circled.ui.activity.MyRewardActivity;
import com.rz.circled.ui.activity.PersonInfoAty;
import com.rz.circled.ui.activity.PersonScanAty;
import com.rz.circled.ui.activity.SettingActivity;
import com.rz.circled.ui.activity.UserInfoActivity;
import com.rz.circled.widget.GlideCircleImage;
import com.rz.circled.widget.GlideRoundImage;
import com.rz.circled.widget.ObservableListView;
import com.rz.common.adapter.CommonAdapter;
import com.rz.common.adapter.ViewHolder;
import com.rz.common.cache.preference.EntityCache;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.CommonCode;
import com.rz.common.constant.Constants;
import com.rz.common.constant.IntentCode;
import com.rz.common.constant.IntentKey;
import com.rz.common.constant.Type;
import com.rz.common.event.BaseEvent;
import com.rz.common.ui.fragment.BaseFragment;
import com.rz.common.utils.DensityUtils;
import com.rz.common.utils.Protect;
import com.rz.common.utils.StringUtils;
import com.rz.httpapi.api.ResponseData.ResponseData;
import com.rz.httpapi.bean.DataStatisticsBean;
import com.rz.httpapi.bean.UserSignBean;
import com.rz.httpapi.bean.ProveStatusBean;

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
    ScrollView swipeRefreshLayout;

    RelativeLayout signLayout;
    TextView titlebarSignTxt;
    ImageView scoreImg;

    private ProveStatusBean data;
    public static String URL = "https://wap.yryz.com/inviteRegister.html?inviter=";
    public static String MINEFRGFOCUS = "mine_focus_push";

    List<MineFragItemModel> mModelList;
    CommonAdapter adapter;
    TextView tvacticlesCount;        //文章
    TextView tvrewardCount;      //悬赏
    TextView tvcircletCount;        //私圈
    TextView tvactivityCount;       //活动

    //    private SplashPresenter mSplashPresenter;
    protected IPresenter presenter;
    private MessageReceiver receiver;
    private CustormServiceModel mCustormServiceModel;
    private SharedPreferences mSp;

    private TextView mTxtPersonName;
    private TextView levelTxt;
    private TextView custPointsTxt;
    private TextView famousTxt;
    private LinearLayout famousLayout;

    private TextView articleCount;
    private TextView rewardCount;
    private TextView circleCount;
    private TextView activityCount;

    View header;
    View newTitilbar;
    private int headHight;
    private ProveStatusBean proveStatusBean;
    private ProveInfoPresenter proveInfoPresenter;


    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_mine, null);
    }


    @Override
    public void initPresenter() {
        presenter = new V3CirclePresenter();
        presenter.attachView(this);

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
        getData();
        newTitilbar = View.inflate(getActivity(), R.layout.titlebar_mine, null);
        newTitilbar.setBackgroundColor(getResources().getColor(R.color.color_main));
        newTitilbar.getBackground().setAlpha(0);
        TextView tv = (TextView) newTitilbar.findViewById(R.id.titlebar_main_tv);
        ImageView ib = (ImageView) newTitilbar.findViewById(R.id.titlebar_main_left_btn);
        signLayout = (RelativeLayout) newTitilbar.findViewById(R.id.sign_layout);
        signLayout.setVisibility(View.VISIBLE);
        titlebarSignTxt = (TextView) newTitilbar.findViewById(R.id.titlebar_login_icon_img);
        scoreImg = (ImageView) newTitilbar.findViewById(R.id.scores_img);

        ib.setVisibility(View.VISIBLE);
        ib.setImageResource(R.mipmap.ic_message);

        tv.setText("我的");
        mTitleContent.addView(newTitilbar);

        signLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((V3CirclePresenter) presenter).signRequest(Session.getUserId(), "15");
            }
        });
//        idPersonNewsRela.setBackgroundColor(getResources().getColor(R.color.color_main));
//        swipeRefreshLayout.setRefreshing(false);

        getUserProveStatus();
    }

    private void getData(){

        if(presenter != null){
            //获取签到状态
            ((V3CirclePresenter) presenter).getSignStatus(Session.getUserId(),"15");

            //获取数据统计
            ((V3CirclePresenter) presenter).getUserStat(Session.getUserId());

            //获取活动数统计
            ((V3CirclePresenter) presenter).getMylistCount(Session.getUserId());

            //获取达人信息
            ((V3CirclePresenter) presenter).getFamousStatus(Session.getUserId());
        }

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
            header = View.inflate(getActivity(), R.layout.header_show_frag, null);
            //个人中心
            header.findViewById(R.id.id_person_news_rela).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isLogin()) {
                        UserInfoActivity.newFrindInfo(mActivity,Session.getUserId());

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
                        jump(MyRewardActivity.class);
                    }
                }
            });

            //私圈
            header.findViewById(R.id.btn_my_circle).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isLogin()) {
                        MyPrivateGroupActivity.startMyPrivateGroup(mActivity, 0);
                    }
                }
            });

            //活动
            header.findViewById(R.id.btn_activity_collect).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isLogin()) {
                        jump(MinePageActivity.class);
                    }
                }
            });

            mImgPersonHead = (ImageView) header.findViewById(R.id.id_person_head_img);
            RelativeLayout bgRlyout = (RelativeLayout) header.findViewById(R.id.bg_rl_head);
            idPersonLoginDays = (TextView) header.findViewById(R.id.id_person_login_days);
            tvacticlesCount = (TextView) header.findViewById(R.id.tv_circle_count);
            tvrewardCount = (TextView) header.findViewById(R.id.tv_transfer_count);
            tvcircletCount = (TextView) header.findViewById(R.id.tv_collect_count);
            tvactivityCount = (TextView) header.findViewById(R.id.tv_activity_count);

            bgRlyout.getBackground().setAlpha(77);
            if (Protect.checkLoadImageStatus(mActivity)) {
                Glide.with(mActivity).load(Session.getUserPicUrl()).transform(new GlideCircleImage(mActivity)).
                        placeholder(R.drawable.ic_default_head).error(R.drawable.ic_default_head).crossFade().into(mImgPersonHead);
            }

            mTxtPersonName = (TextView) header.findViewById(R.id.id_person_name_txt);
            levelTxt = (TextView) header.findViewById(R.id.level_txt);
            custPointsTxt = (TextView) header.findViewById(R.id.cust_points_txt);
            famousTxt = (TextView) header.findViewById(R.id.famous_txt);
            famousLayout = (LinearLayout) header.findViewById(R.id.famous_layout);
            famousLayout.getBackground().setAlpha(77);

            famousLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if("0".equals(Session.getCustRole())){
                        Intent intent = new Intent(mActivity, ChooseProveIdentityActivity.class);
                        if (proveStatusBean != null)
                            intent.putExtra(IntentKey.EXTRA_SERIALIZABLE, proveStatusBean);
                        startActivity(intent);
//                    }
                }
            });

            if (Session.getUserIsLogin()) {
                mTxtPersonName.setText(Session.getUserName());
                levelTxt.setText("Lv. "+Session.getUserLevel());
                custPointsTxt.setText("积分 " + Session.getCustPoints());
                if("0".equals(Session.getCustRole())){
                    famousTxt.setText("去认证");
                }

                if (TextUtils.isEmpty(Session.getUser_signatrue())){
                    idPersonLoginDays.setText("");
                }
                else{
                    idPersonLoginDays.setText("个性签名：" + Session.getUser_signatrue());
                }
            } else {
                mTxtPersonName.setText(getString(R.string.mine_no_login));
                idPersonLoginDays.setText("");
            }

            headHight = 146 + DensityUtils.dip2px(mActivity, 20);
            mListView.addHeaderView(header);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                swipeRefreshLayout.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                    @Override
                    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                        if (scrollY <= 0) {
                            newTitilbar.getBackground().mutate().setAlpha(0);
                            signLayout.setVisibility(View.VISIBLE);
                        } else if (scrollY > 0 && scrollY <= headHight) {
                            float scale = (float) scrollY / headHight;
                            float alpha = (255 * scale);
                            // 只是layout背景透明(仿知乎滑动效果)
                            newTitilbar.getBackground().mutate().setAlpha((int) alpha);
                        } else {
                            newTitilbar.getBackground().mutate().setAlpha(255);
                            signLayout.setVisibility(View.GONE);
                        }
                    }
                });
            }

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
        // TODO: 2017/9/18 判断是否有申请达人,未申请则不去请求
        getUserProveStatus();
        setData();
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
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);

        mModelList = new ArrayList<MineFragItemModel>();
        MineFragItemModel model = new MineFragItemModel(false, getString(R.string.my_purchase), R.mipmap.ic_buy, false);
        MineFragItemModel mode2 = new MineFragItemModel(false, getString(R.string.v3_my_reward), R.mipmap.ic_reward, false);
        MineFragItemModel mode3 = new MineFragItemModel(false, getString(R.string.my_collect), R.mipmap.ic_colection, true);
        MineFragItemModel mode4 = new MineFragItemModel(false, getString(R.string.my_level), R.mipmap.ic_level, false);
        MineFragItemModel mode5 = new MineFragItemModel(false, getString(R.string.mine_my_account), R.mipmap.ic_count, false);
        MineFragItemModel mode6 = new MineFragItemModel(false, getString(R.string.mine_my_ticket), R.mipmap.ic_ticket, true);
        MineFragItemModel mode7 = new MineFragItemModel(false, getString(R.string.mine_my_contacts), R.mipmap.ic_addlist, false);
        MineFragItemModel mode8 = new MineFragItemModel(false, getString(R.string.mine_my_qrcode), R.mipmap.ic_code, false);
        MineFragItemModel mode9 = new MineFragItemModel(false, getString(R.string.mine_my_invite_friend), R.mipmap.ic_friend, true);

        MineFragItemModel mode10 = new MineFragItemModel(false, getString(R.string.v3_customer_service), R.mipmap.ic_custom_service, false);
        MineFragItemModel mode11 = new MineFragItemModel(false, getString(R.string.mine_my_setting), R.mipmap.ic_setting, false);


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
        }else if(t instanceof UserSignBean){
            UserSignBean signBean = (UserSignBean)t;
            if(signBean.isSignFlag()){
                scoreImg.setVisibility(View.GONE);
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                lp.addRule(RelativeLayout.CENTER_IN_PARENT);
                titlebarSignTxt.setTextColor(getResources().getColor(R.color.sign_color));
                titlebarSignTxt.setText("已签到");
                titlebarSignTxt.setLayoutParams(lp);

            }else{
                titlebarSignTxt.setTextColor(getResources().getColor(R.color.white));
                titlebarSignTxt.setText("签到");
                scoreImg.setVisibility(View.VISIBLE);
            }
        }else if(t instanceof ResponseData){
            //签到成功
            scoreImg.setVisibility(View.GONE);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp.addRule(RelativeLayout.CENTER_IN_PARENT);
            titlebarSignTxt.setTextColor(getResources().getColor(R.color.sign_color));
            titlebarSignTxt.setText("已签到");
            titlebarSignTxt.setLayoutParams(lp);

        }else if(t instanceof DataStatisticsBean) {
            DataStatisticsBean data = (DataStatisticsBean) t;

            tvacticlesCount.setText(data.getArticleNum()+"");
            tvrewardCount.setText(data.getOfferNum()+"");
            tvcircletCount.setText(data.getCoterieNum()+"");
//            tvactivityCount.setText(data.getArticleNum()+"");
        }else if(t instanceof  ProveStatusBean) {
            data = (ProveStatusBean) t;
            if(Session.getCustRole().equals("0")){
                famousTxt.setText("去认证");
            }else if(data.getAuthStatus() == 0){
                famousTxt.setText("认证审核中");
            }else if(data.getAuthStatus() == 1){
                famousTxt.setText(data.getTradeField());
            }else if(data.getAuthStatus() == 2){
                famousTxt.setText("认证失败");
            }else if(data.getAuthStatus() == 3){
                famousTxt.setText("认证失败");
            }
        }else if(t instanceof ResponseData){
            tvactivityCount.setText(((ResponseData) t).getData()+"");
        }
    }

    @Override
    public <T> void updateViewWithFlag(T t, int flag) {
        super.updateViewWithFlag(t, flag);
        if (flag == ProveInfoPresenter.FLAG_PROVE_STATUS_SUCCESS) {//获得达人信息申请状态成功
            proveStatusBean = (ProveStatusBean) t;
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

            case R.id.id_person_head_img:
                if (isLogin()) {
                    trackUser("我的", "入口名称", "我的头像");
//                    V3FriendInfoActivity.newFrindInfo(frg, Session.getUserId());
                }
                break;
            //查看个人详情信息
            case R.id.id_person_news_rela:
                if (Session.getUserIsLogin()) {
                    trackUser("我的", "入口名称", "个人详情");
                    jump(PersonInfoAty.class);

                } else {
                    Intent login = new Intent(mActivity, LoginActivity.class);
                    startActivityForResult(login, IntentCode.Login.LOGIN_REQUEST_CODE);
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            //通讯录
            case 1:
                if (isLogin()) {
                    trackUser("我的", "入口名称", "我的购买");
                    jump(MyBuyActivity.class);

                }
                break;
            //我的账户
            case 2:
                if (isLogin()) {
                    trackUser("我的", "入口名称", "我的打赏");
//                    showActivity(frg, MyAccountAty.class);
                    jump(MineRewardActivity.class);
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
                    jump(MyLevelActivity.class);
                }
                break;
            //转发券

            case 5:
                if (isLogin()) {
                    trackUser("我的", "入口名称", "账户");
                    jump(MyAccountAty.class);

                }
                break;

            case 6:
                if (isLogin()) {
                    trackUser("我的", "入口名称", "我的卡卷");
                    Intent intent = new Intent(getActivity(), AwesomeTabsAty.class);
                    intent.putExtra(IntentKey.KEY_TYPE, Type.TYPE_TICKET);
                    startActivity(intent);

                }
                break;

            case 7:
                if (isLogin()) {
                    trackUser("我的", "入口名称", "通讯录");
                    jump(ContactsAty.class);

                }
                break;

            case 8:
                if (isLogin()) {
                    trackUser("我的", "入口名称", "我的二维码");
                    jump(PersonScanAty.class);

                }
                break;

            case 9:
                if (isLogin()) {
                    trackUser("我的", "入口名称", "一键邀请好友");

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
                trackUser("我的", "入口名称", "设置");
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
                tvacticlesCount.setText("0");
                tvrewardCount.setText("0");
                tvcircletCount.setText("0");

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
    public void onEvent(BaseEvent baseEvent) {
        if (baseEvent.type == CommonCode.EventType.PROVE_UPDATE) {
            getUserProveStatus();
            return;
        }
        if (baseEvent.type == CommonCode.EventType.TYPE_LOGOUT) {
            if (proveStatusBean != null)
                proveStatusBean = null;
            return;
        }
        if (baseEvent.type == CommonCode.EventType.TYPE_LOGIN) {
            if (proveInfoPresenter != null)
                getUserProveStatus();
                setData();
        }
        if(baseEvent.type == CommonCode.EventType.TYPE_USER_UPDATE){
            setData();
        }
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


    /**
     * 获得个人认证状态
     */
    private void getUserProveStatus() {
        if (proveInfoPresenter == null) {
            proveInfoPresenter = new ProveInfoPresenter();
            proveInfoPresenter.attachView(this);
        }
        proveInfoPresenter.getProveStatus();
    }


    private void setData(){
        mTxtPersonName.setText(Session.getUserName());
        levelTxt.setText("Lv. "+Session.getUserLevel());
        custPointsTxt.setText("积分 " + Session.getCustPoints());

        Glide.with(mActivity).load(Session.getUserPicUrl()).transform(new GlideCircleImage(mActivity)).
                placeholder(R.drawable.ic_default_head).error(R.drawable.ic_default_head).crossFade().into(mImgPersonHead);

        if("0".equals(Session.getCustRole())){
            famousTxt.setText("去认证");
        }

        if (TextUtils.isEmpty(Session.getUser_signatrue())){
            idPersonLoginDays.setText("");
        }
        else{
            idPersonLoginDays.setText("个性签名：" + Session.getUser_signatrue());
        }
        getData();

    }



}
