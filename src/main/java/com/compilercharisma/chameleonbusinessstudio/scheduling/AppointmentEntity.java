package com.compilercharisma.chameleonbusinessstudio.scheduling;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * https://stackoverflow.com/questions/6164123/org-hibernate-mappingexception-could-not-determine-type-for-java-util-set
 * 
 * While appointmentTags really should be a Map<String, Set<String>>, JPA does
 * not support that: https://stackoverflow.com/a/14571422
 * 
 * https://stackoverflow.com/a/62848296
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
    private String location;
    
    @Setter
    @Getter
    @Column(nullable=false)
    private String description = "";
    
    @Setter
    @Getter
    @Column(nullable=false)
    private String restrictions = "";
    
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
    @Embedded
    @ElementCollection
    @CollectionTable(joinColumns = @JoinColumn(name = "appt_id"))
    @JoinColumn(name = "appt_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<AppointmentTagEntity> tags = new HashSet<>();
    
    @Setter
    @Getter
    @Column(nullable=false)
    @ElementCollection
    @CollectionTable(joinColumns = @JoinColumn(name = "appt_id"))
    @JoinColumn(name = "appt_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<String> registeredUsers = new HashSet<>();
    
    
    public AppointmentEntity(){
        
    }
    
    /**
     * Adds the given value to the given tag for this appointment
     * 
     * @param tag the tag key
     * @param value the value to add to the set of values for the tag 
     */
    public void addTagValue(String tag, String value){
        AppointmentTagEntity e = new AppointmentTagEntity();
        e.setName(tag);
        e.setValue(value);
        tags.add(e);
    }
    
    /**
     * @param tag the tag to get values for
     * @return a copy of the values associated with the given tag
     */
    public Set<String> getValuesForTag(String tag){
        return tags.stream().filter((e)->{
            return e.getName().equals(tag);
        }).map((e)->{
            return e.getValue();
        }).collect(Collectors.toSet());
    }
    
    public void addRegisteredUser(String email){
        registeredUsers.add(email);
    }
    
    public boolean isUserRegistered(String email){
        return registeredUsers.contains(email);
    }
    
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
            String.format("restrictions : \"%s\"", restrictions),
            String.format("isCanceled : %b", isCanceled),
            String.format("location: \"%s\"", location),
            String.format("totalSlots: %d", totalSlots)
        };
        for(String line : lines){
            sb.append(String.format("* %s%n", line));
        }
        
        sb.append("* tags: \n");
        tags.forEach((e)->{
            sb.append(String.format("\t%s = %s", e.getName(), e.getValue()));
        });
        
        sb.append("* registeredUsers: \n");
        registeredUsers.forEach((email)->{
            sb.append(String.format("\t%s%n", email));
        });
        
        return sb.toString();
    }
}
