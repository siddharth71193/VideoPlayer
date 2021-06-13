package com.example.videosample1.util;

import android.content.Context;

import com.example.videosample1.model.VideoModel;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;

public class CommonUtils {

    public static VideoModel parseJson(Context context){
        VideoModel myModel = null;
        try {
            String myJson=inputStreamToString(context.getResources().getAssets().open("video_urls.json"));
            myModel = new Gson().fromJson(myJson, VideoModel.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return myModel;
    }

    public static String inputStreamToString(InputStream inputStream) {
        try {
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes, 0, bytes.length);
            String json = new String(bytes);
            return json;
        } catch (IOException e) {
            return null;
        }
    }
}
