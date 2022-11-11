package com.compilercharisma.chameleonbusinessstudio.controller;

import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;

import com.compilercharisma.chameleonbusinessstudio.dto.Appointment;
import com.compilercharisma.chameleonbusinessstudio.dto.User;
import com.compilercharisma.chameleonbusinessstudio.enumeration.UserRole;
import com.compilercharisma.chameleonbusinessstudio.service.AppointmentService;
import com.compilercharisma.chameleonbusinessstudio.service.AuthenticationService;

import reactor.core.publisher.Mono;


// not sure what is the best format to go about doing this
public class AppointmentControllerTester {
    private final AppointmentService appointments = Mockito.mock(AppointmentService.class);
    private final AuthenticationService authentication = Mockito.mock(AuthenticationService.class);
    private final AppointmentController sut;
    private final Appointment appointment = new Appointment();
    private final User user = new User();
    private final Authentication token = null;

    public AppointmentControllerTester(){
        sut = new AppointmentController(
            appointments,
            authentication
        );
    }

    @Test
    public void bookMe_always_forwardsToService(){
        when(appointments.bookAppointmentForUser(appointment.get_id(), user.getEmail()))
            .thenReturn(Mono.just(appointment));
        
        sut.bookMe(token, appointment.get_id()).block();

        Mockito.verify(appointments).bookAppointmentForUser(appointment.get_id(), user.getEmail());
    }

    //@Test we need email validation
    public void bookThem_givenAnInvalidEmail_doesNotRegisterAnyone(){
        givenTheAppointmentIsValid();
        givenTheUserIsValid();

        Assertions.assertThrows(IllegalArgumentException.class, ()->{
            sut.bookThem(appointment.get_id(), "an invalid email");
        });

        Mockito
            .verify(appointments, Mockito.never())
            .bookEmailInAppointment(Mockito.any(Appointment.class), Mockito.any(String.class));
    }

    private void givenTheUserIsValid(){
        user.setEmail("foo.bar@baz.qux");
        user.setDisplayName("John Doe");
        user.set_id("foo");
        user.setRole(UserRole.PARTICIPANT);

        when(authentication.getLoggedInUserFrom(token)).thenReturn(Mono.just(user));
        when(authentication.getEmailFrom(token)).thenReturn(user.getEmail());
    }

    private void givenTheAppointmentIsValid(){
        appointment.setDescription("foo");
        appointment.setLocation("bar");
        appointment.setTotalSlots(1);
        appointment.setStartTime(LocalDateTime.now());
        appointment.setEndTime(appointment.getStartTime().plusHours(1));

        when(appointments.getAppointmentById(appointment.get_id())).thenReturn(Mono.just(appointment));
        when(appointments.isSlotAvailable(appointment)).thenReturn(true);
    }
}