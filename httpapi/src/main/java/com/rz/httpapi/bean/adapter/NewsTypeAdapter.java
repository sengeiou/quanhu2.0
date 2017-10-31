package com.rz.httpapi.bean.adapter;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.rz.httpapi.bean.NewsBean;

import java.io.IOException;

/**
 * Created by rzw2 on 2017/10/16.
 */

public class NewsTypeAdapter extends TypeAdapter<JsonObject> {
    @Override
    public void write(JsonWriter out, JsonObject value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        out.value(value.toString());
    }

    @Override
    public JsonObject read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return new JsonObject();
        }
        return new JsonParser().parse(in.nextString()).getAsJsonObject();
    }
}
