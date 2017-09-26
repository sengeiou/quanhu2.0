package com.rz.common.ui.view;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.AbstractDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.rz.common.R;
import com.rz.common.constant.CommonCode;
import com.rz.common.widget.svp.SVProgressHUD;
import com.rz.common.widget.toasty.Toasty;

/**
 * Created by Gsm on 2017/8/4.
 */
public class BaseLoadView implements View.OnClickListener {
    private Context mContext;

    private View contentView;
    private View statusView;
    private TextView tvStatus;
    private ImageView ivStatus;
    private TextView tvFunction;
    private TextView tvAction;
    private SimpleDraweeView ivLoading;
    private RefreshListener refreshListener;
    private FrameLayout llError;

    private boolean needRefresh;

    public BaseLoadView(Context context, View statusView, View contentView) {
        this.mContext = context;
        this.statusView = statusView;
        this.contentView = contentView;
        initView();
    }

    private void initView() {
        ivLoading = (SimpleDraweeView) statusView.findViewById(R.id.iv_base_load_loading);
        llError = (FrameLayout) statusView.findViewById(R.id.ll_base_load_status);
        tvStatus = (TextView) statusView.findViewById(R.id.tv_base_load_status);
        ivStatus = (ImageView) statusView.findViewById(R.id.iv_base_load_status);
        tvFunction = (TextView) statusView.findViewById(R.id.tv_base_load_function);
//        tvAction = (TextView) statusView.findViewById(R.id.tv_base_load_action);
        tvFunction.setOnClickListener(this);
        tvAction.setOnClickListener(this);
        statusView.setOnClickListener(this);
        statusView.setVisibility(View.GONE);
    }

    public void setRefreshListener(RefreshListener refreshListener) {
        this.refreshListener = refreshListener;
    }

    /**
     * @param flag
     * @param haveData 當前頁面是否已經有數據顯示
     */
    public void onLoading(int flag, boolean haveData, boolean needRefresh) {
        onLoading(flag, null, haveData, needRefresh);
    }

    /**
     * @param flag
     * @param msg
     * @param haveData 當前頁面是否已經有數據顯示
     */
    public void onLoading(int flag, String msg, boolean haveData, boolean needRefresh) {
        this.needRefresh = needRefresh;
        ivLoading.setVisibility(View.GONE);
        SVProgressHUDDismiss();
        switch (flag) {
            case CommonCode.General.UN_NETWORK://沒有網絡或者網絡錯誤
                processUnNetWork(msg, haveData);
                break;
            case CommonCode.General.DATA_LOADING://加载中
                processLoading(msg, haveData);
                break;
            case CommonCode.General.LOAD_ERROR://加载失败
                processLoadError(msg, haveData);
                break;
            case CommonCode.General.DATA_EMPTY://空数据
                processDataEmpty(msg, haveData);
                break;
            case CommonCode.General.ERROR_DATA://错误数据
                processLoadError(msg, haveData);
                break;
            case CommonCode.General.DATA_LACK://数据不足够
                processDataLack(msg, haveData);
                break;
            case CommonCode.General.DATA_SUCCESS://加载成功且数据满足
                processDataSuccess(msg, haveData);
                break;
            case CommonCode.General.WEB_ERROR://web页面不存在
                processWebError(msg, haveData);
                break;
            case CommonCode.General.NEWS_EMPTY://没有任何消息页面
                processNewsEmpty(msg, haveData);
                break;
        }
    }

    /**
     * 没有消息页面
     */
    private void processNewsEmpty(String msg, boolean haveData) {
        if (haveData) {
            statusView.setVisibility(View.GONE);
            Toasty.info(mContext, TextUtils.isEmpty(msg) ? mContext.getString(R.string.status_data_luck) : msg).show();
        } else {
            statusView.setVisibility(View.VISIBLE);
            ivStatus.setImageResource(R.mipmap.icon_news_empty);
            tvStatus.setText(TextUtils.isEmpty(msg) ? mContext.getString(R.string.status_news_empty) : msg);
            tvFunction.setVisibility(View.GONE);
            ivLoading.setVisibility(View.GONE);
            llError.setVisibility(View.VISIBLE);
        }
    }

    /**
     * web页加载失败
     */
    private void processWebError(String msg, boolean haveData) {
        statusView.setVisibility(View.VISIBLE);
        ivStatus.setImageResource(R.mipmap.icon_web_error);
        tvStatus.setText(TextUtils.isEmpty(msg) ? mContext.getString(R.string.status_web_error) : msg);
        tvFunction.setVisibility(View.GONE);
        ivLoading.setVisibility(View.GONE);
        llError.setVisibility(View.VISIBLE);
    }

    /**
     * 加载成功
     */
    private void processDataSuccess(String msg, boolean haveData) {
        ivLoading.setVisibility(View.GONE);
        statusView.setVisibility(View.GONE);
    }

    /**
     * 加载数据不足
     */
    private void processDataLack(String msg, boolean haveData) {
        statusView.setVisibility(View.GONE);
        Toasty.info(mContext, TextUtils.isEmpty(msg) ? mContext.getString(R.string.status_data_luck) : msg).show();
    }

    /**
     * 数据为空
     */
    private void processDataEmpty(String msg, boolean haveData) {
        if (haveData) {
            statusView.setVisibility(View.GONE);
            Toasty.info(mContext, TextUtils.isEmpty(msg) ? mContext.getString(R.string.status_empty_load) : msg).show();
        } else {
            ivLoading.setVisibility(View.GONE);
            statusView.setVisibility(View.VISIBLE);
            ivStatus.setImageResource(R.mipmap.icon_data_empty);
            tvStatus.setText(TextUtils.isEmpty(msg) ? mContext.getString(R.string.status_empty_load) : msg);
            if (tvFunction.getVisibility() != View.VISIBLE) {
                tvAction.setText(R.string.status_network_retry);
                tvAction.setVisibility(View.VISIBLE);
            }
            llError.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 加载出错
     */
    private void processLoadError(String msg, boolean haveData) {
        if (haveData) {
            statusView.setVisibility(View.GONE);
            Toasty.info(mContext, TextUtils.isEmpty(msg) ? mContext.getString(R.string.status_load_error) : msg).show();
        } else {
            ivLoading.setVisibility(View.GONE);
            statusView.setVisibility(View.VISIBLE);
            ivStatus.setImageResource(R.mipmap.icon_load_error);
            tvStatus.setText(TextUtils.isEmpty(msg) ? mContext.getString(R.string.status_load_error) : msg);
            tvFunction.setVisibility(View.GONE);
            tvAction.setText(R.string.status_network_retry);
            tvAction.setVisibility(View.VISIBLE);
            llError.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 正在加载中
     */
    private void processLoading(String msg, boolean haveData) {
        if (haveData) {
            statusView.setVisibility(View.GONE);
            SVProgressHUD.showWithStatus(mContext, TextUtils.isEmpty(msg) ? mContext.getString(R.string.status_is_loading) : msg);
        } else {
            statusView.setVisibility(View.VISIBLE);
            ivLoading.setVisibility(View.VISIBLE);
            llError.setVisibility(View.GONE);
            AbstractDraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setAutoPlayAnimations(true)
                    .setUri(Uri.parse("res://" + mContext.getPackageName() + "/" + R.drawable.icon_login))//设置uri
                    .build();
            ivLoading.setController(controller);

        }
    }

    /**
     * 没有网络
     */
    private void processUnNetWork(String msg, boolean haveData) {
        if (haveData) {
            statusView.setVisibility(View.GONE);
            Toasty.info(mContext, TextUtils.isEmpty(msg) ? mContext.getString(R.string.status_un_network) : msg).show();
        } else {
            ivLoading.setVisibility(View.GONE);
            statusView.setVisibility(View.VISIBLE);
            ivStatus.setImageResource(R.mipmap.icon_load_error);
            tvStatus.setText(TextUtils.isEmpty(msg) ? mContext.getString(R.string.status_un_network) : msg);
            tvFunction.setVisibility(View.GONE);
            tvAction.setText(R.string.status_network_retry);
            tvAction.setVisibility(View.VISIBLE);
            llError.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置功能按钮显示文字
     *
     * @param stringId
     */
    public void setFunctionText(int stringId) {
        setFunctionText(mContext.getString(stringId));
    }

    /**
     * 设置功能按钮显示文字
     *
     * @param string
     */
    public void setFunctionText(String string) {
        if (tvFunction.getVisibility() != View.VISIBLE)
            tvFunction.setVisibility(View.VISIBLE);
        tvFunction.setText(string);
    }

    /**
     * 设置功能按钮点击事件
     *
     * @param clickListener
     */
    public void setFunctionClickListener(View.OnClickListener clickListener) {
        tvFunction.setOnClickListener(clickListener);
    }

    public void SVProgressHUDDismiss() {
        if (SVProgressHUD.isShowing(mContext)) {
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    SVProgressHUD.dismiss(mContext);
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        if (ivLoading.getVisibility() == View.VISIBLE) return;
        if (!needRefresh) return;
        //刷新
        statusView.setVisibility(View.GONE);
        if (refreshListener != null)
            refreshListener.refreshPage();
    }

    public interface RefreshListener {
        void refreshPage();
    }
}
