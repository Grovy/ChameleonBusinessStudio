package com.compilercharisma.chameleonbusinessstudio.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.compilercharisma.chameleonbusinessstudio.dto.Schedule;

public class TestScheduleBuilder {
    private boolean scheduleIsEnabled;
    private final List<TestRepeatingAppointmentBuilder> appts = new ArrayList<>();

    public TestScheduleBuilder enabled(){
        scheduleIsEnabled = true;
        return this;
    }

    public TestScheduleBuilder disabled(){
        scheduleIsEnabled = false;
        return this;
    }

    public TestScheduleBuilder withAppointment(Consumer<TestRepeatingAppointmentBuilder> doThis){
        var trab = new TestRepeatingAppointmentBuilder();
        doThis.accept(trab);
        appts.add(trab);
        return this;
    }

    public Schedule build(){
        return Schedule.builder()
            .isEnabled(scheduleIsEnabled)
            .appointments(appts.stream().map(trab->trab.build()).toList())
            .build();
    }
}
