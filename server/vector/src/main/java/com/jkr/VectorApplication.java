package com.jkr;

import org.springframework.ai.vectorstore.pgvector.autoconfigure.PgVectorStoreAutoConfiguration;
import org.springframework.ai.vectorstore.redis.autoconfigure.RedisVectorStoreAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {
        RedisVectorStoreAutoConfiguration.class,
        PgVectorStoreAutoConfiguration.class
})
public class VectorApplication {

    public static void main(String[] args) {
        SpringApplication.run(VectorApplication.class, args);
    }
}