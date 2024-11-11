package com.opdl.api.stats.specification;

import com.opdl.api.stats.model.Player;
import com.opdl.api.stats.model.Quality;
import com.opdl.api.stats.model.Team;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class PlayerSpecifications {

    public static Specification<Player> hasPlayerWithQuality(long playerId) {
        Player player = new Player(playerId);
        return (root, query, criteriaBuilder) -> {
            Join<Quality, Player> PlayersQuality = root.join("quality");
            return criteriaBuilder.equal(PlayersQuality.get("player"), player);
        };
    }

    public static Specification<Player> belongsToTeam(long teamId) {
        Team team = new Team(teamId);
        return (root, query, cb) -> {
            Join<Team, Player> PlayersTeam = root.join("team");
            return cb.equal(PlayersTeam.get("id"), teamId);
        };
    }

    public static Specification<Player> isCaptain() {
        return (root, query, cb) -> {
            String role = "Captain";
            return cb.equal(root.get("playerType"), role);
        };
    }

    public static Specification<Player> isCoCaptain() {
        return (root, query, cb) -> {
            String role = "Co-Captain";
            return cb.equal(root.get("playerType"), role);
        };
    }

    public static Specification<Player> isSeason(long seasonId) {
        return (root, query, cb) -> {
            return cb.equal(root.get("seasonId"), seasonId);
        };
    }
}
