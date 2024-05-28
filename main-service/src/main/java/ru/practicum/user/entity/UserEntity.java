package ru.practicum.user.entity;

import lombok.*;
import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "users")
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String name;

    @Override
    public int hashCode() {
        return Objects.hash(id, email, name);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;

        if (o == null || o.getClass() != this.getClass()) return false;

        UserEntity u = (UserEntity) o;

        return id.equals(u.id) && email.equals(u.email) && name.equals(u.name);
    }
}
