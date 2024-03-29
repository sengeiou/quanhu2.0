package com.rz.circled.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.rz.circled.R;
import com.rz.circled.event.EventConstant;
import com.rz.circled.modle.CircleStatsModel;
import com.rz.circled.modle.CustormServiceModel;
import com.rz.circled.modle.MineFragItemModel;
import com.rz.circled.presenter.IPresenter;
import com.rz.circled.presenter.impl.AccountPresenter;
import com.rz.circled.presenter.impl.FriendPresenter1;
import com.rz.circled.presenter.impl.ProveInfoPresenter;
import com.rz.circled.presenter.impl.V3CirclePresenter;
import com.rz.circled.ui.activity.ChooseProveIdentityActivity;
import com.rz.circled.ui.activity.CommonH5Activity;
import com.rz.circled.ui.activity.ContactsAty;
import com.rz.circled.ui.activity.InviteRewardActivity;
import com.rz.circled.ui.activity.MinePageActivity;
import com.rz.circled.ui.activity.MineRewardActivity;
import com.rz.circled.ui.activity.MyAccountAty;
import com.rz.circled.ui.activity.MyArticleActivity;
import com.rz.circled.ui.activity.MyBuyActivity;
import com.rz.circled.ui.activity.MyCollectionActivity;
import com.rz.circled.ui.activity.MyCouponsActivity;
import com.rz.circled.ui.activity.MyLevelActivity;
import com.rz.circled.ui.activity.MyPrivateGroupActivity;
import com.rz.circled.ui.activity.MyRewardActivity;
import com.rz.circled.ui.activity.NewsActivity;
import com.rz.circled.ui.activity.PersonScanAty;
import com.rz.circled.ui.activity.ScoreDetailAty;
import com.rz.circled.ui.activity.SettingActivity;
import com.rz.circled.ui.activity.UserInfoActivity;
import com.rz.circled.widget.GlideCircleImage;
import com.rz.circled.widget.MyScrollView;
import com.rz.circled.widget.ObservableListView;
import com.rz.common.adapter.CommonAdapter;
import com.rz.common.adapter.ViewHolder;
import com.rz.common.cache.preference.EntityCache;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.CommonCode;
import com.rz.common.constant.Constants;
import com.rz.common.constant.H5Address;
import com.rz.common.constant.IntentCode;
import com.rz.common.constant.Type;
import com.rz.common.event.BaseEvent;
import com.rz.common.permission.AfterPermissionGranted;
import com.rz.common.permission.AppSettingsDialog;
import com.rz.common.permission.EasyPermissions;
import com.rz.common.ui.fragment.BaseFragment;
import com.rz.common.utils.DensityUtils;
import com.rz.common.utils.Protect;
import com.rz.common.utils.StringUtils;
import com.rz.httpapi.api.ResponseData.ResponseData;
import com.rz.httpapi.bean.DataStatisticsBean;
import com.rz.httpapi.bean.FriendInformationBean;
import com.rz.httpapi.bean.ProveStatusBean;
import com.rz.httpapi.bean.UserInviteLinkBean;
import com.rz.httpapi.bean.UserSignBean;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.UpgradeInfo;
import com.zbar.lib.activity.CaptureActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;

import static com.rz.common.utils.SystemUtils.trackUser;


/**
 * Created by Gsm on 2017/8/29.
 */
public class MineFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    private final int REQUEST_CAMER_CODE = 100;

    /**
     * 用户头像
     */
    ImageView mImgPersonHead;
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
    MyScrollView scrollViewLayout;

    RelativeLayout signLayout;
    TextView titlebarSignTxt;
    TextView titlebarSigned;
    ImageView scoreImg;
    private ImageView mUnread;

    List<MineFragItemModel> mModelList;
    CommonAdapter adapter;
    TextView tvacticlesCount;        //文章
    TextView tvrewardCount;      //悬赏
    TextView tvcircletCount;        //私圈
    TextView tvactivityCount;       //活动


    protected IPresenter presenter;
    protected IPresenter userPresenter;
    protected IPresenter accountPresenter;
    private CustormServiceModel mCustormServiceModel;
    private SharedPreferences mSp;

    private TextView mTxtPersonName;
    private TextView levelTxt;
    private TextView famousTxt;
    private LinearLayout famousLayout;
//    private TextView loginTxt;

    TextView tv_remark;

    View header;
    View newTitilbar;
    private int headHight;
    private ProveStatusBean proveStatusBean;
    private ProveInfoPresenter proveInfoPresenter;
    ProveStatusBean data;
    UserSignBean signBean = new UserSignBean();

    MineFragItemModel modeScore;

    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_mine, null);
    }

    @Override
    public void initPresenter() {
        presenter = new V3CirclePresenter();
        presenter.attachView(this);

        userPresenter = new FriendPresenter1();
        userPresenter.attachView(this);

        accountPresenter = new AccountPresenter();
        accountPresenter.attachView(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void initView() {
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);

        mSp = getContext().getSharedPreferences("", Context.MODE_PRIVATE);
        newTitilbar = View.inflate(getActivity(), R.layout.titlebar_mine, null);
        newTitilbar.setBackgroundColor(getResources().getColor(R.color.mine_blue_color));
        newTitilbar.getBackground().setAlpha(0);
        TextView tv = (TextView) newTitilbar.findViewById(R.id.titlebar_main_tv);
        ImageView ib = (ImageView) newTitilbar.findViewById(R.id.titlebar_main_left_btn);
        mUnread = (ImageView) newTitilbar.findViewById(R.id.unread_msg_number);
        signLayout = (RelativeLayout) newTitilbar.findViewById(R.id.sign_layout);
        signLayout.setVisibility(View.VISIBLE);
        titlebarSignTxt = (TextView) newTitilbar.findViewById(R.id.titlebar_login_icon_img);
        titlebarSigned = (TextView) newTitilbar.findViewById(R.id.titlebar_signed);
        scoreImg = (ImageView) newTitilbar.findViewById(R.id.scores_img);

        ib.setVisibility(View.VISIBLE);
        ib.setImageResource(R.mipmap.ic_message);
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity, NewsActivity.class));
            }
        });

        tv.setText("我的");
        mTitleContent.addView(newTitilbar);

        signLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLogin()) {
                    if (signBean != null && !signBean.isSignFlag()) {
                        ((V3CirclePresenter) presenter).signRequest(Session.getUserId(), "15");
                    }
                }
            }
        });


        /**
         * 获得个人认证状态
         */
//        getUserProveStatus();
    }


    /**
     * 获取我的头部栏数据
     */
    private void getHeadData() {

        if (presenter != null) {
            //获取签到状态
            ((V3CirclePresenter) presenter).getSignStatus(Session.getUserId(), "15");

            //获取数据统计
            ((V3CirclePresenter) presenter).getUserStat(Session.getUserId());

            //获取活动数统计
            ((V3CirclePresenter) presenter).getMylistCount(Session.getUserId());

            //获取达人信息
            ((V3CirclePresenter) presenter).getFamousStatus(Session.getUserId());

            //更新用户详情
            ((FriendPresenter1) userPresenter).getFriendInfoDetail(Session.getUserId());

            ((AccountPresenter) accountPresenter).getInviteLink();
        }
    }

    //初始化用户信息
    public void initUserNews() {

        if (mListView.getHeaderViewsCount() == 0) {
            header = View.inflate(getActivity(), R.layout.header_show_frag, null);
            HeadOnCLickListener headOnCLickListener = new HeadOnCLickListener();

            header.findViewById(R.id.bg_rl_head).setOnClickListener(headOnCLickListener);
            header.findViewById(R.id.btn_my_article).setOnClickListener(headOnCLickListener);
            header.findViewById(R.id.btn_my_transfer).setOnClickListener(headOnCLickListener);
            header.findViewById(R.id.btn_my_circle).setOnClickListener(headOnCLickListener);
            header.findViewById(R.id.btn_activity_collect).setOnClickListener(headOnCLickListener);


            mImgPersonHead = (ImageView) header.findViewById(R.id.id_person_head_img);
            RelativeLayout bgRlyout = (RelativeLayout) header.findViewById(R.id.bg_rl_head);
            idPersonLoginDays = (TextView) header.findViewById(R.id.id_person_login_days);
            tvacticlesCount = (TextView) header.findViewById(R.id.tv_circle_count);
            tvrewardCount = (TextView) header.findViewById(R.id.tv_transfer_count);
            tvcircletCount = (TextView) header.findViewById(R.id.tv_collect_count);
            tvactivityCount = (TextView) header.findViewById(R.id.tv_activity_count);

            idPersonLoginDays.setTextColor(Color.argb(166, 255, 255, 255)); //背景透明度

            if (Protect.checkLoadImageStatus(mActivity)) {
                Glide.with(mActivity).load(Session.getUserPicUrl()).transform(new GlideCircleImage(mActivity)).
                        placeholder(R.drawable.ic_default_head).error(R.drawable.ic_default_head).crossFade().into(mImgPersonHead);
            }

            mTxtPersonName = (TextView) header.findViewById(R.id.id_person_name_txt);
            levelTxt = (TextView) header.findViewById(R.id.level_txt);
            famousTxt = (TextView) header.findViewById(R.id.famous_txt);
            famousLayout = (LinearLayout) header.findViewById(R.id.famous_layout);
//            loginTxt = (TextView) header.findViewById(R.id.login_txt);

            famousLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ChooseProveIdentityActivity.startProveIdentity(mActivity, proveStatusBean);
                }
            });


            if (Session.getUserIsLogin()) {
                setData();
            } else {

//                loginTxt.setVisibility(View.VISIBLE);
//                mTxtPersonName.setText(getString(R.string.mine_no_login));
                idPersonLoginDays.setText("");
            }

            headHight = 146 + DensityUtils.dip2px(mActivity, 20);
            mListView.addHeaderView(header);

            scrollViewLayout.setOnScrollChanged(new MyScrollView.OnScrollChanged() {
                @Override
                public void onScroll(int scrollX, int scrollY, int oldX, int oldY) {
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


    class HeadOnCLickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                //个人中心
                case R.id.bg_rl_head:
                    if (isLogin()) {
                        UserInfoActivity.newFrindInfo(mActivity, Session.getUserId());
                    }
                    break;
                //资源
                case R.id.btn_my_article:
                    if (isLogin()) {
                        jump(MyArticleActivity.class);
                    }
                    break;
                //悬赏
                case R.id.btn_my_transfer:
                    if (isLogin()) {
                        jump(MyRewardActivity.class);
                    }
                    break;
                //私圈
                case R.id.btn_my_circle:
                    if (isLogin()) {
                        MyPrivateGroupActivity.startMyPrivateGroup(mActivity, 0);
                    }
                    break;
                //活动
                case R.id.btn_activity_collect:
                    if (isLogin()) {
                        jump(MinePageActivity.class);
                    }
                    break;
            }
        }
    }

    private void checkUpdate() {
        /***** 获取升级信息 *****/
        UpgradeInfo upgradeInfo = Beta.getUpgradeInfo();
        boolean update = Session.getUserIsLogin() && upgradeInfo != null && !mModelList.isEmpty() && null != adapter;
        for (MineFragItemModel item : mModelList) {
            if (TextUtils.equals(item.getName(), getString(R.string.mine_my_setting))) {
                item.setUpdate(update);
                adapter.notifyDataSetChanged();
                return;
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
        requestUnreadMsg();

        getUserProveStatus();
        if (Session.getUserIsLogin()) {
            setData();
        } else {
//            loginTxt.setVisibility(View.VISIBLE);
//            mTxtPersonName.setText(getString(R.string.mine_no_login));
            idPersonLoginDays.setText("");
        }
        if (!isNotity()) {
            EventBus.getDefault().post(new BaseEvent(EventConstant.NEWS_COME_UNREAD));
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        requestUnreadMsg();
//        if (null != mTxtPersonName && null != mImgPersonHead) {
//            initUserNews();
//        }
        getUserProveStatus();
        if (Session.getUserIsLogin()) {
            setData();
        } else {
//            loginTxt.setVisibility(View.VISIBLE);
//            mTxtPersonName.setText(getString(R.string.mine_no_login));
            idPersonLoginDays.setText("");
        }
        if (!isNotity()) {
            EventBus.getDefault().post(new BaseEvent(EventConstant.NEWS_COME_UNREAD));
        }
    }

    @Subscribe
    public void eventBus(BaseEvent event) {
        switch (event.getType()) {
            case EventConstant.NEWS_UNREAD_CHANGE:
                requestUnreadMsg();
                break;
        }
    }

    @Override
    public void onLoadingStatus(int loadingStatus, String string) {
    }


    @Override
    public void initData() {
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);

        initUserNews();

        mModelList = new ArrayList<>();
        MineFragItemModel modeBuy = new MineFragItemModel(true, getString(R.string.my_purchase), R.mipmap.ic_buy, false);
        MineFragItemModel modeReward = new MineFragItemModel(true, getString(R.string.v3_my_reward), R.mipmap.ic_reward, false);
        MineFragItemModel modeColection = new MineFragItemModel(true, getString(R.string.my_collect), R.mipmap.ic_colection, true);
        modeScore = new MineFragItemModel(true, Session.getCustPoints(), getString(R.string.my_score), R.mipmap.ic_score, false);
        MineFragItemModel modeLevel = new MineFragItemModel(true, getString(R.string.my_level), R.mipmap.ic_level, false);
        MineFragItemModel modeAcount = new MineFragItemModel(true, getString(R.string.mine_my_account), R.mipmap.ic_count, false);
        MineFragItemModel modeTicket = new MineFragItemModel(true, getString(R.string.mine_my_ticket), R.mipmap.ic_ticket, true);
        MineFragItemModel modeAddlist = new MineFragItemModel(true, getString(R.string.mine_my_contacts), R.mipmap.ic_addlist, false);
        MineFragItemModel modeCode = new MineFragItemModel(true, getString(R.string.mine_my_qrcode), R.mipmap.ic_code, false);
        MineFragItemModel modeScan = new MineFragItemModel(true, getString(R.string.add_friend_sweep), R.mipmap.icon_me_scan, false);
        MineFragItemModel modeFriend = new MineFragItemModel(true, getString(R.string.mine_my_invite_friend), R.mipmap.ic_friend, true);
        MineFragItemModel modeService = new MineFragItemModel(true, getString(R.string.v3_customer_service), R.mipmap.ic_custom_service, false);
        MineFragItemModel modeHelp = new MineFragItemModel(true, getString(R.string.mine_my_kefu), R.mipmap.ic_help, false);
        MineFragItemModel modeSetting = new MineFragItemModel(true, getString(R.string.mine_my_setting), R.mipmap.ic_setting, false);


        mModelList.add(modeBuy);//我的购买
        mModelList.add(modeReward);//我的打赏
        mModelList.add(modeColection);//我的收藏
        mModelList.add(modeScore);//我的积分

        mModelList.add(modeLevel);//我的等级
        mModelList.add(modeAcount);//我的账户

        mModelList.add(modeTicket);//我的卡卷
        mModelList.add(modeAddlist);//通讯录

        mModelList.add(modeCode);//我的二维码
        mModelList.add(modeScan);//扫一扫
        mModelList.add(modeFriend);//一键邀请好友
        mModelList.add(modeService);//联系客服
        mModelList.add(modeHelp);//帮助
        mModelList.add(modeSetting);//设置

        mListView.setAdapter(adapter = new CommonAdapter<MineFragItemModel>(mActivity, mModelList, R.layout.adp_mine_frg) {

            @Override
            public void convert(ViewHolder helper, MineFragItemModel item, int position) {

                View mDivider = helper.getViewById(R.id.id_divider_v);
                LinearLayout dividerLayout = (LinearLayout) helper.getViewById(R.id.id_divider_layout);

                View mLine = helper.getViewById(R.id.id_line_v);
                ImageView isUpdate = (ImageView) helper.getViewById(R.id.version_update);
                TextView mContactNum = (TextView) helper.getViewById(R.id.id_contact_num_update);

                if (item.isDivider()) {
                    mDivider.setVisibility(View.VISIBLE);
                    dividerLayout.setVisibility(View.VISIBLE);
                    mLine.setVisibility(View.GONE);
                } else {
                    mDivider.setVisibility(View.GONE);
                    dividerLayout.setVisibility(View.GONE);
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
                tv_remark = (TextView) helper.getViewById(R.id.id_remark);
                if (TextUtils.isEmpty(item.remark)) {
                    tv_remark.setVisibility(View.GONE);
                } else {
                    if (Session.getUserIsLogin()) {
                        tv_remark.setVisibility(View.VISIBLE);
                        tv_remark.setText(item.remark);
                    }
                }

                helper.setImageResource(R.id.id_icon_img, item.getDrawable());
                helper.setText(R.id.id_name_txt, item.getName());
            }
        });
        mListView.setOnItemClickListener(this);

        if (Session.getUserIsLogin()) {
            EntityCache<CircleStatsModel> entityCache = new EntityCache<>(mActivity, CircleStatsModel.class);
            CircleStatsModel circleStatsModel = entityCache.getEntity(CircleStatsModel.class);
            if (circleStatsModel != null) {
                updateView(circleStatsModel);
            }
        }

        checkUpdate();
    }

    @Override
    public <T> void updateView(T t) {
        if (t instanceof CustormServiceModel) {
            mCustormServiceModel = (CustormServiceModel) t;
            if (null != mCustormServiceModel) {
                String messageUrl = mCustormServiceModel.getMessageUrl();
                mSp.edit().putString(Constants.CUSTOMER_SERVICE, messageUrl).commit();
            }
        } else if (t instanceof UserSignBean) {
            signBean = (UserSignBean) t;
            if (signBean.isSignFlag()) {
                scoreImg.setVisibility(View.GONE);
                titlebarSignTxt.setVisibility(View.GONE);
                titlebarSigned.setVisibility(View.VISIBLE);

                titlebarSigned.setTextColor(Color.argb(168, 1, 133, 255));   //文字透明度
                titlebarSigned.setText("已签到");

            } else {

                titlebarSignTxt.setTextColor(Color.argb(255, 1, 133, 255));   //文字透明度
                titlebarSignTxt.setText("签到");

                scoreImg.setVisibility(View.VISIBLE);
                titlebarSignTxt.setVisibility(View.VISIBLE);
                titlebarSigned.setVisibility(View.GONE);
            }
        } else if (t instanceof DataStatisticsBean) {
            DataStatisticsBean data = (DataStatisticsBean) t;

            tvacticlesCount.setText(data.getArticleNum() + "");
            tvrewardCount.setText(data.getOfferNum() + "");
            tvcircletCount.setText(data.getCoterieNum() + "");

            modeScore.remark = data.getScore();
            adapter.notifyDataSetChanged();
            Session.setCustPoints(data.getScore());

            levelTxt.setText("Lv. " + data.getCustLevel());
            Session.setUserLevel(data.getCustLevel());
        } else if (t instanceof ProveStatusBean) {
            data = (ProveStatusBean) t;

            if (data.getAuthStatus() == ProveStatusBean.STATUS_ING) {
                if (famousLayout != null && famousLayout.getBackground() != null) {
                    famousLayout.setBackgroundResource(R.drawable.shape_white_bg1);
                    famousLayout.getBackground().setAlpha(255);
                }
                famousTxt.setText("认证审核中");
            } else if (data.getAuthStatus() == ProveStatusBean.STATUS_SUCCESS) {
                if (famousLayout != null && famousLayout.getBackground() != null) {
                    famousLayout.setBackgroundResource(R.drawable.shape_white_bg);
                    famousLayout.getBackground().setAlpha(0);
                }
                famousTxt.setTextColor(ContextCompat.getColor(mActivity, R.color.color_fff000));
                famousTxt.setText(data.getTradeField());
            } else if (data.getAuthStatus() == ProveStatusBean.STATUS_FAIL) {
                if (famousLayout != null && famousLayout.getBackground() != null) {
                    famousLayout.setBackgroundResource(R.drawable.shape_white_bg1);
                    famousLayout.getBackground().setAlpha(255);
                }
                famousTxt.setText("认证失败");
            } else if (data.getAuthStatus() == ProveStatusBean.STATUS_CANCEL || data.getAuthStatus() == ProveStatusBean.STATUS_NORMAL) {
                if (famousLayout != null && famousLayout.getBackground() != null) {
                    famousLayout.setBackgroundResource(R.drawable.shape_white_bg1);
                    famousLayout.getBackground().setAlpha(255);
                }
                famousTxt.setText("达人认证");
            }

        } else if (t instanceof ResponseData) {
            if (((ResponseData) t).getData() != null) {

                NumberFormat nf = new DecimalFormat("#");
                double num = (double) ((ResponseData) t).getData();

                tvactivityCount.setText(nf.format(num) + "");
            }
        } else if (t instanceof FriendInformationBean) {
            if (t != null) {
                FriendInformationBean bean = (FriendInformationBean) t;

                if (Protect.checkLoadImageStatus(mActivity)) {
                    Glide.with(mActivity).load(bean.getCustImg()).transform(new GlideCircleImage(mActivity)).
                            placeholder(R.drawable.ic_default_head).error(R.drawable.ic_default_head).crossFade().into(mImgPersonHead);
                }

                Session.setUserPicUrl(bean.getCustImg());
            }
        } else if (t instanceof UserInviteLinkBean) {
            UserInviteLinkBean data = (UserInviteLinkBean) t;
            Session.setInviteLink(data.getInviterLink());
        }
    }

    @Override
    public <T> void updateViewWithFlag(T t, int flag) {
        super.updateViewWithFlag(t, flag);
        if (flag == ProveInfoPresenter.FLAG_PROVE_STATUS_SUCCESS) {//获得达人信息申请状态成功
            proveStatusBean = (ProveStatusBean) t;
            if (proveStatusBean == null) {
                famousLayout.setVisibility(View.GONE);
                return;
            }
            famousLayout.setVisibility(View.VISIBLE);
            if (proveStatusBean.getAuthStatus() == ProveStatusBean.STATUS_ING) {
                if (famousLayout != null && famousLayout.getBackground() != null) {
                    famousLayout.getBackground().setAlpha(255);
                    famousLayout.setBackgroundResource(R.drawable.shape_white_bg1);

                }
                famousTxt.setText("认证审核中");
            } else if (proveStatusBean.getAuthStatus() == ProveStatusBean.STATUS_SUCCESS) {
                if (famousLayout != null  && famousLayout.getBackground() != null) {
                    famousLayout.setBackgroundResource(R.drawable.shape_white_bg);
                    famousLayout.setBackgroundResource(0);
                }

                famousTxt.setTextColor(ContextCompat.getColor(mActivity, R.color.color_fff000));
                famousTxt.setText(proveStatusBean.getTradeField());
            } else if (proveStatusBean.getAuthStatus() == ProveStatusBean.STATUS_FAIL) {
                if (famousLayout != null  && famousLayout.getBackground() != null) {
                    famousLayout.getBackground().setAlpha(255);
                    famousLayout.setBackgroundResource(R.drawable.shape_white_bg1);
                }

                famousTxt.setText("认证失败");
            } else if (proveStatusBean.getAuthStatus() == ProveStatusBean.STATUS_CANCEL || proveStatusBean.getAuthStatus() == ProveStatusBean.STATUS_NORMAL) {
                if (famousLayout != null  && famousLayout.getBackground() != null) {
                    famousLayout.setBackgroundResource(R.drawable.shape_white_bg1);
                    famousLayout.getBackground().setAlpha(255);
                }

                famousTxt.setText("达人认证");
            }
            return;
        }
        if (flag == ProveInfoPresenter.FLAG_PROVE_STATUS_ERROR) {
            famousLayout.setVisibility(View.GONE);
        }

        if (flag == V3CirclePresenter.TAG_SIGN) {

            //签到成功
            scoreImg.setVisibility(View.GONE);
            titlebarSignTxt.setVisibility(View.GONE);

            titlebarSigned.setVisibility(View.VISIBLE);
            titlebarSigned.setTextColor(Color.argb(168, 1, 133, 255));   //文字透明度
            titlebarSigned.setText("已签到");

            //签到成功，重新拉取积分
            //获取数据统计
            ((V3CirclePresenter) presenter).getUserStat(Session.getUserId());

        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 1:
                if (isLogin()) {
                    trackUser("我的", "入口名称", "我的购买");
                    jump(MyBuyActivity.class);

                }
                break;
            case 2:
                if (isLogin()) {
                    trackUser("我的", "入口名称", "我的打赏");
                    jump(MineRewardActivity.class);
                }
                break;

            case 3:
                if (isLogin()) {
                    trackUser("我的", "入口名称", "我的收藏");
                    jump(MyCollectionActivity.class);
                }
                break;

            case 4:
                if (isLogin()) {
                    trackUser("我的", "入口名称", "我的积分");
                    ScoreDetailAty.startAccountDetail(mActivity, Type.TYPE_SCORE);
                }
                break;

            case 5:
                if (isLogin()) {
                    trackUser("我的", "入口名称", "我的等级");
                    jump(MyLevelActivity.class);
                }
                break;
            case 6:
                if (isLogin()) {
                    trackUser("我的", "入口名称", "账户");
                    jump(MyAccountAty.class);

                }
                break;

            case 7:
                if (isLogin()) {
                    trackUser("我的", "入口名称", "我的卡卷");
                    jump(MyCouponsActivity.class);

                }
                break;

            case 8:
                if (isLogin()) {
                    trackUser("我的", "入口名称", "通讯录");
                    jump(ContactsAty.class);
                }
                break;

            case 9:
                if (isLogin()) {
                    trackUser("我的", "入口名称", "我的二维码");
                    jump(PersonScanAty.class);

                }
                break;
            case 10:
                toScan();
                break;
            case 11:
                if (isLogin()) {
                    trackUser("我的", "入口名称", "一键邀请好友");
                    jump(InviteRewardActivity.class);
                }
                break;

            case 12:
                if (isLogin() && null != mCustormServiceModel) {
                    trackUser("我的", "入口名称", "联系客服");
                    starCustormService();
                } else {
                    CommonH5Activity.startCommonH5(mActivity, "", H5Address.CONECT_US);
                }
                break;
            case 13:
                trackUser("我的", "帮助", "设置");
                CommonH5Activity.startCommonH5(mActivity, "", H5Address.USER_HELP);
                break;
            case 14:
                trackUser("我的", "入口名称", "设置");
                Intent intent = new Intent(mActivity, SettingActivity.class);
                startActivityForResult(intent, IntentCode.MineFrg.MINE_REQUEST_CODE);
                break;
        }
    }

    @AfterPermissionGranted(REQUEST_CAMER_CODE)
    private void toScan() {
        trackUser("我的", "添加好友", "扫一扫");
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(mActivity, perms)) {
            Intent intent = new Intent();
            intent.setClass(mActivity, CaptureActivity.class);
            startActivityForResult(intent, 0);
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.carme_sd_permission), REQUEST_CAMER_CODE, perms);
        }
    }

    private void starCustormService() {
        int startHour = mCustormServiceModel.getStartHour();
        int startMinute = mCustormServiceModel.getStartMinute();
        int endHour = mCustormServiceModel.getEndHour();
        int endMinute = mCustormServiceModel.getEndMinute();
        boolean isWrokDate = isCurrentInTimeScope(startHour, startMinute, endHour, endMinute);
        if (mCustormServiceModel.getStatus() == 1 && isWrokDate == true) {

            CommonH5Activity.startCommonH5(mActivity, "客服", mCustormServiceModel.getCustomUrl());
        } else {

            CommonH5Activity.startCommonH5(mActivity, "客服", mCustormServiceModel.getMessageUrl());
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
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BaseEvent baseEvent) {
        if (baseEvent.type == CommonCode.EventType.PROVE_UPDATE) {
            if (baseEvent.getData() == null)
                getUserProveStatus();
            else {
                updateViewWithFlag(baseEvent.getData(), ProveInfoPresenter.FLAG_PROVE_STATUS_SUCCESS);
            }
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
        if (baseEvent.type == CommonCode.EventType.TYPE_USER_UPDATE || baseEvent.type == EventConstant.USER_AVATAR_REFUSE) {
            setData();
            return;
        }
    }

    @Override
    public void refreshPage() {

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


    private void setData() {
        mTxtPersonName.setText(Session.getUserName());
        levelTxt.setText("Lv. " + Session.getUserLevel());
        if (tv_remark != null) {
            tv_remark.setText(Session.getCustPoints());
        }

        if (Protect.checkLoadImageStatus(mActivity)) {
            Glide.with(mActivity).load(Session.getUserPicUrl()).transform(new GlideCircleImage(mActivity)).
                    placeholder(R.drawable.ic_default_head).error(R.drawable.ic_default_head).crossFade().into(mImgPersonHead);
        }

        if (TextUtils.isEmpty(Session.getUser_desc())) {
            idPersonLoginDays.setText(getString(R.string.mine_sign_default));
        } else {
            idPersonLoginDays.setText(Session.getUser_desc());
        }
        getHeadData();
    }

    private void requestUnreadMsg() {
        if (isNotity()) {
            mUnread.setVisibility(View.VISIBLE);
        } else {
            mUnread.setVisibility(View.GONE);
        }
        int unreadNum = (TextUtils.isEmpty(Session.getUserFocusNum()) ? 0 : Integer.parseInt(Session.getUserFocusNum()));
        if (mModelList == null) return;
        boolean redDot = Session.getUserIsLogin() && unreadNum != 0 && !mModelList.isEmpty() && null != adapter;
        for (MineFragItemModel item : mModelList) {
            if (TextUtils.equals(item.getName(), getString(R.string.mine_my_contacts))) {
                item.setContacts(redDot);
                item.setmFocusNum(redDot ? String.valueOf(unreadNum) : "");
                adapter.notifyDataSetChanged();
                return;
            }
        }
    }

    private boolean isNotity() {
        return Session.getNewsAnnouncementNum() != 0 ||
                Session.getNewsSystemInformationNum() != 0 ||
                Session.getNewsAccountInformationNum() != 0 ||
                Session.getNewsRecommendNum() != 0 ||
                Session.getNewsCommentNum() != 0 ||
                Session.getNewsQaNum() != 0 ||
                Session.getNewsGroupNum() != 0 ||
                Session.getNewsActivityNum() != 0;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        super.onPermissionsGranted(requestCode, perms);
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this, getString(R.string.carme_sd_permission))
                    .setPositiveButton(getString(R.string.setting))
                    .setNegativeButton(getString(R.string.cancel), null /* click listener */)
                    .setRequestCode(REQUEST_CAMER_CODE)
                    .build()
                    .show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            String scanResult = data.getExtras().getString("result");
            try {
                JSONObject object = new JSONObject(scanResult);
                Bundle bundle = new Bundle();
                if (object.has("uid")) {
                    UserInfoActivity.newFrindInfo(mActivity, object.getString("uid"));
                } else {
                    Toast.makeText(mActivity, "不支持此类链接", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                Toast.makeText(mActivity, "不支持此类链接", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }
}
