package com.jkr.config;

import cn.hutool.crypto.SecureUtil;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import java.nio.charset.Charset;
import java.util.List;

@Configuration
public class InitVectorDatabaseConfig {

    @Resource
    @Qualifier("pgVectorStore")
    private VectorStore pgVectorStore;


    @Resource
    @Qualifier("redisVectorStore")
    private VectorStore redisVectorStore;


    private final RedisTemplate<String, String> redisTemplate;

    public InitVectorDatabaseConfig(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Value("classpath:/knowledge-base/ops.txt")
    private org.springframework.core.io.Resource opsFile;

    @PostConstruct
    public void init() {

        TextReader textReader = new TextReader(opsFile);
        textReader.setCharset(Charset.defaultCharset());
        List<Document> list = new TokenTextSplitter().transform(textReader.read());

        String sourceMetadata = (String) textReader.getCustomMetadata().get("source");
        String textHash = SecureUtil.md5(sourceMetadata);

        // 为 PostgreSQL 和 Redis 分别设置防重复键
        String pgRedisKey = "vector-pg:" + textHash;
        String redisRedisKey = "vector-redis:" + textHash;

        // PostgreSQL 防重复检查
        Boolean pgFlag = redisTemplate.opsForValue().setIfAbsent(pgRedisKey, "1");
        if (Boolean.TRUE.equals(pgFlag)) {
            pgVectorStore.add(list);
            System.out.println("PostgreSQL 向量数据初始化完成");
        } else {
            System.out.println("PostgreSQL 向量数据已存在，跳过初始化");
        }

        // Redis 防重复检查
        Boolean redisFlag = redisTemplate.opsForValue().setIfAbsent(redisRedisKey, "1");
        if (Boolean.TRUE.equals(redisFlag)) {
            redisVectorStore.add(list);
            System.out.println("Redis 向量数据初始化完成");
        } else {
            System.out.println("Redis 向量数据已存在，跳过初始化");
        }
    }

}
