package com.rz.common.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.rz.common.R;
import com.rz.common.permission.EasyPermissions;
import com.rz.common.ui.inter.IViewController;
import com.rz.common.ui.view.BaseLoadView;
import com.rz.common.widget.SwipeBackLayout;

import java.util.List;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity implements IViewController, EasyPermissions.PermissionCallbacks {
    protected static final int RC_CAMERA_PERM = 123;
    protected static final int RC_LOCATION_CONTACTS_PERM = 124;
    protected static final int RC_SETTINGS_SCREEN = 125;
    protected static final int RC_AUDIO_AND_EXTENER = 126;
    protected static final int RC_VIDEO_AND_EXTENER = 127;
    protected static final int RC_EXTENER = 130;

    public String TAG;

    protected Context mContext;

    private View commonTitle;
    private TextView tvCommonTitle;
    private TextView tvCommonTitleRight;
    private ImageView ivCommonLeft;
    private ImageView ivCommonRight;
    private BaseLoadView mLoadView;
    public Activity aty;

    private InputMethodManager mImm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        this.aty=this;
        TAG = getClass().getSimpleName();
        setContentView(R.layout.activity_base);

        LinearLayout llTitle = (LinearLayout) findViewById(R.id.ll_base_title);
        LinearLayout llContent = (LinearLayout) findViewById(R.id.ll_base_content);
        FrameLayout flTransTitle = (FrameLayout) findViewById(R.id.fl_base_title_transparent);

        initSupportSwipeBack();
        initTint();


        mImm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        initTitleView(llTitle, flTransTitle);
        initContentView(llContent);
        ButterKnife.bind(this);
        initPresenter();
        initView();
        initData();
    }

    public void initPresenter() {

    }

    public abstract void initView();

    public abstract void initData();


    private void initTitleView(LinearLayout llTitle, FrameLayout flTransTitle) {
        if (!needShowTitle()) return;
        if (getTransTitleView() == null) {//加载非透明title
            llTitle.setVisibility(View.VISIBLE);
            llTitle.removeAllViews();
            if (getTitleView() == null) {
                commonTitle = getLayoutInflater().inflate(R.layout.layout_base_title, null);
                initCommonTitle();
                llTitle.addView(commonTitle);
            } else {
                llTitle.addView(getTitleView());
            }
        } else {//加载透明title
            llTitle.setVisibility(View.GONE);
            flTransTitle.removeAllViews();
            flTransTitle.addView(getTransTitleView());
        }
    }

    private void initCommonTitle() {
        tvCommonTitle = (TextView) commonTitle.findViewById(R.id.tv_base_title);
        tvCommonTitleRight = (TextView) commonTitle.findViewById(R.id.tv_base_title_right);
        ivCommonLeft = (ImageView) commonTitle.findViewById(R.id.iv_base_title_left);
        ivCommonRight = (ImageView) commonTitle.findViewById(R.id.iv_base_title_right);
        setTitleLeftListener(null);
    }

    private void initContentView(LinearLayout llContent) {
        if (needLoadingView()) {
            View loadView = getLayoutInflater().inflate(R.layout.layout_base_load, null);
            mLoadView = new BaseLoadView(mContext, loadView, llContent);
            llContent.addView(loadView);
        }
        if (loadView(getLayoutInflater()) != null)
            llContent.addView(loadView(getLayoutInflater()));
    }
    /**
     * show to @param(cls)，but can't finish activity
     */

    public void showActivity(Activity aty, Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(aty, cls);
        aty.startActivity(intent);
    }

    /**
     * show to @param(cls)，but can't finish activity
     */
    public void showActivity(Activity aty, Intent it) {
        aty.startActivity(it);
    }
    public void showActivity(Activity aty, Class<?> cls, Bundle extras) {
        Intent intent = new Intent();
        intent.putExtras(extras);
        intent.setClass(aty, cls);
        aty.startActivity(intent);
    }
    /**
     * skip to @param(cls)，and call @param(aty's) finish() method
     */
    public void skipActivity(Activity aty, Class<?> cls) {
        showActivity(aty, cls);
        aty.finish();
    }

    /**
     * skip to @param(cls)，and call @param(aty's) finish() method
     */

    public void skipActivity(Activity aty, Intent it) {
        showActivity(aty, it);
        aty.finish();
    }

    /**
     * skip to @param(cls)，and call @param(aty's) finish() method
     */
    public void skipActivity(Activity aty, Class<?> cls, Bundle extras) {
        showActivity(aty, cls, extras);
        aty.finish();
    }
    /**
     * 初始化沉浸式
     */
    private void initTint() {
        if (needStatusBarTint()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                SystemBarTintManager tintManager = new SystemBarTintManager(this);
                tintManager.setStatusBarTintEnabled(true);
                tintManager.setStatusBarTintResource(getStatusTintColor());
            } else {
                SystemBarTintManager tintManager = new SystemBarTintManager(this);
                tintManager.setStatusBarTintEnabled(false);
            }
        }
    }

    /**
     * 初始化滑动返回
     */
    private void initSupportSwipeBack() {
        if (needSwipeBack()) {
            SwipeBackLayout swipeLayout = (SwipeBackLayout) LayoutInflater.from(this).inflate(R.layout.swipe_back_base, null);
            swipeLayout.attachToActivity(this);
        }
    }

    /**
     * 设置错误、空页面刷新监听
     *
     * @param refreshListener
     */
    public void setRefreshListener(BaseLoadView.RefreshListener refreshListener) {
        if (mLoadView != null || isFinishing() || mLoadView == null)
            mLoadView.setRefreshListener(refreshListener);
    }

    /**
     * @return 状态栏颜色
     */
    protected int getStatusTintColor() {
        return android.R.color.transparent;
    }
//-------------------------设置布局相关start--------------------//

    /**
     * 加载布局
     *
     * @param inflater
     * @return
     */
    protected View loadView(LayoutInflater inflater) {
        return null;
    }

    /**
     * 返回标题栏(非透明背景)
     *
     * @return view
     */
    protected View getTitleView() {
        return commonTitle;
    }

    /**
     * 返回标题栏(透明背景)
     *
     * @return view
     */
    protected View getTransTitleView() {
        return null;
    }

//-------------------------设置布局相关end--------------------//

//-------------------------判断条件相关start--------------------//

    /**
     * 当前也是都否已经有数据展示
     *
     * @return true 有 , false 没有
     */
    protected boolean hasDataInPage() {
        return false;
    }

    /**
     * 是否需要loadingView、空页面显示
     *
     * @return true 需要 , false 不需要
     */
    protected boolean needLoadingView() {
        return false;
    }

    /**
     * 是否需要显示标题(包括透明背景与非透明背景)
     *
     * @return true 需要 , false 不需要
     */
    protected boolean needShowTitle() {
        return true;
    }

    /**
     * 是否支持滑动返回
     *
     * @return true 允许滑动返回，false 不允许滑动返回
     */
    protected boolean needSwipeBack() {
        return true;
    }

    /**
     * 是否需要使用沉浸式状态栏
     *
     * @return true 使用,false 不使用
     */
    protected boolean needStatusBarTint() {
        return true;
    }

    /**
     * 是否支持空页面、错误页面点击刷新
     *
     * @return
     */
    protected boolean needSupportRefresh() {
        return true;
    }

//-------------------------判断条件相关end--------------------//

//-------------------------标题栏相关start--------------------//

    /**
     * 设置title背景色
     */
    public void setTitleBackground(int colorId) {
        if (commonTitle != null)
            commonTitle.setBackgroundColor(colorId);
    }

    /**
     * 設置公用title的标题
     *
     * @param StringId
     */
    public void setTitleText(int StringId) {
        setTitleText(getString(StringId));
    }

    /**
     * 設置公用title的标题
     *
     * @param text
     */
    public void setTitleText(String text) {
        if (tvCommonTitle != null && text != null)
            tvCommonTitle.setText(text);
    }

    /**
     * 设置公用title的文字颜色
     *
     * @param colorId
     */
    public void setTitleTextColor(int colorId) {
        if (tvCommonTitle != null)
            tvCommonTitle.setTextColor(ContextCompat.getColor(mContext, colorId));
    }

    /**
     * 设置公用title左边按钮图片
     *
     * @param drawableId
     */
    public void setTitleLeftIcon(int drawableId) {
        if (ivCommonLeft != null)
            ivCommonLeft.setImageResource(drawableId);
    }

    /**
     * 设置公用title左边按钮点击事件
     *
     * @param leftListener
     */
    public void setTitleLeftListener(View.OnClickListener leftListener) {
        if (leftListener != null) {
            ivCommonLeft.setOnClickListener(leftListener);
        } else {
            ivCommonLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
    }

    /**
     * 设置公用title右边文字信息
     *
     * @param StringId
     */
    public void setTitleRightText(int StringId) {
        setTitleRightText(getString(StringId));
    }

    /**
     * 设置公用title右边文字信息
     *
     * @param text
     */
    public void setTitleRightText(String text) {
        if (tvCommonTitleRight != null && text != null) {
            tvCommonTitleRight.setText(text);
            tvCommonTitleRight.setVisibility(View.VISIBLE);
            ivCommonRight.setVisibility(View.GONE);
        }
    }

    /**
     * 设置右侧文字颜色
     *
     * @param colorId
     */
    public void setTitleRightTextColor(int colorId) {
        if (tvCommonTitleRight != null)
            tvCommonTitleRight.setTextColor(ContextCompat.getColor(mContext, colorId));
    }

    /**
     * 设置右侧图标
     *
     * @param drawableId    图标地址
     * @param rightListener 响应事件
     */
    public void setTitleRightImageView(int drawableId, View.OnClickListener rightListener) {
        if (ivCommonRight != null) {
            ivCommonRight.setImageResource(drawableId);
            if (rightListener != null) ivCommonRight.setOnClickListener(rightListener);
            ivCommonRight.setVisibility(View.VISIBLE);
            tvCommonTitleRight.setVisibility(View.GONE);
        }
    }

    /**
     * 设置公用title右边按钮点击事件
     *
     * @param rightListener
     */
    public void setTitleRightListener(View.OnClickListener rightListener) {
        if (rightListener != null) {
            if (tvCommonTitleRight.getVisibility() == View.VISIBLE)
                tvCommonTitleRight.setOnClickListener(rightListener);
            if (ivCommonRight.getVisibility() == View.VISIBLE)
                ivCommonRight.setOnClickListener(rightListener);
        }
    }

    /**
     * 设置右侧按钮是否可点击
     */
    public void setTittleRightEnabled(boolean isEnabled) {
        if (tvCommonTitleRight.getVisibility() == View.VISIBLE)
            tvCommonTitleRight.setEnabled(isEnabled);
        if (ivCommonRight.getVisibility() == View.VISIBLE)
            ivCommonRight.setEnabled(isEnabled);
    }

    /**
     * 清除右侧图标和文字
     */
    protected void titleRightClear() {
        tvCommonTitleRight.setVisibility(View.GONE);
        ivCommonRight.setVisibility(View.GONE);
    }

//-------------------------标题栏相关end--------------------//

    @Override
    public <T> void updateView(T t) {

    }

    @Override
    public <T> void updateViewWithLoadMore(T t, boolean loadMore) {

    }

    @Override
    public <T> void updateViewWithFlag(T t, int flag) {

    }

    @Override
    public void onLoadingStatus(int loadingStatus) {
        if (mContext == null || isFinishing() || mLoadView == null) return;
        mLoadView.onLoading(loadingStatus, hasDataInPage(), needSupportRefresh());
    }

    @Override
    public void onLoadingStatus(int loadingStatus, String string) {
        if (mContext == null || isFinishing() || mLoadView == null) return;
        mLoadView.onLoading(loadingStatus, string, hasDataInPage(), needSupportRefresh());
    }
    //-------------------------------点击editText以外的区域start-----------------------------------//

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null) {
                if (getCurrentFocus().getWindowToken() != null) {
                    mImm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     *
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    //-------------------------------点击editText以外的区域end-----------------------------------//

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLoadView != null)
            mLoadView = null;
    }


    @Override
    /**
     * 授权成功回调
     */
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Log.d(TAG, "onPermissionsGranted");
    }

    @Override
    /**
     * 授权失败回调
     */
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d(TAG, "onPermissionsDenied");
    }
}
