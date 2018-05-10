package cn.thinker.wechatmomentsdemo.model;

import android.os.Message;

import java.util.WeakHashMap;

import cn.thinker.wechatmomentsdemo.controller.IViewController;
import cn.thinker.wechatmomentsdemo.core.IExecutors;
import cn.thinker.wechatmomentsdemo.core.ITask;
import cn.thinker.wechatmomentsdemo.core.NoCacheTask;

public class DataModelHelper implements IDataOperation, ITask.TaskCallback<Message> {

    private IExecutors mExecutors;
    private WeakHashMap<Message, IViewController> mNotifyTargets;

    public DataModelHelper(IExecutors executors) {
        mExecutors = executors;
        mNotifyTargets = new WeakHashMap<>();
    }


    @Override
    public void requestDataOperation(IViewController target, Message msg) {
        mNotifyTargets.put(msg, target);
        ITask<Message> task = null;
        switch (msg.what) {
            case IDataOperation.REQUEST_TYPE_GET_USER_INFO:
            case IDataOperation.REQUEST_TYPE_GET_TWEETS_INFO:
                task = new NoCacheTask(msg, this);
                break;
            default:
                break;
        }
        if (task != null) {
            task.executeOnExecutors(mExecutors);
        }
    }

    @Override
    public void cancelDataOperation(IViewController target, Message msg) {
        //TODO
    }

    @Override
    public void onTaskTimeout(Message result) {
        //TODO
    }

    @Override
    public void onTaskFinish(Message result) {
        IViewController controller = mNotifyTargets.remove(result);
        if (controller != null) {
            controller.handleMessage(result);
        }
    }
}
