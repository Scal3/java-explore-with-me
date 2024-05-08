package ru.practicum.statistic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.statistic.entity.Ip;

import java.util.Optional;

@Repository
public interface IpRepository extends JpaRepository<Ip, Long> {

    Optional<Ip> findOneByAddress(String address);
}
