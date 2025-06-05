package com.opdl.api.stats.controller;

import com.opdl.api.stats.exception.ResourceNotFoundException;
import com.opdl.api.stats.model.Bar;
import com.opdl.api.stats.repository.BarRepository;
import com.opdl.api.stats.service.BarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/")
public class BarController {

    @Autowired
    private BarRepository barRepository;

    @Autowired
    private BarService barService;

    // get all Bars
    @GetMapping("/bars")
    public List<Bar> getAllBars(){
        return barRepository.findAll();
    }

    @GetMapping("/bars/{seasonId}")
    public List<Bar> getAllBarsBySeasonId(@PathVariable Long seasonId){
        return barRepository.findBySeasonId(seasonId).orElse(new ArrayList<Bar>());
    }

    // get specified Bar
    @PostMapping("/bar")
    public ResponseEntity<List<Bar>> geBarByDetails(@RequestBody Bar bar){
        List<Bar> results =  new ArrayList<Bar>();

        try {
            results = barService.getquery(bar);
        } catch (ResourceNotFoundException e) {
            System.out.println(e.getMessage());
        }

        return (!results.isEmpty())? ResponseEntity.ok(results): ResponseEntity.badRequest().build();
    }

    // create Bar rest api
    @PostMapping("/registerBar")
    public Bar createBar(@RequestBody Bar bar) {
        return barRepository.save(bar);
    }

    @PostMapping("/registerBars")
    public List<Bar> createBars(@RequestBody ArrayList<Bar> bars) {
        return barRepository.saveAll(bars);
    }

    // get Bar by id rest api
    @GetMapping("/bar/{id}")
    public ResponseEntity<Bar> getBarById(@PathVariable Long id) {
        Bar bar = barRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bar not exist with id :" + id));
        return ResponseEntity.ok(bar);
    }

    @PutMapping("/bar/{id}")
    public ResponseEntity<Bar> updateBar(@PathVariable Long id, @RequestBody Bar barDetails){
        Bar bar = barRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bar not exist with id :" + id));

        bar.setBarName(barDetails.getBarName());
        bar.setNumBoards(barDetails.getNumBoards());
        bar.setNumTeams(barDetails.getNumTeams());
        bar.setSeasonId(barDetails.getSeasonId());

        Bar updatedBar = barRepository.save(bar);
        return ResponseEntity.ok(updatedBar);
    }

    // delete Bar rest api
    @DeleteMapping("/bar/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteBar(@PathVariable Long id){
        Bar bar = barRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bar not exist with id :" + id));

        barRepository.delete(bar);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}
