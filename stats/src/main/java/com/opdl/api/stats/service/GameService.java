package com.opdl.api.stats.service;

import com.opdl.api.stats.exception.ResourceNotFoundException;
import com.opdl.api.stats.model.Game;
import com.opdl.api.stats.model.Player;
import com.opdl.api.stats.repository.GameRepository;
import com.opdl.api.stats.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    public List<Game> getquery(Game gameQuery) throws ResourceNotFoundException{
        ArrayList<Game> results = new ArrayList<Game>();

        boolean hasGameId = gameQuery.getId() != 0;
        boolean hasMatchId = (gameQuery.getMatch() != null);

        if(hasGameId){
            results.add(gameRepository.findById(gameQuery.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Game not exist with id :" + gameQuery.getId())));
        } else if(hasMatchId){
            results.addAll(gameRepository.findByMatch(gameQuery.getMatch()).stream().findAny()
                    .orElseThrow(() -> new ResourceNotFoundException("Game not exist with Match id : " + gameQuery.getMatch().getId())));
       } else {
            // do we want to throw an error or return everything in teh DB. Not wanting to return everything in the DB.
        }
        return results;
    }

    public List<Game> getGamesByPlayer(long playerId) throws ResourceNotFoundException{
        ArrayList<Game> results = new ArrayList<Game>();
        Player playerQuery = new Player(playerId);
        if(playerId > 0){
            results.addAll(gameRepository.findByHomePlayerOrHomePlayerDBLOrAwayPlayerOrAwayPlayerDBL(playerQuery, playerQuery, playerQuery, playerQuery).stream().findAny()
                    .orElseThrow(() -> new ResourceNotFoundException("Game not exist with Player id : " + playerId)));
        }
        return results;

    }
}
