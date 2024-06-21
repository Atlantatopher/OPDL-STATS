package com.opdl.api.stats.repository;

import com.opdl.api.stats.model.Bar;
import com.opdl.api.stats.model.Team;
import com.opdl.api.stats.specification.TeamSpecifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long>, JpaSpecificationExecutor<TeamSpecifications> {

    Optional<List<Team>> findBySeasonIdOrderById(long seasonId);

    Optional<Team> findByIdAndSeasonId(long id, long seasonId);

    Optional<Team> findByIdAndSeasonIdAndHomeBar(long id, long seasonId, Bar homeBar);

    Optional<List<Team>> findBySeasonIdAndHomeBarOrderById(long seasonId, Bar homeBar);
}
