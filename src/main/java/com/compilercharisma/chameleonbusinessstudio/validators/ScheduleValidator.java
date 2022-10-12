package com.compilercharisma.chameleonbusinessstudio.validators;

import java.time.DayOfWeek;
import java.util.HashSet;

import com.compilercharisma.chameleonbusinessstudio.dto.RepeatingAppointment;
import com.compilercharisma.chameleonbusinessstudio.dto.Schedule;
import com.compilercharisma.chameleonbusinessstudio.exception.InvalidScheduleException;

/**
 * ensures Schedules and RepeatingAppointments adhere to business rules
 */
public class ScheduleValidator {
    private final AppointmentValidator appointmentValidator;

    /**
     * allows us to inject mocks of AppointmentValidator
     * @param appointmentValidator used to validate the appointments in 
     *  schedules
     */
    public ScheduleValidator(AppointmentValidator appointmentValidator){
        this.appointmentValidator = appointmentValidator;
    }

    /**
     * checks if this schedule, all of its repeating appointments, and all of
     * their appointment templates are all valid. Throws an exception if any
     * part of the schedule is invalid
     * 
     * @param schedule the schedule to validate
     */
    public void validate(Schedule schedule){
        if(schedule.getName().isBlank()){
            throw new InvalidScheduleException("schedule name cannot be empty");
        }
        schedule.getAppointments().forEach(this::validate);
    }

    private void validate(RepeatingAppointment ra){
        var asList = ra.getRepeatsOn();
        var asSet = new HashSet<DayOfWeek>(asList);
        if(asList.size() > asSet.size()){
            throw new InvalidScheduleException("repeatsOn cannot contain duplicates");
        }

        appointmentValidator.validate(ra.getAppointment());
    }
}
