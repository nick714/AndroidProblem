package cn.thinker.wechatmomentsdemo.common;

public interface IPoolObject {
    /**
     * Initialization method. Called when an object is retrieved from the object
     * pool or has just been created.
     */
    void initializePoolObject();

    /**
     * Finalization method. Called when an object is stored in the object pool
     * to mark it as free.
     */
    void finalizePoolObject();
}
