package com.compilercharisma.chameleonbusinessstudio.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import com.compilercharisma.chameleonbusinessstudio.dto.Schedule;
import com.compilercharisma.chameleonbusinessstudio.exception.InvalidScheduleException;
import com.compilercharisma.chameleonbusinessstudio.repository.ScheduleRepository;
import com.compilercharisma.chameleonbusinessstudio.validators.ScheduleValidator;

import reactor.core.publisher.Flux;

public class ScheduleServiceTester {
    
    @Test
    public void getAllSchedules_whenRepositoryIsEmpty_returnsEmptyList(){
        var repo = Mockito.mock(ScheduleRepository.class);
        Mockito.when(repo.getAllSchedules()).thenReturn(Flux.empty());
        var validator = Mockito.mock(ScheduleValidator.class);
        var sut = new ScheduleService(repo, validator);

        var result = sut.getAllSchedules();
        var listIsEmpty = !result.hasElements().block();
        Assertions.assertTrue(listIsEmpty, "getAllSchedules returned elements when repository was empty");
    }

    @Test
    public void saveSchedule_givenAnInvalidSchedule_throwsAnError(){
        var anInvalidSchedule = new Schedule();
        var repo = Mockito.mock(ScheduleRepository.class);
        var validator = Mockito.mock(ScheduleValidator.class);
        Mockito.doThrow(new InvalidScheduleException())
            .when(validator).validate(anInvalidSchedule);
        var sut = new ScheduleService(repo, validator);

        Assertions.assertThrows(
            InvalidScheduleException.class, 
            ()->sut.saveSchedule(anInvalidSchedule)
        );
    }

    @Test
    public void saveSchedule_givenAValidSchedule_forwardsToRepo(){
        var aValidSchedule = new Schedule();
        var repo = Mockito.mock(ScheduleRepository.class);
        var validator = Mockito.mock(ScheduleValidator.class);
        var sut = new ScheduleService(repo, validator);

        sut.saveSchedule(aValidSchedule);

        /*
        Mockito probably doesn't do async repo, but if issues arise, it might
        be bc it's running saveSched async, so it may not get run by this point
        */
        Mockito.verify(repo).storeSchedule(aValidSchedule);
    }
}
