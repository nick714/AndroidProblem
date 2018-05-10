package cn.thinker.wechatmomentsdemo.model.cache;

import android.graphics.Bitmap;
import android.util.Log;

import cn.thinker.wechatmomentsdemo.common.BitmapLruCache;

public class BitmapCache implements ICache<String, Bitmap> {

    private static final int DEFAULT_BITMAP_CACHE_COUNT = 200;
    private static final String TAG = "BitmapCache";

    private BitmapLruCache<String> mInternalCache;

    public BitmapCache() {
        Runtime rt = Runtime.getRuntime();
        long maxMemory = rt.maxMemory();
        Log.i(TAG, "set maxMemory to:" + Long.toString(maxMemory));
        mInternalCache = new BitmapLruCache<>(DEFAULT_BITMAP_CACHE_COUNT, maxMemory / 10);
    }

    @Override
    public Bitmap getCache(String key) {
        return mInternalCache.get(key);
    }

    @Override
    public void put(String key, Bitmap cache) {
        mInternalCache.put(key, cache);
    }

    @Override
    public void remove(String key) {
        mInternalCache.remove(key);
    }

    @Override
    public void limitCache(int cacheSize) {
        mInternalCache.trimToSize(cacheSize);
    }

    @Override
    public void clearCache() {
        mInternalCache.evictAll();
    }

}
