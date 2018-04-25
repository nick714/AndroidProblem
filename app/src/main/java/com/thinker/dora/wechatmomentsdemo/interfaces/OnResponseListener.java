package com.thinker.dora.wechatmomentsdemo.interfaces;


import com.thinker.dora.wechatmomentsdemo.model.TweetsItemBean;

import java.util.ArrayList;
import java.util.List;


public interface OnResponseListener {

	public void onSuccess(ArrayList<TweetsItemBean> bean);

	public void onFailed();
}
