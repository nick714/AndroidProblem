package com.thinker.dora.wechatmomentsdemo.tools;


import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.thinker.dora.wechatmomentsdemo.model.TweetsItemBean;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class PostGsonRequest<T> extends Request<T> {
	private final Gson mGson = new Gson();
	private Class<T> mClazz;
	private Response.Listener<T> mListener;
	private Map<String, String> mHeaders;

	public PostGsonRequest(String url, Class<T> clazz, Response.Listener<T> listener,
			Response.ErrorListener errorListener) {
		this(Request.Method.GET, url, clazz, listener, errorListener, null);
		this.mClazz = clazz;
		mListener = listener;

	}

	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		// TODO Auto-generated method stub
		return mHeaders != null ? mHeaders : super.getParams();
	}

	public PostGsonRequest(int method, String url, Class<T> clazz,
			Response.Listener<T> listener, Response.ErrorListener errorListener,
			Map<String, String> headers) {
		super(method, url, errorListener);
		this.mClazz = clazz;
		this.mHeaders = headers;
		this.mListener = listener;
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		return mHeaders != null ? mHeaders : super.getHeaders();
	}

	@Override
	protected void deliverResponse(T response) {
		mListener.onResponse(response);
	}

//	@Override
//	public RetryPolicy getRetryPolicy() {
//		RetryPolicy retryPolicy = new DefaultRetryPolicy(Value.HTTP_TIMEOUT,
//				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//		return retryPolicy;
//	}

	@Override
	protected Response<T> parseNetworkResponse(NetworkResponse response) {
		try {
			String json = new String(response.data,
					HttpHeaderParser.parseCharset(response.headers));
			json = exchangeJson(json);
			return Response.success(mGson.fromJson(json, mClazz),
					HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (JsonSyntaxException e) {
			return Response.error(new ParseError(e));
		}
	}

	private String exchangeJson(String json) {
		StringBuffer buffer = new StringBuffer().append("{").append("\"data\"").append(":").append(json).append("}");
		return buffer.toString();
	}
}