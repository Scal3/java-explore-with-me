package ru.practicum.statistic.service;

import ru.practicum.dto.CreateStatisticDto;
import ru.practicum.dto.GetStatisticDto;
import ru.practicum.dto.StatisticDto;

import java.util.List;

public interface StatisticService {

    List<StatisticDto> getStatistic(GetStatisticDto dto);

    void saveStatistic(CreateStatisticDto dto);
}
