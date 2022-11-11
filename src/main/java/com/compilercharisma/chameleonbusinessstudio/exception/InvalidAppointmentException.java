package com.compilercharisma.chameleonbusinessstudio.exception;

import com.compilercharisma.chameleonbusinessstudio.dto.Appointment;

public class InvalidAppointmentException extends RuntimeException {
    public static final long serialVersionUID = 1L;
    private final Appointment appointment;

    public InvalidAppointmentException(Appointment appointment, String message){
        super(message);
        this.appointment = appointment;
    }

    public Appointment getInvalidAppointment(){
        return appointment;
    }
}
