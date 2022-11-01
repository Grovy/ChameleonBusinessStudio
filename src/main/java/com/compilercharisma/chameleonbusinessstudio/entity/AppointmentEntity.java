package com.compilercharisma.chameleonbusinessstudio.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;
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
@Entity
public class AppointmentEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter
    @Getter
    @Column(name="appt_id")
    private int id;
    
    @Setter
    @Getter
    @Column(nullable=false)
    private LocalDateTime startTime;
    
    @Setter
    @Getter
    @Column(nullable=false)
    private LocalDateTime endTime;
    
    @Setter
    @Getter
    @Column(nullable=false)
    private String title;
    
    @Setter
    @Getter
    @Column(nullable=false)
    private String location = "online"; // default to online appointment
    
    @Setter
    @Getter
    @Column(nullable=false)
    private String description = "";

    
    @Setter
    @Getter
    @Column(nullable=false)
    private boolean isCanceled = false;
    
    @Setter
    @Getter
    @Column(nullable=false)
    private int totalSlots = 1; // default to one person per appointment
    
    @Setter
    @Getter
    @Column(nullable=false)
    @ElementCollection(fetch = FetchType.EAGER) // https://stackoverflow.com/a/11746720
    @CollectionTable(joinColumns = @JoinColumn(name = "appt_id"))
    @JoinColumn(name = "appt_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<String> tags = new HashSet<>();
    
    @Setter
    @Getter
    @Column(nullable=false)
    @ElementCollection(fetch = FetchType.EAGER) // "load these with the initial DB query, not later"
    @CollectionTable(joinColumns = @JoinColumn(name = "appt_id"))
    @JoinColumn(name = "appt_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<String> registeredUsers = new HashSet<>();
    /*
    registeredUsers column should have an FK relation to user email, but I don't
    think it's possible to specify in JPA. Regardless, we'll be migrating to
    Vendia in the near future, so it likely isn't worth investing time into.
    */
    
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Appointment:\n");
        String[] lines = {
            String.format("id : %d", id),
            String.format("startTime : %s", startTime.toString()),
            String.format("endTime : %s", endTime.toString()),
            String.format("title : \"%s\"", title),
            String.format("description : \"%s\"", description),
            String.format("isCanceled : %b", isCanceled),
            String.format("location: \"%s\"", location),
            String.format("totalSlots: %d", totalSlots)
        };
        for(String line : lines){
            sb.append(String.format("* %s%n", line));
        }
        
        sb.append("* tags: \n");
        tags.forEach((t)->{
            sb.append(String.format("\t%s%n", t));
        });
        
        sb.append("* registeredUsers: \n");
        registeredUsers.forEach((email)->{
            sb.append(String.format("\t%s%n", email));
        });
        
        return sb.toString();
    }
}
