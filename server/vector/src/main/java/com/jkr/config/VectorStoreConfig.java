package com.jkr.config;

import jakarta.annotation.Resource;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.ai.vectorstore.redis.RedisVectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import redis.clients.jedis.JedisPooled;

@Configuration
public class VectorStoreConfig {

    @Resource
    @Qualifier("ollamaEmbeddingModel")
    private EmbeddingModel ollamaEmbeddingModel;

    @Resource
    private JedisPooled jedisClient;

    @Bean("redisVectorStore")
    public VectorStore redisVectorStore() {
        return RedisVectorStore.builder(jedisClient, ollamaEmbeddingModel)
                .indexName("my-redis-index")
                .build();
    }

    @Bean("pgVectorStore")
    public VectorStore pgVectorStore(JdbcTemplate jdbcTemplate) {
        return PgVectorStore.builder(jdbcTemplate, ollamaEmbeddingModel).build();
    }
}