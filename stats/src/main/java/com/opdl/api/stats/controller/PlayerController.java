package com.opdl.api.stats.controller;

import com.opdl.api.stats.exception.ResourceNotFoundException;
import com.opdl.api.stats.model.Bar;
import com.opdl.api.stats.model.Player;
import com.opdl.api.stats.model.Player;
import com.opdl.api.stats.repository.PlayerRepository;
import com.opdl.api.stats.service.BarService;
import com.opdl.api.stats.service.PlayerService;
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
public class PlayerController {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private BarService barService;

    // get all Players
    @GetMapping("/players")
    public List<Player> getAllPlayers(){
        return playerRepository.findAll();
    }

    // get all Players
    @GetMapping("/players/email")
    public Map<String, String> getAllPlayersEmail(){
        long seasonId = 5;
        List<Player> players =  playerRepository.findBySeasonId(seasonId).orElse(new ArrayList<>());
        Map<String, String> results = new HashMap<>();
        players.stream().filter(player -> (null != player && null != player.getEmailAddress() && !player.getEmailAddress().isEmpty())).forEach(player -> { results.put(player.getEmailAddress(), player.getPlayerName());});
        return results;
    }

    @GetMapping("/owners/email")
    public Map<String, String> getAllOwnerEmail(){
        long seasonId = 5;
        Bar barQuery = new Bar();
        barQuery.setSeasonId(seasonId);
        List<Bar> owners =  barService.getquery(barQuery);
        Map<String, String> results = new HashMap<>();
        owners.stream().filter(owner -> (null != owner && null != owner.getEmailAddress() && !owner.getEmailAddress().isEmpty())).forEach(owner -> { results.put(owner. getEmailAddress(), owner.getOwnerName());});
        return results;
    }

    @PostMapping("/registerPlayers")
    public List<Player> createPlayers(@RequestBody List<Player> players) {
        return playerRepository.saveAll(players);
    }


    // get specified Player
    @PostMapping("/player")
    public ResponseEntity<List<Player>> gePlayerByDetails(@RequestBody Player player){
        List<Player> results =  new ArrayList<Player>();

        try {
            results = playerService.getquery(player);
        } catch (ResourceNotFoundException e) {
            System.out.println(e.getMessage());
        }

        return (!results.isEmpty())? ResponseEntity.ok(results): ResponseEntity.badRequest().build();
    }

    // create Player rest api
    @PostMapping("/players")
    public Player createPlayer(@RequestBody Player player) {
        return playerRepository.save(player);
    }

    // get Player by id rest api
    @GetMapping("/players/{id}")
    public ResponseEntity<Player> getPlayerById(@PathVariable Long id) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Player not exist with id :" + id));
        return ResponseEntity.ok(player);
    }

    @GetMapping("/players/team/{teamId}")
    public ResponseEntity<List<Player>> gePlayerByTeam(@PathVariable Long teamId){
        List<Player> results =  new ArrayList<Player>();

        try {
            results = playerService.getPlayersForTeam(teamId);
        } catch (ResourceNotFoundException e) {
            System.out.println(e.getMessage());
        }

        return (!results.isEmpty())? ResponseEntity.ok(results): ResponseEntity.badRequest().build();
    }

    @GetMapping("/player/quality/{playerId}")
    public ResponseEntity<List<Player>> gePlayerWithQuality(@PathVariable Long playerId){
        List<Player> results =  new ArrayList<Player>();

        try {
            results = playerService.getPlayerWithQuality(playerId);
        } catch (ResourceNotFoundException e) {
            System.out.println(e.getMessage());
        }

        return (!results.isEmpty())? ResponseEntity.ok(results): ResponseEntity.badRequest().build();
    }

    @PutMapping("/players/{id}")
    public ResponseEntity<Player> updatePlayer(@PathVariable Long id, @RequestBody Player playerDetails){
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Player not exist with id :" + id));

        player.setPlayerName(playerDetails.getPlayerName());
        player.setTeam(playerDetails.getTeam());
        player.setSeasonId(playerDetails.getSeasonId());

        Player updatedPlayer = playerRepository.save(player);
        return ResponseEntity.ok(updatedPlayer);
    }

    // delete Player rest api
    @DeleteMapping("/players/{id}")
    public ResponseEntity<Map<String, Boolean>> deletePlayer(@PathVariable Long id){
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Player not exist with id :" + id));

        playerRepository.delete(player);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }

    //Get Captains Info
    @GetMapping("/captains/{seasonId}")
    public ResponseEntity<List<Player>> getCaptainsInfo(@PathVariable Long seasonId){
        List<Player> results =  new ArrayList<Player>();

        try {
            results = playerService.getCaptains(seasonId);
        } catch (ResourceNotFoundException e) {
            System.out.println(e.getMessage());
        }

        return (!results.isEmpty())? ResponseEntity.ok(results): ResponseEntity.badRequest().build();
    }
}
