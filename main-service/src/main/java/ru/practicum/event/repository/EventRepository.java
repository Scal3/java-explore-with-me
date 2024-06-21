package ru.practicum.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.event.entity.EventEntity;
import ru.practicum.event.enums.EventState;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long> {

    @EntityGraph(attributePaths = {"initiator", "category"})
    List<EventEntity> findByInitiatorIdOrderByViewsDesc(long initiatorId, Pageable pageable);

    Optional<EventEntity> findByIdAndInitiatorId(long eventId, long initiatorId);

    @EntityGraph(attributePaths = {"initiator", "category" , "location"})
    @Query("SELECT e FROM EventEntity e JOIN UserEntity u JOIN CategoryEntity c WHERE " +
            "(:users IS NULL OR u.id IN :users) AND " +
            "(:categories IS NULL OR c.id IN :categories) AND " +
            "(:states IS NULL OR e.state IN :states) AND " +
            "(:rangeStart IS NULL OR e.eventDate > :rangeStart) AND " +
            "(:rangeEnd IS NULL OR e.eventDate < :rangeEnd)")
    List<EventEntity> findByFilters(List<Long> users,
                                    List<Long> categories,
                                    List<EventState> states,
                                    LocalDateTime rangeStart,
                                    LocalDateTime rangeEnd,
                                    Pageable pageable);
}
