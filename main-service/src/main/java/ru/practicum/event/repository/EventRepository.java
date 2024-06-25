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
    @EntityGraph(attributePaths = {"initiator", "category"})
    @Query("SELECT e FROM EventEntity e JOIN CategoryEntity c WHERE " +
            "(:text IS NULL OR LOWER(e.annotation) LIKE LOWER(CONCAT('%', :text, '%')) OR LOWER(e.description) LIKE LOWER(CONCAT('%', :text, '%'))) AND " +
            "(:categories IS NULL OR c.id IN :categories) AND " +
            "(:paid IS NULL OR e.paid = :paid) AND " +

            // TODO add when add request module
            "(:onlyAvailable IS NULL) AND " +

            "(:rangeStart IS NULL OR e.eventDate > :rangeStart) AND " +
            "(:rangeEnd IS NULL OR e.eventDate < :rangeEnd) AND " +
            "(:rangeStart IS NOT NULL OR e.eventDate > current_date) AND " +
            "e.state = PUBLISHED " +
            "ORDER BY e.eventDate DESC")
    List<EventEntity> findByFiltersOrderByEventDateDesc(String text,
                                                        List<Long> categories,
                                                        Boolean paid,
                                                        Boolean onlyAvailable,
                                                        LocalDateTime rangeStart,
                                                        LocalDateTime rangeEnd,
                                                        Pageable pageable);
    @EntityGraph(attributePaths = {"initiator", "category"})
    @Query("SELECT e FROM EventEntity e JOIN CategoryEntity c WHERE " +
            "(:text IS NULL OR LOWER(e.annotation) LIKE LOWER(CONCAT('%', :text, '%')) OR LOWER(e.description) LIKE LOWER(CONCAT('%', :text, '%'))) AND " +
            "(:categories IS NULL OR c.id IN :categories) AND " +
            "(:paid IS NULL OR e.paid = :paid) AND " +

            // TODO add when add request module
            "(:onlyAvailable IS NULL) AND " +

            "(:rangeStart IS NULL OR e.eventDate > :rangeStart) AND " +
            "(:rangeEnd IS NULL OR e.eventDate < :rangeEnd) AND " +
            "(:rangeStart IS NOT NULL OR e.eventDate > current_date) AND " +
            "e.state = PUBLISHED " +
            "ORDER BY e.views DESC")
    List<EventEntity> findByFiltersOrderByViewsDesc(String text,
                                                    List<Long> categories,
                                                    Boolean paid,
                                                    Boolean onlyAvailable,
                                                    LocalDateTime rangeStart,
                                                    LocalDateTime rangeEnd,
                                                    Pageable pageable);
    Optional<EventEntity> findByIdAndState(long eventId, EventState state);
}
