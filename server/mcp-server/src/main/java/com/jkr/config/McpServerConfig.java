package com.jkr.config;

import com.jkr.service.TourismNewsService;
import com.jkr.service.WeatherService;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class McpServerConfig {


    public final WeatherService weatherService;
    public final TourismNewsService tourismNewsService;


    public McpServerConfig(WeatherService weatherService,
                           TourismNewsService tourismNewsService) {
        this.weatherService = weatherService;
        this.tourismNewsService = tourismNewsService;
    }

    /**
     * 将工具方法暴露给外部 mcp client 调用
     *
     * @return ToolCallbackProvider
     */
    @Bean
    public ToolCallbackProvider weatherTools() {
        return MethodToolCallbackProvider.builder()
                .toolObjects(weatherService, tourismNewsService)
                // .toolObjects(tourismNewsService)
                .build();
    }
}

