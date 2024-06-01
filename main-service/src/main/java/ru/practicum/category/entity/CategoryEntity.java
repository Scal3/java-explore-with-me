package ru.practicum.category.entity;

import lombok.*;
import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "categories")
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;

        if (o == null || o.getClass() != this.getClass()) return false;

        CategoryEntity c = (CategoryEntity) o;

        return Objects.equals(id, c.id) && Objects.equals(name, c.name);
    }
}
