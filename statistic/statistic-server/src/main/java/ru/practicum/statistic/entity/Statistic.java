package ru.practicum.statistic.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "statistics")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Statistic {

    public Statistic(AppEntity app, UriEntity uri, IpEntity ip, LocalDateTime timestamp) {
        this.app = app;
        this.uri = uri;
        this.ip = ip;
        this.timestamp = timestamp;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_id", nullable = false)
    private AppEntity app;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uri_id", nullable = false)
    private UriEntity uri;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ip_id", nullable = false)
    private IpEntity ip;

    @Column(name = "created", nullable = false)
    private LocalDateTime timestamp;
}
