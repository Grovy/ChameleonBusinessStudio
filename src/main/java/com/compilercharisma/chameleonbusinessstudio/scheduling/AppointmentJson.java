package com.compilercharisma.chameleonbusinessstudio.scheduling;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.persistence.Column;
import lombok.Getter;
import lombok.Setter;

/**
 * this is the version of appointments we'll send / receive through the 
 * controllers
 * 
 * @author Matt Crow <mattcrow19@gmail.com>
 */
public class AppointmentJson {
    @Setter
    @Getter
    private int id;
    
    @Setter
    @Getter
    private LocalDateTime startTime;
    
    @Setter
    @Getter
    private LocalDateTime endTime;
    
    @Setter
    @Getter
    private String title;
    
    @Setter
    @Getter
    private String location;
    
    @Setter
    @Getter
    private String description = "";
    
    @Setter
    @Getter
    private String restrictions = "";
    
    @Setter
    @Getter
    private boolean isCanceled = false;
    
    @Setter
    @Getter
    private int totalSlots = 1;
    
    @Setter
    @Getter
    private Map<String, Set<String>> tags = new HashMap<>();
    
    @Setter
    @Getter
    private Set<String> registeredUsers;
    
    public AppointmentJson(){
        // need empty ctor for post handler?
    }
}
