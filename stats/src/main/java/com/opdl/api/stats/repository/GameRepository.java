package com.opdl.api.stats.repository;

import com.opdl.api.stats.model.Bar;
import com.opdl.api.stats.model.Game;
import com.opdl.api.stats.model.Match;
import com.opdl.api.stats.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Long> {
    Optional<List<Game>> findByMatchId(long matchId);

    Optional<Game> findByIdAndMatch(long id, Match match);

    Optional<List<Game>> findByMatch(Match match);

    Optional<List<Game>> findByHomePlayerOrHomePlayerDBLOrAwayPlayerOrAwayPlayerDBL(Player homePlayerId, Player homePlayerDBLId, Player awayPlayerId, Player awayPlayerDBLId);
}
