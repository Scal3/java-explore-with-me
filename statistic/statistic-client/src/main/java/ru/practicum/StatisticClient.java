package ru.practicum;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.dto.CreateStatisticDto;

import java.util.List;
import java.util.Map;

@Component
public class StatisticClient extends BaseClient {

    private static final String API_PREFIX_HIT = "/hit";

    private static final String API_PREFIX_STATS = "/stats";

    public StatisticClient(@Value("${ewm.server.url}") String server_url, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(server_url))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getStatistic(
            String start, String end, List<String> uris, boolean unique
    ) {
        String path = API_PREFIX_STATS + "?start={start}&end={end}&uris={uris}&unique={unique}";
        Map<String, Object> parameters = Map.of(
                "start", start,
                "end", end,
                "uris", uris,
                "unique", unique
        );

        return get(path, parameters);
    }

    public ResponseEntity<Object> saveStatistic(CreateStatisticDto requestDto) {
        return post(API_PREFIX_HIT, requestDto);
    }
}