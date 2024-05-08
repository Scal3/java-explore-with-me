package ru.practicum.statistic.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.statistic.entity.Statistic;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatisticRepository extends JpaRepository<Statistic, Long> {

    @EntityGraph(attributePaths = {"app", "uri"})
    @Query("SELECT s FROM Statistic s JOIN s.uri u WHERE u.name IN :uris AND s.timestamp > :start AND s.timestamp < :end")
    List<Statistic> findAllByUriInAndStartBeforeAndEndBefore(List<String> uris, LocalDateTime start, LocalDateTime end);

    @EntityGraph(attributePaths = {"app", "uri"})
    @Query("SELECT s FROM Statistic s WHERE s.timestamp > :start AND s.timestamp < :end")
    List<Statistic> findAllByStartBeforeAndEndBefore(LocalDateTime start, LocalDateTime end);
}