package com.dinghu.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * @author 1.0
 * @author huding
 * @Date: 2024/05/23 15:04
 * @Description:
 */

@Configuration
@ConfigurationProperties(prefix = "spring.data.mongodb")
public class DemoMongoConfig extends AbstractMongoConfig {

    @Override
    @Bean(name = "DemoMongoTemplate")
    public MongoTemplate getMongoTemplate() {
        return new MongoTemplate(getMongoDbFactory());
    }

}
