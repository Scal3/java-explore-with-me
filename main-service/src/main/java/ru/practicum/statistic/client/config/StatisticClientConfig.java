package ru.practicum.statistic.client.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.stats.client.StatisticClient;

@Configuration
public class StatisticClientConfig {

    @Value("${stats-server.url}")
    private String url;

    @Bean
    public StatisticClient statsClient(RestTemplateBuilder builder) {
        return new StatisticClient(url, builder);
    }
}
