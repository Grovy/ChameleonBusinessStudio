package com.compilercharisma.chameleonbusinessstudio.entity.appointment;

import com.compilercharisma.chameleonbusinessstudio.entity.AppointmentEntity;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

/**
 * Used to convert our database entity class to a JSON representation with links
 * to the various endpoints that act upon it.
 * 
 * Currently only used to add "_links" attribute to the response from 
 * /api/v1/appointments/available
 * 
 * See https://spring.io/guides/tutorials/rest/ for more info.
 * 
 * @author Matt Crow <mattcrow19@gmail.com>
 */
@Component
public class AppointmentModelAssembler implements RepresentationModelAssembler<AppointmentEntity, EntityModel<AppointmentEntity>> {
    
    @Override
    public EntityModel<AppointmentEntity> toModel(AppointmentEntity entity) {
        EntityModel<AppointmentEntity> mod = EntityModel.of(entity);
        
        // can add rest links here
        
        return mod;
    }
}
