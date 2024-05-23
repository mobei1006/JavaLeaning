package com.dinghu.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author 1.0
 * @author huding
 * @Date: 2024/05/23 15:01
 * @Description:
 */


@Data
@Component
@ConfigurationProperties(prefix = "spring.data.mongodb")
public class MongoProperties {

    private Integer maxConnectionIdleTime = 0;

    private Integer maxConnectionLifeTime = 0;

}
