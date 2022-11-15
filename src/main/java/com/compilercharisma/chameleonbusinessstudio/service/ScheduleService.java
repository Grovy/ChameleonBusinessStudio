package com.compilercharisma.chameleonbusinessstudio.service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.compilercharisma.chameleonbusinessstudio.dto.Appointment;
import com.compilercharisma.chameleonbusinessstudio.dto.RepeatingAppointment;
import com.compilercharisma.chameleonbusinessstudio.dto.Schedule;
import com.compilercharisma.chameleonbusinessstudio.repository.ScheduleRepository;
import com.compilercharisma.chameleonbusinessstudio.validators.ScheduleValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ScheduleService {
    /**
     * The number of days to generate appointments out for.
     * For example, if this is set to 14, calling generateAppointments on
     * January 1st will generate appointments scheduled on January 1st to the
     * 15th.
     */
    private static final int DAYS_IN_ADVANCE = 14;

    private final ScheduleRepository repo;
    private final ScheduleValidator validator;
    private final AppointmentService appointments;

    public ScheduleService(
        ScheduleRepository repo, 
        ScheduleValidator validator,
        AppointmentService appointments
    ){
        this.repo = repo;
        this.validator = validator;
        this.appointments = appointments;
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

    /**
     * @param id the ID of the Schedule to get
     * @return the Schedule with the given ID, if any such Schedule exists.
     */
    public Mono<Schedule> getScheduleById(String id){
        return repo.getScheduleById(id);
    }

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.DAYS)
    public void doAppointmentGeneration(){
        generateAppointments().subscribe();
    }
    
    public Mono<Void> generateAppointments(){
        log.info("Generating appointments...");
        return repo.getAllSchedulesWhereEnabledEquals(true)
            .filter(this::canGenerateAppointmentFrom)
            .flatMap(this::generateAppointmentsFrom)
            .collectList()
            .flatMap(this::saveSchedules) // save modified schedules
            .doFinally((signal)->log.info("Done generating appointments"))
            .then();
    }

    private boolean canGenerateAppointmentFrom(Schedule s){
        return s.getAppointments().parallelStream()
            .anyMatch(this::canGenerateAppointmentFrom);
    }

    private boolean canGenerateAppointmentFrom(RepeatingAppointment ra){
        var now = LocalDateTime.now().toLocalDate();
        var maxDate = now.plusDays(DAYS_IN_ADVANCE);
        var lastGen = ra.getLastGenerated();
        return ra.getIsEnabled()
            && (lastGen == null || lastGen.toLocalDate().isBefore(maxDate));
    }

    /**
     * Generates appointments from the given schedule
     * 
     * @param s the schedule to generate appointments from
     * @return a mono containing the schedule if it was updated, or nothing if
     *  no new appointments were generated from it
     */
    private Mono<Schedule> generateAppointmentsFrom(Schedule s){        
        return Flux.fromIterable(s.getAppointments())
            .filter(this::canGenerateAppointmentFrom)
            .flatMapIterable(this::copyToDays)
            .flatMap(appointments::createAppointment) // don't have a method for batch-creating appts in Vendia yet
            .collectList()
            .doOnSuccess(appts -> log.info("Created %d appointments".formatted(appts.size())))
            .then(Mono.just(s));
    }

    private List<Appointment> copyToDays(RepeatingAppointment ra){
        var copies = new LinkedList<Appointment>();
        var now = LocalDateTime.now();
        var maxDate = now.plusDays(DAYS_IN_ADVANCE);
        
        Appointment copy;
        var start = (ra.getLastGenerated() == null) ? now : ra.getLastGenerated();
        for(var day = start; day.isBefore(maxDate); day = day.plusDays(1)){
            if(ra.getRepeatsOn().contains(day.getDayOfWeek())){
                // copy to that day
                copy = copyToDay(ra.getAppointment(), day);
                copies.add(copy);
            }
        }
        
        ra.setLastGenerated(maxDate);

        return copies;
    }

    /**
     * Creates a deep copy of the given appointment, except it occurs on the 
     * given day instead of the input's day.
     * 
     * @param appt the appointment to copy
     * @param day the day to copy the appointment to
     * @return a copy of the appointment, except on the given day
     */
    public Appointment copyToDay(Appointment appt, LocalDateTime day){
        // https://github.com/FasterXML/jackson-modules-java8
        var serializer = new ObjectMapper().registerModule(new JavaTimeModule());
        
        var copy = new Appointment();
        try {
            copy = serializer.readValue(serializer.writeValueAsString(appt), Appointment.class);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        copy.setStartTime(LocalDateTime.of(day.toLocalDate(), appt.getStartTime().toLocalTime()));
        copy.setEndTime(LocalDateTime.of(day.toLocalDate(), appt.getEndTime().toLocalTime()));
        copy.set_id(null); // prevent overriding existing appointments

        return copy;
    }
}
