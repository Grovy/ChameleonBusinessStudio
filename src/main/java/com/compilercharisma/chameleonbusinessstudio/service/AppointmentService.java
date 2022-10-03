package com.compilercharisma.chameleonbusinessstudio.service;

import static com.compilercharisma.chameleonbusinessstudio.entity.appointment.AppointmentSpecifications.*;
import java.time.LocalDateTime;
import java.util.*;

import com.compilercharisma.chameleonbusinessstudio.dto.Appointment;
import com.compilercharisma.chameleonbusinessstudio.entity.AppointmentEntity;
import com.compilercharisma.chameleonbusinessstudio.entity.appointment.AppointmentValidator;
import com.compilercharisma.chameleonbusinessstudio.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * https://stackoverflow.com/q/56241495
 * if we filter the Page, then convert to a List, then back to a Page, this
 * loses a lot of info, and so the _links attribute of the JSON response
 * cannot be properly generated. Therefore, we must use the Criteria API to
 * filter
 * 
 * @author Matt Crow <mattcrow19@gmail.com>
 */
@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;

    public AppointmentService(AppointmentRepository appointmentRepository){
        this.appointmentRepository = appointmentRepository;
    }

    public Mono<Appointment> createAppointment(Appointment appointment)
    {
        return Mono.just(appointment).flatMap(u -> appointmentRepository.createAppointment(appointment));
    }
    
//    private final AppointmentRepository repo;
//    private final AppointmentValidator validator;
//
//    @Autowired
//    public AppointmentService(AppointmentRepository repo, AppointmentValidator validator){
//        this.repo = repo;
//        this.validator = validator;
//    }
//
//    /**
//     * temporary until we have actual data to test with
//     * @param event
//     */
//    @Override
//    public void onApplicationEvent(ApplicationReadyEvent event) {
//        LocalDateTime now = LocalDateTime.now();
//        LocalDateTime later = now.plusDays(30);
//        List<AppointmentEntity> data = getAppointmentsBetween(now, later);
//        if(data.isEmpty()){
//            System.out.println("Creating test appointment data...");
//            createTestData();
//            System.out.println("done creating test appointment data");
//        }
//    }
//
//    /**
//     * temporary until we have actual data to test with
//     */
//    private void createTestData(){
//        LocalDateTime now = LocalDateTime.now().withSecond(0).withNano(0);
//        AppointmentEntity appt;
//        for(int i = 1; i <= 50; ++i){
//            appt = new AppointmentEntity();
//            // year, month, day, hour, minute
//            appt.setStartTime(now.plusDays(i - 10).plusHours(i % 4).plusMinutes(i / 15));
//            appt.setEndTime(appt.getStartTime().plusHours(i % 4 + 1));
//            appt.setTitle(String.format("Appt. #%d", i));
//            appt.setLocation(String.format("%d J Street", i * 20));
//            appt.setTotalSlots(i % 10 + 1);
//            if(i % 5 == 0){
//                for(int j = 0; j <= i / 5; ++j){
//                    addTag(appt, "Tag " + j);
//                }
//            }
//            if(i % 3 == 0){
//                for(int j = 0; j <= i / 3 && appt.getRegisteredUsers().size() < appt.getTotalSlots(); ++j){
//                    registerUser(appt, "email " + j);
//                }
//            }
//            createAppointment(appt);
//        }
//    }
//
//    public void createAppointment(AppointmentEntity appt){
//        repo.save(appt);
//    }
//
//    /**
//     * Gets the appointment with the given ID. Does not throw an exception if no
//     * such appointment exists.
//     *
//     * @param appointmentId the ID of the appointment to get
//     * @return an optional containing the appointment, if it exists
//     */
//    public Optional<AppointmentEntity> getAppointmentById(int appointmentId){
//        return repo.findById(appointmentId);
//    }
//
//    // todo
//    public Page<AppointmentEntity> getAppointmentsForUser(String email, Pageable pageable){
//        var appts = new ArrayList<AppointmentEntity>();
//        appts.add(new AppointmentEntity());
//        appts.add(new AppointmentEntity());
//        appts.add(new AppointmentEntity());
//        for(int i = 0; i < appts.size(); i++){
//            appts.get(i).setTitle("Fake appointment #" + i);
//            appts.get(i).getRegisteredUsers().add(email);
//        }
//
//        var page = new PageImpl<AppointmentEntity>(appts, pageable, appts.size());
//
//        return page;
//    }
//
//    public List<AppointmentEntity> getAppointmentsBetween(LocalDateTime startTime, LocalDateTime endTime){
//        return repo.findAll(occursWithin(startTime, endTime));
//    }
//
//    public Page<AppointmentEntity> getAppointmentsBetween(LocalDateTime startTime, LocalDateTime endTime, Pageable pageable){
//        return repo.findAll(occursWithin(startTime, endTime), pageable);
//    }
//
//    public List<AppointmentEntity> getAvailableAppointments(LocalDateTime startTime, LocalDateTime endTime){
//        return repo.findAll(occursWithin(startTime, endTime).and(isAvailable()));
//    }
//
//    public Page<AppointmentEntity> getAvailableAppointments(LocalDateTime startTime, LocalDateTime endTime, Pageable page){
//        return repo.findAll(occursWithin(startTime, endTime).and(isAvailable()), page);
//    }
//
//    public void updateAppointment(AppointmentEntity appt){
//        // this might also send notifications to users subscribed to the appointment
//        repo.save(appt);
//    }
//
//    /**
//     * Checks if the given appointment is valid, and thus can be stored.
//     *
//     * @param e the appointment to validate
//     * @return whether or not the appointment is valid
//     */
//    public boolean isAppointmentValid(AppointmentEntity e){
//        return validator.isValid(e);
//    }
//
//    /**
//     * Checks if the given appointment is valid, throwing an
//     * InvalidAppointmentException if it isn't.
//     *
//     * @param e the appointment to validate
//     */
//    public void validateAppointment(AppointmentEntity e){
//        validator.validate(e);
//    }
//
//    public void registerUser(AppointmentEntity appt, String email){
//        if(!isSlotAvailable(appt)){
//            throw new UnsupportedOperationException(String.format("Cannot register \"%s\" in appointment#%d: no slots available", email, appt.getId()));
//        }
//        Set<String> oldReg = appt.getRegisteredUsers();
//        oldReg.add(email);
//        appt.setRegisteredUsers(oldReg);
//        repo.save(appt);
//    }
//
//    /**
//     * Checks if the given appointment can accept more users.
//     *
//     * @param appt the appointment to check
//     * @return whether or not the appointment has any slots available
//     */
//    public boolean isSlotAvailable(AppointmentEntity appt){
//        return appt.getTotalSlots() > appt.getRegisteredUsers().size();
//    }
//
//    /**
//     * Adds the given tag to this appointment
//     *
//     * @param appt
//     * @param tag the tag key
//     */
//    public void addTag(AppointmentEntity appt, String tag){
//        Set<String> copyOfOldTags = appt.getTags();
//        copyOfOldTags.add(tag);
//        appt.setTags(copyOfOldTags);
//    }
//
//    /**
//     * Checks if the given appointment has the given tag attached to it.
//     *
//     * @param appt the appointment to check
//     * @param tag the tag to look for
//     * @return whether the given appointment has the given tag
//     */
//    public boolean hasTag(AppointmentEntity appt, String tag){
//        return appt.getTags().contains(tag);
//    }
//
//    public boolean isUserRegistered(AppointmentEntity appt, String email){
//        return appt.getRegisteredUsers().contains(email);
//    }
//
//    public Mono<Appointment> createAppointment(Appointment appointment)
//    {
//        return Mono.just(appointment).flatMap(u -> appointmentRepository.createAppointment(appointment));
//    }
}
