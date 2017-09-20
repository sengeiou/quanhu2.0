package com.rz.circled.db.dao;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.rz.circled.db.DBHelper;
import com.rz.httpapi.bean.FriendInformationBean;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by rzw2 on 2017/8/14.
 */

public class FriendInformationDao {
    private Dao<FriendInformationBean, Integer> friendDao;
    private DBHelper helper;

    @SuppressWarnings("unchecked")
    public FriendInformationDao(Context context) {
        try {
            helper = DBHelper.getHelper(context);
            friendDao = helper.getDao(FriendInformationBean.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加一个systemInformation
     *
     * @param friendInformationBean
     */
    public void add(FriendInformationBean friendInformationBean) {
        try {
            friendDao.createOrUpdate(friendInformationBean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除一条记录
     *
     * @param friendInformationBean
     */
    public void delete(FriendInformationBean friendInformationBean) {
        try {
            friendDao.delete(friendInformationBean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * 删除多条记录
     *
     * @param beanList
     */
    public void delete(List<FriendInformationBean> beanList) {
        try {
            friendDao.delete(beanList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新一条记录
     *
     * @param friendInformationBean
     */
    public void update(FriendInformationBean friendInformationBean) {
        try {
            friendDao.update(friendInformationBean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新多条记录
     *
     * @param beanList
     */
    public void update(final List<FriendInformationBean> beanList) {
        try {
            friendDao.callBatchTasks(new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    for (FriendInformationBean bean : beanList) {
                        friendDao.createOrUpdate(bean);
                    }
                    return null;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存多条记录
     *
     * @param beanList
     */
    public void save(final List<FriendInformationBean> beanList) {
        try {
            friendDao.create(beanList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询一条记录
     *
     * @param id
     * @return
     */
    public FriendInformationBean queryForId(int id) {
        FriendInformationBean theme = null;
        try {
            theme = friendDao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return theme;
    }

    /**
     * 查询一条记录
     *
     * @param custId
     * @return
     */
    public FriendInformationBean queryForSid(String custId) {
        FriendInformationBean theme = null;
        try {
            List<FriendInformationBean> list = friendDao.queryBuilder().where().eq("custId", custId).query();
            if (list != null && !list.isEmpty() && list.size() > 0) {
                theme = list.get(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return theme;
    }

    /**
     * 查询所有记录
     *
     * @return
     */
    public List<FriendInformationBean> queryForAll() {
        List<FriendInformationBean> themes = new ArrayList<>();
        try {
            themes = friendDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return themes;
    }

    /**
     * 查询所有记录倒序
     *
     * @return
     */
    public List<FriendInformationBean> queryForAllReverse() {
        List<FriendInformationBean> themes = new ArrayList<>();
        try {
            themes = friendDao.queryBuilder().orderBy("custId", false).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return themes;
    }

}
