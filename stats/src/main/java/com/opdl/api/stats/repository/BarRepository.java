package com.opdl.api.stats.repository;

import com.opdl.api.stats.model.Bar;
import com.opdl.api.stats.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BarRepository extends JpaRepository<Bar, Long> {

    Optional<List<Bar>> findBySeasonId(long seasonId);

    Optional<Bar> findByIdAndSeasonId(long id, long seasonId);
}
