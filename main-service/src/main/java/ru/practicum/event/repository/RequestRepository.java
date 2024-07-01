package ru.practicum.event.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.event.entity.RequestEntity;
import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<RequestEntity, Long> {

    @EntityGraph(attributePaths = {"requester", "event"})
    List<RequestEntity> findByRequesterIdOrderByCreatedDesc(long requesterId);

    Optional<RequestEntity> findByRequesterIdAndEventId(long requesterId, long eventId);
}
