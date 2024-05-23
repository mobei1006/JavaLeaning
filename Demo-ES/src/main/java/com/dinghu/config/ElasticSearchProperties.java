package com.dinghu.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author 1.0
 * @author huding
 * @Date: 2024/05/23 15:13
 * @Description:
 */
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

    private String userName;

    private String password;


}
