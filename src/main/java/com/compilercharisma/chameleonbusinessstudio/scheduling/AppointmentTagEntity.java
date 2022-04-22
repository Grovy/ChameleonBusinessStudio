package com.compilercharisma.chameleonbusinessstudio.scheduling;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * @ElementCollection cannot be used inside an @Embeddable that is also 
 * contained within an @ElementCollection, so this is unfortunate
 * 
 * @author Matt Crow <mattcrow19@gmail.com>
 */
@Embeddable
public class AppointmentTagEntity {
    @Setter
    @Getter
    @Column(nullable=false)
    private String name;
    
    @Setter
    @Getter
    @Column(nullable=false)
    private String value;
    
    public AppointmentTagEntity(){
        
    }
}
