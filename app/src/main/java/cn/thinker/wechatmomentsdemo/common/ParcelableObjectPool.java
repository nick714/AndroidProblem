package cn.thinker.wechatmomentsdemo.common;

public class ParcelableObjectPool {
    protected final int MAX_PREE_OBJECT_INDEX;

    protected ParcelablePoolObjectFactory mFactory;
    protected ParcelablePoolObject[] mFreeObjects;

    protected int mFreeObjectIndex = -1;

    public ParcelableObjectPool(ParcelablePoolObjectFactory factory, int maxSize) {
        MAX_PREE_OBJECT_INDEX = maxSize - 1;
        mFreeObjects = new ParcelablePoolObject[maxSize];
        mFactory = factory;
    }

    public synchronized ParcelablePoolObject newObject() {
        ParcelablePoolObject obj;
        if (mFreeObjectIndex == -1) {
            obj = mFactory.createPoolObject();
        } else {
            obj = mFreeObjects[mFreeObjectIndex];
            mFreeObjectIndex--;
        }
        obj.initializePoolObject();
        return obj;
    }

    public synchronized void freeObject(ParcelablePoolObject obj) {
        if (obj != null) {
            obj.finalizePoolObject();
            if (mFreeObjectIndex < MAX_PREE_OBJECT_INDEX) {
                mFreeObjectIndex ++;
                mFreeObjects[mFreeObjectIndex] = obj;
            }
        }
    }
}
