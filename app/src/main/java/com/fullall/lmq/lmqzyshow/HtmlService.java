package com.fullall.lmq.lmqzyshow;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HtmlService {
    public static String getHtml(String path) throws Exception {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        conn.setReadTimeout(5000);
        if (conn.getResponseCode() == 200) {
            InputStream inputStream = conn.getInputStream();
            ByteArrayOutputStream data = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buffer)) != -1) {
                data.write(buffer, 0, len);
            }
            inputStream.close();
            return new String(data.toByteArray(), "utf-8");
        }
        return "获取源代码失败";
    }
}