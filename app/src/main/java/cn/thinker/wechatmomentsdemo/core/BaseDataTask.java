package cn.thinker.wechatmomentsdemo.core;

import android.content.res.Resources;
import android.os.Message;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import cn.thinker.wechatmomentsdemo.R;
import cn.thinker.wechatmomentsdemo.StaticApplication;
import cn.thinker.wechatmomentsdemo.common.ParcelablePoolObject;
import cn.thinker.wechatmomentsdemo.model.IDataOperation;

public abstract class BaseDataTask implements ITask<Message>, Response.Listener<Message>, Response.ErrorListener {

    protected Message mData;
    protected TaskCallback<Message> mCallback;

    public BaseDataTask(Message data, TaskCallback<Message> callback) {
        mData = data;
        mCallback = callback;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        mData.arg1 = IDataOperation.REQUEST_RESULT_FAILED;
        final Resources res = StaticApplication.peekInstance().getResources();
        if (error instanceof NetworkError || error instanceof ParseError
                || error instanceof ServerError
                || error instanceof AuthFailureError) {
            ((ParcelablePoolObject) mData.obj).getData().putString(
                    IDataOperation.BUNDLE_KEY_RESULT_MESSAGE, res.getString(R.string.network_error));
        } else if (error instanceof TimeoutError) {
            ((ParcelablePoolObject) mData.obj).getData().putString(
                    IDataOperation.BUNDLE_KEY_RESULT_MESSAGE, res.getString(R.string.network_timeout_error));
        } else {
            ((ParcelablePoolObject) mData.obj).getData().putString(
                    IDataOperation.BUNDLE_KEY_RESULT_MESSAGE,
                    error.getMessage());
        }
        mCallback.onTaskFinish(mData);
    }

    public abstract boolean perfromPretreat();

    public abstract Request<?> getVolleyRequest();

    public abstract boolean executeOnExecutors(IExecutors executors);

    @Override
    public boolean isCanceled() {
        return false;
    }

    @Override
    public void onPretreatResult(int result) {
        mData.arg1 = result;
        mCallback.onTaskFinish(mData);
    }

    @Override
    public void onResponse(Message response) {
        mCallback.onTaskFinish(response);
    }
}
