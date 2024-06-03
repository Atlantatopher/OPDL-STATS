package com.opdl.api.stats.service;

import com.opdl.api.stats.exception.ResourceNotFoundException;
import com.opdl.api.stats.model.Team;
import com.opdl.api.stats.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    public List<Team> getquery(Team teamQuery) throws ResourceNotFoundException{
        ArrayList<Team> results = new ArrayList<Team>();

        boolean hasSeasonId = teamQuery.getSeasonId() != 0;
        boolean hasTeamId = teamQuery.getId() != 0;
        boolean hasBarId = (teamQuery.getHomeBar() != null && teamQuery.getHomeBar().getId() != 0);

        if(hasSeasonId && hasBarId & hasTeamId){
            results.add(teamRepository.findByIdAndSeasonIdAndHomeBar(teamQuery.getId(), teamQuery.getSeasonId(), teamQuery.getHomeBar())
                    .orElseThrow(() -> new ResourceNotFoundException("Team not exist with id :" + teamQuery.getId()
                            + " and season id : " + teamQuery.getSeasonId()
                            + " and Bar ID id : " + teamQuery.getHomeBar().getId())));
        } else if(hasBarId && hasSeasonId){
            results.addAll(teamRepository.findBySeasonIdAndHomeBarOrderById(teamQuery.getSeasonId(), teamQuery.getHomeBar()).stream().findAny()
                    .orElseThrow(() -> new ResourceNotFoundException("Team not exist with bar id : " + teamQuery.getHomeBar().getId()
                            + " and season id : " + teamQuery.getSeasonId())));
        } else if(hasTeamId && hasSeasonId){
            results.add(teamRepository.findByIdAndSeasonId(teamQuery.getId(), teamQuery.getSeasonId())
                    .orElseThrow(() -> new ResourceNotFoundException("Team not exist with id : " + teamQuery.getHomeBar().getId()
                            + " and season id : " + teamQuery.getSeasonId())));
        } else if(hasSeasonId){
            results.addAll(teamRepository.findBySeasonIdOrderById(teamQuery.getSeasonId()).stream().findAny()
                    .orElseThrow(() -> new ResourceNotFoundException("Team not exist with season id : " + teamQuery.getSeasonId())));
        } else if(hasTeamId){
            results.add(teamRepository.findById(teamQuery.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Team not exist with  id : " + teamQuery.getId())));
        } else {
            // do we want to throw an error or return everything in teh DB. Not wanting to return everything in the DB.
        }
        return results;
    }
}
