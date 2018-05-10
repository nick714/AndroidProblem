package cn.thinker.wechatmomentsdemo.core.request;

import android.os.Bundle;
import android.os.Message;

import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.thinker.wechatmomentsdemo.common.ParcelablePoolObject;
import cn.thinker.wechatmomentsdemo.model.IDataOperation;
import cn.thinker.wechatmomentsdemo.model.data.UserInfo;

public class UserInfoRequest extends  BasicJSONRequest<Message> {

    public UserInfoRequest(String url, Response.Listener<Message> listener, Response.ErrorListener errorListener, Message param) {
        super(url, listener, errorListener, param);
    }

    @Override
    protected Message parseResult(String ob) {
        int result = IDataOperation.REQUEST_RESULT_SUCCESS;
        UserInfo user = new UserInfo();
        try {
            user.fromJSONData(new JSONObject(ob));
            Bundle param = ((ParcelablePoolObject) mParam.obj).getData();
            param.putParcelable(IDataOperation.BUNDLE_KEY_RESULT_USER, user);
        } catch (JSONException e ) {
            e.printStackTrace();
            result = IDataOperation.REQUEST_RESULT_FAILED;
        }
        mParam.arg1 = result;
        return mParam;
    }

    @Override
    protected Map<String, String> getParamsFromBundle(Bundle param) {
        // used for post request to fill in parameters.
        return null;
    }
}
