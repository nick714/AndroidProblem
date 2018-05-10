package cn.thinker.wechatmomentsdemo.common;

public class ParcelablePoolObjectFactory implements IPoolObjectFactory {
    @Override
    public ParcelablePoolObject createPoolObject() {
        return new ParcelablePoolObject();
    }
}
