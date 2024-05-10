package ru.practicum.statistic.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.practicum.dto.CreateStatisticDto;
import ru.practicum.dto.StatisticDto;
import ru.practicum.statistic.service.StatisticService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StatisticController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class StatisticControllerTest {

    private final ObjectMapper mapper;

    private final MockMvc mvc;

    @MockBean
    private final StatisticService statisticServiceMock;

    @Test
    void getStatistic() throws Exception {
        List<StatisticDto> dtos = List.of(new StatisticDto("my-app", "/app", 1));

        when(statisticServiceMock.getStatistic(any()))
                .thenReturn(dtos);

        mvc.perform(get("/stats")
                    .param("start", "2020-05-05 00:00:00")
                    .param("end", "2023-05-05 00:00:00")
                    .param("unique", "false")
                    .characterEncoding(StandardCharsets.UTF_8)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].app").value(dtos.get(0).getApp()));
    }

    @Test
    void saveStatistic() throws Exception {
        CreateStatisticDto dto = new CreateStatisticDto(
                "my-app",
                "/app",
                "1.1.1.1",
                "2020-05-05 00:00:00");

        doNothing().when(statisticServiceMock).saveStatistic(any());

        mvc.perform(post("/hit")
                        .content(mapper.writeValueAsString(dto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }
}