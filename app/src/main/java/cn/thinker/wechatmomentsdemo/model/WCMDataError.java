package cn.thinker.wechatmomentsdemo.model;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

public class WCMDataError extends VolleyError {
    private Object mParam;

    public WCMDataError(NetworkResponse response, Object param) {
        super(response);
        mParam = param;
    }

    @SuppressWarnings("unused")
    public WCMDataError(String message, Object param) {
        super(message);
        mParam = param;
    }

    public Object getParam() {
        return mParam;
    }
}
