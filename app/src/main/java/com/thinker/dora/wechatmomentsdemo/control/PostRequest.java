package com.thinker.dora.wechatmomentsdemo.control;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.thinker.dora.wechatmomentsdemo.interfaces.OnResponseListener;
import com.thinker.dora.wechatmomentsdemo.interfaces.OnUserInfoResponse;
import com.thinker.dora.wechatmomentsdemo.model.TweetsItemBean;
import com.thinker.dora.wechatmomentsdemo.model.TweetsResponse;
import com.thinker.dora.wechatmomentsdemo.model.UserBean;
import com.thinker.dora.wechatmomentsdemo.tools.Constants;
import com.thinker.dora.wechatmomentsdemo.tools.NetLoad;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class PostRequest {

    private static OnUserInfoResponse userResponse;
    private static Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (userResponse != null) {
                String json = (String) msg.obj;
                Gson mGson = new Gson();
                UserBean userBean = mGson.fromJson(json, UserBean.class);
                userBean.setProfileImage(parseProfile(json));
                userResponse.onSuccess(userBean);
            }
        }
    };

    public static UserBean getUserInfo(OnUserInfoResponse onUserResponse){
        userResponse = onUserResponse;
        UserBean bean = new UserBean();
        new Thread(){
            @Override
            public void run() {
                super.run();
                Message msg = mHandler.obtainMessage(0, postUrl(Constants.HEAD));
                mHandler.sendMessage(msg);
            }
        }.start();
        return bean;

    }

    public static void getData(final Context mContext,final OnResponseListener onPaperDateResponseListener) {
        if (networkDetector(mContext) == 0) {
            onPaperDateResponseListener.onFailed();
        } else {
            NetLoad.loadGetGson(mContext, TweetsResponse.class,
                    new Response.Listener<TweetsResponse>() {

                        @Override
                        public void onResponse(TweetsResponse response) {
                            if (response != null && response.getData().length > 0) {
                                ArrayList<TweetsItemBean> list = new ArrayList<TweetsItemBean>();
                                for (int i = 0; i < response.getData().length; i++) {
                                    if (response.getData()[i].getSender() != null) {
                                        list.add(response.getData()[i]);
                                    }
                                }
                                onPaperDateResponseListener.onSuccess(list);
                            }else{
                                onPaperDateResponseListener.onFailed();
                            }
                        }

                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            onPaperDateResponseListener.onFailed();
                        }
                    }, Constants.URL);
        }

    }


    /**
     * 检测网络状态，Activity，无网络返回0，wifi返回1，其他返回2
     */
    public static int networkDetector(Context act) {
        try {
            ConnectivityManager manager = (ConnectivityManager) act.getSystemService(
                    Context.CONNECTIVITY_SERVICE);
            if (manager == null) {
                return 0;
            }
            NetworkInfo networkinfo = manager.getActiveNetworkInfo();

            if (networkinfo != null
                    && networkinfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return 1;
            } else if (networkinfo == null || !networkinfo.isAvailable()) {
                return 0;
            }
        } catch (NullPointerException e) {
            return 0;
        }
        return 2;
    }

    public static String postUrl(String url) {

        String content = "";
        String str = "1";
        HttpURLConnection connection;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inputStream));
            StringBuffer buffer = new StringBuffer();
            while ((content = reader.readLine()) != null) {
                buffer.append(content);
            }
            str = buffer.toString();
            inputStream.close();
        } catch (MalformedURLException e) {
            str = "2";
        } catch (IOException e) {
            str = "0";
        }
        return str;
    }

    private static String parseProfile(String json) {
        if (json == null || json.equals("")) {
            return "";
        }
        try {
            JSONObject jsonObject = new JSONObject(json);
            if (jsonObject.has("profile-image")) {
                return jsonObject.getString("profile-image");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }
}
