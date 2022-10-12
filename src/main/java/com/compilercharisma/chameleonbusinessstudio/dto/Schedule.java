package com.compilercharisma.chameleonbusinessstudio.dto;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;

/**
 * A schedule represents a collection of repeating appointments, which can be
 * collectively enabled or disabled
 */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Schedule {
    
    /**
     * required for Vendia compatability
     */
    private String _id;

    /**
     * a helpful name for this schedule, such as "Haircut with John" or 
     * "Game night"
     */
    private String name;

    /**
     * if set to false, this schedule will be ignored when constructing 
     * appointments from this schedule
     */
    @Builder.Default
    private Boolean isEnabled = true;

    /**
     * the appointments contained in this schedule
     */
    @Builder.Default
    private List<RepeatingAppointment> appointments = new ArrayList<>();
}
