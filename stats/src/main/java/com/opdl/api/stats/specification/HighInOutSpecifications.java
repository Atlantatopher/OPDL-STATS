package com.opdl.api.stats.specification;

import com.opdl.api.stats.model.HighInOut;
import com.opdl.api.stats.model.Player;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class HighInOutSpecifications {

    public static Specification<HighInOut> isCurrentSeason(long seasonId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("seasonId"),seasonId);
    }

    public static Specification<HighInOut> hasHighIn() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get("highIn"),0);
    }

    public static Specification<HighInOut> hasHighOut() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get("highOut"),0);
    }

    public static Specification<HighInOut> hasPlayerWithHighOrOut(long playerId) {
        Player player = new Player(playerId);
        return (root, query, criteriaBuilder) -> {
            Join<Player, HighInOut> PlayersHighOut = root.join("player");
            return criteriaBuilder.equal(PlayersHighOut.get("id"), playerId);
        };
    }

    public static Specification<HighInOut> getPlayersWithHighInForSeason(long seasonId) {
        return Specification.where(isCurrentSeason(seasonId))
                .and(hasHighIn());
    }

    public static Specification<HighInOut> getPlayersWithHighOutForSeason(long seasonId) {
        return Specification.where(isCurrentSeason(seasonId))
                .and(hasHighOut());
    }




}
