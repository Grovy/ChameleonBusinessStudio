package com.compilercharisma.chameleonbusinessstudio.controller;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/appointments")
@Slf4j
public class AppointmentController {

    private final AppointmentService appointments;
    private final AuthenticationService authentication;

    public AppointmentController(
            AppointmentService appointments,
            AuthenticationService authentication
    ) {
        this.appointments = appointments;
        this.authentication = authentication;
    }

    /**
     * Get all appointments in Vendia
     *
     * @return {@link Mono} of List of {@link Appointment}s
     */
    @GetMapping
    public Mono<ResponseEntity<List<Appointment>>> getAllAppointments() {
        log.info("Retrieving all user appointments from Vendia...");
        return appointments.getAllAppointments()
                .map(ResponseEntity::ok)
                .doOnNext(r -> log.info("Finished retrieving all user appointments from Vendia!"))
                .doOnError(e -> log.error("Could not retrieve user appointments from Vendia"));
    }

    /**
     * Get an Appointment by their id
     *
     * @param apptId the appointment id
     * @return {@link Mono} of an {@link Appointment}
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Appointment>> getAppointmentById(
            @PathVariable("id") String apptId
    ) {
        return appointments.getAppointmentById(apptId)
                .map(ResponseEntity::ok);
    }

    /**
     * GET /api/v1/appointments/mine
     * <p>
     * Gets appointments the currently logged in user is booked for, sorted and
     * filtered by the given pagination options.
     *
     * @param token    the current context's authentication token
     * @param pageable pagination options
     * @return a mono containing some of the user's booked appointments
     */
    @GetMapping("mine")
    public Mono<ResponseEntity<Page<Appointment>>> myAppointments(
            Authentication token,
            Pageable pageable
    ) {
        var email = authentication.getEmailFrom(token);
        return appointments
                .getAppointmentsForUser(email, pageable)
                .map(ResponseEntity::ok);
    }

    /**
     * @param days the number of days to check for. A value of 3 means
     *             available appointments occuring within the next 3 days.
     * @param page size={size}&page={page}&sort={attr},{by}
     *             - page is 0-indexed
     *             - attr is the name of one of AppointmentEntity's attributes
     *             - by is either asc or desc
     * @return a response containing a page of available appintments
     */
    @GetMapping(path = "available")
    public Mono<ResponseEntity<Page<Appointment>>> getAvailableInDays(
            @RequestParam(required = false, defaultValue = "30") int days,
            Pageable page
    ) {
        LocalDate now = LocalDate.now();
        LocalDate later = now.plusDays(days);
        return appointments.getAvailableAppointments(now, later, page)
                .map(ResponseEntity::ok);
    }

    /**
     * Creates and stores a new appointment, if it is valid.
     * Note that using RequestBody means it works as an API endpoint, but might
     * not handle a form submission.
     *
     * @param root        contains the URI component of the app root, such as
     *                    http://localhost:8080
     * @param token       the current context's authentication token
     * @param appointment the appointment to create
     * @return a 201 Created At response if successful
     */
    @PostMapping
    public Mono<ResponseEntity<Appointment>> createAppointment(
            UriComponentsBuilder root,
            Authentication token,
            @RequestBody Appointment appointment
    ) {
        appointments.validateAppointment(appointment);

        log.info("Creating an appointment");
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
     * POST /api/v1/appointments/123?email=foo.bar@baz.qux
     * Books a user for an appointment.
     *
     * @param appointmentId the ID of the appointment to book the given email
     * @param email         the email of the user to book an appointment for
     * @return either an OK response with the updated appointment as its body,
     * or one of several error responses
     */
    @PostMapping("/book-them/{appointment-id}")
    public Mono<ResponseEntity<Appointment>> bookThem(
            @PathVariable("appointment-id") String appointmentId,
            @RequestParam("email") String email) {
        if (email == null) {
            return Mono.error(new IllegalArgumentException("Email cannot be null"));
        }
        if (!isValidEmail(email)) {
            return Mono.error(new IllegalArgumentException("Invalid email: " + email));
        }

        return appointments.bookAppointmentForUser(appointmentId, email)
                .map(ResponseEntity::ok);
    }

    @PostMapping("/unbook-them/{appointment-id}")
    public Mono<ResponseEntity<Appointment>> unbookThem(
            @PathVariable("appointment-id") String appointmentId,
            @RequestParam("email") String email
    ) {
        return appointments.unBookAppointmentForUser(appointmentId, email)
                .map(ResponseEntity::ok);
    }

    /**
     * Books the currently logged in user for the given appointment.
     *
     * @param token         the current context's authentication token
     * @param appointmentId the ID of theappointment to book the logged-in user
     *                      for.
     * @return either a bad request or an OK response containing the updated
     * appointment
     */
    @PostMapping("/book-me/{id}")
    public Mono<ResponseEntity<Appointment>> bookMe(
            Authentication token,
            @PathVariable("id") String appointmentId
    ) {
        var email = authentication.getEmailFrom(token);

        return appointments.bookAppointmentForUser(appointmentId, email)
                .map(ResponseEntity::ok);
    }

    /**
     * Unbooks the currently logged in user from an appointment in Vendia
     *
     * @param token         the OAuth2 token of the currently logged in user
     * @param appointmentId the appointment to be unbooked for
     * @return {@link Mono} of an {@link Appointment}
     */
    @PostMapping("/unbook-me/{id}")
    public Mono<ResponseEntity<Appointment>> unbookMe(
            Authentication token,
            @PathVariable("id") String appointmentId
    ) {
        var email = authentication.getEmailFrom(token);
        return appointments.unBookAppointmentForUser(appointmentId, email)
                .map(ResponseEntity::ok);
    }

    /**
     * Cancels the appointment with the given ID, if it exists.
     * Throws an exception if no such appointment exists.
     *
     * @param id the ID of the appointment to cancel
     * @return a response containing the status of the request after processing
     */
    @PostMapping("/cancel/{id}")
    public Mono<ResponseEntity<Appointment>> cancelAppointmentById(
            @PathVariable String id
    ) {
        return appointments.getAppointmentById(id)
                .flatMap(appointments::cancelAppointment)
                .map(ResponseEntity::ok);
    }

    /**
     * Updates the given appointment.
     * Throws an exception if the appointment is not yet stored in Vendia.
     *
     * @param token       the current context's authentication token
     * @param appointment the appointment to create or update
     * @return a response containing the updated or created appointment
     */
    @PutMapping
    public Mono<ResponseEntity<Appointment>> updateAppointment(
            Authentication token,
            @RequestBody Appointment appointment
    ) {
        return appointments.updateAppointment(appointment)
                .map(r -> ResponseEntity.ok(appointment));
    }

    /**
     * This deletes the appointment called in Vendia
     *
     * @param appointment The appointment you want to delete
     * @return The (@link DeletionResponse} of the appointment being updated.
     */
    @DeleteMapping
    public Mono<ResponseEntity<Appointment>> deleteVendiaAppointment(
            @RequestBody Appointment appointment
    ) {
        log.info("Deleting an appointment");
        return appointments.deleteAppointment(appointment)
                .map(ResponseEntity::ok)
                .doOnNext(u -> log.info("Appointment deleted in Vendia share!"))
                .onErrorMap(e -> new Exception("Error deleting appointment in Vendia"));
    }

    private boolean isValidEmail(String email) {
        var isValid = true;
        // how to validate properly?
        return isValid;
    }
}