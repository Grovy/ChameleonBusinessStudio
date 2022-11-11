package com.compilercharisma.chameleonbusinessstudio.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.compilercharisma.chameleonbusinessstudio.dto.Appointment;
import com.compilercharisma.chameleonbusinessstudio.dto.AppointmentResponse;
import com.compilercharisma.chameleonbusinessstudio.exception.UserNotRegisteredException;
import com.compilercharisma.chameleonbusinessstudio.repository.AppointmentRepository;
import com.compilercharisma.chameleonbusinessstudio.validators.AppointmentValidator;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

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

    /**
     * Method that creates an appointment in Vendia
     *
     * @param appt The appointment that is created
     * @return {@link Mono} of {@link Appointment}
     */
    public Mono<Appointment> createAppointment(Appointment appt) {
        return appointmentRepository.createAppointment(appt);
    }

    /**
     * Book the user with the given email for the appointment with the given ID,
     * if the appointment is available.
     *
     * @param appointmentId the ID of the appointment to book
     * @param email         the email of the participant to book
     * @return {@link Mono} of an {@link Appointment}
     */
    public Mono<Appointment> bookAppointmentForUser(String appointmentId, String email) {
        return userService.isRegistered(email)
                .filter(Boolean.TRUE::equals)
                .switchIfEmpty(Mono.error(new UserNotRegisteredException(
                        "User with email [%s] does not exist".formatted(email))))
                .flatMap(u -> userService.getUser(email))
                .flatMap(u -> userService.addNewAppointmentForUser(u.get_id(), appointmentId))
                .doOnSuccess(u -> log.info("Appointment [{}] was added to user with email [{}]", appointmentId, email))
                .flatMap(a -> appointmentRepository.getAppointmentById(appointmentId))
                .flatMap(apt -> bookEmailInAppointment(apt, email));
    }

    /**
     * Unbook the user with the given email for the appointment with the given Id,
     * if the appointment is available
     * @param appointmentId the ID of the appointment to unbook
     * @param email the email of the participant to unbook
     * @return {@link Mono} of an {@link Appointment}
     */
    public Mono<Appointment> unBookAppointmentForUser(String appointmentId, String email) {
        return userService.isRegistered(email)
                .filter(Boolean.TRUE::equals)
                .switchIfEmpty(Mono.error(new UserNotRegisteredException(
                        "User with email [%s] does not exist".formatted(email))))
                .flatMap(u -> userService.getUser(email))
                .flatMap(u -> userService.removeAppointmentForUser(u.get_id(), appointmentId))
                .doOnSuccess(u -> log.info("Appointment [{}] was removed for user with email [{}]", appointmentId, email))
                .flatMap(a -> appointmentRepository.getAppointmentById(appointmentId))
                .flatMap(apt -> unbookEmailInAppointment(apt, email));
    }

    /**
     * Get Appointment by their Id
     *
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

    /**
     * Get available appointments in a date range
     *
     * @param startDate start date of the range
     * @param endDate   end date of the range
     * @param page      the page
     * @return {@link Mono} of an {@link Appointment}
     */
    public Mono<Page<Appointment>> getAvailableAppointments(
            LocalDate startDate,
            LocalDate endDate,
            Pageable page
    ) {
        return appointmentRepository.getAvailableAppointments(startDate, endDate, page);
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

    /**
     * Deletes the given appointment in Vendia
     *
     * @param appt the appointment in Vendia
     * @return the deleted appointment
     */
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
    public Mono<Appointment> bookEmailInAppointment(Appointment appt, String email) {
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
    public Mono<Appointment> unbookEmailInAppointment(Appointment appt, String email) {
        validateAppointment(appt);
        if (!appt.getParticipants().contains(email)) {
            // already unbooked. Don't waste Vendia's time
            return Mono.just(appt);
        }
        appt.getParticipants().remove(email);
        return appointmentRepository.updateAppointment(appt);
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
