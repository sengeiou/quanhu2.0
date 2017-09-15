package com.rz.common.ui.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.rz.common.R;
import com.rz.common.cache.preference.Session;
import com.rz.common.permission.EasyPermissions;
import com.rz.common.ui.inter.IViewController;
import com.rz.common.ui.view.BaseLoadView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Gsm on 2017/8/23.
 */
public abstract class BaseFragment extends Fragment implements IViewController, EasyPermissions.PermissionCallbacks {
    private Unbinder unbinder;
    public Activity mActivity;
    public String TAG;
    protected boolean isViewInit = false;
    private boolean isAddIn = false;
    private BaseLoadView mLoadView;

    public BaseFragment() {
        TAG = getClass().getSimpleName();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_base_fragment, null);
        LinearLayout llRoot = (LinearLayout) rootView.findViewById(R.id.ll_base_fragment_root);
        initContentView(inflater, llRoot);
        unbinder = ButterKnife.bind(this, rootView);
        isViewInit = true;
        initPresenter();
        initView();
        initData();
        return rootView;
    }

    public void initPresenter() {

    }

    private void initContentView(LayoutInflater inflater, LinearLayout llContent) {
        if (needLoadingView()) {
            View loadView = inflater.inflate(R.layout.layout_base_load, null);
            mLoadView = new BaseLoadView(mActivity, loadView, llContent);
            ViewGroup.LayoutParams layoutParams = loadView.getLayoutParams();
            if (layoutParams == null)
                layoutParams = new ViewGroup.LayoutParams(-1, -1);
            else {
                layoutParams.height = -1;
                layoutParams.width = -1;
            }
            llContent.addView(loadView,layoutParams);
        }
        if (loadView(inflater) != null){
            View contentView = loadView(inflater);
            ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
            if (layoutParams == null) {
                layoutParams = new ViewGroup.LayoutParams(-1, -1);
            } else {
                layoutParams.height = -1;
                layoutParams.width = -1;
            }
            llContent.addView(contentView,layoutParams);
        }
    }

    @Nullable
    public View loadView(LayoutInflater inflater) {
        return null;
    }


    public abstract void initView();

    public abstract void initData();


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d(TAG, "setUserVisibleHint:" + isVisibleToUser);
        if (isViewInit) {
            if (getUserVisibleHint()) {
                onVisible();
            } else {
                onInVisible();
            }
        }
    }
    /**
     * 指定Activity跳转，不带参数
     *
     * @param claz 组件名
     */
    protected void jump(Class<? extends Activity> claz) {
        jump(claz, null);
    }

    /**
     * 指定Activity跳转，带参数
     *
     * @param claz 组件名
     * @param data 参数
     */
    protected void jump(Class<? extends Activity> claz, Bundle data) {
        Intent intent = new Intent(getContext(), claz);
        if (data != null) {
            intent.putExtras(data);
        }
        startActivity(intent);
    }
    /**
     * 用户是否登录
     */
    public boolean isLogin() {
        if (Session.getUserIsLogin()) {
            return true;
        } else {
//            Intent login = new Intent(mActivity, LoginActivity.class);
//            startActivityForResult(login, IntentCode.Login.LOGIN_REQUEST_CODE);
            return false;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.d(TAG, "onHiddenChanged:" + hidden);
        if (isViewInit) {
            if (!isHidden()) {
                onVisible();
            } else {
                onInVisible();
            }
        }
    }

    /**
     * Fragment可见时回调
     */
    protected void onVisible() {
        Log.d(TAG, "onVisible");
    }

    /**
     * Fragment隐藏时回调
     */
    protected void onInVisible() {
        Log.d(TAG, "onInVisible");
    }

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
     * 是否支持空页面、错误页面点击刷新
     *
     * @return
     */
    protected boolean needSupportRefresh() {
        return true;
    }

    @Override
    public <T> void updateView(T t) {

    }

    @Override
    public <T> void updateViewWithLoadMore(T t, boolean loadMore) {

    }

    @Override
    public <T> void updateViewWithFlag(T t, int flag) {

    }

    public void setRefreshListener(BaseLoadView.RefreshListener refreshListener) {
        if (mLoadView != null) mLoadView.setRefreshListener(refreshListener);
    }

    @Override
    public void onLoadingStatus(int loadingStatus) {
        if (mActivity == null || mActivity.isFinishing() || mLoadView == null) return;
        mLoadView.onLoading(loadingStatus, hasDataInPage(), needSupportRefresh());
    }

    @Override
    public void onLoadingStatus(int loadingStatus, String string) {
        if (mActivity == null || mActivity.isFinishing() || mLoadView == null) return;
        mLoadView.onLoading(loadingStatus, string, hasDataInPage(), needSupportRefresh());
    }

    /**
     * 设置功能按钮显示文字
     *
     * @param stringId
     */
    public void setFunctionText(int stringId) {
        setFunctionText(getString(stringId));
    }

    /**
     * 设置功能按钮显示文字
     *
     * @param string
     */
    public void setFunctionText(String string) {
        if (mActivity == null || mActivity.isFinishing() || mLoadView == null) return;
        mLoadView.setFunctionText(string);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mLoadView = null;
        unbinder.unbind();
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
