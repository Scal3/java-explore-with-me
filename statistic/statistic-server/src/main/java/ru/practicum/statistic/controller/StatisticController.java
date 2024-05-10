package ru.practicum.statistic.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CreateStatisticDto;
import ru.practicum.dto.GetStatisticDto;
import ru.practicum.dto.StatisticDto;
import ru.practicum.statistic.service.StatisticService;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
public class StatisticController {

    private final StatisticService statisticService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/stats")
    public List<StatisticDto> getStatistic(
            @RequestParam @NotBlank String start,
            @RequestParam @NotBlank String end,
            @RequestParam(required = false) List<String> uris,
            @RequestParam(required = false, defaultValue = "false") boolean unique
    ) {
        log.info("Entering getStatistic method: start = {}, end = {}, uris = {}, unique = {}",
                start, end, uris, unique);

        List<StatisticDto> dtos =
                statisticService.getStatistic(new GetStatisticDto(start, end, uris, unique));
        log.info("Exiting getStatistic method");

        return dtos;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/hit")
    public void saveStatistic(@RequestBody @Valid CreateStatisticDto dto) {
        log.info("Entering saveStatistic method: CreateStatisticDto = {}", dto);
        statisticService.saveStatistic(dto);
        log.info("Exiting saveStatistic method");
    }
}
