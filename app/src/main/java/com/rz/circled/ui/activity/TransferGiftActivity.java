package com.rz.circled.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rz.circled.R;
import com.rz.circled.adapter.MyPagerAdapter;
import com.rz.circled.modle.AccountModel;
import com.rz.circled.modle.TransferModule;
import com.rz.circled.presenter.impl.CirclePresenter;
import com.rz.circled.presenter.impl.PayPresenter;
import com.rz.circled.widget.BounceBackViewPager;
import com.rz.circled.widget.MyGridView;
import com.rz.common.adapter.CommonAdapter;
import com.rz.common.adapter.ViewHolder;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.CommonCode;
import com.rz.common.constant.IntentCode;
import com.rz.common.constant.IntentKey;
import com.rz.common.constant.Type;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.utils.CountDownTimer;
import com.rz.common.utils.Currency;
import com.rz.common.utils.DensityUtils;
import com.rz.common.widget.svp.SVProgressHUD;
import com.rz.httpapi.bean.RewardGiftModel;
import com.rz.httpapi.bean.UserInfoModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 打赏通用界面
 */
public class TransferGiftActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    //加载进度条
    @BindView(R.id.id_gift_loading_pb)
    ProgressBar mPBar;
    //展示礼物布局
    @BindView(R.id.id_red_viewpager)
    BounceBackViewPager mViewPager;
    //礼物的列数-横线
    @BindView(R.id.ll_transfer_gift_line)
    LinearLayout llLine;
    //    //余额不足按钮
//    @BindView(R.id.id_balance_txt)
//    TextView mTxtNoMoney;
    //用户的钱
    @BindView(R.id.tv_transfer_gift_balance)
    TextView mTvUserMoney;
    //记录用户的余额
    private Double mUserMoney;

    //记录屏幕的宽度
    private int mScreenWidth;

    //记录当前viewpager第几页
    private int currentItem = 0;

    //每页显示的条数
    private int page_size = 8;

    //存储礼物adp
    List<CommonAdapter<RewardGiftModel>> mGiftAdps = new ArrayList<>();
    //存储viewpager的子布局
    List<View> mItemsView = new ArrayList<View>();

    //记录点击的礼物信息
    private RewardGiftModel mGifrModel;
    //打赏相关
    private CirclePresenter v3CirclePresenter;
    //    //支付相关
    private PayPresenter mPayPresenter;
    //
    private TransferModule transferBean;

    public static final int REQUEST_REWARD = 2001;

    public static final String EXTRA_IS_CIRCLE = "isCircle";//圈子内的打赏
    private boolean isCircle = false;

    @Override
    public boolean hasDataInPage() {
        return true;
    }

    public static void startTransferGiftAty(Context context, TransferModule transferModule, boolean isCircle) {
        Intent i = new Intent(context, TransferGiftActivity.class);
        i.putExtra(IntentKey.EXTRA_SERIALIZABLE, transferModule);
        i.putExtra(IntentKey.EXTRA_BOOLEAN, isCircle);
        context.startActivity(i);
    }

    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_transfer_gift, null, false);
    }


    @Override
    public void initPresenter() {
        v3CirclePresenter = new CirclePresenter();
        mPayPresenter = new PayPresenter(false);
        v3CirclePresenter.attachView(this);
        mPayPresenter.attachView(this);
    }

    @Override
    public void initView() {
        Intent intent = getIntent();
        transferBean = (TransferModule) intent.getSerializableExtra(IntentKey.EXTRA_SERIALIZABLE);
        isCircle = intent.getBooleanExtra(IntentKey.EXTRA_BOOLEAN, false);

        mScreenWidth = DensityUtils.getScreenW(mContext) / 4;

        FrameLayout.LayoutParams frameParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, (int) (mScreenWidth * 2.04));
        mViewPager.setLayoutParams(frameParams);
    }

    @Override
    public void initData() {
        mPayPresenter.getUserAccount(Session.getUserId(), "");
        v3CirclePresenter.loadRewardGiftList();
    }

    @Override
    public void onLoadingStatus(int loadingStatus, String string) {
        super.onLoadingStatus(loadingStatus, string);
        if (loadingStatus == CommonCode.General.DATA_LOADING) {
            if (mItemsView.isEmpty()) {
                mPBar.setVisibility(View.VISIBLE);
            }
        } else {
            mPBar.setVisibility(View.GONE);
            if (loadingStatus == CommonCode.General.DATA_SUCCESS) {
                if (!TextUtils.isEmpty(string)) {
                    //打赏礼物成功
                    SVProgressHUD.showSuccessWithStatus(mContext, getString(R.string.send_gift_success));
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
//                            BaseEvent event = new BaseEvent();
//                            event.key = "rewardGift";
//                            event.event = mGifrModel;
//                            EventBus.getDefault().post(event);

                            Intent intent = new Intent();
                            intent.putExtra(IntentKey.EXTRA_SERIALIZABLE, mGifrModel);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }, 2000);
                }
            }
        }
    }

    @Override
    public <T> void updateView(T t) {
        if (null != t) {
            if (t instanceof AccountModel) {
                AccountModel model = (AccountModel) t;
                if (null != model) {
                    if (Type.USER_MONEY_NORMAL == model.getAccountState()) {
                        Session.setUserMoneyState(true);
                    } else {
                        Session.setUserMoneyState(false);
                    }
                    mUserMoney = Double.parseDouble(model.getAccountSum());
                    mTvUserMoney.setText(Currency.returnDollar(Currency.RMB, mUserMoney + "", 0));
                    if (mUserMoney > 0) {
//                        mTxtNoMoney.setText("余额");
                        mTvUserMoney.setTextColor(ContextCompat.getColor(this, R.color.color_main));
                    } else {
//                        mTxtNoMoney.setText("余额不足");
                        mTvUserMoney.setTextColor(ContextCompat.getColor(this, R.color.color_666666));
                    }
                }
            } else if (t instanceof UserInfoModel) {
                UserInfoModel user = (UserInfoModel) t;
                if (null != user) {
                    if (Type.HAD_SET_PW == user.getIsPayPassword()) {
                        Session.setUserSetpaypw(true);
                    } else {
                        Session.setUserSetpaypw(false);
                    }
                    if (Type.OPEN_EASY_PAY == user.getSmallNopass()) {
                        Session.setIsOpenGesture(true);
                    } else {
                        Session.setIsOpenGesture(false);
                    }
                    mPayPresenter.checkIsOpenEasyPay(Double.parseDouble(mGifrModel.getPrice()), mUserMoney, getString(R.string.pay_amount), 0);
                }
            } else if (t instanceof String) {
                //去支付
            } else {
                List<RewardGiftModel> mGifts = (List<RewardGiftModel>) t;
                if (null != mGifts && !mGifts.isEmpty()) {
                    initGridView(mGifts);
                }
            }
        }
    }

    /**
     * 初始化gridview
     *
     * @param mGifts
     */
    private void initGridView(List<RewardGiftModel> mGifts) {
        llLine.removeAllViews();
        mItemsView.clear();
        mGiftAdps.clear();
        int size = mGifts.size();
        final List<View> mDotsView = new ArrayList<View>();
        LayoutInflater inflater = LayoutInflater.from(this);
        int total = (size % page_size == 0 ? size / page_size : size / page_size + 1);
        for (int i = 0; i < total; i++) {
            //存储子列表数据
            List<RewardGiftModel> mChildList = new ArrayList<>();
            View view = inflater.inflate(R.layout.comm_gift_gridview, null);
            MyGridView mGridView = (MyGridView) view.findViewById(R.id.id_gift_gird);

            LinearLayout.LayoutParams gridParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (mScreenWidth * 2.04));
            view.setLayoutParams(gridParams);
            mGridView.setColumnWidth(mScreenWidth);

            mGridView.setBackgroundColor(ContextCompat.getColor(this, R.color.color_F4F4F4));

            //取出子列表
            mChildList.addAll(mGifts.subList(i * page_size, i == total - 1 ? size : (i + 1) * page_size));

            CommonAdapter<RewardGiftModel> mGiftAdp = new CommonAdapter<RewardGiftModel>(mContext, R.layout.item_transfer_gift) {

                @Override
                public void convert(ViewHolder helper, RewardGiftModel item, int position) {
                    RelativeLayout rlRoot = (RelativeLayout) helper.getViewById(R.id.rl_item_transfer_gift_root);

                    ImageView mIvIcon = (ImageView) helper.getViewById(R.id.iv_item_transfer_gift_icon);
                    helper.setImageByUrl(mIvIcon, item.getIcon(), R.color.white);
                    TextView mTvName = (TextView) helper.getViewById(R.id.tv_item_transfer_gift_name);
                    TextView mTvPrice = (TextView) helper.getViewById(R.id.tv_item_transfer_gift_price);
                    ImageView mIvCheck = (ImageView) helper.getViewById(R.id.iv_item_transfer_gift_check);
                    mTvName.setText(item.getName());
                    mTvPrice.setText(Integer.parseInt(item.getPrice()) / 100 + getString(R.string.youranbi));
                    if (checkMap.get(helper.getPosition(), false)) {
                        mIvCheck.setVisibility(View.VISIBLE);
                        rlRoot.setBackgroundResource(R.drawable.shape_ring_blue);
                    } else {
                        mIvCheck.setVisibility(View.GONE);
                        rlRoot.setBackgroundColor(Color.WHITE);

                    }
                }
            };
            mGiftAdps.add(mGiftAdp);
            mGiftAdp.setData(mChildList);
            mGridView.setAdapter(mGiftAdp);
            mItemsView.add(view);

            if (total != 1) {
                View mDots = new View(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        15, 2);
                params.setMargins(5, 0, 5, 0);
                mDots.setLayoutParams(params);
                mDots.setBackgroundResource(R.drawable.selector_reward_vpager_index);
                mDotsView.add(mDots);
                llLine.addView(mDots);
                llLine.setVisibility(View.VISIBLE);
            } else {
                llLine.setVisibility(View.GONE);
            }
        }
        if (mItemsView.isEmpty()) {
            return;
        }
        // 设置填充ViewPager页面的适配器
        mViewPager.setAdapter(new MyPagerAdapter(mItemsView));
        mViewPager.setCurrentItem(currentItem);
//        mDotsView.get(currentItem).setSelected(true);
        ((MyGridView) mItemsView.get(currentItem).findViewById(R.id.id_gift_gird)).setOnItemClickListener(this);
        // 设置一个监听器，当ViewPager中的页面改变时调用
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            //记录上一次的位置
            int oldPosition = 0;

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentItem = position;
                mDotsView.get(oldPosition).setSelected(false);
                mDotsView.get(position).setSelected(true);
                oldPosition = position;
                showItem(position);
            }
        });
    }

    public void showItem(int pos) {
        for (int i = 0; i < mItemsView.size(); i++) {
            if (pos == i) {
                ((MyGridView) mItemsView.get(i).findViewById(R.id.id_gift_gird)).setOnItemClickListener(this);
            } else {
                ((MyGridView) mItemsView.get(i).findViewById(R.id.id_gift_gird)).setOnItemClickListener(null);
            }
        }
    }

    @OnClick({R.id.tv_transfer_gift_recharge, R.id.tv_transfer_gift_pay, R.id.id_root_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            //点击屏幕消失
            case R.id.id_root_layout:
                finish();
                break;
            //充值
            case R.id.tv_transfer_gift_recharge:
//                if (isLogin()) {
//                    Intent intent = new Intent(aty, RechargeMoneyAty.class);
//                    startActivityForResult(intent, IntentCode.RechargeMoney.RECHARGE_REQUEST_CODE);
//                }
                break;
            //支付
            case R.id.tv_transfer_gift_pay:
                if (null == mGifrModel) {
                    SVProgressHUD.showInfoWithStatus(aty, getString(R.string.choose_gift));
                } else {
                    if (null != mUserMoney) {
                        if (Double.parseDouble(mGifrModel.getPrice()) / 100 > mUserMoney) {
                            SVProgressHUD.showInfoWithStatus(aty, getString(R.string.no_money));
                        } else {
                            if (CountDownTimer.isFastClick()) {
                                return;
                            }
                            if (transferBean != null) {
                                transferBean.price = Long.valueOf(mGifrModel.getPrice());
                                transferBean.id = 0;
                                Log.d("yeying", "giftActivity " + transferBean.toString());
                                TransferMoneyActivity.startV3TransferMoneyAty(TransferGiftActivity.this, transferBean, isCircle);
                                finish();
                            } else {
//                                transferModule.price = Long.valueOf(mGifrModel.getPrice());
                            }
                            //去支付
//                            mPayPresenter.isSettingPw(true);
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
        for (int i = 0; i < mItemsView.size(); i++) {
            CommonAdapter<RewardGiftModel> mAdp = mGiftAdps.get(i);
            if (currentItem == i) {
                mAdp.setCheckAtPosFalse(pos, true);
                mGifrModel = (RewardGiftModel) mAdp.getItem(pos);
                if (null != mGifrModel && null != mUserMoney) {
                    if (Double.parseDouble(mGifrModel.getPrice()) / 100 > mUserMoney) {
//                        mTxtNoMoney.setText("余额不足");
                        mTvUserMoney.setTextColor(ContextCompat.getColor(this, R.color.color_666666));
                    } else {
//                        mTxtNoMoney.setText("余额");
                        mTvUserMoney.setTextColor(ContextCompat.getColor(this, R.color.color_main));
                    }
                }
            } else {
                mAdp.setCheckAtPosFalse(pos, false);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentCode.RechargeMoney.RECHARGE_REQUEST_CODE) {
            if (resultCode == IntentCode.RechargeMoney.RECHARGE_RESULT_CODE) {
                //重新获取余额
                mPayPresenter.getUserAccount(Session.getUserId(), "");
            }
        }
    }
}
