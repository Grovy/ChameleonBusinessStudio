package com.compilercharisma.chameleonbusinessstudio.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserAppointmentsResponse {

    /**
     * Appointments for a user
     */
    private List<Appointment> appointments;
}

