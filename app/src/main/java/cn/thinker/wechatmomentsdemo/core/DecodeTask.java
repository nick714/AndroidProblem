package cn.thinker.wechatmomentsdemo.core;

import android.graphics.Bitmap;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.io.File;

import cn.thinker.wechatmomentsdemo.common.BitmapUtils;
import cn.thinker.wechatmomentsdemo.model.IDataOperation;
import cn.thinker.wechatmomentsdemo.model.cache.ICache;
import cn.thinker.wechatmomentsdemo.model.data.ImageItem;
import cn.thinker.wechatmomentsdemo.model.image.BitmapHolder;
import cn.thinker.wechatmomentsdemo.model.image.ImageItemInfoHelper;

public class DecodeTask implements ITask<BitmapHolder>, Response.Listener<BitmapHolder>, Response.ErrorListener {

    private TaskCallback<BitmapHolder> mCallback;
    private final BitmapHolder mHolder;
    private final ImageItem mImageItem;
    private ICache<String, Bitmap> mBitmapCache;

    public DecodeTask(BitmapHolder holder, TaskCallback<BitmapHolder> callback, ICache<String, Bitmap> cache) {
        mCallback = callback;
        mHolder = holder;
        mImageItem = mHolder.getParam().getData().getParcelable(IDataOperation.BUNDLE_KEY_PARAM_IMAGE_REQUEST_ITEM);
        mBitmapCache = cache;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
    }

    @Override
    public void onResponse(BitmapHolder response) {
    }

    @Override
    public boolean executeOnExecutors(IExecutors executors) {
        executors.executeOnLocal(this);
        return false;
    }

    @Override
    public Request<?> getVolleyRequest() {
        return null;
    }

    @Override
    public boolean perfromPretreat() {
        String path = ImageItemInfoHelper.getImageSavedPath(mImageItem);
        if (!new File(path).exists()) {
            return false;
        }
        Bitmap bitmap = BitmapUtils.createNewBitmapAndCompressByFile(path, new int[]{mImageItem.width, mImageItem.height}, true);
        if (bitmap != null) {
            mBitmapCache.put(mImageItem.id, bitmap);
            mHolder.setBitmap(bitmap);
            return true;
        } else {
            return false;
        }
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
