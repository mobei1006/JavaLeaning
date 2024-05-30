package com.dinghu.framework;

import java.util.Set;

/**
 * @author 1.0
 * @author huding
 * @Date: 2024/05/30 16:01
 * @Description: 管理接口
 */
public interface ICacheManager {

    <K, V> ICache<K, V> getCache(String key, Class<K> keyType, Class<V> valueType);

    void createCache(String key, CacheType cacheType);

    void destoryCache(String key);

    void destoryAllCache();

    Set<String> getAllCacheNames();

}
