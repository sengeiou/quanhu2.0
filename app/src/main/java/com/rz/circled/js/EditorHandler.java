package com.rz.circled.js;

import android.app.Activity;
import android.util.Log;

import com.google.gson.Gson;
import com.rz.sgt.jsbridge.BaseParamsObject;
import com.rz.sgt.jsbridge.ServerHandler;
import com.rz.sgt.jsbridge.core.Callback;
import com.rz.sgt.jsbridge.core.ParamsObject;
import com.rz.sgt.jsbridge.core.WebViewProxy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 作品编辑处理
 */

public class EditorHandler extends ServerHandler {

    public EditorHandler(Activity mActivity) {
        super(mActivity);
    }


    public static class EditorEntity {

        public ArrayList<Map<String, Object>> content;
        public int imgMaxNum;//20
        public int imgCurrentNum;
        public int txtMaxNum;//10000
        public int currentTxtNum;
        public boolean videoEnable = true;//true
        public boolean audioEnable = true;//true
        public boolean imageEnable = true;//true

//        public Config config;
//        public Info info;

//        public static class Config {
//            public VideoConfig video;
//            public AudioConfig audio;
//            public ImageConfig image;
//            public TextConfig text;
//
//            public static class VideoConfig {
//                public boolean enable;
//            }
//
//            public static class AudioConfig {
//                public boolean enable;
//            }
//
//            public static class ImageConfig {
//                public boolean enable;
//                public int imgMaxNum;
//            }
//
//            public static class TextConfig {
//                public int txtMaxNum;
//            }
//        }
//
//        public static class Info {
//            public VideoInfo video;
//            public AudioInfo audio;
//            public ImageInfo image;
//            public TextInfo text;
//
//            public static class VideoInfo {
//                public boolean enable;
//            }
//
//            public static class AudioInfo {
//                public boolean enable;
//            }
//
//            public static class ImageInfo {
//                public boolean enable;
//                public int imgMaxNum;
//                public int imgCurrentNum;
//            }
//
//            public static class TextInfo {
//                public int txtMaxNum;
//                public int currentTxtNum;
//            }
//        }

    }

    @Override
    public String getInvokeName() {
        return "createImageText";
    }

    @Override
    public void handle(String params, ParamsObject paramObj, Callback callback) {
        ArrayList<Map<String, Object>> list = new ArrayList<>();
//        Map<String, Object> map = new HashMap<>();
//        map.put("text", "2132");
//        list.add(map);
//        Map<String, Object> map1 = new HashMap<>();
//        map1.put("image", "http://yryz-resources.oss-cn-hangzhou.aliyuncs.com/pic/opus/3742ed41-21a2-434d-8d14-693761ef637d.jpg");
//        list.add(map1);
//        Map<String, Object> map2 = new HashMap<>();
//        map2.put("image", "http://yryz-resources.oss-cn-hangzhou.aliyuncs.com/pic/opus/5a771e79-13c9-42a1-93d3-ce9e970c34d5.jpg");
//        list.add(map2);
//        Map<String, Object> map3 = new HashMap<>();
//        map3.put("text", "右骒");
//        list.add(map3);
//        EditorActivity.VideoEntity videoEntity = new EditorActivity.VideoEntity();
//        videoEntity.url = "http://yryz-resources.oss-" +
//                "cn-hangzhou.aliyuncs.com/efdb6cfc-ba24-4dbc-8c0a-e14a572033b8.mp4";
//        videoEntity.thumbnailImage = "http://yryz-" +
//                "resources.oss-cn-hangzhou.aliyuncs.com/pic/opus/4cfa8cae-1da9-45f1-b4c5-e3ce0583ba71.jpg";
//        Map<String, Object> map4 = new HashMap<>();
//        map4.put("video", videoEntity);
//        list.add(map4);
//
//        EditorActivity.AudioEntity audioEntity = new EditorActivity.AudioEntity();
//        audioEntity.url = "http://yryz-" +
//                "resources.oss-cn-hangzhou.aliyuncs.com/ed911897-a095-4dd1-b0fa-a8200cb181ea.mp3";
//        audioEntity.time = 1200;
//        Map<String, Object> map5 = new HashMap<>();
//        map5.put("audio", audioEntity);
//        list.add(map5);
////
////        String json = new Gson().toJson(list);
//
//        list.clear();
//
//        EditorEntity editorEntity = new EditorEntity();
//        editorEntity.content = list;
//        editorEntity.config = new EditorEntity.Config();
//        editorEntity.config.video = new EditorEntity.Config.VideoConfig();
//        editorEntity.config.video.enable = false;
//        editorEntity.config.audio = new EditorEntity.Config.AudioConfig();
//        editorEntity.config.audio.enable = false;
//        editorEntity.config.image = new EditorEntity.Config.ImageConfig();
//        editorEntity.config.image.enable = true;
//        editorEntity.config.image.imgMaxNum = 10;
//        editorEntity.config.text = new EditorEntity.Config.TextConfig();
//        editorEntity.config.text.txtMaxNum = 1000;

        EditorEntity editorEntity = null;

        try {
            String json = new Gson().toJson(paramObj.data);
            editorEntity = new Gson().fromJson(json, EditorEntity.class);
            String contentJson = new Gson().toJson(editorEntity.content);
            org.json.JSONArray jsonArray = new org.json.JSONArray(contentJson);
            Map<String, Object> temp = null;
            org.json.JSONObject jsonObject1 = null;
            for (int i = 0; i < jsonArray.length(); i++) {
                org.json.JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.has("text")) {
                    temp = new HashMap<>();
                    temp.put("text", jsonObject.getString("text"));
                    list.add(temp);
                    continue;
                } else if (jsonObject.has("image")) {
                    temp = new HashMap<>();
                    temp.put("image", jsonObject.getString("image"));
                    list.add(temp);
                    continue;
                } else if (jsonObject.has("video")) {
                    temp = new HashMap<>();
                    jsonObject1 = jsonObject.getJSONObject("video");
//                    if (jsonObject1 != null) {
//                        EditorActivity.VideoEntity videoEntity1 = new EditorActivity.VideoEntity();
//                        if (jsonObject1.has("thumbnailImage")) {
//                            videoEntity1.thumbnailImage = jsonObject1.getString("thumbnailImage");
//                        }
//                        if (jsonObject1.has("url")) {
//                            videoEntity1.url = jsonObject1.getString("url");
//                        }
//                        temp.put("video", videoEntity1);
//                        list.add(temp);
//                    }
                    continue;
                } else if (jsonObject.has("audio")) {
                    temp = new HashMap<>();
                    jsonObject1 = jsonObject.getJSONObject("audio");
//                    if (jsonObject1 != null) {
//                        EditorActivity.AudioEntity audioEntity1 = new EditorActivity.AudioEntity();
//                        if (jsonObject1.has("time")) {
//                            audioEntity1.time = jsonObject1.getLong("time");
//                        }
//                        if (jsonObject1.has("url")) {
//                            audioEntity1.url = jsonObject1.getString("url");
//                        }
//                        temp.put("audio", audioEntity1);
//                        list.add(temp);
//                    }
                    continue;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("yeying", "Exception " + e.getMessage());
        }


        Log.d("yeying", "list " + list.toString());
//        final Intent i = new Intent(mActivity, EditorActivity.class);
//        if (list != null) {
//            i.putExtra("map", list);
//        }
        //------------------------
//        if (editorEntity != null) {
//            if (editorEntity.txtMaxNum < 0 || editorEntity.txtMaxNum > 10000) {
//                editorEntity.txtMaxNum = 10000;
//            }
//            i.putExtra(IntentKey.KEY_TEXT_MAX, editorEntity.txtMaxNum);
//            if (editorEntity.imageEnable) {
//                if (editorEntity.imgMaxNum <= 0 || editorEntity.imgMaxNum > 20) {
//                    editorEntity.imgMaxNum = 20;
//                }
//                i.putExtra(IntentKey.KEY_IMAGE_MAX, editorEntity.imgMaxNum);
//            }
//            i.putExtra(IntentKey.KEY_ENABLE_IMAGE, editorEntity.imageEnable);
//            i.putExtra(IntentKey.KEY_ENABLE_AUDIO, editorEntity.audioEnable);
//            i.putExtra(IntentKey.KEY_ENABLE_VIDEO, editorEntity.videoEnable);
//        }
//        Log.d("yeying","editorEntity  "+editorEntity.toString());
//        mActivity.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                i.putExtra(IntentKey.KEY_URL, webContainerAty.getOssDir());
//                mActivity.startActivity(i);
//            }
//        });
    }

    @Override
    public Callback generateCallBack(WebViewProxy webViewProxy, ParamsObject paramsObject) {
        Callback callback = new Callback(webViewProxy, paramsObject.getInvokeId(), paramsObject.getInvokeName()) {
            @Override
            public void invoke(Object businessParms, BaseParamsObject baseParamsObject) {
                baseParamsObject.data = businessParms;
            }
        };
        return callback;
    }

    @Override
    public boolean isUi() {
        return true;
    }
}
