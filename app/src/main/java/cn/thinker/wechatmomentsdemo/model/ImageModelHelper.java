package cn.thinker.wechatmomentsdemo.model;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import java.util.WeakHashMap;

import cn.thinker.wechatmomentsdemo.controller.IViewController;
import cn.thinker.wechatmomentsdemo.core.DecodeTask;
import cn.thinker.wechatmomentsdemo.core.IExecutors;
import cn.thinker.wechatmomentsdemo.core.ITask;
import cn.thinker.wechatmomentsdemo.core.ImageTask;
import cn.thinker.wechatmomentsdemo.model.cache.BitmapCache;
import cn.thinker.wechatmomentsdemo.model.cache.ICache;
import cn.thinker.wechatmomentsdemo.model.data.ImageItem;
import cn.thinker.wechatmomentsdemo.model.image.BitmapHolder;

public class ImageModelHelper extends Handler implements IDataOperation, ITask.TaskCallback<BitmapHolder> {

    private static final String TAG = "ImageModelHelper";

    private WeakHashMap<BitmapHolder, IViewController> mNotifyTargets = new WeakHashMap<>();
    private ICache<String, Bitmap> mCache;
    private IExecutors mExecutors;

    public ImageModelHelper(IExecutors executors) {
        mExecutors = executors;
        mCache = new BitmapCache();
    }


    @Override
    public void requestDataOperation(IViewController target, Message msg) {
        BitmapHolder holder = new BitmapHolder(msg, null);
        ITask<?> task;
        if (msg.what == IDataOperation.REQUEST_TYPE_DECODE_IMAGE_BITMAP) {
            task = new DecodeTask(holder, this, mCache);
        } else {
            task = new ImageTask(holder, this, mCache);
        }
        task.executeOnExecutors(mExecutors);
        mNotifyTargets.put(holder, target);
    }

    @Override
    public void cancelDataOperation(IViewController target, Message msg) {
        //TODO
    }

    @Override
    public void onTaskTimeout(BitmapHolder result) {
        //TODO
    }

    @Override
    public void onTaskFinish(BitmapHolder result) {
        if (result == null) {
            Log.e(TAG, "why we received a null BitmapHolder ??");
            return;
        }
        int resultCode = IDataOperation.REQUEST_RESULT_FAILED;
        if (result.getBitmap() != null) {
            resultCode = IDataOperation.REQUEST_RESULT_SUCCESS;
        }
        mNotifyTargets.get(result).handleMessage(
                Message.obtain(null, result.getMessageType(), resultCode, 0, result));
    }

    /*package*/ Bitmap getBitmap(ImageItem item) {
        if (TextUtils.isEmpty(item.url)) {
            return null;
        }
        return mCache.getCache(item.id);
    }
}
