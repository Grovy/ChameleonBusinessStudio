package com.compilercharisma.chameleonbusinessstudio.controller;

import java.net.URI;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.compilercharisma.chameleonbusinessstudio.dto.Schedule;
import com.compilercharisma.chameleonbusinessstudio.service.ScheduleService;

import reactor.core.publisher.Mono;

/**
 * Handles HTTP requests related to schedules of repeating appointments
 */
// only talents, admins, or organizers should access this
@RequestMapping("/api/v1/schedules")
@RestController
public class ScheduleController {
    private ScheduleService schedules;

    /**
     * @param schedules handles business logic needed by this controller
     */
    public ScheduleController(ScheduleService schedules){
        this.schedules = schedules;
    }

    /**
     * @return a page containing all schedules stored in the application
     */
    @GetMapping()
    public Mono<ResponseEntity<Page<Schedule>>> getAllSchedules(){
        return schedules.getAllSchedules()
            .collectList()
            .map(this::toPage)
            .map(page->ResponseEntity.ok(page));
    }

    /**
     * @param id the ID of the schedule to get
     * @return either a 200 or 404 response, based upon whether or not a
     *  schedule exists with the given ID.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Schedule>> getScheduleById(@PathVariable String id){
        return schedules.getScheduleById(id)
            .map(sched->ResponseEntity.ok(sched))
            .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    /**
     * Creates or updates the given schedule
     * @param schedule the schedule to create or update
     * @return a response containing the stored schedule with a link to where it
     *  can be found.
     */
    @PostMapping()
    public Mono<ResponseEntity<Schedule>> createSchedule(@RequestBody Schedule schedule){
        URI at = ServletUriComponentsBuilder
            .fromCurrentContextPath() // relative to application root
            .pathSegment("/api/v1/schedules/" + schedule.get_id())
            .build()
            .toUri();
        return schedules.saveSchedule(schedule) // service handles validation
            .then(Mono.just(schedule))
            .map(sched->ResponseEntity.created(at).body(schedule));
    }

    /**
     * Creates / updates a batch of schedules at once.
     * This is more efficient than posting the same schedules one by one.
     * 
     * @param scheduleList the schedules to create or update
     * @return a page containing the schedules stored
     */
    @PutMapping()
    public Mono<ResponseEntity<Page<Schedule>>> updateSchedules(@RequestBody List<Schedule> scheduleList){
        return schedules.saveSchedules(scheduleList) // service handles validation
            .then(Mono.just(scheduleList))
            .map(this::toPage)
            .map(page->ResponseEntity.ok(page));
    }

    private Page<Schedule> toPage(List<Schedule> schedules){
        var page = new PageImpl<Schedule>(schedules);
        return page;
    }
}
