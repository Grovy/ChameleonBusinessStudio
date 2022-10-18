package com.compilercharisma.chameleonbusinessstudio.util;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.compilercharisma.chameleonbusinessstudio.dto.Appointment;
import com.compilercharisma.chameleonbusinessstudio.dto.RepeatingAppointment;

public class TestRepeatingAppointmentBuilder {
    private boolean isEnabled;
    private Set<DayOfWeek> repeatsOn = new HashSet<>(); 

    public TestRepeatingAppointmentBuilder enabled(){
        isEnabled = true;
        return this;
    }

    public TestRepeatingAppointmentBuilder disabled(){
        isEnabled = false;
        return this;
    }

    public TestRepeatingAppointmentBuilder on(DayOfWeek day){
        repeatsOn.add(day);
        return this;
    }

    public RepeatingAppointment build(){
        return RepeatingAppointment.builder()
            .isEnabled(isEnabled)
            .repeatsOn(List.copyOf(repeatsOn))
            .appointment(Appointment.builder()
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusHours(1))
                .build()
            )
            .build();
    }
}
