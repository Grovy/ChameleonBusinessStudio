package com.compilercharisma.chameleonbusinessstudio.appointments;

import static com.compilercharisma.chameleonbusinessstudio.appointments.AppointmentSpecifications.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
public class AppointmentService implements ApplicationListener<ApplicationReadyEvent>{
    
    private final AppointmentRepository repo;
    
    @Autowired
    public AppointmentService(AppointmentRepository repo){
        this.repo = repo;
    }
    
    /**
     * temporary until we have actual data to test with
     * @param event 
     */
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime later = now.plusDays(30);
        List<AppointmentEntity> data = getAppointmentsBetween(now, later);
        if(data.isEmpty()){
            System.out.println("Creating test appointment data...");
            createTestData();
            System.out.println("done creating test appointment data");
        }
    }
    
    /**
     * temporary until we have actual data to test with
     */
    private void createTestData(){
        LocalDateTime now = LocalDateTime.now().withSecond(0).withNano(0);
        AppointmentEntity appt;
        for(int i = 1; i <= 50; ++i){
            appt = new AppointmentEntity();
            // year, month, day, hour, minute
            appt.setStartTime(now.plusDays(i - 10).plusHours(i % 4).plusMinutes(i / 15));
            appt.setEndTime(appt.getStartTime().plusHours(i % 4 + 1));
            appt.setTitle(String.format("Appt. #%d", i));
            appt.setLocation(String.format("%d J Street", i * 20));
            appt.setTotalSlots(i % 10 + 1);
            if(i % 5 == 0){
                for(int j = 0; j <= i / 5; ++j){
                    addTagValue(appt, "Tag " + j, "Value " + j);
                }
            }
            if(i % 3 == 0){
                for(int j = 0; j <= i / 3 && appt.getRegisteredUsers().size() < appt.getTotalSlots(); ++j){
                    registerUser(appt, "email " + j);
                }
            }
            createAppointment(appt);
        }
    }
    
    public void createAppointment(AppointmentEntity appt){
        repo.save(appt);
    }
    
    public List<AppointmentEntity> getAppointmentsBetween(LocalDateTime startTime, LocalDateTime endTime){
        return repo.findAll(occursWithin(startTime, endTime));
    }
    
    public Page<AppointmentEntity> getAppointmentsBetween(LocalDateTime startTime, LocalDateTime endTime, Pageable pageable){
        return repo.findAll(occursWithin(startTime, endTime), pageable);
    }
    
    public List<AppointmentEntity> getAvailableAppointments(LocalDateTime startTime, LocalDateTime endTime){
        return repo.findAll(occursWithin(startTime, endTime).and(isAvailable()));
    }
    
    public Page<AppointmentEntity> getAvailableAppointments(LocalDateTime startTime, LocalDateTime endTime, Pageable page){
        return repo.findAll(occursWithin(startTime, endTime).and(isAvailable()), page);
    }
    
    private void registerUser(AppointmentEntity appt, String email){
        if(!isSlotAvailable(appt)){
            throw new UnsupportedOperationException(String.format("Cannot register \"%s\" in appointment#%d: no slots available", email, appt.getId()));
        }
        Set<String> oldReg = appt.getRegisteredUsers();
        oldReg.add(email);
        appt.setRegisteredUsers(oldReg);
        // don't save yet
    }
    
    private boolean isSlotAvailable(AppointmentEntity appt){
        return appt.getTotalSlots() > appt.getRegisteredUsers().size();
    }
    
    /**
     * Adds the given value to the given tag for this appointment
     * 
     * @param appt
     * @param tag the tag key
     * @param value the value to add to the set of values for the tag 
     */
    public void addTagValue(AppointmentEntity appt, String tag, String value){
        AppointmentTagEntity e = new AppointmentTagEntity();
        e.setName(tag);
        e.setValue(value);
        appt.getTags().add(e);
    }
    
    /**
     * @param appt
     * @param tag the tag to get values for
     * @return a copy of the values associated with the given tag
     */
    public Set<String> getValuesForTag(AppointmentEntity appt, String tag){
        return appt.getTags().stream().filter((e)->{
            return e.getName().equals(tag);
        }).map((e)->{
            return e.getValue();
        }).collect(Collectors.toSet());
    }
    
    public Set<String> getTagKeys(AppointmentEntity appt){
        return appt.getTags().stream().map((ate)->ate.getName()).collect(Collectors.toSet());
    }
    
    public Map<String, Set<String>> getTagMapping(AppointmentEntity appt){
        Map<String, Set<String>> ret = new HashMap<>();
        getTagKeys(appt).forEach((t)->ret.put(t, getValuesForTag(appt, t)));
        return ret;
    }
    
    public boolean hasAnyValueForTag(AppointmentEntity appt, String tag){
        return getTagKeys(appt).contains(tag);
    }
    
    public boolean hasValueForTag(AppointmentEntity appt, String tag, String value){
        return appt.getTags().stream().anyMatch((ate)->{
            return ate.getName().equals(tag) && ate.getValue().equals(value);
        });
    }
    
    public boolean isUserRegistered(AppointmentEntity appt, String email){
        return appt.getRegisteredUsers().contains(email);
    }
}
