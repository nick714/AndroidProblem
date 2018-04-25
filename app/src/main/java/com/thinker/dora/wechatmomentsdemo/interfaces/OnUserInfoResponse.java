package com.thinker.dora.wechatmomentsdemo.interfaces;

import com.thinker.dora.wechatmomentsdemo.model.TweetsItemBean;
import com.thinker.dora.wechatmomentsdemo.model.UserBean;

import java.util.ArrayList;


public interface OnUserInfoResponse {

    public void onSuccess(UserBean bean);

    public void onFailed();
}
