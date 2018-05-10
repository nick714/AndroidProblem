package cn.thinker.wechatmomentsdemo.model.cache;

@SuppressWarnings("unused")
public interface ICache<K, V> {
    V getCache(K key);
    void put(K key, V cache);
    void remove(K key);
    void limitCache(int cacheSize);
    void clearCache();
}
