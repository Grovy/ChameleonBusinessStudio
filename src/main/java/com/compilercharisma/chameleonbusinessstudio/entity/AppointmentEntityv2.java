package com.compilercharisma.chameleonbusinessstudio.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

import lombok.*;

/**
 * @Author Ariel Camargo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppointmentEntityv2 implements Serializable {
    private  String _id;


    private LocalDateTime startTime;


    private LocalDateTime endTime;


    private String title;


    private String location = "online"; // default to online appointment


    private String description = "";




    private Boolean isCanceled = false;


    private Integer totalSlots = 1; // default to one person per appointment

    private Set<String> participants;


}
