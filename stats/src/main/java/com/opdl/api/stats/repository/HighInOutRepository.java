package com.opdl.api.stats.repository;

import com.opdl.api.stats.model.HighInOut;
import com.opdl.api.stats.model.Match;
import com.opdl.api.stats.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface HighInOutRepository extends JpaRepository<HighInOut, Long>, JpaSpecificationExecutor<HighInOut> {

    Optional<List<HighInOut>> findBySeasonId(long seasonId);

    Optional<List<HighInOut>> findBySeasonIdAndPlayer(long seasonId, Player player);

    Optional<List<HighInOut>> findByPlayer(Player player);

    Optional<List<HighInOut>> findByMatch(Match match);

    Optional<List<HighInOut>> findByMatchAndPlayer(Match match, Player player);
}
