package cn.thinker.wechatmomentsdemo.core;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import cn.thinker.wechatmomentsdemo.core.request.ExtendImageRequest;
import cn.thinker.wechatmomentsdemo.model.IDataOperation;
import cn.thinker.wechatmomentsdemo.model.cache.ICache;
import cn.thinker.wechatmomentsdemo.model.data.ImageItem;
import cn.thinker.wechatmomentsdemo.model.image.BitmapHolder;

public class ImageTask implements ITask<BitmapHolder>, Response.Listener<BitmapHolder>, Response.ErrorListener {

    private ICache<String, Bitmap> mBitmapCache;
    private TaskCallback<BitmapHolder> mCallback;
    private final BitmapHolder mHolder;
    private final ImageItem mImageItem;

    public ImageTask(BitmapHolder holder, TaskCallback<BitmapHolder> callback,
                     ICache<String, Bitmap> cache) {
        mBitmapCache = cache;
        mCallback = callback;
        mHolder = holder;
        mImageItem = mHolder.getParam().getData().getParcelable(IDataOperation.BUNDLE_KEY_PARAM_IMAGE_REQUEST_ITEM);
    }

    @Override
    public boolean perfromPretreat() {
        return false;
    }

    @Override
    public Request<?> getVolleyRequest() {
        return new ExtendImageRequest(mImageItem.url, this, mHolder, Config.ARGB_8888, this);
    }

    @Override
    public boolean executeOnExecutors(IExecutors executors) {
        executors.executeOnVolley(this);
        return false;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        mHolder.getParam().getData().putString(IDataOperation.BUNDLE_KEY_RESULT_MESSAGE, error.getMessage());
        mCallback.onTaskFinish(mHolder);
    }

    @Override
    public void onResponse(BitmapHolder response) {
        mBitmapCache.put(mImageItem.id, response.getBitmap());
        mCallback.onTaskFinish(response);
    }

    @Override
    public boolean isCanceled() {
        return false;
    }

    @Override
    public void onPretreatResult(int result) {
        mCallback.onTaskFinish(mHolder);
    }
}
