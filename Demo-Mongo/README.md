### SpringBoot整合Mongo
引入依赖
```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-data-mongodb</artifactId>
</dependency>
```
首先我们在 springboot 配置文件中配置 mongo 的连接信息
```java
spring.data.mongodb.url=mongodb://username:password==@127.0.0.1:port/demo?authSource=admin&authMechanism=SCRAM-SHA-1
```
定义 MongoProperties 加载 mongo 的配置信息
```java
@Data
@Component
@ConfigurationProperties(prefix = "spring.data.mongodb")
public class MongoProperties {

    private Integer maxConnectionIdleTime = 0;

    private Integer maxConnectionLifeTime = 0;

}
```
定义抽象的配置连接类，用于多个数据源的使用，AbstractMongoConfig 
```java
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

```
继承抽象类配置专用的数据源
```java
@Configuration
@ConfigurationProperties(prefix = "spring.data.mongodb")
public class DemoMongoConfig extends AbstractMongoConfig {

    @Override
    @Bean(name = "dataApiMongoTemplate")
    public MongoTemplate getMongoTemplate() {
        return new MongoTemplate(getMongoDbFactory());
    }
}
```
