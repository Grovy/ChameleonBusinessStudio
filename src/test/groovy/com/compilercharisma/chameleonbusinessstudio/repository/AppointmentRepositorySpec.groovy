package com.compilercharisma.chameleonbusinessstudio.repository

import com.compilercharisma.chameleonbusinessstudio.client.VendiaClient
import com.compilercharisma.chameleonbusinessstudio.dto.Appointment
import com.compilercharisma.chameleonbusinessstudio.dto.AppointmentResponse
import reactor.core.publisher.Mono
import spock.lang.Specification

import java.time.LocalDateTime

class AppointmentRepositorySpec extends Specification {

    def vendiaClient = Mock(VendiaClient)
    def sut = new AppointmentRepositoryv2(vendiaClient)

    def "finding all appointments is successful"() {
        when: "findAllAppointments is called"
        def response = sut.findAllAppointments().block()

        then: "the response is correct"
        response
        response.appointments.size() == 0

        and: "the expected interactions occur"
        1 * vendiaClient.executeQuery(_, "list_AppointmentItems", AppointmentResponse.class) >> Mono.just(new AppointmentResponse(appointments: []))
        0 * _
    }

    def "finding all appointments is successful"() {
        given: "some test data"
        def appointmentResponse = new AppointmentResponse(appointments: [
                new Appointment(_id: "1", title: "Appointment1", startTime: LocalDateTime.now(), endTime: LocalDateTime.now(),
                        location: "somewhere", restrictions: "none", cancelled: false, totalSlots: 2, participants: ["1", "2", "3"]),
                new Appointment(_id: "2", cancelled: false, description: "Some description",
                        endTime: LocalDateTime.now(), location: "Sacramento State", participants: ["2","1"],
                        restrictions: "none", startTime: LocalDateTime.now(), title: "Ap2", totalSlots: 1)
        ])

        when: "findAllAppointments is called"
        def response = sut.findAllAppointments().block()

        then: "the response is correct"
        response
        response.appointments.size() == 2
        with(response.appointments.find {it._id == appointmentResponse.appointments[0]._id }) {
            title == appointmentResponse.appointments[0].title
            startTime == appointmentResponse.appointments[0].startTime
            endTime == appointmentResponse.appointments[0].endTime
            location == appointmentResponse.appointments[0].location
            restrictions == appointmentResponse.appointments[0].restrictions
            cancelled == appointmentResponse.appointments[0].cancelled
            totalSlots == appointmentResponse.appointments[0].totalSlots
            participants.size() == 3
            participants.contains("1")
            participants.contains("2")
            participants.contains("3")
        }
        with(response.appointments.find {it._id == appointmentResponse.appointments[1]._id }) {
            title == appointmentResponse.appointments[1].title
            startTime == appointmentResponse.appointments[1].startTime
            endTime == appointmentResponse.appointments[1].endTime
            location == appointmentResponse.appointments[1].location
            restrictions == appointmentResponse.appointments[1].restrictions
            cancelled == appointmentResponse.appointments[1].cancelled
            totalSlots == appointmentResponse.appointments[1].totalSlots
            participants.size() == 2
            participants.contains("1")
            participants.contains("2")
        }

        and: "the expected interactions occur"
        1 * vendiaClient.executeQuery(_, "list_AppointmentItems", AppointmentResponse.class) >> Mono.just(appointmentResponse)
        0 * _
    }
}