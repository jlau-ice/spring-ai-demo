package com.jkr.config;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SaaLLMConfig {

    /**
     * 方式1:${}
     * 持有yml文件配置：spring.ai.dashscope.api-key=${DASH_SCOPE_API_KEY}
     */
    //@Value("${spring.ai.dashscope.api-key}")
    //private String apiKey;
    //
    //@Bean
    //public DashScopeApi dashScopeApi() {
    //    return DashScopeApi.builder().apiKey(apiKey).build();
    //}

    /**
     * 方式2:System.getenv("环境变量")
     * 持有yml文件配置：spring.ai.dashscope.api-key=${DASH_SCOPE_API_KEY}
     */
    @Bean
    public DashScopeApi dashScopeApi() {
        return DashScopeApi.builder()
                .apiKey(System.getenv("DASH_SCOPE_API_KEY"))
                .build();
    }

}
