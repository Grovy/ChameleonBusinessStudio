package com.compilercharisma.chameleonbusinessstudio.controller;

import com.compilercharisma.chameleonbusinessstudio.dto.Appointment;

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

    @PostMapping("/createAppointment")
    public Mono<ResponseEntity<Appointment>> createVendiaAppointment(@RequestBody Appointment appointment){
        log.info("Creating an appointment", appointment);
        return appointmentService.createAppointment(appointment)
                .map(r -> new ResponseEntity<>(r, HttpStatus.OK))
                .doOnNext(u -> log.info("Appointment created in Vendia share!"))
                .onErrorMap(e -> new Exception("Error creating appointment in Vendia"));
    }

}