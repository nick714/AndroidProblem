package cn.thinker.wechatmomentsdemo.core.request;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import cn.thinker.wechatmomentsdemo.common.LogLevel;
import cn.thinker.wechatmomentsdemo.common.ParcelablePoolObject;

public abstract class BasicJSONRequest<T> extends JsonRequest<T> {

    public static final String TAG = "BasicJSONRequest";
    private static final String PROTOCOL_CONTENT_TYPE = String.format(
            "application/x-www-form-urlencoded; charset=%s", PROTOCOL_CHARSET);

    protected Message mParam;
    protected String mGetUrl;

    public BasicJSONRequest(String url, Listener<T> listener,
                            ErrorListener errorListener, Message param) {
        super(Method.GET, url, "", listener, errorListener);
        mParam = param;
    }

    @Override
    public String getUrl() {
        if (getMethod() == Method.POST) {
            return super.getUrl();
        } else {
            if (mGetUrl == null) {
                mGetUrl = formatGetUrl(super.getUrl());
            }
            return mGetUrl;
        }
    }

    private String formatGetUrl(String orig) {
        StringBuilder sb = new StringBuilder(orig);
        Map<String, String> params = getParamsFromBundle(((ParcelablePoolObject) mParam.obj)
                .getData());
        if (params != null && params.size() > 0) {
            sb.append("?");
            try {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    sb.append(URLEncoder.encode(entry.getKey(), PROTOCOL_CHARSET));
                    sb.append('=');
                    sb.append(URLEncoder.encode(entry.getValue(),
                            PROTOCOL_CHARSET));
                    sb.append('&');
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    protected abstract T parseResult(String ob);

    protected abstract Map<String, String> getParamsFromBundle(Bundle param);

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        String error = "unknown error";
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            if (LogLevel.isLoggerable(LogLevel.DEBUG)) {
                Log.d(TAG, "receive json result: " + jsonString);
            }
            return Response.success(parseResult(jsonString), getCacheEntry());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            error = "unsupported encoding";
        }
        return Response.error(new VolleyError(error));
    }

    @Override
    public String getBodyContentType() {
        return PROTOCOL_CONTENT_TYPE;
    }

    @Override
    public byte[] getBody() {
        Map<String, String> params = getParamsFromBundle(((ParcelablePoolObject) mParam.obj)
                .getData());
        if (params != null && params.size() > 0) {
            return encodeParameters(params, getParamsEncoding());
        }
        return null;
    }

    private byte[] encodeParameters(Map<String, String> params,
                                    String paramsEncoding) {
        StringBuilder encodedParams = new StringBuilder();
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                encodedParams.append(URLEncoder.encode(entry.getKey(),
                        paramsEncoding));
                encodedParams.append('=');
                encodedParams.append(URLEncoder.encode(entry.getValue(),
                        paramsEncoding));
                encodedParams.append('&');
            }
            return encodedParams.toString().getBytes(paramsEncoding);
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: "
                    + paramsEncoding, uee);
        }
    }
}
