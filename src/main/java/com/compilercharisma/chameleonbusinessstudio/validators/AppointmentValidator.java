package com.compilercharisma.chameleonbusinessstudio.validators;

import com.compilercharisma.chameleonbusinessstudio.dto.Appointment;
import com.compilercharisma.chameleonbusinessstudio.exception.InvalidAppointmentException;

import org.springframework.stereotype.Service;

/**
 * Validates AppointmentEntities to ensure they have been properly created.
 *
 * @author Matt Crow
 */
@Service
public class AppointmentValidator {

    /**
     * Checks if the given appointment is valid, and throws an exception upon
     * encountering any invalid fields.
     *
     * @param e the appointment to validate
     * @throws InvalidAppointmentException if the appointment contains any
     *                                     invalid fields
     */
    public void validate(Appointment e) throws InvalidAppointmentException {
        if (e == null) {
            throw new InvalidAppointmentException(e, "Appointment cannot be null");
        }

        if (e.getTitle() == null) {
            throw new InvalidAppointmentException(e, "Appointment must have a title");
        }
        if (e.getTitle().trim().isEmpty()) {
            throw new InvalidAppointmentException(e, "Appointment title must not be empty");
        }

        if (e.getStartTime() == null) {
            throw new InvalidAppointmentException(e, "Appointment must have a start time");
        }
        if (e.getEndTime() == null) {
            throw new InvalidAppointmentException(e, "Appointment must have an end time");
        }
        if (!e.getStartTime().isBefore(e.getEndTime())) {
            throw new InvalidAppointmentException(e, "Appointment start time must come before its end time");
        }

        if (e.getLocation() == null) {
            throw new InvalidAppointmentException(e, "Appointment must have a location");
        }
        if (e.getLocation().trim().isEmpty()) {
            throw new InvalidAppointmentException(e, "Appointment location cannot be empty");
        }

        if (e.getDescription() == null) {
            throw new InvalidAppointmentException(e, "Appointment must have a description");
        }

        if (e.getTotalSlots() <= 0) {
            throw new InvalidAppointmentException(e, "Appointment must have a positive number of slots");
        }
    }

}
