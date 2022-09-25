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

    private String _id;

    private String title;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String location;

    private String description;

    private String restrictions;

    private Boolean cancelled;

    private Integer totalSlots;

    private List<String> participants;

}
