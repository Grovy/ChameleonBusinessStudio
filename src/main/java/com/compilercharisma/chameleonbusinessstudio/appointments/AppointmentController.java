package com.compilercharisma.chameleonbusinessstudio.appointments;

import java.time.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author Matt Crow <mattcrow19@gmail.com>
 */
@RestController
@RequestMapping("/api/v1/appointments")
public class AppointmentController {
    
    private final AppointmentService serv;
    private final PagedResourcesAssembler<AppointmentEntity> asm;
    private final AppointmentModelAssembler modelAssembler;
    
    @Autowired
    public AppointmentController(
            AppointmentService serv,
            PagedResourcesAssembler<AppointmentEntity> asm,
            AppointmentModelAssembler modelAssembler
    ){
        this.serv = serv;
        this.asm = asm;
        this.modelAssembler = modelAssembler;
    }
    
    /**
     * 
     * @param days
     * @param page size={size}&page={page}&sort={attr},{by}
     *  page is 0-indexed
     *  attr is the name of one of AppointmentEntity's attributes
     *  by is either asc or desc
     * 
     * @return 
     */
    // https://stackoverflow.com/a/63966321
    @GetMapping(path="available")
    public ResponseEntity<PagedModel<EntityModel<AppointmentEntity>>> getAvailableInDays(
            @RequestParam(required=false, defaultValue="30") int days,
            Pageable page
    ){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime later = now.plusDays(days);
        
        Page<AppointmentEntity> entities = serv.getAvailableAppointments(now, later, page);
        
        return ResponseEntity
                .ok()
                .contentType(MediaTypes.HAL_JSON)
                .body(asm.toModel(entities, modelAssembler));
    }
}
