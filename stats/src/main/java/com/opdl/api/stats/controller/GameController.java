package com.opdl.api.stats.controller;

import com.opdl.api.stats.exception.ResourceNotFoundException;
import com.opdl.api.stats.model.Game;
import com.opdl.api.stats.model.Player;
import com.opdl.api.stats.repository.GameRepository;
import com.opdl.api.stats.service.GameService;
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
public class GameController {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameService gameService;

    // get all Bars
    @GetMapping("/games")
    public List<Game> getAllGames(){
        return gameRepository.findAll();
    }

    // create Game rest api
    @PostMapping("/games")
    public List<Game> createGame(@RequestBody List<Game> games) {
        return gameRepository.saveAll(games);
    }

    @PostMapping("/game")
    public ResponseEntity<List<Game>> getGamesByDetails(@RequestBody Game game){
        List<Game> results =  new ArrayList<Game>();

        try {
            results = gameService.getquery(game);
        } catch (ResourceNotFoundException e) {
            System.out.println(e.getMessage());
        }

        return (!results.isEmpty())? ResponseEntity.ok(results): ResponseEntity.badRequest().build();
    }

    // get Game by id rest api
    @GetMapping("/games/{id}")
    public ResponseEntity<Game> getGameById(@PathVariable Long id) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Game not exist with id :" + id));
        return ResponseEntity.ok(game);
    }

    @PutMapping("/games/{id}")
    public ResponseEntity<Game> updateGame(@PathVariable Long id, @RequestBody Game gameDetails){
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Game not exist with id :" + id));

        game.setGameType(gameDetails.getGameType());
        game.setAwayPlayer(gameDetails.getAwayPlayer());
        game.setDoubles(gameDetails.isDoubles());
        game.setAwayScore(gameDetails.getAwayScore());
        game.setHomePlayer(gameDetails.getHomePlayer());
        game.setHomeScore(gameDetails.getHomeScore());
        game.setHomePlayerDBL(gameDetails.getHomePlayerDBL());
        game.setAwayPlayerDBL(gameDetails.getAwayPlayerDBL());
        game.setMatch(gameDetails.getMatch());

        Game updatedGame = gameRepository.save(game);
        return ResponseEntity.ok(updatedGame);
    }

    // delete Game rest api
    @DeleteMapping("/games/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteGame(@PathVariable Long id){
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Game not exist with id :" + id));

        gameRepository.delete(game);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}
