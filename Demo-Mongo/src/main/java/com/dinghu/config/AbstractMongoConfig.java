package com.dinghu.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author 1.0
 * @author huding
 * @Date: 2024/05/23 15:02
 * @Description:
 */
public abstract class AbstractMongoConfig {

    private String url;

    @Autowired
    MongoProperties mongoProperties;

    // 简单配置
    /*public SimpleMongoClientDatabaseFactory getMongoDbFactory() {
        SimpleMongoClientDatabaseFactory factory = new SimpleMongoClientDatabaseFactory(url);
        return factory;
    }*/

    public SimpleMongoClientDatabaseFactory getMongoDbFactory() {
        // 连接字符串
        ConnectionString connectionString = new ConnectionString(url);

        // 连接池配置
        int minPoolSize = 2; // 最小连接池大小
        int maxPoolSize = 1500; // 最大连接池大小
        int maxConnectionIdleTime = 30000; // 连接的最长空闲时间（毫秒）

        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .applyToConnectionPoolSettings(builder -> builder
                        .minSize(minPoolSize)
                        .maxSize(maxPoolSize)
                        .maxConnectionIdleTime(maxConnectionIdleTime, TimeUnit.MILLISECONDS))
                .build();

        // 创建MongoClient实例
        MongoClient mongoClient = MongoClients.create(mongoClientSettings);

        // 创建SimpleMongoClientDatabaseFactory实例
        SimpleMongoClientDatabaseFactory factory = new SimpleMongoClientDatabaseFactory(mongoClient, connectionString.getDatabase());
        return factory;
    }

    public abstract MongoTemplate getMongoTemplate();

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
