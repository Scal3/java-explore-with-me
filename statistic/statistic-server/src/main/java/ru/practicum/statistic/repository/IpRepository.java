package ru.practicum.statistic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.statistic.entity.IpEntity;

import java.util.Optional;

@Repository
public interface IpRepository extends JpaRepository<IpEntity, Long> {

    Optional<IpEntity> findOneByAddress(String address);
}
