package com.opdl.api.stats.repository;

import com.opdl.api.stats.model.Match;
import com.opdl.api.stats.model.Player;
import com.opdl.api.stats.model.Quality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface QualityRepository extends JpaRepository<Quality, Long>, JpaSpecificationExecutor<Quality> {

    Optional<List<Quality>> findBySeasonId(long seasonId);

    Optional<List<Quality>> findBySeasonIdAndPlayer(long seasonId, Player player);

    Optional<List<Quality>> findByPlayer(Player player);

    Optional<List<Quality>> findByMatch(Match match);

    Optional<List<Quality>> findByMatchAndPlayer(Match match, Player player);
}
