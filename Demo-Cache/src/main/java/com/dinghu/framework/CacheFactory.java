package com.dinghu.framework;

/**
 * @author 1.0
 * @author huding
 * @Date: 2024/05/30 16:03
 * @Description:
 */
public final class CacheFactory {

    public static ICache createCache(CacheType cacheType) {
        try {
            return cacheType.getClassType().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("failed to generate cache instance", e);
        }
    }

}
