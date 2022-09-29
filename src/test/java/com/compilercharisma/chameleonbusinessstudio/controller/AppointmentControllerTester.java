package com.compilercharisma.chameleonbusinessstudio.controller;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.compilercharisma.chameleonbusinessstudio.entity.AppointmentEntity;
import com.compilercharisma.chameleonbusinessstudio.entity.UserEntity;
import com.compilercharisma.chameleonbusinessstudio.entity.appointment.AppointmentModelAssembler;
import com.compilercharisma.chameleonbusinessstudio.entity.user.Participant;
import com.compilercharisma.chameleonbusinessstudio.entity.user.Role;
import com.compilercharisma.chameleonbusinessstudio.exception.InvalidAppointmentException;
import com.compilercharisma.chameleonbusinessstudio.service.AppointmentService;
import com.compilercharisma.chameleonbusinessstudio.service.AuthenticationService;
import com.compilercharisma.chameleonbusinessstudio.service.UserService;


// not sure what is the best format to go about doing this
public class AppointmentControllerTester {
    private final AppointmentService appointments = Mockito.mock(AppointmentService.class);
    private final AuthenticationService authentication = Mockito.mock(AuthenticationService.class);
    private final UserService users = Mockito.mock(UserService.class);
    private final AppointmentModelAssembler modelAssembler = Mockito.mock(AppointmentModelAssembler.class);
    private final AppointmentController sut;
    private final AppointmentEntity appointment = new AppointmentEntity();
    private final UserEntity user = new UserEntity();

    public AppointmentControllerTester(){
        sut = new AppointmentController(
            appointments,
            authentication,
            users,
            modelAssembler
        );
    }

    @Test
    public void bookMe_givenAValidAppointment_registersLoggedInUser(){
        givenTheAppointmentIsValid();
        givenTheUserIsValid();
        
        sut.bookMe(appointment.getId()).block();

        Mockito.verify(appointments).registerUser(appointment, user.getEmail());
    }

    @Test
    public void bookMe_givenAnInvalidAppointment_doesNotRegisterAnyone() throws InvalidAppointmentException{
        var e = new UserEntity();
        e.setEmail("foo.bar@gmail.com");
        
        var user = new Participant(e);
        var anInvalidAppointment = new AppointmentEntity();

        Mockito.doNothing().when(appointments).validateAppointment(anInvalidAppointment);
        Mockito.when(appointments.isAppointmentValid(anInvalidAppointment)).thenReturn(false);
        Mockito.when(authentication.getLoggedInUser()).thenReturn(user);

        Assertions.assertThrows(IllegalArgumentException.class, ()->sut.bookMe(anInvalidAppointment.getId()).block());

        Mockito
            .verify(appointments, Mockito.never())
            .registerUser(Mockito.eq(anInvalidAppointment), Mockito.any(String.class));
    }

    @Test
    public void bookThem_givenAnInvalidAppointmentId_doesNotRegisterAnyone(){
        givenTheUserIsValid();
        var anInvalidAppointment = new AppointmentEntity();

        Assertions.assertThrows(IllegalArgumentException.class, ()->sut.bookThem(anInvalidAppointment.getId(), "foo.bar@baz.qux").block());
        
        Mockito
            .verify(appointments, Mockito.never())
            .registerUser(Mockito.eq(anInvalidAppointment), Mockito.any(String.class));
    }

    //@Test we need email validation
    public void bookThem_givenAnInvalidEmail_doesNotRegisterAnyone(){
        givenTheAppointmentIsValid();
        givenTheUserIsValid();

        Assertions.assertThrows(IllegalArgumentException.class, ()->sut.bookThem(appointment.getId(), "an invalid email"));

        Mockito
            .verify(appointments, Mockito.never())
            .registerUser(Mockito.any(AppointmentEntity.class), Mockito.any(String.class));
    }

    private void givenTheUserIsValid(){
        user.setEmail("foo.bar@baz.qux");
        user.setDisplayName("John Doe");
        user.setId(1);
        user.setRole(Role.PARTICIPANT);

        var userWrapper = new Participant(user);

        Mockito.when(users.get(user.getEmail())).thenReturn(userWrapper);
        Mockito.when(users.isRegistered(user.getEmail())).thenReturn(true);
        Mockito.when(authentication.getLoggedInUser()).thenReturn(userWrapper);
    }

    private void givenTheAppointmentIsValid(){
        appointment.setDescription("foo");
        appointment.setLocation("bar");
        appointment.setTotalSlots(1);
        appointment.setStartTime(LocalDateTime.now());
        appointment.setEndTime(appointment.getStartTime().plusHours(1));

        Mockito.doNothing().when(appointments).validateAppointment(appointment);
        Mockito.when(appointments.isAppointmentValid(appointment)).thenReturn(true);
        Mockito.when(appointments.getAppointmentById(appointment.getId())).thenReturn(Optional.of(appointment));
        Mockito.when(appointments.isSlotAvailable(appointment)).thenReturn(true);
    }
}
