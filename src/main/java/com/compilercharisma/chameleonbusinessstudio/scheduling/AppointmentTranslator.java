package com.compilercharisma.chameleonbusinessstudio.scheduling;

import java.util.HashSet;
import java.util.Set;
import org.springframework.stereotype.Component;

/**
 * translates between Appointment, AppointmentEntity, and AppointmentJson
 * 
 * @author Matt Crow <mattcrow19@gmail.com>
 */
@Component
public class AppointmentTranslator {
    
    /**
     * converts the given, <b>mutable</b> JSON object into a less mutable data
     * object.
     * 
     * @param asJson the JSON object version of the appointment
     * 
     * @return the same appointment, represented in a different class 
     */
    public Appointment toAppointment(AppointmentJson asJson){
        Appointment appt = new Appointment(
                asJson.getId(),
                asJson.getStartTime(),
                asJson.getEndTime(),
                asJson.getTitle(),
                asJson.getLocation(),
                asJson.getDescription(),
                asJson.getRestrictions(),
                asJson.isCanceled(),
                asJson.getTotalSlots()
        );
        
        asJson.getTags().forEach((k, vs)->{
            vs.forEach((v)->appt.addTag(k, v));
        });
        
        asJson.getRegisteredUsers().forEach(appt::registerUser);
        
        return appt;
    }
    
    public AppointmentJson toJson(Appointment appt){
        AppointmentJson json = new AppointmentJson();
        json.setId(appt.getId());
        json.setStartTime(appt.getStartTime());
        json.setEndTime(appt.getEndTime());
        json.setTitle(appt.getTitle());
        json.setLocation(appt.getLocation());
        json.setRestrictions(appt.getRestrictions());
        json.setCanceled(appt.isCanceled());
        json.setTotalSlots(appt.getTotalSlots());
        json.setTags(appt.getTagValues());
        json.setRegisteredUsers(appt.getRegisteredUsers());
        return json;
    }
    
    public Appointment toAppointment(AppointmentEntity e){
        Appointment appt = new Appointment(
                e.getId(),
                e.getStartTime(),
                e.getEndTime(),
                e.getTitle(),
                e.getLocation(),
                e.getDescription(),
                e.getRestrictions(),
                e.isCanceled(),
                e.getTotalSlots()
        );
        
        e.getTags().forEach((ate)->{
            appt.addTag(ate.getName(), ate.getValue());
        });
        
        e.getRegisteredUsers().forEach(appt::registerUser);
        
        return appt;
    }
    
    public AppointmentEntity toEntity(Appointment appt){
        AppointmentEntity e = new AppointmentEntity();
        e.setId(appt.getId());
        e.setStartTime(appt.getStartTime());
        e.setEndTime(appt.getEndTime());
        e.setTitle(appt.getTitle());
        e.setLocation(appt.getLocation());
        e.setDescription(appt.getDescription());
        e.setRestrictions(appt.getRestrictions());
        e.setCanceled(appt.isCanceled());
        e.setTotalSlots(appt.getTotalSlots());
        
        Set<AppointmentTagEntity> tags = new HashSet<>();
        appt.getTagValues().forEach((k, vs)->{
            vs.forEach((v)->{
                AppointmentTagEntity ate = new AppointmentTagEntity();
                ate.setName(k);
                ate.setValue(v);
                tags.add(ate);
            });
        });
        e.setTags(tags);
        
        e.setRegisteredUsers(appt.getRegisteredUsers());
        
        return e;
    }
}
