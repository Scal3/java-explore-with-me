package ru.practicum.event.entity;

import lombok.*;
import ru.practicum.event.enums.RequestState;
import ru.practicum.user.entity.UserEntity;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "requests")
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", nullable = false)
    private UserEntity requester;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private EventEntity event;

    @Column(nullable = false)
    private RequestState status;

    @Column(nullable = false)
    private LocalDateTime created;

    @Override
    public int hashCode() {
        return Objects.hash(id, status, created);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;

        if (o == null || o.getClass() != this.getClass()) return false;

        RequestEntity r = (RequestEntity) o;

        return Objects.equals(id, r.id) &&
                Objects.equals(status, r.status) &&
                Objects.equals(created, r.created);
    }
}