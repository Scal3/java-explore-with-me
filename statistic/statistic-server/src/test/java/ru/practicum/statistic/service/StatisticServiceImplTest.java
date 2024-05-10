package ru.practicum.statistic.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.dto.CreateStatisticDto;
import ru.practicum.dto.GetStatisticDto;
import ru.practicum.dto.StatisticDto;
import ru.practicum.exceptions.implementation.BadRequestException;
import ru.practicum.statistic.entity.AppEntity;
import ru.practicum.statistic.entity.IpEntity;
import ru.practicum.statistic.entity.Statistic;
import ru.practicum.statistic.entity.Uri;
import ru.practicum.statistic.repository.AppRepository;
import ru.practicum.statistic.repository.IpRepository;
import ru.practicum.statistic.repository.StatisticRepository;
import ru.practicum.statistic.repository.UriRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatisticServiceImplTest {

    @InjectMocks
    private StatisticServiceImpl statisticService;

    @Mock
    private StatisticRepository statisticRepositoryMock;

    @Mock
    private AppRepository appRepositoryMock;

    @Mock
    private IpRepository ipRepositoryMock;

    @Mock
    private UriRepository uriRepositoryMock;

    @Test
    void getStatistic_requestHasUrisListUniqueIsFalse_thenReturnStatisticDtoListWhereUriIsInUrisList() {
        GetStatisticDto dto = new GetStatisticDto(
                "2020-05-05 00:00:00",
                "2035-05-05 00:00:00",
                List.of("/kek"),
                false);

        Statistic statistic1 = Statistic.builder()
                .id(1)
                .uri(new Uri("/kek"))
                .app(new AppEntity("my-app"))
                .ip(new IpEntity("1.1.1.1"))
                .build();

        Statistic statistic2 = Statistic.builder()
                .id(2)
                .uri(new Uri("/kek"))
                .app(new AppEntity("my-app"))
                .ip(new IpEntity("1.1.1.1"))
                .build();

        List<Statistic> statistics = List.of(statistic1, statistic2);

        when(statisticRepositoryMock.findAllByUriInAndStartBeforeAndEndBefore(any(), any(), any()))
                .thenReturn(statistics);

        List<StatisticDto> statisticDtos = statisticService.getStatistic(dto);

        assertEquals(1, statisticDtos.size());
        assertEquals(2, statisticDtos.get(0).getHits());
        assertEquals("/kek", statisticDtos.get(0).getUri());
    }

    @Test
    void getStatistic_requestHasUrisListUniqueIsTrue_thenReturnStatisticDtoListWhichContainsOnlyUniqueIpObjectsWhereUriIsInUrisList() {
        GetStatisticDto dto = new GetStatisticDto(
                "2020-05-05 00:00:00",
                "2035-05-05 00:00:00",
                List.of("/kek"),
                true);

        Statistic statistic1 = Statistic.builder()
                .id(1)
                .uri(new Uri("/kek"))
                .app(new AppEntity("my-app"))
                .ip(new IpEntity("1.1.1.1"))
                .build();

        Statistic statistic2 = Statistic.builder()
                .id(2)
                .uri(new Uri("/kek"))
                .app(new AppEntity("my-app"))
                .ip(new IpEntity("1.1.1.1"))
                .build();

        List<Statistic> statistics = List.of(statistic1, statistic2);

        when(statisticRepositoryMock.findAllByUriInAndStartBeforeAndEndBefore(any(), any(), any()))
                .thenReturn(statistics);

        List<StatisticDto> statisticDtos = statisticService.getStatistic(dto);

        assertEquals(1, statisticDtos.size());
        assertEquals(1, statisticDtos.get(0).getHits());
        assertEquals("/kek", statisticDtos.get(0).getUri());
    }

    @Test
    void getStatistic_requestDoesNotHaveUrisListUniqueIsFalse_thenReturnStatisticDtoListWithAllObjects() {
        GetStatisticDto dto = new GetStatisticDto(
                "2020-05-05 00:00:00",
                "2035-05-05 00:00:00",
                null,
                false);

        Statistic statistic1 = Statistic.builder()
                .id(1)
                .uri(new Uri("/kek"))
                .app(new AppEntity("my-app"))
                .ip(new IpEntity("1.1.1.1"))
                .build();

        Statistic statistic2 = Statistic.builder()
                .id(2)
                .uri(new Uri("/kek"))
                .app(new AppEntity("my-app"))
                .ip(new IpEntity("1.1.1.1"))
                .build();

        List<Statistic> statistics = List.of(statistic1, statistic2);

        when(statisticRepositoryMock.findAllByStartBeforeAndEndBefore(any(), any()))
                .thenReturn(statistics);

        List<StatisticDto> statisticDtos = statisticService.getStatistic(dto);
        System.out.println(statisticDtos);

        assertEquals(1, statisticDtos.size());
        assertEquals(2, statisticDtos.get(0).getHits());
        assertEquals("my-app", statisticDtos.get(0).getApp());
    }

    @Test
    void getStatistic_requestDoesNotHaveUrisListUniqueIsTrue_thenReturnStatisticDtoListWhichContainsOnlyUniqueIpObjects() {
        GetStatisticDto dto = new GetStatisticDto(
                "2020-05-05 00:00:00",
                "2035-05-05 00:00:00",
                null,
                true);

        Statistic statistic1 = Statistic.builder()
                .id(1)
                .uri(new Uri("/kek"))
                .app(new AppEntity("my-app"))
                .ip(new IpEntity("1.1.1.1"))
                .build();

        Statistic statistic2 = Statistic.builder()
                .id(2)
                .uri(new Uri("/kek"))
                .app(new AppEntity("my-app"))
                .ip(new IpEntity("1.1.1.1"))
                .build();

        List<Statistic> statistics = List.of(statistic1, statistic2);

        when(statisticRepositoryMock.findAllByStartBeforeAndEndBefore(any(), any()))
                .thenReturn(statistics);

        List<StatisticDto> statisticDtos = statisticService.getStatistic(dto);

        assertEquals(1, statisticDtos.size());
        assertEquals(1, statisticDtos.get(0).getHits());
        assertEquals("my-app", statisticDtos.get(0).getApp());
    }

    @Test
    void getStatistic_wrongDateFormat_thenThrowBadRequest() {
        GetStatisticDto dto = new GetStatisticDto(
                "00:00:00",
                "2035-05-05 00:00:00",
                null,
                true);

        assertThrows(BadRequestException.class, () -> statisticService.getStatistic(dto));
    }

    @Test
    void getStatistic_startDateIsAfterEndDate_thenThrowBadRequest() {
        GetStatisticDto dto = new GetStatisticDto(
                "2040-05-05 00:00:00",
                "2035-05-05 00:00:00",
                null,
                true);

        assertThrows(BadRequestException.class, () -> statisticService.getStatistic(dto));
    }

    @Test
    void saveStatistic_thereIsNoAppLikeInDto_thenSaveNewAppAndStatistic() {
        CreateStatisticDto dto = new CreateStatisticDto(
                "my-app",
                "/app",
                "1.1.1.1",
                "2024-05-05 00:00:00");

        IpEntity ip = new IpEntity("1.1.1.1");
        Uri uri = new Uri("/app");
        AppEntity app = new AppEntity("my-app");

        when(appRepositoryMock.findOneByName(any()))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(app));
        when(uriRepositoryMock.findOneByName(any()))
                .thenReturn(Optional.of(uri));
        when(ipRepositoryMock.findOneByAddress(any()))
                .thenReturn(Optional.of(ip));

        statisticService.saveStatistic(dto);

        verify(appRepositoryMock, times(1))
                .save(any());
        verify(statisticRepositoryMock, times(1))
                .save(any());
    }

    @Test
    void saveStatistic_thereIsNoIpLikeInDto_thenSaveNewIpAndStatistic() {
        CreateStatisticDto dto = new CreateStatisticDto(
                "my-app",
                "/app",
                "1.1.1.1",
                "2024-05-05 00:00:00");

        IpEntity ip = new IpEntity("1.1.1.1");
        Uri uri = new Uri("/app");
        AppEntity app = new AppEntity("my-app");

        when(appRepositoryMock.findOneByName(any()))
                .thenReturn(Optional.of(app));
        when(uriRepositoryMock.findOneByName(any()))
                .thenReturn(Optional.of(uri));
        when(ipRepositoryMock.findOneByAddress(any()))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(ip));

        statisticService.saveStatistic(dto);

        verify(ipRepositoryMock, times(1))
                .save(any());
        verify(statisticRepositoryMock, times(1))
                .save(any());
    }

    @Test
    void saveStatistic_thereIsNoUriLikeInDto_thenSaveNewUriAndStatistic() {
        CreateStatisticDto dto = new CreateStatisticDto(
                "my-app",
                "/app",
                "1.1.1.1",
                "2024-05-05 00:00:00");

        IpEntity ip = new IpEntity("1.1.1.1");
        Uri uri = new Uri("/app");
        AppEntity app = new AppEntity("my-app");

        when(appRepositoryMock.findOneByName(any()))
                .thenReturn(Optional.of(app));
        when(uriRepositoryMock.findOneByName(any()))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(uri));
        when(ipRepositoryMock.findOneByAddress(any()))
                .thenReturn(Optional.of(ip));

        statisticService.saveStatistic(dto);

        verify(uriRepositoryMock, times(1))
                .save(any());
        verify(statisticRepositoryMock, times(1))
                .save(any());
    }

    @Test
    void saveStatistic_wrongDateFormat_thenThrowBadRequest() {
        CreateStatisticDto dto = new CreateStatisticDto(
                "my-app",
                "/app",
                "1.1.1.1",
                "00:00:00");

        assertThrows(BadRequestException.class, () -> statisticService.saveStatistic(dto));
    }

    @Test
    void saveStatistic_dateIsNotCorrect_thenThrowBadRequest() {
        CreateStatisticDto dto = new CreateStatisticDto(
                "my-app",
                "/app",
                "1.1.1.1",
                "2030-05-05 00:00:00");

        assertThrows(BadRequestException.class, () -> statisticService.saveStatistic(dto));
    }
}