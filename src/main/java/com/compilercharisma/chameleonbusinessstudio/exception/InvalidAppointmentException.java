package com.compilercharisma.chameleonbusinessstudio.exception;

import com.compilercharisma.chameleonbusinessstudio.dto.Appointment;
import com.compilercharisma.chameleonbusinessstudio.entity.AppointmentEntity;

// rework this one once we have a single appointment class
public class InvalidAppointmentException extends RuntimeException {
    
    private final AppointmentEntity appointment;
    private final Appointment appointment2;

    public InvalidAppointmentException(AppointmentEntity appointment, String message){
        super(message);
        this.appointment = appointment;
        this.appointment2 = null;
    }

    public InvalidAppointmentException(Appointment appointment, String message){
        super(message);
        this.appointment = null;
        this.appointment2 = appointment;
    }

    public AppointmentEntity getInvalidAppointment(){
        return appointment;
    }

    public Appointment getInvalidAppointment2(){
        return appointment2;
    }
}
