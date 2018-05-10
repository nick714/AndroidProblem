package cn.thinker.wechatmomentsdemo.core.request;

import android.os.Bundle;
import android.os.Message;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Map;

import cn.thinker.wechatmomentsdemo.common.ParcelablePoolObject;
import cn.thinker.wechatmomentsdemo.model.IDataOperation;
import cn.thinker.wechatmomentsdemo.model.data.TweetInfo;

public class TweetsRequest extends BasicJSONRequest<Message> {

    public TweetsRequest(String url, Response.Listener<Message> listener, Response.ErrorListener errorListener, Message param) {
        super(url, listener, errorListener, param);
    }

    @Override
    protected Message parseResult(String ob) {
        int result = IDataOperation.REQUEST_RESULT_SUCCESS;
        try {
            JSONArray arrays = new JSONArray(ob);
            int size = arrays.length();
            ArrayList<TweetInfo> tweets = new ArrayList<>(size);
            for (int i = 0; i < size; i ++) {
                TweetInfo tweet = new TweetInfo();
                tweet.fromJSONData(arrays.getJSONObject(i));
                // ignore tweets that error occurs.
                if (!tweet.isErrorOccurs()) {
                    tweets.add(tweet);
                }
            }
            Bundle param = ((ParcelablePoolObject) mParam.obj).getData();
            param.putParcelableArrayList(IDataOperation.BUNDLE_KEY_RESULT_TWEETS, tweets);
        } catch (JSONException e ) {
            e.printStackTrace();
            result = IDataOperation.REQUEST_RESULT_FAILED;
        }
        mParam.arg1 = result;
        return mParam;
    }

    @Override
    protected Map<String, String> getParamsFromBundle(Bundle param) {
        return null;
    }
}
