package com.compilercharisma.chameleonbusinessstudio.dto;

import java.time.DayOfWeek;
import java.util.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.compilercharisma.chameleonbusinessstudio.exception.InvalidAppointmentException;
import com.compilercharisma.chameleonbusinessstudio.exception.InvalidScheduleException;
import com.compilercharisma.chameleonbusinessstudio.validators.AppointmentValidator;
import com.compilercharisma.chameleonbusinessstudio.validators.ScheduleValidator;

public class ScheduleValidatorTester {
    
    @Test
    public void validate_givenANullSchedule_throwsAnException(){
        var sut = makeSut();
        Assertions.assertThrows(Exception.class, ()->sut.validate(null));
    } 

    @ParameterizedTest
    @ValueSource(strings = {"\n\t\n", "", " ", "\r\n"})
    public void validate_givenAScheduleWithABlankName_throwsAnException(String aBlankName){
        var sut = makeSut();
        var aScheduleWithABlankName = Schedule.builder()
            .name(aBlankName)
            .build();
        Assertions.assertThrows(
            InvalidScheduleException.class, 
            ()->sut.validate(aScheduleWithABlankName)
        );
    }

    @Test
    public void validate_givenAScheduleWithRepeatedDates_throwsAnException(){
        var sut = makeSut();
        var aScheduleWithRepeatedDates = Schedule.builder()
            .appointments(List.of(RepeatingAppointment.builder()
                .repeatsOn(List.of(DayOfWeek.MONDAY, DayOfWeek.MONDAY))
                .build()
            )).build();
        Assertions.assertThrows(Exception.class, ()->sut.validate(aScheduleWithRepeatedDates));
    }

    @Test
    public void validate_givenAScheduleWithAnInvalidAppointment_throwsAnException(){
        var sut = makeSut();
        var aScheduleWithAnInvalidAppointment = Schedule.builder()
            .name("foo")
            .appointments(List.of(
                RepeatingAppointment.builder()
                    .appointment(new Appointment())
                    .build()
            ))
            .build();

        Assertions.assertThrows(
            InvalidAppointmentException.class, 
            ()->sut.validate(aScheduleWithAnInvalidAppointment
        ));
    }

    private ScheduleValidator makeSut(){
        var appointmentValidator = new AppointmentValidator();
        var scheduleValidator = new ScheduleValidator(appointmentValidator);
        return scheduleValidator;
    }
}
