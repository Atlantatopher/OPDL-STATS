package com.opdl.api.stats.controller;

import com.opdl.api.stats.exception.ResourceNotFoundException;
import com.opdl.api.stats.model.Player;
import com.opdl.api.stats.model.Quality;
import com.opdl.api.stats.model.Team;
import com.opdl.api.stats.repository.QualityRepository;
import com.opdl.api.stats.service.QualityService;
import com.opdl.api.stats.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/")
public class QualityController {

    @Autowired
    private QualityRepository qualityRepository;

    @Autowired
    private QualityService qualityService;

    @Autowired
    private TeamService teamService;

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

    @GetMapping("/quality/player/{playerId}")
    public ResponseEntity<List<Quality>> gePlayerWithQuality(@PathVariable Long playerId){
        List<Quality> results =  new ArrayList<Quality>();

        try {
            results = qualityService.getPlayerWithQuality(playerId);
        } catch (ResourceNotFoundException e) {
            System.out.println(e.getMessage());
        }

        return (!results.isEmpty())? ResponseEntity.ok(results): ResponseEntity.badRequest().build();
    }

    @GetMapping("/quality/player")
    public ResponseEntity<List<HashMap<String, String>>> getPlayersWithQuality(){
        List<HashMap<String, String>> qualities =  new ArrayList<HashMap<String, String>>();
        long seasonId = 6;
        try {
            qualities = qualityService.getPlayersWithQuality(seasonId);
        } catch (ResourceNotFoundException e) {
            System.out.println(e.getMessage());
        }
        qualities.sort(new Comparator<HashMap<String, String>>() {
            public int compare(HashMap<String, String> one, HashMap<String, String> two) {
                int intOne = Integer.parseInt(Optional.ofNullable(one.get("total")).orElse("0"));
                int intTwo = Integer.parseInt(Optional.ofNullable(two.get("total")).orElse("0"));
                return Integer.compare(intTwo, intOne);
            }
        });

        return (!qualities.isEmpty())? ResponseEntity.ok(qualities): ResponseEntity.badRequest().build();
    }

    @GetMapping("/quality/player/ninemark")
    public ResponseEntity<ArrayList<HashMap<String, String>>> getPlayersWithNineMarks(){
        ArrayList<HashMap<String, String>> results =  new ArrayList<HashMap<String, String>>();
        long seasonId = 6;
        try {
            results = qualityService.getPlayersWithNineMark(seasonId);
        } catch (ResourceNotFoundException e) {
            System.out.println(e.getMessage());
        }
        results.sort(new Comparator<HashMap<String, String>>() {
            public int compare(HashMap<String, String> one, HashMap<String, String> two) {
                if((null == one)||(null == two)) return 0;
                int intOne = Integer.parseInt(Optional.ofNullable(one.get("total")).orElse("0"));
                int intTwo = Integer.parseInt(Optional.ofNullable(two.get("total")).orElse("0"));
                return Integer.compare(intTwo, intOne);
            }
        });

        return (!results.isEmpty())? ResponseEntity.ok(results): ResponseEntity.badRequest().build();
    }

    @GetMapping("/quality/player/toneighty")
    public ResponseEntity<ArrayList<HashMap<String, String>>> getPlayersWithTonEighty(){
        ArrayList<HashMap<String, String>> results =  new ArrayList<HashMap<String, String>>();
        long seasonId = 6;
        try {
            results = qualityService.getPlayersWithTonEighty(seasonId);
        } catch (ResourceNotFoundException e) {
            System.out.println(e.getMessage());
        }
        results.sort(new Comparator<HashMap<String, String>>() {
            public int compare(HashMap<String, String> one, HashMap<String, String> two) {
                if((null == one)||(null == two)) return 0;
                int intOne = Integer.parseInt(Optional.ofNullable(one.get("total")).orElse("0"));
                int intTwo = Integer.parseInt(Optional.ofNullable(two.get("total")).orElse("0"));
                return Integer.compare(intTwo, intOne);
            }
        });

        return (!results.isEmpty())? ResponseEntity.ok(results): ResponseEntity.badRequest().build();
    }

    @GetMapping("/quality/player/corks")
    public ResponseEntity<ArrayList<HashMap<String, String>>> getPlayersWithCorks(){
        ArrayList<HashMap<String, String>> results =  new ArrayList<HashMap<String, String>>();
        long seasonId = 6;
        try {
            results = qualityService.getPlayersWithCorks(seasonId);
        } catch (ResourceNotFoundException e) {
            System.out.println(e.getMessage());
        }
        results.sort(new Comparator<HashMap<String, String>>() {
            public int compare(HashMap<String, String> one, HashMap<String, String> two) {
                if((null == one)||(null == two)) return 0;
                int intOne = Integer.parseInt(Optional.ofNullable(one.get("total")).orElse("0"));
                int intTwo = Integer.parseInt(Optional.ofNullable(two.get("total")).orElse("0"));
                return Integer.compare(intTwo, intOne);
            }
        });

        return (!results.isEmpty())? ResponseEntity.ok(results): ResponseEntity.badRequest().build();
    }

    @GetMapping("/quality/team")
    public ResponseEntity<List<HashMap<String, String>>> getAllTeamsWithQuality(){
        List<HashMap<String, String>> results = new ArrayList<>();
        long currentSeason = 6;
        Team  query = new Team();
        query.setSeasonId(currentSeason);
        List<Team> teams = teamService.getquery(query);
        teams.forEach(team -> {
            results.add(qualityService.calcutlateTeamQuality(team.getId(), team.getTeamName()));
        });
        results.sort(new Comparator<HashMap<String, String>>() {
            public int compare(HashMap<String, String> one, HashMap<String, String> two) {
                if((null == one)||(null == two)) return 0;
                int teamOneTotal = Integer.parseInt(Optional.ofNullable(one.get("teamTotal")).orElse("0"));
                int teamTwoTotal = Integer.parseInt(Optional.ofNullable(two.get("teamTotal")).orElse("0"));
                return Integer.compare(teamTwoTotal, teamOneTotal);
            }
        });
        return (!results.isEmpty())? ResponseEntity.ok(results): ResponseEntity.badRequest().build();
    }

    @GetMapping("/quality/team/{teamId}")
    public ResponseEntity<HashMap<String, String>> getTeamWithQuality(@PathVariable Long teamId){
        HashMap<String, String> results = new HashMap<>();
        Team  query = new Team();
        query.setId(teamId);
        Team team = teamService.getquery(query).get(0);
        try {
            results = qualityService.calcutlateTeamQuality(teamId, team.getTeamName());
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
