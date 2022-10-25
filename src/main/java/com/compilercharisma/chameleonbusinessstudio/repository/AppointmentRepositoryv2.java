package com.compilercharisma.chameleonbusinessstudio.repository;

import com.compilercharisma.chameleonbusinessstudio.client.VendiaClient;
import com.compilercharisma.chameleonbusinessstudio.client.VendiaField;
import com.compilercharisma.chameleonbusinessstudio.client.VendiaQueryBuilder;
import com.compilercharisma.chameleonbusinessstudio.client.VendiaSort;
import com.compilercharisma.chameleonbusinessstudio.dto.Appointment;
import com.compilercharisma.chameleonbusinessstudio.dto.AppointmentResponse;
import com.compilercharisma.chameleonbusinessstudio.dto.DeletionResponse;
import com.compilercharisma.chameleonbusinessstudio.dto.UserResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Mono;

@Slf4j
@Repository
public class AppointmentRepositoryv2
{
    private final VendiaClient vendiaClient;

    public AppointmentRepositoryv2(VendiaClient vendiaClient){
        this.vendiaClient = vendiaClient;
    }

    /**
     * Gets all appointments in Vendia
     *
     * @return {@link AppointmentResponse}
     */
    public Mono<AppointmentResponse> findAllAppointments() {
        var query = """
                  query {
                  list_AppointmentItems {
                    _AppointmentItems {
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
                }""";
        return vendiaClient.executeQuery(query, "list_AppointmentItems", AppointmentResponse.class);
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
     * retrieves the appointments for which the given user is a participant
     * 
     * @param email the user's email
     * @param page sorting options
     * @return a page containing the user's appointments
     */
    public Mono<Page<Appointment>> getAppointmentsForUser(String email, Pageable page){
      var sorts = page.getSort()
        .map(order -> VendiaSort.by(order.getProperty(), order.isAscending()))
        .toList();

      var query = new VendiaQueryBuilder()
        .select("_id", "title", 
          //"startTime", 
          //"endTime", 
          "location", "description", "cancelled", "participants")
        .from("Appointment")
        .where(new VendiaField("participants").contains(email))
        .orderBy(sorts)
        .build();
      
      return vendiaClient.executeQuery(query, "list_AppointmentItems", AppointmentResponse.class)
        .map(appts -> appts.getAppointments())
        .map(appts -> toPage(appts, page));
    }

    private Page<Appointment> toPage(List<Appointment> appts, Pageable pageable){
      // https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/domain/PageImpl.html
      /*
        we'll have to change the 3rd parameter if we ever split into multiple
        pages, as that records the total number of appointments available.
        For example, if a query returns 3 appointments, but only 2 are included
        in appts due to the pageable, the 3rd parameter will need to be adjusted
      */
      var page = new PageImpl<Appointment>(appts, pageable, (long)appts.size());
      return page;
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
     * @param id The appointment that is getting deleted
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
