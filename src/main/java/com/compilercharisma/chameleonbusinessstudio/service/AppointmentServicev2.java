package com.compilercharisma.chameleonbusinessstudio.service;


import com.compilercharisma.chameleonbusinessstudio.dto.Appointment;
import com.compilercharisma.chameleonbusinessstudio.repository.AppointmentRepositoryv2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * @author Ariel Camargo
 */
@Service
public class AppointmentServicev2 {
    private final AppointmentRepositoryv2 appointmentRepository;

    public AppointmentServicev2(AppointmentRepositoryv2 appointmentRepository){
        this.appointmentRepository = appointmentRepository;
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

}
