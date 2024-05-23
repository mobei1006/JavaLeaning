package com.dinghu.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author 1.0
 * @author huding
 * @Date: 2024/05/23 14:36
 * @Description:
 */

@Data
@Component
@ConfigurationProperties(prefix = "spring.redis")
public class RedisProperties {
    private Integer database;

    private String host;

    private Integer port;

    private String timeout;

    private String password;

    private Boolean enablecluster;

}
