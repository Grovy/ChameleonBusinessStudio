package com.compilercharisma.chameleonbusinessstudio.service

import com.compilercharisma.chameleonbusinessstudio.dto.Appointment
import com.compilercharisma.chameleonbusinessstudio.dto.AppointmentResponse
import com.compilercharisma.chameleonbusinessstudio.repository.AppointmentRepository
import reactor.core.publisher.Mono
import spock.lang.Specification

import java.time.LocalDateTime

class AppointmentServiceSpec extends Specification {

    def appointmentRepository = Mock(AppointmentRepository)
    def sut = new AppointmentServicev2(appointmentRepository)


    def "getting all appointments is successful"() {
        when: "getAllAppointments is called"
        def response = sut.getAllAppointments().block()

        then: "the response is correct"
        response

        and: "the expected interactions occur"
        1 * appointmentRepository.findAllAppointments() >> Mono.just(Void)
        0 * _
    }

    def "finding all appointments is successful"() {
        given: "appointments when getAllAppointments is called"
        def startTime = LocalDateTime.of(2022,10,5,5,30)
        def endTime = LocalDateTime.of(2022,10,5,6,0)

        def expected =
                new AppointmentResponse(appointments: [
                        new Appointment(_id: "5555555", cancelled : false, description: "Some description",
                                endTime: endTime, location: "Sacramento State", participants: [],
                                startTime: startTime, title: "Help", totalSlots: 1
                        )
                ])

        when: "getAllAppointments is called"
        def response = sut.getAllAppointments().block()

        then: "the response is correct"
        response
        response.appointments.size()==1
        response.appointments[0]._id == expected.appointments[0]._id
        response.appointments[0].endTime == expected.appointments[0].endTime
        response.appointments[0].startTime == expected.appointments[0].startTime
        response.appointments[0].participants.size()==0

        and: "the expected interactions occur"
        1 * appointmentRepository.findAllAppointments() >> Mono.just(expected)
        0 * _
    }



}
