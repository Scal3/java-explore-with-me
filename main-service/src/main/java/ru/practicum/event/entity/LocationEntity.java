package ru.practicum.event.entity;

import lombok.*;
import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "events")
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LocationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double lat;

    @Column(nullable = false)
    private Double lon;

    @Override
    public int hashCode() {
        return Objects.hash(id, lat, lon);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;

        if (o == null || o.getClass() != this.getClass()) return false;

        LocationEntity l = (LocationEntity) o;

        return Objects.equals(id, l.id) && Objects.equals(lat, l.lat) && Objects.equals(lon, l.lon);
    }
}
