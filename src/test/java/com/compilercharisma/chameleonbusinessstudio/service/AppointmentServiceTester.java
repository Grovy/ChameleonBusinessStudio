package com.compilercharisma.chameleonbusinessstudio.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.compilercharisma.chameleonbusinessstudio.dto.Appointment;
import com.compilercharisma.chameleonbusinessstudio.dto.User;
import com.compilercharisma.chameleonbusinessstudio.exception.InvalidAppointmentException;
import com.compilercharisma.chameleonbusinessstudio.exception.UserNotRegisteredException;
import com.compilercharisma.chameleonbusinessstudio.repository.AppointmentRepository;
import com.compilercharisma.chameleonbusinessstudio.validators.AppointmentValidator;

import reactor.core.publisher.Mono;

public class AppointmentServiceTester {
    private final AppointmentRepository repo = mock(AppointmentRepository.class);
    private final UserService users = mock(UserService.class);
    private final AppointmentValidator validator = mock(AppointmentValidator.class);
    private final Appointment theAppointment = new Appointment();
    private final String theEmail = "test.user@gmail.com";
    private final User theUser = User.builder().email(theEmail)._id("a-b-c").build();

    @Test
    public void bookAppointmentForUser_givenTheUserIsNotRegistered_doesNotBookTheUser(){
        givenTheUserIsInvalid();
        givenTheAppointmentIsNotFull();
        var sut = makeSut();

        Assertions.assertThrows(UserNotRegisteredException.class, () -> {
            sut.bookAppointmentForUser(theAppointment.get_id(), theEmail).block();
        });
    }

    @Test
    public void bookAppointmentForUser_givenAnInvalidAppointment_doesNotBookTheUser(){
        givenTheUserIsValid();
        givenTheAppointmentIsInvalid();
        var sut = makeSut();

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            sut.bookAppointmentForUser(theAppointment.get_id(), theEmail).block();
        });
    }

    @Test
    public void bookAppointmentForUser_givenTheUserIsNotBookedAndTheAppointmentIsNotFull_booksTheUser(){
        givenTheUserIsValid();
        givenTheEmailIsNotBooked();
        
        givenTheAppointmentExistsInVendia(); // need to call before givenTheAppointmentIsValid
        givenTheAppointmentIsValid();
        givenTheAppointmentIsNotFull();
        var sut = makeSut();

        sut.bookAppointmentForUser(theAppointment.get_id(), theEmail).block();

        verify(repo, times(1)).updateAppointment(theAppointment);
    }

    @Test
    public void bookAppointmentForUser_givenTheUserIsBookedAndTheAppointmentIsNotFull_doesNotThrowAnExceptionAndDoesNotUpdateTheAppointment(){        
        givenTheUserIsValid();
        givenTheEmailIsBooked();
        
        givenTheAppointmentExistsInVendia(); // need to call before givenTheAppointmentIsValid
        givenTheAppointmentIsValid();
        givenTheAppointmentIsNotFull();
        var sut = makeSut();

        sut.bookAppointmentForUser(theAppointment.get_id(), theEmail).block();

        verify(repo, never()).updateAppointment(theAppointment);
    }

    @Test
    public void cancelAppointment_givenAnInvalidAppointment_throwsAnError(){
        givenTheAppointmentIsInvalid();
        var sut = makeSut();

        Assertions.assertThrows(InvalidAppointmentException.class, 
            () -> sut.cancelAppointment(theAppointment).block());
    }

    @Test
    public void cancelAppointment_givenAValidAppointment_returnsTheCanceledAppointment(){
        givenTheAppointmentIsValid();
        givenTheAppointmentExistsInVendia();
        var sut = makeSut();

        var result = sut.cancelAppointment(theAppointment).block();

        Assertions.assertEquals(theAppointment.get_id(), result.get_id());
        Assertions.assertTrue(result.getCancelled(), 
            "Failed to cancel the appointment");
    }

    @Test
    public void cancelAppointment_whenTheAppointmentIsAlreadyCanceled_doesNotCallUpdate(){
        givenTheAppointmentIsValid();
        givenTheAppointmentExistsInVendia();
        theAppointment.setCancelled(true);
        var sut = makeSut();

        sut.cancelAppointment(theAppointment).block(); // make sure to block!

        verify(repo, never()).updateAppointment(theAppointment);
    }

    @Test
    public void bookEmail_givenAnInvalidAppointment_throwsAnError(){
        givenTheAppointmentIsInvalid();
        var sut = makeSut();

        Assertions.assertThrows(InvalidAppointmentException.class,
            () -> sut.bookEmail(theAppointment, theEmail).block());
    }

    @Test
    public void bookEmail_givenAnUnbookedUser_addsTheToTheAppointment(){
        givenTheAppointmentIsValid();
        givenTheAppointmentExistsInVendia();
        givenTheEmailIsNotBooked();
        var sut = makeSut();

        var actual = sut.bookEmail(theAppointment, theEmail).block();

        Assertions.assertEquals(theAppointment.get_id(), actual.get_id());
        Assertions.assertTrue(actual.getParticipants().contains(theEmail), 
            "book failed to add the email to the appointment");
    }

    @Test
    public void bookEmail_givenABookedUser_doesNotCallUpdate(){
        givenTheAppointmentIsValid();
        givenTheAppointmentExistsInVendia();
        givenTheEmailIsBooked();
        var sut = makeSut();

        sut.bookEmail(theAppointment, theEmail).block();

        verify(repo, never()).updateAppointment(theAppointment);
    }

    @Test
    public void bookEmail_givenTheAppointmentIsFull_throwsAnError(){
        givenTheAppointmentIsValid();
        givenTheAppointmentIsFull();
        givenTheEmailIsNotBooked();
        var sut = makeSut();

        Assertions.assertThrows(UnsupportedOperationException.class, 
            () -> sut.bookEmail(theAppointment, theEmail).block());
    }

    @Test
    public void unbook_givenAnInvalidAppointment_throwsAnError(){
        givenTheAppointmentIsInvalid();
        var sut = makeSut();

        Assertions.assertThrows(InvalidAppointmentException.class,
            () -> sut.unbookEmail(theAppointment, theEmail).block());
    }

    @Test
    public void unbook_givenABookedUser_removesThemFromTheAppointment(){
        givenTheAppointmentIsValid();
        givenTheAppointmentExistsInVendia();
        givenTheEmailIsBooked();
        var sut = makeSut();

        var actual = sut.unbookEmail(theAppointment, theEmail).block();

        Assertions.assertEquals(theAppointment.get_id(), actual.get_id());
        Assertions.assertFalse(actual.getParticipants().contains(theEmail),
            "unbook failed to remove the email from the appointment");
    }

    @Test
    public void unbook_givenTheUserIsNotBooked_doesNotCallUpdate(){
        givenTheAppointmentIsValid();
        givenTheAppointmentExistsInVendia();
        givenTheEmailIsNotBooked();
        var sut = makeSut();

        sut.unbookEmail(theAppointment, theEmail).block(); // make sure to block!

        verify(repo, never()).updateAppointment(theAppointment);
    }

    private void givenTheAppointmentIsValid(){
        theAppointment.setTitle("baz");
        theAppointment.setLocation("bar");
        theAppointment.setDescription("foo");
        theAppointment.setStartTime(LocalDateTime.now());
        theAppointment.setEndTime(LocalDateTime.now().plusHours(1));
        theAppointment.setTotalSlots(3);
        doNothing()
            .when(validator).validate(theAppointment);
        when(repo.updateAppointment(theAppointment))
            .thenReturn(Mono.just(theAppointment));
    }

    private void givenTheAppointmentIsInvalid() {
        doThrow(InvalidAppointmentException.class)
            .when(validator).validate(theAppointment);
        when(users.addNewAppointmentForUser(any(), isNull()))
            .thenReturn(Mono.error(new IllegalArgumentException())); // not the best way of doing things
        when(repo.getAppointmentById(theAppointment.get_id()))
            .thenReturn(Mono.error(new IllegalArgumentException())); // not the best way of doing things
    }

    private void givenTheAppointmentExistsInVendia(){
        theAppointment.set_id("foo-bar-baz-qux");
        when(repo.getAppointmentById(theAppointment.get_id()))
            .thenReturn(Mono.just(theAppointment));
        when(repo.updateAppointment(theAppointment))
            .thenReturn(Mono.just(theAppointment));
    }

    private void givenTheAppointmentIsFull(){
        theAppointment.getParticipants().add("not." + theEmail);
        theAppointment.setTotalSlots(theAppointment.getParticipants().size());
    }

    private void givenTheAppointmentIsNotFull(){
        theAppointment.setTotalSlots(theAppointment.getParticipants().size() + 1);
    }

    private void givenTheUserIsValid(){
        when(users.isRegistered(theEmail))
            .thenReturn(Mono.just(true));
        when(users.getUser(theEmail))
            .thenReturn(Mono.just(theUser));
        when(users.addNewAppointmentForUser(any(String.class), any(String.class)))
            .thenReturn(Mono.just(theUser));
    }

    private void givenTheUserIsInvalid(){
        when(users.isRegistered(theEmail))
            .thenReturn(Mono.just(false));
    }

    private void givenTheEmailIsBooked(){
        theAppointment.getParticipants().add(theEmail);
    }

    private void givenTheEmailIsNotBooked(){
        theAppointment.getParticipants().remove(theEmail);
    }

    private AppointmentService makeSut(){
        return new AppointmentService(repo, users, validator);
    }
}
