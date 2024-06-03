package com.opdl.api.stats.repository;

import com.opdl.api.stats.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    Optional<List<Schedule>> findBySeasonIdOrderById(long seasonId);

    Optional<List<Schedule>> findByWeekIdOrderById(long weekId);

    Optional<List<Schedule>> findBySeasonIdAndWeekIdLessThanEqualOrderById(long seasonId, long weekId);

    Optional<Schedule> findBySeasonIdAndWeekIdOrderById(long seasonId, long weekId);
}
