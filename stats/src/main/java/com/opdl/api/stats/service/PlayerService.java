package com.opdl.api.stats.service;

import com.opdl.api.stats.exception.ResourceNotFoundException;
import com.opdl.api.stats.model.Player;
import com.opdl.api.stats.repository.PlayerRepository;
import com.opdl.api.stats.specification.PlayerSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    public List<Player> getquery(Player playerQuery) throws ResourceNotFoundException{
        ArrayList<Player> results = new ArrayList<Player>();

        boolean hasSeasonId = playerQuery.getSeasonId() != 0;
        boolean hasPlayerId = playerQuery.getId() != 0;
        boolean hasTeamId = (playerQuery.getTeam() != null && playerQuery.getTeam().getId() != 0);

        if(hasSeasonId && hasTeamId & hasPlayerId){
            results.add(playerRepository.findByIdAndSeasonIdAndTeam(playerQuery.getSeasonId(), playerQuery.getId(), playerQuery.getTeam())
                    .orElseThrow(() -> new ResourceNotFoundException("Player not exist with id :" + playerQuery.getId()
                            + " and season id : " + playerQuery.getSeasonId()
                            + " and Bar ID id : " + playerQuery.getTeam().getId())));
        } else if(hasTeamId && hasSeasonId){
            results.addAll(playerRepository.findBySeasonIdAndTeam(playerQuery.getSeasonId(), playerQuery.getTeam()).stream().findAny()
                    .orElseThrow(() -> new ResourceNotFoundException("Player not exist with bar id : " + playerQuery.getTeam().getId()
                            + " and season id : " + playerQuery.getSeasonId())));
        } else if(hasPlayerId && hasSeasonId){
            results.add(playerRepository.findByIdAndSeasonId(playerQuery.getId(), playerQuery.getSeasonId())
                    .orElseThrow(() -> new ResourceNotFoundException("Player not exist with id : " + playerQuery.getTeam().getId()
                            + " and season id : " + playerQuery.getSeasonId())));
        } else if(hasSeasonId){
            results.addAll(playerRepository.findBySeasonId(playerQuery.getSeasonId()).stream().findAny()
                    .orElseThrow(() -> new ResourceNotFoundException("Player not exist with season id : " + playerQuery.getSeasonId())));
        } else if(hasPlayerId){
            results.add(playerRepository.findById(playerQuery.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Player not exist with  id : " + playerQuery.getId())));
        } else {
            // do we want to throw an error or return everything in teh DB. Not wanting to return everything in the DB.
        }
        return results;
    }

    public List<Player> getPlayerWithQuality(long playerId) throws ResourceNotFoundException{
        List<Player> results = new ArrayList<>();
        Specification<Player> spec = PlayerSpecifications.hasPlayerWithQuality(playerId);
        results = playerRepository.findAll(spec);
        return results;
    }

    public List<Player> getPlayersForTeam(long  teamId) throws ResourceNotFoundException{
        Specification<Player> spec = PlayerSpecifications.belongsToTeam(teamId);
        return new ArrayList<>(playerRepository.findAll(spec));
    }

    public List<Player> getCaptains(long seasonId) throws ResourceNotFoundException{
        Specification<Player> spec = PlayerSpecifications.isSeason(seasonId).and(PlayerSpecifications.isCaptain().or(PlayerSpecifications.isCoCaptain()));
        List<Player> results = new ArrayList<>(playerRepository.findAll(spec));
        results.sort(Comparator.comparing((Player p) -> p.getTeam().getHomeBar().getId())
                .thenComparing(p -> p.getTeam().getTeamName())
                .thenComparing(Player::getPlayerType));

        return results;
    }

}
