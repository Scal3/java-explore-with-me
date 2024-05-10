package ru.practicum.statistic.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.CreateStatisticDto;
import ru.practicum.dto.GetStatisticDto;
import ru.practicum.dto.StatisticDto;
import ru.practicum.exceptions.implementation.BadRequestException;
import ru.practicum.statistic.entity.AppEntity;
import ru.practicum.statistic.entity.IpEntity;
import ru.practicum.statistic.entity.Statistic;
import ru.practicum.statistic.entity.UriEntity;
import ru.practicum.statistic.repository.AppRepository;
import ru.practicum.statistic.repository.IpRepository;
import ru.practicum.statistic.repository.StatisticRepository;
import ru.practicum.statistic.repository.UriRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {

    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final StatisticRepository statisticRepository;

    private final AppRepository appRepository;

    private final UriRepository uriRepository;

    private final IpRepository ipRepository;

    @Transactional(readOnly = true)
    @Override
    public List<StatisticDto> getStatistic(GetStatisticDto dto) {
        log.info("Entering getStatistic method: GetStatisticDto = {}", dto);
        List<Statistic> statistics;
        LocalDateTime start;
        LocalDateTime end;

        try {
            start = LocalDateTime.parse(dto.getStart(), formatter);
            end = LocalDateTime.parse(dto.getEnd(), formatter);
        } catch (DateTimeParseException e) {
            throw new BadRequestException("Wrong date format");
        }

        if (!isDateCorrect(start, end)) {
            throw new BadRequestException("Date is not correct");
        }

        if (dto.getUris() != null) {
            statistics = statisticRepository
                    .findAllByUriInAndStartBeforeAndEndBefore(dto.getUris(), start, end);
        } else {
            statistics = statisticRepository
                    .findAllByStartBeforeAndEndBefore(start, end);
        }

        if (dto.isUnique()) {
            statistics = statistics.stream()
                    .collect(Collectors.groupingBy(Statistic::getIp))
                    .values()
                    .stream()
                    .map(statisticsWithSameIp -> statisticsWithSameIp.get(0))
                    .collect(Collectors.toList());
        }

        log.info("Exiting getStatistic method");

        return mapToStatisticDtoList(statistics).stream()
                .sorted((d1, d2) -> d2.getHits() - d1.getHits())
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void saveStatistic(CreateStatisticDto dto) {
        log.info("Entering saveStatistic method: CreateStatisticDto = {}", dto);

        LocalDateTime timestamp;

        try {
            timestamp = LocalDateTime.parse(dto.getTimestamp(), formatter);
        } catch (DateTimeParseException e) {
            throw new BadRequestException("Wrong date format");
        }

        if (!isDateCorrect(timestamp)) {
            throw new BadRequestException("Date is not correct");
        }

        if (appRepository.findOneByName(dto.getApp()).isEmpty()) {
            appRepository.save(new AppEntity(dto.getApp()));
        }

        if (uriRepository.findOneByName(dto.getUri()).isEmpty()) {
            uriRepository.save(new UriEntity(dto.getUri()));
        }

        if (ipRepository.findOneByAddress(dto.getIp()).isEmpty()) {
            ipRepository.save(new IpEntity(dto.getIp()));
        }

        AppEntity app = appRepository.findOneByName(dto.getApp()).get();
        UriEntity uri = uriRepository.findOneByName(dto.getUri()).get();
        IpEntity ip = ipRepository.findOneByAddress(dto.getIp()).get();

        statisticRepository.save(new Statistic(app, uri, ip, timestamp));
        log.info("Exiting saveStatistic method");
    }

    private List<StatisticDto> mapToStatisticDtoList(List<Statistic> statistic) {
        Map<String, StatisticDto> statisticDtoMap = new HashMap<>();

        for (Statistic s : statistic) {
            if (statisticDtoMap.containsKey(s.getUri().getName())) {
                StatisticDto statisticDto = statisticDtoMap.get(s.getUri().getName());
                statisticDto.setHits(statisticDto.getHits() + 1);
            } else {
                StatisticDto newStatisticDto =
                        new StatisticDto(s.getApp().getName(), s.getUri().getName(), 1);
                statisticDtoMap.put(s.getUri().getName(), newStatisticDto);
            }
        }

        return new ArrayList<>(statisticDtoMap.values());
    }

    private boolean isDateCorrect(LocalDateTime timestamp) {
        return timestamp != null && !timestamp.isAfter(LocalDateTime.now());
    }

    private boolean isDateCorrect(LocalDateTime start, LocalDateTime end) {
        return start != null && end != null && !start.isAfter(end);
    }
}
