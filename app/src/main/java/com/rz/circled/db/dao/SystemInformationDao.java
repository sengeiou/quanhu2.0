package com.rz.circled.db.dao;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.rz.circled.db.DBHelper;
import com.rz.circled.db.model.SystemInformation;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rzw2 on 2017/8/14.
 */

public class SystemInformationDao {
    private Dao<SystemInformation, Integer> systemDaoOpe;
    private DBHelper helper;

    @SuppressWarnings("unchecked")
    public SystemInformationDao(Context context) {
        try {
            helper = DBHelper.getHelper(context);
            systemDaoOpe = helper.getDao(SystemInformation.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加一个systemInformation
     *
     * @param systemInformation
     */
    public void add(SystemInformation systemInformation) {
        try {
            systemDaoOpe.createOrUpdate(systemInformation);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除一条记录
     *
     * @param systemInformation
     */
    public void delete(SystemInformation systemInformation) {
        try {
            systemDaoOpe.delete(systemInformation);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * 更新一条记录
     *
     * @param systemInformation
     */
    public void update(SystemInformation systemInformation) {
        try {
            systemDaoOpe.update(systemInformation);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询一条记录
     *
     * @param id
     * @return
     */
    public SystemInformation queryForId(int id) {
        SystemInformation theme = null;
        try {
            theme = systemDaoOpe.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return theme;
    }

    /**
     * 查询一条记录
     *
     * @param sid
     * @return
     */
    public SystemInformation queryForSid(String sid) {
        SystemInformation theme = null;
        try {
            List<SystemInformation> list = systemDaoOpe.queryBuilder().where().eq("sid", sid).query();
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
    public List<SystemInformation> queryForAll() {
        List<SystemInformation> themes = new ArrayList<>();
        try {
            themes = systemDaoOpe.queryForAll();
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
    public List<SystemInformation> queryForAllReverse() {
        List<SystemInformation> themes = new ArrayList<>();
        try {
            themes = systemDaoOpe.queryBuilder().orderBy("sid", false).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return themes;
    }

}
