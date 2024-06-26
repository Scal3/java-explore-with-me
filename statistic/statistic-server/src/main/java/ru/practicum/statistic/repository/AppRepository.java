package ru.practicum.statistic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.statistic.entity.AppEntity;

import java.util.Optional;

@Repository
public interface AppRepository extends JpaRepository<AppEntity, Long> {

    Optional<AppEntity> findOneByName(String name);
}
