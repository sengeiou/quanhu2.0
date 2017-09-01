package com.rz.circled.presenter;


import com.rz.common.ui.inter.IViewController;

/**
 * Created by JS01 on 2016/6/7.
 * MVP Presenter层，加载数据并控制View层
 */
public interface IPresenter {


    //
    public static final int SUCRESS_FULL = 2000;
    public static final int SUCRESS_NO_DATA = 2001;

    public static final int BEGIN = 2002;
    public static final int DATA_NULL = 2003;

    public static final int ERROR_LOAD = 3001;
    public static final int ERROR_NET = 3002;

    public static final int DEFAULT = 10000;

    /**
     * 与View（Activity或Fragment）关联，在onCreate中调用，
     *
     * @param view
     */
    public void attachView(IViewController view);

    /**
     * 加载数据，处理相关逻辑
     */
    public void loadData();

    /**
     * 与View分离，在onDestory调用
     */
    public void detachView();

}
