package com.compilercharisma.chameleonbusinessstudio.entity.appointment;

import org.springframework.data.domain.Page;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import com.compilercharisma.chameleonbusinessstudio.entity.AppointmentEntity;

public class AppointmentResourceAssembler {

    public static PagedModel<EntityModel<AppointmentEntity>> toModel(Page<AppointmentEntity> appointments){
        var asm = new AppointmentModelAssembler();
        var data = asm.toCollectionModel(appointments);
        var pm = PagedModel.of(data.getContent(), new PagedModel.PageMetadata(
            appointments.getSize(),
            appointments.getNumber(), 
            appointments.getTotalPages()
        ), data.getLinks());
        return pm;
    }
}
