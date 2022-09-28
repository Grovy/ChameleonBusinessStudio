package com.compilercharisma.chameleonbusinessstudio.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Appointment implements Serializable {

    /**
     * The Vendia Id of the appointment
     */
    private String _id;

    /**
     * The title of the appointment
     */
    private String title;

    /**
     * The start time of the appointment
     */
    private LocalDateTime startTime;

    /**
     * The end time of the appointment
     */
    private LocalDateTime endTime;

    /**
     * The location of the appointment
     */
    private String location;

    /**
     * Description of the appointment
     */
    private String description;

    /**
     * The restrictions related to this appointment
     */
    private String restrictions;

    /**
     * If the appointment is cancelled or not
     */
    private Boolean cancelled;

    /**
     * Total number of slots available for the appointment
     */
    private Integer totalSlots;

    /**
     * List of the email/Ids of the users participating in the appointment
     */
    private List<String> participants;

}
