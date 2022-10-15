package com.compilercharisma.chameleonbusinessstudio.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.compilercharisma.chameleonbusinessstudio.dto.Appointment;
import com.compilercharisma.chameleonbusinessstudio.dto.RepeatingAppointment;
import com.compilercharisma.chameleonbusinessstudio.dto.Schedule;
import com.compilercharisma.chameleonbusinessstudio.entity.AppointmentEntity;
import com.compilercharisma.chameleonbusinessstudio.exception.InvalidScheduleException;
import com.compilercharisma.chameleonbusinessstudio.repository.ScheduleRepository;
import com.compilercharisma.chameleonbusinessstudio.validators.ScheduleValidator;

import reactor.core.publisher.Flux;

public class ScheduleServiceTester {
    private final ScheduleRepository repo;
    private final ScheduleValidator validator;
    private final AppointmentService appointments;
    private final ScheduleService sut;

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
        var disabledRepeatingAppointment = RepeatingAppointment.builder()
            .isEnabled(false)
            .build();
        var schedule = Schedule.builder()
            .appointments(List.of(disabledRepeatingAppointment))
            .build();
        when(repo.getAllSchedulesWhereEnabledEquals(true)).thenReturn(Flux.just(schedule));

        sut.generateAppointments();

        thenNoAppointmentsWereGenerated();
    }

    @Test
    public void generateAppointments_withAValidSchedule_copiesAppointmentsToTheProperDays(){
        var created = new HashSet<>();
        when(appointments.createAppointment(any(Appointment.class))).thenAnswer(new Answer<Appointment>() {
            @Override
            public Appointment answer(InvocationOnMock invocation) throws Throwable {
                var arg = invocation.getArgument(0);
                if(!(arg instanceof Appointment)){
                    throw new RuntimeException("better way of doing side effects?");
                }
                var appt = (Appointment)arg;
                created.add(appt);
                return appt;
            }
        });

        throw new UnsupportedOperationException();
    }

    private void thenNoAppointmentsWereGenerated(){
        // verify no appointments were created
        verify(appointments, never()).createAppointment(any(AppointmentEntity.class));

        // verify no schedules were updated
        verify(repo, never()).storeSchedule(any(Schedule.class));
        verify(repo, never()).storeSchedules(anyList());
    }
}
