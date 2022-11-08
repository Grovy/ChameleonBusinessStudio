package com.compilercharisma.chameleonbusinessstudio.repository;

import java.util.List;

import com.compilercharisma.chameleonbusinessstudio.dto.Schedule;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * When we store schedules in Vendia, subclass this interface, don't overwrite 
 * it!
 */
public interface ScheduleRepository {

    /**
     * stores or updates the given schedule in this repository's backing store
     * if the given schedule already exists in the backing store, updates it,
     * otherwise, creates it
     * 
     * @param s the schedule to store
     * @return nothing
     */
    public Mono<Void> storeSchedule(Schedule s);

    /**
     * stores or updates all of the given schedules in the backing store
     * 
     * @param ss the schedules to store
     * @return nothing
     */
    public Mono<Void> storeSchedules(List<Schedule> ss);

    /**
     * retrieves a schedule with the given ID, if any exist
     * 
     * @param id the ID of the schedule to retrieve
     * @return a mono containing either the schedule with the given ID, or 
     *  nothing
     */
    public Mono<Schedule> getScheduleById(String id);

    /**
     * retrieves all schedules from the backing store where their isEnabled
     * property equals the given value
     * 
     * @return all schedules in the backing store
     */
    public Flux<Schedule> getAllSchedulesWhereEnabledEquals(boolean b);

    /**
     * retrieves all schedules from the backing store
     * 
     * @return all schedules in the backing store
     */
    public Flux<Schedule> getAllSchedules();

    /**
     * deletes the schedule with the given ID, if such a schedule exists
     * 
     * @param id the ID of the schedule to delete
     * @return nothing
     */
    public Mono<Void> deleteScheduleById(String id);
}
