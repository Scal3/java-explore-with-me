package ru.practicum.statistic.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.statistic.entity.StatisticEntity;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatisticRepository extends JpaRepository<StatisticEntity, Long> {

    @EntityGraph(attributePaths = {"app", "uri"})
    @Query("SELECT s FROM StatisticEntity s JOIN s.uri u WHERE u.name IN :uris AND s.timestamp > :start AND s.timestamp < :end")
    List<StatisticEntity> findAllByUriInAndStartBeforeAndEndBefore(List<String> uris, LocalDateTime start, LocalDateTime end);

    @EntityGraph(attributePaths = {"app", "uri"})
    @Query("SELECT s FROM StatisticEntity s WHERE s.timestamp > :start AND s.timestamp < :end")
    List<StatisticEntity> findAllByStartBeforeAndEndBefore(LocalDateTime start, LocalDateTime end);
}