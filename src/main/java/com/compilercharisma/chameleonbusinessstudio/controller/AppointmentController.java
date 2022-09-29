package com.compilercharisma.chameleonbusinessstudio.controller;

import com.compilercharisma.chameleonbusinessstudio.service.AuthenticationService;
import com.compilercharisma.chameleonbusinessstudio.service.UserService;

import reactor.core.publisher.Mono;

import com.compilercharisma.chameleonbusinessstudio.entity.AppointmentEntity;
import com.compilercharisma.chameleonbusinessstudio.entity.appointment.AppointmentModelAssembler;
import com.compilercharisma.chameleonbusinessstudio.entity.user.AbstractUser;
import com.compilercharisma.chameleonbusinessstudio.entity.user.Role;
import com.compilercharisma.chameleonbusinessstudio.service.AppointmentService;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.*;
import java.util.HashSet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import java.util.HashSet;

import org.springframework.data.domain.*;
import org.springframework.hateoas.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
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
    private final UserService users;
    //private final PagedResourcesAssembler<AppointmentEntity> asm;
    //private final AppointmentModelAssembler modelAssembler;

    public AppointmentController(
            AppointmentService appointments,
            AuthenticationService authentication,
            UserService users,
            //PagedResourcesAssembler<AppointmentEntity> asm,
            AppointmentModelAssembler modelAssembler){
        this.appointments = appointments;
        this.authentication = authentication;
        this.users = users;
        //this.asm = asm;
        //this.modelAssembler = modelAssembler;
    }
    
    /**
     * page is 0-indexed
     *      *  attr is the name of one of AppointmentEntity's attributes
     *      *  by is either asc or desc
     * 
     * @param days
     * @param page size={size}&page={page}&sort={attr},{by}
     *
     * @return 
     */
//     https://stackoverflow.com/a/63966321
//    @GetMapping(path="available")
//    public ResponseEntity<PagedModel<EntityModel<AppointmentEntity>>> getAvailableInDays(
//            @RequestParam(required=false, defaultValue="30") int days,
//            Pageable page){
//        LocalDateTime now = LocalDateTime.now();
//        LocalDateTime later = now.plusDays(days);
//
//        Page<AppointmentEntity> entities = appointments.getAvailableAppointments(now, later, page);
//
//        return ResponseEntity
//                .ok()
//                .contentType(MediaTypes.HAL_JSON)
//                .body(asm.toModel(entities, modelAssembler));
//    }
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
    public Mono<ResponseEntity<AppointmentEntity>> create(@RequestBody AppointmentEntity appointment){
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
        
        return Mono.just(ResponseEntity.created(at).body(appointment));
    }
    
    @PutMapping
    public Mono<ResponseEntity<AppointmentEntity>> update(@RequestBody AppointmentEntity appointment){
        appointments.validateAppointment(appointment);
        
        AbstractUser postedBy = authentication.getLoggedInUser();
        if(!isRoleAllowedToCreateAppointments(postedBy.getAsEntity().getRole())){
            return Mono.just(ResponseEntity.status(HttpStatus.FORBIDDEN).build());
        }
        
        if(appointment.getId() == 0){ // new appointment
            appointments.createAppointment(appointment);
        } else { // updating an old appointment
            appointments.updateAppointment(appointment);
        }
        
        return Mono.just(ResponseEntity.status(HttpStatus.ACCEPTED).body(appointment));
    }

    /**
     * Books the currently logged in user for the given appointment.
     * @param appointmentId the ID of theappointment to book the logged-in user 
     *  for.
     * @return either a bad request or an OK response containing the updated 
     *  appointment
     */
    @PostMapping("/book-me/{appointment-id}")
    public Mono<ResponseEntity<AppointmentEntity>> bookMe(
            @PathVariable("appointment-id") int appointmentId
    ){
        var appointmentMaybe = appointments.getAppointmentById(appointmentId);
        if(appointmentMaybe.isEmpty()){
            return Mono.error(new IllegalArgumentException("Invalid appointment ID: " + appointmentId));
        }
        
        var appointment = appointmentMaybe.get();
        if(!appointments.isSlotAvailable(appointment)){
            return Mono.error(new UnsupportedOperationException("Appointment is full; it cannot have any more participants"));
        }

        var postedBy = authentication.getLoggedInUser().getAsEntity();
        if(!appointments.isUserRegistered(appointment, postedBy.getEmail())){
            appointments.registerUser(appointment, postedBy.getEmail());
        }

        return Mono.just(ResponseEntity.ok(appointment));
    }

    /**
     * POST /api/v1/appointments/123?email=foo.bar@baz.qux
     * Books a user for an appointment.
     * 
     * @param appointmentId the ID of the appointment to book the given email 
     *  for
     * @param email the email of the user to book an appointment for
     * @return either an OK response with the updated appointment as its body, 
     *  or one of several error responses
     */
    @PostMapping("/book-them/{appointment-id}")
    public Mono<ResponseEntity<AppointmentEntity>> bookThem(
            @PathVariable("appointment-id") int appointmentId,
            @RequestParam("email") String email
    ){
        if(email == null){
            return Mono.error(new IllegalArgumentException("Email cannot be null"));
        }
        if(!isValidEmail(email)){
            return Mono.error(new IllegalArgumentException("Invalid email: " + email));
        }
        if(!users.isRegistered(email)){
            return Mono.error(new IllegalArgumentException("Email is not registered as a user: " + email));
        }

        var appointmentMaybe = appointments.getAppointmentById(appointmentId);
        if(appointmentMaybe.isEmpty()){
            return Mono.error(new IllegalArgumentException("Invalid appointment ID: " + appointmentId));
        }

        // check if logged in user is allowed to book
        var postedBy = authentication.getLoggedInUser().getAsEntity();
        if(!isRoleAllowedToBookOtherPeople(postedBy.getRole())){
            var msg = String.format(
                "Users with role \"%s\" are not allowed to book appointments for other users",
                postedBy.getRole()
            );
            return Mono.error(new AccessDeniedException(msg));
        }
        
        var appointment = appointmentMaybe.get();
        if(!appointments.isSlotAvailable(appointment)){
            return Mono.error(new UnsupportedOperationException("Appointment is full; it cannot have any more participants"));
        }

        if(!appointments.isUserRegistered(appointment, email)){
            appointments.registerUser(appointment, email);
        }

        return Mono.just(ResponseEntity.ok(appointment));
    }
    
    private boolean isRoleAllowedToCreateAppointments(String role){
        var hs = new HashSet<>();
        hs.add(Role.ADMIN);
        hs.add(Role.ORGANIZER);
        hs.add(Role.TALENT);
        return hs.contains(role);
    }

    private boolean isRoleAllowedToBookOtherPeople(String role){
        var hs = new HashSet<>();
        hs.add(Role.ADMIN);
        hs.add(Role.ORGANIZER);
        hs.add(Role.TALENT);
        return hs.contains(role);
    }

    private boolean isValidEmail(String email){
        var isValid = true;
        // how to validate properly?
        return isValid;
    }
}
