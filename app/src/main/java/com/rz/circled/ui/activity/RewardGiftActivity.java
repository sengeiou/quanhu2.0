package com.rz.circled.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rz.circled.R;
import com.rz.circled.adapter.MyPagerAdapter;
import com.rz.circled.adapter.RewardGiftAdapter;
import com.rz.circled.dialog.InsufficientBalanceDialog;
import com.rz.circled.presenter.impl.PayPresenter;
import com.rz.circled.presenter.impl.RewardGiftPresenter;
import com.rz.circled.widget.BounceBackViewPager;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.CommonCode;
import com.rz.common.constant.IntentCode;
import com.rz.common.constant.IntentKey;
import com.rz.common.event.BaseEvent;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.utils.CountDownTimer;
import com.rz.common.utils.Currency;
import com.rz.common.widget.svp.SVProgressHUD;
import com.rz.common.widget.toasty.Toasty;
import com.rz.httpapi.api.ApiPayService;
import com.rz.httpapi.api.Http;
import com.rz.httpapi.api.ResponseData.ResponseData;
import com.rz.httpapi.bean.AccountBean;
import com.rz.httpapi.bean.PayOrderInfoBean;
import com.rz.httpapi.bean.RewardBean;
import com.rz.httpapi.bean.RewardGiftModel;
import com.rz.httpapi.bean.RewardInfoBean;
import com.rz.httpapi.bean.UserInfoModel;
import com.rz.sgt.jsbridge.JsEvent;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.circlenavigator.CircleNavigator;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 打赏通用界面
 */
public class RewardGiftActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private static final int PAGE_SIZE = 8;

    @BindView(R.id.viewpager)
    BounceBackViewPager viewpager;
    @BindView(R.id.progress_gift_loading)
    ProgressBar progressGiftLoading;
    @BindView(R.id.indicator)
    MagicIndicator indicator;
    @BindView(R.id.line_dot)
    LinearLayout lineDot;
    @BindView(R.id.tv_transfer_gift_pay)
    TextView tvTransferGiftPay;
    @BindView(R.id.tv_transfer_gift_recharge)
    TextView tvTransferGiftRecharge;
    @BindView(R.id.tv_transfer_gift_balance)
    TextView tvTransferGiftBalance;
    @BindView(R.id.layout)
    LinearLayout layout;

    //记录用户的余额
    private Double mUserBalance;
    //记录点击的礼物信息
    private RewardGiftModel mGiftModel;
    //打赏相关
    private RewardGiftPresenter mRewardGiftPresenter;
    //支付相关
    private PayPresenter mPayPresenter;
    //被打赏信息
    private RewardInfoBean rewardInfoBean;
    //订单id
    private String orderId;

    private MyPagerAdapter myPagerAdapter;

    public static void startRewardGiftAty(Context context, RewardInfoBean rewardInfoBean, boolean isCircle) {
        Intent i = new Intent(context, RewardGiftActivity.class);
        i.putExtra(IntentKey.EXTRA_SERIALIZABLE, rewardInfoBean);
        i.putExtra(IntentKey.EXTRA_BOOLEAN, isCircle);
        context.startActivity(i);
    }

    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_transfer_gift, null, false);
    }

    @Override
    public void initPresenter() {
        mRewardGiftPresenter = new RewardGiftPresenter();
        mPayPresenter = new PayPresenter(false);
        mRewardGiftPresenter.attachView(this);
        mPayPresenter.attachView(this);
    }

    @Override
    public void initView() {
        Intent intent = getIntent();
        rewardInfoBean = (RewardInfoBean) intent.getSerializableExtra(IntentKey.EXTRA_SERIALIZABLE);
    }

    @Override
    public void initData() {
        mPayPresenter.getUserAccount(Session.getUserId(), "");
        //查询全部礼物
        mRewardGiftPresenter.rewardGiftList(0, -1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected boolean hasDataInPage() {
        return true;
    }

    @Override
    protected boolean needSwipeBack() {
        return false;
    }

    @Override
    protected boolean needShowTitle() {
        return false;
    }

    @Override
    protected boolean needLoadingView() {
        return true;
    }

    @Override
    public void onLoadingStatus(int loadingStatus) {
        super.onLoadingStatus(loadingStatus);
        if (loadingStatus == CommonCode.General.DATA_LOADING) {
            if (viewpager.getAdapter() == null) {
                progressGiftLoading.setVisibility(View.VISIBLE);
            }
        } else {
            progressGiftLoading.setVisibility(View.GONE);
        }
    }

    @Override
    public <T> void updateView(T t) {
        if (null != t) {
            if (t instanceof AccountBean) {
                AccountBean model = (AccountBean) t;
                if (null != model) {
                    mUserBalance = Double.parseDouble(model.getAccountSum());
                    tvTransferGiftBalance.setText(Currency.returnDollar(Currency.RMB, mUserBalance + "", 0));
                    if (mUserBalance > 0) {
                        tvTransferGiftBalance.setTextColor(ContextCompat.getColor(this, R.color.color_F5BB1E));
                    } else {
                        tvTransferGiftBalance.setTextColor(ContextCompat.getColor(this, R.color.color_666666));
                    }
                }
            } else if (t instanceof List) {
                List list = (List) t;
                if (!list.isEmpty() && list.size() > 0 && list.get(0) instanceof RewardGiftModel) {
                    List<RewardGiftModel> mGifts = (List<RewardGiftModel>) t;
                    if (!mGifts.isEmpty()) {
                        initGridView(mGifts);
                    }
                }
            } else if (t instanceof RewardBean) {
                RewardBean data = (RewardBean) t;
                orderId = data.getOrderId();
                //去检查
                mPayPresenter.pay(orderId, Double.parseDouble(mGiftModel.getPrice()), mUserBalance, 0);
            } else if (t instanceof Integer) {
                if (((Integer) t).intValue() == 1000) {
                    InsufficientBalanceDialog.newInstance().show(getSupportFragmentManager(), "");
                } else {
                    mPayPresenter.payRewardDetails(orderId);
                }
            } else if (t instanceof PayOrderInfoBean) {
                PayOrderInfoBean data = (PayOrderInfoBean) t;
                JsEvent.callJsEvent(data.getNotifyStatus(), true);
                finish();
            }
        }
    }

    /**
     * 初始化gridview
     *
     * @param mGifts
     */
    private void initGridView(List<RewardGiftModel> mGifts) {
        int size = mGifts.size();
        List<View> views = new ArrayList<>();
        LayoutInflater inflater = LayoutInflater.from(this);
        int total = (size % PAGE_SIZE == 0 ? size / PAGE_SIZE : size / PAGE_SIZE + 1);
        for (int i = 0; i < total; i++) {
            //存储子列表数据
            List<RewardGiftModel> mChildList = new ArrayList<>();
            //取出子列表
            mChildList.addAll(mGifts.subList(i * PAGE_SIZE, i == total - 1 ? size : (i + 1) * PAGE_SIZE));

            View view = inflater.inflate(R.layout.layout_reward_gift_grid, null);
            GridView mGridView = (GridView) view.findViewById(R.id.gv);
            RewardGiftAdapter mGiftAdp = new RewardGiftAdapter(mContext, mChildList, R.layout.item_transfer_gift);
            mGridView.setAdapter(mGiftAdp);
            mGridView.setOnItemClickListener(this);

            views.add(view);
        }
        // 设置填充ViewPager页面的适配器
        viewpager.setAdapter(myPagerAdapter = new MyPagerAdapter(views));

        initIndicator(views);

        if (total != 1) {
            lineDot.setVisibility(View.VISIBLE);
        } else {
            lineDot.setVisibility(View.GONE);
        }
    }

    private void initIndicator(List list) {
        CircleNavigator circleNavigator = new CircleNavigator(mContext);
        circleNavigator.setCircleCount(list.size());
        circleNavigator.setCircleColor(getResources().getColor(R.color.color_b3b3b3));
        circleNavigator.setStrokeWidth(0);
        circleNavigator.setCircleSpacing(getResources().getDimensionPixelOffset(R.dimen.px20));
        circleNavigator.setCircleClickListener(new CircleNavigator.OnCircleClickListener() {
            @Override
            public void onClick(int index) {
                viewpager.setCurrentItem(index);
            }
        });
        indicator.setNavigator(circleNavigator);
        ViewPagerHelper.bind(indicator, viewpager);
    }

    @Subscribe
    public void eventBus(BaseEvent event) {
    }

    @OnClick({R.id.tv_transfer_gift_pay, R.id.tv_transfer_gift_recharge, R.id.layout})
    public void onClick(View view) {
        switch (view.getId()) {
            //点击屏幕消失
            case R.id.layout:
                finish();
                break;
            //充值
            case R.id.tv_transfer_gift_recharge:
                Intent intent = new Intent(aty, RechargeMoneyAty.class);
                startActivityForResult(intent, IntentCode.RechargeMoney.RECHARGE_REQUEST_CODE);
                break;
            //支付
            case R.id.tv_transfer_gift_pay:
                if (null == mGiftModel) {
                    SVProgressHUD.showInfoWithStatus(aty, getString(R.string.choose_gift));
                } else {
                    if (null != mUserBalance) {
                        if (Double.parseDouble(mGiftModel.getPrice()) / 100 > mUserBalance) {
                            SVProgressHUD.showInfoWithStatus(aty, getString(R.string.no_money));
                        } else {
                            if (CountDownTimer.isFastClick()) {
                                return;
                            }
                            //创建订单
                            mRewardGiftPresenter.rewardDo(Session.getUserId(), mGiftModel.getGiftid(), 1, rewardInfoBean.getResourceId(), rewardInfoBean.getAuthorId(), "0");
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
        for (int i = 0; i < viewpager.getAdapter().getCount(); i++) {
            if (i == viewpager.getCurrentItem()) {
                RewardGiftAdapter adapter = (RewardGiftAdapter) adapterView.getAdapter();
                adapter.setCheckAtPosFalse(pos, true);
                mGiftModel = adapter.getItem(pos);
                if (null != mGiftModel && null != mUserBalance) {
                    if (Double.parseDouble(mGiftModel.getPrice()) / 100 > mUserBalance) {
                        tvTransferGiftBalance.setTextColor(ContextCompat.getColor(this, R.color.color_666666));
                    } else {
                        tvTransferGiftBalance.setTextColor(ContextCompat.getColor(this, R.color.color_F5BB1E));
                    }
                }
            } else {
                ((RewardGiftAdapter) ((GridView) (((View) myPagerAdapter.getItem(i)).findViewById(R.id.gv))).getAdapter()).setCheckAtPosFalse(pos, false);
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

    @Override
    public void refreshPage() {

    }
}
