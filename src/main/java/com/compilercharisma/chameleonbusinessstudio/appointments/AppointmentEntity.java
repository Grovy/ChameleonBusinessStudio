package com.compilercharisma.chameleonbusinessstudio.appointments;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * This class contains all the raw data that represents an appointment
 * don't put business logic in this class, 
 * as some methods might be called by JPA,
 * and business logic belongs in the service.
 * 
 * Using Collections with OnDelete: https://stackoverflow.com/a/62848296
 * 
 * @author Matt Crow <mattcrow19@gmail.com>
 */
@Entity
@Data
@RequiredArgsConstructor
public class AppointmentEntity implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="appt_id")
    private int id;

    @Column(nullable=false)
    private LocalDateTime startTime;

    @Column(nullable=false)
    private LocalDateTime endTime;

    @Column(nullable=false)
    private String title;

    @Column(nullable=false)
    private String location;

    @Column(nullable=false)
    private String description = "";

    @Column(nullable=false)
    private String restrictions = "";

    @Column(nullable=false)
    private boolean isCanceled = false;

    @Column(nullable=false)
    private int totalSlots = 1; // default to one person per appointment

    @Column(nullable=false)
    @Embedded
    @ElementCollection
    @CollectionTable(joinColumns = @JoinColumn(name = "appt_id"))
    @JoinColumn(name = "appt_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<AppointmentTagEntity> tags = new HashSet<>();

    @Column(nullable=false)
    @ElementCollection
    @CollectionTable(joinColumns = @JoinColumn(name = "appt_id"))
    @JoinColumn(name = "appt_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<String> registeredUsers = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AppointmentEntity that = (AppointmentEntity) o;
        return false;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
