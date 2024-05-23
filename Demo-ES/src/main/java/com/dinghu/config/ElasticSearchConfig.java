package com.dinghu.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 1.0
 * @author huding
 * @Date: 2024/05/23 15:13
 * @Description:
 */

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
