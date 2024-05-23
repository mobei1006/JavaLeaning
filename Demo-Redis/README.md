### SpringBoot整合Redis
引入依赖
```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```
首先我们需要在 springboot 的配置文件中将 redis 的基本参数信息配置好
```properties
spring.redis.database = 0
spring.redis.host = host
spring.redis.port = port
spring.redis.timeout = 15000s
spring.redis.password = password
spring.redis.enablecluster=true
```
配置好基本的信息，我们可以通过配置来读取这些信息。我们定义 `RedisProperties` 类来读取这些信息
```java
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
```
然后我们 `RedisConfig` 中配置 redis 的连接
```java
@Configuration
@Slf4j
public class RedisConfig extends CachingConfigurerSupport {

    // 注入redis的配置
    @Resource
    private RedisProperties redisProperties;

    // 配置redis的序列化
    @Bean(name = "redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        // 配置redisTemplate
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        // 注入连接
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        //设置序列化
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper om = new ObjectMapper();
        // 指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 指定序列化输入的类型，类必须是非final修饰的，final修饰的类，比如String,Integer等会跑出异常
        //om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);

        RedisSerializer stringSerializer = new StringRedisSerializer();
        // key序列化
        redisTemplate.setKeySerializer(stringSerializer);
        // value序列化
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        // Hash key序列化
        redisTemplate.setHashKeySerializer(stringSerializer);
        // Hash value序列化
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    // 配置连接信息
    @Primary
    @Bean(name = "oneRedisConnectionFactory")
    public RedisConnectionFactory oneRedisConnectionFactory() {
        if (redisProperties.getEnablecluster()) {
            RedisClusterConfiguration config = new RedisClusterConfiguration();
            List<RedisNode> list = new ArrayList<>();
            Arrays.stream(redisProperties.getHost().split(",")).forEach(i -> {
                String[] urlArr = i.split(":");
                list.add(new RedisNode(urlArr[0], Integer.parseInt(urlArr[1])));
            });
            config.setClusterNodes(list);
            config.setPassword(redisProperties.getPassword());
            return new LettuceConnectionFactory(config);
        }

        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(redisProperties.getHost());
        config.setDatabase(redisProperties.getDatabase());
        config.setPort(redisProperties.getPort());
        config.setPassword(redisProperties.getPassword());
        return new LettuceConnectionFactory(config);
    }

    // 配置缓存信息
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);

        //解决查询缓存转换异常的问题
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);

        // 配置序列化（解决乱码的问题）
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ZERO)
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer))
                .disableCachingNullValues();

        return RedisCacheManager.builder(factory)
                .cacheDefaults(config)
                .build();
    }

}
```
