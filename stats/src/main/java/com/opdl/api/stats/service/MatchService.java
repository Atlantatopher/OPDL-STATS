package com.opdl.api.stats.service;

import com.opdl.api.stats.exception.ResourceNotFoundException;
import com.opdl.api.stats.model.Game;
import com.opdl.api.stats.model.Match;
import com.opdl.api.stats.model.Team;
import com.opdl.api.stats.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class MatchService {

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private GameService gameService;

    public List<Match> getquery(Match matchQuery) throws ResourceNotFoundException{
        ArrayList<Match> results = new ArrayList<Match>();

        boolean hasSeasonId = matchQuery.getSeasonId() != 0;
        boolean hasMatchId = matchQuery.getId() != 0;
        boolean hasScheuleId = matchQuery.getScheduleId() != 0;

        if(hasMatchId){
            results.add(matchRepository.findById(matchQuery.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Match not exist with  id : " + matchQuery.getId())));
        }else if(hasScheuleId){
            results.addAll(matchRepository.findByScheduleId(matchQuery.getScheduleId())
                    .orElseThrow(() -> new ResourceNotFoundException("Match not exist with schedule id :" + matchQuery.getScheduleId())));
        } else if(hasSeasonId){
            results.addAll(matchRepository.findBySeasonId(matchQuery.getSeasonId())
                    .orElseThrow(() -> new ResourceNotFoundException("Match not exist with season id : " + matchQuery.getSeasonId())));
        } else {
            // do we want to throw an error or return everything in teh DB. Not wanting to return everything in the DB.
        }
        results = (ArrayList<Match>) results.stream().map(this::calculateScore).collect(Collectors.toList());
        return results;
    }

    public Match getHeadToHeadMatch(long seasonId, Team homeTeam, Team awayTeam){
        Match result = new Match();
        if(seasonId < 1 || homeTeam == null || awayTeam == null)
                return result;
        result = matchRepository.findBySeasonIdAndHomeTeamAndAwayTeam(seasonId, homeTeam, awayTeam).orElse(result);
        if(result.getId() > 0){
             calculateScore(result);
        }

        return result;
    }

    private Match calculateScore(Match match){
        Game gameQuery = new Game();
        AtomicInteger homeScore = new AtomicInteger();
        AtomicInteger awayScore = new AtomicInteger();
        gameQuery.setMatch(match);
        List<Game> results = gameService.getquery(gameQuery);
        results.forEach(game -> {
            homeScore.addAndGet(game.getHomeScore());
            awayScore.addAndGet(game.getAwayScore());
        });
        match.setHomeScore(homeScore.get());
        match.setAwayScore(awayScore.get());
        return match;
    }
}
