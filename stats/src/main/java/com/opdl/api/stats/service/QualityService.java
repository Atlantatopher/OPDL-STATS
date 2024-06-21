package com.opdl.api.stats.service;

import com.opdl.api.stats.exception.ResourceNotFoundException;
import com.opdl.api.stats.model.Quality;
import com.opdl.api.stats.repository.QualityRepository;
import com.opdl.api.stats.specification.QualitySpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<Quality> getPlayerWithQuality(long playerId) throws ResourceNotFoundException{
        List<Quality> results = new ArrayList<>();
        Specification<Quality> spec = QualitySpecifications.hasPlayerWithQuality(playerId);
        results = qualityRepository.findAll(spec);
        return results;
    }

    public List<Quality> getTeamWithQuality(long teamId) throws ResourceNotFoundException{
        List<Quality> results = new ArrayList<>();
        Specification<Quality> spec = QualitySpecifications.hasTeamWithQuality(teamId);
        results = qualityRepository.findAll(spec);
        return results;
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
}
