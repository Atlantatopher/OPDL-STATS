package com.opdl.api.stats.repository;

import com.opdl.api.stats.model.Player;
import com.opdl.api.stats.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Long>, JpaSpecificationExecutor<Player> {

    Optional<List<Player>> findBySeasonId(long seasonId);

    Optional<Player> findByIdAndSeasonId(long id, long seasonId);

    Optional<List<Player>> findBySeasonIdAndTeam(long seasonId, Team team);

    Optional<Player> findByIdAndSeasonIdAndTeam(long seasonId, long playerId, Team team);
}
