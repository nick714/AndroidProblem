package cn.thinker.wechatmomentsdemo.core;

import android.os.Message;

import com.android.volley.Request;

import cn.thinker.wechatmomentsdemo.common.Constants;
import cn.thinker.wechatmomentsdemo.core.request.TweetsRequest;
import cn.thinker.wechatmomentsdemo.core.request.UserInfoRequest;
import cn.thinker.wechatmomentsdemo.model.IDataOperation;

public class NoCacheTask extends BaseDataTask {

    public NoCacheTask(Message data, TaskCallback<Message> callback) {
        super(data, callback);
    }

    @Override
    public boolean perfromPretreat() {
        return false;
    }

    @Override
    public Request<?> getVolleyRequest() {
        Request<?> request = null;
        switch (mData.what) {
            case IDataOperation.REQUEST_TYPE_GET_USER_INFO:
                request = new UserInfoRequest(Constants.USER_URL, this, this, mData);
                break;
            case IDataOperation.REQUEST_TYPE_GET_TWEETS_INFO:
                request = new TweetsRequest(Constants.TWEETS_URL, this, this, mData);
                break;
        }
        return request;
    }

    @Override
    public boolean executeOnExecutors(IExecutors executors) {
        executors.executeOnVolley(this);
        return false;
    }
}
