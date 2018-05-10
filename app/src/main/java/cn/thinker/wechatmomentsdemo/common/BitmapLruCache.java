package cn.thinker.wechatmomentsdemo.common;

import android.graphics.Bitmap;

public class BitmapLruCache<K> extends LruCache<K, Bitmap> {

    protected long mMaxByteSize = -1;
    protected long mByteSize = 0;

    /**
     * @param maxSize for caches that do not override {@link #sizeOf}, this is
     *                the maximum number of entries in the cache. For all other caches,
     *                this is the maximum sum of the sizes of the entries in this cache.
     */
    public BitmapLruCache(int maxSize, long maxByteSize) {
        super(maxSize);
        mByteSize = 0;
        mMaxByteSize = maxByteSize;
    }

    /**
     * Caches {@code value} for {@code key}. The value is moved to the head of
     * the queue.
     *
     * @return the previous value mapped by {@code key}.
     */
    @Override
    public Bitmap put(K key, Bitmap value) {
        if (key == null || value == null) {
            throw new NullPointerException("key == null || value == null");
        }

        //only calculate consumed memory size when mMaxByteSize is set.
        if (mMaxByteSize > 0) {
            long bytes = ((long) value.getRowBytes()) * value.getHeight();
            //in case we are recycle a bitmap that is now using
            while (bytes + mByteSize > mMaxByteSize && size() >= 3)
                trimToSize(size() - 1);
            mByteSize += bytes;
        }
        return super.put(key, value);
    }
}
