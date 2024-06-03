package com.opdl.api.stats.controller;

import com.opdl.api.stats.exception.ResourceNotFoundException;
import com.opdl.api.stats.model.*;
import com.opdl.api.stats.repository.MatchRepository;
import com.opdl.api.stats.repository.ScheduleRepository;
import com.opdl.api.stats.service.MatchService;
import com.opdl.api.stats.service.ScheduleService;
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
public class ScheduleController {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private MatchService matchService;



    // get all Schedules
    @GetMapping("/schedules")
    public List<Schedule> getAllSchedules(){
        return scheduleRepository.findAll();
    }

    @PostMapping("/registerSchedule")
    public List<Schedule> createSchedules(@RequestBody List<Schedule> schedules) {
        return scheduleRepository.saveAll(schedules);
    }

    @PostMapping("/registerMatch")
    public List<Match> createMatches(@RequestBody List<Match> matches) {
        return matchRepository.saveAll(matches);
    }

    // get specified Schedule
    @PostMapping("/schedule/week")
    public ResponseEntity<List<Match>> geMatchByDetails(@RequestBody Match match){
        List<Match> results =  new ArrayList<Match>();

        try {
            results = matchService.getquery(match);
        } catch (ResourceNotFoundException e) {
            System.out.println(e.getMessage());
        }

        return (!results.isEmpty())? ResponseEntity.ok(results): ResponseEntity.badRequest().build();
    }

    @PostMapping("/schedule/weeks")
    public ResponseEntity<List<Schedule>> getWeeksByDetails(@RequestBody Schedule scheduleQuery){
        List<Schedule> results =  new ArrayList<Schedule>();

        try {
            results = scheduleService.getCulmativeWeeks(scheduleQuery);
        } catch (ResourceNotFoundException e) {
            System.out.println(e.getMessage());
        }

        return (!results.isEmpty())? ResponseEntity.ok(results): ResponseEntity.badRequest().build();
    }

    // get specified Schedule
    @PostMapping("/schedule")
    public ResponseEntity<List<Schedule>> geScheduleByDetails(@RequestBody Schedule schedule){
        List<Schedule> results =  new ArrayList<Schedule>();

        try {
            results = scheduleService.getquery(schedule);
        } catch (ResourceNotFoundException e) {
            System.out.println(e.getMessage());
        }

        return (!results.isEmpty())? ResponseEntity.ok(results): ResponseEntity.badRequest().build();
    }

    // create Schedule rest api
    @PostMapping("/schedules")
    public Schedule createSchedule(@RequestBody Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    // get Schedule by id rest api
    @GetMapping("/schedules/{id}")
    public ResponseEntity<Schedule> getScheduleById(@PathVariable Long id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not exist with id :" + id));
        return ResponseEntity.ok(schedule);
    }

    @PutMapping("/schedules/{id}")
    public ResponseEntity<Schedule> updateSchedule(@PathVariable Long id, @RequestBody Schedule scheduleDetails){
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not exist with id :" + id));

        schedule.setSeasonId(scheduleDetails.getSeasonId());
        schedule.setMatchDate(scheduleDetails.getMatchDate());
        schedule.setWeekId(scheduleDetails.getWeekId());

        Schedule updatedSchedule = scheduleRepository.save(schedule);
        return ResponseEntity.ok(updatedSchedule);
    }

    // delete Schedule rest api
    @DeleteMapping("/schedules/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteSchedule(@PathVariable Long id){
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not exist with id :" + id));

        scheduleRepository.delete(schedule);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}
