package com.jkr.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPooled;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Value("${spring.data.redis.database:0}")
    private int redisDatabase;

    @Bean
    public JedisPooled jedisPooled() {
        String url = String.format("redis://%s:%d/%d",
                redisHost, redisPort, redisDatabase);
        return new JedisPooled(url);
    }
}


