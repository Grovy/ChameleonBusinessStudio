package com.compilercharisma.chameleonbusinessstudio.scheduling;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Matt Crow <mattcrow19@gmail.com>
 */
@RestController
@RequestMapping("/api/v1/appointments")
public class AppointmentController {
    
    private final AppointmentService serv;
    private final AppointmentTranslator translator;
    
    @Autowired
    public AppointmentController(AppointmentService serv, AppointmentTranslator translator){
        this.serv = serv;
        this.translator = translator;
    }
    
    @GetMapping(path="test")
    public List<AppointmentJson> test(){
        List<AppointmentJson> appts = serv.getAppointmentsBetween( // between 4/15/2022 & 4/25/2022
                LocalDateTime.of(2022, 4, 15, 0, 0), 
                LocalDateTime.of(2022, 4, 25, 0, 0)
        ).stream().map(translator::toJson).collect(Collectors.toList());
        
        if(appts.isEmpty()){
            // populate test data. yeah, I know this is ugly
            AppointmentEntity appt;
            for(int i = 1; i <= 28; ++i){
                appt = new AppointmentEntity();
                appt.setStartTime(LocalDateTime.of(2022, 4, i, 0, 0));
                appt.setEndTime(LocalDateTime.of(2022, 4, i, 12, 0));
                appt.setTitle(String.format("Appt. #%d", i));
                appt.setLocation(String.format("%d J Street", i * 20));
                appt.setTotalSlots(i % 10 + 1);
                if(i % 5 == 0){
                    for(int j = 0; j <= i / 5; ++j){
                        appt.addTagValue("Tag " + j, "Value " + j);
                    }
                }
                if(i % 3 == 0){
                    for(int j = 0; j <= i / 3 && appt.getRegisteredUsers().size() < appt.getTotalSlots(); ++j){
                        appt.addRegisteredUser("email " + j);
                    }
                }
                serv.createAppointment(translator.toAppointment(appt));
            }
            appts = serv.getAppointmentsBetween( // between 4/15/2022 & 4/25/2022
                    LocalDateTime.of(2022, 4, 15, 0, 0), 
                    LocalDateTime.of(2022, 4, 25, 0, 0)
            ).stream().map(translator::toJson).collect(Collectors.toList());
        }
        
        System.out.println(appts.get(0));
        
        return appts;
    }
}
