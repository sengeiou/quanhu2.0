package com.rz.common.cache.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/23 0013.
 */
public class NoticeCache<T> {

    private Gson gson = null;
    private Context context;
    private Class<T> mClass;

    public NoticeCache(Context context, Class<T> entityClass) {
        gson = new Gson();
        this.context = context;
        this.mClass = entityClass;
    }

    public static final String notice_cache = "notice_cache";

    public static final String LIST_KEY = "list_notice_key";

    public final void putEntity(T t) {
        try {
            write(context, gson.toJson(t), t.getClass().getCanonicalName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final T getEntity(Class<T> cls) {
        T t = null;
        try {
            t = read(context, cls);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    public final List<T> getListEntityAddTag(Class<T> cls, String type) {
        if (null == context)
            return null;
        SharedPreferences sp = context.getSharedPreferences(notice_cache, Context.MODE_PRIVATE);
        String result = sp.getString(LIST_KEY + type + cls.getCanonicalName(), "");
        Log.d("ChannelMode", "key is " + LIST_KEY + type + cls.getCanonicalName());
        Log.d("ChannelMode", "getListEntityAddTag " + result);
        ArrayList<T> mList = new ArrayList<T>();
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        try {
            JsonArray array = new JsonParser().parse(result).getAsJsonArray();
            for (final JsonElement elem : array) {
                mList.add(gson.fromJson(elem, cls));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return mList;
    }


    public final void putListEntityAddTag(List<T> object, String type) {
        if (object == null || object.size() == 0) {
            if (mClass != null) {
                write(context, gson.toJson(object), LIST_KEY + type + mClass.getCanonicalName());
            }
            return;
        }
        write(context, gson.toJson(object), LIST_KEY + type + object.get(0).getClass().getCanonicalName());
    }

    private void write(Context context, String cacheStr, String tag) {
        if (null == context)
            return ;
        SharedPreferences sp = context.getSharedPreferences(notice_cache, Context.MODE_PRIVATE);
        sp.edit().putString(tag, cacheStr).commit();
        Log.d("ChannelMode", "write tag is  " + tag);
        Log.d("ChannelMode", "sp.getString  is  " + sp.getString(tag, ""));
    }

    private T read(Context context, Class<T> cls) {
        SharedPreferences sp = context.getSharedPreferences(notice_cache, Context.MODE_PRIVATE);
        return gson.fromJson((sp.getString(cls.getCanonicalName(), "")), cls);
    }

    public final void putListEntity(List<T> object) {
        putListEntityAddTag(object, "");
    }

    public final List<T> getListEntity(Class<T> cls) {
        return getListEntityAddTag(cls, "");
    }

//    private static Object readList(Context context, TypeToken typeToken) {
//        SharedPreferences sp = context.getSharedPreferences(notice_cache, Context.MODE_PRIVATE);
//        return gson.toJson((sp.getString(cls.getCanonicalName(), "")), cls);
//        List<Object> retList = gson.fromJson(sp.getString("list-" + cls, ""), typeToken.getType());
//
//    }

    public static void clean(Context context) {
        SharedPreferences sp = context.getSharedPreferences(notice_cache, Context.MODE_PRIVATE);
        sp.edit().clear().commit();
    }
}
