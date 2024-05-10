package ru.practicum.statistic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.statistic.entity.UriEntity;

import java.util.Optional;

@Repository
public interface UriRepository extends JpaRepository<UriEntity, Long> {

    Optional<UriEntity> findOneByName(String name);
}
