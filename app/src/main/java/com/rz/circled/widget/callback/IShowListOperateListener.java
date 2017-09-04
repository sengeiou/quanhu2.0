package com.rz.circled.widget.callback;


import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rz.circled.modle.CommentModel;
import com.rz.circled.modle.ShowListModel;
import com.rz.circled.ui.view.circle_favite.FavortListAdapter;
/**
 * @author wsf
 * @ClassName: IShowListOperateListener
 * @Description: view, 晒一晒页面 点赞，取消点赞，删除item，评论操作
 * @date 2016-8-27
 */
public interface IShowListOperateListener {

    /**
     * @param position 按下的item
     * @param info     模型
     * @param rateImg  点赞icon
     * @param rateLl   点赞列表
     * @param parentLl 是否显示点赞列表
     * @param isFirst  是否是第一次点赞
     * @param fa       刷新点赞列表
     */
    public void OnRate(int position, ShowListModel info, ImageView rateImg, TextView rateLl, LinearLayout parentLl, boolean isFirst, FavortListAdapter fa);

    public void OnDeleteItem(ShowListModel info);

    /**
     * @param position 按下的item
     * @param sid      随手晒id
     * @param authId   评论对象id
     */
    public void OnComment(int position, int sid, String authId, String content, ShowListModel info, CommentModel cInfo);

}
