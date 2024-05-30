package com.dinghu.framework;

/**
 * @author 1.0
 * @author huding
 * @Date: 2024/05/30 15:59
 * @Description: 缓存数据的管理与维护
 */
public interface ICacheHandler<K> {
    void removeIfExpired(K key);

    void clearAllExpiredCaches();
}
