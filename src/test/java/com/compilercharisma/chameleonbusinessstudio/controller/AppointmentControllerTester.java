//package com.compilercharisma.chameleonbusinessstudio.controller;
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.http.HttpStatus;
//
//import com.compilercharisma.chameleonbusinessstudio.entity.AppointmentEntity;
//import com.compilercharisma.chameleonbusinessstudio.entity.UserEntity;
//import com.compilercharisma.chameleonbusinessstudio.entity.appointment.AppointmentModelAssembler;
//import com.compilercharisma.chameleonbusinessstudio.entity.user.Participant;
//import com.compilercharisma.chameleonbusinessstudio.service.AppointmentService;
//import com.compilercharisma.chameleonbusinessstudio.service.AuthenticationService;
//
//public class AppointmentControllerTester {
//
//    @Test
//    public void bookMe_givenAValidAppointment_registersLoggedInUser(){
//        var e = new UserEntity();
//        e.setEmail("foo.bar@gmail.com");
//
//        var user = new Participant(e);
//        var aValidAppointment = new AppointmentEntity();
//
//        var appointments = Mockito.mock(AppointmentService.class);
//        var authentication = Mockito.mock(AuthenticationService.class);
//        var modelAssembler = Mockito.mock(AppointmentModelAssembler.class);
//        var sut = new AppointmentController(
//            appointments,
//            authentication,
//            modelAssembler
//        );
//
//        Mockito.when(appointments.isAppointmentValid(aValidAppointment)).thenReturn(true);
//        Mockito.when(authentication.getLoggedInUser()).thenReturn(user);
//
//        var response = sut.bookMe(aValidAppointment).block();
//
//        Assertions.assertNotNull(response);
//        Mockito.verify(appointments).registerUser(aValidAppointment, user.getEmail());
//    }
//
//    @Test
//    public void bookMe_givenAnInvalidAppointment_doesNotRegisterAnyone(){
//        var e = new UserEntity();
//        e.setEmail("foo.bar@gmail.com");
//
//        var user = new Participant(e);
//        var aValidAppointment = new AppointmentEntity();
//
//        var appointments = Mockito.mock(AppointmentService.class);
//        var authentication = Mockito.mock(AuthenticationService.class);
//        var modelAssembler = Mockito.mock(AppointmentModelAssembler.class);
//        var sut = new AppointmentController(
//            appointments,
//            authentication,
//            modelAssembler
//        );
//
//        Mockito.when(appointments.isAppointmentValid(aValidAppointment)).thenReturn(false);
//        Mockito.when(authentication.getLoggedInUser()).thenReturn(user);
//
//        var response = sut.bookMe(aValidAppointment).block();
//
//        Assertions.assertNotNull(response);
//        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//        Mockito
//            .verify(appointments, Mockito.never())
//            .registerUser(Mockito.eq(aValidAppointment), Mockito.any(String.class));
//    }
//}
