
package com.compilercharisma.chameleonbusinessstudio.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.compilercharisma.chameleonbusinessstudio.client.VendiaClient;
import com.compilercharisma.chameleonbusinessstudio.utils.VendiaField;
import com.compilercharisma.chameleonbusinessstudio.utils.VendiaQueryBuilder;
import com.compilercharisma.chameleonbusinessstudio.utils.VendiaSort;
import com.compilercharisma.chameleonbusinessstudio.dto.Appointment;
import com.compilercharisma.chameleonbusinessstudio.dto.AppointmentResponse;
import com.compilercharisma.chameleonbusinessstudio.dto.DeletionResponse;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
public class AppointmentRepository {
    private final VendiaClient vendiaClient;

    public AppointmentRepository(VendiaClient vendiaClient) {
        this.vendiaClient = vendiaClient;
    }

    /**
     * Get an appointment by their ID
     * @param id Appointment Id
     * @return Mono of {@link Appointment}
     */
    public Mono<Appointment> getAppointmentById(String id) {
        var query = "query get_Appointment { get_Appointment(id: \"%s\") { _id cancelled description endTime location participants startTime title totalSlots } }"
                .formatted(id);
        return vendiaClient.executeQuery(query, "get_Appointment", Appointment.class)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("No appointment with ID " + id)));
    }

    /**
     * Gets all appointments in Vendia
     *
     * @return {@link AppointmentResponse}
     */
    public Mono<AppointmentResponse> findAllAppointments() {
        var query = "query {list_AppointmentItems {_AppointmentItems {_id,cancelled,description,endTime,location,participants,startTime,title,totalSlots}}}";
        return vendiaClient.executeQuery(query, "list_AppointmentItems", AppointmentResponse.class);
    }

    /**
     * @param appointment the appointment that will be created
     * @return The {@link} of the appointment created
     */
    public Mono<Appointment> createAppointment(Appointment appointment) {
        var participantString = makeParticipantStringFor(appointment);
        var query = "mutation {add_Appointment(input: {cancelled: %s, endTime: \"%s\", description: \"%s\", location: \"%s\", participants: %s, startTime: \"%s\", title: \"%s\", totalSlots: %d}) {result {cancelled,description,endTime,location,participants,startTime,title,totalSlots}}}"
                .formatted(appointment.getCancelled(), appointment.getEndTime(),
                        appointment.getDescription(), appointment.getLocation(),
                        participantString, appointment.getStartTime(), 
                        appointment.getTitle(), appointment.getTotalSlots());
        return vendiaClient.executeQuery(query, "add_Appointment.result", Appointment.class);
    }

    /**
     * Retrieves the appointments for which the given user is a participant
     *
     * @param email the user's email
     * @param page  sorting options
     * @return a page containing the user's appointments
     */
    public Mono<Page<Appointment>> getAppointmentsForUser(String email, Pageable page) {
        var sorts = page.getSort()
                .map(order -> VendiaSort.by(order.getProperty(), order.isAscending()))
                .toList();

        var query = new VendiaQueryBuilder()
                .select("_id", "title", "startTime", "endTime", "location",
                        "description", "cancelled", "participants")
                .from("Appointment")
                .where(new VendiaField("participants").contains(email))
                .orderBy(sorts)
                .build();

        return vendiaClient.executeQuery(query, "list_AppointmentItems", AppointmentResponse.class)
                .map(AppointmentResponse::getAppointments)
                .map(appts -> toPage(appts, page));
    }

    public Mono<Page<Appointment>> getAvailableAppointments(LocalDate startTime, LocalDate endTime, Pageable page) {
        /*
         * var sorts = page.getSort()
         * .map(order -> VendiaSort.by(order.getProperty(), order.isAscending()))
         * .toList();
         */
        var start = startTime.toString();
        var end = endTime.toString();

        var query = new VendiaQueryBuilder()
                .select("_id", "title", "startTime", "endTime", "location",
                        "description", "cancelled", "participants")
                .from("Appointment")
                .where(
                        new VendiaField("startTime").ge(start),
                        new VendiaField("endTime").le(end))
                // .orderBy(sorts)
                .build();

        return vendiaClient.executeQuery(query, "list_AppointmentItems", AppointmentResponse.class)
                .map(AppointmentResponse::getAppointments)
                .flatMapMany(Flux::fromIterable)
                .filter(appt -> appt.getTotalSlots() > appt.getParticipants().size()) // can't do in Vendia?
                .collectList()
                .map(appts -> toPage(appts, page));
    }

    private Page<Appointment> toPage(List<Appointment> appts, Pageable pageable) {
        // https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/domain/PageImpl.html
        /*
         * we'll have to change the 3rd parameter if we ever split into multiple
         * pages, as that records the total number of appointments available.
         * For example, if a query returns 3 appointments, but only 2 are included
         * in appts due to the pageable, the 3rd parameter will need to be adjusted
         */
        var page = new PageImpl<Appointment>(appts, pageable, (long) appts.size());
        return page;
    }

    /**
     * @param appointment The appointment that is getting updated
     * @return The {@link} of the appointment getting updated
     */
    public Mono<Appointment> updateAppointment(Appointment appointment) {
        var participantString = makeParticipantStringFor(appointment);
        var query = "mutation {update_Appointment(id: \"%s\", input: {cancelled: %s, description: \"%s\", endTime: \"%s\", location: \"%s\", participants: %s, title: \"%s\", totalSlots: %d, startTime: \"%s\"} 		) {result {_id,cancelled,description,endTime,location,participants,startTime,title,totalSlots}}}"
                .formatted(appointment.get_id(), appointment.getCancelled(), appointment.getDescription(),
                        appointment.getEndTime(), appointment.getLocation(), participantString,
                        appointment.getTitle(), appointment.getTotalSlots(), appointment.getStartTime());
        return vendiaClient.executeQuery(query, "update_Appointment.result", Appointment.class)
                .doOnNext(u -> log.info("Appointment updated in Vendia share!"))
                .onErrorMap(e -> new Exception("Error updating appointment in Vendia"));
    }

    /**
     * @param id The appointment that is getting deleted
     * @return The {@link DeletionResponse} of the appointment getting deleted
     */
    public Mono<DeletionResponse> deleteAppointment(String id) {
        var deleteAppointmentMutation = "mutation {remove_Appointment(id: \"%s\") {transaction {_id}}}".formatted(id);
        return vendiaClient
                .executeQuery(deleteAppointmentMutation, "remove_Appointment.transaction", DeletionResponse.class)
                .doOnError(l -> log.error(
                        "Something bad happened when executing mutation for deleting appointment, check syntax"));
    }

    /**
     * New Endpoint to get a single appointment from Vendia
     *
     * @param _id The _id of the appointment you want to fully get
     * @return The full details of the appointment gets returned
     */
    public Mono<Appointment> getAppointment(String _id) {
        var query = """
                query { get_Appointment(id: "%s") {cancelled, description, endTime, location, participants, startTime, title, totalSlots}}"""
                .formatted(_id);
        return (vendiaClient.executeQuery(query, "get_Appointment", Appointment.class));

    }

    private String makeParticipantStringFor(Appointment appointment){
        return appointment.getParticipants().stream()
                .map(s -> String.format("\"%s\"", s))
                .collect(Collectors.joining(",", "[", "]"));
    }
}
