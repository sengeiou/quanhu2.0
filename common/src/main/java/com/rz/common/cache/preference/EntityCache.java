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
 * Created by Administrator on 2016/8/13 0013.
 */
public class EntityCache<T> {

    private static final String ENTITY_CACHE = "entity_cache";
    private static final String LIST_KEY = "list_key";
    private static final String ENTITY_KEY = "entity_key";

    private Gson gson = null;
    private Context context;
    private Class<T> mClass;

    public EntityCache(Context context, Class<T> entityClass) {
        gson = new Gson();
        this.context = context;
        this.mClass = entityClass;
    }

    public final void putEntity(T t) {
        putEntity(t, "");
    }

    public final void putEntity(T t, String tag) {
        if (null == context) return;
        try {
            write(context, gson.toJson(t), ENTITY_KEY + tag + t.getClass().getCanonicalName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final T getEntity(Class<T> cls) {
        return getEntity(cls, "");
    }

    public final T getEntity(Class<T> cls, String tag) {
        if (null == context) return null;
        T t = null;
        try {
            t = read(cls, ENTITY_KEY + tag + cls.getCanonicalName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    public final void putListEntity(List<T> object) {
        putListEntityAddTag(object, "");
    }

    public final List<T> getListEntity(Class<T> cls) {
        return getListEntityAddTag(cls, "");
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

    public final List<T> getListEntityAddTag(Class<T> cls, String type) {
        if (null == context)
            return null;
        SharedPreferences sp = context.getSharedPreferences(ENTITY_CACHE, Context.MODE_PRIVATE);
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


    private void write(Context context, String cacheStr, String tag) {
        SharedPreferences sp = context.getSharedPreferences(ENTITY_CACHE, Context.MODE_PRIVATE);
        sp.edit().putString(tag, cacheStr).apply();
        Log.d("ChannelMode", "write tag is  " + tag);
        Log.d("ChannelMode", "sp.getString  is  " + sp.getString(tag, ""));
    }

    private T read(Class<T> cls, String tag) {
        SharedPreferences sp = context.getSharedPreferences(ENTITY_CACHE, Context.MODE_PRIVATE);
        return gson.fromJson((sp.getString(tag, "")), cls);
    }

    public static void clean(Context context) {
        SharedPreferences sp = context.getSharedPreferences(ENTITY_CACHE, Context.MODE_PRIVATE);
        sp.edit().clear().apply();
    }

    public void clean() {
        clean("");
    }

    public void clean(String tag) {
        if (mClass != null) {
            SharedPreferences sp = context.getSharedPreferences(ENTITY_CACHE, Context.MODE_PRIVATE);
            String key = LIST_KEY + tag + mClass.getCanonicalName();
            sp.edit().remove(key).apply();
        }
    }

    public void cleanEntity(Class<T> cls, String tag) {
        SharedPreferences sp = context.getSharedPreferences(ENTITY_CACHE, Context.MODE_PRIVATE);
        String key = ENTITY_KEY + tag + cls.getCanonicalName();
        sp.edit().remove(key).apply();
    }
}
