package com.opdl.api.stats.specification;

import com.opdl.api.stats.model.Player;
import com.opdl.api.stats.model.Quality;
import com.opdl.api.stats.model.Team;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class QualitySpecifications {

    public static Specification<Quality> hasPlayerWithQuality(long playerId) {
        Player player = new Player(playerId);
        return (root, query, criteriaBuilder) -> {
            Join<Player, Quality> PlayersQuality = root.join("player");
            return criteriaBuilder.equal(PlayersQuality.get("id"), playerId);
        };
    }

    public static Specification<Quality> hasTeamWithQuality(long teamId) {
        return (root, query, criteriaBuilder) -> {
            Join<Player, Quality> PlayersQuality = root.join("player");
            Join<Team, Player> PlayersTeam = PlayersQuality.join("team");
            return criteriaBuilder.equal(PlayersTeam.get("id"), teamId);
        };
    }




}
