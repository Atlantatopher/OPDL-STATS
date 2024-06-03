package com.opdl.api.stats.repository;

import com.opdl.api.stats.model.Bar;
import com.opdl.api.stats.model.Quality;
import com.opdl.api.stats.model.Schedule;
import com.opdl.api.stats.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {

    Optional<List<Team>> findBySeasonIdOrderById(long seasonId);

    Optional<Team> findByIdAndSeasonId(long id, long seasonId);

    Optional<Team> findByIdAndSeasonIdAndHomeBar(long id, long seasonId, Bar homeBar);

    Optional<List<Team>> findBySeasonIdAndHomeBarOrderById(long seasonId, Bar homeBar);
}
