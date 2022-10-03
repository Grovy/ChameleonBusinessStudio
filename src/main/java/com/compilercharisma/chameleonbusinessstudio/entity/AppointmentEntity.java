package com.compilercharisma.chameleonbusinessstudio.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.*;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * This class contains all the raw data that represents an appointment.
 * Don't put business logic in this class, as some methods might be called by 
 * JPA, and business logic belongs in the service.
 * 
 * Using Collections with OnDelete: https://stackoverflow.com/a/62848296
 * 
 * @author Matt Crow <mattcrow19@gmail.com>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppointmentEntity implements Serializable {
    private  String _id;
    

    private LocalDateTime startTime;
    

    private LocalDateTime endTime;
    

    private String title;
    

    private String location = "online"; // default to online appointment
    

    private String description = "";
    

    private String restrictions = "";
    

    private Boolean isCanceled = false;
    

    private Integer totalSlots = 1; // default to one person per appointment

    private List<String> participants;
    
//    @Setter
//    @Getter
//    @Column(nullable=false)
//    @ElementCollection
//    @CollectionTable(joinColumns = @JoinColumn(name = "appt_id"))
//    @JoinColumn(name = "appt_id")
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    private Set<String> tags = new HashSet<>();

}
