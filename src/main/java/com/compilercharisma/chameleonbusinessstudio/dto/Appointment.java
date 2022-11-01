package com.compilercharisma.chameleonbusinessstudio.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    @Builder.Default
    private String location = "online";

    /**
     * Description of the appointment
     */
    @Builder.Default
    private String description= "";


    /**
     * If the appointment is cancelled or not
     */
    @Builder.Default
    private Boolean cancelled = false;;

    /**
     * Total number of slots available for the appointment
     */
    @Builder.Default
    private Integer totalSlots = 1;

    /**
     * List of the email/Ids of the users participating in the appointment
     */
    @Builder.Default
    private List<String> participants = new ArrayList<>();

}
