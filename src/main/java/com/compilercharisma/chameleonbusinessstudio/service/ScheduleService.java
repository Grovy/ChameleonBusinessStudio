package com.compilercharisma.chameleonbusinessstudio.service;

import java.util.List;

import com.compilercharisma.chameleonbusinessstudio.dto.Schedule;
import com.compilercharisma.chameleonbusinessstudio.dto.validators.ScheduleValidator;
import com.compilercharisma.chameleonbusinessstudio.repository.ScheduleRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ScheduleService {
    private final ScheduleRepository repo;
    private final ScheduleValidator validator;

    public ScheduleService(ScheduleRepository repo, ScheduleValidator validator){
        this.repo = repo;
        this.validator = validator;
    }

    /**
     * adds or updates the given schedule in the app
     * 
     * @param s the schedule to add or update
     * @return nothing
     */
    public Mono<Void> saveSchedule(Schedule s){
        validator.validate(s);
        return repo.storeSchedule(s);
    }

    /**
     * adds or updates the given schedules to the app
     * 
     * @param schedules the schedules to add or update
     * @return nothing
     */
    public Mono<Void> saveSchedules(List<Schedule> schedules){
        schedules.forEach(validator::validate);
        return repo.storeSchedules(schedules);
    }

    /**
     * @return a flux containing all the Schedules stored in the service's 
     *  backing store 
     */
    public Flux<Schedule> getAllSchedules(){
        return repo.getAllSchedules();
    }
}
