package com.compilercharisma.chameleonbusinessstudio.service;


import com.compilercharisma.chameleonbusinessstudio.dto.Appointment;
import com.compilercharisma.chameleonbusinessstudio.dto.AppointmentResponse;
import com.compilercharisma.chameleonbusinessstudio.dto.DeletionResponse;
import com.compilercharisma.chameleonbusinessstudio.repository.AppointmentRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * @author Ariel Camargo
 */
@Service
public class AppointmentServicev2 {
    private final AppointmentRepository appointmentRepository;

    public AppointmentServicev2(AppointmentRepository appointmentRepository){
        this.appointmentRepository = appointmentRepository;
    }

    /**
     * Method returns all appointments in Vendia
     *
     * @return {@link AppointmentResponse}
     */
    public Mono<AppointmentResponse> getAllAppointments() {
        return appointmentRepository.findAllAppointments();
    }

    /**
     * This creates an appointment in Vendia Share
     * @param appointment We give the appointment that we want to make.
     * @return {@link Appointment}
     */
    public Mono<Appointment> createAppointment(Appointment appointment)
    {
        return Mono.just(appointment).flatMap(u -> appointmentRepository.createAppointment(appointment));
    }

    /**
     * This updates an exisitng appointment in Vendia share.
     * @param appointment
     * @return
     */
    public Mono<Appointment> updateAppointment(Appointment appointment)
    {
        return Mono.just(appointment).flatMap(u -> appointmentRepository.updateAppointment(appointment));
    }

    /**
     * This deletes an exisiting appointment in Vendia share.
     * @param appointment Appointment that will be deleted
     * @return
     */
    public Mono<DeletionResponse> deleteAppointment(Appointment appointment)
    {
        return Mono.just(appointment).flatMap(u -> appointmentRepository.deleteAppointment(appointment.get_id()));
    }
}
