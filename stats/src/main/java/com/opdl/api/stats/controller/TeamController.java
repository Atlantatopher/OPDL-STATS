package com.opdl.api.stats.controller;

import com.opdl.api.stats.exception.ResourceNotFoundException;
import com.opdl.api.stats.model.Bar;
import com.opdl.api.stats.model.Player;
import com.opdl.api.stats.model.Team;
import com.opdl.api.stats.repository.TeamRepository;
import com.opdl.api.stats.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/")
public class TeamController {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamService teamService;

    // get all Teams
    @GetMapping("/teams")
    public List<Team> getAllTeams(){
        return teamRepository.findAll();
    }

    @GetMapping("/teams/{seasonId}")
    public List<Team> getAllTeamsBySeason(@PathVariable Long seasonId){
        return teamRepository.findBySeasonIdOrderById(seasonId).orElse(new ArrayList<Team>());
    }

    @PostMapping("/registerTeam")
    public Team createTeam(@RequestBody Team team) {
        return teamRepository.save(team);
    }

    @PostMapping("/registerTeams")
    public List<Team> createTeam(@RequestBody ArrayList<Team> teams) {
        return teamRepository.saveAll(teams);
    }


    // get specified Bar
    @PostMapping("/team")
    public ResponseEntity<List<Team>> getTeamByDetails(@RequestBody Team team){
        List<Team> results =  new ArrayList<Team>();

        try {
            results = teamService.getquery(team);
        } catch (ResourceNotFoundException e) {
            System.out.println(e.getMessage());
        }

        return (!results.isEmpty())? ResponseEntity.ok(results): ResponseEntity.badRequest().build();
    }

    // get Team by id rest api
    @GetMapping("/team/{id}")
    public ResponseEntity<Team> getTeamById(@PathVariable Long id) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team not exist with id :" + id));
        return ResponseEntity.ok(team);
    }

    @PutMapping("/team/{id}")
    public ResponseEntity<Team> updateTeam(@PathVariable Long id, @RequestBody Team teamDetails){
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team not exist with id :" + id));

        team.setTeamName(teamDetails.getTeamName());
        team.setHomeBar(teamDetails.getHomeBar());
        team.setSeasonId(teamDetails.getSeasonId());

        Team updatedTeam = teamRepository.save(team);
        return ResponseEntity.ok(updatedTeam);
    }

    // delete Team rest api
    @DeleteMapping("/team/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteTeam(@PathVariable Long id){
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team not exist with id :" + id));

        teamRepository.delete(team);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}
