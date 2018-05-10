package cn.thinker.wechatmomentsdemo.core;

import android.os.Handler;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;

import java.util.concurrent.LinkedBlockingDeque;

import cn.thinker.wechatmomentsdemo.model.IDataOperation;

public class DataExecutors implements IExecutors, PretreatThread.PretreatCallback {

    public static class ResponseRunnable implements Runnable {

        private ITask<?> mTask;

        private int mResult;

        public ResponseRunnable(ITask<?> task, int result) {
            mTask = task;
            mResult = result;
        }

        @Override
        public void run() {
            mTask.onPretreatResult(mResult);
        }
    }

    private RequestQueue mNetworkQueue;
    private PretreatThread mPretreatThread;
    private LinkedBlockingDeque<ITask<?>> mPretreatQueue = new LinkedBlockingDeque<>();
    private Handler mResponseHandler;

    public DataExecutors(RequestQueue queue) {
        mNetworkQueue = queue;
        mResponseHandler = new Handler();
    }

    @Override
    public boolean executeOnLocal(ITask<?> task) {
        if (mPretreatThread == null || !mPretreatThread.isAlive()) {
            mPretreatThread = new PretreatThread(mPretreatQueue, this);
            mPretreatThread.start();
        }
        mPretreatQueue.addFirst(task);
        return false;
    }

    @Override
    public boolean executeOnVolley(ITask<?> task) {
        Request<?> request = task.getVolleyRequest();
        if (request != null) {
            request.setRetryPolicy(new DefaultRetryPolicy(5 * 1000, 2, 1));
            mNetworkQueue.add(request);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean execute(ITask<?> task) {
        return false;
    }

    @Override
    public void onPretreatComplete(ITask<?> task, int result) {
        if (result == IDataOperation.REQUEST_RESULT_SUCCESS
                || result == IDataOperation.REQUEST_RESULT_CANCEL) {
            mResponseHandler.post(new ResponseRunnable(task, result));
        } else if (result == IDataOperation.REQUEST_RESULT_FAILED) {
            if (!executeOnVolley(task)) {
                mResponseHandler.post(new ResponseRunnable(task, IDataOperation.REQUEST_RESULT_FAILED));
            }
        }
    }
}
