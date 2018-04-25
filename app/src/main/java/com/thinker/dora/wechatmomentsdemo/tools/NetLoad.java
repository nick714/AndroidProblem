package com.thinker.dora.wechatmomentsdemo.tools;

import android.app.Service;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.ImageView;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.json.JSONObject;

import java.util.Map;

public class NetLoad {
	
	public static NetLoad netload;
	
	public NetLoad getinstence(){
		if(netload==null){
			netload = new NetLoad();
		}
		return netload;
	}
	
	/**
	 * get形式，返回String 1、context 2、成功回调 Listener<JSONObject> 3、失败回调 ErrorListener
	 * 4、地址
	 */
	public static void loadGetString(Context context,
			Response.Listener<String> Rlistener, Response.ErrorListener Elistener, String url) {
		StringRequest stringRequest = new StringRequest(Request.Method.GET,
				url, Rlistener, Elistener);
		VolleyTool.getInstance(context).getmRequestQueue().add(stringRequest);
	}

	/**
	 * get形式，返回Gson post提交 1、context 2、成功回调 Listener<Gson> 3、失败回调 ErrorListener
	 * 4、地址 5、Map<String, String>
	 * 
	 * @param <T>
	 */
	public static <T> void loadGetGson(Context context, Class<T> clazz,
			Response.Listener<T> Rlistener, Response.ErrorListener Elistener, String url) {

		@SuppressWarnings("unchecked")
		Request<T> request = new PostGsonRequest(url, clazz, Rlistener,
				Elistener);
		VolleyTool.getInstance(context).getmRequestQueue().add(request);
	}

	/**
	 * post形式，返回Gson post提交 1、context 2、成功回调 Listener<Gson> 3、失败回调 ErrorListener
	 * 4、地址 5、Map<String, String>
	 * 
	 * @param <T>
	 */
	public static <T> void loadPostGson(Context context, Class<T> clazz,
			Response.Listener<T> Rlistener, Response.ErrorListener Elistener, String url,
			final Map<String, String> map) {

		@SuppressWarnings("unchecked")
		Request<T> request = new PostGsonRequest(Request.Method.POST, url, clazz,
				Rlistener, Elistener, map);
		VolleyTool.getInstance(context).getmRequestQueue().add(request);
	}

	/**
	 * post形式，返回Json post提交 1、context 2、成功回调 Listener<JSONObject> 3、失败回调
	 * ErrorListener 4、地址 5、Map<String, String>
	 */
	public static void loadPost(Context context,
			Response.Listener<JSONObject> Rlistener, Response.ErrorListener Elistener,
			String url, final Map<String, String> map) {

		Request<JSONObject> request = new PostRequest(url, Rlistener,
				Elistener, map);
		VolleyTool.getInstance(context).getmRequestQueue().add(request);
	}

	/**
	 * get形式，返回Json 1、context 2、成功回调 Listener<JSONObject> 3、失败回调 ErrorListener
	 * 4、地址
	 */
	public static void loadGet(Context context, Response.Listener<JSONObject> Rlistener,
			Response.ErrorListener Elistener, String url) {
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
				Request.Method.GET, url, null, Rlistener, Elistener);
		VolleyTool.getInstance(context).getmRequestQueue()
				.add(jsonObjectRequest);
	}

	/**
	 * 图片异步加载 1、context 2、图片控件 3、图片地址 4、默认图片 本地 5、失败图片 本地
	 */
	public static void loadImage(Context context, ImageView imgview,
			String url, int dimg, int eimg) {
		ImageLoader.ImageListener listener = ImageLoader.getImageListener(imgview, dimg,
				eimg);

		try {
			VolleyTool.getInstance(context).getmImageLoader()
					.get(url, listener);
		} catch (NullPointerException e) {
			imgview.setImageResource(eimg);
		}

	}
	public static void loadImage(final ImageView imageView,String url,int loading_image,int error_image){

		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(loading_image)
				.showImageForEmptyUri(error_image)
				.showImageOnFail(error_image)
				.cacheInMemory(true)
				.cacheOnDisk(true).build();
		com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(url, imageView,options);

	}
	/**
	 * 检测网络状态，Activity，无网络返回0，wifi返回1，其他返回2
	 */
	public static int NetworkDetector(Context act) {
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

	/**
	 * 检测网络状态，Activity，无网络返回0，wifi返回1，其他返回2
	 */
	public static int NetworkDetector(Service ser) {

		ConnectivityManager manager = (ConnectivityManager) ser
				.getApplicationContext().getSystemService(
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
		return 2;
	}
}
