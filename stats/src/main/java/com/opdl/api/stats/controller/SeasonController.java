package com.opdl.api.stats.controller;

import com.opdl.api.stats.exception.ResourceNotFoundException;
import com.opdl.api.stats.model.Season;
import com.opdl.api.stats.repository.SeasonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/")
public class SeasonController {

    @Autowired
    private SeasonRepository seasonRepository;

    // get all Bars
    @GetMapping("/seasons")
    public List<Season> getAllSeasons(){
        return seasonRepository.findAll();
    }

    // create Season rest api
    @PostMapping("/seasons")
    public Season createSeason(@RequestBody Season season) {
        return seasonRepository.save(season);
    }

    // get Season by id rest api
    @GetMapping("/seasons/{id}")
    public ResponseEntity<Season> getSeasonById(@PathVariable Long id) {
        Season season = seasonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Season not exist with id :" + id));
        return ResponseEntity.ok(season);
    }

    @PutMapping("/seasons/{id}")
    public ResponseEntity<Season> updateSeason(@PathVariable Long id, @RequestBody Season seasonDetails){
        Season season = seasonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Season not exist with id :" + id));

        season.setDescription(seasonDetails.getDescription());
        season.setStartDate(seasonDetails.getStartDate());
        season.setEndDate(seasonDetails.getEndDate());

        Season updatedSeason = seasonRepository.save(season);
        return ResponseEntity.ok(updatedSeason);
    }

    // delete Season rest api
    @DeleteMapping("/seasons/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteSeason(@PathVariable Long id){
        Season season = seasonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Season not exist with id :" + id));

        seasonRepository.delete(season);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}
