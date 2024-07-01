package ru.practicum.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.event.entity.RequestEntity;

@Repository
public interface RequestRepository extends JpaRepository<RequestEntity, Long> {
}
