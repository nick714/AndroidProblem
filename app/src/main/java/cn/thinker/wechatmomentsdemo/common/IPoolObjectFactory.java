package cn.thinker.wechatmomentsdemo.common;

public interface IPoolObjectFactory {
    /**
     * Creates a new object for the object pool.
     *
     * @return new object instance for the object pool
     */
    IPoolObject createPoolObject();
}
