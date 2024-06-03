package com.opdl.api.stats.service;

import com.opdl.api.stats.exception.ResourceNotFoundException;
import com.opdl.api.stats.model.Bar;
import com.opdl.api.stats.repository.BarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.ArrayList;
import java.util.List;

@Service
public class BarService {

    @Autowired
    private BarRepository barRepository;

    public List<Bar> getquery(Bar barQuery) throws ResourceNotFoundException{
        ArrayList<Bar> results = new ArrayList<Bar>();

        boolean hasSeasonId = barQuery.getSeasonId() != 0;
        boolean hasBarId = barQuery.getId() != 0;

        if(hasSeasonId && hasBarId){
            results.add(barRepository.findByIdAndSeasonId(barQuery.getId(), barQuery.getSeasonId())
                    .orElseThrow(() -> new ResourceNotFoundException("Bar not exist with id :" + barQuery.getId() + " and season id : " + barQuery.getSeasonId())));
        } else if(hasSeasonId){
            results.addAll(barRepository.findBySeasonId(barQuery.getSeasonId()).stream().findAny()
                    .orElseThrow(() -> new ResourceNotFoundException("Bar not exist with season id : " + barQuery.getSeasonId())));
        } else if(hasBarId){
            results.add(barRepository.findById(barQuery.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Bar not exist with  id : " + barQuery.getId())));
        } else {
            // do we want to throw an error or return everything in teh DB. Not wanting to return everything in the DB.
        }
        return results;
    }
}
