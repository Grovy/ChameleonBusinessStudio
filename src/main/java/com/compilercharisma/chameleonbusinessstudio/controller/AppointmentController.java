package com.compilercharisma.chameleonbusinessstudio.controller;

import java.net.URI;
import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import com.compilercharisma.chameleonbusinessstudio.service.AppointmentService;
import com.compilercharisma.chameleonbusinessstudio.service.AuthenticationService;
import com.compilercharisma.chameleonbusinessstudio.service.UserService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * This controller handles routes associated with appointments.
 * 
 * @author Ariel Camargo
 * @author Matt Crow <mattcrow19@gmail.com>
 */
@RestController
@RequestMapping("/api/v1/appointments")
@Slf4j
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
     * GET /api/v1/appointments/mine
     * 
     * Gets appointments the currently logged in user is booked for, sorted and
     * filtered by the given pagination options.
     * 
     * @param token the current context's authentication token
     * @param pageable pagination options
     * @return a mono containing some of the user's booked appointments
     */
    @GetMapping("mine")
    public Mono<ResponseEntity<Page<Appointment>>> myAppointments(
            Authentication token,
            Pageable pageable
    ){
        var email = authentication.getEmailFrom(token);
        return appointments
            .getAppointmentsForUser(email, pageable)
            .map(page -> ResponseEntity.ok(page));
    }

    /** 
     * @param days the number of days to check for. A value of 3 means 
     *  available appointments occuring within the next 3 days.
     * @param page size={size}&page={page}&sort={attr},{by}
     *  - page is 0-indexed
     *  - attr is the name of one of AppointmentEntity's attributes
     *  - by is either asc or desc
     *
     * @return a response containing a page of available appintments
     */
    @GetMapping(path="available")
    public Mono<ResponseEntity<Page<Appointment>>> getAvailableInDays(
            @RequestParam(required=false, defaultValue="30") int days,
            Pageable page
    ){
        LocalDate now = LocalDate.now();
        LocalDate later = now.plusDays(days);
        return appointments.getAvailableAppointments(now, later, page)
            .map(appts -> ResponseEntity.ok(appts));
    }

    /**
     * Creates and stores a new appointment, if it is valid.
     * Note that using RequestBody means it works as an API endpoint, but might 
     * not handle a form submission.
     * 
     * @param root contains the URI component of the app root, such as
     *  http://localhost:8080
     * @param token the current context's authentication token
     * @param appointment the appointment to create
     * @return a 201 Created At response if successful
     */
    @PostMapping
    public Mono<ResponseEntity<Appointment>> createAppointment(
            UriComponentsBuilder root,
            Authentication token,
            @RequestBody Appointment appointment
    ){
        appointments.validateAppointment(appointment);

        log.info("Creating an appointment", appointment);

        URI at = root // relative to application root
            .pathSegment("api", "v1", "appointments")
            .build()
            .toUri();
        
        return appointments.createAppointment(appointment)
                .map(r -> ResponseEntity.created(at).body(appointment))
                .doOnNext(u -> log.info("Appointment created in Vendia share!"))
                .onErrorMap(e -> new Exception("Error creating appointment in Vendia"));
    }

    /**
     * Updates or creates the given appointment based upon its ID, if allowed.
     * 
     * @param token the current context's authentication token
     * @param appointment the appointment to create or update
     * @return a response containing the updated or created appointment
     */
    @PutMapping
    public Mono<ResponseEntity<Appointment>> updateVendiaAppointment(
            Authentication token,
            @RequestBody Appointment appointment
    ){
        appointments.validateAppointment(appointment);
        log.info("Updating an appointment", appointment);

        var id = appointment.get_id();
        var action = (id == "" || id == null)
            ? appointments.createAppointment(appointment)
            : appointments.updateAppointment(appointment);

        return action
                .map(r -> ResponseEntity.status(HttpStatus.ACCEPTED).body(appointment))
                .doOnNext(u -> log.info("Appointment updated in Vendia share!"))
                .onErrorMap(e -> new Exception("Error updating appointment in Vendia"));
    }

    /**
     * This deletes the appointment called in Vendia
     * @param appointment The appointment you want to delete
     * @return The (@link DeletionResponse} of the appointment being updated.
     */
    @DeleteMapping
    public Mono<ResponseEntity<Appointment>> deleteVendiaAppointment(
            @RequestBody Appointment appointment
    ){
        log.info("Deleting an appointment");
        return appointments.deleteAppointment(appointment)
                .map(r -> new ResponseEntity<>(r, HttpStatus.OK))
                .doOnNext(u -> log.info("Appointment deleted in Vendia share!"))
                .onErrorMap(e -> new Exception("Error deleting appointment in Vendia"));
    }

    /**
     * POST /api/v1/appointments/123?email=foo.bar@baz.qux
     * Books a user for an appointment.
     * 
     * @param token the current context's authentication token
     * @param appointmentId the ID of the appointment to book the given email 
     *  for
     * @param email the email of the user to book an appointment for
     * @return either an OK response with the updated appointment as its body, 
     *  or one of several error responses
     */
    @PostMapping("/book-them/{appointment-id}")
    public Mono<ResponseEntity<Appointment>> bookThem(
            Authentication token,
            @PathVariable("appointment-id") String appointmentId,
            @RequestParam("email") String email
    ){
        if(email == null){
            return Mono.error(new IllegalArgumentException("Email cannot be null"));
        }
        if(!isValidEmail(email)){
            return Mono.error(new IllegalArgumentException("Invalid email: " + email));
        }

        var getAppt = appointments.getAppointmentById(appointmentId);
        var checkUser = users.isRegistered(email);
        
        return Mono.zip(getAppt, checkUser)
            .flatMap(tuple -> {
                var appt = tuple.getT1();
                var isUserRegistered = tuple.getT2();
                if(!isUserRegistered){
                    throw new IllegalArgumentException("Email is not registered as a user: " + email);
                }
                if(!appointments.isSlotAvailable(appt)){
                    throw new UnsupportedOperationException("Appointment is full; it cannot have any more participants");
                }
                return appointments.registerUser(appt, email);
            })
            .map(appt -> ResponseEntity.ok(appt));
    }

    /**
     * Books the currently logged in user for the given appointment.
     * 
     * @param token the current context's authentication token
     * @param appointmentId the ID of theappointment to book the logged-in user 
     *  for.
     * @return either a bad request or an OK response containing the updated 
     *  appointment
     */
    @PostMapping("/book-me/{appointment-id}")
    public Mono<ResponseEntity<Appointment>> bookMe(
            Authentication token,
            @PathVariable("appointment-id") String appointmentId
    ){
        var email = authentication.getEmailFrom(token);

        return appointments.getAppointmentById(appointmentId)
            .flatMap(appt -> {
                if(!appointments.isSlotAvailable(appt)){
                    return Mono.error(new UnsupportedOperationException("Appointment is full; it cannot have any more participants"));
                }
                return appointments.registerUser(appt, email);
            })
            .map(appt -> ResponseEntity.ok(appt));
    }
    
    private boolean isValidEmail(String email){
        var isValid = true;
        // how to validate properly?
        return isValid;
    }
}