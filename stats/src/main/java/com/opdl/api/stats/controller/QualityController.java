package com.opdl.api.stats.controller;

import com.opdl.api.stats.exception.ResourceNotFoundException;
import com.opdl.api.stats.model.Player;
import com.opdl.api.stats.model.Quality;
import com.opdl.api.stats.repository.QualityRepository;
import com.opdl.api.stats.service.QualityService;
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
public class QualityController {

    @Autowired
    private QualityRepository qualityRepository;

    @Autowired
    private QualityService qualityService;

    // get all Quality points
    @GetMapping("/qualitys")
    public List<Quality> getAllQualitys(){
        return qualityRepository.findAll();
    }

    // create Quality rest api
    @PostMapping("/quality")
    public Quality createQuality(@RequestBody Quality quality) {
        return qualityRepository.save(quality);
    }

    // create Quality rest api
    @PostMapping("/qualities/update")
    public List<Quality> createQualities(@RequestBody List<Quality> qualities) {
        return qualityRepository.saveAll(qualities);
    }

    // get Quality by id rest api
    @GetMapping("/qualitys/{id}")
    public ResponseEntity<Quality> getQualityById(@PathVariable Long id) {
        Quality quality = qualityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quality not exist with id :" + id));
        return ResponseEntity.ok(quality);
    }

    @GetMapping("/qualitys/player/{id}")
    public ResponseEntity<List<Quality>> getQualityByPlayer(@PathVariable Long id) {
        List<Quality> quality = qualityRepository.findByPlayer(new Player(id))
                .orElseThrow(() -> new ResourceNotFoundException("Quality not exist with player id :" + id));
        return ResponseEntity.ok(quality);
    }

    // get quality points by match
    @PostMapping("/qualities/get")
    public ResponseEntity<List<Quality>> geQualityByDetails(@RequestBody Quality quality){
        List<Quality> results =  new ArrayList<Quality>();

        try {
            results = qualityService.getquery(quality);
        } catch (ResourceNotFoundException e) {
            System.out.println(e.getMessage());
        }

        return (!results.isEmpty())? ResponseEntity.ok(results): ResponseEntity.badRequest().build();
    }

    @PutMapping("/qualitys/{id}")
    public ResponseEntity<Quality> updateQuality(@PathVariable Long id, @RequestBody Quality qualityDetails){
        Quality quality = qualityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quality not exist with id :" + id));

        quality.setSeasonId(qualityDetails.getSeasonId());
        quality.setMatch(qualityDetails.getMatch());
        quality.setPlayer(qualityDetails.getPlayer());

        quality.setNumMark6(qualityDetails.getNumMark6());
        quality.setNumMark7(qualityDetails.getNumMark7());
        quality.setNumMark8(qualityDetails.getNumMark8());
        quality.setNumMark9(qualityDetails.getNumMark9());

        quality.setNumCork3(qualityDetails.getNumCork3());
        quality.setNumCork4(qualityDetails.getNumCork4());
        quality.setNumCork5(qualityDetails.getNumCork5());
        quality.setNumCork6(qualityDetails.getNumCork6());

        quality.setNumTon(qualityDetails.getNumTon());
        quality.setNumTon20(qualityDetails.getNumTon20());
        quality.setNumTon40(qualityDetails.getNumTon40());
        quality.setNumTon80(qualityDetails.getNumTon80());

        Quality updatedQuality = qualityRepository.save(quality);
        return ResponseEntity.ok(updatedQuality);
    }

    // delete Quality rest api
    @DeleteMapping("/qualitys/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteQuality(@PathVariable Long id){
        Quality quality = qualityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quality not exist with id :" + id));

        qualityRepository.delete(quality);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}
