### Elasticsearch 集成配置
引入依赖
```xml
<dependency>
  <groupId>org.elasticsearch.client</groupId>
  <artifactId>elasticsearch-rest-high-level-client</artifactId>
  <version>7.15.2</version>
</dependency>
```
首先我们在 springboot 配置文件中配置elasticsearch 的连接信息
```properties
elasticsearch.schema=http
elasticsearch.address=localhost:port
elasticsearch.connectTimeout=10000
elasticsearch.socketTimeout=60000
elasticsearch.connectionRequestTimeout=10000
elasticsearch.maxConnectNum=200
elasticsearch.maxConnectPerRoute=200
elasticsearch.indexName=demo-
elasticsearch.username=username
elasticsearch.password=password

```
我们在`ElasticSearchProperties` 中读取这些配置信息
```java
@Component
@Data
@ConfigurationProperties(prefix = "elasticsearch")
public class ElasticSearchProperties {

    /**
     * 协议
     */
    private String schema;

    /**
     * 集群地址，如果有多个用“,”隔开
     */
    private String address;


    /**
     * 连接超时时间
     */
    private int connectTimeout;

    /**
     * Socket 连接超时时间
     */
    private int socketTimeout;

    /**
     * 获取连接的超时时间
     */
    private int connectionRequestTimeout;

    /**
     * 最大连接数
     */
    private int maxConnectNum;

    /**
     * 最大路由连接数
     */
    private int maxConnectPerRoute;

    /**
     * 用户名
     */
    private String userName;


    /**
     * 密码
     */
    private String password;
}
```
我们在`ElasticSearchConfig`中配置好连接信息
```java
@Configuration
@Slf4j
public class ElasticSearchConfig {

    @Resource
    private ElasticSearchProperties properties;
    private RestHighLevelClient restHighLevelClient;

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();

        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(properties.getUserName(),
                properties.getPassword()));
        // 拆分地址
        List<HttpHost> hostLists = new ArrayList<>();
        String[] hostList = properties.getAddress().split(",");
        for (String addr : hostList) {
            String host = addr.split(":")[0];
            String port = addr.split(":")[1];
            hostLists.add(new HttpHost(host, Integer.parseInt(port), properties.getSchema()));
        }
        // 转换成 HttpHost 数组
        HttpHost[] httpHost = hostLists.toArray(new HttpHost[]{});
        // 构建连接对象
        RestClientBuilder builder = RestClient.builder(httpHost);
        // 异步连接延时配置
        builder.setRequestConfigCallback(requestConfigBuilder -> {
            requestConfigBuilder.setConnectTimeout(properties.getConnectTimeout());
            requestConfigBuilder.setSocketTimeout(properties.getSocketTimeout());
            requestConfigBuilder.setConnectionRequestTimeout(properties.getConnectionRequestTimeout());
            return requestConfigBuilder;
        });
        // 异步连接数配置
        builder.setHttpClientConfigCallback(httpClientBuilder -> {
            httpClientBuilder.setMaxConnTotal(properties.getMaxConnectNum());
            httpClientBuilder.setMaxConnPerRoute(properties.getMaxConnectPerRoute());
            httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            return httpClientBuilder;
        });
        restHighLevelClient = new RestHighLevelClient(builder);
        return restHighLevelClient;
    }

    @PreDestroy
    public void clientClose() {
        try {
            this.restHighLevelClient.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```
