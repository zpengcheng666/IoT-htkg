package com.hss.modules.es7.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
//@Configuration
//@EnableElasticsearchRepositories(basePackages = "com.hss.modules.es7.repository")
@Slf4j
public class ElasticsearchConfig {
    @Value("${spring.elasticsearch.uris}")
    private String elasticsearchUrl;

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        try {
            String[] hostAndPort = elasticsearchUrl.replace("http://", "").split(":");
            String host = hostAndPort[0];
            int port = Integer.parseInt(hostAndPort[1]);

            RestClientBuilder builder = RestClient.builder(
                    new HttpHost(host, port, "http")
            );

            builder.setRequestConfigCallback(requestConfigBuilder -> {
                requestConfigBuilder.setConnectTimeout(5000);
                requestConfigBuilder.setSocketTimeout(60000);
                return requestConfigBuilder;
            });

            return new RestHighLevelClient(builder);
        } catch (Exception e) {
            log.error("Failed to create Elasticsearch client", e);
            throw new RuntimeException("Failed to create Elasticsearch client", e);
        }
    }

    @Bean
    public ElasticsearchRestTemplate elasticsearchRestTemplate() {
        return new ElasticsearchRestTemplate(restHighLevelClient());
    }
}
//
//@Configuration
//@Slf4j
//public class ElasticsearchConfig {
//
//    @Value("${spring.elasticsearch.uris}")
//    private String elasticsearchUrl;
//
//    @Bean
//    public RestHighLevelClient restHighLevelClient() {
//        try {
//            // 创建HTTP主机对象
//            String[] hostAndPort = elasticsearchUrl.replace("http://", "").split(":");
//            String host = hostAndPort[0];
//            int port = Integer.parseInt(hostAndPort[1]);
//
//            // 构建客户端配置
//            RestClientBuilder builder = RestClient.builder(
//                    new HttpHost(host, port, "http")
//            );
//
//            // 设置超时
//            builder.setRequestConfigCallback(requestConfigBuilder -> {
//                requestConfigBuilder.setConnectTimeout(5000);
//                requestConfigBuilder.setSocketTimeout(60000);
//                return requestConfigBuilder;
//            });
//
//            // 创建客户端
//            return new RestHighLevelClient(builder);
//
//        } catch (Exception e) {
//            log.error("Failed to create Elasticsearch client", e);
//            throw new RuntimeException("Failed to create Elasticsearch client", e);
//        }
//    }
//
//    @Bean
//    public ElasticsearchRestTemplate elasticsearchRestTemplate() {
//        return new ElasticsearchRestTemplate(restHighLevelClient());
//    }
//}
