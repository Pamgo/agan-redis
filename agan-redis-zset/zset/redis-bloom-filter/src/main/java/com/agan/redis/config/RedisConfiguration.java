package com.agan.redis.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.rebloom.client.Client;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.exceptions.JedisDataException;


@Configuration
@Slf4j
public class RedisConfiguration {

    // 从application.yml文件中引入配置信息
    @Value("${redis.bloom.url:#{null}}")
    private String rebloomUrl;
    @Value("${redis.bloom.port:#{null}}")
    private Integer rebloomPort;
    @Value("${redis.bloom.init-capacity:#{null}}")
    private Integer rebloomInitCapacity;
    @Value("${redis.bloom.error-rate:#{null}}")
    private Double rebloomErrorRate;

    // 导出bean
    @Bean()
    public Client rebloomClient() {
        Client client = new Client(rebloomUrl, rebloomPort);
        // 创建一个新的bloom过滤器
        try{
            //BF.RESERVE {key} {error_rate} {capacity}
            client.createFilter(Constants.REBLOOM, rebloomInitCapacity, rebloomErrorRate);
        }catch (JedisDataException e){
            log.warn("已存在{}",e.getMessage());
        }
        return client;
    }
}