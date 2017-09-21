package com.rz.circled.db;

/**
 * Created by rzw2 on 2017/8/14.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.rz.httpapi.bean.FriendInformationBean;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 功能：数据库帮助类
 */
public class DBHelper extends OrmLiteSqliteOpenHelper {
    /**
     * 数据库名字
     */
    private static final String DB_NAME = "circled.db";
    /**
     * 数据库版本
     */
    private static final int DB_VERSION = 1;
    private static DBHelper instance;
    /**
     * 用来存放Dao的地图
     */
    private Map<String, Dao> daos = new HashMap<>();

    /**
     * 构造方法
     *
     * @param context
     */
    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /**
     * 获取单例
     *
     * @param context
     * @return
     */
    public static synchronized DBHelper getHelper(Context context) {
        context = context.getApplicationContext();
        if (instance == null) {
            synchronized (DBHelper.class) {
                if (instance == null) {
                    instance = new DBHelper(context);
                }
            }
        }
        return instance;
    }

    /**
     * 这里创建表
     */
    @Override
    public void onCreate(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource) {
        // 创建表
        try {
            TableUtils.createTable(connectionSource, FriendInformationBean.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 这里进行更新表操作
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int oldVersion,
                          int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, FriendInformationBean.class, true);
            onCreate(sqLiteDatabase, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过类来获得指定的Dao
     */
    public synchronized Dao getDao(Class clazz) throws SQLException {
        Dao dao = null;
        String className = clazz.getSimpleName();
        if (daos.containsKey(className)) {
            dao = daos.get(className);
        }
        if (dao == null) {
            dao = super.getDao(clazz);
            daos.put(className, dao);
        }
        return dao;
    }


    /**
     * 释放资源
     */
    @Override
    public void close() {
        super.close();
        for (String key : daos.keySet()) {
            Dao dao = daos.get(key);
            dao = null;
        }
    }


}
