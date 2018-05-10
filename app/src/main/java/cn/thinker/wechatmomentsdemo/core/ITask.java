package cn.thinker.wechatmomentsdemo.core;

import com.android.volley.Request;

public interface ITask<T> {

    interface TaskCallback<T> {
        void onTaskTimeout(T result);
        void onTaskFinish(T result);
    }

    boolean executeOnExecutors(IExecutors executors);

    Request<?> getVolleyRequest();

    boolean perfromPretreat();

    boolean isCanceled();

    void onPretreatResult(int result);
}
