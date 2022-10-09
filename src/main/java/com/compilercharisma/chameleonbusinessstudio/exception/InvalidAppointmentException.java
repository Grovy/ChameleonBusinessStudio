package com.compilercharisma.chameleonbusinessstudio.exception;

import com.compilercharisma.chameleonbusinessstudio.entity.AppointmentEntity;

public class InvalidAppointmentException extends RuntimeException {
    
    private final AppointmentEntity appointment;

    public InvalidAppointmentException(AppointmentEntity appointment, String message){
        super(message);
        this.appointment = appointment;
    }

    public AppointmentEntity getInvalidAppointment(){
        return appointment;
    }
}
