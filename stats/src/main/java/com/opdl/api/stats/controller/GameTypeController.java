package com.opdl.api.stats.controller;

import com.opdl.api.stats.exception.ResourceNotFoundException;
import com.opdl.api.stats.model.GameType;
import com.opdl.api.stats.repository.GameTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/")
public class GameTypeController {

    @Autowired
    private GameTypeRepository gameTypeRepository;

    // get all GameTypes
    @GetMapping("/gameTypes")
    public List<GameType> getAllGameTypes(){
        return gameTypeRepository.findAll();
    }

    // create GameType rest api
    @PostMapping("/gameTypes")
    public GameType createGameType(@RequestBody GameType gameType) {
        return gameTypeRepository.save(gameType);
    }

    // get GameType by id rest api
    @GetMapping("/gameTypes/{id}")
    public ResponseEntity<GameType> getGameTypeById(@PathVariable Long id) {
        GameType gameType = gameTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("GameType not exist with id :" + id));
        return ResponseEntity.ok(gameType);
    }

    @PutMapping("/gameTypes/{id}")
    public ResponseEntity<GameType> updateGameType(@PathVariable Long id, @RequestBody GameType gameTypeDetails){
        GameType gameType = gameTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("GameType not exist with id :" + id));

        gameType.setDescription(gameTypeDetails.getDescription());


        GameType updatedGameType = gameTypeRepository.save(gameType);
        return ResponseEntity.ok(updatedGameType);
    }

    // delete GameType rest api
    @DeleteMapping("/gameTypes/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteGameType(@PathVariable Long id){
        GameType gameType = gameTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("GameType not exist with id :" + id));

        gameTypeRepository.delete(gameType);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}
