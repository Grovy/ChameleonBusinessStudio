package com.compilercharisma.chameleonbusinessstudio.service;

import java.time.LocalDate;
import java.util.List;

import com.compilercharisma.chameleonbusinessstudio.dto.AppointmentResponse;
import com.compilercharisma.chameleonbusinessstudio.exception.ExternalServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.compilercharisma.chameleonbusinessstudio.dto.Appointment;
import com.compilercharisma.chameleonbusinessstudio.repository.AppointmentRepository;
import com.compilercharisma.chameleonbusinessstudio.validators.AppointmentValidator;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * https://stackoverflow.com/q/56241495
 * if we filter the Page, then convert to a List, then back to a Page, this
 * loses a lot of info, and so the _links attribute of the JSON response
 * cannot be properly generated. Therefore, we must use the Criteria API to
 * filter
 *
 * @author Matt Crow <mattcrow19@gmail.com>
 */
@Service
@Slf4j
public class AppointmentService {

    private final UserService userService;
    private final AppointmentRepository appointmentRepository;
    private final AppointmentValidator validator;

    @Autowired
    public AppointmentService(
            AppointmentRepository appointmentRepository,
            UserService userService, AppointmentValidator validator) {
        this.appointmentRepository = appointmentRepository;
        this.userService = userService;
        this.validator = validator;
    }

    public Mono<Appointment> createAppointment(Appointment appt) {
        return appointmentRepository.createAppointment(appt);
    }

    /**
     * Gets the appointment with the given ID. Throws an exception if no such
     * appointment exists.
     *
     * @param appointmentId the ID of the appointment to get
     * @return an mono containing the appointment, if it exists
     */
    public Mono<Appointment> bookAppointmentForUser(String appointmentId, String email) {
        return userService.isRegistered(email)
                .filter(Boolean.TRUE::equals)
                .switchIfEmpty(Mono.error(new ExternalServiceException(
                        "User with email [%s] already exists".formatted(email), HttpStatus.CONFLICT)))
                .flatMap(a -> appointmentRepository.getAppointmentById(appointmentId))
                .flatMap(apt -> bookEmail(apt, email));
    }

    /**
     * Get Appointment by their Id
     * @param appointmentId the appointment id
     * @return Mono of {@link Appointment}
     */
    public Mono<Appointment> getAppointmentById(String appointmentId) {
        return appointmentRepository.getAppointmentById(appointmentId);
    }

    /**
     * Method returns all appointments in Vendia
     *
     * @return a list of all the Vendia appointments
     */
    public Mono<List<Appointment>> getAllAppointments() {
        return appointmentRepository.findAllAppointments()
                .map(AppointmentResponse::getAppointments);
    }

    /**
     * retrieves the appointments for which the given email is a participant
     *
     * @param email    a user's email
     * @param pageable the pagination to apply
     * @return a page of the user's appointments
     */
    public Mono<Page<Appointment>> getAppointmentsForUser(
            String email,
            Pageable pageable
    ) {
        return appointmentRepository.getAppointmentsForUser(email, pageable);
    }

    public Mono<Page<Appointment>> getAvailableAppointments(
            LocalDate startTime,
            LocalDate endTime,
            Pageable page
    ) {
        return appointmentRepository.getAvailableAppointments(startTime, endTime, page);
    }

    /**
     * Updates the given appointment in Vendia.
     *
     * @param appt the appointment in Vendia
     * @return the updated appointment
     */
    public Mono<Appointment> updateAppointment(Appointment appt) {
        validateAppointment(appt);
        log.info("Updating appointment with id [{}]", appt.get_id());

        // this might also send notifications to users subscribed to the appointment
        return appointmentRepository.updateAppointment(appt);
    }

    public Mono<Appointment> deleteAppointment(Appointment appt) {
        // this might also send notifications to users subscribed to the appointment
        return appointmentRepository.deleteAppointment(appt.get_id())
                .then(Mono.just(appt));
    }

    /**
     * Cancels the given appointment. This operation is idempotent.
     *
     * @param appt the appointment to cancel.
     * @return a mono containing the canceled appointment, or nothing if an
     * error occurs.
     */
    public Mono<Appointment> cancelAppointment(Appointment appt) {
        validateAppointment(appt);
        if (appt.getCancelled()) {
            // already canceled. Don't waste Vendia's time
            return Mono.just(appt);
        }

        appt.setCancelled(true);
        return updateAppointment(appt);
    }

    /**
     * Checks if the given appointment is valid, throwing an
     * InvalidAppointmentException if it isn't.
     *
     * @param e the appointment to validate
     */
    public void validateAppointment(Appointment e) {
        validator.validate(e);
    }

    /**
     * Adds the given email to the appointment as a participant, if they are not
     * already in the list of participants.
     *
     * @param appt  the appointment to register a user to
     * @param email the participant's email
     * @return a mono containing the appointment, with the user as a participant
     */
    public Mono<Appointment> bookEmail(Appointment appt, String email) {
        validateAppointment(appt);

        if (appt.getParticipants().contains(email)) {
            return Mono.just(appt);
        }
        if (!isSlotAvailable(appt)) {
            throw new UnsupportedOperationException(String.format("Cannot register [%s] in appointment [%s]: no slots available", email, appt.get_id()));
        }
        var participants = appt.getParticipants();
        participants.add(email);

        return appointmentRepository.updateAppointment(appt);
    }

    /**
     * Removes the given email from the given appointment's list of participants
     * This method is idempotent
     *
     * @param appt  the appointment to remove the email from
     * @param email the user's email
     * @return a mono containing the updated appointment
     */
    public Mono<Appointment> unbookEmail(Appointment appt, String email) {
        validateAppointment(appt);
        if (!appt.getParticipants().contains(email)) {
            // already unbooked. Don't waste Vendia's time
            return Mono.just(appt);
        }
        appt.getParticipants().remove(email);
        return updateAppointment(appt);
    }

    /**
     * Checks if the given appointment can accept more users.
     *
     * @param appt the appointment to check
     * @return whether or not the appointment has any slots available
     */
    public boolean isSlotAvailable(Appointment appt) {
        return appt.getTotalSlots() > appt.getParticipants().size();
    }
}
