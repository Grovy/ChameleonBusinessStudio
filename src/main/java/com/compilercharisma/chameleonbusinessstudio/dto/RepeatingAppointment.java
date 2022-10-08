package com.compilercharisma.chameleonbusinessstudio.dto;

import java.time.DayOfWeek;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;

/**
 * holds an appointment that is meant to repeat on specified days
 */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RepeatingAppointment {
    /**
     * required by Vendia
     */
    private String _id;

    /**
     * if set to false, the appointment contained herein will not be copied when
     * creating new appointments
     */
    private boolean isEnabled;

    /**
     * the days the appointment repeats on
     */
    @Builder.Default
    private List<DayOfWeek> repeatsOn = new ArrayList<>();

    /**
     * the appointment that repeats according to this class
     */
    private Appointment appointment;
}
