package com.compilercharisma.chameleonbusinessstudio.entity.appointment;

import com.compilercharisma.chameleonbusinessstudio.entity.AppointmentEntity;
import org.springframework.stereotype.Service;

/**
 * Validates AppointmentEntitys to ensure they have been properly created.
 * 
 * @author Matt Crow
 */
@Service
public class AppointmentValidator {
    public boolean isValid(AppointmentEntity e){
        return e.getStartTime() != null
                && e.getEndTime() != null
                && e.getStartTime().isBefore(e.getEndTime())
                && e.getLocation() != null
                && !e.getLocation().trim().isEmpty()
                && e.getDescription() != null
                && e.getTotalSlots() > 0
                && !e.getTags().stream().anyMatch(tag -> tag.getName() == null || tag.getName().trim().isEmpty());
    }
}
