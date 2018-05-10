package cn.thinker.wechatmomentsdemo.model;

import android.os.Message;

import cn.thinker.wechatmomentsdemo.controller.IViewController;

public interface IDataOperation {

    //request code
    int REQUEST_RESULT_SUCCESS = 0;
    int REQUEST_RESULT_FAILED = -1;
    @SuppressWarnings("unused")
    int REQUEST_RESULT_NO_DATA = -2;
    @SuppressWarnings("unused")
    int REQUEST_RESULT_TIMEOUT = -3;
    int REQUEST_RESULT_CANCEL = -4;

    //request parameter key
    String BUNDLE_KEY_PARAM_IMAGE_REQUEST_ITEM = "cgw.image.req.item";
    String BUNDLE_KEY_RESULT_MESSAGE = "cgw.result.msg";

    //request result key
    String BUNDLE_KEY_RESULT_USER = "cgw.result.user";
    String BUNDLE_KEY_RESULT_TWEETS = "cgw.result.tweets";

    //request type
    int REQUEST_TYPE_GET_USER_INFO = 0x1001;
    int REQUEST_TYPE_GET_TWEETS_INFO = 0x1002;
    int REQUEST_TYPE_GET_IMAGE_BITMAP = 0x10FE;
    int REQUEST_TYPE_DECODE_IMAGE_BITMAP = 0x10FF;

    void requestDataOperation(IViewController target, Message msg);
    void cancelDataOperation(IViewController target, Message msg);
}
