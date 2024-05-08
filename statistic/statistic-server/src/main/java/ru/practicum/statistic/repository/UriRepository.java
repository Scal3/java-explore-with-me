package ru.practicum.statistic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.statistic.entity.Uri;

import java.util.Optional;

@Repository
public interface UriRepository extends JpaRepository<Uri, Long> {

    Optional<Uri> findOneByName(String name);
}
