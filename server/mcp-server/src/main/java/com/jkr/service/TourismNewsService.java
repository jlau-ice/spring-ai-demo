package com.jkr.service;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 城市旅游新闻服务：为AI Agent提供今日城市旅游头条信息。
 */
@Service
public class TourismNewsService {

    /**
     * 根据城市名称获取今日热门旅游头条新闻或活动信息。
     * * @param city 城市名称，如“北京”、“上海”、“深圳”
     *
     * @return 城市今日旅游头条新闻摘要
     */
    @Tool(description = "根据城市名称获取今日热门旅游头条新闻或活动信息")
    public String getCityTourismNews(String city) {
        // 使用 Map 模拟数据库或外部 API 返回的今日头条数据
        Map<String, String> newsMap = Map.of(
                "北京", "今日头条：故宫博物院启动秋季“清代宫廷生活艺术”特展，限量门票现已开放线上预约。",
                "上海", "今日头条：外滩举办国际艺术周，多家知名美术馆延长开放时间，黄浦江游船票预订火爆。",
                "深圳", "今日头条：欢乐谷主题公园推出五折特惠活动，庆祝深圳特区成立周年，吸引大量家庭出游。"
        );

        return newsMap.getOrDefault(city, "抱歉：未查询到该城市今日旅游热门新闻！");
    }
}
