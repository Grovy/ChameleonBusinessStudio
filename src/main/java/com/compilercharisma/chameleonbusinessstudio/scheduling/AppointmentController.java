package com.compilercharisma.chameleonbusinessstudio.scheduling;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Matt Crow <mattcrow19@gmail.com>
 */
@RestController
@RequestMapping("/api/v1/appointments")
public class AppointmentController {
    
    private final AppointmentService serv;
    
    @Autowired
    public AppointmentController(AppointmentService serv){
        this.serv = serv;
    }
    
    @GetMapping(path="available")
    public List<AppointmentEntity> getAvailableInDays(
            @RequestParam int days,
            @RequestParam(required=false, defaultValue="0") int startHour,
            @RequestParam(required=false, defaultValue="23") int endHour
    ){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime later = now.plusDays(days);
        LocalTime hrStart = LocalTime.of(startHour, 0, 0);
        LocalTime hrEnd = LocalTime.of(endHour, 0, 0);
        return serv.getAvailableAppointments(now, later, hrStart, hrEnd);
    }
    
    @GetMapping(path="test")
    public List<AppointmentEntity> test(){
        List<AppointmentEntity> appts = serv.getAppointmentsBetween( // between 4/15/2022 & 4/25/2022
                LocalDateTime.of(2022, 4, 15, 0, 0), 
                LocalDateTime.of(2022, 4, 25, 0, 0)
        );
        
        if(appts.isEmpty()){
            serv.createTestData();
            appts = serv.getAppointmentsBetween( // between 4/15/2022 & 4/25/2022
                    LocalDateTime.of(2022, 4, 15, 0, 0), 
                    LocalDateTime.of(2022, 4, 25, 0, 0)
            );
        }
        
        System.out.println(appts.get(0));
        
        return appts;
    }
}
