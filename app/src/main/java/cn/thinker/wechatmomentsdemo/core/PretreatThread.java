package cn.thinker.wechatmomentsdemo.core;

import java.util.concurrent.LinkedBlockingDeque;

import cn.thinker.wechatmomentsdemo.model.IDataOperation;

public class PretreatThread extends Thread {

    public interface PretreatCallback {
        void onPretreatComplete(ITask<?> task, int result);
    }

    private LinkedBlockingDeque<ITask<?>> mPretreatQueue;

    private volatile boolean isTerminated = false;

    private PretreatCallback mCallback;

    public PretreatThread(LinkedBlockingDeque<ITask<?>> queue,
                          PretreatCallback callback) {
        mPretreatQueue = queue;
        mCallback = callback;
    }

    public void run() {
        while (!isTerminated) {
            int result = IDataOperation.REQUEST_RESULT_FAILED;
            ITask<?> task;
            try {
                task = mPretreatQueue.take();
            } catch (InterruptedException e) {
                continue;
            }
            if (task != null) {
                if (task.perfromPretreat()) {
                    result = IDataOperation.REQUEST_RESULT_SUCCESS;
                }
                if (mCallback != null) {
                    mCallback.onPretreatComplete(task, result);
                }
            }
        }
    }

    @SuppressWarnings("unused")
    public void terminate() {
        isTerminated = true;
        interrupt();
    }
}
