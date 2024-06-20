package ru.practicum.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.event.entity.EventEntity;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long> {

    @EntityGraph(attributePaths = {"initiator", "category"})
    List<EventEntity> findByInitiatorIdOrderByViewsDesc(long initiatorId, Pageable pageable);

    Optional<EventEntity> findByIdAndInitiatorId(long eventId, long initiatorId);
}
