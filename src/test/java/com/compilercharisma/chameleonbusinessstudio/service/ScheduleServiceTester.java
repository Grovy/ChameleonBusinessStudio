package com.compilercharisma.chameleonbusinessstudio.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.compilercharisma.chameleonbusinessstudio.dto.Appointment;
import com.compilercharisma.chameleonbusinessstudio.dto.Schedule;
import com.compilercharisma.chameleonbusinessstudio.exception.InvalidScheduleException;
import com.compilercharisma.chameleonbusinessstudio.repository.ScheduleRepository;
import com.compilercharisma.chameleonbusinessstudio.util.TestScheduleBuilder;
import com.compilercharisma.chameleonbusinessstudio.validators.ScheduleValidator;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ScheduleServiceTester {
    private final ScheduleRepository repo;
    private final ScheduleValidator validator;
    private final AppointmentService appointments;
    private final ScheduleService sut;
    private final String aParticipant = "foo.bar@baz.qux";
    private Appointment anAppointment;

    public ScheduleServiceTester(){
        repo = Mockito.mock(ScheduleRepository.class);
        validator = Mockito.mock(ScheduleValidator.class);
        appointments = Mockito.mock(AppointmentService.class);
        sut = new ScheduleService(repo, validator, appointments);
    }

    @Test
    public void getAllSchedules_whenRepositoryIsEmpty_returnsEmptyList(){
        Mockito.when(repo.getAllSchedules()).thenReturn(Flux.empty());

        var result = sut.getAllSchedules();
        var listIsEmpty = !result.hasElements().block();
        Assertions.assertTrue(listIsEmpty, "getAllSchedules returned elements when repository was empty");
    }

    @Test
    public void saveSchedule_givenAnInvalidSchedule_throwsAnError(){
        var anInvalidSchedule = new Schedule();
        Mockito.doThrow(new InvalidScheduleException())
            .when(validator).validate(anInvalidSchedule);

        Assertions.assertThrows(
            InvalidScheduleException.class, 
            ()->sut.saveSchedule(anInvalidSchedule)
        );
    }

    @Test
    public void saveSchedule_givenAValidSchedule_forwardsToRepo(){
        var aValidSchedule = new Schedule();

        sut.saveSchedule(aValidSchedule);

        /*
        Mockito probably doesn't do async repo, but if issues arise, it might
        be bc it's running saveSched async, so it may not get run by this point
        */
        Mockito.verify(repo).storeSchedule(aValidSchedule);
    }

    @Test
    public void generateAppointments_withNoEnabledSchedules_doesNothing(){
        when(repo.getAllSchedulesWhereEnabledEquals(true)).thenReturn(Flux.empty());
        sut.generateAppointments();
        thenNoAppointmentsWereGenerated();
    }

    @Test
    public void generateAppointments_withNoEnabledRepeatingAppointments_doesNothing(){
        var schedule = new TestScheduleBuilder()
            .enabled()
            .withAppointment(appt->appt.disabled())
            .build();
        when(repo.getAllSchedulesWhereEnabledEquals(true)).thenReturn(Flux.just(schedule));

        sut.generateAppointments();

        thenNoAppointmentsWereGenerated();
    }

    @Test
    public void generateAppointments_withAValidSchedule_forwardsToAppointmentRepo(){
        when(appointments.createAppointment(any(Appointment.class)))
            .thenReturn(Mono.just(new Appointment()));
        when(appointments.createAppointments(anyList()))
            .thenReturn(Flux.empty());
        when(repo.storeSchedules(anyList()))
            .thenReturn(Mono.empty());

        var aValidSchedule = new TestScheduleBuilder()
            .enabled()
            .withAppointment(appt->appt.enabled().on(DayOfWeek.MONDAY))
            .build();
        when(repo.getAllSchedulesWhereEnabledEquals(true)).thenReturn(Flux.just(aValidSchedule));
        
        sut.generateAppointments().block();

        verify(appointments, times(1)).createAppointments(anyList());
    }

    @Test
    public void copyToDay_givenAParticipantInTheAppointment_includesThemInTheCopy(){
        givenAValidAppointment();
        givenAParticipantInTheAppointment();

        var copy = sut.copyToDay(anAppointment, LocalDateTime.now());

        Assertions.assertTrue(
            copy.getParticipants().contains(aParticipant),
            "copyToDay failed to copy the participant"
        );
    }

    private void givenAValidAppointment(){
        anAppointment = Appointment.builder()
            .startTime(LocalDateTime.now())
            .endTime(LocalDateTime.now().plusHours(2))
            .build();
    }

    private void givenAParticipantInTheAppointment(){
        anAppointment.getParticipants().add(aParticipant);
    }

    private void thenNoAppointmentsWereGenerated(){
        // verify no appointments were created
        verify(appointments, never()).createAppointment(any(Appointment.class));

        // verify no schedules were updated
        verify(repo, never()).storeSchedule(any(Schedule.class));
        verify(repo, never()).storeSchedules(anyList());
    }
}
