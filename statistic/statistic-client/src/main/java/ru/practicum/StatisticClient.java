package ru.practicum;

import org.springframework.web.client.RestTemplate;

public class StatisticClient {

    private final RestTemplate restTemplate;

    public StatisticClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}