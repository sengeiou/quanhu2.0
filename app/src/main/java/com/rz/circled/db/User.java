package com.rz.circled.db;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by Administrator on 2017/9/7/007.
 */

public class User {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = "appid")
    private String name;
    @DatabaseField(columnName = "count")
    private int count;

    public User()
    {
    }

    public User(String name, int count)
    {
        this.name = name;
        this.count = count;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getDesc()
    {
        return count;
    }

    public void setDesc(int count)
    {
        this.count = count;
    }
}
