package com.compilercharisma.chameleonbusinessstudio.controller;

import com.compilercharisma.chameleonbusinessstudio.dto.Appointment;

import com.compilercharisma.chameleonbusinessstudio.dto.DeletionResponse;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import com.compilercharisma.chameleonbusinessstudio.service.AppointmentServicev2;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Ariel Camargo
 */
@RestController
@RequestMapping("/api/appointments")
@Slf4j
public class AppointmentControllerv2 {

    private final AppointmentServicev2 appointmentService;

    public AppointmentControllerv2(AppointmentServicev2 appointmentService)
    {
        this.appointmentService = appointmentService;
    }

    /**
     * This creates a new appointment in Vendia
     * @param appointment The appointment that you are creating
     * @return The {@link} of the appointment being created.
     */
    @PostMapping("/createAppointment")
    public Mono<ResponseEntity<Appointment>> createVendiaAppointment(@RequestBody Appointment appointment){
        log.info("Creating an appointment", appointment);
        return appointmentService.createAppointment(appointment)
                .map(r -> new ResponseEntity<>(r, HttpStatus.OK))
                .doOnNext(u -> log.info("Appointment created in Vendia share!"))
                .onErrorMap(e -> new Exception("Error creating appointment in Vendia"));
    }

    /**
     * This updates the appointment called in Vendia
     * @param appointment The appointment you want to update
     * @return The (@link} of the appointment being updated.
     */
    @PutMapping("/updateAppointment")
    public Mono<ResponseEntity<Appointment>> updateVendiaAppointment(@RequestBody Appointment appointment)
    {
        log.info("Updating an appointment", appointment);
        return appointmentService.updateAppointment(appointment)
                .map(r -> new ResponseEntity<>(r, HttpStatus.OK))
                .doOnNext(u -> log.info("Appointment updated in Vendia share!"))
                .onErrorMap(e -> new Exception("Error updatiing appointment in Vendia"));
    }

    /**
     * This deletes the appointment called in Vendia
     * @param appointment The appointment you want to delete
     * @return The (@link DeletionResponse} of the appointment being updated.
     */
    @DeleteMapping("/deleteAppointment")
    public Mono<ResponseEntity<DeletionResponse>> deleteVendiaAppointment(@RequestBody Appointment appointment){
        log.info("Deleting an appointment");
        return appointmentService.deleteAppointment(appointment)
                .map(r -> new ResponseEntity<>(r, HttpStatus.OK))
                .doOnNext(u -> log.info("Appointment deleted in Vendia share!"))
                .onErrorMap(e -> new Exception("Error deleting appointment in Vendia"));
    }

}