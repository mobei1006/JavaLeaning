package com.dinghu.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author 1.0
 * @author huding
 * @Date: 2024/05/23 18:22
 * @Description:
 */

@Configuration
public class CacheConfig {

    @Bean
    public Cache<String, Object> caffeineCache() {
        return Caffeine.newBuilder()
                // 最后一次写入或者访问后多久时间过期
                .expireAfterWrite(60, TimeUnit.SECONDS)
                // 初始化缓存的空间大小
                .initialCapacity(20)
                // 缓存的最大数量
                .maximumSize(100)
                .build();
    }

}
