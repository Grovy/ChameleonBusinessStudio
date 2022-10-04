package com.compilercharisma.chameleonbusinessstudio.repository;

import com.compilercharisma.chameleonbusinessstudio.client.VendiaClient;
import com.compilercharisma.chameleonbusinessstudio.dto.Appointment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 *
 *
 * @author Ariel Camargo
 */
@Slf4j
@Repository
public class AppointmentRepositoryv2
{
    private final VendiaClient vendiaClient;

    public AppointmentRepositoryv2(VendiaClient vendiaClient){
        this.vendiaClient = vendiaClient;
    }

    /**
     *
     * @author Ariel Camargo
     * @param appointment the appointment that will be created
     * @return The {@link} of the appointment created
     */
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
