package com.compilercharisma.chameleonbusinessstudio.entity.appointment;

import com.compilercharisma.chameleonbusinessstudio.entity.*;
import java.time.LocalDateTime;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 *
 * @author Matt Crow
 */
public class AppointmentValidatorTester {
    private final AppointmentValidator sut = new AppointmentValidator();
    
    
    @Test
    public void givenAValidAppointment_isValidReturnsTrue(){
        AppointmentEntity e = aValidEntity();
        boolean result = sut.isValid(e);
        Assertions.assertTrue(result);
    }
    
    @Test
    public void anAppointmentWithNoStartTimeIsInvalid(){
        AppointmentEntity e = aValidEntity();
        e.setStartTime(null);
        boolean result = sut.isValid(e);
        Assertions.assertFalse(result);
    }
    
    @Test
    public void anAppointmentWithNoEndTimeIsInvalid(){
        AppointmentEntity e = aValidEntity();
        e.setEndTime(null);
        boolean result = sut.isValid(e);
        Assertions.assertFalse(result);
    }
    
    @Test
    public void anAppointmentCannotHaveStartTimeAfterEndTime(){
        AppointmentEntity e = aValidEntity();
        e.setEndTime(e.getStartTime().minusMinutes(5));
        boolean result = sut.isValid(e);
        Assertions.assertFalse(result);
    }
    
    @ParameterizedTest
    @ValueSource(strings={"", "\n", "\t", "\r\n"})
    public void anAppointmentsLocationMustBeReadable(String location){
        AppointmentEntity e = aValidEntity();
        e.setLocation(location);
        boolean result = sut.isValid(e);
        Assertions.assertFalse(result);
    }
    
    @Test
    public void anAppointmentsLocationCannotBeNull(){
        AppointmentEntity e = aValidEntity();
        e.setLocation(null);
        boolean result = sut.isValid(e);
        Assertions.assertFalse(result);
    }
    
    @Test
    public void anAppointmentsDescriptionCannotBeNull(){
        AppointmentEntity e = aValidEntity();
        e.setDescription(null);
        boolean result = sut.isValid(e);
        Assertions.assertFalse(result);
    }
    
    @ParameterizedTest
    @ValueSource(ints={0, -1, Integer.MIN_VALUE})
    public void anAppointmentsTotalSlotsMustBePositive(int totalSlots){
        AppointmentEntity e = aValidEntity();
        e.setTotalSlots(totalSlots);
        boolean result = sut.isValid(e);
        Assertions.assertFalse(result);
    }
    
    @ParameterizedTest
    @ValueSource(strings={"", "\n", "\t", "\r\n"})
    public void tagNamesMustBeReadable(String name){
        AppointmentEntity e = aValidEntity();
        Set<AppointmentTagEntity> tags = e.getTags();
        tags.add(new AppointmentTagEntity(name, "bar"));
        e.setTags(tags);
        boolean result = sut.isValid(e);
        Assertions.assertFalse(result);
    }
    
    private AppointmentEntity aValidEntity(){
        AppointmentEntity e = new AppointmentEntity();
        e.setStartTime(LocalDateTime.now().plusHours(3));
        e.setEndTime(LocalDateTime.now().plusHours(4));
        e.setTitle("foo");
        e.setLocation("bar");
        return e;
    }
}
