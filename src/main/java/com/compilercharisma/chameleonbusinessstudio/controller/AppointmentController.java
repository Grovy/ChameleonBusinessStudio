package com.compilercharisma.chameleonbusinessstudio.controller;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashSet;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.compilercharisma.chameleonbusinessstudio.dto.Appointment;
import com.compilercharisma.chameleonbusinessstudio.entity.AppointmentEntity;
import com.compilercharisma.chameleonbusinessstudio.entity.user.Role;
import com.compilercharisma.chameleonbusinessstudio.service.AppointmentService;
import com.compilercharisma.chameleonbusinessstudio.service.AuthenticationService;
import com.compilercharisma.chameleonbusinessstudio.service.UserService;

import reactor.core.publisher.Mono;
import reactor.core.publisher.SynchronousSink;

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

    public AppointmentController(
            AppointmentService appointments,
            AuthenticationService authentication,
            UserService users
    ){
        this.appointments = appointments;
        this.authentication = authentication;
        this.users = users;
    }
    
    /**
     * page is 0-indexed
     *      *  attr is the name of one of AppointmentEntity's attributes
     *      *  by is either asc or desc
     * 
     * @param days the number of days to check for. A value of 3 means 
     *  available appointments occuring within the next 3 days.
     * @param page size={size}&page={page}&sort={attr},{by}
     *
     * @return a response containing a page of available appintments
     */
    @GetMapping(path="available")
    public Mono<ResponseEntity<Page<AppointmentEntity>>> getAvailableInDays(
            @RequestParam(required=false, defaultValue="30") int days,
            Pageable page
    ){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime later = now.plusDays(days);
        
        var appts = appointments.getAvailableAppointments(now, later, page);
        var response = ResponseEntity.ok(appts);
        return Mono.just(response);
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
    public Mono<ResponseEntity<AppointmentEntity>> create(
            UriComponentsBuilder root,
            @RequestBody AppointmentEntity appointment
    ){
        if(!appointments.isAppointmentValid(appointment) || appointment.getId() != 0){
            return Mono.just(ResponseEntity.badRequest().body(appointment));
        }
        
        return authentication
            .getLoggedInUserReactive()
            .handle((user, sink)->{
                var role = user.getRole().getRoleName();
                if(isRoleAllowedToCreateAppointments(role)){
                    sink.next(user);
                } else {
                    sink.error(new AccessDeniedException(String.format("Users with role %s are not allowed to create appointments", role)));
                }
            }).map((whatever)->{
                appointments.createAppointment(appointment);
        
                URI at = root // relative to application root
                    .pathSegment("api", "v1", "appointments")
                    .build()
                    .toUri();
                
                return ResponseEntity.created(at).body(appointment);
            });
    }

    /**
     * GET /api/v1/appointments/mine
     * 
     * Gets appointments the currently logged in user is booked for, sorted and
     * filtered by the given pagination options.
     * 
     * @param pageable pagination options
     * @return a mono containing some of the user's booked appointments
     */
    @GetMapping("mine")
    public Mono<ResponseEntity<Page<Appointment>>> myAppointments(Pageable pageable){       
        return authentication.getLoggedInUserReactive()
            .flatMap(user -> appointments.getAppointmentsForUser(user.getEmail(), pageable))
            .map(page -> ResponseEntity.ok(page));
    }
    
    @PutMapping
    public Mono<ResponseEntity<AppointmentEntity>> update(@RequestBody AppointmentEntity appointment){
        appointments.validateAppointment(appointment);
        
        return authentication
            .getLoggedInUserReactive()
            .handle((user, sink)->{
                var role = user.getRole().getRoleName();
                if(isRoleAllowedToCreateAppointments(role)){
                    sink.next(user);
                } else {
                    sink.error(new AccessDeniedException(String.format("Users with role %s are not allowed to create appointments", role)));
                }
            }).map((whatever)->{
                if(appointment.getId() == 0){ // new appointment
                    appointments.createAppointment(appointment);
                } else { // updating an old appointment
                    appointments.updateAppointment(appointment);
                }
                
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(appointment);
            });
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

        return authentication
            .getLoggedInUserReactive()
            .map(u -> u.getEmail())
            .map(email->{
                if(!appointments.isUserRegistered(appointment, email)){
                    appointments.registerUser(appointment, email);
                }
                return ResponseEntity.ok(appointment);
            });
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

        // Matt hates Reactor. Java needs Async/await!
        return users.isRegistered(email)
            .handle((Boolean isRegistered, SynchronousSink<Boolean> sink)->{
                if(isRegistered){
                    sink.next(isRegistered);
                } else {
                    sink.error(new IllegalArgumentException("Email is not registered as a user: " + email));
                }
            }).handle((Boolean whatever, SynchronousSink<AppointmentEntity> sink) -> {
                var appointmentMaybe = appointments.getAppointmentById(appointmentId);
                if(appointmentMaybe.isEmpty()){
                    sink.error(new IllegalArgumentException("Invalid appointment ID: " + appointmentId));
                } else {
                    sink.next(appointmentMaybe.get());
                }
            }).handle((AppointmentEntity appt, SynchronousSink<AppointmentEntity> sink)->{
                // need to call reactive from inside another reactive, pass down appt
                authentication
                    .getLoggedInUserReactive()
                    .handle((user, userSink)->{
                        // check if logged in user is allowed to book
                        var role = user.getRole().getRoleName();
                        if(isRoleAllowedToBookOtherPeople(role)){
                            sink.next(appt);
                        } else {
                            var msg = String.format(
                                "Users with role \"%s\" are not allowed to book appointments for other users",
                                role
                            );
                            sink.error(new AccessDeniedException(msg));
                        }
                    });
            }).handle((AppointmentEntity appt, SynchronousSink<AppointmentEntity> sink) -> {
                if(appointments.isSlotAvailable(appt)){
                    sink.next(appt);
                } else {
                    sink.error(new UnsupportedOperationException("Appointment is full; it cannot have any more participants"));
                }
            }).map(appt -> {
                if(!appointments.isUserRegistered(appt, email)){
                    appointments.registerUser(appt, email);
                }
        
                return ResponseEntity.ok(appt);
            });
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