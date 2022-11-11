package com.compilercharisma.chameleonbusinessstudio.service

import com.compilercharisma.chameleonbusinessstudio.repository.AppointmentRepository
import com.compilercharisma.chameleonbusinessstudio.validators.AppointmentValidator
import spock.lang.Specification

class AppointmentServiceSpec extends Specification {

    def appointmentRepository = Mock(AppointmentRepository)
    def userService = Mock(UserService)
    def appointmentValidator = Mock(AppointmentValidator)
    def sut = new AppointmentService(appointmentRepository, userService, appointmentValidator)

}
