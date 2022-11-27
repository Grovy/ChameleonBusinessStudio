package com.compilercharisma.chameleonbusinessstudio.service;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.compilercharisma.chameleonbusinessstudio.dto.User;
import com.compilercharisma.chameleonbusinessstudio.dto.UserAppointments;
import com.compilercharisma.chameleonbusinessstudio.exception.ExternalServiceException;
import com.compilercharisma.chameleonbusinessstudio.repository.AppointmentRepository;
import com.compilercharisma.chameleonbusinessstudio.repository.UserRepository;

import reactor.core.publisher.Mono;

public class UserServiceTester {

    private final String aUserId = "anything";
    private final User aUser = User.builder()._id(aUserId).build();
    private final String anAppointmentId = "also anything";
    private final String anotherAppointmentId = "anything, once more";
    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final AppointmentRepository appointmentRepository = Mockito.mock(AppointmentRepository.class);

    @Test
    public void addNewAppointmentForUser_givenAnInvalidUserId_throwsAnException(){
        givenAnInvalidUserId();
        givenAValidAppointmentId();
        var sut = makeSut();

        Assertions.assertThrows(ExternalServiceException.class, 
            () -> sut.addNewAppointmentForUser(aUserId, anAppointmentId).block());
    }

    @Test
    public void addNewAppointmentsForUser_givenMultipleAppointmentsForOneUser_makesOneRequestPerUser(){
        givenAValidUserId();
        givenAValidAppointmentId();
        var sut = makeSut();

        sut.addNewAppointmentsForUser(
            aUserId, 
            List.of(anAppointmentId, anotherAppointmentId)
        ).block();

        verify(userRepository, times(1))
            .updateAppointmentsForUser(eq(aUserId), anyString());
    }

    private UserService makeSut(){
        return new UserService(userRepository, appointmentRepository);
    }

    private void givenAValidUserId(){
        when(userRepository.getUserAppointments(aUserId))
            .thenReturn(Mono.just(UserAppointments.builder()
                .appointments(aUser.getAppointments())
                .build()));
        when(userRepository.updateAppointmentsForUser(eq(aUserId), anyString()))
            .thenReturn(Mono.just(aUser));
    }

    private void givenAnInvalidUserId(){
        when(userRepository.getUserAppointments(aUserId))
            .thenReturn(Mono.just(new UserAppointments()));
        doThrow(ExternalServiceException.class)
            .when(userRepository)
            .updateAppointmentsForUser(eq(aUserId), anyString());
    }

    private void givenAValidAppointmentId(){
        // we aren't validating this?
    }
}
