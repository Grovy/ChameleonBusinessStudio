package com.compilercharisma.chameleonbusinessstudio.repository;

import com.compilercharisma.chameleonbusinessstudio.client.VendiaClient;
import com.compilercharisma.chameleonbusinessstudio.dto.Appointment;
import com.compilercharisma.chameleonbusinessstudio.dto.DeletionResponse;
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

    /**
     * @param appointment The appointment that is getting updated
     * @return The {@link} of the appointment getting updated
     */
    public Mono<Appointment> updateAppointment(Appointment appointment)
    {
        var query = """
                mutation {
                  update_Appointment(
                    id: "%s"
                    input: {cancelled: %s, description: "%s", endTime: "%s", location: "%s", participants: ["test@gmail.com", "test2@gmail.com"], restrictions: "%s", title: "%s", totalSlots: %d, startTime: "%s"}
                  ) {
                    result {
                      _id
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
                """.formatted(appointment.get_id(), appointment.getCancelled(), appointment.getDescription(),
                    appointment.getEndTime(),appointment.getLocation(), appointment.getRestrictions(),
                    appointment.getTitle(), appointment.getTotalSlots(), appointment.getStartTime());
        return vendiaClient.executeQuery(query, "update_Appointment.result", Appointment.class);
    }

    /**
     * @param appointment The appointment that is getting deleted
     * @return The {@link DeletionResponse} of the appointment getting deleted
     */
    public Mono<DeletionResponse> deleteAppointment(String id) {
        var deleteAppointmentMutation = """
                mutation {
                  remove_Appointment(id: "%s") {
                    transaction {
                      _id
                    }
                  }
                }""".formatted(id);
        return vendiaClient.executeQuery(deleteAppointmentMutation, "remove_Appointment.transaction" , DeletionResponse.class)
                .doOnError(l -> log.error("Something bad happened when executing mutation for deleting appointment, check syntax"));
    }

    /**
     *
     * This would be to help parse arrays into strings for the vendia queries.
     *
     * @param array Array that you want as a string
     * @return A string that can be used to up Vendia
     */
//    public String arrayToString(Set<String> array)
//    {
//        Integer max = array.size();
//        String output = "[";
//        for(int i = 0; i < max; i++)
//        {
//            //.forEach
//        }
//    }

}
