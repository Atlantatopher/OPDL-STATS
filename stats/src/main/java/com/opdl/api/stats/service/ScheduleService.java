package com.opdl.api.stats.service;

import com.opdl.api.stats.exception.ResourceNotFoundException;
import com.opdl.api.stats.model.Schedule;
import com.opdl.api.stats.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    public List<Schedule> getquery(Schedule scheduleQuery) throws ResourceNotFoundException{
        ArrayList<Schedule> results = new ArrayList<Schedule>();

        boolean hasSeasonId = scheduleQuery.getSeasonId() != 0;
        boolean hasScheduleId = scheduleQuery.getId() != 0;
        boolean hasWeekId = scheduleQuery.getWeekId() != 0;

        if(hasScheduleId){
            results.add(scheduleRepository.findById(scheduleQuery.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Schedule not exist with  id : " + scheduleQuery.getId())));
        }else if(hasSeasonId && hasWeekId){
            results.add(scheduleRepository.findBySeasonIdAndWeekIdOrderById(scheduleQuery.getSeasonId(), scheduleQuery.getWeekId())
                    .orElseThrow(() -> new ResourceNotFoundException("Schedule not exist with week id :" + scheduleQuery.getWeekId() + " and season id : " + scheduleQuery.getSeasonId())));
        } else if(hasSeasonId){
            results.addAll(scheduleRepository.findBySeasonIdOrderByWeekId(scheduleQuery.getSeasonId()).stream().findAny()
                    .orElseThrow(() -> new ResourceNotFoundException("Schedule not exist with season id : " + scheduleQuery.getSeasonId())));
        } else {
            // do we want to throw an error or return everything in teh DB. Not wanting to return everything in the DB.
        }
        return results;
    }

    public List<Schedule> getCulmativeWeeks(Schedule scheduleQuery) throws ResourceNotFoundException{
        ArrayList<Schedule> results = new ArrayList<Schedule>();

        boolean hasSeasonId = scheduleQuery.getSeasonId() != 0;
        boolean hasWeekId = scheduleQuery.getWeekId() != 0;

        if(hasSeasonId && hasWeekId){
            results.addAll(scheduleRepository.findBySeasonIdAndWeekIdLessThanEqualOrderByIdDesc(scheduleQuery.getSeasonId(), scheduleQuery.getWeekId())
                    .orElseThrow(() -> new ResourceNotFoundException("Schedule not exist with week id :" + scheduleQuery.getWeekId() + " and season id : " + scheduleQuery.getSeasonId())));
        } else if(hasSeasonId){
            results.addAll(scheduleRepository.findBySeasonIdOrderById(scheduleQuery.getSeasonId()).stream().findAny()
                    .orElseThrow(() -> new ResourceNotFoundException("Schedule not exist with season id : " + scheduleQuery.getSeasonId())));
        } else {
            // do we want to throw an error or return everything in teh DB. Not wanting to return everything in the DB.
        }
        return results;
    }
}
