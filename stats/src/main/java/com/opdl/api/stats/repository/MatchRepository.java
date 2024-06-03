package com.opdl.api.stats.repository;

import com.opdl.api.stats.model.Match;
import com.opdl.api.stats.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MatchRepository extends JpaRepository<Match, Long> {

    Optional<Match> findByScheduleIdAndHomeTeamOrAwayTeam(long scheduleId, Team homeTeam, Team awayTeam);

    Optional<List<Match>> findBySeasonIdAndHomeTeamOrAwayTeam(long seasonId, Team homeTeam, Team awayTeam);

    Optional<List<Match>> findBySeasonId(long seasonId);

    Optional<List<Match>> findByScheduleId(long scheduleId);

}
