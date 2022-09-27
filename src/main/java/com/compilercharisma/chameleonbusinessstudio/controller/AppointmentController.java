package com.compilercharisma.chameleonbusinessstudio.controller;

import com.compilercharisma.chameleonbusinessstudio.service.AuthenticationService;

import reactor.core.publisher.Mono;

import com.compilercharisma.chameleonbusinessstudio.entity.AppointmentEntity;
import com.compilercharisma.chameleonbusinessstudio.entity.appointment.AppointmentModelAssembler;
import com.compilercharisma.chameleonbusinessstudio.entity.user.AbstractUser;
import com.compilercharisma.chameleonbusinessstudio.entity.user.Role;
import com.compilercharisma.chameleonbusinessstudio.service.AppointmentService;

import java.net.URI;
import java.util.HashSet;

import org.springframework.data.domain.*;
import org.springframework.hateoas.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * This controller handles routes associated with appointments.
 * 
 * @author Matt Crow <mattcrow19@gmail.com>
 */
@RestController
@RequestMapping("/api/v1/appointments")
public class AppointmentController {

    private final AppointmentService appointments;
    private final AuthenticationService authentication;
    //private final PagedResourcesAssembler<AppointmentEntity> asm;
    //private final AppointmentModelAssembler modelAssembler;

    public AppointmentController(
            AppointmentService appointments,
            AuthenticationService authentication,
            //PagedResourcesAssembler<AppointmentEntity> asm,
            AppointmentModelAssembler modelAssembler){
        this.appointments = appointments;
        this.authentication = authentication;
        //this.asm = asm;
        //this.modelAssembler = modelAssembler;
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
    public Mono<ResponseEntity<PagedModel<EntityModel<AppointmentEntity>>>> getAvailableInDays(
            @RequestParam(required=false, defaultValue="30") int days,
            Pageable page
    ){
        throw new UnsupportedOperationException("Need to reimplement paging");
        /*
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime later = now.plusDays(days);

        Page<AppointmentEntity> entities = appointments.getAvailableAppointments(now, later, page);

        return ResponseEntity
                .ok()
                .contentType(MediaTypes.HAL_JSON)
                .body(asm.toModel(entities, modelAssembler));
         */
    }
    
    /**
     * Creates and stores a new appointment, if it is valid.
     * Note that using RequestBody means it works as an API endpoint, but might 
     * not handle a form submission.
     * 
     * @param appointment the appointment to create
     * @return a 201 Created At response if successful
     */
    @PostMapping
    public Mono<ResponseEntity<?>> create(@RequestBody AppointmentEntity appointment){
        if(!appointments.isAppointmentValid(appointment) || appointment.getId() != 0){
            return Mono.just(ResponseEntity.badRequest().body(appointment));
        }
        
        AbstractUser postedBy = authentication.getLoggedInUser();
        if(!isRoleAllowedToCreateAppointments(postedBy.getAsEntity().getRole())){
            return Mono.just(ResponseEntity.status(HttpStatus.FORBIDDEN).build());
        }
        
        appointments.createAppointment(appointment);
        
        URI at = ServletUriComponentsBuilder
                .fromCurrentContextPath() // relative to application root
                .pathSegment("api", "v1", "appointments")
                .build()
                .toUri();
        
        return Mono.just(ResponseEntity.created(at).build());
    }
    
    @PutMapping
    public Mono<ResponseEntity<?>> update(@RequestBody AppointmentEntity appointment){
        if(!appointments.isAppointmentValid(appointment)){
            return Mono.just(ResponseEntity.badRequest().body(appointment));
        }
        
        AbstractUser postedBy = authentication.getLoggedInUser();
        if(!isRoleAllowedToCreateAppointments(postedBy.getAsEntity().getRole())){
            return Mono.just(ResponseEntity.status(HttpStatus.FORBIDDEN).build());
        }
        
        if(appointment.getId() == 0){ // new appointment
            appointments.createAppointment(appointment);
        } else { // updating an old appointment
            appointments.updateAppointment(appointment);
        }
        
        return Mono.just(ResponseEntity.status(HttpStatus.ACCEPTED).build());
    }

    /**
     * Books the currently logged in user for the given appointment.
     * @param appointment the appointment to book the logged-in user for.
     * @return either a bad request or an OK response containing the updated 
     *  appointment
     */
    @PostMapping("/book-me")
    public Mono<ResponseEntity<AppointmentEntity>> bookMe(@RequestBody AppointmentEntity appointment){
        if(!appointments.isAppointmentValid(appointment)){
            return Mono.just(ResponseEntity.badRequest().body(appointment));
        }

        var postedBy = authentication.getLoggedInUser().getAsEntity();
        if(!appointments.isUserRegistered(appointment, postedBy.getEmail())){
            appointments.registerUser(appointment, postedBy.getEmail());
        }

        return Mono.just(ResponseEntity.ok().body(appointment));
    }
    
    private boolean isRoleAllowedToCreateAppointments(String role){
        HashSet<String> hs = new HashSet<>();
        hs.add(Role.ADMIN);
        hs.add(Role.ORGANIZER);
        hs.add(Role.TALENT);
        return hs.contains(role);
    }
}
