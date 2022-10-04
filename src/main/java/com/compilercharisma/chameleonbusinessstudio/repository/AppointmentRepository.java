package com.compilercharisma.chameleonbusinessstudio.repository;

import java.time.LocalDateTime;
import java.util.List;

import com.compilercharisma.chameleonbusinessstudio.client.VendiaClient;
import com.compilercharisma.chameleonbusinessstudio.dto.Appointment;
import com.compilercharisma.chameleonbusinessstudio.entity.AppointmentEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/** 
 * can use findAll(AppointmentSpecification.xyz)
 * @author Matt Crow <mattcrow19@gmail.com>
 */
@Slf4j
@Repository
public class AppointmentRepository
{
    private final VendiaClient vendiaClient;

    public AppointmentRepository(VendiaClient vendiaClient){
        this.vendiaClient = vendiaClient;
    }

    public Mono<Appointment> createAppointment(Appointment appointment)
    {
        var query = """
                mutation {
                  add_Appointment(
                    input: {cancelled: %s, endTime: "%s", description: "%s", location: "%s", participants: [], restrictions: "%s", startTime: "%s", title: "%s", totalSlots: %d}
                  ) {
                    result {
                      cancelled
                      description
                      endTime
                      location
                      participants
                      restrictions
                      startTime
                      title
                      totalSlots
                    }
                  }
                }
                """.formatted(appointment.getCancelled(), appointment.getEndTime(),
                    appointment.getDescription(), appointment.getLocation(),
                    appointment.getRestrictions(),
                    appointment.getStartTime(), appointment.getTitle(), appointment.getTotalSlots());
        return vendiaClient.executeQuery(query, "add_Appointment.result", Appointment.class);
    }
}
