package com.opdl.api.stats.service;

import com.opdl.api.stats.exception.ResourceNotFoundException;
import com.opdl.api.stats.model.Player;
import com.opdl.api.stats.model.Quality;
import com.opdl.api.stats.repository.QualityRepository;
import com.opdl.api.stats.specification.QualitySpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;

@Service
public class QualityService {

    @Autowired
    private QualityRepository qualityRepository;

    public List<Quality> getquery(Quality qualityQuery) throws ResourceNotFoundException{
        ArrayList<Quality> results = new ArrayList<Quality>();

        boolean hasSeasonId = qualityQuery.getSeasonId() != 0;
        boolean hasMatchId = qualityQuery.getMatch() != null;
        boolean hasPlayer = qualityQuery.getPlayer() != null;


        if(hasMatchId && hasPlayer){
            results.addAll(qualityRepository.findByMatchAndPlayer(qualityQuery.getMatch(), qualityQuery.getPlayer())
                    .orElseThrow(() -> new ResourceNotFoundException("Match not exist with match id and player combination")));
        }else if(hasMatchId){
            results.addAll(qualityRepository.findByMatch(qualityQuery.getMatch())
                    .orElseThrow(() -> new ResourceNotFoundException("Match not exist with match id")));
        } else if(hasPlayer){
            results.addAll(qualityRepository.findByPlayer(qualityQuery.getPlayer())
                    .orElseThrow(() -> new ResourceNotFoundException("Match not exist with player id")));
        } else if(hasSeasonId){
        results.addAll(qualityRepository.findBySeasonId(qualityQuery.getSeasonId())
                .orElseThrow(() -> new ResourceNotFoundException("Match not exist with season id : " + qualityQuery.getSeasonId())));
        } else {
            // do we want to throw an error or return everything in teh DB. Not wanting to return everything in the DB.
        }
        results = (ArrayList<Quality>) results.stream().map(this::calculateTotal).collect(Collectors.toList());
        return results;
    }

    public List<HashMap<String, String>> getPlayersWithQuality(long seasonId) throws ResourceNotFoundException{
        List<HashMap<String, String>> results = new ArrayList<>();
        List<Quality> qualities;
        qualities = qualityRepository.findBySeasonId(seasonId).orElseThrow(() -> new ResourceNotFoundException("Match not exist with season id : " + seasonId));
        qualities = qualities.stream().map(this::calculateTotal).toList().stream().filter(quality -> quality.getTotal() > 0).toList();
        Map <Player, Integer> byPlayer = qualities.stream()
                .collect(groupingBy(Quality::getPlayer, summingInt(Quality::getTotal)));
        byPlayer.forEach((player, numQuality) -> {
            HashMap<String, String> playerWithQualities = new HashMap<>();
            playerWithQualities.put("playerName", player.getPlayerName());
            playerWithQualities.put("teamName", player.getTeam().getTeamName());
            playerWithQualities.put("total", String.valueOf(numQuality));
            results.add(playerWithQualities);
        });
        return results;
    }

    public List<Quality> getPlayerWithQuality(long playerId) throws ResourceNotFoundException{
        List<Quality> results = new ArrayList<>();
        Specification<Quality> spec = QualitySpecifications.hasPlayerWithQuality(playerId);
        results = qualityRepository.findAll(spec);
        return results;
    }

    public ArrayList<HashMap<String, String>> getPlayersWithNineMark(long seasonId) throws ResourceNotFoundException{
        ArrayList<HashMap<String, String>> results = new ArrayList<>();
        List<Quality> nineMarks = new ArrayList<>(qualityRepository.findBySeasonIdAndNumMark9GreaterThan(seasonId, 0).orElseThrow(() -> new ResourceNotFoundException("Match not exist with season id : " + seasonId)));
        Map <Player, Integer> byPlayer = nineMarks.stream()
                .collect(groupingBy(Quality::getPlayer, summingInt(Quality::getNumMark9)));
        byPlayer.forEach((player, numNineMark) -> {
            HashMap<String, String> playerWithNineMarks = new HashMap<>();
            playerWithNineMarks.put("playerName", player.getPlayerName());
            playerWithNineMarks.put("teamName", player.getTeam().getTeamName());
            playerWithNineMarks.put("total", String.valueOf(numNineMark));
            results.add(playerWithNineMarks);
        });
        return results;
    }

    public ArrayList<HashMap<String, String>> getPlayersWithTonEighty(long seasonId) throws ResourceNotFoundException{
        ArrayList<HashMap<String, String>> results = new ArrayList<>();
        List<Quality> qualities = new ArrayList<>(qualityRepository.findBySeasonIdAndNumTon80GreaterThan(seasonId, 0).orElseThrow(() -> new ResourceNotFoundException("Match not exist with season id : " + seasonId)));
        Map <Player, Integer> byPlayer = qualities.stream()
                .collect(groupingBy(Quality::getPlayer, summingInt(Quality::getNumTon80)));
        byPlayer.forEach((player, total) -> {
            HashMap<String, String> playerWithQuality = new HashMap<>();
            playerWithQuality.put("playerName", player.getPlayerName());
            playerWithQuality.put("teamName", player.getTeam().getTeamName());
            playerWithQuality.put("total", String.valueOf(total));
            results.add(playerWithQuality);
        });
        return results;
    }

    public ArrayList<HashMap<String, String>> getPlayersWithCorks(long seasonId) throws ResourceNotFoundException{
        ArrayList<HashMap<String, String>> results = new ArrayList<>();
        List<Quality> qualities = new ArrayList<>(qualityRepository.findBySeasonId(seasonId).orElseThrow(() -> new ResourceNotFoundException("Match not exist with season id : " + seasonId)));
        Map <Player, List<Quality>> byPlayer = qualities.stream()
                .collect(groupingBy(Quality::getPlayer));
        byPlayer.forEach((player, playerQualities) -> {
            int totalCorks = calculateTotalCorks(playerQualities);
            if(totalCorks > 0) {
                HashMap<String, String> playerWithQuality = new HashMap<>();
                playerWithQuality.put("playerName", player.getPlayerName());
                playerWithQuality.put("teamName", player.getTeam().getTeamName());
                playerWithQuality.put("total", String.valueOf(totalCorks));

                results.add(playerWithQuality);
            }
        });
        return results;
    }

    public List<Quality> getTeamWithQuality(long teamId) throws ResourceNotFoundException{
        List<Quality> results = new ArrayList<>();
        Specification<Quality> spec = QualitySpecifications.hasTeamWithQuality(teamId);
        results = qualityRepository.findAll(spec);
        return results.stream().map(this::calculateTotal).toList();
    }

    private Quality calculateTotal(Quality quality){
        int totalQualityScore = 0;
        int cork3 = quality.getNumCork3() * 1;
        int cork4 = quality.getNumCork4() * 2;
        int cork5 = quality.getNumCork5() * 3;
        int cork6 = quality.getNumCork6() * 4;

        int mark3 = quality.getNumMark6() * 1;
        int mark4 = quality.getNumMark7() * 2;
        int mark5 = quality.getNumMark8() * 3;
        int mark6 = quality.getNumMark9() * 4;

        int ton = quality.getNumTon() * 1;
        int ton20 = quality.getNumTon20() * 2;
        int ton40 = quality.getNumTon40() * 3;
        int ton80 = quality.getNumTon80() * 4;
        totalQualityScore = cork3 + cork4 + cork5 + cork6 + mark3 + mark4 + mark5 + mark6 + ton + ton20 + ton40 + ton80;
        quality.setTotal(totalQualityScore);

        return quality;
    }

    private int calculateTotalCorks(List<Quality> qualities){
        AtomicInteger totalQualityScore = new AtomicInteger();
        qualities.forEach(quality -> {
            int cork3 = quality.getNumCork3() * 3;
            int cork4 = quality.getNumCork4() * 4;
            int cork5 = quality.getNumCork5() * 5;
            int cork6 = quality.getNumCork6() * 6;
            totalQualityScore.getAndAdd(cork3 + cork4 + cork5 + cork6);
        });
        return totalQualityScore.get();
    }

    public HashMap<String, String> calcutlateTeamQuality(long teamId, String teamName) {
        HashMap<String, String> results = new HashMap<>();
        List<Quality> teamQuality = new ArrayList<Quality>();
        teamQuality = getTeamWithQuality(teamId);
        AtomicInteger totalQuality = new AtomicInteger();
        results.put("teamId", String.valueOf(teamId));
        results.put("teamName", teamName);
        teamQuality.forEach(quality -> {
            totalQuality.addAndGet(quality.getTotal());
        });
        results.put("teamTotal", String.valueOf(totalQuality));
        return results;
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }
}
