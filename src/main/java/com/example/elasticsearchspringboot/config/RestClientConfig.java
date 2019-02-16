package com.example.elasticsearchspringboot.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestClientConfig {
    @Bean
    public RestClient getRestClient() {
        return RestClient.builder(new HttpHost("192.168.40.4", 9200, "http"),
                                  new HttpHost("192.168.40.5", 9200, "http"))
                         .build();
    }
}
