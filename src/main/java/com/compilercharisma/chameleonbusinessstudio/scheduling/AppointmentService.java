package com.compilercharisma.chameleonbusinessstudio.scheduling;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Matt Crow <mattcrow19@gmail.com>
 */
@Service
public class AppointmentService {
    
    private final AppointmentRepository repo;
    private final AppointmentTranslator translator;
    
    @Autowired
    public AppointmentService(AppointmentRepository repo, AppointmentTranslator translator){
        this.repo = repo;
        this.translator = translator;
    }
    
    
    public void createAppointment(Appointment appt){
        repo.save(translator.toEntity(appt));
    }
    
    public List<Appointment> getAppointmentsBetween(LocalDateTime startTime, LocalDateTime endTime){
        return repo
                .findAllByStartTimeGreaterThanAndEndTimeLessThan(startTime, endTime)
                .stream()
                .map(translator::toAppointment)
                .collect(Collectors.toList());
    }
}
