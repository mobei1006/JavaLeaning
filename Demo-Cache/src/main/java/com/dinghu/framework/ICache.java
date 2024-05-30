package com.dinghu.framework;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author 1.0
 * @author huding
 * @Date: 2024/05/30 15:57
 * @Description: 本地缓存的API接口
 */
public interface ICache<K, V> {
    Optional<V> get(K key);

    void put(K key, V value);

    void put(K key, V value, int timeIntvl, TimeUnit timeUnit);

    Optional<V> remove(K key);

    boolean containsKey(K key);

    void clear();

    Optional<Map<K, V>> getAll(Set<K> keys);

    void putAll(Map<K, V> map);

    void putAll(Map<K, V> map, int timeIntvl, TimeUnit timeUnit);

    boolean putIfAbsent(K key, V value);

    boolean putIfPresent(K key, V value);

    void expireAfter(K key, int timeIntvl, TimeUnit timeUnit);
}
