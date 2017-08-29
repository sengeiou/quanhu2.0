package com.rz.common.ui.inter;

/**
 * Created by JS01 on 2016/6/7.
 * MVP View层，只处理UI相关逻辑
 */
public interface IViewController {
    /**
     * 更新UI
     *
     * @param t   数据
     * @param <T>
     */
    <T> void updateView(T t);

    /**
     * 带分页的更新UI，主要用於界面元素
     *
     * @param t
     * @param loadMore
     * @param <T>
     */
    <T> void updateViewWithLoadMore(T t, boolean loadMore);

    /**
     * 带状态返回
     *
     * @param t
     * @param flag
     * @param <T>
     */
    <T> void updateViewWithFlag(T t, int flag);

    /**
     * 处理默认提示
     *
     * @param loadingStatus
     */
    void onLoadingStatus(int loadingStatus);

    /**
     * 根据状态处理提醒
     *
     * @param loadingStatus
     * @param string
     */
    void onLoadingStatus(int loadingStatus, String string);


}
