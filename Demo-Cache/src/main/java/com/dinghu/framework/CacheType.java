package com.dinghu.framework;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 1.0
 * @author huding
 * @Date: 2024/05/30 16:06
 * @Description:
 */

@Getter
@AllArgsConstructor
public enum CacheType {
    DEFAULT(DefaultCache.class),
    LRU(LruCache.class);
    private Class<? extends ICache> classType;
}
