package com.opdl.api.stats.controller;

import com.opdl.api.stats.exception.ResourceNotFoundException;
import com.opdl.api.stats.model.*;
import com.opdl.api.stats.repository.HighInOutRepository;
import com.opdl.api.stats.repository.QualityRepository;
import com.opdl.api.stats.service.HighInOutService;
import com.opdl.api.stats.service.QualityService;
import com.opdl.api.stats.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/")
public class HighInOutController {

    @Autowired
    private HighInOutRepository highInOutRepository;

    @Autowired
    private HighInOutService highInOutService;


    // get all HighInOut
    @GetMapping("/highInOuts")
    public List<HighInOut> getAllHighInOuts(){
        return highInOutRepository.findAll();
    }

    @GetMapping("/highInOuts/match/{id}")
    public List<HighInOut> getAllHighInOuts(@PathVariable Long id){
        Match match = new Match(id);
        return highInOutRepository.findByMatch(match)
                .orElseThrow(() -> new ResourceNotFoundException("HighInOut not exist with match id :" + id));
    }

    @GetMapping("/highInOuts/player/in/{seasonId}")
    public List<HighInOut> getAllHighIns(@PathVariable Long seasonId){
        return highInOutService.getPlayerWithHighIns(seasonId);
    }

    @GetMapping("/highInOuts/player/out/{seasonId}")
    public List<HighInOut> getAllHighOuts(@PathVariable Long seasonId){
        return highInOutService.getPlayerWithHighOuts(seasonId);
    }

    @GetMapping("/highInOuts/player/report/in/{seasonId}")
    public List<HighInOutReport> getAllHighInsReport(@PathVariable Long seasonId){
        return highInOutService.reportPlayersWithHighIns(seasonId);
    }

    @GetMapping("/highInOuts/player/report/out/{seasonId}")
    public List<HighInOutReport> getAllHighOutsReport(@PathVariable Long seasonId){
        return highInOutService.reportPlayersWithHighOuts(seasonId);
    }

    // create HighInOut rest api
    @PostMapping("/highInOuts")
    public HighInOut createHighInOut(@RequestBody HighInOut highInOut) {
        return highInOutRepository.save(highInOut);
    }

    // create HighInOut rest api
    @PostMapping("/highInOuts/update")
    public List<HighInOut> createHighInOuts(@RequestBody List<HighInOut> highInOuts) {
        return highInOutRepository.saveAll(highInOuts);
    }

    // delete Quality rest api
    @DeleteMapping("/highInOuts/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteHighInOut(@PathVariable Long id){
        HighInOut highInOut = highInOutRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("HighInOut not exist with id :" + id));

        highInOutRepository.delete(highInOut);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}
