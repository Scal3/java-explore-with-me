package ru.practicum.event.entity;

import lombok.*;
import ru.practicum.category.entity.CategoryEntity;
import ru.practicum.event.enums.EventState;
import ru.practicum.user.entity.UserEntity;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "events")
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2000)
    private String annotation;

    @Column(nullable = false)
    private LocalDateTime createdOn;

    @Column(nullable = false, length = 7000)
    private String description;

    @Column(nullable = false)
    private LocalDateTime eventDate;

    @Column(nullable = false)
    private Boolean paid;

    @Column(nullable = false)
    private Integer participantLimit;

    @Column
    private LocalDateTime publishedOn;

    @Column(nullable = false)
    private Boolean requestModeration;

    @Column(nullable = false)
    private EventState state;

    @Column(nullable = false,  length = 120)
    private String title;

    @Column(nullable = false)
    private Integer views;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator_id", nullable = false)
    private UserEntity initiator;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private LocationEntity location;

    @Override
    public int hashCode() {
        return Objects.hash(
                id, annotation, createdOn,
                description, eventDate, paid,
                participantLimit, publishedOn, requestModeration,
                state, title, views);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;

        if (o == null || o.getClass() != this.getClass()) return false;

        EventEntity e = (EventEntity) o;

        return Objects.equals(id, e.id) &&
                Objects.equals(annotation, e.annotation) &&
                Objects.equals(createdOn, e.createdOn) &&
                Objects.equals(description, e.description) &&
                Objects.equals(eventDate, e.eventDate) &&
                Objects.equals(paid, e.paid) &&
                Objects.equals(participantLimit, e.participantLimit) &&
                Objects.equals(publishedOn, e.publishedOn) &&
                Objects.equals(requestModeration, e.requestModeration) &&
                Objects.equals(state, e.state) &&
                Objects.equals(title, e.title) &&
                Objects.equals(views, e.views);
    }
}
